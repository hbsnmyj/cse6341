package parser;

import ast.Token;
import ast.TreeNode;
import scanner.ITokenizer;
import scanner.StringScanner;
import scanner.Tokenizer;
import scanner.TokenizerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static final String ERROR_LIST_NOT_COMPLETE = "ERROR: List not complete, expected Atom, List or Right Parenthesis, but found: ";
    public static final String ERROR_EXCEPT_ATOM_OR_LIST = "ERROR: Excepting Atom or List, but found: ";
    private ITokenizer _tokenizer;

    public Parser(ITokenizer tokenizer) {
        _tokenizer = tokenizer;
    }

    public List<TreeNode> getExpressions() throws ParsingException {
        return Start();
    }

    public TreeNode getNextExcepression() throws ParsingException {
        return Expr();
    }

    public boolean hasNext() throws ParsingException {
        return _tokenizer.peekCurrent().getTokenKind() != Token.TokenKind.EOF;
    }

    private List<TreeNode> Start() throws ParsingException {
        List<TreeNode> result = new ArrayList<>();
        do {
            result.add(Expr());
        } while(hasNext());
        return result;
    }

    private TreeNode Expr() throws ParsingException {
        Token nextToken = _tokenizer.peekCurrent();
        switch(nextToken.getTokenKind()) {
            case LiteralAtom:
            case NumericAtom:
                return Atom();
            case LPar:
                _tokenizer.getCurrent();
                return List();
            default:
                throw new ParsingException(ERROR_EXCEPT_ATOM_OR_LIST + nextToken);
        }
    }

    private TreeNode Atom() throws ParsingException {
        return new TreeNode(_tokenizer.getCurrent());
    }

    private TreeNode List() throws ParsingException {
        TreeNode result = new TreeNode(new Token(Token.TokenKind.LiteralAtom, "NIL"));
        TreeNode tail = result;
        while(_tokenizer.peekCurrent().getTokenKind() != Token.TokenKind.RPar) { // to avoid stack overflow
            try {
                tail.setLeftChild(Expr());
                tail.setRightChild(Token.NIL_TOKEN);
                tail.setAsTreeNode();
                tail = tail.rightChild();
            } catch (ParsingException pe)  {
                throw new ParsingException(ERROR_LIST_NOT_COMPLETE + _tokenizer.peekCurrent());
            }
        }
        _tokenizer.getCurrent();
        return result;
    }

    public static List<TreeNode> parseString(String input) throws ParsingException {
        TokenizerAdapter tokenizer;
        tokenizer = new TokenizerAdapter(new StringScanner(input), new Tokenizer());
        Parser parser = new Parser(tokenizer);
        return parser.getExpressions();
    }
}
