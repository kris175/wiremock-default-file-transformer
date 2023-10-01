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
    public static String getFileName(String requestedBodyFileName, JsonNode requestBody) throws InvalidAttributeValueException {
        // TODO: Enable multilevel json field search for multiple instances
        JsonNode jsonFieldNode = null;

        String prePaddingText = requestedBodyFileName.substring(0, requestedBodyFileName.indexOf(PRE_BOUNDARY_CHAR));
        String postPaddingText = requestedBodyFileName.substring(requestedBodyFileName.indexOf(POST_BOUNDARY_CHAR) + 1);
        String fileNameField = requestedBodyFileName.substring(requestedBodyFileName.indexOf(PRE_BOUNDARY_CHAR) + 1, requestedBodyFileName.indexOf(POST_BOUNDARY_CHAR));

        jsonFieldNode = requestBody.findValue(fileNameField);

        if (jsonFieldNode == null) {
            throw new InvalidAttributeValueException("Field value does not exists in Json Body");
        } else {
            String fieldValue = jsonFieldNode.textValue();
            return prePaddingText + fieldValue + postPaddingText;
        }
    }

    public static Optional<String> getFullFilePath(String fileName){
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
