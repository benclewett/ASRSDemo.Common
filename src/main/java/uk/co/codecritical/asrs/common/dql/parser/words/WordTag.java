package uk.co.codecritical.asrs.common.dql.parser.words;

import java.util.Optional;
import java.util.regex.Pattern;

public enum WordTag {
    FIREBREAK("FIREBREAK"),
    NON_FIREBREAK("NON_FIREBREAK"),
    PRIORITY("PRIORITY,[A-Z_]+,[0-9.]+");

    final String regEx;
    final Pattern pattern;
    WordTag(String regEx) {
        this.regEx = regEx;
        this.pattern = Pattern.compile(regEx);
    }

    public static Optional<WordTag> mapFromString(String token) {
        token = token.toUpperCase();
        for (var tag : WordTag.values()) {
            if (tag.pattern.asMatchPredicate().test(token)) {
                return Optional.of(tag);
            }
        }
        return Optional.empty();
    }
}
