package uk.co.codecritical.asrs.common.dql.executor;

import uk.co.codecritical.asrs.common.dql.interfaces.ScriptFactoryDql;
import uk.co.codecritical.asrs.common.dql.parser.DqlExceptionType;
import uk.co.codecritical.asrs.common.dql.parser.DqlException;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;
import uk.co.codecritical.asrs.common.dql.parser.TokensToPredicates;

public class DqlExecutor {
    private final ScriptFactoryDql scriptFactoryDql;

    public DqlExecutor(ScriptFactoryDql grid) {
        this.scriptFactoryDql = grid;
    }

    public DqlQuery execute(String query) {
        DqlQuery dqlQuery = DqlQuery.create();
        try {
            dqlQuery = dqlQuery.mutate().setQuery(query).build();

            var words = Tokeniser.queryToStrings(query);
            var tokens = Tokeniser.stringsToTokens(words);

            Tokeniser.assertLegality(tokens);

            TokensToPredicates tokensToPredicates = new TokensToPredicates(tokens);

            switch (tokensToPredicates.getKeyWord()) {
                case RETRIEVE -> {
                    dqlQuery = scriptFactoryDql .toteRetrievalDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(),
                            tokensToPredicates.getStationPredicate(),
                            tokensToPredicates.getAssignment());
                }
                case STORE -> {
                    dqlQuery = scriptFactoryDql.toteStorageDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(),
                            tokensToPredicates.getAssignment());
                }
                default -> throw new DqlException(
                        DqlExceptionType.UNSUPPORTED,
                        "Unsupported keyword: " + tokensToPredicates.getKeyWord());
            }

            dqlQuery = dqlQuery.mutate().setQueryResponse(DqlQuery.QueryResponse.OK).build();
        } catch (DqlException ex) {
            dqlQuery = dqlQuery.mutate()
                    .setErrorMessage(ex.getMessage())
                    .setErrorType(ex.dqlExceptionType)
                    .setQueryResponse(DqlQuery.QueryResponse.FAIL)
                    .build();
        }
        return dqlQuery;
    }
}
