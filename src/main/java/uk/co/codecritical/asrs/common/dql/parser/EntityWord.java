package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Optional;

/** Entities, such as 'tote' or 'station'. */
public enum EntityWord {
    TOTE,
    STATION,
    PROPERTY,
    CAPABILITY,
    TAG;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(EntityWord.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<EntityWord> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(EntityWord::valueOf);
    }
}
