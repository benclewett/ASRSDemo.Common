package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableSet;

/*
 * <p>For each token, list the allowed token to follow</p>
 * <p>Used to filler out illegal queries.</p>
 */
public class LegalFollowingTokens {
    private LegalFollowingTokens() {}

    public static ImmutableSet<Token.TokenType> get(Token.TokenType tokenType) {
        return switch (tokenType) {
            case KEYWORD -> ImmutableSet.of(Token.TokenType.AXIOM_ENTITY, Token.TokenType.KEYWORD, Token.TokenType.END, Token.TokenType.SELECT_ENTITY, Token.TokenType.SET);
            case AXIOM_ENTITY -> ImmutableSet.of(Token.TokenType.LOGICAL, Token.TokenType.COMPARISON, Token.TokenType.KEYWORD, Token.TokenType.END);
            case LOGICAL -> ImmutableSet.of(Token.TokenType.AXIOM_ENTITY);
            case COMPARISON -> ImmutableSet.of(Token.TokenType.VALUE);
            case COMMA -> ImmutableSet.of(Token.TokenType.METRIC_NAME);
            case VALUE -> ImmutableSet.of(Token.TokenType.COMMA, Token.TokenType.KEYWORD, Token.TokenType.END, Token.TokenType.LOGICAL, Token.TokenType.SET);
            case END -> ImmutableSet.of();
            case SET -> ImmutableSet.of(Token.TokenType.METRIC_NAME);
            case METRIC_NAME -> ImmutableSet.of(Token.TokenType.ASSIGN);
            case ASSIGN -> ImmutableSet.of(Token.TokenType.METRIC_VALUE);
            case METRIC_VALUE -> ImmutableSet.of(Token.TokenType.COMMA, Token.TokenType.END);
            case NOOP -> ImmutableSet.copyOf(Token.TokenType.values());
            case SELECT_ENTITY -> ImmutableSet.of(Token.TokenType.END, Token.TokenType.SET);
        };
    }
}
