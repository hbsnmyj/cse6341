package scanner;

import ast.Token;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.testng.Assert.assertEquals;


public class TokenizerTest {
    private static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenLiteral1() throws Exception {
        String test = readFile("test_resources/scanner/LiteralAtoms1", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "LITERALATOM1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "LITERALATOM2"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "LITERALATOM3"));
        assertEquals(tokenizer.getNextToken(scanner), Token.EOF_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenLiteral2() throws Exception {
        String test = readFile("test_resources/scanner/LiteralAtoms2", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "LITERALATOM1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "LITE3ATOM"));
        assertEquals(tokenizer.getNextToken(scanner), Token.EOF_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenLiteral3() throws Exception {
        String test = readFile("test_resources/scanner/LiteralAtoms3", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "TEST1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "TEST"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LPar, "("));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.RPar, ")"));
        assertEquals(tokenizer.getNextToken(scanner), Token.ERROR_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenLiteral4() throws Exception {
        String test = readFile("test_resources/scanner/LiteralAtoms4", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "TEST1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "TEST"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LPar, "("));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.RPar, ")"));
        assertEquals(tokenizer.getNextToken(scanner), Token.EOF_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenNumeric1() throws Exception {
        String test = readFile("test_resources/scanner/NumericAtom1", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "2"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "3"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "4"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "100"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "5000000"));
        assertEquals(tokenizer.getNextToken(scanner), Token.EOF_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenNumeric2() throws Exception {
        String test = readFile("test_resources/scanner/NumericAtom2", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "1"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "2"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "3"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "4"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "500"));
        assertEquals(tokenizer.getNextToken(scanner), Token.EOF_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenIllegal1() throws Exception {
        String test = readFile("test_resources/scanner/IllegalTest1", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), Token.ERROR_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenIllegal2() throws Exception {
        String test = readFile("test_resources/scanner/IllegalTest1", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), Token.ERROR_TOKEN);
    }

    @org.testng.annotations.Test
    public void testGetNextTokenOffical1() throws Exception {
        String test = readFile("test_resources/scanner/OfficialTest1", Charset.defaultCharset());
        StringScanner scanner = new StringScanner(test);
        Tokenizer tokenizer = new Tokenizer();
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LPar, "("));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "DEFUN"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "F23"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LPar, "("));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "X"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.RPar, ")"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LPar, "("));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "PLUS"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.LiteralAtom, "X"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "12"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.NumericAtom, "55"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.RPar, ")"));
        assertEquals(tokenizer.getNextToken(scanner), new Token(Token.TokenKind.RPar, ")"));

    }
}