package ast;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.reflect.generics.tree.Tree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Evaluator {
    private Map<String, BuiltInOp> builtins;
    private Map<String, TreeNode> dlist;

    public Evaluator() {
        builtins = new HashMap<>();
        dlist = new HashMap<>();
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
        return eval(exp, new HashMap<>());
    }

    public TreeNode eval(TreeNode exp, Map<String, TreeNode> alist) throws EvaluationException {
        if(exp == null) throw new EvaluationException("Cannot evaluate null node.");
        if(exp.isAtom()) {
            if(exp.isNil() || exp.isTrue() || exp.isNumeric()) return exp;
            else if(exp.isLiteral() && alist.containsKey(exp.getAtom().getLexme())) {
                return alist.get(exp.getAtom().getLexme());
            }
            else throw new EvaluationException("Unbounded variable " + exp.getAtom().getLexme());
        }
        if(!exp.isList())
            throw new EvaluationException("eval() is undefined for non-list trees.");
        if(!exp.leftChild().isAtom() || !exp.leftChild().isLiteral())
            throw new EvaluationException("eval() is undefined for list " + exp.toListNotation() + ".");

        //searching for the built-in
        String carName = exp.leftChild().getAtom().getLexme();
        if(carName.equals("DEFUN")) {
            return defun(exp.rightChild());
        } else {
            if(builtins.containsKey(carName)) {
                BuiltInOp op = getBuiltInOp(carName);

                //check for parameter length
                if (op._expectedParams > 0 && exp.getListLength() != op._expectedParams) {
                    throw new EvaluationException("Expected " + op._expectedParams + " params, found " + exp.getListLength());
                }

                //invoke the builtin
                return op.invoke(this, exp, alist);
            } else {
                return apply(carName, exp.rightChild(), alist);
            }
        }
    }

    private TreeNode apply(String carName, TreeNode treeNode, Map<String, TreeNode> alist) throws EvaluationException {
        if(!dlist.containsKey(carName)) throw new EvaluationException("Unbounded atom " + carName);
        else {
            TreeNode funcDefinition = dlist.get(carName);
            List<TreeNode> formalList = funcDefinition.leftChild().retrieveListElements();
            TreeNode s2 = funcDefinition.rightChild();
            /* check for parameter numbers */
            if(formalList.size() != treeNode.getListLength()) throw new EvaluationException("The number of formal arguments does not match formal arguments");
            Map<String, TreeNode> newAlist = new HashMap<>();
            List<TreeNode> actualList = treeNode.retrieveListElements();
            /* bind the arguments */
            for(int i=0;i<actualList.size();++i) {
                bindVariable(newAlist, formalList.get(i), eval(actualList.get(i), alist));
            }
            return eval(s2, newAlist);
        }
    }

    private void bindVariable(Map<String, TreeNode> newAlist, TreeNode treeNode, TreeNode eval) {
        newAlist.put(treeNode.getAtom().getLexme(), eval);
    }

    private TreeNode defun(TreeNode exp) throws EvaluationException {
        if(exp == null || !exp.isList() || exp.getListLength() != 3) throw new EvaluationException("Expected name, formal list and body for expr DEFUN");
        List<TreeNode> params = exp.retrieveListElements();
        TreeNode nodeName = params.get(0);
        String functionName = nodeName.getAtom().getLexme();
        if(!nodeName.isAtom() || !nodeName.isLiteral()) throw new EvaluationException("Expected function name for expr DEFUN");
        /* check for s1 */
        TreeNode s1 = params.get(1);
        if(!s1.isList()) throw new EvaluationException("Expected formal list to be a list");
        for(TreeNode t: s1.retrieveListElements())
            if(!t.isAtom() || !t.isLiteral()) {
                throw new EvaluationException("Expected formal list to be a list of literals.");
            }

        /* look up function name */
        if(dlist.containsKey(functionName) || builtins.containsKey(functionName)) throw new EvaluationException("function or built-in have been defined");
        /* save the definition in dlist */
        dlist.put(functionName, new TreeNode(s1,params.get(2)));
        return nodeName;
    }

    private BuiltInOp getBuiltInOp(String builtinName) throws EvaluationException {
        BuiltInOp op = builtins.get(builtinName);
        if(op == null)
            throw new EvaluationException("Cannot find built in " + builtinName);
        return op;
    }

    private class BuiltInOp {
        public Method _method;
        public int _expectedParams;
        public BuiltInOp(Method method, int expectedParams) {
            _method = method;
            _expectedParams = expectedParams;
        }

        public TreeNode invoke(Evaluator evaluator, TreeNode exp, Map<String, TreeNode> alist) throws EvaluationException {
            String builtinName = exp.leftChild().getAtom().getLexme();
            try {
                return (TreeNode)_method.invoke(null, evaluator, builtinName, exp.rightChild(), alist);
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
