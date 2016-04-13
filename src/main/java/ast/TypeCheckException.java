package ast;

/**
 * Created by snmyj on 4/12/16.
 */
public class TypeCheckException extends Exception {
    public TypeCheckException(String val) {
        super("ERROR: TYPE " + val);
    }
}
