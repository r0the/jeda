/*
 * Copyright (C) 2011 by Stefan Rothe
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
import ch.jeda.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class BlockMap {

    private final List<Entity> entities;
    private final List<MapEvent> events;
    private final List<Field> fields;
    private final Size size;
    private Entity scrollLockEntity;
    private boolean isDirty;

    BlockMap(Size size) {
        this.entities = new ArrayList<Entity>();
        this.events = new ArrayList<MapEvent>();
        this.fields = new ArrayList<Field>();
        for (int x = -1; x <= size.width; ++x) {
            for (int y = -1; y <= size.height; ++y) {
                this.fields.add(new Field(this, x, y));
            }
        }
        this.size = size;
        this.isDirty = false;
    }

    List<Entity> entities() {
        return Collections.unmodifiableList(this.entities);
    }

    boolean isValidCoordinate(int x, int y) {
        return 0 <= x && x < this.size.width && 0 <= y && y < this.size.height;
    }

    boolean isValidCoordinate(Location location) {
        return this.size.contains(location);
    }

    void addEvent(MapEvent event) {
        this.events.add(event);
    }

    void dispose() {
        List<Entity> removing = new ArrayList<Entity>(this.entities);
        for (Entity entity : removing) {
            if (entity.field() != null) {
                entity.field().removeEntity(entity);
            }
        }
    }

    Field fieldAt(Location location) {
        return this.fieldAt(location.x, location.y);
    }

    Field fieldAt(int x, int y) {
        if (this.isValidCoordinate(x, y)) {
            return this.fields.get(index(x, y));
        }
        else {
            return null;
        }
    }

    Size getSize() {
        return this.size;
    }

    void setScrollLock(Entity entity) {
        this.scrollLockEntity = entity;
    }

    Field uncheckedFieldAt(Location location) {
        return this.uncheckedFieldAt(location.x, location.y);
    }

    Field uncheckedFieldAt(int x, int y) {
        if (x < -1 || this.size.width < x) {
            throw new IndexOutOfBoundsException("x=" + x);
        }
        if (y < -1 || this.size.height < y) {
            throw new IndexOutOfBoundsException("y=" + y);
        }
        return this.fields.get(this.index(x, y));
    }

    void update(RenderContext renderContext) {
        this.evaluateEvents();
        this.render(renderContext);
    }

    void updateEntities(BlockWorld blockWorld) {
        for (Entity entity : this.entities) {
            entity.internalUpdate(blockWorld);
            if (entity.isMoving()) {
                this.isDirty = true;
            }
        }
    }

    void doAddEntity(Entity entity) {
        if (!this.entities.contains(entity)) {
            this.entities.add(entity);
        }
    }

    void doRemoveEntity(Entity entity) {
        if (!this.entities.contains(entity)) {
            this.entities.remove(entity);
        }
    }

    private void evaluateEvents() {
        if (!this.events.isEmpty()) {
            this.isDirty = true;
            for (MapEvent event : this.events) {
                event.evaluate();
            }
            this.events.clear();
        }
    }

    private void render(RenderContext renderContext) {
        if (this.isDirty) {
            renderContext.prepare(this.scrollLockEntity);
            Location topLeft = renderContext.start();
            Location bottomRight = renderContext.end();
            for (int y = topLeft.y; y < bottomRight.y; ++y) {
                for (int x = topLeft.x; x < bottomRight.x; ++x) {
                    this.uncheckedFieldAt(x, y).render(renderContext);
                }
            }
            for (Entity entity : this.entities) {
                if (entity.getMessage() != null) {
                    renderContext.drawSpeechBubble(entity);
                }
            }
            renderContext.finish();

            this.isDirty = false;
        }
    }

    private int index(int x, int y) {
        return (x + 1) * (this.size.height + 2) + y + 1;
    }
}
