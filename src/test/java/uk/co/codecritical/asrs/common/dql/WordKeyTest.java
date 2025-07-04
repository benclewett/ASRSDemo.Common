package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.parser.words.WordKey;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordKeyTest {

    @Test
    void testParse() {
        assertEquals(Optional.empty(), WordKey.mapFromString("foo"));
        assertEquals(Optional.of(WordKey.NONE), WordKey.mapFromString("none"));
        assertEquals(Optional.of(WordKey.NONE), WordKey.mapFromString("none"));
    }
}
