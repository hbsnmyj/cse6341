package ast;

/*
 * A token from the lexer
 */
public class Token {
    public static final Token ERROR_TOKEN = new Token(TokenKind.Error, "");
    public static final Token EOF_TOKEN = new Token(TokenKind.EOF, "");
    public static final Token NIL_TOKEN = new Token(TokenKind.LiteralAtom, "NIL");
    public static final Token T_TOKEN = new Token(TokenKind.LiteralAtom, "T");
    public static final Token F_TOKEN = new Token(TokenKind.LiteralAtom, "F");

    public String toPrintString() {
        switch(_kind) {
            case LiteralAtom:
            case NumericAtom:
                return _lexme;
            case LPar:
                return "'('";
            case RPar:
                return "')'";
            case EOF:
                return "EOF";
            case Error:
                return "Invalid Token " + _lexme;
        }
        return "<Wrong Token>" + _lexme;    }

    public int toInteger() throws EvaluationException {
        if(!_kind.equals(TokenKind.NumericAtom))
            throw new EvaluationException(this.toString() + " is not a numeric atom.");
        return Integer.parseInt(_lexme);
    }

    public enum TokenKind {
        LPar, RPar, LiteralAtom, NumericAtom, EOF, Error
    }

    @Override
    public String toString() {
        switch(_kind) {
            case LiteralAtom:
                return "<Literal>" + _lexme;
            case NumericAtom:
                return "<Number>" + _lexme;
            case LPar:
                return "'('";
            case RPar:
                return "')'";
            case EOF:
                return "EOF";
            case Error:
                return "Invalid Token " + _lexme;
        }
        return "<Wrong Token>" + _lexme;
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
            if(_kind == TokenKind.LiteralAtom || _kind == TokenKind.NumericAtom) {
                return _lexme.equals(((Token) other)._lexme);
            } else {
                return true;
            }
        } else
            return false;
    }

    public static Token Atom(String s) { return new Token(TokenKind.LiteralAtom, s);}
    public static Token Atom(Integer d) { return new Token(TokenKind.NumericAtom, d.toString());}

    private TokenKind _kind;
    private String _lexme;

}
