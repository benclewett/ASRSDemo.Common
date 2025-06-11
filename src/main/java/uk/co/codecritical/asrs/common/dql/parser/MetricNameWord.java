package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum MetricNameWord {
    PROPERTY,
    ADD_PROPERTY,
    DEL_PROPERTY,
    SKU,
    AMOUNT,
    TEST;   // Does nothing

    private static final ImmutableSet<String> SET = createMap();

    private static ImmutableSet<String> createMap() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (var e : MetricNameWord.values()) {
            builder.add(e.name());
        }
        return builder.build();
    }

    public static boolean isPresent(String name) {
        return SET.contains(name.toUpperCase());
    }

    public static Optional<MetricNameWord> mapFromString(String word) {
        if (SET.contains(word.toUpperCase())) {
            return Optional.of(MetricNameWord.valueOf(word.toUpperCase()));
        } else {
            return Optional.empty();
        }
    }
}
