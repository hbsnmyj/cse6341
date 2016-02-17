package scanner;

import ast.Token;

import java.io.IOException;

/**
 * Created by Haoyuan on 1/14/2016.
 */
public class Tokenizer {
    public Token getNextToken(IScanner scanner) {
        try {
            if (!scanner.isEOF()) {
                char next = scanner.peek();
                while(!scanner.isEOF() && isWhiteSpace(next)) {
                    scanner.consume();
                    if(scanner.isEOF()) return Token.EOF_TOKEN;
                    next = scanner.peek();
                }
                if (next == '(') {
                    scanner.consume();
                    return new Token(Token.TokenKind.LPar, "");
                } else if (next == ')') {
                    scanner.consume();
                    return new Token(Token.TokenKind.RPar, "");
                } else
                    return consumeAtom(scanner);
            } else {
                return new Token(Token.TokenKind.EOF, "");
            }
        } catch (IOException e) {
            return new Token(Token.TokenKind.Error, "");
        }
    }

    private Token consumeAtom(IScanner scanner) {
        String token;
        try {
            token = readToken(scanner);
        } catch (IOException e) {
            return new Token(Token.TokenKind.Error, "");
        }
        if(Character.isUpperCase(token.charAt(0)))
            return readLiteralAtom(token);
        else if(Character.isDigit(token.charAt(0)))
            return readNumericAtom(token);
        else
            return new Token(Token.TokenKind.Error, token);
    }

    private Token readLiteralAtom(String token) {
        for(char s: token.toCharArray())
            if(!Character.isUpperCase(s) && !Character.isDigit(s))
                return new Token(Token.TokenKind.Error, token);
        return new Token(Token.TokenKind.LiteralAtom, token);
    }

    private Token readNumericAtom(String token) {
        for(char s: token.toCharArray())
            if(!Character.isDigit(s))
                return new Token(Token.TokenKind.Error, token);
        return new Token(Token.TokenKind.NumericAtom, token);
    }

    private String readToken(IScanner scanner) throws IOException {
        StringBuilder str = new StringBuilder("");
        while(!scanner.isEOF() && !isWhiteSpace(scanner.peek()) && scanner.peek() != '(' && scanner.peek() != ')') {
            str.append(scanner.consume());
        }
        return str.toString();
    }

    private boolean isWhiteSpace(char next) {
        return next == '\r' || next == '\n' || next == ' ';
    }


}
