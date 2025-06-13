package uk.co.codecritical.asrs.common.dql.executor;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;

public class DqlTable {
    public static final DqlTable EMPTY = new DqlTable(ImmutableList.of(), ImmutableList.of(), ImmutableList.of());

    public final ImmutableList<String> tableColumnNames;
    public final ImmutableList<DqlColumnType> tableColumnTypes;
    public final ImmutableList<ImmutableList<String>> rows;

    public DqlTable(
            ImmutableList<String> tableColumnNames,
            ImmutableList<DqlColumnType> tableColumnTypes,
            ImmutableList<ImmutableList<String>> rows
    ) {
        this.tableColumnNames = tableColumnNames;
        this.tableColumnTypes = tableColumnTypes;
        this.rows = rows;
    }

    public static DqlTable ofString(String name, String value) {
        return builder(
                ImmutableList.of(name),
                ImmutableList.of(DqlColumnType.STRING))
                .addRow(ImmutableList.of(value))
                .build();
    }

    public static DqlTable ofInt(String name, int value) {
        return builder(
                ImmutableList.of(name),
                ImmutableList.of(DqlColumnType.INT))
                .addRow(ImmutableList.of(String.valueOf(value)))
                .build();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("tableColumnNames", tableColumnNames)
                .add("tableColumnTypes", tableColumnTypes)
                .add("table.size", rows.size())
                .toString();
    }

    //region Builder

    public static Builder builder(ImmutableList<String> tableColumnNames, ImmutableList<DqlColumnType> tableColumnTypes) {
        return new Builder(tableColumnNames, tableColumnTypes);
    }

    public static class Builder {
        public ImmutableList<String> tableColumnNames;
        public ImmutableList<DqlColumnType> tableColumnTypes;
        public ImmutableList.Builder <ImmutableList<String>> rowBuilder = new ImmutableList.Builder<>();
        public Builder(ImmutableList<String> tableColumnNames, ImmutableList<DqlColumnType> tableColumnTypes) {
            this.tableColumnNames = tableColumnNames;
            this.tableColumnTypes = tableColumnTypes;
        }
        public Builder addRow(ImmutableList<String> row) {
            rowBuilder.add(row);
            return this;
        }
        public DqlTable build() {
            return new DqlTable(tableColumnNames, tableColumnTypes, rowBuilder.build());
        }
    }

    //endregion
}
