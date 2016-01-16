import org.testng.annotations.Test;
import utils.TestUtils;

public class InterpreterTest {

    @Test
    public void testMain() throws Exception {
        TestUtils.RedirectStdin("test_resources/scanner/OfficialTest1");
        TestUtils.RedirectStdout("OfficialTest1_Result");
        Interpreter.main(new String[]{});
        TestUtils.resetAllStd();
    }
}