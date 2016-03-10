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
        testCase("(QUOTE Q)", "Q\n");
        testCase("(QUOTE ((Q)))", "((Q))\n");
        testCase("(PLUS 2 (PLUS 2 3))(PLUS 2 3)", "7\n5\n");
        testCase("(EQ (QUOTE Q) (QUOTE Q))", "T\n");
        testCase("3 2", "3\n2\n");
        testCase("(NULL (QUOTE (NIL)))", "NIL\n");
        testCase("(COND ((NULL 3) 4) ((QUOTE (1 2 3)) 7) ((NULL NIL) 8))", "7\n");
    }

    @Test
    public void testDefunSimple() throws Exception {
        testCase("(DEFUN GETI(X) 5)(GETI)", "GETI\n5\n");
        testCase("(DEFUN GETI(X) 5)", "GETI\n5\n");
    }

    @Test
    public void testDefunConflictName() throws Exception {
        testCaseStartsWith("(DEFUN PLUS (X) 5)", "ERROR: ");
    }

    @Test
    public void testDefunPlus() throws Exception {
        testCase("(DEFUN PLUS5(X) (+ X 5))\n(PLUS5 5)", "PLUS5\n10\n");
        testCase("(DEFUN PLUSN(X Y) (+ X Y))\n(PLUSN 5 5)", "PLUSN\n10\n");
    }

    @Test
    public void testDefunList() throws Exception {
        testCase("(DEFUN PREPEND(X AS) (CONS AS X))\n(PREPEND (QUOTE (2 3)) 5)", "PREPEND\n(5 3 2)\n");
    }

    public void testDefunListAppend() throws Exception {
        String appendDef = "(DEFUN APPEND (X AS) (COND ((EQ AS NIL) (CONS X NIL)) (T (CONS (CAR AS) (APPEND X (CDR AS))))))";
        testCase(appendDef + "\n(APPEND (QUOTE (2 3)) 5)", "APPEND\n(5 3 2)\n");
    }

    @Test
    public void testError() throws Exception {
        testCaseStartsWith("(COND (NIL 1) (T 3 2))", "ERROR: ");
        testCaseStartsWith("(2)", "ERROR: ");
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