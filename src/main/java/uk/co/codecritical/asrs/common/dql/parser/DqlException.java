package uk.co.codecritical.asrs.common.dql.parser;

import com.google.common.base.MoreObjects;

public class DqlException extends RuntimeException {
    public final DqlExceptionType dqlExceptionType;

    public DqlException(DqlExceptionType dqlExceptionType, String message) {
        super(message);
        this.dqlExceptionType = dqlExceptionType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("dqlExceptionType", dqlExceptionType)
                .add("message", getMessage())
                .toString();
    }
}
