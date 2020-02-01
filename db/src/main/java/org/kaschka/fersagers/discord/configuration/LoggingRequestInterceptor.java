package org.kaschka.fersagers.discord.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        String requestBody = new String(body);
        String logMessage = "Sent " + httpRequest.getMethod() + " " + httpRequest.getURI() + " body: " + requestBody;

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);

        logMessage += "\nresponse HttpStatus: " + response.getStatusCode() + " " + response.getStatusText();
        String responseBody = new BufferedReader(new InputStreamReader(response.getBody())).lines().collect(Collectors.joining("\n"));
        logger.info(String.format("%s %s", logMessage, responseBody));
        return response;
    }
}
