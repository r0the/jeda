package ch.jeda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConvertTest {

    private List<Integer> intList;

    public ConvertTest() {
        intList = new ArrayList<Integer>();
        intList.add(1);
        intList.add(2);
        intList.add(3);
        intList.add(4);
    }

    @Test
    public void testToString() {
        assertEquals("String, int=42, boolean=true, float=3.141",
                     Convert.toString("String", ", int=", 42, ", boolean=", true, ", float=", 3.141f));
        assertEquals("[1, 2, 3, 4]", Convert.toString(new int[]{1, 2, 3, 4}));
        assertEquals("[1, 2, 3, 4]", Convert.toString(intList));
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "eins");
        map.put(2, "zwei");
        assertEquals("{1: eins, 2: zwei}", Convert.toString(map));
    }

}
