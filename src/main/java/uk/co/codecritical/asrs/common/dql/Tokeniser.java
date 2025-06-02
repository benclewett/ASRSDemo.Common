package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class Tokeniser {
    private static final ImmutableSet<Character> WHITE_SPACE = ImmutableSet.of(' ', '\t', '\n', '\r');
    private static final Character DOUBLE_QUOTE = '\"';
    private static final Character SINGLE_QUOTE = '\'';
    private static final ImmutableSet<Character> SINGLE_CHARACTER_CONJUNCTION = ImmutableSet.of('=', ',');

    enum ParseState {
        BETWEEN,
        IN_TOKEN,
        IN_DOUBLE_QUOTE,
        IN_SINGLE_QUOTE
    }

    private Tokeniser() {
        // Static Class
    }

    public static void checkLegality(ImmutableList<Token> tokens) {
        Keyword primary = null;
        Token prevToken = null;
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if (Token.TokenType.END.equals(token.tokenType)) {
                break;
            }

            if (i == 0) {
                if (!Token.TokenType.KEYWORD.equals(token.tokenType)) {
                    throw new QueryParserException(
                            QueryParserException.ExceptionType.UNKNOWN_KEYWORD,
                            "Query should start with a keyword, not: " + token.token);
                }
                primary = Keyword.mapFromString(token.token).get();
                if (!primary.primaryKeyword) {
                    throw new QueryParserException(
                            QueryParserException.ExceptionType.UNEXPECTED_SYNTAX,
                            "Query starts with a unexpected keyword: " + token.token);
                }
            } else {
                if (!prevToken.getLegalFollowingTokens().contains(token.tokenType)) {
                    throw new QueryParserException(
                            QueryParserException.ExceptionType.UNEXPECTED_SYNTAX,
                            "Token " + token.token + " should not follow " + prevToken.tokenType);
                }
                if (Token.TokenType.KEYWORD.equals(token.tokenType)) {
                    var keyword = Keyword.mapFromString(token.token).orElseThrow();
                    if (!primary.secondaries.contains(keyword)) {
                        throw new QueryParserException(
                                QueryParserException.ExceptionType.UNEXPECTED_SYNTAX,
                                "Keyword " + keyword + " should not follow " + primary);
                    }
                }
            }

            prevToken = token;
        }
    }

    public static ImmutableList<Token> stringsToTokens(ImmutableList<String> words) {
        ImmutableList.Builder<Token> builder = ImmutableList.builder();

        Token prevToken = null;
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            var keyword = Keyword.mapFromString(word);
            var entityWord = EntityWord.mapFromString(word);
            Token token;

            if (prevToken != null && Token.TokenType.EQUALS.equals(prevToken.tokenType)) {
                token = new Token(word, Token.TokenType.VALUE);
            } else if (keyword.isPresent()) {
                token = new Token(word, Token.TokenType.KEYWORD);
            } else if (entityWord.isPresent()) {
                token = new Token(word, Token.TokenType.RESERVED_WORD);
            } else if (word.equals("=")) {
                token = new Token(word, Token.TokenType.EQUALS);
            } else if (word.equals(",")) {
                token = new Token(word, Token.TokenType.COMMA);
            } else {
                token = new Token(word, Token.TokenType.VALUE);
            }

            builder.add(token);
            prevToken = token;
        }

        builder.add(new Token("", Token.TokenType.END));

        return builder.build();
    }

    public static ImmutableList<String> queryToStrings(String s) {
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        StringBuilder token = new StringBuilder();
        ParseState state = ParseState.BETWEEN;
        for (char c : s.toCharArray()) {
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
                        if (!token.isEmpty()) {
                            builder.add(token.toString());
                        }
                        token.setLength(0);
                    } else {
                        token.append(c);
                    }
                }
                case IN_SINGLE_QUOTE -> {
                    if (SINGLE_QUOTE.equals(c)) {
                        state = ParseState.BETWEEN;
                        if (!token.isEmpty()) {
                            builder.add(token.toString());
                        }
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

        return builder.build();
    }
}
