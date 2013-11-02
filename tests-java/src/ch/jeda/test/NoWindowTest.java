package ch.jeda.test;

import ch.jeda.*;
import ch.jeda.event.*;
import ch.jeda.ui.*;

public class NoWindowTest extends Program {
    
    @Override
    public void run() {
        Sound sound = new Sound("res:/raw/rooster.wav");
        sound.play();
//        Thread[] ts = new Thread[Thread.currentThread().getThreadGroup().activeCount()];
//        Thread.currentThread().getThreadGroup().enumerate(ts);
//        for (Thread t: ts) {
//            System.out.println(t.getName() + ": daemon=" + t.isDaemon());
//            System.out.flush();
//        }
    }
}
