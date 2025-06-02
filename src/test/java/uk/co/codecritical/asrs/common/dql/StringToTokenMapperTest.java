package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringToTokenMapperTest {

    @Test
    void testSentance() {
        final String input = "The  quick\t\t\nbrown\rfox  \t jumped over   the\t lazy dog.   ";
        final String target = "The quick brown fox jumped over the lazy dog.";

        var out = StringToTokenMapper.map(input);
        var outJoined = String.join(" ", out);

        assertEquals(target, outJoined);
    }

    @Test
    void testDoubleQuotes1() {
        final String input = "\"a b c\" d e f";
        final ImmutableList<String> target = ImmutableList.of(
                "a b c", "d", "e", "f"
        );

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

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

        var out = StringToTokenMapper.map(input);

        assertEquals(target.size(), out.size());
        for (int i = 0; i < target.size(); i++) {
            assertEquals(target.get(i), out.get(i));
        }
    }
}