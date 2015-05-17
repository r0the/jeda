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

import ch.jeda.geometry.Circle;
import ch.jeda.geometry.Ellipse;
import ch.jeda.geometry.Polygon;
import ch.jeda.geometry.PolygonalChain;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import ch.jeda.ui.Canvas;
import ch.jeda.ui.Color;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;

final class PhysicsBodyImp implements BodyImp {

    private final double density;
    private final double friction;
    private final org.jbox2d.dynamics.Body imp;
    private final Physics physics;
    private final List<Shape> shapes;

    PhysicsBodyImp(final Physics physics, final Body body, final BodyImp oldImp) {
        density = oldImp.getDensity();
        friction = oldImp.getFriction();
        shapes = new ArrayList<Shape>();
        final BodyDef bodyDef = new BodyDef();
        bodyDef.angle = (float) oldImp.getAngleRad();
        bodyDef.angularDamping = (float) oldImp.getAngularDamping();
        bodyDef.angularVelocity = (float) oldImp.getAngularVelocity();
        bodyDef.position.x = physics.scaleLength(oldImp.getX());
        bodyDef.position.y = physics.scaleLength(oldImp.getY());
        bodyDef.linearDamping = (float) oldImp.getDamping();
        bodyDef.linearVelocity.x = (float) oldImp.getVx();
        bodyDef.linearVelocity.y = (float) oldImp.getVy();
        bodyDef.type = convert(oldImp.getType());
        bodyDef.fixedRotation = oldImp.isRotationFixed();
        imp = physics.createBodyImp(bodyDef);
        imp.m_userData = body;
        this.physics = physics;
        for (final Shape shape : oldImp.getShapes()) {
            addShape(shape);
        }
    }

    @Override
    public void addShape(final Shape shape) {
        shapes.add(shape);
        final FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = convert(shape, physics.getScale());
        fixtureDef.density = (float) density;
        fixtureDef.friction = (float) friction;
        fixtureDef.userData = this;
        imp.createFixture(fixtureDef);
    }

    @Override
    public void applyForce(final double fx, final double fy) {
        imp.applyForce(new Vec2((float) fx, (float) fy), imp.getWorldCenter());
    }

    @Override
    public void applyTorque(final double torque) {
        imp.applyTorque((float) torque);
    }

    @Override
    public boolean belongsTo(final Physics physics) {
        return physics == physics;
    }

    @Override
    public void destroy() {
        physics.destroyBodyImp(imp);
    }

    @Override
    public void drawOverlay(final Canvas canvas) {
        canvas.setColor(Color.RED);
        if (physics.isDebugging()) {
            for (final Shape shape : shapes) {
                shape.draw(canvas);
            }
        }
    }

    @Override
    public double getAngleRad() {
        return imp.getAngle();
    }

    @Override
    public double getAngularDamping() {
        return imp.getAngularDamping();
    }

    @Override
    public double getAngularVelocity() {
        return imp.getAngularVelocity();
    }

    @Override
    public double getDamping() {
        return imp.getLinearDamping();
    }

    @Override
    public double getDensity() {
        return density;
    }

    @Override
    public double getFriction() {
        return friction;
    }

    @Override
    public double getMass() {
        return imp.getMass();
    }

    @Override
    public Physics getPhysics() {
        return physics;
    }

    @Override
    public Shape[] getShapes() {
        return shapes.toArray(new Shape[shapes.size()]);
    }

    @Override
    public BodyType getType() {
        switch (imp.m_type) {
            case DYNAMIC:
                return BodyType.DYNAMIC;
            case KINEMATIC:
                return BodyType.KINEMATIC;
            case STATIC:
                return BodyType.STATIC;
            default:
                return null;
        }
    }

    @Override
    public double getVx() {
        return imp.getLinearVelocity().x;
    }

    @Override
    public double getVy() {
        return imp.getLinearVelocity().y;
    }

    @Override
    public double getX() {
        return imp.getPosition().x * physics.getScale();
    }

    @Override
    public double getY() {
        return imp.getPosition().y * physics.getScale();
    }

    @Override
    public boolean isRotationFixed() {
        return imp.isFixedRotation();
    }

    @Override
    public void setAngleRad(final double rotation) {
        // ignore
    }

    @Override
    public void setAngularDamping(final double angularDamping) {
        imp.setAngularDamping((float) angularDamping);
    }

    @Override
    public void setAngularVelocity(double angularVelocity) {
        imp.setAngularVelocity((float) angularVelocity);
    }

    @Override
    public void setDamping(final double damping) {
        imp.setLinearDamping((float) damping);
    }

    @Override
    public void setDensity(final double density) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setFriction(final double friction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPosition(final double x, final double y) {
        throw new IllegalStateException("Body is in a physics simulation.");
    }

    @Override
    public void setRotationFixed(final boolean rotationFixed) {
        imp.setFixedRotation(rotationFixed);
    }

    @Override
    public void setType(final BodyType type) {
        imp.setType(convert(type));
    }

    @Override
    public void setVelocity(double vx, double vy) {
        imp.setLinearVelocity(new Vec2((float) vx, (float) vy));
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Body(name=");
        result.append(", x=");
        result.append(getX());
        result.append(", y=");
        result.append(getY());
        result.append(")");
        return result.toString();
    }

    private static org.jbox2d.dynamics.BodyType convert(final BodyType bodyType) {
        switch (bodyType) {
            case DYNAMIC:
                return org.jbox2d.dynamics.BodyType.DYNAMIC;
            case KINEMATIC:
                return org.jbox2d.dynamics.BodyType.KINEMATIC;
            case STATIC:
                return org.jbox2d.dynamics.BodyType.STATIC;
            default:
                return null;
        }
    }

    private static org.jbox2d.collision.shapes.Shape convert(final Shape shape, final double scale) {
        if (shape instanceof Circle) {
            return convertCircle((Circle) shape, scale);
        }
        else if (shape instanceof Ellipse) {
            return convertEllipse((Ellipse) shape, scale);
        }
        else if (shape instanceof Rectangle) {
            return convertRectangle((Rectangle) shape, scale);
        }
        else if (shape instanceof Polygon) {
            return convertPolygon((Polygon) shape, scale);
        }
        else if (shape instanceof PolygonalChain) {
            return convertPolygonalChain((PolygonalChain) shape, scale);
        }
        else {
            throw new RuntimeException("Invalid shape type.");
        }
    }

    private static org.jbox2d.collision.shapes.Shape convertCircle(final Circle circle, final double scale) {
        final org.jbox2d.collision.shapes.CircleShape result = new org.jbox2d.collision.shapes.CircleShape();
        result.m_radius = (float) (circle.getRadius() / scale);
        return result;
    }

    private static org.jbox2d.collision.shapes.Shape convertEllipse(final Ellipse ellipse, final double scale) {
        if (ellipse.getEccentricity() < 0.1) {
            final org.jbox2d.collision.shapes.CircleShape result = new org.jbox2d.collision.shapes.CircleShape();
            result.m_radius = (float) ((ellipse.getRadiusX() + ellipse.getRadiusY()) / (2.0 * scale));
            return result;
        }
        else {
            return convertPolygon(ellipse.toPolygon(24), scale);
        }
    }

    private static org.jbox2d.collision.shapes.Shape convertRectangle(final Rectangle rectangle, final double scale) {
        final org.jbox2d.collision.shapes.PolygonShape result = new org.jbox2d.collision.shapes.PolygonShape();
        result.setAsBox((float) (rectangle.getWidth() / scale / 2.0), (float) (rectangle.getHeight() / scale / 2.0), new Vec2(0, 0), 0);
        return result;
    }

    private static org.jbox2d.collision.shapes.Shape convertPolygon(final Polygon polygon, final double scale) {
        final org.jbox2d.collision.shapes.PolygonShape result = new org.jbox2d.collision.shapes.PolygonShape();
        final Vec2[] vertices = new Vec2[polygon.getVertexCount()];
        for (int i = 0; i < polygon.getVertexCount(); ++i) {
            vertices[i] = new Vec2((float) (polygon.getVertexX(i) / scale), (float) (polygon.getVertexY(i) / scale));
        }

        result.set(vertices, vertices.length);
        return result;
    }

    private static org.jbox2d.collision.shapes.Shape convertPolygonalChain(final PolygonalChain chain, final double scale) {
        final org.jbox2d.collision.shapes.ChainShape result = new org.jbox2d.collision.shapes.ChainShape();
        final Vec2[] vertices = new Vec2[chain.getVertexCount()];
        for (int i = 0; i < chain.getVertexCount(); ++i) {
            vertices[i] = new Vec2((float) (chain.getVertexX(i) / scale), (float) (chain.getVertexY(i) / scale));
        }

        result.createChain(vertices, vertices.length);
        return result;
    }
}
