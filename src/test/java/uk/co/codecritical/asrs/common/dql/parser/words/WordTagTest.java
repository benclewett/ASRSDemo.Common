package uk.co.codecritical.asrs.common.dql.parser.words;

import com.google.common.base.VerifyException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import uk.co.codecritical.asrs.common.dql.parser.ToteTagParser;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordTagTest {

    @ParameterizedTest()
    @MethodSource("testQueryFormat")
    public void testQueryFormat(String word, @CheckForNull WordTag wordTag, boolean result) {
        Optional<WordTag> testResult = WordTag.mapFromString(word);

        assertEquals(result, testResult.isPresent());

        if (wordTag == null) {
            assertTrue(testResult.isEmpty());
        } else {
            assertEquals(wordTag, testResult.orElseThrow());
        }
    }

    @ParameterizedTest()
    @MethodSource("testQueryVelocity")
    public void testQueryVelocity(String word, @CheckForNull String capacility, double value) {
        if (capacility == null) {
            assertThrows(VerifyException.class, () -> ToteTagParser.parse(word));
        } else {
            var tag = ToteTagParser.parse(word);
            assertEquals(capacility.toUpperCase(), tag.capability());
            assertEquals(value, tag.value());
        }
    }

    //---------------------------- Support Code -----------------------

    private static List<Arguments> testQueryVelocity() {
        return List.of(
                Arguments.of("priority,cap,0.0", "cap", 0.0),
                Arguments.of("priority,cap,1.0", "cap", 1.0),
                Arguments.of("priority,CAP,1.0", "cap", 1.0),
                Arguments.of("priority,cap,1.0", "CAP", 1.0),
                Arguments.of("nonPriority,cap,0.0", null, 0.0),
                Arguments.of("priority,cap,-0.01", null, 0.0),
                Arguments.of("priority,cap,1.01", null, 0.0)
        );
    }

    private static List<Arguments> testQueryFormat() {
        return List.of(
                Arguments.of("FOO", null, false),
                Arguments.of("FIREBREAK", WordTag.FIREBREAK, true),
                Arguments.of("firebreak", WordTag.FIREBREAK, true),
                Arguments.of("NON_FIREBREAK", WordTag.NON_FIREBREAK, true),
                Arguments.of("PRIORITY,FOO,1.0", WordTag.PRIORITY, true),
                Arguments.of("PRIORITY,FOO,0", WordTag.PRIORITY, true),
                Arguments.of("PRIORITY,FOO,0.5", WordTag.PRIORITY, true),
                Arguments.of("PRIORITY,FOO", null, false),
                Arguments.of("PRIORITY,1.0", null, false),
                Arguments.of("PRIORITY,0.0,FOO", null, false)
        );
    }
}