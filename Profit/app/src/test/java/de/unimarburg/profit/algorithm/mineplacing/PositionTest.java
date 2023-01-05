package de.unimarburg.profit.algorithm.mineplacing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PositionTest {

  @Test
  public void testEquals(){

    Position p1 = new Position(10,10);
    Position p2 = new Position(10,10);
    Position p3 = new Position(0,0);

    Assertions.assertEquals(p1,p2);
    Assertions.assertEquals(p1,p1);
    Assertions.assertNotEquals(p1,p3);
    Assertions.assertNotEquals(p2,p3);
    Assertions.assertNotEquals(p1, "Not Equal because String");

  }

}