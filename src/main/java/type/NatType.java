package type;

/**
 * Created by snmyj on 4/12/16.
 */
public class NatType extends Type {
    @Override
    public boolean equals(Object o) {
        if(o instanceof NatType)
            return true;
        else
            return false;
    }
}
