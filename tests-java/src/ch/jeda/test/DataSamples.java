package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class DataSamples extends Program {

    @Override
    public void run() {
        sample1();
        sample2();
        sample3();
    }

    private void sample1() {
        Data data = new Data();
        data.writeInt("Die_Antwort", 42);
        data.save("sample1.xml");
    }

    private void sample2() {
        Data data = new Data();
        data.writeString("A", "Hello");
        data.writeString("B", "");
        data.writeString("C", null);
        data.save("sample2.xml");
    }

    private void sample3() {
        Kreis kreis = new Kreis(100, Color.RED);
        Data data = new Data();
        data.writeObject("ein_Kreis", kreis);
        data.save("sample3.xml");
    }
}

class Kreis implements Storable {

    private int radius;
    private Color farbe;

    public Kreis(Data data) {
        radius = data.readInt("radius", 50);
        farbe = data.readObject("farbe", Color.BLACK);
    }

    public Kreis(int radius, Color farbe) {
        this.radius = radius;
        this.farbe = farbe;
    }

    public void writeTo(Data data) {
        data.writeInt("radius", radius);
        data.writeObject("farbe", farbe);
    }
}
