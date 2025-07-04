package uk.co.codecritical.asrs.common.dql.parser.words;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/** <p>WordKey in a statement.  Expected a primary, with one or more secondaries.</p>
 * <p>Example, statements in uppercase:</p>
 * <pre>RETRIEVE bin=123 TO station=456</pre>
 */
public enum WordKey {
    // Primary:
    RETRIEVE(true),
    STORE(true),
    UPDATE(true),
    PICK(true),
    RELEASE(true),
    SELECT(true),

    // Secondary:
    SET(false),
    INTO(false),
    OUT_OF(false),
    TO(false),
    WHERE(false),

    NONE(false);

    WordKey(boolean primaryKeyword) {
        this.primaryKeyword = primaryKeyword;
    }
    public final boolean primaryKeyword;

    public static final ImmutableSet<String> ALL_AS_STRING = Arrays.stream(WordKey.values())
            .map(Enum::toString)
            .collect(ImmutableSet.toImmutableSet());

    public static Optional<WordKey> mapFromString(String s) {
        final String sUpper = s.toUpperCase();
        return ALL_AS_STRING.stream()
                .filter(sUpper::equals)
                .findFirst()
                .map(WordKey::valueOf);
    }

    private static final ImmutableMap<WordKey, ImmutableSet<WordKey>> keyWordSequence = ImmutableMap.of(
            RETRIEVE, ImmutableSet.of(TO),
            STORE, ImmutableSet.of(SET),
            UPDATE, ImmutableSet.of(SET),
            PICK, ImmutableSet.of(INTO, OUT_OF, SET),
            RELEASE, ImmutableSet.of(SET),
            SET, ImmutableSet.of(),
            INTO, ImmutableSet.of(SET, OUT_OF),
            OUT_OF, ImmutableSet.of(SET, INTO),
            TO, ImmutableSet.of(SET),
            SELECT, ImmutableSet.of(WHERE)
    );

    public static boolean checkValidSequence(WordKey k1, WordKey k2) {
        return keyWordSequence.containsKey(k1)
                && Objects.requireNonNull(keyWordSequence.get(k1)).contains(k2);
    }

    public boolean checkValidSequence(WordKey wordKeyAfter) {
        return keyWordSequence.containsKey(this)
                && Objects.requireNonNull(keyWordSequence.get(this)).contains(wordKeyAfter);
    }

}
