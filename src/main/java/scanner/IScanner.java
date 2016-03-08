package scanner;

import java.io.IOException;

public interface IScanner {
    public char peek();
    public char consume() throws IOException;
    public boolean isEOF();
}
