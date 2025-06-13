package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

public class Token {
    public final String word;
    public final TokenType tokenType;
    public Token(String word, TokenType tokenType) {
        this.word = word;
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("token", word)
                .add("tokenType", tokenType)
                .toString();
    }

    public enum TokenType {
        KEYWORD,
        AXIOM_ENTITY,
        LOGICAL,
        COMPARISON,
        COMMA,
        END,
        VALUE,
        SET,
        METRIC_NAME,
        ASSIGN,
        METRIC_VALUE,
        NOOP,
        SELECT_ENTITY
    }

    public ImmutableSet<TokenType> getLegalFollowingTokens() {
        return Token.getLegalFollowingTokens(this.tokenType);
    }

    public static ImmutableSet<TokenType> getLegalFollowingTokens(TokenType tokenType) {
        return switch (tokenType) {
            case KEYWORD -> ImmutableSet.of(TokenType.AXIOM_ENTITY, TokenType.KEYWORD, TokenType.END, TokenType.SELECT_ENTITY);
            case AXIOM_ENTITY -> ImmutableSet.of(TokenType.LOGICAL, TokenType.COMPARISON, TokenType.KEYWORD, TokenType.END);
            case LOGICAL -> ImmutableSet.of(TokenType.AXIOM_ENTITY);
            case COMPARISON -> ImmutableSet.of(TokenType.VALUE);
            case COMMA -> ImmutableSet.of(TokenType.METRIC_NAME);
            case VALUE -> ImmutableSet.of(TokenType.COMMA, TokenType.KEYWORD, TokenType.END, TokenType.LOGICAL, TokenType.SET);
            case END -> ImmutableSet.of();
            case SET -> ImmutableSet.of(TokenType.METRIC_NAME);
            case METRIC_NAME -> ImmutableSet.of(TokenType.ASSIGN);
            case ASSIGN -> ImmutableSet.of(TokenType.METRIC_VALUE);
            case METRIC_VALUE -> ImmutableSet.of(TokenType.COMMA, TokenType.END);
            case NOOP -> ImmutableSet.copyOf(TokenType.values());
            case SELECT_ENTITY -> ImmutableSet.of(TokenType.END, TokenType.SET);
        };
    }
}
