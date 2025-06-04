package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.executor.DqlExecutor;
import uk.co.codecritical.asrs.common.dql.executor.DqlQuery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DqlExecutorTest {
    TestGrid grid;
    DqlExecutor executor;

    @BeforeEach
    void beforeEach() {
        grid = new TestGrid();
        executor = new DqlExecutor(grid);
    }

    @Test
    void testOK() {
        var query = executor.execute("RETRIEVE tote=1 TO station=1");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
    }

    @Test
    void testFail() {
        var query = executor.execute("RUBBISH");
        assertEquals(DqlQuery.QueryResponse.FAIL, query.queryResponse);
    }

    @Test
    void testAllTotesAndStations() {
        var query = executor.execute("RETRIEVE TO");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(1, grid.selectedStation.get().getId());
    }

    @Test
    void testSpecificTotesAndStations() {
        var query = executor.execute("RETRIEVE tote=2 TO station=3");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(2, grid.selectedTote.get().getId());
        assertEquals(3, grid.selectedStation.get().getId());
    }

    @Test
    void testNonExistentTotesAndStations() {
        var query = executor.execute("RETRIEVE tote=200 TO station=300");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertFalse(grid.selectedTote.isPresent());
        assertFalse(grid.selectedStation.isPresent());
    }

    @Test
    void testNamedTotesAndStations() {
        var query = executor.execute("RETRIEVE property=empty TO capability=picking");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(3, grid.selectedStation.get().getId());
    }
}
