package scanner;

import java.io.IOException;

public class StringScanner implements IScanner {

    public StringScanner(String str) {
        _str = str;
        _pos = 0;
    }

    @Override
    public char peek() {
        return _str.charAt(_pos);
    }

    @Override
    public char consume() throws IOException {
        return _str.charAt(_pos++);
    }

    @Override
    public boolean isEOF() {
        if(_pos == _str.length())
            return true;
        else
            return false;
    }

    private String _str;
    private int _pos = 0;
}
