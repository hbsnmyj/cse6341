package scanner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

public class StdInputScanner implements IScanner {
    private static final int EOF_CHAR = -1;
    private int _next = -1;

    public static StdInputScanner getStdInputScanner() throws IOException {
        StdInputScanner instance = new StdInputScanner();
        instance.consume();
        return instance;
    }

    @Override
    public char peek() {
        return (char) _next;
    }

    @Override
    public char consume() throws IOException {
        int old = _next;
        _next = System.in.read();
        return (char)old;
    }

    @Override
    public boolean isEOF() {
        return _next == EOF_CHAR;
    }

}
