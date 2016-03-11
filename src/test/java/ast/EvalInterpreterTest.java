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
        testCase("(DEFUN GETI () 5)(GETI)", "GETI\n5\n");
        testCase("(DEFUN GETI () 5)", "GETI\n");
    }

    @Test
    public void testDefunConflictName() throws Exception {
        testCaseStartsWith("(DEFUN PLUS (X) 5)", "ERROR: ");
    }

    @Test
    public void testDefunPlus() throws Exception {
        testCase("(DEFUN PLUS5(X) (PLUS X 5))\n(PLUS5 5)", "PLUS5\n10\n");
        testCase("(DEFUN PLUSN(X Y) (PLUS X Y))\n(PLUSN 5 5)", "PLUSN\n10\n");
    }

    @Test
    public void testDefunList() throws Exception {
        testCase("(DEFUN PREPEND(X AS) (CONS AS X))\n(PREPEND (QUOTE (2 3)) 5)", "PREPEND\n(5 2 3)\n");
        testCase("(DEFUN PREPEND(X AS) (CONS AS X))\n(PREPEND (QUOTE (2 3 4)) 5)", "PREPEND\n(5 2 3 4)\n");
        testCase("(DEFUN PREPEND(X AS) (CONS AS X))\n(PREPEND (QUOTE (2 3 (4))) 5)", "PREPEND\n(5 2 3 (4))\n");
    }

    @Test
    public void testDefunListAppend() throws Exception {
        String appendDef = "(DEFUN APPEND (X AS) (COND ((NULL AS) (CONS X NIL)) (T (CONS (CAR AS) (APPEND X (CDR AS))))))";
        testCase(appendDef + "\n(APPEND 5 (QUOTE (2 3)))", "APPEND\n(2 3 5)\n");
    }

    @Test
    public void testSum() throws Exception {
        String sumDef = "(DEFUN SUM (AS) (COND ((NULL AS) 0) (T (PLUS (CAR AS) (SUM (CDR AS))))))";
        testCase(sumDef + "\n(SUM (QUOTE (2 3 5)))", "SUM\n10\n");
        testCase(sumDef + "\n(SUM (QUOTE (2 3 5 7)))", "SUM\n17\n");
    }

    @Test
    public void testDefunNotCorrectLength() throws Exception {
        testCaseStartsWith("(DEFUN X Y Z)", "ERROR: ");
    }

    @Test
    public void testDefunFIsDefined() throws Exception {
        testCaseStartsWith("(DEFUN PLUS (X) X)", "ERROR: ");
    }

    @Test
    public void testDefunS1NotList() throws Exception {
        testCaseStartsWith("(DEFUN PLUS X X)", "ERROR: ");
    }

    @Test
    public void testDefunS1NotLiteralList() throws Exception {
        testCaseStartsWith("(DEFUN PLUS (X 3) X)", "ERROR: ");
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