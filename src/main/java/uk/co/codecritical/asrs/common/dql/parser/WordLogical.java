package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum WordLogical {
    AND("AND", "&&"),
    OR("OR", "||"),
    NOT("NOT", "!");

    WordLogical(String... matchingWords) {
        this.matchingString = ImmutableSet.copyOf(matchingWords);
    }
    public final ImmutableSet<String> matchingString;

    private static final ImmutableMap<String, WordLogical> MAP = createMap();

    private static ImmutableMap<String, WordLogical> createMap() {
        ImmutableMap.Builder<String, WordLogical> builder = ImmutableMap.builder();
        for (var e : WordLogical.values()) {
            for (String word : e.matchingString) {
                builder.put(word, e);
            }
        }
        return builder.build();
    }

    public static Optional<WordLogical> mapFromString(String word) {
        return Optional.ofNullable(MAP.get(word.toUpperCase()));
    }
}
