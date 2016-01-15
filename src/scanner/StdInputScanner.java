package scanner;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PushbackInputStream;

/**
 * Created by Haoyuan on 1/15/2016.
 */
public class StdInputScanner implements IScanner {
    private static final int EOF_CHAR = -1;
    private static StdInputScanner _instance = null;
    private int _next = -1;

    public static StdInputScanner getStdInputScanner() throws IOException {
        if(_instance == null) {
            _instance = new StdInputScanner();
            _instance.consume();
        }
        return _instance;
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
