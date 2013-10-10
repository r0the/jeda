/*
 * Copyright (C) 2013 by Stefan Rothe
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
package ch.jeda.geometry;

import ch.jeda.Util;

/**
 * Implements the Greiner-Hormann polygon clipping algorithm. See <i>Efficient clipping of arbitrary polygons</i>,
 * Günther Greiner and Kai Hormann, ACM Transactions on Graphics, 17(2), April 1998, pp. 71 - 83
 */
class GreinerHormann {

    enum Operation {

        INTERSECTION, UNION
    }
    private Polygon p;
    private Polygon q;
    private Node headP;
    private Node headQ;

    void execute(final Polygon p, final Polygon q, final Polygons result, final Operation operation) {
        this.p = p;
        this.q = q;
        this.headP = convertPolygon(p);
        this.headQ = convertPolygon(q);
        this.phaseOne();
        this.phaseTwo();
        switch (operation) {
            case INTERSECTION:
                this.buildIntersection(result);
                break;
            case UNION:
                this.buildUnion(result);
                break;
            default:
                break;
        }
    }

    /**
     * Creates a double linked list of Nodes representing the corners of a polygon. The resulting linked list is cyclic.
     */
    private static Node convertPolygon(final Polygon p) {
        final Node head = Node.createCorner(p.getX(0), p.getY(0));
        Node tail = head;
        for (int i = 1; i < p.size(); ++i) {
            Node node = Node.createCorner(p.getX(i), p.getY(i));
            tail.insertAfter(node);
            tail = node;
        }

        return head;
    }

    /**
     * Performs phase one of the algorithm. Iterates over all edges of both polygons and determines intersections.
     */
    private void phaseOne() {
        Node currP = this.headP;
        do {
            Node currQ = this.headQ;
            do {
                intersect(currP, currQ);
                currQ = currQ.nextCorner();
            }
            while (currQ != this.headQ);

            currP = currP.nextCorner();
        }
        while (currP != this.headP);
    }

    /**
     * Performs phase two of the algorithm. Iterates through both linked lists and marks all intersections as entry or
     * exit points accordingly.
     */
    private void phaseTwo() {
        markEnterExit(this.headP, this.q);
        markEnterExit(this.headQ, this.p);
    }

    /**
     * Performs phase three of the algorithm. Builds the intersection of the two polygons.
     */
    private void buildIntersection(final Polygons result) {
        Node current = this.headP.firstPendingIntersection();
        while (current != null) {
            Node polyStart = current;
            final PolygonBuilder res = new PolygonBuilder();
            res.addPoint(current.x, current.y);
            do {
                current.visited = true;
                if (current.entry) {
                    do {
                        current = current.next;
                        res.addPoint(current.x, current.y);
                    }
                    while (!current.intersection);
                }
                else {
                    do {
                        current = current.prev;
                        res.addPoint(current.x, current.y);
                    }
                    while (!current.intersection);
                }
                current = current.neighbour;
            }
            while (current != polyStart);
            result.add(res.toPolygon());
            current = this.headP.firstPendingIntersection();
        }
    }

    /**
     * Performs phase three of the algorithm. Builds the intersection of the two polygons.
     */
    private void buildUnion(final Polygons result) {
        Node current = this.headP.firstPendingIntersection();
        while (current != null) {
            Node polyStart = current;
            final PolygonBuilder res = new PolygonBuilder();
            res.addPoint(current.x, current.y);
            do {
                current.visited = true;
                if (current.entry) {
                    current = current.neighbour;
                    do {
                        current = current.next;
                        res.addPoint(current.x, current.y);
                    }
                    while (!current.intersection);
                }
                else {
                    current = current.neighbour;
                    do {
                        current = current.prev;
                        res.addPoint(current.x, current.y);
                    }
                    while (!current.intersection);
                }
            }
            while (current != polyStart);
            result.add(res.toPolygon());
            current = this.headP.firstPendingIntersection();
        }
    }

    private static float distance(final float x1, final float y1, final float x2, final float y2) {
        return Util.distance(x1 - x2, y1 - y2);
    }

    /**
     * Test if the edges starting with cp and cq intersect. If they do, create nodes representing the intersection and
     * insert them in the linked lists.
     */
    private static void intersect(final Node p1, final Node q1) {
        final Node p2 = p1.nextCorner();
        final Node q2 = q1.nextCorner();

        final float par = (p2.x - p1.x) * (q2.y - q1.y) - (p2.y - p1.y) * (q2.x - q1.x);
        if (Util.isZero(par)) {
            // parallel lines
            return;
        }

        final float tp = ((q1.x - p1.x) * (q2.y - q1.y) - (q1.y - p1.y) * (q2.x - q1.x)) / par;
        final float tq = ((p2.y - p1.y) * (q1.x - p1.x) - (p2.x - p1.x) * (q1.y - p1.y)) / par;
        if (tp < 0f || tp > 1f || tq < 0f || tq > 1f) {
            return;
        }

        final float x = p1.x + tp * (p2.x - p1.x);
        final float y = p1.y + tp * (p2.y - p1.y);
        final float alphaP = distance(p1.x, p1.y, x, y) / distance(p1.x, p1.y, p2.x, p2.y);
        final float alphaQ = distance(q1.x, q1.y, x, y) / distance(q1.x, q1.y, q2.x, q2.y);
        Node ip = Node.createIntersection(x, y, alphaP);
        Node iq = Node.createIntersection(x, y, alphaQ);
        ip.neighbour = iq;
        iq.neighbour = ip;
        p1.insertIntersection(ip);
        q1.insertIntersection(iq);
    }

    private static void markEnterExit(final Node head, final Polygon other) {
        // If the first point of the polygon is inside the other polygon, the first intersection will be an exit.
        boolean entry = !other.contains(head.x, head.y);
        Node curr = head;
        do {
            if (curr.intersection) {
                curr.entry = entry;
                entry = !entry;
            }

            curr = curr.next;
        }
        while (curr != head);
    }

    private static class Node {

        float x;
        float y;
        Node next;
        Node prev;
        boolean intersection;
        boolean entry;
        Node neighbour;
        float alpha;
        boolean visited;

        static Node createCorner(final float x, final float y) {
            final Node result = new Node(x, y);
            result.next = result;
            result.prev = result;
            return result;
        }

        static Node createIntersection(final float x, final float y, final float alpha) {
            final Node result = new Node(x, y);
            result.alpha = alpha;
            result.intersection = true;
            return result;
        }

        public Node(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Node firstPendingIntersection() {
            Node result = this;
            while (!result.intersection || (result.intersection && result.visited)) {
                result = result.next;
                // Back to myself, no unvisited intersection found
                if (result == this) {
                    return null;
                }
            }

            return result;
        }

        public void insertAfter(Node node) {
            node.prev = this;
            node.next = this.next;
            node.prev.next = node;
            node.next.prev = node;
        }

        public void insertBefore(Node node) {
            node.prev = this.prev;
            node.next = this;
            node.prev.next = node;
            node.next.prev = node;
        }

        public void insertIntersection(Node toInsert) {
            Node node = this.next;
            while (node.intersection && node.alpha < toInsert.alpha) {
                node = node.next;
            }

            node.insertBefore(toInsert);
        }

        public Node nextCorner() {
            Node result = this.next;
            while (result != null && result.intersection) {
                result = result.next;
            }

            return result;
        }
    }
}
