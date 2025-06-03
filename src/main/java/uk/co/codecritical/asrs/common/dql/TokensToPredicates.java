package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.Tote;

import java.util.Optional;
import java.util.function.Predicate;

public class TokensToPredicates {
    private final ImmutableList<Token> tokens;

    private Optional<Predicate<Tote>> totePredicate = Optional.empty();

    public TokensToPredicates(ImmutableList<Token> tokens) {
        this.tokens = tokens;

        ImmutableSet<Axiom> axioms = getAxioms();
        axioms.forEach(this::getPredicate);
    }

    private void getPredicate(Axiom axiom) {
        switch (axiom.entityWord) {
            case TOTE -> addPredicateToteId(axiom.value, axiom.comparison, axiom.preLogic);
            case PROPERTY -> addPredicateToteProperty(axiom.value, axiom.comparison, axiom.preLogic);
            default ->
                throw new QueryParserException(
                        QueryParserException.ExceptionType.UNSUPPORTED,
                        "Entity '%s' is currently unsupported.".formatted(axiom.entityWord.name()));
        }
    }

    private void addPredicateToteProperty(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        Predicate<Tote> p = switch (comparisonWord) {
            case EQUAL -> (value.isEmpty())
                    ? tote -> tote.properties.isEmpty()
                    : tote -> tote.properties.isPresent(value);
            case NOT_EQUAL -> (value.isEmpty())
                    ? tote -> !tote.properties.isEmpty()
                    : tote -> !tote.properties.isPresent(value);
        };
        totePredicate = switch (preLogic.orElse(LogicalWord.AND)) {
            case AND -> totePredicate.map(predicate -> predicate.and(p))
                    .or(() -> Optional.of(p));
            case OR -> totePredicate.map(predicate -> predicate.or(p))
                    .or(() -> Optional.of(p));
            case NOT -> throw new QueryParserException(
                    QueryParserException.ExceptionType.UNSUPPORTED,
                    "Logic 'NOT' is currently unsupported.");
        };
    }

    private void addPredicateToteId(String value, ComparisonWord comparisonWord, Optional<LogicalWord> preLogic) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<Tote> p = switch (comparisonWord) {
                case EQUAL -> tote -> tote.id == valueI;
                case NOT_EQUAL -> tote -> tote.id != valueI;
            };
            totePredicate = switch (preLogic.orElse(LogicalWord.AND)) {
                case AND -> totePredicate.map(predicate -> predicate.and(p))
                        .or(() -> Optional.of(p));
                case OR -> totePredicate.map(predicate -> predicate.or(p))
                        .or(() -> Optional.of(p));
                case NOT -> throw new QueryParserException(
                        QueryParserException.ExceptionType.UNSUPPORTED,
                        "Logic 'NOT' is currently unsupported.");
            };
        } catch (NumberFormatException ignore) {
            throw new QueryParserException(
                    QueryParserException.ExceptionType.BAD_INTEGER,
                    "Value '%s' is not an integer.".formatted(value));
        }
    }

    private ImmutableSet<Axiom> getAxioms() {
        ImmutableSet.Builder<Axiom> builder = ImmutableSet.builder();
        for (int i = 1; i < tokens.size() - 2; i++) {
            if (Token.TokenType.RESERVED_WORD.equals(tokens.get(i).tokenType)
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

    private record Axiom (Optional<LogicalWord> preLogic, EntityWord entityWord, ComparisonWord comparison, String value) {}

    public Optional<Predicate<Tote>> getTotePredicate() {
        return totePredicate;
    }
}
