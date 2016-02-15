package ast;

import org.testng.annotations.Test;
import parser.Parser;
import scanner.StringScanner;
import scanner.Tokenizer;
import scanner.TokenizerAdapter;

import java.util.List;

import static ast.Token.Atom;

import static ast.Token.NIL_TOKEN;
import static ast.TreeNode.Cons;
import static ast.TreeNode.TokenNode;
import static org.testng.Assert.*;

public class TreeNodeTest {

    private void testEvalCase(TreeNode input, String output) {
        assertEquals(input.eval().toListNotation(), output);
    }

    private void testEvalCase(String input, String output) throws Exception {
        TokenizerAdapter tokenizer;
        tokenizer = new TokenizerAdapter(new StringScanner(input), new Tokenizer());
        Parser parser = new Parser(tokenizer);
        List<TreeNode> results = parser.getExpressions();
        assertEquals(results.size(), 1);
        assertEquals(results.get(0).eval().toListNotation(), output);
    }


    @Test
    public void testAtomEval() throws Exception {
        testEvalCase(TokenNode(Atom("NIL")), "NIL");
        testEvalCase(TokenNode(Atom(3)), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testAtomIllegal() throws Exception {
        testEvalCase(TokenNode(Atom("A")),"");
    }

    @Test
    public void testCar() throws Exception {
        testEvalCase(Cons(Atom("CAR"), Cons(Atom(2), Token.NIL_TOKEN)), "2");
        testEvalCase(Cons(Atom("CAR"), Cons(Atom(2), Atom(3))), "2");
        testEvalCase(Cons(Atom("CAR"), Cons(Cons(Atom(3), Atom(3)), Token.NIL_TOKEN)), "((3 . 3))");
        testEvalCase(Cons(Atom("CAR"), Cons(Cons(Atom(2), Token.NIL_TOKEN), Cons(Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)), Token.NIL_TOKEN))), "(2)");
    }


    @Test(expectedExceptions = EvaluationException.class)
    public void testCarIsUndefinedToAtom() throws Exception {
        testEvalCase(Cons(Atom("CAR"), TokenNode(Atom(2))), "");
    }

    @Test
    public void testCdr() throws Exception {
        testEvalCase(Cons(Atom("CDR"), Cons(Atom(2), Token.NIL_TOKEN)), "NIL");
        testEvalCase(Cons(Atom("CDR"), Cons(Atom(2), Atom(3))), "3");
        testEvalCase(Cons(Atom("CDR"), Cons(Cons(Atom(3), Atom(3)), Token.NIL_TOKEN)), "NIL");
        testEvalCase(Cons(Atom("CDR"), Cons(Cons(Atom(2), Token.NIL_TOKEN), Cons(Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)), Token.NIL_TOKEN))), "((2 3))");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testCdrIsUndefinedToAtom() throws Exception {
        testEvalCase(Cons(Atom("CDR"), TokenNode(Atom(2))), "");
    }

    @Test
    public void testCons() throws Exception {
        testEvalCase(Cons(Atom("CONS"), Cons(Atom(3), Cons(Atom(2), Token.NIL_TOKEN))), "(3 2)");
        testEvalCase(Cons(Atom("CONS"), Cons(Atom(3), Cons(Cons(Atom(2), Cons(Atom(3), Token.NIL_TOKEN)), Token.NIL_TOKEN))), "(3 (2 3))");
    }

    @Test
    public void testOperators() throws Exception {
        testEvalCase(Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(2), NIL_TOKEN))), "3");
        testEvalCase(Cons(Atom("MINUS"), Cons(Atom(1), Cons(Atom(2), NIL_TOKEN))), "-1");
        testEvalCase(Cons(Atom("TIMES"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "6");
        testEvalCase(Cons(Atom("LESS"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "NIL");
        testEvalCase(Cons(Atom("GREATER"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "T");
        testEvalCase(Cons(Atom("PLUS"), Cons(Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(1), NIL_TOKEN))), Cons(Atom(2), NIL_TOKEN))), "4");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testPlusIsUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testPlusIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("PLUS"), Cons(Atom("A"), Cons(Atom(2), NIL_TOKEN))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testMinusIsUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("MINUS"), Cons(Atom(1), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testMinusIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("MINUS"), Cons(Atom("A"), Cons(Atom(2), NIL_TOKEN))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testTimesUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("TIMES"), Cons(Atom(1), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testTimesIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("TIMES"), Cons(Atom("A"), Cons(Atom(2), NIL_TOKEN))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testLessUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("LESS"), Cons(Atom(1), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testLessIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("LESS"), Cons(Atom("A"), Cons(Atom(2), NIL_TOKEN))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testGreaterUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("GREATER"), Cons(Atom(1), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN)))), "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testGreaterIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("GREATER"), Cons(Atom("A"), Cons(Atom(2), NIL_TOKEN))), "3");
    }

    @Test
    public void testEq() throws Exception {
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom(3), NIL_TOKEN))), "NIL");
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom(2), NIL_TOKEN))), "T");
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(1), NIL_TOKEN))))), "T");
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(2), NIL_TOKEN))))), "NIL");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testEQIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom(3), Cons(Atom(4), NIL_TOKEN)))), "T");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testEQIsUndefined() throws Exception {
        testEvalCase(Cons(Atom("EQ"), Cons(Atom("A"), Cons(Atom(3), NIL_TOKEN))), "T");
    }



    @Test
    public void testUnaryOperators() throws Exception {
        testEvalCase(Cons(Atom("ATOM"), Atom(2)), "T");
        testEvalCase(Cons(Atom("ATOM"), Atom("T")), "T");
        testEvalCase(Cons(Atom("ATOM"), Atom("NIL")), "T");
        testEvalCase(Cons(Atom("ATOM"), Cons(Atom("NIL"), Atom("T"))), "NIL");
        testEvalCase(Cons(Atom("INT"), Atom(2)), "T");
        testEvalCase(Cons(Atom("INT"), Atom("NIL")), "NIL");
        testEvalCase(Cons(Atom("NULL"), Atom("NIL")), "T");
        testEvalCase(Cons(Atom("NULL"), Atom(2)), "NIL");
    }

    @Test
    public void testQuote() throws Exception {
        testEvalCase(Cons(Atom("QUOTE"), Cons(Atom("PLUS"), Cons(Atom(2), Atom(3)))), "(PLUS 2 3)");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testQuoteIsTooLong() throws Exception {
        testEvalCase(Cons(Atom("QUOTE"), Cons(Atom("Plus"), Cons(Atom(2), NIL_TOKEN))), "");
    }

    @Test
    public void testCond() throws Exception {
        testEvalCase("(COND (3 2) (4 3))", "2");
        testEvalCase("(COND (NIL 2) (4 3))", "3");
        testEvalCase("(COND ((EQ 2 3) 2) (4 3))", "3");
        testEvalCase("(COND ((EQ 2 2) 2) (4 3))", "2");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testCondIsIllegalWhenElementIsNotList() throws Exception {
        testEvalCase("(COND (3 2) 3)", "2");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testCondIsIllegalWhenElementLengthIsNotCorrect() throws Exception {
        testEvalCase("(COND (3 2) (3 2 3))", "2");
    }

   private void testListNotation(TreeNode input, String output) {
        assertEquals(input.toListNotation(), output);
    }
    @Test
    void testListNotation1() {
        testListNotation(TokenNode(Atom("NIL")), "NIL");
        testListNotation(TokenNode(Atom("3")), "3");
        testListNotation(TokenNode(Atom("ABC")), "ABC");
        testListNotation(Cons(Atom("NIL"), Atom("NIL")), "(NIL)");
        testListNotation(Cons(Atom(2), Cons(Atom(3), Cons(Atom(4), Atom(5)))), "(2 3 4 . 5)");
        testListNotation(Cons(Atom(2), Cons(Atom(3), Cons(Atom(4), Token.NIL_TOKEN))), "(2 3 4)");
        testListNotation(Cons(Cons(Atom(2), Token.NIL_TOKEN), Cons(Atom(3), Cons(Atom(4), Token.NIL_TOKEN))), "((2) 3 4)");
    }

}