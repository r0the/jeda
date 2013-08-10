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

import ch.jeda.Engine;
import ch.jeda.Simulation;
import ch.jeda.Transformation;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Window;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.IViewportTransform;
import org.jbox2d.common.OBBViewportTransform;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;

/**
 * This class represents a virtual world, which is a simulation with a graphical representation. This class extends the
 * {@link ch.jeda.Simulation} class and implements a simulation loop that renders to a {@link ch.jeda.ui.Window}. A jeda
 * world can contain the following kinds of objects:
 * <ul>
 * <li>A <b>body</b> is subject to the laws of physics and is controlled by a physics engine. Bodies can obly be used
 * when an appropriate plugin is used.
 * <li>A <b>particle</b> is subject to the laws of physics and is controlled by a physics engine. Particles can obly be
 * used when an appropriate plugin is used.
 * <li>A <b>figure</b> is a graphic
 * </ul>
 *
 * @since 1
 */
public class World extends Simulation {

    static final float FACTOR = 2f;
    private final int velocityIterations;
    private final int positionIterations;
    protected static final Color DEBUG_OVERLAY_BG_COLOR = new Color(255, 200, 200);
    protected static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    private static final Transformation IDENTITY = new Transformation();
    private static final EnumSet<Window.Feature> NO_FEATURES = EnumSet.noneOf(Window.Feature.class);
    private final Set<WorldFeature> features;
    private final WorldState defaultState;
    private final org.jbox2d.dynamics.World physics;
    private final Map<Class<? extends WorldState>, WorldState> states;
    private Class<? extends WorldState> nextState;
    private final Window window;
    private Objects objects;
    private boolean paused;
    private WorldState state;

    public final void addObject(final WorldObject object) {
        if (object == null) {
            throw new NullPointerException("object");
        }

        this.objects.add(object);
        this.window.addEventListener(object);
        if (object instanceof PhysicsObject) {
            ((PhysicsObject) object).addToWorld(this.physics);
        }
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

    public final WorldObject[] getObjects() {
        return this.objects.getAll();
    }

    /**
     * Checks whether the specified world feature is enabled. See {@link WorldFeature} for more information.
     *
     * @return <tt>true</tt> if specified world feature is enabled
     * @see #setFeature(ch.jeda.ui.Window.Feature, boolean)
     */
    public final boolean hasFeature(final WorldFeature feature) {
        if (feature == null) {
            return false;
        }

        return this.features.contains(feature);
    }

    public final boolean hasFeature(final Window.Feature feature) {
        return this.window.hasFeature(feature);
    }

    public void removeObject(final WorldObject object) {
        if (object == null) {
            throw new NullPointerException("object");
        }

        this.objects.remove(object);
        this.window.removeEventListener(object);
        if (object instanceof PhysicsObject) {
            ((PhysicsObject) object).removeFromWorld(this.physics);
        }
    }

    /**
     * Enables or disables the specified world feature. See {@link WorldFeature} for more information.
     *
     * @param enable <tt>true</tt> to enabled debug feature,
     * <tt>false</tt> to disable it
     * @see #hasFeature(ch.jeda.world.WorldFeature)
     */
    public final void setFeature(final WorldFeature feature, final boolean enable) {
        if (enable) {
            this.features.add(feature);
        }
        else {
            this.features.remove(feature);
        }
    }

    public final void setFeature(final Window.Feature feature, final boolean enabled) {
        this.window.setFeature(feature, enabled);
    }

    public final void setGravity(float gx, float gy) {
        this.physics.setGravity(new Vec2(gx, gy));
    }

    public final void setPaused(final boolean paused) {
        this.paused = paused;
    }

    public final void setState(final Class<? extends WorldState> state) {
        if (state == null) {
            throw new NullPointerException("state");
        }

        if (!this.states.containsKey(state)) {
            Engine.getContext().warning("jeda.game.state.error", state);
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
    public final void setTitle(final String title) {
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
     * Creates a new world with a window that has a drawing area of the specified size.
     *
     * @param width the width of the window's drawing area
     * @param height the height of the window's drawing area
     * @throws IllegalArgumentException if width or height are smaller than 1
     * @since 1
     */
    protected World(final int width, final int height) {
        this(width, height, NO_FEATURES);
    }

    protected World(final int width, final int height,
                    final Window.Feature... features) {
        this(width, height, toSet(features));
    }

    protected final void addState(final WorldState state) {
        if (state == null) {
            throw new NullPointerException("state");
        }

        this.states.put(state.getClass(), state);
    }

    protected void drawBackground(final Canvas canvas) {
        canvas.setColor(Color.WHITE);
        canvas.fill();
    }

    protected void drawOverlay(final Canvas canvas) {
    }

    @Override
    protected void init() {
    }

    /**
     * This method is overridden to implement the simulation step for a world.
     */
    @Override
    public final void step() {
        // --------------------------------------------------------------------
        // 1. Initialization phase
        // 1.1 Check if we need to change the state.
        this.checkState();
        // 1.2 Process pending insertions and deletions
        this.objects.processPending();
        // --------------------------------------------------------------------
        // 3. Update phase
        final float dt = (float) this.getLastStepDuration();
        // 3.1 Process events
        this.window.processEvents();
        // 3.2 Update state
        this.state.update();
        this.physics.step(dt, this.velocityIterations, this.positionIterations);
        if (!this.paused) {
            // 3.3 Update objects
        }

        // --------------------------------------------------------------------
        // 4. Draw phase
        // 4.1 Draw state background
        this.state.drawBackground(this.window);
        // 4.2 Draw entities
        this.objects.draw(this.window);

        this.window.setTransformation(IDENTITY);
        // 4.3 Draw debug overlay for entities
        if (this.hasFeature(WorldFeature.SHOW_COLLISION_SHAPES)) {
            this.physics.drawDebugData();
        }

        // 4.4 Draw state overlay
        this.state.drawOverlay(this.window);
        // 4.5 Draw world debug overlay
        this.drawDebugOverlay();
        // --------------------------------------------------------------------
        // 5. Update window
        this.window.update();
    }

    protected void update() {
    }

    private World(final int width, final int height, final EnumSet<Window.Feature> features) {
        features.add(Window.Feature.DoubleBuffered);
        this.features = EnumSet.noneOf(WorldFeature.class);
        this.defaultState = new WorldState();
        this.window = new Window(width, height, features.toArray(new Window.Feature[0]));
        this.nextState = null;
        this.objects = new Objects();
        this.state = this.defaultState;
        this.state.notifyEnter(this);
        this.states = new HashMap<Class<? extends WorldState>, WorldState>();
        this.window.addEventListener(this);
        this.velocityIterations = 8;
        this.positionIterations = 3;
        // Init physics
        this.physics = new org.jbox2d.dynamics.World(new Vec2());
        final OBBViewportTransform viewport = new OBBViewportTransform();
        final float halfWidth = this.window.getWidth() / 2f;
        final float halfHeight = this.window.getHeight() / 2f;
        viewport.setExtents(halfWidth, halfHeight);
        viewport.setCenter(halfWidth, halfHeight);
        viewport.setYFlip(true);
        this.physics.setDebugDraw(new DebugDrawAdapter(this.window, viewport));
    }

    private void drawDebugOverlay() {
        if (this.hasFeature(WorldFeature.SHOW_WORLD_INFO)) {
            this.window.setColor(DEBUG_OVERLAY_BG_COLOR);
            this.window.fillRectangle(5, 5, this.window.getWidth() - 10, 25);
            this.window.setColor(DEBUG_TEXT_COLOR);
            this.window.drawText(10, 10, "FPS: " + this.getCurrentFrequency() +
                                         ", Last Step Duration: " + (int) (this.getLastStepDuration() * 1000) +
                                         "ms, Objects: " + this.objects.count());
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
            this.state = this.defaultState;
        }

        this.nextState = null;
        this.state.notifyEnter(this);
    }

    private static EnumSet<Window.Feature> toSet(final Window.Feature... features) {
        final EnumSet<Window.Feature> result = EnumSet.noneOf(Window.Feature.class);
        for (int i = 0; i < features.length; ++i) {
            result.add(features[i]);
        }

        return result;
    }

    private static final class DebugDrawAdapter extends DebugDraw {

        private final Canvas canvas;

        public DebugDrawAdapter(final Canvas canvas, final IViewportTransform viewport) {
            super(viewport);
            this.canvas = canvas;
            this.setFlags(e_shapeBit | e_centerOfMassBit);
        }

        @Override
        public void drawCircle(Vec2 center, float radius, Color3f color) {
            this.canvas.setColor(convertColor(color));
            this.canvas.drawCircle(center.x * FACTOR, center.y * FACTOR, radius);
        }

        @Override
        public void drawPoint(final Vec2 point, final float radius, final Color3f color) {
            this.canvas.setColor(convertColor(color));
            this.canvas.fillCircle(point.x * FACTOR, point.y * FACTOR, radius);
        }

        @Override
        public void drawSegment(Vec2 p1, Vec2 p2, Color3f color) {
            this.canvas.setColor(convertColor(color));
            this.canvas.drawLine(p1.x * FACTOR, p1.y * FACTOR, p2.x * FACTOR, p2.y * FACTOR);
        }

        @Override
        public void drawSolidCircle(final Vec2 center, final float radius, final Vec2 axis, final Color3f color) {
            this.canvas.setColor(convertColor(color));
            this.canvas.fillCircle(center.x, center.y, radius);
        }

        @Override
        public void drawSolidPolygon(final Vec2[] vertices, int vertexCount, final Color3f color) {
            final float[] points = new float[2 * vertexCount];
            for (int i = 0; i < vertexCount; ++i) {
                points[2 * i] = vertices[i].x * FACTOR;
                points[2 * i + 1] = vertices[i].y * FACTOR;
            }

            this.canvas.setColor(convertColor(color));
            this.canvas.drawPolygon(points);
        }

        @Override
        public void drawString(final float x, final float y, final String s, final Color3f color) {
            this.canvas.setColor(convertColor(color));
            this.canvas.drawText(x * FACTOR, y * FACTOR, s);
        }

        @Override
        public void drawTransform(final Transform t) {
        }

        private static Color convertColor(final Color3f color) {
            return new Color((int) (255f * color.x), (int) (255f * color.y), (int) (255f * color.z));
        }
    }
}
