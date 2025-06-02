package uk.co.codecritical.asrs.common.dql;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableSet;

public class Token {
    public final String token;
    public final TokenType tokenType;
    public Token(String token, TokenType tokenType) {
        this.token = token;
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("token", token)
                .add("tokenType", tokenType)
                .toString();
    }

    public enum TokenType {
        KEYWORD,
        RESERVED_WORD,
        LOGICAL,
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
            case RESERVED_WORD -> ImmutableSet.of(TokenType.LOGICAL, TokenType.COMMA, TokenType.KEYWORD, TokenType.END);
            case LOGICAL -> ImmutableSet.of(TokenType.VALUE, TokenType.RESERVED_WORD);
            case COMMA -> ImmutableSet.of(TokenType.RESERVED_WORD);
            case VALUE -> ImmutableSet.of(TokenType.COMMA, TokenType.KEYWORD, TokenType.END, TokenType.LOGICAL);
            case END -> ImmutableSet.of();
        };
    }
}
