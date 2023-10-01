import com.github.kris175.utils.CommonUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommonUtilsTest {
    @Test
    public void getFullFilePath_mustReturnCorrect() {
        Optional<String> absolutePath = CommonUtils.getFullFilePath("helloEarth.txt");
        assertEquals("data/sampleResponses/helloEarth.txt", absolutePath.orElse(null));
    }
}
