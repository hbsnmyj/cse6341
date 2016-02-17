package scanner;

import ast.Token;

public class TokenizerAdapter implements ITokenizer {
    IScanner _scanner;
    Tokenizer _tokenizer;
    Token _current;

    public TokenizerAdapter(IScanner scanner, Tokenizer tokenizer) {
        _current = tokenizer.getNextToken(scanner);
        _scanner = scanner;
        _tokenizer = tokenizer;
    }

    @Override
    public Token getCurrent() {
        Token value = _current;
        _current = _tokenizer.getNextToken(_scanner);
        return value;
    }

    @Override
    public Token peekCurrent() {
        return _current;
    }
}
