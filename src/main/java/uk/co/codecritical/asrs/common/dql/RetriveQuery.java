package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;

public class RetriveQuery extends Query {
    public final Statement statement;

    public RetriveQuery(Keyword keyword, ImmutableList<String> tokens) {
        super(keyword);
        this.statement = parse(tokens);
    }

    private Statement parse(ImmutableList<String> tokens) {
        Statement.Builder statementBuilder = Statement.builder();

        StatementPart.Builder statementPartBuilder = StatementPart.builder(this.keyword);
        for (String token : tokens) {
            statementPartBuilder.addCondition(new ConditionSingle(token));
        }
        statementBuilder.addStatementPart(statementPartBuilder.build());

        return statementBuilder.build();
    }
}
