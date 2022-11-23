package model;

import junit.framework.TestCase;

import static org.junit.Assert.*;

public class FieldTest extends TestCase {
    Field field = new Field(100, 100);

    public void testAddBaseObject() throws CouldNotPlaceObjectException {
        BaseObject o1 = Mine.createMine(0, 0, Mine.MineSubType.OUTPUT_EAST);
        field.addBaseObject(o1);

        assertThrows(CouldNotRemoveObjectException.class, () -> field.addBaseObject(Mine.createMine(0,0, Mine.MineSubType.OUTPUT_EAST)));

        assertTrue(field.getObjects().contains(o1));
    }

    public void testRemoveBaseObject() throws CouldNotPlaceObjectException, CouldNotRemoveObjectException {
        assertThrows(CouldNotRemoveObjectException.class, () -> field.removeBaseObject(Mine.createMine(0,0, Mine.MineSubType.OUTPUT_EAST)));

        BaseObject o1 = Mine.createMine(0, 0, Mine.MineSubType.OUTPUT_EAST);
        field.addBaseObject(o1);
        field.removeBaseObject(o1);

        assertFalse(field.getObjects().contains(o1));
    }
}