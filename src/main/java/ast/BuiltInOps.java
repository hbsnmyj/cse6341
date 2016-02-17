package ast;

import java.util.List;

/**
 * Created by snmyj on 2/15/16.
 */
public class BuiltInOps {
    @BuiltIn(operatorName = {"PLUS", "MINUS", "TIMES", "GREATER", "LESS"}, expectedLength = 3)
    public static TreeNode binaryOperator(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        TreeNode op1 = evaluator.eval(params.get(0));
        TreeNode op2 = evaluator.eval(params.get(1));
        if (!op1.isAtom() || !op2.isAtom()) throw new EvaluationException("Expecting numeric atoms");
        if (!op1.isNumeric() || !op2.isNumeric()) throw new EvaluationException("Expecting numeric atoms");
        int op1i = op1.getAtom().toInteger();
        int op2i = op2.getAtom().toInteger();
        switch(op) {
            case "PLUS":
               return new TreeNode(Token.Atom(op1i + op2i));
            case "MINUS":
               return new TreeNode(Token.Atom(op1i - op2i));
            case "TIMES":
               return new TreeNode(Token.Atom(op1i * op2i));
            case "LESS":
                return TreeNode.BooleanNode(op1i < op2i);
            case "GREATER":
                return TreeNode.BooleanNode(op1i > op2i);
            default:
                throw new EvaluationException("op " + op + " is not supported");
        }
    }

    @BuiltIn(operatorName = {"EQ"}, expectedLength = 3)
    public static TreeNode eqOperator(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        TreeNode op1 = evaluator.eval(params.get(0));
        TreeNode op2 = evaluator.eval(params.get(1));
        if (!op1.isAtom() || !op2.isAtom()) throw new EvaluationException("Expecting atoms");
        if (op1.isLiteral() && op1.getAtom().equals(op2.getAtom()))
            return TreeNode.BooleanNode(true);
        else if(op1.isNumeric() && op1.getAtom().equals(op2.getAtom()))
            return TreeNode.BooleanNode(true);
        else
            return TreeNode.BooleanNode(false);
    }

    @BuiltIn(operatorName = {"INT", "NULL", "ATOM"}, expectedLength = 2)
    public static TreeNode unaryOperator(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        TreeNode op1 = evaluator.eval(params.get(0));
        switch(op) {
            case "ATOM":
                return TreeNode.BooleanNode(op1.isAtom());
            case "INT":
                return TreeNode.BooleanNode(op1.isNumeric());
            case "NULL":
                return TreeNode.BooleanNode(op1.isNil());
            default:
                throw new EvaluationException("op " + op + " is not supported");
        }
    }

    @BuiltIn(operatorName = {"CAR", "CDR"}, expectedLength = 2)
    public static TreeNode carcdr(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        exp = evaluator.eval(exp.leftChild());
        if(exp.isAtom())
            throw new EvaluationException("car/cdr is not supported on atoms.");
        switch(op) {
            case "CAR":
                return exp.leftChild();
            case "CDR":
                return exp.rightChild();
            default:
                throw new EvaluationException("op " + op + " is not supported");
        }
    }

    @BuiltIn(operatorName = {"QUOTE"}, expectedLength = 2)
    public static TreeNode quote(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        return exp.leftChild();
    }

    @BuiltIn(operatorName = {"CONS"}, expectedLength = 3)
    public static TreeNode cons(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        return new TreeNode(evaluator.eval(params.get(0)), evaluator.eval(params.get(1)));
    }

    @BuiltIn(operatorName = {"COND"}, expectedLength = -1)
    public static TreeNode cond(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        for(TreeNode s : params) {
            if(!s.isList()) throw new EvaluationException("Expecting only list for cond's branches.");
            if(s.getListLength() != 2) throw new EvaluationException("Illegal cond branches.");
        }
        for(TreeNode s : params) {
            if(!evaluator.eval(s.leftChild()).isNil())
                return evaluator.eval(s.rightChild().leftChild());
        }
        throw new EvaluationException("All branches of cond are false.");
    }
}
