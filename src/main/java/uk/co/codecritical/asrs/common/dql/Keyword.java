package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Optional;

/** <p>Keyword in a statement.  Expected a primary, with one or more secondaries.</p>
 * <p>Example, statements in uppercase:</p>
 * <pre>RETRIEVE bin=123 TO station=456</pre>
 */
public enum Keyword {
    // Secondary:
    SET(false),
    INTO(false),
    TO(false),
    FROM(false),
    WITH(false),

    // Primary:
    SELECT(true, FROM),
    RETRIEVE(true, TO),
    STORE(true, WITH),
    UPDATE(true, SET),
    PICK(true, INTO);

    Keyword(boolean primaryKeyword, Keyword... secondaries) {
        this.primaryKeyword = primaryKeyword;
        this.secondaries = ImmutableSet.copyOf(secondaries);
    }
    public final boolean primaryKeyword;
    public final ImmutableSet<Keyword> secondaries;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(Keyword.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<Keyword> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(Keyword::valueOf);
    }
}
