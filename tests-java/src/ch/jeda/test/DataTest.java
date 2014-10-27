package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class DataTest extends Program {

    private static final String NAME = "multi-\nline name with equal = and comma ,";
    private static final String VALUE = "multi-\nline value with equal = and comma ,";

    @Override
    public void run() {
        Data data = new Data();
        data.writeBoolean("Boolean", true);
        data.writeObject("Color", Color.JEDA);
        data.writeDouble("Double", 3.141);
        data.writeInt("Int", 42);
        data.writeKey("Key", Key.CTRL_LEFT);
        data.writeString(NAME, VALUE);

        int[] values = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        data.writeInts("array", values);

        data.writeObject("circle", new Circle(42));

        writeLines(data.toString());

        data = new Data(data.toLine());

        boolean b = data.readBoolean("Boolean");
        if (b != true) {
            writeLines("Boolean failed");
        }

        Color color = data.readObject("Color");
        if (!color.equals(Color.JEDA)) {
            writeLines("Color failed");
        }

        double d = data.readDouble("Double");
        if (d != 3.141) {
            writeLines("Double failed");
        }

        int in = data.readInt("Int");
        if (in != 42) {
            writeLines("Int failed");
        }

        Key key = data.readKey("Key");
        if (!key.equals(Key.CTRL_LEFT)) {
            writeLines("Key failed");
        }

        String s = data.readString(NAME);
        if (!s.equals(VALUE)) {
            writeLines("String failed");
        }

        int[] array = data.readInts("array");
        if (array.length != values.length) {
            writeLines("array failed");
        }

        for (int i = 0; i < array.length; ++i) {
            if (array[i] != values[i]) {
                writeLines("array failed");
            }
        }

        Circle circle = data.readObject("circle");
        if (circle.getRadius() != 42) {
            writeLines("Circle failed");
        }
    }

    public static class Circle implements Storable {

        private int radius;

        public Circle(Data data) {
            radius = data.readInt("radius");
        }

        public int getRadius() {
            return radius;
        }

        public Circle(int radius) {
            this.radius = radius;
        }

        @Override
        public void writeTo(Data data) {
            data.writeInt("radius", radius);
        }

    }
}
