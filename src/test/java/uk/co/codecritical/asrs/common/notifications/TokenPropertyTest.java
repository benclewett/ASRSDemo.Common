package uk.co.codecritical.asrs.common.notifications;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import uk.co.codecritical.asrs.common.Pos;
import uk.co.codecritical.asrs.common.TokenSet;
import uk.co.codecritical.asrs.common.Tote;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenPropertyTest {
    static final int TOTE_ID = 123;
    static final String P1 = "Bilbo";
    static final String P2 = "Frodo";
    static final String P3 = "Merry";
    static final String P4 = "Pippin";

    @Test
    void testEmpty() {
        Tote t = Tote.builder(TOTE_ID).clearProperties().build();

        assertTrue(t.properties.isEmpty());
        assertFalse(t.properties.isPresent(P1));
        assertFalse(t.properties.isPresent(P2));

        Tote clone = t.mutate().build();

        assertEquals(t, clone);
        assertTrue(clone.properties.isEmpty());
        assertFalse(clone.properties.isPresent(P1));
    }

    @Test
    void testSingleProperty() {
        Tote t = Tote.builder(TOTE_ID).setProperty(P1).build();

        assertFalse(t.properties.isEmpty());
        assertTrue(t.properties.isPresent(P1));
        assertTrue(t.properties.isPresent("bIlBo"));
        assertFalse(t.properties.isPresent(P2));

        Tote clone = t.mutate().build();

        assertEquals(t, clone);
        assertFalse(clone.properties.isEmpty());
        assertTrue(clone.properties.isPresent(P1));
        assertFalse(clone.properties.isPresent(P2));
    }

    @Test
    void testDoubleProperty() {
        Tote t = Tote.builder(TOTE_ID)
                .setProperty(P1)
                .setProperty(P2)
                .build();

        assertFalse(t.properties.isEmpty());
        assertTrue(t.properties.isPresent(P1));
        assertTrue(t.properties.isPresent("bIlBo"));
        assertTrue(t.properties.isPresent(P2));
        assertFalse(t.properties.isPresent(P3));

        Tote clone = t.mutate().build();

        assertEquals(t, clone);
        assertFalse(clone.properties.isEmpty());
        assertTrue(clone.properties.isPresent(P1));
        assertTrue(clone.properties.isPresent(P2));
        assertFalse(clone.properties.isPresent(P3));
    }

    @Test
    void testPropertiesShuffled() {
        Tote t = Tote.builder(TOTE_ID)
                .setProperty(P1)
                .setProperty(P2)
                .setProperty(P3)
                .setProperty(P4)
                .build();

        assertFalse(t.properties.isEmpty());
        assertTrue(t.properties.isPresent(P1));
        assertTrue(t.properties.isPresent(P2));
        assertTrue(t.properties.isPresent(P3));
        assertTrue(t.properties.isPresent(P4));

        Tote clone = t.mutate()
                .clearProperties()
                .setProperty(P4)
                .setProperty(P3)
                .setProperty(P2)
                .setProperty(P1)
                .build();

        assertEquals(t, clone);
        assertFalse(clone.properties.isEmpty());
        assertTrue(clone.properties.isPresent(P1));
        assertTrue(clone.properties.isPresent(P2));
        assertTrue(clone.properties.isPresent(P3));
        assertTrue(clone.properties.isPresent(P4));
    }

    @Test
    void testPropertiesShuffled2() {
        Tote t = Tote.builder(TOTE_ID)
                .setProperty(P1)
                .setProperty(P2)
                .setProperty(P3)
                .setProperty(P4)
                .build();

        assertFalse(t.properties.isEmpty());
        assertTrue(t.properties.isPresent(P1));
        assertTrue(t.properties.isPresent(P2));
        assertTrue(t.properties.isPresent(P3));
        assertTrue(t.properties.isPresent(P4));

        Tote clone = t.mutate()
                .clearProperties()
                .setProperties(ImmutableSet.of(
                        TokenSet.filter(P4),
                        TokenSet.filter(P2),
                        TokenSet.filter(P3),
                        TokenSet.filter(P1)))
                .build();

        assertEquals(t, clone);
        assertFalse(clone.properties.isEmpty());
        assertTrue(clone.properties.isPresent(P1));
        assertTrue(clone.properties.isPresent(P2));
        assertTrue(clone.properties.isPresent(P3));
        assertTrue(clone.properties.isPresent(P4));
    }

    @Test
    void testSetGridPos() {
        Tote t = Tote.builder(TOTE_ID).setProperty(P1).build();

        assertFalse(t.properties.isEmpty());
        assertTrue(t.properties.isPresent(P1));
        assertTrue(t.properties.isPresent("bIlBo"));
        assertFalse(t.properties.isPresent(P2));

        Tote clone = t.mutate().setGridPos(Optional.of(Pos.of(3,4))).build();

        assertNotEquals(t, clone);
        assertFalse(clone.properties.isEmpty());
        assertTrue(clone.properties.isPresent(P1));
        assertFalse(clone.properties.isPresent(P2));
    }

}
