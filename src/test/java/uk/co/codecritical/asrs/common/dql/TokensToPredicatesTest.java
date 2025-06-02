package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.Tote;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokensToPredicatesTest {
    static final Tote TOTE_1 = Tote.builder(1).setProperty("EMPTY").build();
    static final Tote TOTE_2 = Tote.builder(2).setProperty("EMPTY").build();
    static final Tote TOTE_3 = Tote.builder(3).clearProperties().build();
    static final Tote TOTE_4 = Tote.builder(4).setProperty("EMPTY").setProperty("BLUE").build();

    @Test
    void testToteById() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1"));

        Tokeniser.assertLegality(tokens);


        var t = new TokensToPredicates(tokens);
        assertTrue(t.getTotePredicate().isPresent());

        var p = t.getTotePredicate().orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY'"));

        Tokeniser.assertLegality(tokens);

        var t = new TokensToPredicates(tokens);
        assertTrue(t.getTotePredicate().isPresent());

        var p = t.getTotePredicate().orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByPropertyAndId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY' AND tote=2"));

        Tokeniser.assertLegality(tokens);

        var t = new TokensToPredicates(tokens);
        assertTrue(t.getTotePredicate().isPresent());

        var p = t.getTotePredicate().orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByPropertyAndProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY' AND property=\"BLUE\""));

        Tokeniser.assertLegality(tokens);

        var t = new TokensToPredicates(tokens);
        assertTrue(t.getTotePredicate().isPresent());

        var p = t.getTotePredicate().orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
        assertTrue(p.test(TOTE_4));
    }

    @Test
    void testToteByNoProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property=\"\""));

        Tokeniser.assertLegality(tokens);

        var t = new TokensToPredicates(tokens);
        assertTrue(t.getTotePredicate().isPresent());

        var p = t.getTotePredicate().orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertTrue(p.test(TOTE_3));
        assertFalse(p.test(TOTE_4));
    }
}