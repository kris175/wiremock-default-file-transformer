import com.github.kris175.utils.CommonUtils;
import org.junit.jupiter.api.Test;

import javax.management.InvalidAttributeValueException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonUtilsTest {
    @Test
    public void getFullFilePath_mustReturnCorrect() {
        Optional<String> absolutePath = CommonUtils.getFullFilePathOf("helloEarth.txt");
        assertEquals("data/sampleResponses/helloEarth.txt", absolutePath.orElse(null));
    }

    @Test
    public void getFileNameTest() throws InvalidAttributeValueException {
        String requestedBodyFileName = "{l4}.txt";
        String requestBody = """
                {
                    "l1":{"l4":"helloWorld"}
                }
                """;
        assertEquals("helloWorld.txt", CommonUtils.getFileName(requestedBodyFileName, requestBody));
    }
}
