package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.base.MoreObjects;

public class QueryParserException extends RuntimeException {

    public enum ExceptionType {
        UNKNOWN_KEYWORD,
        UNEXPECTED_SYNTAX,
        UNSUPPORTED,
        BAD_INTEGER,
    }

    private final ExceptionType exceptionType;

    public QueryParserException(ExceptionType exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("exceptionType", exceptionType)
                .add("message", getMessage())
                .toString();
    }
}
