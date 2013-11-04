package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToStringTest extends Program {

    @Override
    public void run() {
        write("String", ", int=", 42, ", boolean=", true, ", float=", 3.141f, "\n");
        int[] iarr = {1, 2, 3, 4};
        write(iarr, "\n");
        List<Integer> ilist = Util.intList(1, 2, 3, 4);
        write(ilist, "\n");
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, "eins");
        map.put(2, "zwei");
        write(map);
    }
}
