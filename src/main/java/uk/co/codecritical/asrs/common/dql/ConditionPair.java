package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;

/**
 * <p>Name/value qualifier for a statement:</p>
 * <pre>RETRIEVE bin=123</pre>
 */
public class ConditionPair implements Condition {
    public final String name;
    public final String value;

    public ConditionPair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("value", value)
                .toString();
    }
}
