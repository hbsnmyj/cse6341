package ast;

import type.BoolType;
import type.ListType;
import type.NatType;
import type.Type;

import java.util.List;

/**
 * Created by snmyj on 2/15/16.
 */
public class BuiltInOps {
    @BuiltIn(operatorName = {"PLUS", "MINUS", "TIMES", "GREATER", "LESS"}, expectedLength = 3, type = "INVOKE")
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

    @BuiltIn(operatorName = {"PLUS", "LESS", "EQ"}, expectedLength = 3, type = "CHECK")
    public static Type binaryOperatorCheck(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        Type op1 = evaluator.getType(params.get(0));
        Type op2 = evaluator.getType(params.get(1));
        if (!op1.isNat() || !op2.isNat()) throw new TypeCheckException (op + ": Expecting numeric atoms");
        return new NatType();
    }

    @BuiltIn(operatorName = {"PLUS", "LESS", "EQ"}, expectedLength = 3, type = "BOUND")
    public static int binaryOperatorBound(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        evaluator.getBound(params.get(0));
        evaluator.getBound(params.get(1));
        return 0;
    }

    @BuiltIn(operatorName = {"EQ"}, expectedLength = 3, type = "INVOKE")
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


    @BuiltIn(operatorName = {"INT", "NULL", "ATOM"}, expectedLength = 2, type = "INVOKE")
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

    @BuiltIn(operatorName = {"INT", "NULL", "ATOM"}, expectedLength = 2, type = "BOUND")
    public static int unaryOperatorBound(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        evaluator.getBound(params.get(0));
        return 0;
    }

    @BuiltIn(operatorName = {"INT", "NULL", "ATOM"}, expectedLength = 2, type = "CHECK")
    public static Type unaryOperatorCheck(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        Type op1 = evaluator.getType(params.get(0));
        switch(op) {
            case "ATOM":
                if(!op1.isT()) throw new TypeCheckException("CHECK: Cannot check list of lists");
                else return new BoolType();
            case "INT":
                if(!op1.isT()) throw new TypeCheckException("CHECK: Cannot check list of lists");
                return new BoolType();
            case "NULL":
                if(!op1.equals(new ListType(new NatType())))
                    throw new TypeCheckException("CHECK: NULL: ONLY ALLOW List(Nat)");
                return new BoolType();
            default:
                throw new TypeCheckException("op " + op + " is not supported");
        }    }


    @BuiltIn(operatorName = {"CAR", "CDR"}, expectedLength = 2, type = "CHECK")
    public static Type carcdrCheck(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException{
        Type op1 = evaluator.getType(exp.leftChild());
        if(!op1.isNatList()) throw new TypeCheckException("CAR/CDR expects List(Nat)");
        switch(op) {
            case "CAR":
                return new NatType();
            case "CDR":
                return new ListType(new NatType());
            default:
                throw new TypeCheckException("op " + op + " is not supported");
        }
    }

    @BuiltIn(operatorName = {"CAR", "CDR"}, expectedLength = 2, type = "BOUND")
    public static int carcdrBound(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException{
        int op1 = evaluator.getBound(exp.leftChild());
        if(op1 == 0) throw new TypeCheckException("Cannot do car on a emptyable list.");
        switch(op) {
            case "CAR":
                return 1;
            case "CDR":
                return op1 - 1;
            default:
                throw new TypeCheckException("op " + op + " is not supported");
        }
    }

    @BuiltIn(operatorName = {"CAR", "CDR"}, expectedLength = 2, type = "INVOKE")
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

    @BuiltIn(operatorName = {"QUOTE"}, expectedLength = 2, type = "INVOKE")
    public static TreeNode quote(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        return exp.leftChild();
    }

    @BuiltIn(operatorName = {"CONS"}, expectedLength = 3, type = "INVOKE")
    public static TreeNode cons(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        return new TreeNode(evaluator.eval(params.get(0)), evaluator.eval(params.get(1)));
    }

    @BuiltIn(operatorName = {"CONS"}, expectedLength = 3, type = "CHECK")
    public static Type consCheck(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        Type op1 = evaluator.getType(params.get(0));
        Type op2 = evaluator.getType(params.get(1));
        if(op1.isNat() && op2.isNatList())
            return new ListType(new NatType());
        else
            throw new TypeCheckException("CHECK: CONS expected Nat and List(Nat).");
    }

    @BuiltIn(operatorName = {"CONS"}, expectedLength = 3, type = "BOUND")
    public static int consBound(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        int op1 = evaluator.getBound(params.get(0));
        int op2 = evaluator.getBound(params.get(1));
        return op2 + 1;
    }

    @BuiltIn(operatorName = {"COND"}, expectedLength = -1, type = "INVOKE")
    public static TreeNode cond(Evaluator evaluator, String op, TreeNode exp) throws EvaluationException {
        List<TreeNode> params = exp.retrieveListElements();
        if(params.size() < 1) throw new EvaluationException("No Branches");
        for(TreeNode s : params) {
            if(!s.isList()) throw new EvaluationException("Expecting only list for cond's branches.");
            if(s.getListLength() != 2) throw new EvaluationException("Illegal cond branches.");
        }
        for(TreeNode s : params) {
            TreeNode result = evaluator.eval(s.leftChild());
            if(result.isTrue())
                return evaluator.eval(s.rightChild().leftChild());
            else if(!result.isFalse())
                throw new EvaluationException("Invalid cond conditions.");
        }
        throw new EvaluationException("All branches of cond are false.");
    }

    @BuiltIn(operatorName = {"COND"}, expectedLength = -1, type = "CHECK")
    public static Type condCheck(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        if(params.size() < 1) throw new TypeCheckException("No Branches");
        for(TreeNode s : params) {
            if(!s.isList()) throw new TypeCheckException("Expecting only list for cond's branches.");
            if(s.getListLength() != 2) throw new TypeCheckException("Illegal cond branches.");
        }
        Type typeT = evaluator.getType(params.get(0).rightChild().leftChild());
        for(TreeNode s : params) {
            Type condType = evaluator.getType(s.leftChild());
            if(!condType.isBool())
                throw new TypeCheckException("COND's condition is not bool");
            Type resultType = evaluator.getType(s.rightChild().leftChild());
            if(!typeT.equals(resultType))
                throw new TypeCheckException("COND's result should be the same type");
        }
        return typeT;
   }

    @BuiltIn(operatorName = {"COND"}, expectedLength = -1, type = "BOUND")
    public static int condBound(Evaluator evaluator, String op, TreeNode exp) throws TypeCheckException {
        List<TreeNode> params = exp.retrieveListElements();
        int min = Integer.MAX_VALUE;
        for(TreeNode s : params) {
            evaluator.getBound(s.leftChild());
            int value = evaluator.getBound(s.rightChild().leftChild());
            if(min > value) min = value;
        }
        return min;
   }

}
