package uk.co.codecritical.asrs.common.dql;

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
        RESERVED_WORD,
        LOGICAL,
        COMPARISON,
        COMMA,
        END,
        VALUE
    }

    public ImmutableSet<TokenType> getLegalFollowingTokens() {
        return Token.getLegalFollowingTokens(this.tokenType);
    }

    public static ImmutableSet<TokenType> getLegalFollowingTokens(TokenType tokenType) {
        return switch (tokenType) {
            case KEYWORD -> ImmutableSet.of(TokenType.RESERVED_WORD, TokenType.KEYWORD, TokenType.END);
            case RESERVED_WORD -> ImmutableSet.of(TokenType.LOGICAL, TokenType.COMPARISON, TokenType.COMMA, TokenType.KEYWORD, TokenType.END);
            case LOGICAL -> ImmutableSet.of(TokenType.RESERVED_WORD);
            case COMPARISON -> ImmutableSet.of(TokenType.VALUE);
            case COMMA -> ImmutableSet.of(TokenType.RESERVED_WORD);
            case VALUE -> ImmutableSet.of(TokenType.COMMA, TokenType.KEYWORD, TokenType.END, TokenType.LOGICAL);
            case END -> ImmutableSet.of();
        };
    }
}
