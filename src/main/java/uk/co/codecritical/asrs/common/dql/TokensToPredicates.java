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

        getPredicates(axioms);
    }

    private void getPredicates(ImmutableSet<Axiom> axioms) {
        axioms.forEach(this::getPredicate);
    }

    private void getPredicate(Axiom axiom) {
        if (!LogicalWord.EQUAL.equals(axiom.logic)) {
            return; // TODO, handle more than just '='
        }
        switch (axiom.entityWord) {
            case TOTE -> addPredicateToteId(axiom.value);
            case PROPERTY -> addPredicateToteProperty(axiom.value);
            default ->
                throw new QueryParserException(
                        QueryParserException.ExceptionType.UNSUPPORTED,
                        "Entity '%s' is currently unsupported.".formatted(axiom.entityWord.name()));
        }
    }

    private void addPredicateToteProperty(String value) {
        Predicate<Tote> p = (value.isEmpty())
                ? tote -> tote.properties.isEmpty()
                : tote -> tote.properties.isPresent(value);
        if (totePredicate.isPresent()) {
            totePredicate = Optional.of(totePredicate.get().and(p));
        } else {
            totePredicate = Optional.of(p);
        }
    }

    private void addPredicateToteId(String value) {
        try {
            var valueI = Integer.parseInt(value);
            Predicate<Tote> p = tote -> tote.id == valueI;
            if (totePredicate.isPresent()) {
                totePredicate = Optional.of(totePredicate.get().and(p));
            } else {
                totePredicate = Optional.of(p);
            }
        } catch (NumberFormatException ignore) {
            throw new QueryParserException(
                    QueryParserException.ExceptionType.BAD_INTEGER,
                    "Value '%s' can't be parsed into integers.".formatted(value));
        }
    }

    private ImmutableSet<Axiom> getAxioms() {
        ImmutableSet.Builder<Axiom> builder = ImmutableSet.builder();
        for (int i = 1; i < tokens.size() - 2; i++) {
            if (Token.TokenType.RESERVED_WORD.equals(tokens.get(i).tokenType)
                && Token.TokenType.LOGICAL.equals(tokens.get(i + 1).tokenType)
                && Token.TokenType.VALUE.equals(tokens.get(i + 2).tokenType)) {
                builder.add(new Axiom(
                        EntityWord.mapFromString(tokens.get(i).token).orElseThrow(),
                        LogicalWord.mapFromString(tokens.get(i + 1).token).orElseThrow(),
                        tokens.get(i + 2).token));
            }
        }
        return builder.build();
    }

    private record Axiom (EntityWord entityWord, LogicalWord logic, String value) {}

    public Optional<Predicate<Tote>> getTotePredicate() {
        return totePredicate;
    }
}
