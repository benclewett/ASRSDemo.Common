package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum WordComparison {
    EQUAL("=", "=="),
    NOT_EQUAL("!=");

    WordComparison(String... matchingWords) {
        this.matchingString = ImmutableSet.copyOf(matchingWords);
    }
    public final ImmutableSet<String> matchingString;

    private static final ImmutableMap<String, WordComparison> MAP = createMap();

    private static ImmutableMap<String, WordComparison> createMap() {
        ImmutableMap.Builder<String, WordComparison> builder = ImmutableMap.builder();
        for (var e : WordComparison.values()) {
            for (String word : e.matchingString) {
                builder.put(word, e);
            }
        }
        return builder.build();
    }

    public static Optional<WordComparison> mapFromString(String word) {
        return Optional.ofNullable(MAP.get(word.toUpperCase()));
    }
}
