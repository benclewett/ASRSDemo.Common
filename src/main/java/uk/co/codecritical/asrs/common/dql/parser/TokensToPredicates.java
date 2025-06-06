package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;

import java.util.Optional;
import java.util.function.Predicate;

public class TokensToPredicates {
    private final ImmutableList<Token> tokens;

    private Optional<Predicate<ToteDql>> totePredicate = Optional.empty();
    private Optional<Predicate<StationDql>> stationPredicate = Optional.empty();
    private final ImmutableList<Assignment> assignments;

    public TokensToPredicates(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        ImmutableSet<Axiom> axioms = findAxioms();
        axioms.forEach(this::buildPredicate);

        assignments = findAssignments();
    }

    private void buildPredicate(Axiom axiom) {
        switch (axiom.entityWord) {
            case TOTE -> addPredicateToteId(axiom.value, axiom.comparison, axiom.preLogic);
            case PROPERTY -> addPredicateToteProperty(axiom.value, axiom.comparison, axiom.preLogic);
            case STATION -> addPredicateStationId(axiom.value, axiom.comparison, axiom.preLogic);
            case CAPABILITY -> addPredicateStationCapability(axiom.value, axiom.comparison, axiom.preLogic);
            default ->
                throw new DqlException(
                        DqlExceptionType.UNSUPPORTED,
                        "Entity '%s' is currently unsupported.".formatted(axiom.entityWord.name()));
        }
    }

    private void addPredicateStationCapability(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        Predicate<StationDql> p = switch (comparisonWord) {
            case EQUAL -> (value.isEmpty())
                    ? station -> station.getCapability().isEmpty()
                    : station -> station.getCapability().map(c -> station.filter(value).equals(c)).orElse(false);
            case NOT_EQUAL -> (value.isEmpty())
                    ? station -> station.getCapability().isPresent()
                    : station -> !station.getCapability().map(c -> station.filter(value).equals(c)).orElse(false);
        };
        stationPredicate = switch (preLogic.orElse(LogicalWord.AND)) {
            case AND -> stationPredicate.map(predicate -> predicate.and(p))
                    .or(() -> Optional.of(p));
            case OR -> stationPredicate.map(predicate -> predicate.or(p))
                    .or(() -> Optional.of(p));
            case NOT -> throw new DqlException(
                    DqlExceptionType.UNSUPPORTED,
                    "Logic 'NOT' is currently unsupported.");
        };
    }

    private void addPredicateStationId(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<StationDql> p = switch (comparisonWord) {
                case EQUAL -> station -> station.getId() == valueI;
                case NOT_EQUAL -> station -> station.getId() != valueI;
            };
            stationPredicate = switch (preLogic.orElse(LogicalWord.AND)) {
                case AND -> stationPredicate.map(predicate -> predicate.and(p))
                        .or(() -> Optional.of(p));
                case OR -> stationPredicate.map(predicate -> predicate.or(p))
                        .or(() -> Optional.of(p));
                case NOT -> throw new DqlException(
                        DqlExceptionType.UNSUPPORTED,
                        "Logic 'NOT' is currently unsupported.");
            };
        } catch (NumberFormatException ignore) {
            throw new DqlException(
                    DqlExceptionType.BAD_INTEGER,
                    "Value '%s' is not an integer.".formatted(value));
        }
    }

    private void addPredicateToteProperty(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        Predicate<ToteDql> p = switch (comparisonWord) {
            case EQUAL -> (value.isEmpty())
                    ? tote -> tote.getProperties().isEmpty()
                    : tote -> tote.getProperties().contains(tote.filter(value));
            case NOT_EQUAL -> (value.isEmpty())
                    ? tote -> !tote.getProperties().isEmpty()
                    : tote -> !tote.getProperties().contains(tote.filter(value));
        };
        totePredicate = switch (preLogic.orElse(LogicalWord.AND)) {
            case AND -> totePredicate.map(predicate -> predicate.and(p))
                    .or(() -> Optional.of(p));
            case OR -> totePredicate.map(predicate -> predicate.or(p))
                    .or(() -> Optional.of(p));
            case NOT -> throw new DqlException(
                    DqlExceptionType.UNSUPPORTED,
                    "Logic 'NOT' is currently unsupported.");
        };
    }

    private void addPredicateToteId(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<ToteDql> p = switch (comparisonWord) {
                case EQUAL -> tote -> tote.getId() == valueI;
                case NOT_EQUAL -> tote -> tote.getId() != valueI;
            };
            totePredicate = switch (preLogic.orElse(LogicalWord.AND)) {
                case AND -> totePredicate.map(predicate -> predicate.and(p))
                        .or(() -> Optional.of(p));
                case OR -> totePredicate.map(predicate -> predicate.or(p))
                        .or(() -> Optional.of(p));
                case NOT -> throw new DqlException(
                        DqlExceptionType.UNSUPPORTED,
                        "Logic 'NOT' is currently unsupported.");
            };
        } catch (NumberFormatException ignore) {
            throw new DqlException(
                    DqlExceptionType.BAD_INTEGER,
                    "Value '%s' is not an integer.".formatted(value));
        }
    }

    //region Axioms

    private record Axiom (Optional<LogicalWord> preLogic, EntityWord entityWord, ComparisonWord comparison, String value) {}

    private ImmutableSet<Axiom> findAxioms() {
        ImmutableSet.Builder<Axiom> builder = ImmutableSet.builder();
        for (int i = 1; i < tokens.size() - 2; i++) {
            if (Token.TokenType.ENTITY.equals(tokens.get(i).tokenType)
                && Token.TokenType.COMPARISON.equals(tokens.get(i + 1).tokenType)
                && Token.TokenType.VALUE.equals(tokens.get(i + 2).tokenType)) {
                Optional<LogicalWord> logic = (i > 1)
                        ? LogicalWord.mapFromString(tokens.get(i - 1).word)
                        : Optional.empty();
                builder.add(new Axiom(
                        logic,
                        EntityWord.mapFromString(tokens.get(i).word).orElseThrow(),
                        ComparisonWord.mapFromString(tokens.get(i + 1).word).orElseThrow(),
                        tokens.get(i + 2).word));
            }
        }
        return builder.build();
    }

    public KeyWord getKeyWord() {
        return KeyWord.mapFromString(tokens.get(0).word).orElseThrow();
    }

    //endregion

    //region Assignments

    private ImmutableList<Assignment> findAssignments() {
        ImmutableList.Builder<Assignment> builder = ImmutableList.builder();
        for (int i = 1; i < tokens.size() - 2; i++) {
            if (Token.TokenType.METRIC_NAME.equals(tokens.get(i).tokenType)
                    && Token.TokenType.ASSIGN.equals(tokens.get(i + 1).tokenType)
                    && Token.TokenType.METRIC_VALUE.equals(tokens.get(i + 2).tokenType)) {
                builder.add(new Assignment(
                        tokens.get(i).word,
                        tokens.get(i + 2).word));
            }
        }
        return builder.build();
    }

    //endregion

    //region Output

    static final Predicate<ToteDql> ALL_TOES = toteDql -> true;
    static final Predicate<StationDql> ALL_STATIONS = toteDql -> true;

    public Predicate<ToteDql> getTotePredicate() {
        return totePredicate.orElse(ALL_TOES);
    }

    public Predicate<StationDql> getStationPredicate() {
        return stationPredicate.orElse(ALL_STATIONS);
    }

    public ImmutableList<Assignment> getAssignment() {
        return assignments;
    }

    //endregion

}
