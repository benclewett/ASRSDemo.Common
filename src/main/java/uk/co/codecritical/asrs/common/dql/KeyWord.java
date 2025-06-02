package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Optional;

/** <p>KeyWord in a statement.  Expected a primary, with one or more secondaries.</p>
 * <p>Example, statements in uppercase:</p>
 * <pre>RETRIEVE bin=123 TO station=456</pre>
 */
public enum KeyWord {
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

    KeyWord(boolean primaryKeyword, KeyWord... secondaries) {
        this.primaryKeyword = primaryKeyword;
        this.secondaries = ImmutableSet.copyOf(secondaries);
    }
    public final boolean primaryKeyword;
    public final ImmutableSet<KeyWord> secondaries;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(KeyWord.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<KeyWord> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(KeyWord::valueOf);
    }
}
