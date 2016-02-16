package ast;

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
                    builtins.put(name, new BuiltInOp(m, annotation.expectedLength()));
                }
            }
        }
    }

    public TreeNode eval(TreeNode exp) throws EvaluationException {
        if(exp == null) throw new EvaluationException("Cannot evaluate null node.");
        if(exp.isAtom()) {
            if(exp.isNil() || exp.isTrue() || exp.isNumeric()) return exp;
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
        public int _expectedParams;
        public BuiltInOp(Method method, int expectedParams) {
            _method = method;
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
    }
}
