package uk.co.codecritical.asrs.common.dql.executor;

import uk.co.codecritical.asrs.common.dql.interfaces.DqlExecutorListener;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;
import uk.co.codecritical.asrs.common.dql.parser.DqlExceptionType;
import uk.co.codecritical.asrs.common.dql.parser.DqlException;
import uk.co.codecritical.asrs.common.dql.parser.KeyWord;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;
import uk.co.codecritical.asrs.common.dql.parser.TokensToPredicates;

import java.util.function.Predicate;

public class DqlExecutor {
    static final Predicate<ToteDql> ALL_TOTES = toteDql -> true;
    static final Predicate<StationDql> ALL_STATIONS = toteDql -> true;

    private final DqlExecutorListener dqlExecutorListener;

    public DqlExecutor(DqlExecutorListener dqlExecutorListener) {
        this.dqlExecutorListener = dqlExecutorListener;
    }

    public DqlQuery execute(String query) {
        DqlQuery dqlQuery = DqlQuery.create();
        try {
            dqlQuery = dqlQuery.mutate().setQuery(query).build();

            var words = Tokeniser.queryToStrings(query);
            var tokens = Tokeniser.stringsToTokens(words);

            Tokeniser.assertLegality(tokens);

            TokensToPredicates tokensToPredicates = TokensToPredicates.parse(tokens);

            switch (tokensToPredicates.getKeyWord()) {
                case RETRIEVE -> {
                    dqlQuery = dqlExecutorListener.toteRetrievalDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(KeyWord.RETRIEVE).orElse(ALL_TOTES),
                            tokensToPredicates.getStationPredicate(KeyWord.TO).orElse(ALL_STATIONS),
                            tokensToPredicates.getAssignment());
                }
                case STORE -> {
                    dqlQuery = dqlExecutorListener.toteStorageDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(KeyWord.STORE).orElse(ALL_TOTES),
                            tokensToPredicates.getAssignment());
                }
                case RELEASE -> {
                    dqlQuery = dqlExecutorListener.toteReleaseDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(KeyWord.RELEASE).orElse(ALL_TOTES),
                            tokensToPredicates.getAssignment());
                }
                case PICK -> {
                    var pickOutOf = tokensToPredicates.getTotePredicate(KeyWord.OUT_OF);
                    var pickInTo = tokensToPredicates.getTotePredicate(KeyWord.INTO);
                    if (pickOutOf.isEmpty() && pickInTo.isEmpty()) {
                        throw new DqlException(
                                DqlExceptionType.UNEXPECTED_SYNTAX,
                                "PICK query must have either OUTOF, INTO, or both for PIG.  This query has neither.");
                    } else if (pickOutOf.isEmpty()) {
                        dqlQuery = dqlExecutorListener.pickIntoDql(
                                dqlQuery,
                                pickInTo.get(),
                                tokensToPredicates.getAssignment());
                    } else if (pickInTo.isEmpty()) {
                        dqlQuery = dqlExecutorListener.pickOutOfDql(
                                dqlQuery,
                                pickOutOf.get(),
                                tokensToPredicates.getAssignment());
                    } else {
                        dqlQuery = dqlExecutorListener.pickToteToToteDql(
                                dqlQuery,
                                pickOutOf.get(),
                                pickInTo.get(),
                                tokensToPredicates.getAssignment());
                    }
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
