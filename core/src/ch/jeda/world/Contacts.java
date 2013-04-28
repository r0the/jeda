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

import ch.jeda.geometry.Collision;
import ch.jeda.geometry.Shape;
import java.util.ArrayList;
import java.util.List;

class Contacts {

    private final List<Entity.Contact> contacts;
    private final List<Detector> detectors;

    public Contacts() {
        this.contacts = new ArrayList<Entity.Contact>();
        this.detectors = new ArrayList<Detector>();
    }

    void addDetection(final Class<? extends Entity> type,
                      final float restitution) {
        this.detectors.add(new SingleTypeDetector(type, restitution));
    }

    void addDetection(final Class<? extends Entity> type1,
                      final Class<? extends Entity> type2,
                      final float restitution) {
        this.detectors.add(new DoubleTypeDetector(type1, type2, restitution));
    }

    void detect(final World world) {
        this.contacts.clear();
        for (int i = 0; i < this.detectors.size(); ++i) {
            this.detectors.get(i).detect(this, world);
        }
    }

    void check() {
        for (int i = 0; i < this.contacts.size(); ++i) {
            this.contacts.get(i).check();
        }
    }

    void resolve(final float dt) {
        int iterations = 0;
        float max;
        int maxIndex = -1;
        while (iterations < this.contacts.size() * 2) {
            max = -Float.MAX_VALUE;
            for (int i = 0; i < this.contacts.size(); ++i) {
                final Entity.Contact c = this.contacts.get(i);
                if (c.getPenetration() >= 0f) {
                    final float v = c.separatingSpeed();
                    if (v > max) {
                        max = v;
                        maxIndex = i;
                    }
                }
            }

            if (maxIndex == -1) {
                return;
            }

            final Entity.Contact c = this.contacts.get(maxIndex);
            c.resolve(dt);

            for (int i = 0; i < this.contacts.size(); ++i) {
                this.contacts.get(i).updatePenetration(c);
            }

            ++iterations;
        }
    }

    private void checkCollision(final Entity e1, final Entity e2,
                                final float restitution) {
        // Cannot collide two immobile entities.
        if (!e1.canMove() && !e2.canMove()) {
            return;
        }

        final Shape s1 = e1.getCollisionShape();
        final Shape s2 = e2.getCollisionShape();
        if (s1 == null || s2 == null) {
            return;
        }

        final Collision c = s1.collideWith(s2);
        if (c == null) {
            return;
        }

        this.contacts.add(new Entity.Contact(e1, e2, c, restitution));
    }

    private abstract static class Detector {

        abstract void detect(final Contacts contacts, final World world);
    }

    private static class DoubleTypeDetector extends Detector {

        private final Class<? extends Entity> type1;
        private final Class<? extends Entity> type2;
        private final float restitution;

        public DoubleTypeDetector(final Class<? extends Entity> type1,
                                  final Class<? extends Entity> type2,
                                  final float restitution) {
            this.type1 = type1;
            this.type2 = type2;
            this.restitution = restitution;
        }

        @Override
        void detect(final Contacts contacts, final World world) {
            Entity[] entities1 = world.entities(this.type1);
            Entity[] entities2 = world.entities(this.type2);
            for (int i = 0; i < entities1.length; ++i) {
                for (int j = 0; j < entities2.length; ++j) {
                    contacts.checkCollision(entities1[i], entities2[j],
                                            this.restitution);
                }
            }
        }
    }

    private static class SingleTypeDetector extends Detector {

        private final Class<? extends Entity> type;
        private final float restitution;

        public SingleTypeDetector(final Class<? extends Entity> type,
                                  final float restitution) {
            this.type = type;
            this.restitution = restitution;
        }

        @Override
        void detect(final Contacts contacts, final World world) {
            Entity[] entities = world.entities(this.type);
            for (int i = 0; i < entities.length; ++i) {
                for (int j = i + 1; j < entities.length; ++j) {
                    contacts.checkCollision(entities[i], entities[j],
                                            this.restitution);
                }
            }
        }
    }
}
