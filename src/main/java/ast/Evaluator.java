package ast;

import type.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Evaluator {
    private Map<String, BuiltInOp> builtins;

    public Evaluator() {
        builtins = new HashMap<>();
        for(Method m: BuiltInOps.class.getMethods()) {
            BuiltIn annotation = (BuiltIn) m.getAnnotation(BuiltIn.class);
            if(annotation != null) {
                String[] names = annotation.operatorName();
                for (String name : names) {
                    if(!builtins.containsKey(name)) {
                        builtins.put(name, new BuiltInOp(annotation.expectedLength()));
                    }
                    switch(annotation.type()) {
                        case "INVOKE":
                            builtins.get(name)._method = m;
                            break;
                        case "CHECK":
                            builtins.get(name)._check = m;
                            break;
                        case "BOUND":
                            builtins.get(name)._bound = m;
                            break;
                    }
                }
            }
        }
    }

    public Type getType(TreeNode exp) throws TypeCheckException {
        if(exp == null) throw new TypeCheckException("Cannot evaluate null node.");

        if(exp.isAtom()) {
            if(exp.isTrue() || exp.isFalse()) return new BoolType();
            if(exp.isNil()) return new ListType(new NatType());
            if(exp.isNumeric()) return new NatType();
            else throw new TypeCheckException("Type Error: Literal not allowed.");
        }

        if(!exp.isList())
            throw new TypeCheckException("CHECK: eval() is undefined for non-list trees.");
        if(exp.isList() && exp.getListLength() < 2) {
            throw new TypeCheckException("CHECK: eval() is undefined for one-element trees");
        }
        if(!exp.leftChild().isAtom() || !exp.leftChild().isLiteral())
            throw new TypeCheckException("CHECK: eval() is undefined for list " + exp.toListNotation() + ".");

        //searching for the builtin
        String builtinName = exp.leftChild().getAtom().getLexme();

        BuiltInOp op = builtins.get(builtinName);
        if(op == null)
            throw new TypeCheckException("Cannot find built in " + builtinName);

        //check for parameter length
        if(op._expectedParams > 0 && exp.getListLength() != op._expectedParams) {
            throw new TypeCheckException("Expected " + op._expectedParams + " params, found " + exp.getListLength());
        }

        //invoke the builtin
        return op.check(this, exp);
    }

    public int getBound(TreeNode exp) throws TypeCheckException {
        if(exp == null) throw new TypeCheckException("Cannot evaluate null node.");
        if(exp.isAtom()) {
            return 0;
        }

        if(!exp.isList())
            throw new TypeCheckException("CHECK: eval() is undefined for non-list trees.");
        if(exp.getListLength() < 2)
            throw new TypeCheckException("CHECK: eval() is undefined for one-element trees");
        if(!exp.leftChild().isAtom() || !exp.leftChild().isLiteral())
            throw new TypeCheckException("CHECK: eval() is undefined for list " + exp.toListNotation() + ".");

        //searching for the builtin
        String builtinName = exp.leftChild().getAtom().getLexme();

        BuiltInOp op = builtins.get(builtinName);
        if(op == null)
            throw new TypeCheckException("Cannot find built in " + builtinName);

        //check for parameter length
        if(op._expectedParams > 0 && exp.getListLength() != op._expectedParams) {
            throw new TypeCheckException("Expected " + op._expectedParams + " params, found " + exp.getListLength());
        }

        int ret = op.bound(this, exp);
        if(ret < 0) throw new TypeCheckException("There can be empty list exception");
        return ret;
    }

    public TreeNode eval(TreeNode exp) throws EvaluationException {
        if(exp == null) throw new EvaluationException("Cannot evaluate null node.");
        if(exp.isAtom()) {
            if(exp.isNil() || exp.isTrue() || exp.isFalse() || exp.isNumeric()) return exp;
            else throw new EvaluationException("eval() is undefined for this atom.");
        }
        if(!exp.isList())
            throw new EvaluationException("eval() is undefined for non-list trees.");
        if(!exp.leftChild().isAtom() || !exp.leftChild().isLiteral())
            throw new EvaluationException("eval() is undefined for list " + exp.toListNotation() + ".");

        //searching for the built-in
        String builtinName = exp.leftChild().getAtom().getLexme();
        BuiltInOp op = builtins.get(builtinName);
        if(op == null)
            throw new EvaluationException("Cannot find built in " + builtinName);

        //check for parameter length
        if(op._expectedParams > 0 && exp.getListLength() != op._expectedParams) {
            throw new EvaluationException("Expected " + op._expectedParams + " params, found " + exp.getListLength());
        }

        //invoke the builtin
        return op.invoke(this, exp);
    }

    private class BuiltInOp {
        public Method _method;
        public Method _check;
        public Method _bound;
        public int _expectedParams;

        public BuiltInOp(int expectedParams) {
            _expectedParams = expectedParams;
        }

        public TreeNode invoke(Evaluator evaluator, TreeNode exp) throws EvaluationException {
            String builtinName = exp.leftChild().getAtom().getLexme();
            try {
                return (TreeNode)_method.invoke(null, evaluator, builtinName, exp.rightChild());
            } catch (IllegalAccessException e) {
                throw new EvaluationException("error while invoking built-in method");
            } catch (InvocationTargetException e) {
                if(e.getCause() instanceof  EvaluationException) {
                    throw (EvaluationException) e.getCause();
                } else {
                    throw new EvaluationException("error while invoking built-in method");
                }
            }
        }

        public Type check(Evaluator evaluator, TreeNode exp) throws TypeCheckException {
            String builtinName = exp.leftChild().getAtom().getLexme();
            try {
                return (Type)_check.invoke(null, evaluator, builtinName, exp.rightChild());
            } catch (IllegalAccessException e) {
                throw new TypeCheckException("error while invoking built-in type check method");
            } catch (InvocationTargetException e) {
                if(e.getCause() instanceof TypeCheckException) {
                    throw (TypeCheckException) e.getCause();
                } else {
                    throw new TypeCheckException("error while invoking built-in type check method:" + e.getMessage());
                }
            }
        }
        public int bound(Evaluator evaluator, TreeNode exp) throws TypeCheckException {
            String builtinName = exp.leftChild().getAtom().getLexme();
            try {
                return (int)_bound.invoke(null, evaluator, builtinName, exp.rightChild());
            } catch (IllegalAccessException e) {
                throw new TypeCheckException("error while invoking built-in bound check method");
            } catch (InvocationTargetException e) {
                if(e.getCause() instanceof TypeCheckException) {
                    throw (TypeCheckException) e.getCause();
                } else {
                    throw new TypeCheckException("error while invoking built-in bound check method");
                }
            }
        }
    }

}
