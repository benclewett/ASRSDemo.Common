package uk.co.codecritical.asrs.common.dql;

import com.google.common.collect.ImmutableList;
import uk.co.codecritical.asrs.common.dql.executor.DqlColumnType;
import uk.co.codecritical.asrs.common.dql.executor.DqlTable;

public class TestSystemVariables {
    public static String VERSION = "VERSION";

    private static final ImmutableList<String> COLUMN_NAMES = ImmutableList.of("NAME");
    private static final ImmutableList<DqlColumnType> COLUMN_TYPES = ImmutableList.of(DqlColumnType.STRING);

    private TestSystemVariables() {}

    public static final String version = "1.1";

    public static DqlTable asTable() {
        var builder = DqlTable.builder(COLUMN_NAMES, COLUMN_TYPES);
        builder.addRow(ImmutableList.of(VERSION, version));
        return builder.build();
    }
}
