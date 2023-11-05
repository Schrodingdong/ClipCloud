package com.serverless.save_lambda;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.InputValidator;
import com.serverless.Response;
import jdk.javadoc.internal.doclets.toolkit.taglets.snippet.Parser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private static final String TABLE_NAME = System.getenv("tableName");
	private final Regions  REGION = Regions.EU_WEST_2;
	private final Map<String,String> headers = new HashMap<>();
	private final InputValidator inputValidator = new InputValidator();
	private AmazonDynamoDB client;

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
		initDynamoDbClient();

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

	private void initDynamoDbClient() {
		this.client = AmazonDynamoDBClientBuilder
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
		client.putItem(TABLE_NAME, itemToSave);
		return new Response("TextClipBoardElementHandler", input);
	}

	private Response fileClipBoardElementHandler(Map<String, Object> input) {
		return new Response("FileClipBoardElementHandler", input);
	}

	private Response imageClipBoardElementHandler(Map<String, Object> input) {
		return new Response("ImageClipBoardElementHandler", input);
	}



}
