package uk.co.codecritical.asrs.common.dql.parser;

import uk.co.codecritical.asrs.common.dql.parser.words.WordMeta;

import static com.google.common.base.Verify.verify;

public class ToteMetaParser {

    // Static class
    private ToteMetaParser() {
    }

    public static ToteMeta parse(String meta) {
        verify(meta != null && !meta.isEmpty(), "No meta value.");
        var tokens = meta.split(",");

        var wordMeta = WordMeta.mapFromString(meta);
        verify(wordMeta.isPresent(),
                "Meta '" + meta + "' is not valid meta.");

        return switch (wordMeta.get()) {
            case FIREBREAK, NON_FIREBREAK -> {
                verify(1 == tokens.length);
                yield new ToteMeta(wordMeta.get(), null, null);
            }
            case VELOCITY -> {
                verify(3 == tokens.length);
                double value = Double.parseDouble(tokens[2]);
                verify(value >= 0 && value <= 1,
                        "Velocity value " + value + " must be in the range: 0.0 <= value <= 1.0");
                yield new ToteMeta(wordMeta.get(), tokens[1].toUpperCase(), value);
            }
        };
    }
}
