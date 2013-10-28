package ch.jeda.test;

import ch.jeda.Plugin;
import ch.jeda.event.Event;
import ch.jeda.event.EventType;
import java.util.ArrayList;
import java.util.List;

public class TestPlugin implements Plugin {

    @Override
    public void initialize() throws Exception {
        System.out.println("Init plugin");
    }

    public String name() {
        return "Test Plugin";
    }

    public Event[] processEvents(Event[] events) {
        List<Event> result = new ArrayList<Event>();
        for (int i = 0; i < events.length; ++i) {
            if (events[i].getType() != EventType.KEY_TYPED) {
                result.add(events[i]);
            }
        }
        return result.toArray(new Event[result.size()]);
    }
}
