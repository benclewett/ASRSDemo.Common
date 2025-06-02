package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.Keyword;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeywordTest {

    @Test
    void testParse() {
        assertEquals(Optional.empty(), Keyword.mapFromString("foo"));
        assertEquals(Optional.of(Keyword.SELECT), Keyword.mapFromString("select"));
        assertEquals(Optional.of(Keyword.SELECT), Keyword.mapFromString("SELECT"));
    }
}
