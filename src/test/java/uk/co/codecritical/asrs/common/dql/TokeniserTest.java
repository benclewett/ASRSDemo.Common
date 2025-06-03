package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.parser.Token;
import uk.co.codecritical.asrs.common.dql.parser.Tokeniser;

import static org.junit.jupiter.api.Assertions.*;

class TokeniserTest {

    @Test
    void testSentence() {
        final String input = "The  quick\t\t\nbrown\rfox  \t jumped over   the\t lazy dog.   ";
        final String target = "The quick brown fox jumped over the lazy dog.";

        var out = Tokeniser.queryToStrings(input);
        var outJoined = String.join(" ", out);

        assertEquals(target, outJoined);
    }

    @Test
    void testDoubleQuotes1() {
        final String input = "\"a b c\" d e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a b c", "d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testDoubleQuotes2() {
        final String input = "a \"b c d\" e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "b c d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testDoubleQuotes3() {
        final String input = "a b \"c d e\"";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "b", "c d e"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testDoubleQuotes1Nested() {
        final String input = "\"a 'b' c\" d e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a 'b' c", "d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleQuotes1() {
        final String input = "'a b c' d e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a b c", "d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleQuotes2() {
        final String input = "a 'b c d' e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "b c d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleQuotes3() {
        final String input = "a b 'c d e'";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "b", "c d e"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleQuotesNested() {
        final String input = "a 'b \"c\" d' e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "b \"c\" d", "e", "f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleCharConjunctions() {
        final String input = "a=b,c=d";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "=", "b", ",", "c", "=", "d"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleCharConjunctionsWithWhiteSpace() {
        final String input = "a =b, c = d";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "=", "b", ",", "c", "=", "d"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testSingleCharConjunctionsWithQuotes() {
        final String input = "a='b=4',c=\"d,e,f\"";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "=", "b=4", ",", "c", "=", "d,e,f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testDoubleCharacterConjectiuons() {
        final String input = "a=='b=4',c!=\"d,e,f\"";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "==", "b=4", ",", "c", "!=", "d,e,f"
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testEmptyStrings() {
        final String input = "a='' or c!=\"\"";
        final ImmutableList<String> target = ImmutableList.of(
                "a", "=", "", "or", "c", "!=", ""
        );

        var out = Tokeniser.queryToStrings(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }

    @Test
    void testTokenType() {
        var words = Tokeniser.queryToStrings(
                "retrieve tote=42 or tote!=32 to station=pick, property=\"empty\"");

        var tokens = Tokeniser.stringsToTokens(words);

        int i = 0;
        assertEquals(Token.TokenType.KEYWORD, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.RESERVED_WORD, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.COMPARISON, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.VALUE, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.LOGICAL, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.RESERVED_WORD, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.COMPARISON, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.VALUE, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.KEYWORD, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.RESERVED_WORD, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.COMPARISON, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.VALUE, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.COMMA, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.RESERVED_WORD, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.COMPARISON, tokens.get(i++).tokenType);
        assertEquals(Token.TokenType.VALUE, tokens.get(i++).tokenType);

        assertEquals(Token.TokenType.END, tokens.get(i++).tokenType);

        Tokeniser.assertLegality(tokens);
    }
}