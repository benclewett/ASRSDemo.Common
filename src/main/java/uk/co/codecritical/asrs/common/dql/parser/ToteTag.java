package uk.co.codecritical.asrs.common.dql.parser;

import uk.co.codecritical.asrs.common.dql.parser.words.WordTag;

import javax.annotation.CheckForNull;

public record ToteTag(WordTag wordTag, @CheckForNull String capability, @CheckForNull Double value) {
    public String toTokenString() {
        return switch (wordTag) {
            case FIREBREAK, NON_FIREBREAK -> wordTag.name();
            case PRIORITY -> "%s,%s,%.3f".formatted(
                    wordTag.name(),
                    capability,
                    value);
        };
    }
}
