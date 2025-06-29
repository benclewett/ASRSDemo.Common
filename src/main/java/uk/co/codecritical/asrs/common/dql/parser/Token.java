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
        return LegalFollowingTokens.get(this.tokenType);
    }
}
