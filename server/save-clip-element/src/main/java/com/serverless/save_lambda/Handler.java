package com.serverless.save_lambda;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.InputValidator;
import com.serverless.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private static final String TABLE_NAME = System.getenv("tableName");
	private static final String BUCKET_NAME = System.getenv("bucketName");
	private final Regions  REGION = Regions.EU_WEST_2;
	private final Map<String,String> headers = new HashMap<>();
	private final InputValidator inputValidator = new InputValidator();
	private AmazonDynamoDB amazonDynamoDbClient;
	private AmazonS3 amazonS3Client;
	private Map<String, AttributeValue> itemToSave = new HashMap<>();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> apiInput, Context context) {
		// extract request body (don't forget to parse the string to a map
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

		// init methods
		initHeaders();
		initSdkClients();

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
				if (!inputValidator.validateTextInputFields(input)) {
					responseBody = new Response("Invalid input", input);
					return ApiGatewayResponse.builder()
							.setStatusCode(400)
							.setObjectBody(responseBody)
							.setHeaders(headers)
							.build();
				}
				responseBody = textClipBoardElementHandler(input);
				break;
			case "FILE":
				if (!inputValidator.validateFileInputFields(input)) {
					responseBody = new Response("Invalid input", input);
					return ApiGatewayResponse.builder()
							.setStatusCode(400)
							.setObjectBody(responseBody)
							.setHeaders(headers)
							.build();
				}
				responseBody = fileClipBoardElementHandler(input);
				break;
			case "IMAGE":
				if (!inputValidator.validateImageInputFields(input)) {
					responseBody = new Response("Invalid input", input);
					return ApiGatewayResponse.builder()
							.setStatusCode(400)
							.setObjectBody(responseBody)
							.setHeaders(headers)
							.build();
				}
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
		this.amazonDynamoDbClient = AmazonDynamoDBClientBuilder
				.standard()
				.withRegion(REGION)
				.build();
		this.amazonS3Client = AmazonS3ClientBuilder
				.standard()
				.withRegion(REGION)
				.build();
	}

	// TODO : implement handlers for each type(make a factory design pattern here)
	private Response textClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		Map<String, AttributeValue> itemToSave = new HashMap<>();
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(key, new AttributeValue(input.get(key).toString()));
		}

		// save it
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(TABLE_NAME, itemToSave);
		LOG.info(">>> Saved to dynamodb");
		return new Response("TextClipBoardElementHandler", input);
	}

	private Response fileClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(key, new AttributeValue(input.get(key).toString()));
		}

		// get extension
		String extension = input.get("tmpPath").toString().substring(
				input.get("tmpPath").toString().lastIndexOf('.')+1
		);
		LOG.info(">>> object extensiosn :" + extension);


		// save it TO DYNAMODB =======================================================
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(TABLE_NAME, itemToSave);
		LOG.info(">>> Saved to dynamodb");

		// save it to S3 =============================================================
		// get base64 content
		byte[] contentBase64 = Base64.getDecoder().decode(
				input.get("contentBase64").toString()
		);
		LOG.info(">>> Got the base64 content");
		// format it & add metadata
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBase64);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBase64.length);
		metadata.setContentType("image/"+extension);
		metadata.addUserMetadata("author",input.get("userName").toString());
		metadata.addUserMetadata("created",input.get("created").toString());
		// save to bucket
		amazonS3Client.putObject(//TODO make a standalone function for S3 uploads ???
				BUCKET_NAME,
				input.get("uuid").toString() + "." + extension,
				byteArrayInputStream,
				metadata
		);
		LOG.info(">>> saved to S3");
		return new Response("FileClipBoardElementHandler", input);
	}

	private Response imageClipBoardElementHandler(Map<String, Object> input) {
		// Create the object to save
		for(String key : InputValidator.INPUT_ESSENTIAL_FIELDS){
			itemToSave.put(key, new AttributeValue(input.get(key).toString()));
		}

		// get extension
		String extension = input.get("tmpPath").toString().substring(
				input.get("tmpPath").toString().lastIndexOf('.')+1
		);
		LOG.info(">>> object extensiosn :" + extension);


		// save it TO DYNAMODB =======================================================
		LOG.info(">>> item to save to dynamo : "+ itemToSave);
		amazonDynamoDbClient.putItem(TABLE_NAME, itemToSave);
		LOG.info(">>> Saved to dynamodb");

		// save it to S3 =============================================================
		// get base64 content
		byte[] contentBase64 = Base64.getDecoder().decode(
				input.get("contentBase64").toString()
		);
		LOG.info(">>> Got the base64 content");
		// format it & add metadata
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(contentBase64);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(contentBase64.length);
		metadata.setContentType("image/"+extension);
		metadata.addUserMetadata("author",input.get("userName").toString());
		metadata.addUserMetadata("created",input.get("created").toString());
		// save to bucket
		amazonS3Client.putObject(//TODO make a standalone function for S3 uploads ???
				BUCKET_NAME,
				input.get("uuid").toString() + "." + extension,
				byteArrayInputStream,
				metadata
		);
		LOG.info(">>> saved to S3");

		return new Response("ImageClipBoardElementHandler", input);
	}

}
