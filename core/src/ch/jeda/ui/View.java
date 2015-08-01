/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.ui;

import ch.jeda.Jeda;
import ch.jeda.JedaInternal;
import ch.jeda.event.Button;
import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.EventType;
import ch.jeda.event.Key;
import ch.jeda.event.KeyEvent;
import ch.jeda.event.PointerEvent;
import ch.jeda.event.PointerListener;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.event.WheelListener;
import ch.jeda.platform.ViewCallback;
import ch.jeda.platform.ViewImp;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents the visualization of a virtual world. The view is where the user interacts with the program. On the Java
 * platform (Window, Mac OS, Linux), a view is contained in the application window. On Android the view is the visible
 * part of the app.
 * <p>
 * A Jeda view has a <b>background</b> and contains {@link ch.jeda.ui.Element}s, which are objects that live in the
 * virtual world and have a graphical representation. The view also receives {@link ch.jeda.event.Event}s from the user
 * and the operating system.
 * </p><p>
 * The virtual world has a mathematical, cartesian coordinate system. That means the the x axis points to the right and
 * the y axis points upwards.
 *
 * @since 2.0
 */
public class View {

    private static final float METER_TO_DP = 100f * 160f / 2.54f;
    private static final float DP_TO_METER = 1f / METER_TO_DP;
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 800;
    private static final EnumSet<ViewFeature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final Callback callback;
    private final Object elementLock;
    private final Map<String, Set<Element>> elementsByName;
    private final Set<Element> elementSet;
    private final EventQueue eventQueue;
    private final Set<Element> pendingInsertions;
    private final Set<Element> pendingRemovals;
    private final UserControl userControl;
    private Canvas background;
    private boolean elementLoop;
    private Element[] elements;
    private boolean elementsChanged;
    private Canvas foreground;
    private ViewImp imp;
    private float scale;
    private String title;
    private float translationX;
    private float translationY;

    /**
     * Constructs a view. The view is shown on the screen.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @since 2.0
     */
    public View() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Constructs a view. The view is shown on the screen. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @param features the features of the view
     *
     * @since 2.0
     */
    public View(final ViewFeature... features) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, features);
    }

    /**
     * Constructs a view. The view is shown on the screen. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has the specified
     * <code>width</code> and <code>height</code>.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.</p>
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the view
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 2.0
     */
    public View(final int width, final int height, final ViewFeature... features) {
        callback = new Callback(this);
        elementLock = new Object();
        elementsByName = new HashMap<String, Set<Element>>();
        elementSet = new HashSet<Element>();
        eventQueue = new EventQueue();
        pendingInsertions = new HashSet<Element>();
        pendingRemovals = new HashSet<Element>();
        elementLoop = false;
        elements = new Element[0];
        elementsChanged = false;
        scale = 0.01f;
        title = Jeda.getProgramName();
        userControl = new UserControl(this);
        resetImp(width, height, toSet(features));
        Jeda.addEventListener(eventQueue);
        Jeda.addEventListener(new EventLoop(this));
        eventQueue.addListener(userControl);
    }

    /**
     * Adds a {@link ch.jeda.ui.Element} to this view. Has no effect if <code>element</code> is <code>null</code>.
     *
     * @param element the element to be added to this view
     *
     * @since 2.0
     */
    public final void add(final Element element) {
        if (element != null) {
            synchronized (elementLock) {
                if (elementLoop) {
                    pendingInsertions.add(element);
                    pendingRemovals.remove(element);
                }
                else {
                    doAdd(element);
                }
            }
        }
    }

    /**
     * Adds multiple elements to this view.
     *
     * @param elements the elements to be added to this view
     *
     * @since 2.0
     */
    public final void add(final Element... elements) {
        for (int i = 0; i < elements.length; ++i) {
            add(elements[i]);
        }
    }

    /**
     * Adds an event listener to the view. The specified object will receive events for all events listener interfaces
     * it implements. Has no effect if <code>listener</code> is <code>null</code> or an element of this view.
     *
     * @param listener the event listener
     *
     * @since 2.0
     */
    public final void addEventListener(final Object listener) {
        eventQueue.addListener(listener);
    }

    /**
     * Closes the view. The view becomes invalid, all subsequent method calls to the view will cause an error.
     *
     * @since 2.0
     */
    public final void close() {
        imp.close();
    }

    /**
     * Returns the background canvas of this view. The content of the background canvas is displayed behind all elements
     * of the view.
     *
     * @return the background canvas of this view
     *
     * @since 2.0
     */
    public final Canvas getBackground() {
        return background;
    }

    /**
     * Returns the horizontal world coordinate of the center of this view.
     *
     * @return the horizontal world coordinate of the center of this view
     *
     * @since 2.0
     */
    public final float getCenterX() {
        return toWorldX(foreground.getWidth() / 2f);
    }

    /**
     * Returns the vertical world coordinate of the center of this view.
     *
     * @return the vertical world coordinate of the center of this view
     *
     * @since 2.0
     */
    public final float getCenterY() {
        return toWorldY(foreground.getHeight() / 2f);
    }

    /**
     * Returns an element with the specified name. If more than one element with the specified name are managed by the
     * view, an arbitrary element with the name is returned.
     *
     * @param name the name to look for
     * @return an element with the specified name
     *
     * @since 2.0
     */
    public final Element getElement(final String name) {
        final Set<Element> result = elementsByName.get(name);
        if (result == null) {
            return null;
        }
        else {
            for (final Element element : result) {
                return element;
            }

            return null;
        }
    }

    /**
     * Returns an element of the specified class with the specified name.
     *
     * @param <T> the type of the element to return
     * @param clazz the class of the element to return
     * @param name the name of the element to return
     * @return an element of the specified class with the specified name
     * @throws NullPointerException if <code>clazz</code> is <code>null</code>
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getElements()
     * @see #remove(ch.jeda.ui.Element)
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    public final <T extends Element> T getElement(final Class<T> clazz, final String name) {
        final Set<Element> result = elementsByName.get(name);
        if (result == null) {
            return null;
        }
        else {
            for (final Element element : result) {
                if (clazz.isInstance(element)) {
                    // Unchecked cast
                    return (T) element;
                }
            }

            return null;
        }
    }

    /**
     * Returns all elements currently managed by the view.
     *
     * @return all elements currently managed by the view.
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getElements(java.lang.Class)
     * @see #remove(ch.jeda.ui.Element)
     * @since 2.0
     */
    public final Element[] getElements() {
        return Arrays.copyOf(elements, elements.length);
    }

    /**
     * Returns all elements of the specified class currently managed by the view.
     *
     * @param <T> the type of elements to return
     * @param clazz the class of elements to return
     * @return all elements currently managed by the view.
     * @throws NullPointerException if <code>clazz</code> is <code>null</code>
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getElements()
     * @see #remove(ch.jeda.ui.Element)
     * @since 2.0
     */
    @SuppressWarnings("unchecked")
    public final <T extends Element> T[] getElements(final Class<T> clazz) {
        final List<T> result = new ArrayList<T>();
        for (int i = 0; i < elements.length; ++i) {
            if (clazz.isInstance(elements[i])) {
                // Unchecked cast
                result.add((T) elements[i]);
            }
        }

        // Unchecked cast
        return result.toArray((T[]) Array.newInstance(clazz, result.size()));
    }

    /**
     * Returns all elements with the specified name.
     *
     * @param name the name to look for
     * @return all elements with the specified name
     *
     * @since 2.0
     */
    public final Element[] getElements(final String name) {
        final Set<Element> result = elementsByName.get(name);
        if (result == null) {
            return new Element[0];
        }
        else {
            return result.toArray(new Element[result.size()]);
        }
    }

    /**
     * Returns the height of this view in device-independent pixels (dp).
     *
     * @return the height of this view in dp
     *
     * @since 2.0
     */
    public final float getHeightDp() {
        return foreground.getHeight();
    }

    /**
     * Returns the height of this view in meters.
     *
     * @return the height of this view in meters
     *
     * @since 2.0
     */
    public final float getHeightM() {
        return toWorld(foreground.getHeight());
    }

    /**
     * Returns the current scale of this view.
     *
     * @return the current scale of this view
     *
     * @since 2.0
     */
    public final float getScale() {
        return scale;
    }

    /**
     * Returns the current view title.
     *
     * @return current view title
     *
     * @see #setTitle(java.lang.String)
     * @since 2.0
     */
    public final String getTitle() {
        return title;
    }

    /**
     * Returns the width of this view in device-independent pixels (dp).
     *
     * @return the width of this view in dp
     *
     * @since 2.0
     */
    public final float getWidthDp() {
        return foreground.getWidth();
    }

    /**
     * Returns the width of this view in meters.
     *
     * @return the width of this view in meters
     *
     * @since 2.0
     */
    public final float getWidthM() {
        return toWorld(foreground.getWidth());
    }

    /**
     * Checks for a view feature. Returns <code>true</code> if the specified feature is currently enabled for the view.
     *
     * @param feature the feature to check for
     * @return <code>true</code> if the feature is enabled, otherwise returns <code>false</code>
     *
     * @see #setFeature(ch.jeda.ui.ViewFeature, boolean)
     * @since 2.0
     */
    public final boolean hasFeature(final ViewFeature feature) {
        if (feature == null) {
            return false;
        }
        else {
            return imp.getFeatures().contains(feature);
        }
    }

    /**
     * Removes an {@link ch.jeda.ui.Element} from this view. Has no effect if <code>element</code> is <code>null</code>.
     *
     * @param element the element to be removed from this view
     *
     * @see ch.jeda.ui.Element
     * @since 2.0
     */
    public final void remove(final Element element) {
        if (element != null) {
            synchronized (elementLock) {
                if (elementLoop) {
                    pendingInsertions.remove(element);
                    pendingRemovals.add(element);
                }
                else {
                    doRemove(element);
                }
            }
        }
    }

    /**
     * Remove multiple elements from this view.
     *
     * @param elements the elements to be removed from this view
     *
     * @since 2.0
     */
    public final void remove(final Element... elements) {
        for (int i = 0; i < elements.length; ++i) {
            remove(elements[i]);
        }
    }

    /**
     * Removes an event listener from the view. The specified object will not receive events anymore. Has no effect if
     * <code>listener</code> is <code>null</code> or an element of this view.
     *
     * @param listener the event listener
     * @since 2.0
     */
    public final void removeEventListener(final Object listener) {
        eventQueue.removeListener(listener);
    }

    /**
     * Changes the scale of this view. Multiplies the current scale of this view with the specified factor. Adjusts the
     * translation of this view in order to keep the specified point fixed.
     *
     * @param factor the factor
     *
     * @since 2.0
     */
    public final void scale(final double factor, final double centerX, final double centerY) {
        scale((float) factor, (float) centerX, (float) centerY);
    }

    /**
     * Changes the scale of this view. Multiplies the current scale of this view with the specified factor. Adjusts the
     * translation of this view in order to keep the specified point fixed.
     *
     * @param factor the factor
     *
     * @since 2.0
     */
    public final void scale(final float factor, final float centerX, final float centerY) {
        final float relX = centerX + translationX;
        final float relY = centerY + translationY;
        scale = scale * factor;
        translationX = -(centerX - relX / factor);
        translationY = -(centerY - relY / factor);
    }

    /**
     * Enables or disables a feature of this view.
     *
     * @param feature the feature to be enabled or disabled
     * @param enabled <code>true</code> to enable the feature, <code>false</code> to disable it
     * @throws NullPointerException if <code>feature</code> is <code>null</code>
     *
     * @see #hasFeature(ch.jeda.ui.ViewFeature)
     * @since 2.0
     */
    public final void setFeature(final ViewFeature feature, final boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        if (IMP_CHANGING_FEATURES.contains(feature)) {
            final EnumSet<ViewFeature> featureSet = EnumSet.copyOf(imp.getFeatures());
            if (enabled) {
                featureSet.add(feature);
            }
            else {
                featureSet.remove(feature);
            }

            resetImp(imp.getWidth(), imp.getHeight(), featureSet);
        }
        else if (feature == ViewFeature.USER_SCALE) {
            userControl.setScalingEnabled(enabled);
        }
        else if (feature == ViewFeature.USER_SCROLL) {
            userControl.setScrollingEnabled(enabled);
        }
        else {
            imp.setFeature(feature, enabled);
        }
    }

    /**
     * Sets the shape of the mouse cursor.
     *
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <code>mouseCursor</code> is <code>null</code>
     *
     * @see MouseCursor
     * @since 2.0
     */
    public final void setMouseCursor(final MouseCursor mouseCursor) {
        if (mouseCursor == null) {
            throw new NullPointerException("mouseCursor");
        }

        imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the scale of this view.
     *
     * @param scale the scale
     *
     * @since 2.0
     */
    public final void setScale(final double scale) {
        setScale((float) scale);
    }

    /**
     * Sets the scale of this view.
     *
     * @param scale the scale
     *
     * @since 2.0
     */
    public final void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * Sets the title of this view.
     *
     * @param title new title of this view
     *
     * @see #getTitle()
     * @since 2.0
     */
    public final void setTitle(final String title) {
        if (title == null) {
            this.title = "";
        }
        else {
            this.title = title;
        }

        imp.setTitle(this.title);
    }

    /**
     * Sets the translation of this view in world coordinates.
     *
     * @param tx the horizontal translation
     * @param ty the vertical translation
     *
     * @since 2.0
     */
    public final void setTranslation(final double tx, final double ty) {
        translationX = (float) tx;
        translationY = (float) ty;
    }

    /**
     * Changes the translation of this view in world coordinates.
     *
     * @param tx the additional horizontal translation
     * @param ty the additional vertical translation
     *
     * @since 2.0
     */
    public final void translate(final double tx, final double ty) {
        translationX = translationX + (float) tx;
        translationY = translationY + (float) ty;
    }

    /**
     * This method is invoked after an element has been added to the view.
     *
     * @param element the element that has been added
     *
     * @since 2.0
     */
    protected void elementAdded(final Element element) {
    }

    /**
     * This method is invoked after an element has been removed from the view.
     *
     * @param element the element that has been removed
     *
     * @since 2.0
     */
    protected void elementRemoved(final Element element) {
    }

    /**
     * This method is invoked after the world elements has been drawn. Override this method to draw an overlay in world
     * coordinates.
     *
     * @param canvas the canvas to draw on
     * @since 2.2
     */
    protected void drawWorldOverlay(final Canvas canvas) {
    }

    void addName(final Element element, final String name) {
        if (!elementsByName.containsKey(name)) {
            elementsByName.put(name, new HashSet<Element>());
        }

        elementsByName.get(name).add(element);
    }

    void drawOrderChanged(final Element element) {
        elementsChanged = true;
    }

    void removeName(final Element element, final String name) {
        if (elementsByName.containsKey(name)) {
            elementsByName.get(name).remove(element);
        }
    }

    void postEvent(final Event event) {
        eventQueue.addEvent(event);
    }

    private void doAdd(final Element element) {
        if (elementSet.add(element)) {
            addEventListener(element);
            element.addToView(this);
            addName(element, element.getName());
            elementAdded(element);
            elementsChanged = true;
        }
    }

    private void doRemove(final Element element) {
        if (elementSet.remove(element)) {
            removeEventListener(element);
            element.removeFromView(this);
            removeName(element, element.getName());
            elementRemoved(element);
            elementsChanged = true;
        }
    }

    private float toWorld(final float length) {
        return length * DP_TO_METER / scale;
    }

    private float toWorldX(final float x) {
        return toWorld(x) - translationX;
    }

    private float toWorldY(final float y) {
        return toWorld(y) - translationY;
    }

    private void tick(final TickEvent event) {
        if (imp.isVisible()) {
            elementLoop = true;
            updateElements();
            eventQueue.processEvents();
            foreground.setWorldTransformation(1f, 1f, 0f, 0f);
            foreground.setOpacity(255);
            foreground.setAlignment(Alignment.BOTTOM_LEFT);
            foreground.drawCanvas(0f, 0f, background);
            foreground.setWorldTransformation(scale * METER_TO_DP, scale * METER_TO_DP, translationX, translationY);
            boolean world = true;
            for (int i = 0; i < elements.length; ++i) {
                if (world && elements[i].getDrawOrder() >= 0) {
                    foreground.setWorldTransformation(1f, 1f, 0f, 0f);
                    world = false;
                }

                elements[i].internalDraw(foreground);
            }

            elementLoop = false;
            imp.update();
        }
    }

    private void resetImp(final int width, final int height, final EnumSet<ViewFeature> features) {
        if (imp != null) {
            imp.close();
        }

        imp = JedaInternal.createViewImp(callback, width, height, features);
        imp.setTitle(title);
        userControl.setScalingEnabled(features.contains(ViewFeature.USER_SCALE));
        userControl.setScrollingEnabled(features.contains(ViewFeature.USER_SCROLL));

        foreground = new Canvas(imp.getForeground());
        background = new Canvas(imp.getBackground());
        background.setColor(Color.WHITE);
        background.fill();
        background.setColor(Color.BLACK);
    }

    private void updateElements() {
        synchronized (elementLock) {
            if (!pendingRemovals.isEmpty()) {
                for (final Element element : pendingRemovals) {
                    doRemove(element);
                }

                pendingRemovals.clear();
            }

            if (!pendingInsertions.isEmpty()) {
                for (final Element element : pendingInsertions) {
                    doAdd(element);
                }

                pendingInsertions.clear();
            }

            if (elementsChanged) {
                elements = elementSet.toArray(new Element[elementSet.size()]);
                Arrays.sort(elements, Element.DRAW_ORDER);
            }
        }
    }

    private static EnumSet<ViewFeature> initImpChangingFeatures() {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        result.add(ViewFeature.FULLSCREEN);
        return result;
    }

    private static EnumSet<ViewFeature> toSet(final ViewFeature... features) {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        for (final ViewFeature feature : features) {
            result.add(feature);
        }

        return result;
    }

    private static class Callback implements ViewCallback {

        private final View view;

        public Callback(final View view) {
            this.view = view;
        }

        @Override
        public void postKeyDown(final Object source, final Key key, final int count) {
            postEvent(new KeyEvent(source, EventType.KEY_DOWN, key));
        }

        @Override
        public void postKeyTyped(final Object source, final Key key) {
            postEvent(new KeyEvent(source, EventType.KEY_TYPED, key));
        }

        @Override
        public void postKeyTyped(final Object source, final char ch) {
            postEvent(new KeyEvent(source, EventType.KEY_TYPED, ch));
        }

        @Override
        public void postKeyUp(final Object source, final Key key) {
            postEvent(new KeyEvent(source, EventType.KEY_UP, key));
        }

        @Override
        public void postPointerDown(final Object source, final int pointerId, final EnumSet<Button> pressedButtons, float x, float y) {
            x = view.foreground.deviceToCanvasX(x);
            y = view.foreground.deviceToCanvasY(y);
            postEvent(new PointerEvent(source, EventType.POINTER_DOWN, pointerId, pressedButtons, 0f, x, y,
                                       view.toWorldX(x), view.toWorldY(y)));
        }

        @Override
        public void postPointerMoved(final Object source, final int pointerId, final EnumSet<Button> pressedButtons, float x, float y) {
            x = view.foreground.deviceToCanvasX(x);
            y = view.foreground.deviceToCanvasY(y);
            postEvent(new PointerEvent(source, EventType.POINTER_MOVED, pointerId, pressedButtons, 0f, x, y,
                                       view.toWorldX(x), view.toWorldY(y)));
        }

        @Override
        public void postPointerUp(final Object source, final int pointerId, final EnumSet<Button> pressedButtons, float x, float y) {
            x = view.foreground.deviceToCanvasX(x);
            y = view.foreground.deviceToCanvasY(y);
            postEvent(new PointerEvent(source, EventType.POINTER_UP, pointerId, pressedButtons, 0f, x, y,
                                       view.toWorldX(x), view.toWorldY(y)));
        }

        @Override
        public void postWheel(final Object source, final int pointerId, final EnumSet<Button> pressedButtons, float x, float y, final float rotation) {
            x = view.foreground.deviceToCanvasX(x);
            y = view.foreground.deviceToCanvasY(y);
            postEvent(new PointerEvent(source, EventType.WHEEL, pointerId, pressedButtons, rotation, x, y,
                                       view.toWorldX(x), view.toWorldY(y)));
        }

        private void postEvent(final Event event) {
            view.eventQueue.addEvent(event);
        }
    }

    private static class UserControl implements PointerListener, WheelListener {

        private final View view;
        private PointerEvent lastDragEvent;
        private boolean scalingEnabled;
        private boolean scrollingEnabled;

        public UserControl(final View view) {
            this.view = view;
            scalingEnabled = false;
            scrollingEnabled = false;
            lastDragEvent = null;
        }

        public void setScalingEnabled(final boolean enabled) {
            this.scalingEnabled = enabled;
        }

        public void setScrollingEnabled(final boolean enabled) {
            this.scrollingEnabled = enabled;
            if (!this.scrollingEnabled) {
                lastDragEvent = null;
            }
        }

        @Override
        public void onPointerDown(final PointerEvent event) {

            if (scrollingEnabled && lastDragEvent == null) {
                lastDragEvent = event;
            }
        }

        @Override
        public void onPointerMoved(final PointerEvent event) {
            if ((lastDragEvent != null) && (event.getPointerId() == lastDragEvent.getPointerId())) {
                float dx = view.toWorld(event.getViewX() - lastDragEvent.getViewX());
                float dy = view.toWorld(event.getViewY() - lastDragEvent.getViewY());
                view.translate(dx, dy);
                lastDragEvent = event;
            }
        }

        @Override
        public void onPointerUp(final PointerEvent event) {
            if (lastDragEvent != null && lastDragEvent.getPointerId() == event.getPointerId()) {
                lastDragEvent = null;
            }
        }

        @Override
        public void onWheel(final PointerEvent event) {
            if (scalingEnabled) {
                if (event.getWheel() > 0f) {
                    view.scale(1 / 1.1, event.getWorldX(), event.getWorldY());
                }
                else {
                    view.scale(1.1, event.getWorldX(), event.getWorldY());
                }
            }
        }
    }

    private static class EventLoop implements TickListener {

        private final View view;

        public EventLoop(final View view) {
            this.view = view;
        }

        @Override
        public void onTick(final TickEvent event) {
            view.tick(event);
        }
    }
}
