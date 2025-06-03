package uk.co.codecritical.asrs.common.dql.executor;

import com.google.common.base.MoreObjects;

import java.util.Objects;
import java.util.UUID;

public class DqlQuery {
    public final String query;
    public final String queryId;
    public final QueryResponse queryResponse;
    public final String errorMessage;

    private DqlQuery(String query, String queryId, QueryResponse queryResponse, String errorMessage) {
        this.query = query;
        this.queryId = queryId;
        this.queryResponse = queryResponse;
        this.errorMessage = errorMessage;
    }

    public static DqlQuery create() {
        return new DqlQuery(
                "",
                UUID.randomUUID().toString().substring(0, 5),
                QueryResponse.NEW,
                "");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("query", query)
                .add("queryId", queryId)
                .add("queryResponse", queryResponse)
                .add("errorMessage", errorMessage)
                .toString();
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
                .setErrorMessage(errorMessage);
    }

    public static Builder builder() {
        return new Builder();
    }

    //region builder

    public static class Builder {
        String query = null;
        String queryId = null;
        QueryResponse queryResponse = null;
        String errorMessage = null;
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
        public Builder setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
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
                    errorMessage);
        }
    }

    //endregion
}
