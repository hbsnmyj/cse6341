package ast;

import org.testng.annotations.Test;
import utils.TestUtils;

import java.io.ByteArrayOutputStream;

import static org.testng.Assert.assertEquals;

public class TypeCheckInterpreterTest {
    @Test
    public void testSimple() throws Exception {
        testCase("T", "T\n");
        testCase("F", "F\n");
        testCase("NIL", "NIL\n");
        testCase("3", "3\n");
        testCase("()", "NIL\n");
        testCase("(PLUS 3  3)", "(PLUS 3 3)\n");
        testCase("(NULL ())", "(NULL NIL)\n");
        testCase("(INT 3)", "(INT 3)\n");
        testCase("(INT (CONS 2 NIL))", "(INT (CONS 2 NIL))\n");
        testCorrect("(COND (T 5) (F 6))");
        testCorrect("(COND (F 5) (F 6))");
        testCorrect("(CONS 3 NIL)");
        testCorrect("(COND (F 3) (T 4))");
        testCorrect("(COND (T F) (F T))");
        testCorrect("(COND (T (CONS 3 NIL)) (T NIL))");
        testCorrect("(COND (F (CONS 3 NIL)) (F NIL))");
        testCorrect("(LESS 3 5)");
        testCorrect("(LESS (CAR (CONS 3 NIL)) 6)");
        testCorrect("(ATOM 3)");
        testCorrect("(ATOM NIL)");
        testCorrect("(ATOM T)");
        testCorrect("(ATOM (CONS 3 NIL))");
        testCorrect("(INT 3)");
        testCorrect("(INT NIL)");
        testCorrect("(INT T)");
        testCorrect("(INT (CONS 3 NIL))");
        testCorrect("(PLUS (CAR (CONS 5 NIL)) (CAR (CONS 5 NIL)))");
        testCorrect("(COND (F NIL) (T (CONS 1 NIL)))");
        testCorrect("(COND ((NULL NIL) 3) ((ATOM NIL) 4) ((EQ 2 3) 5) ((INT NIL) 6) ((LESS 3 5) 7))");
    }

    @Test
    public void testError() throws Exception {
        testCaseStartsWith("CAR","ERROR: ");
        testCaseStartsWith("(1)","ERROR: ");
        testCaseStartsWith("(CAR)","ERROR: ");
        testCaseStartsWith("(CAR T)","ERROR: ");
        testCaseStartsWith("(CAR F)","ERROR: ");
        testCaseStartsWith("(CAR NIL)", "ERROR: ");
        testCaseStartsWith("(CAR 1)", "ERROR: ");
        testCaseStartsWith("(CAR 1 2)", "ERROR: ");
        testCaseStartsWith("(COND (T 3) (F NIL))","ERROR: ");
        testCaseStartsWith("(CAR (COND (T (CONS 3 NIL)) (F NIL)))","ERROR: ");
        testError("(CAR NIL)");
        testError("CDR 5");
        testError("(CDR (COND (T (CONS 1 NIL)) (F NIL)))");
        testError("(CDR (CDR (CDR (CONS 1 (CONS 1 NIL)))))");
        testError("(CDR (CDR (CDR (CDR (CONS 1 (CONS 1 (CONS 1 NIL)))))))");
        testError("(CONS)");
        testError("(CONS 1 2 3)");
        testError("(CONS 1 NIL NIL)");
        testError("(CONS 1 2)");
        testError("(CONS NIL NIL)");
        testError("(CONS T NIL)");
        testError("(CONS 1 T)");
        testError("(CONS 1 (CDR (CDR (CONS 1 NIL))))");
        testError("(ATOM)");
        testError("(ATOM 1 1)");
        testError("(EQ T T)");
        testError("(EQ NIL NIL)");
        testError("(EQ NIL T)");
        testError("(EQ 3 T)");
        testError("(EQ A 3)");
        testError("(EQ T (NULL (CONS 1 (CDR (CDR (CONS 1 NIL))))))");
        testError("(NULL)");
        testError("(NULL 100)");
        testError("(NULL (CONS 3 (CDR (CDR (CONS 3 NIL)))))");
        testError("(INT)");
        testError("(INT 1 1)");
        testError("(INT (CONS 3 3))");
        testError("(INT (CONS T 3))");
        testError("(INT (NULL (CONS 3 (CDR (CDR (CONS 3 NIL))))))");
        testError("(PLUS)");
        testError("(PLUS NIL 3)");
        testError("(EQ 5 T)");
        testError("(NULL 5)");
        testError("(COND ((CONS 4 NIL) T) (T 5))");
        testError("(CAR (COND (F NIL) (T (CONS 1 NIL))))");
        testError("(COND (T 5) (F NIL))");
        testError("(LESS 3 NIL)");
        testError("(LESS NIL)");
        testError("(LESS 2 3 4)");
        testError("(CAR (2 4))");
        testError("(COND (T 10) (T V))");
        testError("(CAR (COND (F NIL) (T (CONS 1 NIL))))");
        testError("(CONS () ())");
        testError("(CONS () (1 2) )");
        testError("(CONS 1 (1 2) )");
        testError("(CONS 1 (2) )");
        testError("(CONS (1) () )");
        testError("(CONS 1 ( () ) )");
        testError("(CONS 1 ( 2 () ) )");
        testError("(CDR (CDR (CONS 1 NIL)))");
        testError("(CAR (COND (F NIL) (T (CONS 1 NIL))))");
        testError("(CDR (COND (F NIL) (T (CONS 1 NIL))))");
    }

    private void testCaseStartsWith(String input, String output) throws Exception {
        TestUtils.RedirectStdinToString(input);
        ByteArrayOutputStream os = TestUtils.RedirectStdoutToString();
        TypeCheckInterpreter.main(new String[]{});
        assert(os.toString().replaceAll("\r\n", "\n").startsWith(output));
    }

    private void testError(String input) throws Exception {
        testCaseStartsWith(input, "ERROR: TYPE");
    }

    private void testCorrect(String input) throws Exception {
        testCase(input, input + "\n");
    }

    private void testCase(String input, String output) throws Exception {
        TestUtils.RedirectStdinToString(input);
        ByteArrayOutputStream os = TestUtils.RedirectStdoutToString();
        TypeCheckInterpreter.main(new String[]{});
        assertEquals(os.toString().replaceAll("\r\n", "\n"),output);
    }

}