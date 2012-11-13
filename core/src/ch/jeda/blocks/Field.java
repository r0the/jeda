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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field {

    private final List<String> blocks;
    private final List<Entity> entities;
    private final TypeMap<Entity> entityMap;
    private final FieldHeightInfo heightInfo;
    private final BlockMap map;
    private final Location position;
    private final List<Entity> renderEntities;
    private boolean isDirty;

    Field(BlockMap map, int x, int y) {
        this.blocks = new ArrayList<String>();
        this.entities = new ArrayList<Entity>();
        this.entityMap = new TypeMap<Entity>(Entity.class);
        this.heightInfo = new FieldHeightInfo(this);
        this.map = map;
        this.position = new Location(x, y);
        this.renderEntities = new ArrayList<Entity>();
        this.isDirty = false;
    }

    public void addBlock(String blockType) {
        if (blockType == null) {
            throw new NullPointerException("blockType");
        }
        this.map.addEvent(new AddBlockEvent(this, blockType));
    }

    public void addEntity(Entity entity) {
        if (entity == null) {
            throw new NullPointerException("entity");
        }
        this.map.addEvent(new AddEntityEvent(this.map, this, entity));
    }

    public List<String> blocks() {
        return Collections.unmodifiableList(this.blocks);
    }

    public void clearBlocks() {
        this.map.addEvent(new ClearBlocksEvent(this));
    }

    public List<Entity> entities() {
        return Collections.unmodifiableList(this.entities);
    }

    public <T extends Entity> List<T> entities(Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type");
        }
        return this.entityMap.get(type);
    }

    public Entity firstEntity() {
        if (this.entities.isEmpty()) {
            return null;
        }
        else {
            return this.entities.get(0);
        }
    }

    public Location getPosition() {
        return this.position;
    }

    public int height() {
        return this.blocks.size();
    }

    public Field neighbor(Direction direction) {
        if (direction == null) {
            throw new NullPointerException("direction");
        }
        Location neighborPos = direction.targetLocation(this.position);
        if (this.map.isValidCoordinate(neighborPos)) {
            return this.map.fieldAt(neighborPos);
        }
        else {
            return null;
        }
    }

    public void removeEntity(Entity entity) {
        if (entity == null) {
            throw new NullPointerException("entity");
        }
        this.map.addEvent(new RemoveEntityEvent(this.map, entity));
    }

    public void removeTopBlock() {
        this.map.addEvent(new RemoveTopBlockEvent(this));
    }

    public int slope(Direction direction) {
        return this.heightInfo.slope(direction);
    }

    public String topBlock() {
        if (this.blocks.isEmpty()) {
            return null;
        }
        else {
            return this.blocks.get(this.blocks.size() - 1);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
        result.append(this.position);
        return result.toString();
    }

    void doAddBlock(String blockType) {
        this.blocks.add(blockType);
        for (Entity entity : this.entities) {
            entity.doCheckFloor();
        }
        this.modified();
    }

    void doAddEntity(Entity entity) {
        if (!this.entities.contains(entity)) {
            this.entities.add(entity);
            this.entityMap.add(entity);
        }
    }

    void doAddRenderEntity(Entity entity) {
        if (!this.renderEntities.contains(entity)) {
            this.renderEntities.add(entity);
            this.modified();
        }
    }

    void doClearBlocks() {
        this.blocks.clear();
        this.modified();
    }

    void doRemoveEntity(Entity entity) {
        if (this.entities.contains(entity)) {
            this.entities.remove(entity);
            this.entityMap.remove(entity);
        }
    }

    void doRemoveRenderEntity(Entity entity) {
        this.renderEntities.remove(entity);
        this.modified();
    }

    void doRemoveTopBlock() {
        if (!this.blocks.isEmpty()) {
            this.blocks.remove(this.blocks.size() - 1);
            this.modified();
        }
    }

    Vector3D entityRenderPos() {
        return new Vector3D(this.position.x, this.position.y, this.height());
    }

    void render(RenderContext context) {
        if (this.isDirty) {
            this.heightInfo.update();
            this.isDirty = false;
        }
        context.setRenderPos(this.position);
        for (String blockType : this.blocks) {
            context.drawBlock(blockType);
        }
        for (Direction direction : this.heightInfo.shadows()) {
            context.drawShadow(direction);
        }
        for (Entity entity : this.renderEntities) {
            context.drawEntity(entity);
        }
    }

    void modified() {
        this.isDirty = true;
        for (Direction direction : Direction.ALL) {
            this.uncheckedNeighbor(direction).isDirty = true;
        }
    }

    private Field uncheckedNeighbor(Direction direction) {
        return this.map.uncheckedFieldAt(direction.targetLocation(this.position));
    }
}
