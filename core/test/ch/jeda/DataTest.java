package ch.jeda;

import ch.jeda.event.Key;
import ch.jeda.ui.Color;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataTest {

    private static final double DELTA = 1E-18;
    private static final String KEY = "key";
    private static final String NO_KEY = "nokey";

    @Test
    public void booleanTest() {
        final Data data = new Data();
        // test read/write
        data.writeBoolean(KEY, true);
        assertEquals(true, data.readBoolean(KEY));
        assertEquals("<data><key>true</key></data>", data.toLine());
        data.writeBoolean(KEY, false);
        assertEquals(false, data.readBoolean(KEY));
        assertEquals("<data><key>false</key></data>", data.toLine());
        // test default value for invalid value
        data.writeString(KEY, "not a boolean");
        assertEquals(true, data.readBoolean(KEY, true));
        assertEquals(false, data.readBoolean(KEY, false));
        // test default value for non-existing key
        assertEquals(true, data.readBoolean(NO_KEY, true));
        assertEquals(false, data.readBoolean(NO_KEY, false));
        // test default default value
        assertEquals(false, data.readBoolean(NO_KEY));
        // test reading of null value
        data.writeString(KEY, null);
        assertEquals(false, data.readBoolean(KEY));
    }

    @Test
    public void doubleTest() {
        final Data data = new Data();
        // test read/write
        data.writeDouble(KEY, Math.PI);
        assertEquals(Math.PI, data.readDouble(KEY), DELTA);
        assertEquals("<data><key>3.141592653589793</key></data>", data.toLine());
        data.writeDouble(KEY, 1e300);
        assertEquals(1e300, data.readDouble(KEY), DELTA);
        assertEquals("<data><key>1.0E300</key></data>", data.toLine());
        // test default value for invalid value
        data.writeString(KEY, "not a double");
        assertEquals(-Math.E, data.readDouble(KEY, -Math.E), DELTA);
        // test default value for non-existing key
        assertEquals(-Math.E, data.readDouble(NO_KEY, -Math.E), DELTA);
        // test default default value
        assertEquals(0.0, data.readDouble(NO_KEY), DELTA);
        // test reading of null value
        data.writeString(KEY, null);
        assertEquals(0.0, data.readDouble(KEY), DELTA);
    }

    @Test
    public void floatTest() {
        final Data data = new Data();
        // test read/write
        data.writeFloat(KEY, 3.141f);
        assertEquals(3.141f, data.readFloat(KEY), DELTA);
        assertEquals("<data><key>3.141</key></data>", data.toLine());
        data.writeFloat(KEY, 1e20f);
        assertEquals(1e20f, data.readFloat(KEY), DELTA);
        assertEquals("<data><key>1.0E20</key></data>", data.toLine());
        // test default value for invalid value
        data.writeString(KEY, "not a float");
        assertEquals(-3.141f, data.readFloat(KEY, -3.141f), DELTA);
        // test default value for non-existing key
        assertEquals(-3.141f, data.readFloat(NO_KEY, -3.141f), DELTA);
        // test default default value
        assertEquals(0.0, data.readFloat(NO_KEY), DELTA);
        // test reading of null value
        data.writeString(KEY, null);
        assertEquals(0f, data.readFloat(KEY), DELTA);
    }

    @Test
    public void intTest() {
        final Data data = new Data();
        // test read/write
        data.writeInt(KEY, 42);
        assertEquals(42, data.readInt(KEY));
        assertEquals("<data><key>42</key></data>", data.toLine());
        data.writeInt(KEY, Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, data.readInt(KEY));
        assertEquals("<data><key>-2147483648</key></data>", data.toLine());
        // test default value for invalid value
        data.writeString(KEY, "not an int");
        assertEquals(-42, data.readInt(KEY, -42));
        // test default value for non-existing key
        assertEquals(-42, data.readInt(NO_KEY, -42));
        // test default default value
        assertEquals(0, data.readInt(NO_KEY));
        // test reading of null value
        data.writeString(KEY, null);
        assertEquals(0, data.readInt(KEY));
    }

    @Test
    public void longTest() {
        final Data data = new Data();
        // test read/write
        data.writeLong(KEY, 42l);
        assertEquals(42l, data.readLong(KEY));
        assertEquals("<data><key>42</key></data>", data.toLine());
        data.writeLong(KEY, Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, data.readLong(KEY));
        assertEquals("<data><key>-9223372036854775808</key></data>", data.toLine());
        // test default value for invalid value
        data.writeString(KEY, "not a long");
        assertEquals(-42l, data.readLong(KEY, -42l));
        // test default value for non-existing key
        assertEquals(-42l, data.readLong(NO_KEY, -42l));
        // test default default value
        assertEquals(0l, data.readLong(NO_KEY));
        // test reading of null value
        data.writeString(KEY, null);
        assertEquals(0l, data.readLong(KEY));
    }

    @Test
    public void stringTest() {
        final Data data = new Data();
        // test read/write
        data.writeString(KEY, "Hello");
        assertEquals("Hello", data.readString(KEY));
        assertEquals("<data><key>Hello</key></data>", data.toLine());
        data.writeString(KEY, "");
        assertEquals("", data.readString(KEY));
        assertEquals("<data><key/></data>", data.toLine());
        data.writeString(KEY, null);
        assertEquals(null, data.readString(KEY));
        assertEquals("<data><key null=\"true\"/></data>", data.toLine());
        // test default value for non-existing key
        assertEquals("World", data.readString(NO_KEY, "World"));
        // test default default value
        assertEquals(null, data.readString(NO_KEY));
    }

    @Test
    public void escapeTest() {
        final Data data = new Data();
        data.writeString(KEY, "'\\\u000C><&\n\u2029\"\r\u2028\u0085");
        System.out.println(data.toLine());
        assertEquals("'\\\u000C><&\n\u2029\"\r\u2028\u0085", data.readString(KEY));
        assertEquals("<data><key>\\a\\b\\f\\g\\l\\m\\n\\p\\q\\r\\s\\x</key></data>", data.toLine());
    }

    @Test
    public void intArrayTest() {
        final Data data = new Data();
        final int[] squares = {1, 4, 9, 16, 25};
        data.writeInts(KEY, squares);
        assertEquals(1, data.readInt(KEY));
        assertArrayEquals(squares, data.readInts(KEY));
    }

    @Test
    public void objectListTest() {
        final Data data = new Data();
        final List<Color> colors = new ArrayList<Color>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        data.writeObjectList("Colors", colors);
        assertEquals(colors, data.readObjectList("Colors"));
    }

    @Test
    public void stringArrayTest() {
        final Data data = new Data();
        final String[] days = {"Monday", "Tuesday", null, "Wednesday"};
        data.writeStrings(KEY, days);
        assertEquals("<data><key>Monday</key><key>Tuesday</key><key null=\"true\"/><key>Wednesday</key></data>", data.toLine());
        assertEquals("Monday", data.readString(KEY));
        assertArrayEquals(days, data.readStrings(KEY));
        data.save("datatest1.xml");
        // array of one element
        data.writeString(KEY, "Hello");
        data.save("datatest2.xml");
        assertArrayEquals(new String[]{"Hello"}, data.readStrings(KEY));
    }

    @Test
    public void invalidNameTest() {
        Data data = new Data();
        // test read/write
        try {
            data.writeInt("invalid tag", 42);
            fail("IllegalArgumentException expected");
        }
        catch (IllegalArgumentException ex) {
            // ok
        }
    }

    @Test
    public void fileTest() {
        Data data = createSample();
        data.save("test.xml");
        data = new Data("test.xml");
        assertEquals(true, data.readBoolean("a_boolean"));
        assertEquals(Math.PI, data.readDouble("a_double"), DELTA);
        assertEquals((float) Math.E, data.readFloat("a_float"), DELTA);
        assertEquals(42, data.readInt("an_int"));
        assertArrayEquals(new int[]{1, 4, 9, 16, 25}, data.readInts("squares"));
    }

    private Data createSample() {
        Data result = new Data();
        result.writeBoolean("a_boolean", true);
        result.writeDouble("a_double", Math.PI);
        result.writeFloat("a_float", (float) Math.E);
        result.writeInt("an_int", 42);
        result.writeInts("squares", new int[]{1, 4, 9, 16, 25});
        result.writeKey("a_key", Key.ALT_GRAPH);
        result.writeString("a_string", "Hello, World");
        result.writeObject("a_color", Color.AMBER_100);
        return result;
    }
}
