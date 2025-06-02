package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyWordTest {

    @Test
    void testParse() {
        assertEquals(Optional.empty(), KeyWord.mapFromString("foo"));
        assertEquals(Optional.of(KeyWord.SELECT), KeyWord.mapFromString("select"));
        assertEquals(Optional.of(KeyWord.SELECT), KeyWord.mapFromString("SELECT"));
    }
}
