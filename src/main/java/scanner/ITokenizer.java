package scanner;

import ast.Token;

/**
 * Created by haoyu on 1/23/2016.
 */
public interface ITokenizer {
    Token getCurrent();
    Token peekCurrent();
}
