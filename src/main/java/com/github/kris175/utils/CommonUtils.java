package com.github.kris175.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kris175.DefaultFileTransformer;

import java.util.logging.Logger;

public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(DefaultFileTransformer.class.getName());
    public static String getFileName(String requestedBodyFileName, String requestBody) {
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
}
