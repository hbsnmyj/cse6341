package ast;

import org.testng.annotations.Test;
import utils.TestUtils;
import java.io.ByteArrayOutputStream;
import static org.testng.Assert.assertEquals;

public class EvalInterpreterTest {
    @Test
    public void testSimple() throws Exception {
        testCase("NIL", "NIL\n");
        testCase("NIL    \n NIL", "NIL\nNIL\n");
        testCase("T", "T\n");
        testCase("F", "F\n");
        testCase("(QUOTE Q)", "Q\n");
        testCase("(QUOTE ((Q)))", "((Q))\n");
        testCase("(PLUS 2 (PLUS 2 3))(PLUS 2 3)", "7\n5\n");
        testCase("(EQ (QUOTE Q) (QUOTE Q))", "T\n");
        testCase("3 2", "3\n2\n");
        testCase("(NULL (QUOTE (NIL)))", "F\n");
    }

    @Test
    public void testError() throws Exception {
        testCaseStartsWith("(COND (NIL 1) (T 3 2))", "ERROR: ");
        testCaseStartsWith("(2)", "ERROR: ");
        testCaseStartsWith("(COND ((NULL 3) 4) ((QUOTE (1 2 3)) 7) ((NULL NIL) 8))", "ERROR: ");
    }

    private void testCaseStartsWith(String input, String output) throws Exception {
        TestUtils.RedirectStdinToString(input);
        ByteArrayOutputStream os = TestUtils.RedirectStdoutToString();
        EvalInterpreter.main(new String[]{});
        assert(os.toString().replaceAll("\r\n", "\n").startsWith(output));
    }

    private void testCase(String input, String output) throws Exception {
        TestUtils.RedirectStdinToString(input);
        ByteArrayOutputStream os = TestUtils.RedirectStdoutToString();
        EvalInterpreter.main(new String[]{});
        assertEquals(os.toString().replaceAll("\r\n", "\n"),output);
    }

}