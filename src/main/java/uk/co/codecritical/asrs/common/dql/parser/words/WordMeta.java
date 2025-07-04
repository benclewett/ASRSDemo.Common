package uk.co.codecritical.asrs.common.dql.parser.words;

import java.util.Optional;
import java.util.regex.Pattern;

public enum WordMeta {
    FIREBREAK("FIREBREAK"),
    NON_FIREBREAK("NON_FIREBREAK"),
    VELOCITY("VELOCITY,[A-Z_]+,[0-9.]+");

    final String regEx;
    final Pattern pattern;
    WordMeta(String regEx) {
        this.regEx = regEx;
        this.pattern = Pattern.compile(regEx);
    }

    public static Optional<WordMeta> get(String token) {
        token = token.toUpperCase();
        for (var meta : WordMeta.values()) {
            if (meta.pattern.asMatchPredicate().test(token)) {
                return Optional.of(meta);
            }
        }
        return Optional.empty();
    }
}
