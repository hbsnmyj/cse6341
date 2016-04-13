package type;

/**
 * Created by snmyj on 4/12/16.
 */
public class BoolType extends Type {
    @Override
    public boolean equals(Object o) {
        return o instanceof BoolType;
    }
}
