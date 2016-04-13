package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by haoyu on 1/23/2016.
 */
public class TreeNode {
    private TreeNode _leftChild;
    private TreeNode _rightChild;
    private Token _token;

    public TreeNode(TreeNode left, TreeNode right) {
        _token = null;
        _leftChild = left;
        _rightChild = right;
    }

    public TreeNode(Token token) {
        _leftChild = null;
        _rightChild = null;
        _token = token;
    }

    public TreeNode(Token left, TreeNode right) {
        _leftChild = new TreeNode(left);
        _rightChild = right;
        _token = null;
    }

    public TreeNode(TreeNode left, Token right) {
        _leftChild = left;
        _rightChild = new TreeNode(right);
        _token = null;
    }

    public TreeNode(Token left, Token right) {
        _leftChild = new TreeNode(left);
        _rightChild = new TreeNode(right);
        _token = null;
    }

    public static TreeNode Cons(TreeNode left, Token right) {
        return new TreeNode(left, right);
    }
    public static TreeNode Cons(Token left, TreeNode right) {
        return new TreeNode(left, right);
    }

    public static TreeNode Cons(Token left, Token right) {
        return new TreeNode(left, new TreeNode(right));
    }

    public static TreeNode Cons(TreeNode left, TreeNode right) {
        return new TreeNode(left, right);
    }

    public static TreeNode TokenNode(Token left) {
        return new TreeNode(left);
    }
    boolean isAtom() {
        return _leftChild == null && _rightChild == null;
    }

    @Override
    public String toString() {
        if(isAtom()) return _token.getLexme();
        else
            return "(" + _leftChild.toString() + " . " + _rightChild.toString() + ")";
    }

    public String toListNotation() {
        if(isAtom()) return _token.toPrintString();
        else {
            List<TreeNode> elements = retrieveElements();
            String result = "(";
            result += String.join(" ",
                    elements.subList(0,elements.size() - 1).stream().map(TreeNode::toListNotation).collect(Collectors.toList()));
            if(!elements.get(elements.size() - 1).isNil()) {
                result += " . ";
                result += elements.get(elements.size() - 1).toListNotation();
            }
            result += ")";
            return result;
        }
    }

    private List<TreeNode> retrieveElements() {
        TreeNode now = this;
        List<TreeNode> result = new ArrayList<>();
        while(!now.isAtom()) {
            result.add(now._leftChild);
            now = now._rightChild;
        }
        result.add(now);
        return result;
    }

    public boolean isNil() {
        return this.isAtom() && this._token.equals(Token.NIL_TOKEN);
    }

    public List<TreeNode> retrieveListElements() {
        TreeNode now = this;
        List<TreeNode> result = new ArrayList<>();
        while(!now.isAtom()) {
            result.add(now._leftChild);
            now = now._rightChild;
        }
        return result;
    }

    public void setLeftChild(TreeNode leftChild) {
        _leftChild = leftChild;
    }

    public void setLeftChild(Token leftChild) {
        _leftChild = new TreeNode(leftChild);
    }

    public TreeNode leftChild() {
        return _leftChild;
    }

    public TreeNode rightChild() {
        return _rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        _rightChild = rightChild;
    }
    public void setRightChild(Token rightChild) {
        _rightChild = new TreeNode(rightChild);
    }

    public boolean isNumeric() {
        return this.isAtom() && _token.getTokenKind().equals(Token.TokenKind.NumericAtom);
    }

    public boolean isTrue() {
        return this.isAtom() && _token.equals(Token.T_TOKEN);
    }

    public boolean isFalse() {
        return this.isAtom() && _token.equals(Token.F_TOKEN);
    }

    public boolean isList() {
        TreeNode now = this;
        while(!now.isAtom()) now = now._rightChild;
        return now.isNil();
    }

    public boolean isLiteral() {
        return this.isAtom() && _token.getTokenKind().equals(Token.TokenKind.LiteralAtom);
    }

    public Token getAtom() {
        return _token;
    }

    public int getListLength() {
        TreeNode now = this;
        int result = 0;
        while(!now.isAtom()) {
            now = now._rightChild;
            result += 1;
        }
        return result;
    }


    public static TreeNode BooleanNode(boolean b) {
        if(b)
            return new TreeNode(Token.T_TOKEN);
        else
            return new TreeNode(Token.F_TOKEN);
    }

    public void setAsTreeNode() {
        this._token = null;
    }

}
