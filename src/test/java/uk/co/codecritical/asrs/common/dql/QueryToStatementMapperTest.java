package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryToStatementMapperTest {

    @Test
    void testGoodKeyword() {
        String query = "RetrieVe bin=123";

        var s = QueryToStatementMapper.map(query);

        assertInstanceOf(RetriveQuery.class, s);
        assertEquals(Keyword.RETRIEVE, s.keyword);
    }

    @Test
    void testBadKeyword() {
        String query = "RetrieVeeee bin=123";

        assertThrows(QueryParserException.class, () -> {
            QueryToStatementMapper.map(query);
        });
    }

}