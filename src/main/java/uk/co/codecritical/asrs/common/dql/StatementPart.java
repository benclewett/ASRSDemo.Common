package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

/**
 * <p>Parts of a statement.</P>
 * <p>For instance, two parts here:</p>
 * <pre>STORE bin=123 WITH property="EMPTY"</pre>
 */
public class StatementPart {
    public final Keyword keyword;
    public final ImmutableList<Condition> conditions;

    private StatementPart(Keyword keyword, ImmutableList<Condition> conditions) {
        this.keyword = keyword;
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("keyword", keyword)
                .add("conditions", conditions)
                .toString();
    }

    public static StatementPart.Builder builder(Keyword keyword) {
        return new Builder(keyword);
    }

    public static class Builder {
        private final Keyword keyword;
        private final ImmutableList.Builder<Condition> builder = ImmutableList.builder();
        public Builder(Keyword keyword) {
            this.keyword = keyword;
        }
        public Builder addCondition(Condition condition) {
            builder.add(condition);
            return this;
        }
        public StatementPart build() {
            return new StatementPart(keyword, builder.build());
        }
    }
}
