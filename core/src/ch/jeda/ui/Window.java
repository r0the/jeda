/*
 * Copyright (C) 2011, 2012 by Stefan Rothe
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

import ch.jeda.Engine;
import ch.jeda.platform.ViewImp;
import ch.jeda.Size;

/**
 * Provides a drawing window. The window class has the follwoing functionality:
 * <ul>
 *   <li> fullscreen: the window can be displayed in framed or fullscreen mode.
 *   <li> double buffering: the window supports a double buffering mode for animations.
 *   <li> user input: the window provides means to query keyboard and mouse input.
 * </ul>
 */
public class Window extends Canvas {

    /**
     * The default height of the drawing area of a window.
     *
     * @since 1.0
     */
    public static final int DEFAULT_HEIGHT = 600;
    /**
     * The default width of the drawing area of a window.
     *
     * @since 1.0
     */
    public static final int DEFAULT_WIDTH = 800;
//    private final Input input;
//    private final Keyboard keyboard;
//    private final Mouse mouse;
    private ViewImp imp;
    private Size size;
    private String title;

    /**
     * Creates a new window that has a drawing area of default size as defined
     * by {@link #DEFAULT_WIDTH} and {@link #DEFAULT_HEIGHT}.
     *
     * @since 1.0
     */
    public Window() {
        this(new Size());
    }

    /**
     * Creates a new window that has a drawing with the specified width and
     * height in pixels.
     *
     * @param width width of the drawing area in pixels
     * @param height height of the drawing area in pixels
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     * @since 1.0
     */
    public Window(int width, int height) {
        this(new Size(width, height));
    }

    private Window(Size size) {
        super();
//        this.input = new Input();
//        this.bufferMode = BufferMode.Single;
//        this.keyboard = new Keyboard();
//        this.mouse = new Mouse();
        if (size.isEmpty()) {
            size = new Size(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }

        this.size = size;
        this.title = getClass().getName();
        this.resetImp(false, false);
    }

    /**
     * Closes this window and destroys the window object.
     *
     * @since 1.0
     */
    public void close() {
        this.imp.close();
    }

    /**
     *
     */
    public void flip() {
        this.update();
    }

//    public Input getInput() {
//        return this.input;
//    }
//
//    @Deprecated
//    public Keyboard getKeyboard() {
//        return this.keyboard;
//    }
//
//    @Deprecated
//    public Keyboard keyboard() {
//        return this.keyboard;
//    }
//
//    @Deprecated
//    public Mouse mouse() {
//        return this.mouse;
//    }
    /**
     * Returns the window's current title.
     *
     * @return current window title
     * @see #setTitle(java.lang.String)
     * @since 1.0
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Checks whether the window is in double buffering mode.
     *
     * @return true if the window is in double buffering mode
     * @see #setDoubleBuffered(boolean)
     * @since 1.0
     */
    public boolean isDoubleBuffered() {
        return this.imp.isDoubleBuffered();
    }

    /**
     * Checks whether the window is in fullscreen mode.
     *
     * @return <code>true</code> if the window is in fullscreen mode
     * @see #setFullscreen(boolean)
     * @since 1.0
     */
    public boolean isFullscreen() {
        return this.imp.isFullscreen();
    }

    /**
     * Enables/disables the double buffering mode.
     *
     * @param doubleBuffered <code>true</code> to enable double buffering mode,
     *                       <code>false</code> to disable it
     * @see #isDoubleBuffered()
     * @since 1.0
     */
    public void setDoubleBuffered(boolean doubleBuffered) {
        this.resetImp(doubleBuffered, this.isFullscreen());
    }

    /**
     * Enables/disables the fullscreen mode.
     *
     * @param fullscreen <code>true</code> to enable fullscreen mode,
     *                   <code>false</code> to disable it
     * @see #isFullscreen()
     * @since 1.0
     */
    public void setFullscreen(boolean fullscreen) {
        if (this.imp.isFullscreen() != fullscreen) {
            this.imp.close();
            //this.resetImp(fullscreen);
        }
    }

    public void setMouseCursor(MouseCursor mouseCursor) {
        this.imp.setMouseCursor(mouseCursor);
    }

    /**
     * Sets the window's title.
     *
     * @param title new title of the window
     * @throws NullPointerException if title is null
     * @see #getTitle()
     * @since 1.0
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException("title");
        }
        this.title = title;
        this.imp.setTitle(title);
    }

    /**
     * Updates this window. Depending on the current bufferMode of this window,
     * this method has different effects.
     * <ul>
     *   <li> It always updated the keyboard and mouse state of this window.
     *   <li> In BufferMode.Double, it flips the hidden and visible screen
     *        buffers.
     *   <li> In BufferMode.SingleLazy, it updates the display.
     * </ul>
     *
     * @since 1.0
     */
    public void update() {
        this.imp.update();
    }

    private void resetImp(boolean doubleBuffered, boolean fullscreen) {
        if (this.imp != null) {
            this.imp.close();
        }

        this.imp = Engine.getCurrentEngine().showView(this.size, doubleBuffered, fullscreen);
//        this.input.setImp(this.imp.getInputImp());
//        this.keyboard.setImp(this.imp.getInputImp());
//        this.mouse.setImp(this.imp);
        this.imp.setTitle(this.title);
//        if (this.bufferMode != BufferMode.Double) {
        this.imp.setColor(Color.WHITE);
        this.imp.fill();
//        }
        this.setImp(this.imp);

        this.flip();
    }
}
