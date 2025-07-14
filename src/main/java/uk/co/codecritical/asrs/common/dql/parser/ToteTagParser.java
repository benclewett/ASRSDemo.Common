package uk.co.codecritical.asrs.common.dql.parser;

import uk.co.codecritical.asrs.common.dql.parser.words.WordTag;

import static com.google.common.base.Verify.verify;

public class ToteTagParser {

    // Static class
    private ToteTagParser() {
    }

    public static ToteTag parse(String tag) {
        verify(tag != null && !tag.isEmpty(), "No tag value.");
        var tokens = tag.split(",");

        var wordMeta = WordTag.mapFromString(tag);
        verify(wordMeta.isPresent(),
                "Tag '" + tag + "' is not valid.");

        return switch (wordMeta.get()) {
            case FIREBREAK, NON_FIREBREAK -> {
                verify(1 == tokens.length);
                yield new ToteTag(wordMeta.get(), null, null);
            }
            case PRIORITY -> {
                verify(3 == tokens.length);
                double value = Double.parseDouble(tokens[2]);
                verify(value >= 0 && value <= 1,
                        "Priority value " + value + " must be in the range: 0.0 <= value <= 1.0");
                yield new ToteTag(wordMeta.get(), tokens[1].toUpperCase(), value);
            }
        };
    }
}
