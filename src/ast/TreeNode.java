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

    boolean isAtom() {
        return _leftChild == null && _rightChild == null;
    }

    @Override
    public String toString() {
        if(isAtom()) return _token.getLexme();
        else
            return "(" + _leftChild.toString() + " . " + _rightChild.toString() + ")";
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
}
