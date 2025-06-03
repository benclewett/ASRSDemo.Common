package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum LogicalWord {
    AND("AND", "&&"),
    OR("OR", "||"),
    NOT("NOT", "!");

    LogicalWord(String... matchingWords) {
        this.matchingString = ImmutableSet.copyOf(matchingWords);
    }
    public final ImmutableSet<String> matchingString;

    private static final ImmutableMap<String, LogicalWord> MAP = createMap();

    private static ImmutableMap<String, LogicalWord> createMap() {
        ImmutableMap.Builder<String, LogicalWord> builder = ImmutableMap.builder();
        for (var e : LogicalWord.values()) {
            for (String word : e.matchingString) {
                builder.put(word, e);
            }
        }
        return builder.build();
    }

    public static Optional<LogicalWord> mapFromString(String word) {
        return Optional.ofNullable(MAP.get(word.toUpperCase()));
    }
}
