package uk.co.codecritical.asrs.common.dql.parser;

import uk.co.codecritical.asrs.common.dql.parser.words.WordMeta;

import javax.annotation.CheckForNull;

public record ToteMeta(WordMeta wordMeta, @CheckForNull String capability, @CheckForNull Double value) {
    public String toTokenString() {
        return switch (wordMeta) {
            case FIREBREAK, NON_FIREBREAK -> wordMeta.name();
            case VELOCITY -> "%s,%s,%.3f".formatted(
                    wordMeta.name(),
                    capability,
                    value);
        };
    }
}
