/*
 * Copyright (C) 2011 - 2013 by Stefan Rothe
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
package ch.jeda.world;

import ch.jeda.Log;
import ch.jeda.Simulation;
import ch.jeda.Size;
import ch.jeda.Transformation;
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Events;
import ch.jeda.ui.Window;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a virtual world, which is a simulation with a graphical
 * representation. This class extends the {@link ch.jeda.Simulation} class and
 * implements a simulation loop that renders to a {@link ch.jeda.ui.Window}.
 *
 * @since 1
 */
public class World extends Simulation {

    private static final EnumSet<Window.Feature> NO_FEATURES = EnumSet.noneOf(Window.Feature.class);
    protected static final Color DEBUG_FILL_COLOR = new Color(255, 0, 0, 70);
    protected static final Color DEBUG_OUTLINE_COLOR = Color.RED;
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;

    public enum DebugFeature {

        FRAMERATE, ENTITY_OVERLAY
    }
    private final Set<DebugFeature> debugFeatures;
    private final Entities entities;
    private final Map<Class<? extends WorldState>, WorldState> states;
    private Class<? extends WorldState> nextState;
    private final Window window;
    private boolean paused;
    private WorldState state;

    public final void addEntity(Entity entity) {
        if (entity == null) {
            throw new NullPointerException("entity");
        }

        this.entities.add(entity);
    }

    /**
     * Returns a list of entities that intersect with the specified shape. Only
     * entities that are instances of the class specified in
     * <code>type</code> or a subclass thereof are returned. Pass
     * <code>Entity.class</code> to return all actors intersecting with the
     * shape.
     *
     * If
     * <code>null</code> is passed as shape, an empty list will be returned.
     *
     * The returned list is a newly created copy, it may be modified.
     *
     * @param <T> base class of all entities to be returned
     * @param shape shape to intersect with the entities
     * @param type base class of all entities to be returned
     * @return list of entities
     */
    public final <T extends Entity> List<T> collidingEntities(Shape shape, Class<T> type) {
        return this.entities.getIntersectingActors(shape, type);
    }

    public final <T extends Entity> List<T> collidingEntities(Entity entity, Class<T> type) {
        return collidingEntities(entity.getCollisionShape(), type);
    }

    public final <T extends Entity> List<T> entities(Class<T> type) {
        return this.entities.byType(type);
    }

    public final Events getEvents() {
        return this.window.getEvents();
    }

    /**
     * Returns the height of the drawing area.
     *
     * @return height of the drawing area
     *
     * @since 1
     */
    public final int getCanvasHeight() {
        return this.window.getHeight();
    }

    /**
     * Returns the title of this worlds's window.
     *
     * @return current window title
     * @see #setTitle(java.lang.String)
     * @since 1
     */
    public final String getTitle() {
        return this.window.getTitle();
    }

    /**
     * Returns the width of the drawing area.
     *
     * @return width of the drawing area
     *
     * @since 1
     */
    public final int getCanvasWidth() {
        return this.window.getWidth();
    }

    /**
     * Checks whether the specified debug feature is enabled. See
     * {@link DebugFeature} for more information.
     *
     * @return <code>true</code> if specified debug feature is enabled
     * @see #setDebugFeature(ch.jeda.physics.DebugFeature, boolean)
     */
    public final boolean hasDebugFeature(DebugFeature feature) {
        if (feature == null) {
            return false;
        }

        return this.debugFeatures.contains(feature);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Enables or disables the specified debug feature. See {@link DebugFeature}
     * for more information.
     *
     * @param enable <code>true</code> to enabled debug feature,
     * <code>false</code> to disable it
     * @see #hasDebugFeature(ch.jeda.physics.DebugFeature)
     */
    public final void setDebugFeature(DebugFeature feature, boolean enable) {
        if (feature == null) {
            return;
        }
        else if (enable) {
            this.debugFeatures.add(feature);
        }
        else {
            this.debugFeatures.remove(feature);
        }
    }

    public final void setPaused(boolean paused) {
        this.paused = paused;
    }

    public final void setState(Class<? extends WorldState> state) {
        if (state == null) {
            throw new NullPointerException("state");
        }

        if (!this.states.containsKey(state)) {
            Log.error("jeda.game.state.error", state);
        }

        this.nextState = state;
    }

    /**
     * Sets the title of the simulations's window.
     *
     * @param title new title of the window
     * @throws NullPointerException if title is null
     * @see #getTitle()
     * @since 1
     */
    public final void setTitle(String title) {
        this.window.setTitle(title);
    }

    /**
     * Creates a new world with a default-sized window.
     *
     * @see Window#Window()
     * @since 1
     */
    protected World() {
        this(0, 0);
    }

    /**
     * Creates a new world with a window that has a drawing area of the
     * specified size.
     *
     * @param width the width of the window's drawing area
     * @param height the height of the window's drawing area
     * @throws IllegalArgumentException if width or height are smaller than 1
     * @since 1
     */
    protected World(int width, int height) {
        this(width, height, NO_FEATURES);
    }

    protected World(int width, int height, Window.Feature... features) {
        this(width, height, toSet(features));
    }

    protected final void addState(WorldState state) {
        if (state == null) {
            throw new NullPointerException("state");
        }

        this.states.put(state.getClass(), state);
    }

    protected void drawBackground(Canvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.fill();
    }

    protected void drawOverlay(Canvas canvas) {
    }

    @Override
    protected void init() {
    }

    /**
     * This method is overridden to implement the simulation step for a world.
     */
    @Override
    protected final void step() {
        // 1. Check if we need to change the state.
        this.checkState();
        // 2. Update entities (inserts, deletions)
        this.entities.executePendingOperations();
        // --------------------------------------------------------------------
        // 3. Update phase
        // 3.1 Update state
        this.state.update(this.window.getEvents());
        // 3.2 Update entities
        if (!this.paused) {
            for (Entity entity : this.entities.updateOrderIterator()) {
                entity.update(this);
            }
        }
        // --------------------------------------------------------------------
        // 4. Draw phase
        // 4.1 Draw state background
        this.state.drawBackground(this.window);
        // 4.2 Draw entities
        for (Entity entity : this.entities.paintOrderIterator()) {
            entity.draw(this.window);
        }

        this.window.setTransformation(Transformation.createIdentity());
        // 4.3 Draw debug overlay for entities
        if (this.hasDebugFeature(DebugFeature.ENTITY_OVERLAY)) {
            for (Entity entity : this.entities.paintOrderIterator()) {
                entity.drawDebugOverlay(this.window);
            }
        }

        // 4.4 Draw state overlay
        this.state.drawOverlay(this.window);
        // 4.5 Draw world debug overlay
        this.drawDebugOverlay();
        // --------------------------------------------------------------------
        // 5. Update window
        this.window.update();
    }

    protected void update(Events events) {
    }

    private World(int width, int height, EnumSet<Window.Feature> features) {
        features.add(Window.Feature.DoubleBuffered);
        this.debugFeatures = EnumSet.noneOf(DebugFeature.class);
        this.entities = new Entities();
        this.window = new Window(width, height, features.toArray(new Window.Feature[0]));
        this.nextState = null;
        this.state = WorldState.NULL;
        this.state.notifyEnter(this);
        this.states = new HashMap<Class<? extends WorldState>, WorldState>();
    }

    private void drawDebugOverlay() {
        if (hasDebugFeature(DebugFeature.FRAMERATE)) {
            this.window.setColor(new Color(255, 200, 200));
            this.window.fillRectangle(5, 5, this.window.getWidth() - 10, 25);
            this.window.setColor(DEBUG_TEXT_COLOR);
            this.window.drawText(10, 10, "FPS: " + getCurrentFrequency() +
                                         ", Last Step Duration: " + (int) (getLastStepDuration() * 1000) +
                                         "ms, Entities: " + this.entities.getEntityCount());
        }
    }

    private void checkState() {
        if (this.nextState == null) {
            return;
        }

        this.state.notifyExit();
        if (this.states.containsKey(this.nextState)) {
            this.state = this.states.get(this.nextState);
        }
        else {
            this.state = WorldState.NULL;
        }

        this.nextState = null;
        this.state.notifyEnter(this);
    }

    private static EnumSet<Window.Feature> toSet(Window.Feature... features) {
        final EnumSet<Window.Feature> result = EnumSet.noneOf(Window.Feature.class);
        for (Window.Feature feature : features) {
            result.add(feature);
        }

        return result;
    }
}
