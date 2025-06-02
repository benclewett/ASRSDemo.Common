package uk.co.codecritical.asrs.common.dql;

public abstract class Query {
    public final Keyword keyword;
    protected Query(Keyword statement) {
        this.keyword = statement;
    }
}
