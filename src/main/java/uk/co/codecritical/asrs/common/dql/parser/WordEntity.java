package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Optional;

/** Entities, such as 'tote' or 'station'. */
public enum WordEntity {
    TOTE,
    STATION,
    PROPERTY,
    CAPABILITY,
    TOTE_FROM,
    TOTE_TO;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(WordEntity.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<WordEntity> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(WordEntity::valueOf);
    }
}
