package uk.co.codecritical.asrs.common.dql.parser.words;

import com.google.common.base.VerifyException;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordMetaTest {

    @ParameterizedTest()
    @MethodSource("testQueryFormat")
    public void testQueryFormat(String word, @CheckForNull WordMeta wordMeta, boolean result) {
        Optional<WordMeta> testResult = WordMeta.get(word);

        assertEquals(result, testResult.isPresent());

        if (wordMeta == null) {
            assertTrue(testResult.isEmpty());
        } else {
            assertEquals(wordMeta, testResult.orElseThrow());
        }
    }

    @ParameterizedTest()
    @MethodSource("testQueryValue")
    public void testQueryValue(String word, @CheckForNull String capacility, double value) {
        if (capacility == null) {
            assertThrows(VerifyException.class, () -> MetaParser.parse(word));
        } else {
            var meta = MetaParser.parse(word);

            assertEquals(capacility.toUpperCase(), meta.capability());
            assertEquals(value, meta.value());
        }
    }

    //---------------------------- Support Code -----------------------

    private static List<Arguments> testQueryValue() {
        return List.of(
                Arguments.of("velocity,cap,0.0", "cap", 0.0),
                Arguments.of("velocity,cap,1.0", "cap", 1.0),
                Arguments.of("velocity,CAP,1.0", "cap", 1.0),
                Arguments.of("velocity,cap,1.0", "CAP", 1.0),
                Arguments.of("notVelocity,cap,0.0", null, 0.0),
                Arguments.of("velocity,cap,-0.01", null, 0.0),
                Arguments.of("velocity,cap, 1.01", null, 0.0)
        );
    }

    private static List<Arguments> testQueryFormat() {
        return List.of(
                Arguments.of("FOO", null, false),
                Arguments.of("FIREBREAK", WordMeta.FIREBREAK, true),
                Arguments.of("firebreak", WordMeta.FIREBREAK, true),
                Arguments.of("NON_FIREBREAK", WordMeta.NON_FIREBREAK, true),
                Arguments.of("VELOCITY,FOO,1.0", WordMeta.VELOCITY, true),
                Arguments.of("VELOCITY,FOO,0", WordMeta.VELOCITY, true),
                Arguments.of("VELOCITY,FOO,0.5", WordMeta.VELOCITY, true),
                Arguments.of("VELOCITY,FOO", null, false),
                Arguments.of("VELOCITY,1.0", null, false),
                Arguments.of("VELOCITY,0.0,FOO", null, false)
        );
    }
}