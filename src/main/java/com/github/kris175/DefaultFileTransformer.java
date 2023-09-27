package com.github.kris175;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kris175.utils.CommonUtils;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.logging.Logger;

public class DefaultFileTransformer implements ResponseDefinitionTransformerV2 {

    private static final String DEFAULT_FILE_NAME_KEY = "defaultFileName";
    private static final String REQUESTED_FILE_NAME_KEY = "requestBodyFileName";

    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        ResponseDefinition responseDefinition = serveEvent.getResponseDefinition();
        JsonNode jsonBody = responseDefinition.getJsonBody();

        /*
          Check if requested body file name is present and has value
          If yes:
            - Iterate through files to check if the file exists
            - If found return that file
            - else proceed to the parent else case
           else:
            - check if default file name is present and has value
            - If yes:
                - Iterate through files to check if the file exists
                - If found return that file
                - else proceed to the parent else case
            - else:
                - return with body "Requested file could not be found. Default response could not be served as either the file does not exist or path is not provided"

         */

        if (jsonBody.has(REQUESTED_FILE_NAME_KEY) && !jsonBody.get(REQUESTED_FILE_NAME_KEY).textValue().isEmpty()){
            
        } else if (jsonBody.has(DEFAULT_FILE_NAME_KEY) && !jsonBody.get(DEFAULT_FILE_NAME_KEY).textValue().isEmpty()){

        }

        return ResponseDefinitionBuilder
                .like(responseDefinition)
                .but()
                .withJsonBody(null)
                .withBody("Requested file could not be found. Default response could not be served as either the file does not exist or path is not provided.")
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
