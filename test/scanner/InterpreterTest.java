package scanner;

import org.testng.annotations.Test;
import scanner.Interpreter;
import utils.TestUtils;

public class InterpreterTest {

    @Test
    public void testMain() throws Exception {
        testCase("test_resources/scanner/OfficialTest1", "OfficialTest1Result", "test_resources/scanner/OfficialTest1Result");
        testCase("test_resources/scanner/OfficialTest2", "OfficialTest2Result", "test_resources/scanner/OfficialTest2Result");
        testCase("test_resources/scanner/OfficialTest3", "OfficialTest3Result", "test_resources/scanner/OfficialTest3Result");
    }

    private void testCase(String input, String output, String standard) throws Exception {
         TestUtils.RedirectStdin(input);
        TestUtils.RedirectStdout(output);
        Interpreter.main(new String[]{});
        TestUtils.resetAllStd();
        TestUtils.compareFile(standard, output);
    }
}