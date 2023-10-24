package com.github.kris175;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kris175.utils.CommonUtils;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import lombok.extern.slf4j.Slf4j;

import javax.management.InvalidAttributeValueException;
import java.util.Optional;

import static com.github.kris175.utils.Constants.*;

@Slf4j
public class DefaultFileTransformer implements ResponseDefinitionTransformerV2 {

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        ResponseDefinition responseDefinition = serveEvent.getResponseDefinition();
        JsonNode inputJsonBody = responseDefinition.getJsonBody();
        String requestBody = serveEvent.getRequest().getBodyAsString();

        if (inputJsonBody.hasNonNull(REQUESTED_BODY_FILE_NAME)) {
            String pathToFileName = inputJsonBody.get(REQUESTED_BODY_FILE_NAME).textValue();
            try {
                String fileName = CommonUtils.getFileName(pathToFileName, requestBody);
                Optional<String> requestedBodyFilePath = CommonUtils.getFullFilePathOf(fileName);
                if (requestedBodyFilePath.isPresent()) {
                    return getResponseWithBodyFileName(requestedBodyFilePath.get(), responseDefinition);
                }
            } catch (InvalidAttributeValueException e) {
                log.error(e.getMessage());
            }
        }

        return serveDefaultResponse(inputJsonBody, responseDefinition);
    }

    private ResponseDefinition serveDefaultResponse(JsonNode jsonBody, ResponseDefinition responseDefinition) {
        if (jsonBody.hasNonNull(DEFAULT_FILE_NAME)) {
            String defaultResponseFileName = jsonBody.get(DEFAULT_FILE_NAME).textValue();
            Optional<String> defaultResponseFilePath = CommonUtils.getFullFilePathOf(defaultResponseFileName);
            if (defaultResponseFilePath.isPresent()) {
                return getResponseWithBodyFileName(defaultResponseFilePath.get(), responseDefinition);
            }
        }
        return getNoFileFoundResponse(responseDefinition);
    }

    private ResponseDefinition getResponseWithBodyFileName(String fullFilePath, ResponseDefinition responseDefinition) {
        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withJsonBody(null)
                .withBodyFile(fullFilePath)
                .build();
    }

    private ResponseDefinition getNoFileFoundResponse(ResponseDefinition responseDefinition) {
        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withJsonBody(null)
                .withBody(NO_FILE_FOUND_MESSAGE)
                .build();
    }

    @Override
    public String getName() {
        return "default-file-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }
}
