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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Entities {

    private final List<Entity> list;
    private final TypeMap<Entity> typeMap;
    private final SortedList<Entity> updateOrder;
    private final SortedList<Entity> paintOrder;
    private final List<Entity> pendingInsertions;
    private final List<Entity> pendingDeletions;

    Entities() {
        this.list = new ArrayList<Entity>();
        this.typeMap = new TypeMap(Entity.class);
        this.paintOrder = new SortedList<Entity>(new PaintOrderComparator());
        this.updateOrder = new SortedList<Entity>(new UpdateOrderComparator());
        this.pendingDeletions = new ArrayList<Entity>();
        this.pendingInsertions = new ArrayList<Entity>();
    }

    void add(Entity entity) {
        this.pendingInsertions.add(entity);
        this.pendingDeletions.remove(entity);
    }

    void executePendingOperations() {
        if (!this.pendingDeletions.isEmpty()) {
            this.list.removeAll(this.pendingDeletions);
            this.typeMap.removeAll(this.pendingDeletions);
            this.updateOrder.removeAll(this.pendingDeletions);
            this.paintOrder.removeAll(this.pendingDeletions);
            this.pendingDeletions.clear();
        }

        if (!this.pendingInsertions.isEmpty()) {
            this.list.addAll(this.pendingInsertions);
            this.typeMap.addAll(this.pendingInsertions);
            this.updateOrder.addAll(this.pendingInsertions);
            this.paintOrder.addAll(this.pendingInsertions);
            this.pendingInsertions.clear();
        }
    }

    Entity get(int index) {
        return this.list.get(index);
    }

    int getEntityCount() {
        return this.list.size();
    }

    Iterable<Entity> paintOrderIterator() {
        return this.paintOrder;
    }

    Iterable<Entity> updateOrderIterator() {
        return this.updateOrder;
    }

    <T extends Entity> List<T> getByLocation(double x, double y, Class<T> type) {
        final ArrayList<T> result = new ArrayList();
        for (T entity : this.byType(type)) {
            if (entity.getCollisionShape().contains(x, y)) {
                result.add(entity);
            }
        }

        return result;
    }

    <T extends Entity> List<T> getIntersectingActors(Shape shape, Class<T> type) {
        final ArrayList<T> result = new ArrayList();
        if (shape != null) {
            for (T entity : this.byType(type)) {
                if (shape.intersectsWith(entity.getCollisionShape())) {
                    result.add(entity);
                }
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    <T extends Entity> List<T> byType(Class<T> type) {
        return (List<T>) this.typeMap.get(type);
    }

    void remove(Entity remove) {
        this.pendingDeletions.add(remove);
        this.pendingInsertions.remove(remove);
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
