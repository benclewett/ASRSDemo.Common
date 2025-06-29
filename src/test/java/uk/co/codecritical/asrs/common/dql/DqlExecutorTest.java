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
    TestQueryHandler grid;
    DqlExecutor executor;

    @BeforeEach
    void beforeEach() {
        grid = new TestQueryHandler();
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
        final Assignment ab = new Assignment("TEST", "a");
        final Assignment cd = new Assignment("TEST", "b");
        final Assignment ef = new Assignment("TEST", "c");
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

    //region RELEASE

    @Test
    void testRelieve() {
        var query = executor.execute("RELEASE tote=1");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(grid.assignments.size(), 0);
    }

    @Test
    void testReleaseAssignment() {
        var query = executor.execute("STORE tote=1 set property=foo");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedTote.isPresent());
        assertEquals(1, grid.selectedTote.get().getId());
        assertEquals(grid.assignments.size(), 1);
        assertEquals("foo", grid.assignments.get(0).value());
    }

    //endregion

    //region Pick

    @Test
    void testPicking_Decant() {
        var query = executor.execute("PICK INTO tote=1 SET sku=123, amount=13, property='change'");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedToteInTo.isPresent());
        assertEquals(1, grid.selectedToteInTo.get().getId());
        assertEquals(3, grid.assignments.size());
        assertEquals(123, grid.sku);
        assertEquals(13, grid.amount);
    }

    @Test
    void testPicking_POG() {
        var query = executor.execute("PICK OUT_OF tote=2 SET sku=124, amount=15");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedToteOutOf.isPresent());
        assertEquals(2, grid.selectedToteOutOf.get().getId());
        assertEquals(2, grid.assignments.size());
        assertEquals(124, grid.sku);
        assertEquals(15, grid.amount);
    }

    @Test
    void testPicking_PIG() {
        var query = executor.execute("PICK OUT_OF tote=2 INTO tote=3 SET sku=127, amount=49");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedToteInTo.isPresent());
        assertEquals(2, grid.selectedToteOutOf.get().getId());
        assertEquals(3, grid.selectedToteInTo.get().getId());
        assertEquals(2, grid.assignments.size());
        assertEquals(127, grid.sku);
        assertEquals(49, grid.amount);
    }

    @Test
    void testPicking_PIG2() {
        var query = executor.execute("PICK INTO tote=3 OUT_OF tote=2 SET sku=127, amount=49");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertTrue(grid.selectedToteInTo.isPresent());
        assertEquals(2, grid.selectedToteOutOf.get().getId());
        assertEquals(3, grid.selectedToteInTo.get().getId());
        assertEquals(2, grid.assignments.size());
        assertEquals(127, grid.sku);
        assertEquals(49, grid.amount);
    }

    @Test
    void testSelect_System() {
        var query = executor.execute("SELECT system");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertEquals(1, query.table.rows.size());
        assertEquals(2, query.table.rows.get(0).size());
        assertEquals(TestSystemVariables.VERSION, query.table.rows.get(0).get(0));
        assertEquals(TestSystemVariables.version, query.table.rows.get(0).get(1));
    }

    @Test
    void testSelect_Station() {
        var query = executor.execute("SELECT station");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertEquals(1, query.table.rows.size());
        assertEquals(2, query.table.rows.get(0).size());
        assertEquals(TestSystemVariables.VERSION, query.table.rows.get(0).get(0));
        assertEquals(TestSystemVariables.version, query.table.rows.get(0).get(1));
    }

    @Test
    void testSelect_Decant() {
        var query = executor.execute("SELECT decant");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertEquals(1, query.table.rows.size());
        assertEquals(2, query.table.rows.get(0).size());
        assertEquals(TestSystemVariables.VERSION, query.table.rows.get(0).get(0));
        assertEquals(TestSystemVariables.version, query.table.rows.get(0).get(1));
    }

    @Test
    void testUpdate_SimSpeed() {
        var query = executor.execute("UPDATE SET sim_speed = 0.5");
        assertEquals(DqlQuery.QueryResponse.OK, query.queryResponse);
        assertEquals(1, grid.assignments.size());
        assertEquals("SIM_SPEED", grid.assignments.get(0).name());
        assertEquals("0.5", grid.assignments.get(0).value());
    }

    //endregion
}
