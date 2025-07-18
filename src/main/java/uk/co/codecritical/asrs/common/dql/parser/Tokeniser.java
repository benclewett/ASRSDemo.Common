package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.co.codecritical.asrs.common.dql.parser.words.WordComparison;
import uk.co.codecritical.asrs.common.dql.parser.words.WordEntity;
import uk.co.codecritical.asrs.common.dql.parser.words.WordKey;
import uk.co.codecritical.asrs.common.dql.parser.words.WordLogical;
import uk.co.codecritical.asrs.common.dql.parser.words.WordMetricName;
import uk.co.codecritical.asrs.common.dql.parser.words.WordSelect;

public class Tokeniser {
    private static final ImmutableSet<Character> WHITE_SPACE = ImmutableSet.of(' ', '\t', '\n', '\r');
    private static final Character DOUBLE_QUOTE = '\"';
    private static final Character SINGLE_QUOTE = '\'';
    private static final ImmutableSet<Character> SINGLE_CHARACTER_CONJUNCTION = ImmutableSet.of('=', ',', '!');
    private static final String EQUALS = "=";

    private Tokeniser() {
        // Static Class
    }

    //region Assert Legality

    public static void assertLegality(ImmutableList<Token> tokens) {
        WordKey wordKey = null;
        Token prevToken = null;
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (Token.TokenType.END.equals(token.tokenType)) {
                break;
            }

            if (i == 0) {
                if (!Token.TokenType.KEYWORD.equals(token.tokenType)) {
                    throw new DqlException(
                            DqlExceptionType.UNKNOWN_KEYWORD,
                            "Query should start with a keyword, not: " + token.word);
                }
                wordKey = WordKey.mapFromString(token.word).orElseThrow();
                if (!wordKey.primaryKeyword) {
                    throw new DqlException(
                            DqlExceptionType.UNEXPECTED_SYNTAX,
                            "Query starts with a unexpected keyword: '" + token.word + "'");
                }
            } else {
                if (!prevToken.getLegalFollowingTokens().contains(token.tokenType)) {
                    throw new DqlException(
                            DqlExceptionType.UNEXPECTED_SYNTAX,
                            "Token '" + token.word + "' should not follow '" + prevToken.word + "'");
                }
                if (Token.TokenType.KEYWORD.equals(token.tokenType)) {
                    var nextKeyWord = WordKey.mapFromString(token.word).orElseThrow();
                    if (!wordKey.checkValidSequence(nextKeyWord)) {
                        throw new DqlException(
                                DqlExceptionType.UNEXPECTED_SYNTAX,
                                "Token '" + nextKeyWord + "' should not follow " + wordKey + "'");
                    }
                    wordKey = nextKeyWord;
                }
            }

            prevToken = token;
        }
    }

    //endregion

    //region Strings to Tokens

    enum QuerySection {
        NOOP(Token.TokenType.KEYWORD),
        SELECT(Token.TokenType.KEYWORD),
        SET(Token.TokenType.SET),
        AXIOM(Token.TokenType.KEYWORD);

        public final Token.TokenType tokenType;
        QuerySection(Token.TokenType tokenType) {
            this.tokenType = tokenType;
        }
    }

    public static ImmutableList<Token> stringsToTokens(ImmutableList<String> strings) {
        ImmutableList.Builder<Token> builder = ImmutableList.builder();

        QuerySection querySection = QuerySection.NOOP;
        Token prevToken = null;
        for (int i = 0; i < strings.size(); i++) {
            String word = strings.get(i);
            Token token;

            var keyword = WordKey.mapFromString(word);
            if (keyword.isPresent()) {
                querySection = switch (keyword.get()) {
                    case RETRIEVE, STORE, RELEASE, OUT_OF, INTO, TO, WHERE -> QuerySection.AXIOM;
                    case PICK, UPDATE, NONE -> QuerySection.NOOP;
                    case SELECT -> QuerySection.SELECT;
                    case SET -> QuerySection.SET;
                };
                token = new Token(word, querySection.tokenType);
                builder.add(token);
                prevToken = token;
                continue;
            }

            switch (querySection) {
                case SELECT -> {
                    var wordSelect = WordSelect.mapFromString(word);
                    if (wordSelect.isPresent()) {
                        token = new Token(word, Token.TokenType.SELECT_ENTITY);
                    } else {
                        token = new Token(word, Token.TokenType.NOOP);
                    }
                }
                case SET -> {
                    var metricNameWord = WordMetricName.mapFromString(word);
                    if (metricNameWord.isPresent()) {
                        token = new Token(word, Token.TokenType.METRIC_NAME);
                    } else if (EQUALS.equals(word)) {
                        token = new Token(word, Token.TokenType.ASSIGN);
                    } else if (word.equals(",")) {
                        token = new Token(word, Token.TokenType.COMMA);
                    } else {
                        token = new Token(word, Token.TokenType.METRIC_VALUE);
                    }
                }
                case AXIOM -> {
                    var entityWord = WordEntity.mapFromString(word);
                    var logicalWord = WordLogical.mapFromString(word);
                    var comparisonWord = WordComparison.mapFromString(word);
                    if (comparisonWord.isPresent()) {
                        token = new Token(word, Token.TokenType.COMPARISON);
                    } else if (prevToken != null && EQUALS.equals(prevToken.word)) {
                        token = new Token(word, Token.TokenType.VALUE);
                    } else if (entityWord.isPresent()) {
                        token = new Token(word, Token.TokenType.AXIOM_ENTITY);
                    } else if (logicalWord.isPresent()) {
                        token = new Token(word, Token.TokenType.LOGICAL);
                    } else if (word.equals(",")) {
                        token = new Token(word, Token.TokenType.COMMA);
                    } else {
                        token = new Token(word, Token.TokenType.VALUE);
                    }
                }
                default -> {
                    token = new Token(word, Token.TokenType.NOOP);
                }
            }

            builder.add(token);
            prevToken = token;
        }

        builder.add(new Token("", Token.TokenType.END));

        return builder.build();
    }

    //endregion

    //region Query to Strings

    enum ParseState {
        BETWEEN,
        IN_TOKEN,
        IN_DOUBLE_QUOTE,
        IN_SINGLE_QUOTE
    }

    public static ImmutableList<String> queryToStrings(String query) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        StringBuilder token = new StringBuilder();
        ParseState state = ParseState.BETWEEN;
        for (char c : query.toCharArray()) {
            switch (state) {
                case BETWEEN -> {
                    if (DOUBLE_QUOTE.equals(c)) {
                        state = ParseState.IN_DOUBLE_QUOTE;
                    } else if (SINGLE_QUOTE.equals(c)) {
                        state = ParseState.IN_SINGLE_QUOTE;
                    } else if (SINGLE_CHARACTER_CONJUNCTION.contains(c)) {
                        builder.add(String.valueOf(c));
                    } else if (!WHITE_SPACE.contains(c)) {
                        state = ParseState.IN_TOKEN;
                        token.append(c);
                    }
                }
                case IN_TOKEN -> {
                    if (WHITE_SPACE.contains(c)) {
                        state = ParseState.BETWEEN;
                        if (!token.isEmpty()) {
                            builder.add(token.toString());
                        }
                        token.setLength(0);
                    } else if (SINGLE_CHARACTER_CONJUNCTION.contains(c)) {
                        state = ParseState.BETWEEN;
                        if (!token.isEmpty()) {
                            builder.add(token.toString());
                        }
                        token.setLength(0);
                        builder.add(String.valueOf(c));
                    } else {
                        token.append(c);
                    }
                }
                case IN_DOUBLE_QUOTE -> {
                    if (DOUBLE_QUOTE.equals(c)) {
                        state = ParseState.BETWEEN;
                        // Empty strings are legal here
                        builder.add(token.toString());
                        token.setLength(0);
                    } else {
                        token.append(c);
                    }
                }
                case IN_SINGLE_QUOTE -> {
                    if (SINGLE_QUOTE.equals(c)) {
                        state = ParseState.BETWEEN;
                        // Empty strings are legal here
                        builder.add(token.toString());
                        token.setLength(0);
                    } else {
                        token.append(c);
                    }
                }
            }
        }

        if (!token.isEmpty()) {
            builder.add(token.toString());
            token.setLength(0);
        }

        var items = builder.build();

        items = parseOutConsecutiveLogical(items);

        return items;
    }

    /** Turn "!","=" into "!=" and "=","=" int "==" */
    private static ImmutableList<String> parseOutConsecutiveLogical(ImmutableList<String> items) {
        final String START = "$$";
        String prev = START;
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        for (var word : items) {
            if (prev.equals("!") && word.equals("=")) {
                prev = "!=";
                continue;
            }
            if (prev.equals("=") && word.equals("=")) {
                prev = "==";
                continue;
            }
            if (!prev.equals(START)) {
                builder.add(prev);
            }
            prev = word;
        }
        if (!prev.equals(START)) {
            builder.add(prev);
        }
        return builder.build();
    }

    //endregion
}
