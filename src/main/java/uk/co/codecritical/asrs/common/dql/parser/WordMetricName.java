package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableSet;

import java.util.Optional;

public enum WordMetricName {
    /** Replace All Tote Properties */
    PROPERTY,
    /** Add a Tote Property */
    ADD_PROPERTY,
    /** Remove a Tote property */
    DEL_PROPERTY,
    /** Sku id */
    SKU,
    /** Sku Amount */
    AMOUNT,
    /** Sets the speed of the SIM */
    SIM_SPEED,
    /** Does nothing */
    TEST;   // Does nothing

    private static final ImmutableSet<String> SET = createMap();

    private static ImmutableSet<String> createMap() {
        ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        for (var e : WordMetricName.values()) {
            builder.add(e.name());
        }
        return builder.build();
    }

    public static boolean isPresent(String name) {
        return SET.contains(name.toUpperCase());
    }

    public static Optional<WordMetricName> mapFromString(String word) {
        if (SET.contains(word.toUpperCase())) {
            return Optional.of(WordMetricName.valueOf(word.toUpperCase()));
        } else {
            return Optional.empty();
        }
    }
}
