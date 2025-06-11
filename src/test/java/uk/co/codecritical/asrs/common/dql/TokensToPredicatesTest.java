package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.parser.KeyWord;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;
import uk.co.codecritical.asrs.common.dql.parser.TokensToPredicates;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokensToPredicatesTest {
    static final TestTote TOTE_1 = new TestTote(1, "EMPTY");
    static final TestTote TOTE_2 = new TestTote(2, "EMPTY");
    static final TestTote TOTE_3 = new TestTote(3);
    static final TestTote TOTE_4 = new TestTote(4, "BLUE", "EMPTY");

    static final TestStation STATION_1 = new TestStation(1, "DECANT");
    static final TestStation STATION_2 = new TestStation(2, "DECANT");
    static final TestStation STATION_3 = new TestStation(3, "PICK");
    static final TestStation STATION_4 = new TestStation(4, "PICK");

    @Test
    void testToteById() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByNotId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote!=1"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertTrue(p.test(TOTE_3));
    }

    @Test
    void testToteByProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY'"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByPropertyAndId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY' AND tote=2"));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
    }

    @Test
    void testToteByIdOrId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1 OR tote=2 OR tote=4"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
        assertTrue(p.test(TOTE_4));
    }

    @Test
    void testToteByPropertyOrId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY' OR tote=2"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertTrue(p.test(TOTE_1));
        assertTrue(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
        assertTrue(p.test(TOTE_4));
    }

    @Test
    void testToteByPropertyAndProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property='EMPTY' AND property=\"BLUE\"")
        );

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertFalse(p.test(TOTE_3));
        assertTrue(p.test(TOTE_4));
    }

    @Test
    void testToteByNoProperty() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE property=\"\""
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getTotePredicate(KeyWord.RETRIEVE).orElseThrow();

        assertFalse(p.test(TOTE_1));
        assertFalse(p.test(TOTE_2));
        assertTrue(p.test(TOTE_3));
        assertFalse(p.test(TOTE_4));
    }

    @Test
    void testStationById() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1 TO station=1"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getStationPredicate(KeyWord.TO).orElseThrow();

        assertTrue(p.test(STATION_1));
        assertFalse(p.test(STATION_2));
        assertFalse(p.test(STATION_3));
        assertFalse(p.test(STATION_4));
    }

    @Test
    void testStationByCapability() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1 TO capability=decant"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getStationPredicate(KeyWord.TO).orElseThrow();

        assertTrue(p.test(STATION_1));
        assertTrue(p.test(STATION_2));
        assertFalse(p.test(STATION_3));
        assertFalse(p.test(STATION_4));
    }

    @Test
    void testStationByCapabilityAndId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1 TO station=1 AND capability=decant"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getStationPredicate(KeyWord.TO).orElseThrow();

        assertTrue(p.test(STATION_1));
        assertFalse(p.test(STATION_2));
        assertFalse(p.test(STATION_3));
        assertFalse(p.test(STATION_4));
    }

    @Test
    void testStationByCapabilityOrId() {
        var tokens = Tokeniser.stringsToTokens(Tokeniser.queryToStrings(
                "RETRIEVE tote=1 TO station=4 OR capability=decant"
        ));

        Tokeniser.assertLegality(tokens);

        var t = TokensToPredicates.parse(tokens);

        var p = t.getStationPredicate(KeyWord.TO).orElseThrow();

        assertTrue(p.test(STATION_1));
        assertTrue(p.test(STATION_2));
        assertFalse(p.test(STATION_3));
        assertTrue(p.test(STATION_4));
    }
}