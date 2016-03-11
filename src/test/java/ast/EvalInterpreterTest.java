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
    public void testDefunQuoteUsage() throws Exception {
        testCase("(DEFUN QUOTEVAR (X)\n" +
                "  (QUOTE (X 2 3))\n" +
                ")\n" +
                "(QUOTEVAR 1)", "QUOTEVAR\n(X 2 3)\n");
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
    public void testDefunS1FunctionNameBinding() throws Exception {
        testCaseStartsWith("(DEFUN F (X) (X NIL))\n(F NULL)", "F\nERROR:");
    }

    @Test
    public void testMultipleDefinition() throws Exception {
        testCase("(DEFUN DIFF (X Y) (COND ( (EQ X Y) NIL ) (T T) ) )  \n" +
                "\n" +
                "(DIFF 5 6)\n" +
                "\n" +
                "(DEFUN MEM (X LIST) (COND ( (NULL LIST) NIL ) ( T (COND ( (EQ X (CAR LIST))T ) ( T (MEM X (CDR LIST)))))))\n" +
                "\n" +
                "(MEM 3 (QUOTE (2 3 4)))\n", "DIFF\nT\nMEM\nT\n");
        testCase("(DEFUN DIFF (X Y) (COND ( (EQ X Y) NIL ) (T T) ) )  \n" +
                "\n" +
                "(DIFF 5 6)\n" +
                "\n" +
                "(DEFUN MEM (X LIST) (COND ( (NULL LIST) NIL ) ( T (COND ( (EQ X (CAR LIST))T ) ( T (MEM X (CDR LIST)))))))\n" +
                "\n" +
                "(MEM 3 (QUOTE (2 3 4)))\n" + "(DEFUN UNI (S1 S2)\n" +
                "(COND ( (NULL S1)S2)\n" +
                "( (NULL S2)S1)\n" +
                "( T (COND\n" +
                "( (MEM (CAR S1) S2)\n" +
                "(UNI (CDR S1) S2))\n" +
                "( T (CONS\n" +
                "(CAR S1) (UNI (CDR S1) S2)))))\n" +
                "))" + "(UNI (QUOTE (2 3 4)) (QUOTE (3 4 5)))", "DIFF\nT\nMEM\nT\nUNI\n(2 3 4 5)\n");
    }

    @Test
    public void testDynamicScoping() throws Exception {
        testCase("(DEFUN F(X) (PLUS X Y))(DEFUN G1(Y) (F 10))(DEFUN G2(Y) (F 20))(G1 5)(G2 5)(G1 (G2 5))", "F\nG1\nG2\n15\n25\n35\n");
    }

    @Test
    public void testFormalList() throws Exception {
        testCase("(DEFUN X1(X) (PLUS 1 X))(DEFUN X2(X) (PLUS X 2))(X2 (X1 0))", "X1\nX2\n3\n");
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