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
package ch.jeda.blocks;

import ch.jeda.Location;
import ch.jeda.Simulation;
import ch.jeda.Size;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Image;
import ch.jeda.ui.Window;
import ch.jeda.ui.VisualSimulation;
import java.util.List;

/**
 * This class represents a block world. It extends the {@link ch.jeda.Simulation}
 * class and implements a simulation loop that renders a block world to a
 * {@link ch.jeda.ui.Window}.
 */
public class BlockWorld extends VisualSimulation {

    private final RenderContext renderContext;
    private BlockMap currentMap;
    private Entity scrollLock;

    /**
     * Creates a new block world with a default-sized window.
     *
     * @see Window#Window()
     */
    protected BlockWorld() {
        this(null);
    }

    /**
     * Creates a new block world with a window that has a drawing area of the
     * specified size.
     *
     * @param width the width of the window's drawing area
     * @param height the height of the window's drawing area
     *
     * @throws IllegalArgumentException if width or height are smaller than 1
     */
    public BlockWorld(int width, int height) {
        this(new Size(width, height));
    }

    /**
     * Creates a new block world with a window that has a drawing area of the
     * specified size.
     *
     * @param size the size of the window's drawing area
     *
     * @throws NullPointerException if size is <code>null</code>
     * @throws IllegalArgumentException if size is empty
     */
    public BlockWorld(Size size) {
        super(size);
        this.renderContext = new RenderContext(this.getCanvasSize());
        this.renderContext.setBorder(50, 200, 50, 50);
        this.renderContext.scroll(-50, -50);
    }

    /**
     * Adds a new block type to this block world. This will make the specified
     * block type available with the given image.
     *
     * @param blockType name of the block type
     * @param image image representing the block type
     *
     * @throws NullPointerException if blockType or image is null
     */
    public final void addBlockType(String blockType, Image image) {
        if (blockType == null) {
            throw new NullPointerException("blockType");
        }

        if (image == null) {
            throw new NullPointerException("image");
        }

        this.renderContext.addImage(blockType, image);
    }

    /**
     * Creates a new map with the specified dimension. All fields of the map
     * will be empty.
     *
     * @param sizeX number of fields in x direction
     * @param sizeY number of fields in y direction
     *
     * @throws IllegalArgumentException if sizeX or sizeY are smaller than 1
     */
    public final void createMap(int sizeX, int sizeY) {
        Size size = new Size(sizeX, sizeY);
        if (this.currentMap != null) {
            this.currentMap.dispose();
        }

        this.currentMap = new BlockMap(size);
        this.currentMap.setScrollLock(this.scrollLock);
        this.renderContext.setMapSize(this.currentMap.getSize());
    }

    /**
     * Creates a new map with the specified dimension. All fields of the map
     * will be initialized with one block of the specified type.
     *
     * @param sizeX number of fields in x direction
     * @param sizeY number of fields in y direction
     * @param blockType name of block type to initialize map with
     *
     * @throws IllegalArgumentException if sizeX or sizeY are smaller than 1
     */
    public final void createMap(int sizeX, int sizeY, String blockType) {
        if (blockType == null) {
            throw new NullPointerException("blockType");
        }

        this.createMap(sizeX, sizeY);
        for (int x = 0; x < sizeX; ++x) {
            for (int y = 0; y < sizeY; ++y) {
                this.fieldAt(x, y).addBlock(blockType);
            }
        }
    }

    public final List<Entity> entities() {
        return this.currentMap.entities();
    }

    public final Field fieldAt(int x, int y) {
        Location location = new Location(x, y);
        if (this.currentMap.isValidCoordinate(location)) {
            return this.currentMap.fieldAt(location);
        }
        else {
            throw new IndexOutOfBoundsException(location.toString());
        }
    }

//    public final Field mouseField() {
//        Point mousePos = this.window.mouse().getPosition();
//        mousePos = this.renderContext.screenToMap(mousePos, this.currentMap);
//        if (mousePos == null) {
//            return null;
//        }
//        else {
//            return this.currentMap.fieldAt(mousePos);
//        }
//    }
    public final Image getBlockImage(String blockType) {
        if (blockType == null) {
            throw new NullPointerException("blockType");
        }
        return this.renderContext.getBlockImage(blockType);
    }

    public final void scrollLock(Entity entity) {
        this.scrollLock = entity;
        if (this.currentMap != null) {
            this.currentMap.setScrollLock(entity);
        }
    }

    public final void scroll(int dx, int dy) {
        this.currentMap.addEvent(new ScrollEvent(dx, dy, this.renderContext));
    }

    /**
     * Returns the number of fields of this world in x-direction.
     */
    public final int sizeX() {
        return this.currentMap.getSize().width;
    }

    /**
     * Returns the number of fields of this world in y-direction.
     */
    public final int sizeY() {
        return this.currentMap.getSize().height;
    }

    @Override
    protected void init() {
    }

    @Override
    protected final void beforeUpdate() {
        if (this.currentMap != null) {
            this.currentMap.updateEntities(this);
            this.currentMap.update(this.renderContext);
        }
    }

    @Override
    protected final void drawForeground(Canvas canvas) {
        if (this.currentMap != null) {
            canvas.setAlpha(255);
            canvas.drawImage(0, 0, this.renderContext.getImage());

        }

        this.drawOverlay(canvas);
    }

    protected void drawOverlay(Canvas canvas) {
    }
}
