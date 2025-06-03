package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum ComparisonWord {
    EQUAL("=", "=="),
    NOT_EQUAL("!=");

    ComparisonWord(String... matchingWords) {
        this.matchingString = ImmutableSet.copyOf(matchingWords);
    }
    public final ImmutableSet<String> matchingString;

    private static final ImmutableMap<String, ComparisonWord> MAP = createMap();

    private static ImmutableMap<String, ComparisonWord> createMap() {
        ImmutableMap.Builder<String, ComparisonWord> builder = ImmutableMap.builder();
        for (var e : ComparisonWord.values()) {
            for (String word : e.matchingString) {
                builder.put(word, e);
            }
        }
        return builder.build();
    }

    public static Optional<ComparisonWord> mapFromString(String word) {
        return Optional.ofNullable(MAP.get(word.toUpperCase()));
    }
}
