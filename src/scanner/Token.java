package scanner;

/**
 * Created by Haoyuan on 1/15/2016.
 */
public class Token {
    public static final Token ERROR_TOKEN = new Token(TokenKind.Error, "");
    public static final Token EOF_TOKEN = new Token(TokenKind.EOF, "");

    public enum TokenKind {
        LPar, RPar, LiteralAtom, NumericAtom, EOF, Error
    }

    @Override
    public String toString() {
        return null;
    }

    public String getLexme() {
        return _lexme;
    }

    public TokenKind getTokenKind() {
        return _kind;
    }

    public Token(TokenKind kind, String lexme) {
        _kind = kind;
        _lexme = lexme;
    }

    @Override
    public boolean equals(Object other) {
        if(this.getClass() != other.getClass()) {
            return false;
        }
        if(_kind == ((Token)other)._kind) {
            if(_kind == TokenKind.LiteralAtom || _kind == TokenKind.NumericAtom)
                return _lexme.equals(((Token)other)._lexme);
            else
                return true;
        } else
            return false;
    }

    private TokenKind _kind;
    private String _lexme;

}
