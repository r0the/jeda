/*
 * Copyright (C) 2012 by Stefan Rothe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY); without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ch.jeda.platform.android;

import android.view.MotionEvent;
import ch.jeda.Location;
import ch.jeda.platform.EventsImp;
import ch.jeda.ui.Key;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AndroidEventsImp implements EventsImp {

    private final EventQueue<MotionEvent> motionEventQueue;
    private final Set<Key> pressedKeys;
    private final List<Key> typedKeys;
    private boolean clickCandidate;
    private boolean clicked;
    private Location pointerLastLocation;
    private Location pointerLocation;
    private Location pointerMovement;
    private String typedChars;

    AndroidEventsImp() {
        this.motionEventQueue = new EventQueue();
        this.pressedKeys = new HashSet();
        this.typedKeys = new ArrayList();

        this.typedChars = "";
    }

    @Override
    public Location getPointerLocation() {
        return this.pointerLocation;
    }

    @Override
    public Location getPointerMovement() {
        return this.pointerMovement;
    }

    @Override
    public Set<Key> getPressedKeys() {
        return Collections.unmodifiableSet(this.pressedKeys);
    }

    @Override
    public String getTypedChars() {
        return this.typedChars;
    }

    @Override
    public List<Key> getTypedKeys() {
        return Collections.unmodifiableList(this.typedKeys);
    }

    @Override
    public boolean isClicked() {
        return this.clicked;
    }

    public boolean isDragging() {
        return this.pointerLocation != null && !this.clickCandidate;
    }

    void addMotionEvent(MotionEvent event) {
        this.motionEventQueue.add(event);
    }

    void update() {
        this.typedChars = "";
        this.typedKeys.clear();

        this.clicked = false;
        this.pointerLastLocation = this.pointerLocation;

        this.motionEventQueue.swap();

        for (MotionEvent event : this.motionEventQueue) {
            this.handleMotionEvent(event);
        }

        if (this.pointerLocation != null && this.pointerLastLocation != null) {
            this.pointerMovement = this.pointerLocation.relativeTo(this.pointerLastLocation);
        }
        else {
            this.pointerMovement = Location.ORIGIN;
        }

        if (!this.pointerMovement.isOrigin()) {
            this.clickCandidate = false;
        }
    }

    private void handleMotionEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.updatePointerLocation(event);
                this.clickCandidate = true;
                break;
            case MotionEvent.ACTION_MOVE:
                this.updatePointerLocation(event);
                break;
            case MotionEvent.ACTION_CANCEL:
                this.pointerLocation = null;
                this.clickCandidate = false;
                break;
            case MotionEvent.ACTION_UP:
                this.pointerLocation = null;
                if (this.clickCandidate) {
                    this.clicked = true;
                }

                break;
        }
    }

    private void updatePointerLocation(MotionEvent event) {
        this.pointerLocation = new Location((int) event.getX(), (int) event.getY());
    }
}
