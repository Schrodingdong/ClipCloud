package com.serverless;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);
	private final Map<String,String> headers = new HashMap<>();
	private final InputValidator inputValidator = new InputValidator();

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		// logging info
		LOG.info("received: {}", input);

		// init headers
		initHeaders();

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


	// TODO : implement handlers for each type

	private Response textClipBoardElementHandler(Map<String, Object> input) {
		return new Response("TextClipBoardElementHandler", input);
	}

	private Response fileClipBoardElementHandler(Map<String, Object> input) {
		return new Response("FileClipBoardElementHandler", input);
	}

	private Response imageClipBoardElementHandler(Map<String, Object> input) {
		return new Response("ImageClipBoardElementHandler", input);
	}


}
