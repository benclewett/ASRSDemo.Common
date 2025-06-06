package uk.co.codecritical.asrs.common.dql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.dql.executor.DqlExecutor;
import uk.co.codecritical.asrs.common.dql.executor.DqlQuery;
import uk.co.codecritical.asrs.common.dql.parser.Assignment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DqlExecutorTest {
    TestExecutorListener grid;
    DqlExecutor executor;

    @BeforeEach
    void beforeEach() {
        grid = new TestExecutorListener();
        executor = new DqlExecutor(grid);
    }

    @Test
    void testFail() {
        var query = executor.execute("RUBBISH");
        assertEquals(DqlQuery.QueryResponse.FAIL, query.queryResponse);
    }

    @Test
    void testRetrieveOK() {
        var query = executor.execute("RETRIEVE tote=1 TO station=1");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
    }

    //region RETRIEVE

    @Test
    void testRetrieveAllTotesAndStations() {
        var query = executor.execute("RETRIEVE TO");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(1, grid.selectedStation.get().getId());
        assertEquals(grid.assignments.size(), 0);
    }

    @Test
    void testRetrieveSpecificTotesAndStations() {
        var query = executor.execute("RETRIEVE tote=2 TO station=3");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(2, grid.selectedTote.get().getId());
        assertEquals(3, grid.selectedStation.get().getId());
        assertEquals(grid.assignments.size(), 0);
    }

    @Test
    void testRetrieveNonExistentTotesAndStations() {
        var query = executor.execute("RETRIEVE tote=200 TO station=300");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertFalse(grid.selectedTote.isPresent());
        assertFalse(grid.selectedStation.isPresent());
        assertEquals(grid.assignments.size(), 0);
    }

    @Test
    void testRetrieveNamedTotesAndStations() {
        var query = executor.execute("RETRIEVE property=empty TO capability=picking");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(3, grid.selectedStation.get().getId());
        assertEquals(grid.assignments.size(), 0);
    }

    //endregion

    //region Assignment

    @Test
    void testAssignments() {
        final Assignment ab = new Assignment("test", "a");
        final Assignment cd = new Assignment("test", "b");
        final Assignment ef = new Assignment("test", "c");
        var query = executor.execute("RETRIEVE property=empty TO capability=picking SET test=a, test=b, test=c");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertTrue(grid.selectedStation.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(3, grid.selectedStation.get().getId());
        assertEquals(grid.assignments.size(), 3);
        assertEquals(grid.assignments.get(0), ab);
        assertEquals(grid.assignments.get(1), cd);
        assertEquals(grid.assignments.get(2), ef);
    }

    //endregion

    //region STORE

    @Test
    void testStore() {
        var query = executor.execute("STORE tote=1 and station=3");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(grid.assignments.size(), 0);
    }

    @Test
    void testStoreAssignment() {
        var query = executor.execute("STORE tote=1 and station=3 set property=foo");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(grid.assignments.size(), 1);
        assertEquals("foo", grid.assignments.get(0).value());
    }

    //endregion

}
