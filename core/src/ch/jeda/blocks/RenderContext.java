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
package ch.jeda.blocks;

import ch.jeda.Direction;
import ch.jeda.Location;
import ch.jeda.Size;
import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Image;
import java.util.ArrayList;
import java.util.List;

class RenderContext {

    private final BlockSet blockSet;
    private final Canvas canvas;
    private final int maxY;
    private int borderLeft;
    private int borderTop;
    private int borderRight;
    private int borderBottom;
    private Image image;
    private Size mapSize;
    private Location scrollPos;
    private Location renderPos;
    private int shadowPosY;

    RenderContext(Size size) {
        this.blockSet = new BlockSet();
        this.canvas = Canvas.create(size);
        this.maxY = this.canvas.getSize().height + this.blockSet.brickSizeZ() + this.blockSet.brickSizeY();
        this.mapSize = null;
        this.scrollPos = Location.ORIGIN;
    }

    void addImage(String blockType, Image image) {
        this.blockSet.addBlockImage(blockType, image);
    }

    Image getBlockImage(String blockType) {
        return this.blockSet.getBlockImage(blockType);
    }

    void setMapSize(Size mapSize) {
        this.mapSize = mapSize;
        this.checkScrollPos();
    }

    void setRenderPos(Location mapPos) {
        this.renderPos = mapToScreen(mapPos);
        this.shadowPosY = this.renderPos.y + this.blockSet.brickSizeZ();
    }

    void drawBlock(String blockType) {
        if (0 < this.renderPos.y && this.renderPos.y < this.maxY) {
            this.canvas.drawImage(this.renderPos.x, this.renderPos.y,
                                  this.blockSet.getBlockImage(blockType),
                                  Alignment.BOTTOM_LEFT);
        }
        this.shadowPosY = this.renderPos.y;
        this.renderPos = Location.from(
                this.renderPos.x,
                this.renderPos.y - this.blockSet.brickSizeZ());
    }

    void drawEntity(Entity entity) {
        if (Entity.INVISIBLE.equals(entity.getImageName())) {
            return;
        }
        Location location = this.mapToScreen(entity.getRenderPos());
        this.canvas.drawImage(location.x, location.y,
                              this.blockSet.getBlockImage(entity.getImageName()),
                              Alignment.BOTTOM_LEFT);
    }

    void drawSpeechBubble(Entity entity) {
        Location location = this.mapToScreen(entity.getRenderPos());
        int x = location.x + this.blockSet.brickSizeX();
        int y = location.y - 2 * this.blockSet.brickSizeZ();
        this.canvas.drawImage(x, y, this.blockSet.getSpeechBubble(), Alignment.BOTTOM_LEFT);
        this.canvas.setColor(Color.BLACK);
        List<String> lines = lineBreak(entity.getMessage(), 90);
        y = y - 75;
        for (String line : lines) {
            this.canvas.drawText(x + 15, y, line, Alignment.BOTTOM_LEFT);
            y = y + 20;
        }
    }

    private List<String> lineBreak(String message, int maxWidth) {
        String[] words = message.split(" ");
        List<String> result = new ArrayList<String>();
        String line;
        String lookahead;
        int i = 0;
        while (i < words.length) {
            lookahead = words[i];
            line = null;
            while (this.canvas.textSize(lookahead).width < maxWidth && i + 1 < words.length) {
                line = lookahead;
                ++i;
                lookahead = line + " " + words[i];
            }
            if (line == null) {
                line = lookahead;
                ++i;
            }
            result.add(line);
        }
        return result;
    }

    void drawShadow(Direction direction) {
        this.canvas.drawImage(this.renderPos.x, this.shadowPosY,
                              this.blockSet.getShadowImage(direction),
                              Alignment.BOTTOM_LEFT);
    }

    Location end() {
        return Location.from(
                Math.min(this.mapSize.width, start().x + this.canvas.getWidth() / this.blockSet.brickSizeX() + 1) + 1,
                this.mapSize.height + 1);
    }

    Image getImage() {
        return this.image;
    }

    Location start() {
        return Location.from(
                Math.max(0, (this.scrollPos.x) / this.blockSet.brickSizeX()) - 1,
                Math.max(0, (this.scrollPos.y) / this.blockSet.brickSizeY()) - 1);
    }

    void prepare(Entity scrollLock) {
        this.canvas.clear();
        if (scrollLock != null) {
            this.checkScrollLock(scrollLock.getRenderPos());
        }
    }

    void finish() {
        this.image = this.canvas.takeSnapshot();
    }

    Location screenToMap(Location screenPos, BlockMap map) {
        Location location = Location.from(
                screenPos.x + this.scrollPos.x,
                screenPos.y + this.scrollPos.y);
        if (location.x < 0 || location.y > (map.getSize().height - 1) * this.blockSet.brickSizeY()) {
            return null;
        }

        int x = (int) (location.x / this.blockSet.brickSizeX());
        int y = map.getSize().height - 1;
        if (!map.isValidCoordinate(x, y)) {
            return null;
        }

        int topY = (y - 1) * this.blockSet.brickSizeY() - this.scrollPos.y - map.fieldAt(x, y).height() * this.blockSet.brickSizeZ();
        while (topY > screenPos.y) {
            --y;
            if (!map.isValidCoordinate(x, y)) {
                return null;
            }

            topY = (y - 1) * this.blockSet.brickSizeY() - this.scrollPos.y - map.fieldAt(x, y).height() * this.blockSet.brickSizeZ();
        }

        return Location.from(x, y);
    }

    void scroll(int dx, int dy) {
        this.scrollPos = Location.from(this.scrollPos.x + dx, this.scrollPos.y + dy);
        this.checkScrollPos();
    }

    void setBorder(int left, int top, int right, int bottom) {
        this.borderLeft = left;
        this.borderTop = top;
        this.borderRight = right;
        this.borderBottom = bottom;
        this.checkScrollPos();
    }

    private void checkScrollLock(Vector3D entityPos) {
        double x = entityPos.x * this.blockSet.brickSizeX() - this.canvas.getWidth() / 2;
        double y = entityPos.y * this.blockSet.brickSizeY() -
                   entityPos.z * this.blockSet.brickSizeZ() - this.blockSet.brickSizeY() - this.canvas.getHeight() / 2;
        this.scrollPos = Location.from((int) x, (int) y);
    }

    private Location mapToScreen(Vector3D pos) {
        double x = pos.x * this.blockSet.brickSizeX() - this.scrollPos.x;
        double y = pos.y * this.blockSet.brickSizeY() -
                   pos.z * this.blockSet.brickSizeZ() - this.scrollPos.y;
        return Location.from((int) x, (int) y);
    }

    private Location mapToScreen(Location mapCoors) {
        return Location.from(mapCoors.x * this.blockSet.brickSizeX() - this.scrollPos.x,
                             mapCoors.y * this.blockSet.brickSizeY() - this.scrollPos.y);
    }

    private void checkScrollPos() {
        Location min = Location.from(-this.borderLeft, -this.borderTop);
        int maxX = this.blockSet.brickSizeX() * this.mapSize.width - this.canvas.getWidth() + this.borderRight;
        int maxY = this.blockSet.brickSizeY() * this.mapSize.height - this.canvas.getHeight() + this.borderBottom;
        this.scrollPos = this.scrollPos.ensureRange(min, Location.from(maxX, maxY));
    }
}
