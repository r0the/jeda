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

import ch.jeda.Direction;
import ch.jeda.Location;

/**
 * This class represents a player or an object in a {@link BlockWorld}. An
 * entity is represented by a block image. It is positioned on a {@link Field}
 * and can move in the eight directions.
 */
public class Entity {

    public static final String INVISIBLE = "Invisible";
    private static final Location INVALID = new Location(-1, -1);
    private static final double MESSAGE_TIMEOUT = 2d;
    private Field field;
    private String imageName;
    private BlockMap map;
    private String message;
    private double messageTimeout;
    private Field renderField;
    private Vector3D renderPos;
    private EntityState state;

    /**
     * Constructs a new entity that will be represented by specified image.
     *
     * @param imageName
     * @throws NullPointerException if <tt>imageName</tt> is null
     */
    public Entity(String imageName) {
        if (imageName == null) {
            throw new NullPointerException("imageName");
        }
        this.imageName = imageName;
        this.state = EntityStateIdle.INSTANCE;
    }

    protected Entity() {
        this(INVISIBLE);
    }

    public boolean canMove(Direction direction) {
        return true;
    }

    /**
     * Returns the field on which this entity is currently positioned.
     *
     * @return
     */
    public final Field field() {
        return this.field;
    }

    /**
     * Returns the current block image name of this entity.
     *
     * @return current block image name of this entity.
     */
    public final String getImageName() {
        return this.imageName;
    }

    /**
     * Returns the current x-coordinate of this entity. Returns -1 if this
     * entity is not on a map.
     *
     * @return current x-coordinate of this entity.
     */
    public final int getX() {
        return this.getPosition().x;
    }

    /**
     * Returns the current y-coordinate of this entity. Returns -1 if this
     * entity is not on a map.
     *
     * @return current y-coordinate of this entity.
     */
    public final int getY() {
        return this.getPosition().y;
    }

    public final boolean isMoving() {
        return this.state != EntityStateIdle.INSTANCE;
    }

    public final void jump(double height) {
        this.map.addEvent(new EntityJumpEvent(this, height));
    }

    /**
     * Moves this entity by one field in the specified direction
     *
     * @param direction direction in which to move this entity.
     * @throws IllegalStateException if this entity is not on a map.
     * @throws NullPointerException if <tt>direction</tt> is null
     */
    public void move(Direction direction) {
        if (direction == null) {
            throw new NullPointerException("direction");
        }
        this.map.addEvent(new EntityMoveEvent(this, direction));
    }

    public final void say(String message) {
        if (this.message == null && message != null || !this.message.equals(message)) {
            this.message = message;
            if (this.message != null) {
                this.messageTimeout = MESSAGE_TIMEOUT;
            }
        }
    }

    /**
     * Sets the name of the block image to represent this entity.
     *
     * @param imageName name of the block image
     * @throws NullPointerException if <tt>imageName</tt> is null
     */
    public final void setImageName(String imageName) {
        if (imageName == null) {
            throw new NullPointerException("imageName");
        }
        this.imageName = imageName;
        //this.fireChanged();
    }

    public final void removeFromMap() {
        if (this.map != null) {
            this.map.addEvent(new RemoveEntityEvent(map, this));
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(this.getClass().getSimpleName());
        result.append('(');
        result.append("imageName=");
        result.append(this.imageName);
        result.append(", x=");
        result.append(this.getX());
        result.append(", y=");
        result.append(this.getY());
        result.append(')');
        return result.toString();
    }

    /**
     * If this entity is in a word, the method is called every tick to allow
     * the entity to update itself.
     *
     * Override this method to implement behavior of this entity.
     */
    protected void update(BlockWorld world) {
    }

    void doCheckFloor() {
        if (this.renderPos.z < this.field.height()) {
            this.renderPos = new Vector3D(this.renderPos.x, this.renderPos.y, this.field.height());
        }
    }

    void doRemoveFromMap() {
        this.state = EntityStateIdle.INSTANCE;
        this.doSetField(null, true);
        this.doSetMap(null);
    }

    void doSetField(Field field, boolean updateRenderPos) {
        if (this.field != field) {
            if (this.field != null) {
                this.field.doRemoveEntity(this);
            }
            this.field = field;
            if (this.field != null) {
                this.field.doAddEntity(this);
            }
            // When this.field is null, the entity is not on the map anymore
            // and thus not visible.
            if (updateRenderPos) {
                if (this.field != null) {
                    this.renderPos = this.field.entityRenderPos();
                    this.updateRenderField();
                }
                else {
                    this.renderField.doRemoveRenderEntity(this);
                    this.renderField = null;
                }
            }
        }
    }

    void doSetMap(BlockMap map) {
        this.map = map;
    }

    void doStartJump(double height) {
        if (!isMoving()) {
            this.state = new EntityStateJumpUp(height);
        }
    }

    void doStartMove(Field toField) {
        if (!isMoving()) {
            this.state = new EntityStateMoveAcross(toField);
        }
    }

    String getMessage() {
        return this.message;
    }

    final Location getPosition() {
        if (this.field == null) {
            return INVALID;
        }
        else {
            return this.field.getPosition();
        }
    }

    Vector3D getRenderPos() {
        return this.renderPos;
    }

    void internalUpdate(BlockWorld world) {
        this.update(world);
        this.doSetField(this.state.nextField(this), false);
        this.state = this.state.nextState(this);
        this.updateRenderField();
        if (this.isMoving()) {
            this.modified();
        }
        if (this.messageTimeout > 0d) {
            this.messageTimeout -= world.getLastStepDuration();
            if (this.messageTimeout <= 0d) {
                this.message = null;
            }
        }
    }

    private void updateRenderField() {
        if (this.field == null) {
            if (this.renderField != null) {
                this.renderField.removeEntity(this);
            }
            return;
        }
        Vector3D newRenderPos = this.state.nextRenderPos(this);
        if (this.renderPos == null || !this.renderPos.equals(newRenderPos)) {
            this.renderPos = newRenderPos;
            this.modified();
        }
        int x = (int) Math.ceil(this.renderPos.x);
        int y = (int) Math.ceil(this.renderPos.y);
        Field newRenderField = this.map.fieldAt(x, y);
        if (this.renderField != newRenderField) {
            if (this.renderField != null) {
                this.renderField.doRemoveRenderEntity(this);
            }
            this.renderField = newRenderField;
            if (this.renderField != null) {
                this.renderField.doAddRenderEntity(this);
            }
        }
    }

    private void modified() {
        if (this.renderField != null) {
            this.renderField.modified();
        }
    }
}
