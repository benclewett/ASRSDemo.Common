package uk.co.codecritical.asrs.common.entity;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.stream.Stream;

/** Tokens for tote properties or tags */
public class TokenSet {
    public static final TokenSet EMPTY = new TokenSet(ImmutableSet.of());

    public static TokenSet of(ImmutableSet<String> token) {
        return new TokenSet(token);
    }

    public static TokenSet of(String... token) {
        return new TokenSet(ImmutableSet.copyOf(token));
    }

    private final ImmutableSet<String> tokens;

    private TokenSet(ImmutableSet<String> tokens) {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        tokens.forEach(t -> builder.add(t.toUpperCase()));
        this.tokens = builder.build();
    }

    public int size() {
        return tokens.size();
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }

    public boolean isPresent(String token) {
        return tokens.contains(filter(token));
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

    public Stream<String> stream() {
        return tokens.stream();
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
            token = token.toUpperCase();
            builder = ImmutableSet.builder();
            for (var t : tokens) {
                if (!t.equals(filter(token))) {
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
