package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.Tote;

/**
 * <p>A collection of statement parts, like the two parts here:</p>
 * <pre>STORE bin=123 WITH property="EMPTY"</pre>
 */
public class Statement {
    public final ImmutableList<StatementPart> statementParts;
    public Keyword keyword;

    public Statement(ImmutableList<StatementPart> statementParts) {
        this.statementParts = statementParts;
        this.keyword = statementParts.get(0).keyword;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("statementParts", statementParts)
                .toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    //region builder

    public static class Builder {
        private final ImmutableList.Builder<StatementPart> builder = ImmutableList.builder();
        public Builder() {
        }
        public Builder addStatementPart(StatementPart statementPart) {
            builder.add(statementPart);
            return this;
        }
        public Statement build() {
            return new Statement(builder.build());
        }
    }

    //endregion
}
