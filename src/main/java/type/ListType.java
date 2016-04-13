package type;

/**
 * Created by snmyj on 4/12/16.
 */
public class ListType extends Type {
    public Type _subtype;
    public ListType(Type subtype) {
        _subtype = subtype;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ListType) {
            return _subtype.equals(((ListType) o)._subtype);
        } else {
            return false;
        }
    }
}
