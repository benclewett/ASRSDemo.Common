package uk.co.codecritical.asrs.common.dql.executor;

import uk.co.codecritical.asrs.common.dql.entity.DqlGrid;
import uk.co.codecritical.asrs.common.dql.parser.ExceptionType;
import uk.co.codecritical.asrs.common.dql.parser.QueryParserException;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;
import uk.co.codecritical.asrs.common.dql.parser.TokensToPredicates;

public class DqlExecutor {
    private final DqlGrid dqlGrid;

    public DqlExecutor(DqlGrid grid) {
        this.dqlGrid = grid;
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
                    var totes = tokensToPredicates.getTotePredicate();
                    var stations = tokensToPredicates.getStationPredicate();
                    dqlGrid.binToStation(totes, stations);
                }
                default -> throw new QueryParserException(
                        ExceptionType.UNSUPPORTED,
                        "Unsupported keyword: " + tokensToPredicates.getKeyWord());
            }

            dqlQuery = dqlQuery.mutate().setQueryResponse(DqlQuery.QueryResponse.OK).build();
        } catch (QueryParserException ex) {
            dqlQuery = dqlQuery.mutate()
                    .setErrorMessage(ex.getMessage())
                    .setQueryResponse(DqlQuery.QueryResponse.FAIL)
                    .build();
        }
        return dqlQuery;
    }
}
