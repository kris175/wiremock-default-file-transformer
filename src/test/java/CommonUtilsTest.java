import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kris175.utils.CommonUtils;
import org.junit.jupiter.api.Test;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonUtilsTest {
    @Test
    public void getFullFilePath_mustReturnCorrect() {
        Optional<String> absolutePath = CommonUtils.getFullFilePath("helloEarth.txt");
        assertEquals("data/sampleResponses/helloEarth.txt", absolutePath.orElse(null));
    }

    @Test
    public void getFileNameTest() throws JsonProcessingException, InvalidAttributeValueException {
        String requestedBodyFileName = "{l4}.txt";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestBody = mapper.readTree("""
                    {
                        "l1":{"l4":"helloWorld"}
                    }
                    """);
        assertEquals("helloWorld.txt", CommonUtils.getFileName(requestedBodyFileName, requestBody));
    }
}
