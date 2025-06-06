package uk.co.codecritical.asrs.common;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import java.util.Objects;

/** Tokens for tote meta */
public class TokenSet {
    public static final TokenSet EMPTY = new TokenSet(ImmutableSet.of());

    public static TokenSet of(ImmutableSet<String> properties) {
        return new TokenSet(properties);
    }

    private final ImmutableSet<String> tokens;

    private TokenSet(ImmutableSet<String> tokens) {
        this.tokens = tokens;
    }

    public int size() {
        return tokens.size();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    public boolean isPresent(String token) {
        token = filter(token);
        return tokens.contains(token);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tokens", tokens)
                .toString();
    }

    public ImmutableSet<String> get() {
        return tokens;
    }

    public static String filter(String token) {
        return token.toUpperCase().strip();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenSet tokenSet = (TokenSet) o;
        return Objects.equals(tokens, tokenSet.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tokens);
    }

    //region Builder

    public Builder mutate() {
        return new Builder()
                .addTokens(tokens);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        public Builder() {
        }
        public Builder addToken(String token) {
            builder.add(TokenSet.filter(token));
            return this;
        }
        public Builder removeToken(String token) {
            var tokens = builder.build();
            builder = ImmutableSet.builder();
            for (var t : tokens) {
                if (!t.equals(token)) {
                    builder.add(t);
                }
            }
            return this;
        }
        public Builder addTokens(ImmutableCollection<String> tokens) {
            tokens.forEach(t -> builder.add(TokenSet.filter(t)));
            return this;
        }
        public TokenSet build() {
            return new TokenSet(builder.build());
        }
    }

    //endregion
}
