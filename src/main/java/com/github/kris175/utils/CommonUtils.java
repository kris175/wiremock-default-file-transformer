package com.github.kris175.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kris175.DefaultFileTransformer;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

public class CommonUtils {
    private static final Logger LOGGER = Logger.getLogger(DefaultFileTransformer.class.getName());
    private static final String ROOT = "__files/";
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
