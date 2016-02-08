package ast;

import org.testng.annotations.Test;
import static ast.Token.Atom;

import static ast.Token.NIL_TOKEN;
import static ast.TreeNode.Cons;
import static ast.TreeNode.TokenNode;
import static org.testng.Assert.*;

public class TreeNodeTest {

    private void testEvalCase(TreeNode input, String output) {
        assertEquals(input.eval().toListNotation(), output);
    }

    @Test
    public void testAtomEval() throws Exception {
        testEvalCase(TokenNode(Atom("NIL")), "NIL");
        testEvalCase(TokenNode(Atom("A")), "A");
        testEvalCase(TokenNode(Atom(3)), "3");
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



    private void testListNotation(TreeNode input, String output) {
        assertEquals(input.toListNotation(), output);
    }

    @Test
    void testListNotation() {
        testListNotation(TokenNode(Atom("NIL")), "NIL");
        testListNotation(TokenNode(Atom("3")), "3");
        testListNotation(TokenNode(Atom("ABC")), "ABC");
        testListNotation(Cons(Atom("NIL"), Atom("NIL")), "(NIL)");
        testListNotation(Cons(Atom(2), Cons(Atom(3), Cons(Atom(4), Atom(5)))), "(2 3 4 . 5)");
        testListNotation(Cons(Atom(2), Cons(Atom(3), Cons(Atom(4), Token.NIL_TOKEN))), "(2 3 4)");
        testListNotation(Cons(Cons(Atom(2), Token.NIL_TOKEN), Cons(Atom(3), Cons(Atom(4), Token.NIL_TOKEN))), "((2) 3 4)");
    }

}