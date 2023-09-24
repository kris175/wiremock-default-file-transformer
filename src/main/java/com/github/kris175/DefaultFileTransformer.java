package com.github.kris175;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.logging.Logger;

public class DefaultFileTransformer implements ResponseDefinitionTransformerV2 {

    private static final Logger LOGGER = Logger.getLogger(DefaultFileTransformer.class.getName());

    private String getFileName(String requestedBodyFileName, String requestBody) {
        String fieldValue = null;

        String prePaddingText = requestedBodyFileName.substring(0, requestedBodyFileName.indexOf("{"));
        String fileNameField = requestedBodyFileName.substring(requestedBodyFileName.indexOf("{") + 1, requestedBodyFileName.indexOf("}"));
        String postPaddingText = requestedBodyFileName.substring(requestedBodyFileName.indexOf("}") + 1);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(requestBody);
            fieldValue = jsonNode.get(fileNameField).textValue();
        } catch (Exception e) {
            LOGGER.info("Error");
        }
        return prePaddingText + fieldValue + postPaddingText;
    }

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        ResponseDefinition responseDefinition = serveEvent.getResponseDefinition();
        JsonNode jsonBody = responseDefinition.getJsonBody();

        String defaultFileName = jsonBody.get("defaultFileName").textValue();

        String requestedBodyFileName = jsonBody.get("requestBodyFileName").textValue();
        String fullFileName = getFileName(requestedBodyFileName, serveEvent.getRequest().getBodyAsString());

        // TODO: Iterate through all folders and check if file exists
        // TODO: Support multilevel jsonFieldMapping

        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withJsonBody(null)
                .withBodyFile(fullFileName)
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
