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

    private void testEvalCase(TreeNode input, String output) throws EvaluationException {
        Evaluator evaluator = new Evaluator();
        assertEquals(evaluator.eval(input).toListNotation(), output);
    }

    private void testEvalCase(String input, String output) throws Exception {
        Evaluator evaluator = new Evaluator();
        TokenizerAdapter tokenizer;
        tokenizer = new TokenizerAdapter(new StringScanner(input), new Tokenizer());
        Parser parser = new Parser(tokenizer);
        List<TreeNode> results = parser.getExpressions();
        assertEquals(results.size(), 1);
        assertEquals(evaluator.eval(results.get(0)).toListNotation(), output);
    }


    @Test
    public void testAtomEval() throws Exception {
        testEvalCase(TokenNode(Atom("NIL")), "NIL");
        testEvalCase("NIL", "NIL");
        testEvalCase("T", "T");
        testEvalCase("3", "3");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testAtomIllegal() throws Exception {
        testEvalCase("LITERAL", "");
    }

    @Test
    public void testCar() throws Exception {
        testEvalCase("(CAR (QUOTE (2)))", "2");
        testEvalCase("(CAR (QUOTE ((1 2))))", "(1 2)");
        testEvalCase("(CAR (QUOTE (2 3)))", "2");
        testEvalCase("(CAR (QUOTE ((2 3) (3 4 5) NIL)))", "(2 3)");
        testEvalCase("(CAR (QUOTE (2 (2 3) (3 4))))", "2");
        testEvalCase("(CAR (QUOTE ((2 3))))", "(2 3)");    }


    @Test(expectedExceptions = EvaluationException.class)
    public void testCarIsUndefinedToAtom() throws Exception {
        testEvalCase("(CAR (QUOTE 2))", "");
    }


    @Test
    public void testCdr() throws Exception {
        testEvalCase("(CDR (QUOTE (2)))", "NIL");
        testEvalCase("(CDR (QUOTE (2 3)))", "(3)");
        testEvalCase("(CDR (QUOTE (2 (2 3))))", "((2 3))");
        testEvalCase("(CDR (QUOTE (2 (2 3) (3 4))))", "((2 3) (3 4))");
        testEvalCase("(CDR (QUOTE ((2 3))))", "NIL");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testCdrIsUndefinedToAtom() throws Exception {
        testEvalCase("(CDR (QUOTE 2))", "");
    }

    @Test
    public void testCons() throws Exception {
        testEvalCase("(CONS 3 2)", "(3 . 2)");
        testEvalCase("(CONS  3 (CONS 2 NIL))", "(3 2)");
        testEvalCase("(CONS 3 (QUOTE (3 2)))", "(3 3 2)");
        testEvalCase("(CONS 2 (CONS 3 (CONS 4 5)))", "(2 3 4 . 5)");
    }

    @Test
    public void testOperators() throws Exception {
        testEvalCase(Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(2), NIL_TOKEN))), "3");
        testEvalCase("(PLUS 3 2)", "5");
        testEvalCase(Cons(Atom("MINUS"), Cons(Atom(1), Cons(Atom(2), NIL_TOKEN))), "-1");
        testEvalCase("(MINUS 3 2)", "1");
        testEvalCase(Cons(Atom("TIMES"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "6");
        testEvalCase("(TIMES 5 3)", "15");
        testEvalCase(Cons(Atom("LESS"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "NIL");
        testEvalCase("(LESS 5 3)", "NIL");
        testEvalCase("(LESS 3 5)", "T");
        testEvalCase(Cons(Atom("GREATER"), Cons(Atom(3), Cons(Atom(2), NIL_TOKEN))), "T");
        testEvalCase("(GREATER 5 3)", "T");
        testEvalCase("(GREATER 3 5)", "NIL");
        testEvalCase(Cons(Atom("PLUS"), Cons(Cons(Atom("PLUS"), Cons(Atom(1), Cons(Atom(1), NIL_TOKEN))), Cons(Atom(2), NIL_TOKEN))), "4");
        testEvalCase("(PLUS 3 (PLUS 1 1))", "5");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testPlusIsUndefinedWhenElementsIsTooLong() throws Exception {
        testEvalCase("(PLUS 3 5 2)", "");
    }

    @Test(expectedExceptions = EvaluationException.class)
    public void testPlusIsUndefined() throws Exception {
        testEvalCase("(PLUS 3 A)", "");
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
        testEvalCase("(EQ 2 3)", "NIL");
        testEvalCase("(EQ 3 3)", "T");
        testEvalCase(Cons(Atom("EQ"), Cons(Atom(2), Cons(Atom(2), NIL_TOKEN))), "T");

        testEvalCase("(EQ () NIL)", "T");
        testEvalCase("(EQ () ())", "T");
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
        testEvalCase("(ATOM 2)", "T");
        testEvalCase("(ATOM T)", "T");
        testEvalCase("(ATOM NIL)", "T");
        testEvalCase("(ATOM ())", "T");
        testEvalCase("(ATOM (PLUS 2 3))", "T");
        testEvalCase("(ATOM (QUOTE (NIL T)))", "NIL");
        testEvalCase("(INT 2)", "T");
        testEvalCase("(INT T)", "NIL");
        testEvalCase("(INT (PLUS 2 3))", "T");
        testEvalCase("(INT (QUOTE (PLUS 2 3)))", "NIL");
        testEvalCase("(NULL NIL)", "T");
        testEvalCase("(NULL 3)", "NIL");
        testEvalCase("(NULL (EQ 3 2))", "T");
    }

    @Test
    public void testQuote() throws Exception {
        testEvalCase("(QUOTE (PLUS1 2 3))", "(PLUS1 2 3)");
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

    @Test
    void adhocTestCases() throws Exception {
        testEvalCase("(CONS (CAR (QUOTE (5 6) ))  (CAR (CDR (QUOTE (5 6)))))", "(5 . 6)");
        testEvalCase("(CONS (QUOTE (1 2) ) (QUOTE (3 4) ) )", "((1 2) 3 4)");
        testEvalCase("(QUOTE (3))", "(3)");
        testEvalCase("(INT (QUOTE (3)))", "NIL");
        testEvalCase("(EQ (INT (QUOTE (3))) NIL)", "T");
        testEvalCase("(COND (T NIL))","NIL");
        testEvalCase("(COND ((PLUS 0 1) NIL))", "NIL");
    }
}