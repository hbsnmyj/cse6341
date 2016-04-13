package type;

/**
 * Created by snmyj on 4/12/16.
 */
public class Type {
    public boolean isNat() {
        return (this instanceof NatType);
    }

    public boolean isBool() {
        return (this instanceof BoolType);
    }

    public boolean isNatList() {
        return (this.equals(new ListType(new NatType())));
    }

    public boolean isT() {
        return isNat() || isBool() || isNatList();
    }
}
