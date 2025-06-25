package uk.co.codecritical.asrs.common.dql.executor;

import uk.co.codecritical.asrs.common.dql.parser.DqlExceptionType;

import javax.annotation.CheckForNull;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;

public class DqlQuery {
    public final String query;
    public final String queryId;
    public final QueryResponse queryResponse;
    public final @CheckForNull DqlExceptionType errorType;
    public final String errorMessage;
    public final OptionalInt scriptId;
    public final DqlTable table;
    public final @CheckForNull Integer selectedToteId;
    public final @CheckForNull Integer selectedStationId;

    private DqlQuery(
            String query,
            String queryId,
            QueryResponse queryResponse,
            @CheckForNull DqlExceptionType errorType,
            String errorMessage,
            OptionalInt scripId,
            DqlTable table,
            @CheckForNull Integer selectedToteId,
            @CheckForNull Integer selectedStationId
    ) {
        this.query = query;
        this.queryId = queryId;
        this.queryResponse = queryResponse;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.scriptId = scripId;
        this.table = table;
        this.selectedToteId = selectedToteId;
        this.selectedStationId = selectedStationId;
    }

    public static DqlQuery create() {
        return new DqlQuery(
                "",
                UUID.randomUUID().toString().substring(0, 5),
                QueryResponse.NEW,
                null,
                "",
                OptionalInt.empty(),
                DqlTable.EMPTY,
                null,
                null);
    }

    @Override
    public String toString() {
        // Use trad toString() as we want the quotes.
        return "DqlQuery{" +
                "query='" + query + '\'' +
                ", queryId='" + queryId + '\'' +
                ", queryResponse=" + queryResponse +
                ", errorType=" + errorType +
                ", errorMessage='" + errorMessage + '\'' +
                ", scriptId='" + scriptId + '\'' +
                ", table='" + table + '\'' +
                ", selectedToteId='" + selectedToteId + '\'' +
                ", selectedStationId='" + selectedStationId + '\'' +
                '}';
    }

    public enum QueryResponse {
        NEW, OK, FAIL
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DqlQuery dqlQuery = (DqlQuery) o;
        return Objects.equals(query, dqlQuery.query) && Objects.equals(queryId, dqlQuery.queryId) && queryResponse == dqlQuery.queryResponse;
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, queryId, queryResponse);
    }

    public Builder mutate() {
        return new Builder()
                .setQuery(query)
                .setQueryId(queryId)
                .setQueryResponse(queryResponse)
                .setErrorMessage(errorMessage)
                .setErrorType(errorType)
                .setScriptId(scriptId)
                .setTable(table)
                .setSelectedToteId(selectedToteId)
                .setSelectedStationId(selectedStationId);
    }

    public static Builder builder() {
        return new Builder();
    }

    //region builder

    public static class Builder {
        String query = null;
        String queryId = null;
        QueryResponse queryResponse = null;
        @CheckForNull
        DqlExceptionType errorType;
        String errorMessage = null;
        OptionalInt scriptId = OptionalInt.empty();
        DqlTable table = DqlTable.EMPTY;
        @CheckForNull Integer selectedToteId = null;
        @CheckForNull Integer selectedStationId = null;
        public Builder() {
        }
        public Builder setQuery(String query) {
            this.query = query;
            return this;
        }
        public Builder setQueryId(String queryId) {
            this.queryId = queryId;
            return this;
        }
        public Builder setQueryResponse(QueryResponse queryResponse) {
            this.queryResponse = queryResponse;
            return this;
        }
        public Builder setErrorType(@CheckForNull DqlExceptionType errorType) {
            this.errorType = errorType;
            return this;
        }
        public Builder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        public Builder setScriptId(OptionalInt scriptId) {
            this.scriptId = scriptId;
            return this;
        }
        public Builder setScriptId(int scriptId) {
            this.scriptId = OptionalInt.of(scriptId);
            return this;
        }
        public Builder setTable(DqlTable table) {
            this.table = table;
            return this;
        }
        public Builder setSelectedToteId(@CheckForNull Integer toteId) {
            this.selectedToteId = toteId;
            return this;
        }
        public Builder setSelectedStationId(@CheckForNull Integer stationId) {
            this.selectedStationId = stationId;
            return this;
        }
        public DqlQuery build() {
            assert (query != null);
            assert(queryId != null);
            assert(queryResponse != null);
            assert(errorMessage != null);
            return new DqlQuery(
                    query,
                    queryId,
                    queryResponse,
                    errorType,
                    errorMessage,
                    scriptId,
                    table,
                    selectedToteId,
                    selectedStationId);
        }
    }

    //endregion
}
