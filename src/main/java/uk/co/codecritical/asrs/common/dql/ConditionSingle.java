package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;

/**
  * <p>A single condition, like a property name:</p>
  * <pre>UPDATE property</pre>
  */
public class ConditionSingle implements Condition {
    public final String name;
    public ConditionSingle(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .toString();
    }
}
