package ch.jeda.geometry;

import ch.jeda.Data;
import org.junit.Test;
import static org.junit.Assert.*;

public class CircleTest {

    private static final double DELTA = 1E-15;
    private final Circle unit;
    private final Circle other;
    private final Circle zero;

    public CircleTest() {
        unit = new Circle(0, 0, 1);
        other = new Circle(-3, -20, 10);
        zero = new Circle(2, 2, 0);
    }

    @Test
    public void testContains() {
        assertTrue(unit.contains(0, 1));
        assertTrue(unit.contains(-1, 0));
        assertFalse(unit.contains(-1, -1));
        assertTrue(other.contains(-2, -15));
        assertFalse(other.contains(0, 0));
        assertTrue(zero.contains(2, 2));
    }

    @Test
    public void testGetArea() {
        assertEquals(Math.PI, unit.getArea(), DELTA);
        assertEquals(100 * Math.PI, other.getArea(), DELTA);
        assertEquals(0, zero.getArea(), DELTA);
    }

    @Test
    public void testGetCenterX() {
        assertEquals(0, unit.getCenterX(), DELTA);
        assertEquals(-3, other.getCenterX(), DELTA);
        assertEquals(2, zero.getCenterX(), DELTA);
    }

    @Test
    public void testGetCenterY() {
        assertEquals(0, unit.getCenterY(), DELTA);
        assertEquals(-20, other.getCenterY(), DELTA);
        assertEquals(2, zero.getCenterY(), DELTA);
    }

    @Test
    public void testGetCircumference() {
        assertEquals(2 * Math.PI, unit.getCircumference(), DELTA);
        assertEquals(20 * Math.PI, other.getCircumference(), DELTA);
        assertEquals(0, zero.getCircumference(), DELTA);
    }

    @Test
    public void testGetDiameter() {
        assertEquals(2, unit.getDiameter(), DELTA);
        assertEquals(20, other.getDiameter(), DELTA);
        assertEquals(0, zero.getDiameter(), DELTA);
    }

    @Test
    public void testGetRadius() {
        assertEquals(1, unit.getRadius(), DELTA);
        assertEquals(10, other.getRadius(), DELTA);
        assertEquals(0, zero.getRadius(), DELTA);
    }

    @Test
    public void testToPolygon() {
        Polygon polygon = unit.toPolygon(4);
        assertEquals(polygon.getPointCount(), 4);
        assertEquals(1, polygon.getPointX(0), DELTA);
        assertEquals(0, polygon.getPointY(0), DELTA);

        assertEquals(0, polygon.getPointX(1), DELTA);
        assertEquals(1, polygon.getPointY(1), DELTA);

        assertEquals(-1, polygon.getPointX(2), DELTA);
        assertEquals(0, polygon.getPointY(2), DELTA);

        assertEquals(0, polygon.getPointX(3), DELTA);
        assertEquals(-1, polygon.getPointY(3), DELTA);
    }

    @Test
    public void testSerialize() {
        Data data = new Data();
        data.writeObject("circle", other);
        Circle other2 = data.readObject("circle");
        assertEquals(10, other2.getRadius(), DELTA);
        assertEquals(-3, other2.getCenterX(), DELTA);
        assertEquals(-20, other2.getCenterY(), DELTA);
    }
}
