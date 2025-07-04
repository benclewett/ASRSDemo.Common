package uk.co.codecritical.asrs.common.dql.executor;

import uk.co.codecritical.asrs.common.dql.interfaces.DqlQueryHandler;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;
import uk.co.codecritical.asrs.common.dql.parser.DqlExceptionType;
import uk.co.codecritical.asrs.common.dql.parser.DqlException;
import uk.co.codecritical.asrs.common.dql.parser.Token;
import uk.co.codecritical.asrs.common.dql.parser.words.WordKey;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;
import uk.co.codecritical.asrs.common.dql.parser.TokensToPredicates;
import uk.co.codecritical.asrs.common.dql.parser.words.WordSelect;

import java.util.function.Predicate;

public class DqlExecutor {
    static final Predicate<ToteDql> ALL_TOTES = toteDql -> true;
    static final Predicate<StationDql> ALL_STATIONS = toteDql -> true;

    private final DqlQueryHandler dqlQueryHandler;

    public DqlExecutor(DqlQueryHandler dqlQueryHandler) {
        this.dqlQueryHandler = dqlQueryHandler;
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
                    dqlQuery = dqlQueryHandler.toteRetrievalDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(WordKey.RETRIEVE).orElse(ALL_TOTES),
                            tokensToPredicates.getStationPredicate(WordKey.TO).orElse(ALL_STATIONS),
                            tokensToPredicates.getAssignment());
                }
                case STORE -> {
                    dqlQuery = dqlQueryHandler.toteStorageDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(WordKey.STORE).orElse(ALL_TOTES),
                            tokensToPredicates.getAssignment());
                }
                case RELEASE -> {
                    dqlQuery = dqlQueryHandler.toteReleaseDql(
                            dqlQuery,
                            tokensToPredicates.getTotePredicate(WordKey.RELEASE).orElse(ALL_TOTES),
                            tokensToPredicates.getAssignment());
                }
                case PICK -> {
                    var pickOutOf = tokensToPredicates.getTotePredicate(WordKey.OUT_OF);
                    var pickInTo = tokensToPredicates.getTotePredicate(WordKey.INTO);
                    if (pickOutOf.isEmpty() && pickInTo.isEmpty()) {
                        throw new DqlException(
                                DqlExceptionType.UNEXPECTED_SYNTAX,
                                "PICK query must have either OUT_OF, INTO, or both for PIG.  This query has neither.");
                    } else if (pickOutOf.isEmpty()) {
                        dqlQuery = dqlQueryHandler.pickIntoDql(
                                dqlQuery,
                                pickInTo.get(),
                                tokensToPredicates.getAssignment());
                    } else if (pickInTo.isEmpty()) {
                        dqlQuery = dqlQueryHandler.pickOutOfDql(
                                dqlQuery,
                                pickOutOf.get(),
                                tokensToPredicates.getAssignment());
                    } else {
                        dqlQuery = dqlQueryHandler.pickToteToToteDql(
                                dqlQuery,
                                pickOutOf.get(),
                                pickInTo.get(),
                                tokensToPredicates.getAssignment());
                    }
                }
                case SELECT -> {
                    var selectEntity = tokens.stream()
                            .filter(t -> Token.TokenType.SELECT_ENTITY.equals(t.tokenType))
                            .findFirst();
                    if (selectEntity.isEmpty()) {
                        throw new DqlException(
                                DqlExceptionType.NOT_POSSIBLE,
                                "No SELECT entity requested.");
                    }
                    var wordSelect = WordSelect.mapFromString(selectEntity.get().word);
                    if (wordSelect.isEmpty()) {
                        throw new DqlException(
                                DqlExceptionType.UNSUPPORTED,
                                "Unsupported select entity: " + selectEntity.get());
                    }
                    dqlQuery = dqlQueryHandler.select(dqlQuery, wordSelect.get());
                }
                case UPDATE -> {
                    dqlQuery = dqlQueryHandler.updateSetDql(dqlQuery, tokensToPredicates.getAssignment());
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
