package com.serverless.save_lambda;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private static final String TABLE_NAME = System.getenv("tableName");
	private static final String BUCKET_NAME = System.getenv("bucketName");
	private final Region REGION = Region.EU_WEST_2;
	private final Map<String,String> headers = new HashMap<>();
	private final InputValidator inputValidator = new InputValidator();
	private DynamoDbClient amazonDynamoDbClient;
	private S3Client amazonS3Client;
	private Map<String, AttributeValue> itemToSave = new HashMap<>();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> apiInput, Context context) {
        // init methods
        initHeaders();
        initSdkClients();

		// extract request body (don't forget to parse the string to a map)
		Map<String, Object> input = null;
		try{
			String sInput = (String) apiInput.get("body");
			input =  new ObjectMapper().readValue(sInput, Map.class);
		} catch (Exception e) {
			Response responseBody = new Response(e.getMessage(), input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(headers)
					.build();
		}



		// logging info
		LOG.info(">>> received: \n{}", input);


		// check input validity
		if(!inputValidator.validateEssentialInputFields(input)) {
			Response responseBody = new Response("Invalid input", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(400)
					.setObjectBody(responseBody)
					.setHeaders(headers)
					.build();
		}

		// process input
		Response responseBody;
		switch (input.get("type").toString().toUpperCase()){
			case "TEXT":
				responseBody = textClipBoardElementHandler(input);
				break;
			case "FILE":
				responseBody = fileClipBoardElementHandler(input);
				break;
			case "IMAGE":
				responseBody = imageClipBoardElementHandler(input);
				break;
			default:
				responseBody = new Response("Unknown type", input);
				return ApiGatewayResponse.builder()
						.setStatusCode(400)
						.setObjectBody(responseBody)
						.setHeaders(headers)
						.build();
		}
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(headers)
				.build();
	}

	private void initHeaders(){
		headers.put("Content-Type", "application/json");
		headers.put("Access-Control-Allow-Origin", "*");
		headers.put("Access-Control-Allow-Methods", "*");
	}

	private void initSdkClients() {
		this.amazonDynamoDbClient = DynamoDbClient.builder()
				.region(REGION)
				.build();
		this.amazonS3Client = S3Client.builder()
				.region(REGION)
				.build();
	}

	// TODO : implement handlers for each type(make a factory design pattern here)
	private Response textClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(
					key,
					AttributeValue.builder().s(input.get(key).toString()).build()
			);
		}
		PutItemRequest putItemRequest = PutItemRequest.builder()
				.tableName(TABLE_NAME)
				.item(itemToSave)
				.build();

		// save it
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(putItemRequest);
		LOG.info(">>> Saved to dynamodb");
		return new Response("TextClipBoardElementHandler", input);
	}

	private Response fileClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(
					key,
					AttributeValue.builder().s(input.get(key).toString()).build()
			);
		}
		PutItemRequest putItemRequest = PutItemRequest.builder()
				.tableName(TABLE_NAME)
				.item(itemToSave)
				.build();

		// get extension
		String extension = input.get("filename").toString().substring(
				input.get("filename").toString().lastIndexOf('.')+1
		);
		LOG.info(">>> object extensiosn :" + extension);

		// save it TO DYNAMODB =======================================================
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(putItemRequest);
		LOG.info(">>> Saved to dynamodb");

		// save it to S3 =============================================================
		// get base64 content
		byte[] contentBase64 = Base64.getDecoder().decode(
				input.get("contentBase64").toString()
		);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBase64);
		LOG.info(">>> Got the base64 content");
		// format it & add metadata
		Map<String, String> userMetadata = new HashMap<>();
		userMetadata.put("author",input.get("userName").toString());
		userMetadata.put("created",input.get("created").toString());
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(BUCKET_NAME)
				.key(input.get("filename").toString())
				.contentLength((long) contentBase64.length)
				.contentType("image/"+extension)
				.metadata(userMetadata)
				.build();

		// save to bucket
		amazonS3Client.putObject(//TODO make a standalone function for S3 uploads ???
				putObjectRequest,
				RequestBody.fromInputStream(byteArrayInputStream, contentBase64.length)
		);
		LOG.info(">>> saved to S3");
		return new Response("FileClipBoardElementHandler", input);
	}

	private Response imageClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(
					key,
					AttributeValue.builder().s(input.get(key).toString()).build()
			);
		}
		PutItemRequest putItemRequest = PutItemRequest.builder()
				.tableName(TABLE_NAME)
				.item(itemToSave)
				.build();

		// get extension
		String extension = input.get("filename").toString().substring(
				input.get("filename").toString().lastIndexOf('.')+1
		);
		LOG.info(">>> object extensiosn :" + extension);


		// save it TO DYNAMODB =======================================================
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(putItemRequest);
		LOG.info(">>> Saved to dynamodb");

		// save it to S3 =============================================================
		// get base64 content
		byte[] contentBase64 = Base64.getDecoder().decode(
				input.get("contentBase64").toString()
		);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBase64);
		LOG.info(">>> Got the base64 content");
		// format it & add metadata
		Map<String, String> userMetadata = new HashMap<>();
		userMetadata.put("author",input.get("userName").toString());
		userMetadata.put("created",input.get("created").toString());
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
				.bucket(BUCKET_NAME)
				.key(input.get("filename").toString())
				.contentLength((long) contentBase64.length)
				.contentType("image/"+extension)
				.metadata(userMetadata)
				.build();

		// save to bucket
		amazonS3Client.putObject(//TODO make a standalone function for S3 uploads ???
				putObjectRequest,
				RequestBody.fromInputStream(byteArrayInputStream, contentBase64.length)
		);
		LOG.info(">>> saved to S3");

		return new Response("ImageClipBoardElementHandler", input);
	}

}
