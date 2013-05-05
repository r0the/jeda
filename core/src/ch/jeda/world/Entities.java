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

import ch.jeda.geometry.Shape;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Entities {

    private static final Entity[] ENTITY_ARRAY = new Entity[0];
    private static final Comparator<Entity> PAINT_SORT_ORDER = new PaintOrderComparator();
    private static final Comparator<Entity> UPDATE_SORT_ORDER = new UpdateOrderComparator();
    private final List<Entity> list;
    private final List<Entity> pendingInsertions;
    private final List<Entity> pendingDeletions;
    private final Map<Class<?>, Object[]> typeMap;
    private Entity[] updateOrder;
    private Entity[] paintOrder;

    Entities() {
        this.list = new ArrayList<Entity>();
        this.typeMap = new HashMap<Class<?>, Object[]>();
        this.paintOrder = ENTITY_ARRAY;
        this.updateOrder = ENTITY_ARRAY;
        this.pendingDeletions = new ArrayList<Entity>();
        this.pendingInsertions = new ArrayList<Entity>();
    }

    void add(Entity entity) {
        this.pendingInsertions.add(entity);
        this.pendingDeletions.remove(entity);
    }

    void executePendingOperations() {
        this.checkTypeMap();

        if (!this.pendingDeletions.isEmpty()) {
            this.list.removeAll(this.pendingDeletions);
            this.pendingDeletions.clear();
            this.paintOrder = null;
            this.updateOrder = null;
        }

        if (!this.pendingInsertions.isEmpty()) {
            this.list.addAll(this.pendingInsertions);
            this.pendingInsertions.clear();
            this.paintOrder = null;
            this.updateOrder = null;
        }
    }

    private void checkTypeMap() {
        for (Entity e : this.pendingDeletions) {
            Class c = e.getClass();
            while (!c.equals(Entity.class)) {
                this.typeMap.remove(c);
                c = c.getSuperclass();
            }
        }

        for (Entity e : this.pendingInsertions) {
            Class c = e.getClass();
            while (!c.equals(Entity.class)) {
                this.typeMap.remove(c);
                c = c.getSuperclass();
            }
        }
    }

    void clear() {
        this.pendingDeletions.addAll(this.list);
    }

    Entity get(int index) {
        return this.list.get(index);
    }

    <T extends Entity> T[] getByLocation(float x, float y, Class<T> type) {
        final List<T> result = new ArrayList<T>();
        final T[] entities = this.getByType(type);
        for (int i = 0; i < entities.length; ++i) {
            if (entities[i].getCollisionShape().contains(x, y)) {
                result.add(entities[i]);
            }
        }

        return toArray(result, type);
    }

    <T extends Entity> T[] getByType(Class<T> type) {
        if (!this.typeMap.containsKey(type)) {
            final List<Entity> l = new ArrayList<Entity>();
            for (Entity e : this.list) {
                if (type.isAssignableFrom(e.getClass())) {
                    l.add(e);
                }
            }

            this.typeMap.put(type, toArray(l, type));
        }
        return (T[]) this.typeMap.get(type);
    }

    <T extends Entity> T[] getIntersectingEntities(Shape shape, Class<T> type) {
        final List<T> result = new ArrayList<T>();
        if (shape != null) {
            final T[] entities = this.getByType(type);
            for (int i = 0; i < entities.length; ++i) {
                if (shape.intersectsWith(entities[i].getCollisionShape())) {
                    result.add(entities[i]);
                }
            }
        }

        return toArray(result, type);
    }

    int getEntityCount() {
        return this.list.size();
    }

    Entity[] paintOrder() {
        if (this.paintOrder == null) {
            this.paintOrder = this.list.toArray(ENTITY_ARRAY);
            Arrays.sort(this.paintOrder, 0, this.paintOrder.length, PAINT_SORT_ORDER);
        }

        return this.paintOrder;
    }

    void remove(Entity remove) {
        this.pendingDeletions.add(remove);
        this.pendingInsertions.remove(remove);
    }

    Entity[] updateOrder() {
        if (this.updateOrder == null) {
            this.updateOrder = this.list.toArray(ENTITY_ARRAY);
            Arrays.sort(this.updateOrder, 0, this.updateOrder.length, UPDATE_SORT_ORDER);
        }

        return this.updateOrder;
    }

    private static <T> T[] toArray(List<?> list, Class<T> type) {
        return list.toArray((T[]) Array.newInstance(type, list.size()));
    }

    private static class PaintOrderComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity first, Entity second) {
            return first.paintOrder() - second.paintOrder();
        }
    }

    private static class UpdateOrderComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity first, Entity second) {
            return first.updateOrder() - second.updateOrder();
        }
    }
}
