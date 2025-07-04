package uk.co.codecritical.asrs.common.dql.parser.words;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Optional;

public enum WordSelect {
    /** System Variables & State */
    SYSTEM,
    STATION,
    DECANT,
    TOTE,
    BOT,
    HIVE;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(WordSelect.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<WordSelect> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(WordSelect::valueOf);
    }
}
