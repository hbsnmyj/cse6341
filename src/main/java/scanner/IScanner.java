package scanner;

import java.io.IOException;

/**
 * Created by Haoyuan on 1/15/2016.
 */
public interface IScanner {
    public char peek();
    public char consume() throws IOException;
    public boolean isEOF();
}
