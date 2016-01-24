package parser;

import org.testng.annotations.Test;
import utils.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.testng.Assert.*;

public class InterpreterTest {
    @Test
    public void testSimple() throws Exception {
        testCase("NIL", "NIL\n");
        testCase("()", "NIL\n");
        testCase("(NIL)", "(NIL . NIL)\n");
        testCase("(NIL\nNIL)", "(NIL . (NIL . NIL))\n");
        testCase("(NIL\nNIL)()", "(NIL . (NIL . NIL))\nNIL\n");
        testCase("(NIL\nNIL)\n()", "(NIL . (NIL . NIL))\nNIL\n");
        testCase("(NIL\nNIL)\r()", "(NIL . (NIL . NIL))\nNIL\n");
        testCase("(NIL\nNIL) ()", "(NIL . (NIL . NIL))\nNIL\n");
        testCase("(3 5 (XYZ) 7)", "(3 . (5 . ((XYZ . NIL) . (7 . NIL))))\n");
        testCase("(3 5 (XYZ 3) 7)", "(3 . (5 . ((XYZ . (3 . NIL)) . (7 . NIL))))\n");
        testCase("(NIL 5 ( ) (( )) 7 (( ) 9 ( )) )", "(NIL . (5 . (NIL . ((NIL . NIL) . (7 . ((NIL . (9 . (NIL . NIL))) . NIL))))))\n");
        testCase("3 5 (2 3)", "3\n5\n(2 . (3 . NIL))\n");
        testCase("3(2 3)", "3\n(2 . (3 . NIL))\n");
        testCase("(()3)", "(NIL . (3 . NIL))\n");
        testCase("((()3))", "((NIL . (3 . NIL)) . NIL)\n");
        testCase("(3 (AB) (3) A)", "(3 . ((AB . NIL) . ((3 . NIL) . (A . NIL))))\n");
    }

    @Test
    public void testError() throws Exception {
        testCase("34XY", Parser.ERROR_EXCEPT_ATOM_OR_LIST + "Invalid Token 34XY\n");
        testCase("(2 3)( 34XY", "(2 . (3 . NIL))\n" + Parser.ERROR_LIST_NOT_COMPLETE + "Invalid Token 34XY\n");
        testCase("(2 3)(", "(2 . (3 . NIL))\n" + Parser.ERROR_LIST_NOT_COMPLETE + "EOF\n");
        testCase(")\n", Parser.ERROR_EXCEPT_ATOM_OR_LIST + "')'\n");
        testCase("", Parser.ERROR_EXCEPT_ATOM_OR_LIST + "EOF\n");
        testCase("(a)", Parser.ERROR_LIST_NOT_COMPLETE + "Invalid Token a\n");
        testCase("(A (3) b)", Parser.ERROR_LIST_NOT_COMPLETE + "Invalid Token b\n");
        testCase("(a) 3)", Parser.ERROR_LIST_NOT_COMPLETE + "Invalid Token a\n");
        testCase("(3) b", "(3 . NIL)\n"+ Parser.ERROR_EXCEPT_ATOM_OR_LIST + "Invalid Token b\n");
    }

    private void testCase(String input, String output) throws Exception {
        TestUtils.RedirectStdinToString(input);
        ByteArrayOutputStream os = TestUtils.RedirectStdoutToString();
        Interpreter.Main(new String[]{});
        assertEquals(os.toString(),output);
    }

}