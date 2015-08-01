/*
 * Copyright (C) 2015 by Stefan Rothe
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
package ch.jeda.physics;

import ch.jeda.ui.Alignment;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import ch.jeda.ui.Typeface;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Transform;
import org.jbox2d.common.Vec2;
import org.jbox2d.particle.ParticleColor;

class PhysicsDebugDraw extends DebugDraw {

    private final Alignment alignment;
    private final float lineWidth;
    private final Typeface typeface;
    private final float textSize;
    private Canvas canvas;

    PhysicsDebugDraw() {
        this.alignment = Alignment.BOTTOM_LEFT;
        this.lineWidth = 1f;
        this.textSize = 15f;
        this.typeface = Typeface.SANS_SERIF;
        setFlags(e_shapeBit | e_wireframeDrawingBit | e_jointBit | e_centerOfMassBit);
    }

    @Override
    public void drawPoint(final Vec2 argPoint, final float argRadiusOnScreen, final Color3f argColor) {
        setColor(argColor);
        canvas.fillCircle(argPoint.x, argPoint.y, argRadiusOnScreen);
    }

    @Override
    public void drawSolidPolygon(final Vec2[] vertices, final int vertexCount, final Color3f color) {
        float[] points = new float[2 * vertexCount];
        for (int i = 0; i < vertexCount; ++i) {
            points[2 * i] = vertices[i].x;
            points[2 * i + 1] = vertices[i].y;
        }

        setColor(color);
        canvas.setLineWidth(lineWidth);
        canvas.fillPolygon(points);
    }

    @Override
    public void drawCircle(final Vec2 center, final float radius, final Color3f color) {
        setColor(color);
        canvas.setLineWidth(lineWidth);
        canvas.drawCircle(center.x, center.y, radius);
    }

    @Override
    public void drawSolidCircle(final Vec2 center, final float radius, final Vec2 axis, final Color3f color) {
        setColor(color);
        canvas.fillCircle(center.x, center.y, radius);
    }

    @Override
    public void drawSegment(final Vec2 p1, final Vec2 p2, final Color3f color) {
        float[] points = new float[4];
        points[0] = p1.x;
        points[1] = p1.y;
        points[2] = p2.x;
        points[3] = p2.y;
        setColor(color);
        canvas.setLineWidth(lineWidth);
        canvas.drawPolyline(points);
    }

    @Override
    public void drawTransform(final Transform xf) {
    }

    @Override
    public void drawString(final float x, final float y, final String s, final Color3f color) {
        setColor(color);
        canvas.setAlignment(alignment);
        canvas.setTextSize(textSize);
        canvas.setTypeface(typeface);
        canvas.drawText(x, y, s);
    }

    @Override
    public void drawParticles(final Vec2[] centers, final float radius, final ParticleColor[] colors, final int count) {
        // ignore
    }

    @Override
    public void drawParticlesWireframe(final Vec2[] centers, final float radius, final ParticleColor[] colors,
                                       final int count) {
        // ignore
    }

    void setCanvas(final Canvas canvas) {
        this.canvas = canvas;
    }

    private void setColor(final Color3f color) {
        canvas.setColor(new Color((int) (255 * color.x), (int) (255 * color.y), (int) (255 * color.z)));
    }
}
