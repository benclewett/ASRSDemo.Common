package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.interfaces.StationDql;
import uk.co.codecritical.asrs.common.dql.interfaces.ToteDql;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class TokensToPredicates {
    private final ImmutableList<Token> tokens;

    private final Map<WordKey, KeyWordPredicates> predicates = new HashMap<>();

    private final ImmutableList<Assignment> assignments;

    public static TokensToPredicates parse(ImmutableList<Token> tokens) {
        return new TokensToPredicates(tokens);
    }

    private TokensToPredicates(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        ImmutableSet<Axiom> axioms = findAxioms();
        axioms.forEach(this::buildPredicate);

        assignments = findAssignments();
    }

    private void buildPredicate(Axiom axiom) {

        KeyWordPredicates keyWordPredicates = predicates.computeIfAbsent(
                axiom.wordKey,
                KeyWordPredicates::new
        );

        switch (axiom.wordEntity) {
            case TOTE -> addPredicateToteId(keyWordPredicates, axiom.value, axiom.comparison, axiom.preLogic);
            case PROPERTY -> addPredicateToteProperty(keyWordPredicates, axiom.value, axiom.comparison, axiom.preLogic);
            case STATION -> addPredicateStationId(keyWordPredicates, axiom.value, axiom.comparison, axiom.preLogic);
            case CAPABILITY -> addPredicateStationCapability(keyWordPredicates, axiom.value, axiom.comparison, axiom.preLogic);
            default ->
                throw new DqlException(
                        DqlExceptionType.UNSUPPORTED,
                        "Entity '%s' is currently unsupported.".formatted(axiom.wordEntity.name()));
        }
    }

    private void addPredicateStationCapability(KeyWordPredicates keyP, String value, WordComparison wordComparison, Optional<WordLogical> preLogic) {
        Predicate<StationDql> p = switch (wordComparison) {
            case EQUAL -> (value.isEmpty())
                    ? station -> station.getCapability().isEmpty()
                    : station -> station.getCapability().map(c -> station.filter(value).equals(c)).orElse(false);
            case NOT_EQUAL -> (value.isEmpty())
                    ? station -> station.getCapability().isPresent()
                    : station -> !station.getCapability().map(c -> station.filter(value).equals(c)).orElse(false);
        };
        keyP.stationP = switch (preLogic.orElse(WordLogical.AND)) {
            case AND -> keyP.stationP.map(predicate -> predicate.and(p))
                    .or(() -> Optional.of(p));
            case OR -> keyP.stationP.map(predicate -> predicate.or(p))
                    .or(() -> Optional.of(p));
            case NOT -> throw new DqlException(
                    DqlExceptionType.UNSUPPORTED,
                    "Logic 'NOT' is currently unsupported.");
        };
    }

    private void addPredicateStationId(KeyWordPredicates keyP, String value, WordComparison wordComparison, Optional<WordLogical> preLogic) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<StationDql> p = switch (wordComparison) {
                case EQUAL -> station -> station.getId() == valueI;
                case NOT_EQUAL -> station -> station.getId() != valueI;
            };
            keyP.stationP = switch (preLogic.orElse(WordLogical.AND)) {
                case AND -> keyP.stationP.map(predicate -> predicate.and(p))
                        .or(() -> Optional.of(p));
                case OR -> keyP.stationP.map(predicate -> predicate.or(p))
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

    private void addPredicateToteProperty(KeyWordPredicates keyP, String value, WordComparison wordComparison, Optional<WordLogical> preLogic) {
        Predicate<ToteDql> p = switch (wordComparison) {
            case EQUAL -> (value.isEmpty())
                    ? tote -> tote.getProperties().isEmpty()
                    : tote -> tote.getProperties().contains(tote.filter(value));
            case NOT_EQUAL -> (value.isEmpty())
                    ? tote -> !tote.getProperties().isEmpty()
                    : tote -> !tote.getProperties().contains(tote.filter(value));
        };
        keyP.toteP = switch (preLogic.orElse(WordLogical.AND)) {
            case AND -> keyP.toteP.map(predicate -> predicate.and(p))
                    .or(() -> Optional.of(p));
            case OR -> keyP.toteP.map(predicate -> predicate.or(p))
                    .or(() -> Optional.of(p));
            case NOT -> throw new DqlException(
                    DqlExceptionType.UNSUPPORTED,
                    "Logic 'NOT' is currently unsupported.");
        };
    }

    private void addPredicateToteId(KeyWordPredicates keyP, String value, WordComparison wordComparison, Optional<WordLogical> preLogic) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<ToteDql> p = switch (wordComparison) {
                case EQUAL -> tote -> tote.getId() == valueI;
                case NOT_EQUAL -> tote -> tote.getId() != valueI;
            };
            keyP.toteP = switch (preLogic.orElse(WordLogical.AND)) {
                case AND -> keyP.toteP.map(predicate -> predicate.and(p))
                        .or(() -> Optional.of(p));
                case OR -> keyP.toteP.map(predicate -> predicate.or(p))
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

    private record Axiom (WordKey wordKey, Optional<WordLogical> preLogic, WordEntity wordEntity, WordComparison comparison, String value) {}

    private ImmutableSet<Axiom> findAxioms() {
        ImmutableSet.Builder<Axiom> builder = ImmutableSet.builder();
        WordKey wordKey = WordKey.NONE;
        for (int i = 0; i < tokens.size() - 2; i++) {
            wordKey = mapTokenToAxiom(wordKey, i, builder);
        }
        return builder.build();
    }

    private WordKey mapTokenToAxiom(WordKey wordKey, int i, ImmutableSet.Builder<Axiom> builder) {
        var tokenType = tokens.get(i).tokenType;

        if (Token.TokenType.KEYWORD.equals(tokenType)) {
            return WordKey.mapFromString(tokens.get(i).word).orElseThrow();
        }

        if (Token.TokenType.AXIOM_ENTITY.equals(tokens.get(i).tokenType)
            && Token.TokenType.COMPARISON.equals(tokens.get(i + 1).tokenType)
            && Token.TokenType.VALUE.equals(tokens.get(i + 2).tokenType)) {

            Optional<WordLogical> logic = (i > 1)
                    ? WordLogical.mapFromString(tokens.get(i - 1).word)
                    : Optional.empty();

            builder.add(new Axiom(
                    wordKey,
                    logic,
                    WordEntity.mapFromString(tokens.get(i).word).orElseThrow(),
                    WordComparison.mapFromString(tokens.get(i + 1).word).orElseThrow(),
                    tokens.get(i + 2).word));
        }

        return wordKey;
    }

    //endregion

    public WordKey getKeyWord() {
        return WordKey.mapFromString(tokens.get(0).word).orElseThrow();
    }

    //region Assignments

    private ImmutableList<Assignment> findAssignments() {
        ImmutableList.Builder<Assignment> builder = ImmutableList.builder();
        for (int i = 1; i < tokens.size() - 2; i++) {
            if (Token.TokenType.METRIC_NAME.equals(tokens.get(i).tokenType)
                    && Token.TokenType.ASSIGN.equals(tokens.get(i + 1).tokenType)
                    && Token.TokenType.METRIC_VALUE.equals(tokens.get(i + 2).tokenType)) {
                builder.add(new Assignment(
                        tokens.get(i).word.toUpperCase(),
                        tokens.get(i + 2).word));
            }
        }
        return builder.build();
    }

    //endregion

    //region Output

    public Optional<Predicate<ToteDql>> getTotePredicate(WordKey wordKey) {
        return Optional.ofNullable(predicates.get(wordKey))
                .flatMap(p -> p.toteP);
    }

    public Optional<Predicate<StationDql>> getStationPredicate(WordKey wordKey) {
        return Optional.ofNullable(predicates.get(wordKey))
                .flatMap(p -> p.stationP);
    }

    public ImmutableList<Assignment> getAssignment() {
        return assignments;
    }

    //endregion

    static class KeyWordPredicates {
        final WordKey keyword;
        public KeyWordPredicates(WordKey keyword) {
            this.keyword = keyword;
        }
        public Optional<Predicate<ToteDql>> toteP = Optional.empty();
        public Optional<Predicate<StationDql>> stationP = Optional.empty();

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("wordKey", keyword)
                    .add("toteDqlPredicate", toteP.isPresent())
                    .add("stationDqlPredicate", stationP.isPresent())
                    .toString();
        }
    }

}
