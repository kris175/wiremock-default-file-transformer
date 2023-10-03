package com.github.kris175.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kris175.DefaultFileTransformer;
import org.apache.commons.io.FileUtils;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(DefaultFileTransformer.class.getName());
    private static final String ROOT = "__files/";
    private static final String PRE_BOUNDARY_CHAR = "{";
    private static final String POST_BOUNDARY_CHAR = "}";
    public static String getFileName(String jsonPathToFileName, String requestBody) throws InvalidAttributeValueException {
        // TODO: Enable multilevel json field search for multiple instances

        String prePaddingText = jsonPathToFileName.substring(0, jsonPathToFileName.indexOf(PRE_BOUNDARY_CHAR));
        String postPaddingText = jsonPathToFileName.substring(jsonPathToFileName.indexOf(POST_BOUNDARY_CHAR) + 1);
        String fileNameField = jsonPathToFileName.substring(jsonPathToFileName.indexOf(PRE_BOUNDARY_CHAR) + 1, jsonPathToFileName.indexOf(POST_BOUNDARY_CHAR));

        JsonNode jsonFieldNode = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(requestBody);
            jsonFieldNode = jsonNode.findValue(fileNameField);
        } catch (Exception e) {
            LOGGER.info("Error, field not found in body");
        }

        if (jsonFieldNode == null) {
            throw new InvalidAttributeValueException("Field value does not exists in Json Body");
        } else {
            String fieldValue = jsonFieldNode.textValue();
            return prePaddingText + fieldValue + postPaddingText;
        }
    }

    public static Optional<String> getFullFilePathOf(String fileName){
        String absolutePath = null;
        try {
            boolean recursive = true;

            Collection<File> files = FileUtils.listFiles(new File(ROOT), null, recursive);
            for (File file : files) {
                if (file.getName().equals(fileName))
                    absolutePath = file.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(absolutePath);
    }
}
