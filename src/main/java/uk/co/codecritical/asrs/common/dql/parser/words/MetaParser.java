package uk.co.codecritical.asrs.common.dql.parser.words;

import static com.google.common.base.Verify.verify;

public class MetaParser {

    // Static class
    private MetaParser() {
    }

    public static Meta parse(String meta) {
        var wordMeta = WordMeta.get(meta);
        verify(wordMeta.isPresent() && WordMeta.VELOCITY.equals(wordMeta.get()),
                "Meta value '" + meta + "' is not a valid velocity token.");

        var tokens = meta.split(",");

        double value = Double.parseDouble(tokens[2]);
        verify(value >= 0 && value <= 1,
                "Velocity value " + value + " must be in the range: 0.0 <= value <= 1.0");

        return new Meta(tokens[1].toUpperCase(), value);
    }

    public record Meta(String capability, double value) {}
}
