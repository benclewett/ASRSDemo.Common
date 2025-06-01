package uk.co.codecritical.asrs.common;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenSetTest {

    @Test
    void testFilter() {
        assertEquals("TOKEN", TokenSet.filter("TOKEN"));
        assertEquals("TOKEN", TokenSet.filter("token"));
        assertEquals("TOKEN", TokenSet.filter("    TOKEN    "));
        assertEquals("TOKEN", TokenSet.filter("\rTOKEN\r"));
        assertEquals("TOKEN", TokenSet.filter("\nTOKEN\n"));
        assertEquals("TOKEN", TokenSet.filter("\tTOKEN\t"));
    }

    @Test
    void testFullBuilder() {
        var builder = TokenSet.builder();
        builder.addToken("T1");
        builder.addToken("T2");
        builder.addTokens(ImmutableSet.of("T3", "T4"));
        var tokens = builder.build();

        assertEquals(4, tokens.size());
        assertFalse(tokens.isEmpty());
        assertTrue(tokens.isPresent("T1"));
        assertTrue(tokens.isPresent("t1"));
        assertTrue(tokens.isPresent("T2"));
        assertTrue(tokens.isPresent("T3"));
        assertTrue(tokens.isPresent("T4"));
        assertFalse(tokens.isPresent("T5"));
    }

    @Test
    void testEmptyBuilder() {
        var tokens = TokenSet.builder().build();

        assertEquals(0, tokens.size());
        assertTrue(tokens.isEmpty());
        assertFalse(tokens.isPresent("T1"));
        assertFalse(tokens.isPresent("T2"));
        assertFalse(tokens.isPresent("T3"));
        assertFalse(tokens.isPresent("T4"));
        assertFalse(tokens.isPresent("T5"));
    }

    @Test
    void testDuplicateBuilder() {
        var builder = TokenSet.builder();
        builder.addToken("T1");
        builder.addToken("T1");
        builder.addTokens(ImmutableSet.of("T2", "T2"));
        var tokens = builder.build();

        assertEquals(2, tokens.size());
        assertFalse(tokens.isEmpty());
        assertTrue(tokens.isPresent("T1"));
        assertTrue(tokens.isPresent("t1"));
        assertTrue(tokens.isPresent("T2"));
        assertFalse(tokens.isPresent("T3"));
        assertFalse(tokens.isPresent("T4"));
        assertFalse(tokens.isPresent("T5"));
    }
}