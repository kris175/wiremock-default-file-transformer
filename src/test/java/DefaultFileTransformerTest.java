import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.kris175.DefaultFileTransformer;
import com.github.kris175.utils.Constants;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultFileTransformerTest {

    @Mock
    private ServeEvent serveEvent;
    @Mock
    private LoggedRequest loggedRequest;
    @InjectMocks
    private DefaultFileTransformer defaultFileTransformer;

    @Test
    public void defaultFileTransformer_serveRequestedFile(){
        ObjectNode inputJsonBody = JsonNodeFactory.instance.objectNode();
        inputJsonBody.put("requestBodyFileName", "{fileName}.json");
        inputJsonBody.put("defaultFileName","default.json");

        ResponseDefinition responseDefinition = new ResponseDefinitionBuilder()
                .withJsonBody(inputJsonBody)
                .withStatus(200)
                .build();

        String requestBody = "{\n" +
                "    \"fileName\": \"helloWorld\"\n" +
                "}";

        when(serveEvent.getResponseDefinition()).thenReturn(responseDefinition);
        when(serveEvent.getRequest()).thenReturn(loggedRequest);
        when(loggedRequest.getBodyAsString()).thenReturn(requestBody);

        ResponseDefinition response = defaultFileTransformer.transform(serveEvent);
        assertEquals("helloWorld.json",response.getBodyFileName());
    }

    @Test
    public void defaultFileTransformer_serveDefaultFile_whenRequestedFileDoNotExist(){
        ObjectNode inputJsonBody = JsonNodeFactory.instance.objectNode();
        inputJsonBody.put("requestBodyFileName", "{fileName}.json");
        inputJsonBody.put("defaultFileName","default.json");

        ResponseDefinition responseDefinition = new ResponseDefinitionBuilder()
                .withJsonBody(inputJsonBody)
                .withStatus(200)
                .build();

        String requestBody = "{\n" +
                "    \"fileName\": \"helloEarth\"\n" +
                "}";

        when(serveEvent.getResponseDefinition()).thenReturn(responseDefinition);
        when(serveEvent.getRequest()).thenReturn(loggedRequest);
        when(loggedRequest.getBodyAsString()).thenReturn(requestBody);

        ResponseDefinition response = defaultFileTransformer.transform(serveEvent);
        assertEquals("default.json",response.getBodyFileName());
    }

    @Test
    public void defaultFileTransformer_serveNoFileFound_ifJsonBodyIsCorrupt(){
        ObjectNode inputJsonBody = JsonNodeFactory.instance.objectNode();
        inputJsonBody.put("xyz", "{fileName}.json");
        inputJsonBody.put("abc","default.json");

        ResponseDefinition responseDefinition = new ResponseDefinitionBuilder()
                .withJsonBody(inputJsonBody)
                .withStatus(200)
                .build();

        String requestBody = "{\n" +
                "    \"fileName\": \"helloEarth\"\n" +
                "}";

        when(serveEvent.getResponseDefinition()).thenReturn(responseDefinition);
        when(serveEvent.getRequest()).thenReturn(loggedRequest);
        when(loggedRequest.getBodyAsString()).thenReturn(requestBody);

        ResponseDefinition response = defaultFileTransformer.transform(serveEvent);
        assertEquals(Constants.NO_FILE_FOUND_MESSAGE,response.getBody());
    }
}
