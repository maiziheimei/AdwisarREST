package de.appsist.service.middrv.rest.server;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.logging.impl.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class WebRequestHandler implements Handler<HttpServerRequest>{
	private Map<String, de.appsist.service.middrv.rest.server.ContentParser> parsers;
	private Logger logger = LoggerFactory.getLogger(WebRequestHandler.class);

	public WebRequestHandler(){
		parsers = new HashMap<>();
	}

	public void putParser(String method, String path, de.appsist.service.middrv.rest.server.ContentParser parser){
		parsers.put(method + path, parser);
	}

	@Override
	public void handle(HttpServerRequest request) {
		HttpServerResponse response = request.response();
		logger.info("Received request for " + request.method() + request.path());
		de.appsist.service.middrv.rest.server.ContentParser parser = parsers.get(request.method() + request.path());


		if (parser == null){
			response.setStatusCode(404);
			String body="No handler for the requested path with the requested method.\nmethod:" + request.method() + "\npath:" + request.path() + "\n\nAvailable handlers:\n";
			for (String handler:parsers.keySet()){
				body +=handler + "\n";
			}
			response.headers().add("errorMessage", body);
			response.end(body);
			logger.error("A request was sent to REST API which could not be handled!");
		} else {
			de.appsist.service.middrv.rest.server.ExceptionHandlerForContentParser handler = new de.appsist.service.middrv.rest.server.ExceptionHandlerForContentParser(request, response, parser);
			request.bodyHandler(handler);
		}
	}
}