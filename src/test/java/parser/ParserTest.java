package parser;

import ast.TreeNode;
import org.testng.annotations.Test;
import scanner.Interpreter;
import scanner.StringScanner;
import scanner.Tokenizer;
import scanner.TokenizerAdapter;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class ParserTest {

    @Test
    void testSimple() throws Exception {
        testSingleStatement("(3 5 (XYZ) 7)", "(3 . (5 . ((XYZ . NIL) . (7 . NIL))))");
        testSingleStatement("(3 5 (XYZ 3) 7)", "(3 . (5 . ((XYZ . (3 . NIL)) . (7 . NIL))))");
        testSingleStatement("(NIL 5 ( ) (( )) 7 (( ) 9 ( )) )", "(NIL . (5 . (NIL . ((NIL . NIL) . (7 . ((NIL . (9 . (NIL . NIL))) . NIL))))))");
        testSingleStatement("()", "NIL");
        testSingleStatement("(NIL)", "(NIL . NIL)");
        testSingleStatement("(3)", "(3 . NIL)");
        testSingleStatement("(ABC)", "(ABC . NIL)");
        testSingleStatement("(DEFUN F23 (X) (PLUS X 12 55))", "(DEFUN . (F23 . ((X . NIL) . ((PLUS . (X . (12 . (55 . NIL)))) . NIL))))");
    }

    private void testSingleStatement(String input, String output) throws Exception {
        TokenizerAdapter tokenizer;
        tokenizer = new TokenizerAdapter(new StringScanner(input), new Tokenizer());
        Parser parser = new Parser(tokenizer);
        List<TreeNode> results = parser.getExpressions();
        assertEquals(results.size(), 1);
        assertEquals(results.get(0).toString(), output);
    }

}