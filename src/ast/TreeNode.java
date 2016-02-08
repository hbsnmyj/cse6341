package ast;

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
        return null;
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

    TreeNode eval() {
        return null;
    }
}
