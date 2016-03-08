package scanner;

import ast.Token;

public interface ITokenizer {
    Token getCurrent();
    Token peekCurrent();
}
