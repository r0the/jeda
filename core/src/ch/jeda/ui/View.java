/*
 * Copyright (C) 2011 - 2015 by Stefan Rothe
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
import ch.jeda.event.Event;
import ch.jeda.event.EventQueue;
import ch.jeda.event.TickEvent;
import ch.jeda.event.TickListener;
import ch.jeda.platform.ViewImp;
import java.util.EnumSet;

/**
 * Represents a graphical view. The view class has the following functionality:
 * <ul>
 * <li> fullscreen: the view can be displayed in framed or fullscreen mode.
 * <li> double buffering: the view supports a double buffering mode for animations.
 * <li> user input: the view provides means to query keyboard and mouse input.
 * </ul>
 *
 * @since 1.6
 */
public class View extends Canvas {

    private static final EnumSet<ViewFeature> IMP_CHANGING_FEATURES = initImpChangingFeatures();
    private final Elements elements;
    private final EventQueue eventQueue;
    private ViewImp imp;
    private String title;

    /**
     * Constructs a view. The view is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @since 1.6
     */
    public View() {
        this(0, 0);
    }

    /**
     * Constructs a view. The view is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * <p>
     * <p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has a width of 800
     * pixels and a height of 600 pixels.
     * <p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.
     *
     * @param features the features of the view
     *
     * @since 1.6
     */
    public View(final ViewFeature... features) {
        this(0, 0, features);
    }

    /**
     * Constructs a view. The view is shown on the screen. All drawing methods inherited from {@link Canvas} are
     * supported. The specified features will be enabled for the view.
     * <p>
     * The size of the view's drawing area depends on the platform:
     * </p><p>
     * <img src="../../../windows.png"> <img src="../../../linux.png"> The drawing area of the view has the specified
     * <tt>width</tt> and
     * <tt>height</tt>.
     * </p><p>
     * <img src="../../../android.png"> The size drawing area depends on the screen size of the device.</p>
     *
     * @param width the width of the drawing area in pixels
     * @param height the height of the drawing area in pixels
     * @param features the features of the view
     * @throws IllegalArgumentException if width or height are smaller than 1
     *
     * @since 1.6
     */
    public View(final int width, final int height, final ViewFeature... features) {
        this.elements = new Elements(this);
        this.eventQueue = new EventQueue();
        this.title = Jeda.getProgramName();
        this.resetImp(width, height, toSet(features));
        Jeda.addEventListener(this.eventQueue);
        Jeda.addEventListener(new EventLoop(this));
    }

    /**
     * Adds a {@link ch.jeda.ui.Element} to the currnt page of the view. Has no effect if <tt>element</tt>
     * is <tt>null</tt>. The {@link ch.jeda.ui.Element} becomes inactive (it no longer receives events) and insivible if
     * the current page changes.
     *
     * @param element the element to be added to the view
     *
     * @see #remove(ch.jeda.ui.Element)
     * @see #getElements()
     * @see #getElements(java.lang.Class)
     * @since 1.6
     */
    public final void add(final Element element) {
        this.elements.add(element);
    }

    /**
     * Adds an event listener to the view. The specified object will receive events for all events listener interfaces
     * it implements. Has no effect if <tt>listener</tt> is <tt>null</tt> or an element of this view.
     *
     * @param listener the event listener
     *
     * @since 1.6
     */
    public final void addEventListener(final Object listener) {
        this.eventQueue.addListener(listener);
    }

    /**
     * Closes the view. The view becomes invalid, all subsequent method calls to the view will cause an error.
     *
     * @since 1.6
     */
    public final void close() {
        this.imp.close();
    }

    /**
     * Returns all elements currently managed by the view.
     *
     * @return all elements currently managed by the view.
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getElements(java.lang.Class)
     * @see #remove(ch.jeda.ui.Element)
     * @since 1.6
     */
    public final Element[] getElements() {
        return this.elements.getAll();
    }

    /**
     * Returns all elements of the specified class currently managed by the view.
     *
     * @param <T> the type of elements to return
     * @param clazz the class of elements to return
     * @return all elements currently managed by the view.
     * @throws NullPointerException if <tt>clazz</tt> is <tt>null</tt>
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getElements()
     * @see #remove(ch.jeda.ui.Element)
     * @since 1.6
     */
    public final <T extends Element> T[] getElements(final Class<T> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz");
        }

        return this.elements.get(clazz);
    }

    /**
     * Returns the current page of the view.
     *
     * @return the current page of the view
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #setPage(java.lang.String)
     * @since 1.6
     */
    public final String getPage() {
        return this.elements.getCurrentPage();
    }

    /**
     * Returns the current view title.
     *
     * @return current view title
     *
     * @see #setTitle(java.lang.String)
     * @since 1.6
     */
    public final String getTitle() {
        return this.title;
    }

    /**
     * Checks for a view feature. Returns <tt>true</tt> if the specified feature is currently enabled for the view.
     *
     * @param feature the feature to check for
     * @return <tt>true</tt> if the feature is enabled, otherwise returns
     * <tt>false</tt>
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #setFeature(WindowFeature, boolean)
     * @since 1.6
     */
    public final boolean hasFeature(final ViewFeature feature) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        return this.imp.getFeatures().contains(feature);
    }

    /**
     * Removes a {@link ch.jeda.ui.Element} from the view. Has no effect if <tt>element</tt> is
     * <tt>null</tt>.
     *
     * @param element the element to be removed from the view
     *
     * @see ch.jeda.ui.Element
     * @since 1.6
     */
    public final void remove(final Element element) {
        this.elements.remove(element);
    }

    /**
     * Removes an event listener from the view. The specified object will not receive events anymore. Has no effect if
     * <tt>listener</tt> is <tt>null</tt> or an element of this view.
     *
     * @param listener the event listener
     * @since 1.6
     */
    public final void removeEventListener(final Object listener) {
        this.eventQueue.removeListener(listener);
    }

    /**
     * Enables or disables a view feature.
     *
     * @param feature the feature to be enabled or disabled
     * @param enabled <tt>true</tt> to enable the feature,
     * <tt>false</tt> to disable it
     * @throws NullPointerException if <tt>feature</tt> is <tt>null</tt>
     *
     * @see #hasFeature(WindowFeature)
     * @since 1.6
     */
    public final void setFeature(final ViewFeature feature, final boolean enabled) {
        if (feature == null) {
            throw new NullPointerException("feature");
        }

        if (IMP_CHANGING_FEATURES.contains(feature)) {
            final EnumSet<ViewFeature> featureSet = EnumSet.copyOf(this.imp.getFeatures());
            if (enabled) {
                featureSet.add(feature);
            }
            else {
                featureSet.remove(feature);
            }

            this.resetImp(this.getWidth(), this.getHeight(), featureSet);
        }
        else {
            this.imp.setFeature(feature, enabled);
        }
    }

    /**
     * Sets the shape of the mouse cursor.
     *
     * @param mouseCursor new shape of mouse cursor
     * @throws NullPointerException if <tt>mouseCursor</tt> is
     * <tt>null</tt>
     *
     * @see MouseCursor
     * @since 1.6
     */
    public final void setMouseCursor(final MouseCursor mouseCursor) {
        if (mouseCursor == null) {
            throw new NullPointerException("mouseCursor");
        }

        this.imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the current page of the view. All {@link ch.jeda.ui.Element}s are added to the current page. They become
     * inactive (they no longer receive events) and insivible if the current page changes.
     *
     * @param page the current page
     *
     * @see #add(ch.jeda.ui.Element)
     * @see #getPage()
     * @since 1.6
     */
    public final void setPage(final String page) {
        this.elements.setPage(page);
    }

    /**
     * Sets the view title.
     *
     * @param title new title of the view
     * @throws NullPointerException if <tt>title</tt> is <tt>null</tt>
     *
     * @see #getTitle()
     * @since 1.6
     */
    public final void setTitle(final String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }

        this.title = title;
        this.imp.setTitle(title);
    }

    void postEvent(final Event event) {
        this.eventQueue.addEvent(event);
    }

    private void tick(final TickEvent event) {
        if (this.imp.isVisible()) {
            this.eventQueue.processEvents();
            this.elements.processPending();
            this.elements.draw(this);
            this.imp.update();
        }
    }

    private void resetImp(final int width, final int height, final EnumSet<ViewFeature> features) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = JedaInternal.createViewImp(width, height, features);
        this.imp.setEventQueue(this.eventQueue);
        this.imp.setTitle(this.title);
        if (!this.hasFeature(ViewFeature.DOUBLE_BUFFERED)) {
            this.imp.setColor(Color.WHITE);
            this.imp.fill();
        }

        super.setImp(this.imp);
    }

    private static EnumSet<ViewFeature> initImpChangingFeatures() {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        result.add(ViewFeature.DOUBLE_BUFFERED);
        result.add(ViewFeature.FULLSCREEN);
        return result;
    }

    private static EnumSet<ViewFeature> toSet(final ViewFeature... features) {
        final EnumSet<ViewFeature> result = EnumSet.noneOf(ViewFeature.class);
        for (ViewFeature feature : features) {
            result.add(feature);
        }

        return result;
    }

    private static class EventLoop implements TickListener {

        private final View view;

        public EventLoop(final View view) {
            this.view = view;
        }

        @Override
        public void onTick(final TickEvent event) {
            this.view.tick(event);
        }
    }
}
