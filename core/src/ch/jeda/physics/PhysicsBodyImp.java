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

import ch.jeda.MathF;
import ch.jeda.geometry.Circle;
import ch.jeda.geometry.Ellipse;
import ch.jeda.geometry.Polygon;
import ch.jeda.geometry.Polyline;
import ch.jeda.geometry.Rectangle;
import ch.jeda.geometry.Shape;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;

final class PhysicsBodyImp implements BodyImp {

    private final float density;
    private final float friction;
    private final org.jbox2d.dynamics.Body imp;
    private final Physics physics;
    private final List<Shape> shapes;

    PhysicsBodyImp(final Physics physics, final Body body, final BodyImp oldImp) {
        density = oldImp.getDensity();
        friction = oldImp.getFriction();
        shapes = new ArrayList<Shape>();
        final BodyDef bodyDef = new BodyDef();
        bodyDef.angle = oldImp.getAngleRad();
        bodyDef.angularDamping = oldImp.getAngularDamping();
        bodyDef.angularVelocity = oldImp.getAngularVelocity();
        bodyDef.position.x = physics.scaleLength(oldImp.getX());
        bodyDef.position.y = physics.scaleLength(oldImp.getY());
        bodyDef.linearDamping = oldImp.getDamping();
        bodyDef.linearVelocity.x = oldImp.getVx();
        bodyDef.linearVelocity.y = oldImp.getVy();
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
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.userData = this;
        imp.createFixture(fixtureDef);
    }

    @Override
    public void applyForce(final float fx, final float fy, final float x, final float y) {
        imp.applyForce(new Vec2(fx, fy), imp.getWorldPoint(new Vec2(x, y)));
    }

    @Override
    public void applyTorque(final float torque) {
        imp.applyTorque(torque);
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
    public float getAngleRad() {
        return MathF.normalizeAngle(imp.getAngle());
    }

    @Override
    public float getAngularDamping() {
        return imp.getAngularDamping();
    }

    @Override
    public float getAngularVelocity() {
        return imp.getAngularVelocity();
    }

    @Override
    public float getDamping() {
        return imp.getLinearDamping();
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public float getMass() {
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
    public float getVelocity() {
        return imp.getLinearVelocity().length();
    }

    @Override
    public float getVx() {
        return imp.getLinearVelocity().x;
    }

    @Override
    public float getVy() {
        return imp.getLinearVelocity().y;
    }

    @Override
    public float getX() {
        return imp.getPosition().x * physics.getScale();
    }

    @Override
    public float getY() {
        return imp.getPosition().y * physics.getScale();
    }

    @Override
    public boolean isRotationFixed() {
        return imp.isFixedRotation();
    }

    @Override
    public void setAngleRad(final float rotation) {
        throw new IllegalStateException("Body is in a physics simulation.");
    }

    @Override
    public void setAngularDamping(final float angularDamping) {
        imp.setAngularDamping(angularDamping);
    }

    @Override
    public void setAngularVelocity(final float angularVelocity) {
        imp.setAngularVelocity(angularVelocity);
    }

    @Override
    public void setDamping(final float damping) {
        imp.setLinearDamping(damping);
    }

    @Override
    public void setDensity(final float density) {
        Fixture fixture = imp.m_fixtureList;
        while (fixture != null) {
            fixture.setDensity(density);
            fixture = fixture.m_next;
        }

        imp.resetMassData();
    }

    @Override
    public void setFriction(final float friction) {
        Fixture fixture = imp.m_fixtureList;
        while (fixture != null) {
            fixture.setFriction(friction);
            fixture = fixture.m_next;
        }
    }

    @Override
    public void setPosition(final float x, final float y) {
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
    public void setVelocity(final float vx, final float vy) {
        imp.setLinearVelocity(new Vec2(vx, vy));
    }

    @Override
    public boolean shouldDrawOverlay() {
        return physics.isDebugging();
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

    private static org.jbox2d.collision.shapes.Shape convert(final Shape shape, final float scale) {
        if (shape instanceof Circle) {
            return convertCircle((Circle) shape, scale);
        }
        else if (shape instanceof Ellipse) {
            final Ellipse ellipse = (Ellipse) shape;
            if (ellipse.getEccentricity() < 0.1) {
                return convertCircle(ellipse.toCircle(), scale);
            }
            else {
                return convertPolygon(ellipse.toPolygon(24), scale);
            }
        }
        else if (shape instanceof Rectangle) {
            return convertPolygon(((Rectangle) shape).toPolygon(), scale);
        }
        else if (shape instanceof Polygon) {
            return convertPolygon((Polygon) shape, scale);
        }
        else if (shape instanceof Polyline) {
            return convertPolygonalChain((Polyline) shape, scale);
        }
        else {
            throw new RuntimeException("Invalid shape type.");
        }
    }

    private static org.jbox2d.collision.shapes.Shape convertCircle(final Circle circle, final float scale) {
        final org.jbox2d.collision.shapes.CircleShape result = new org.jbox2d.collision.shapes.CircleShape();
        result.m_p.x = circle.getCenterX() / scale;
        result.m_p.y = circle.getCenterY() / scale;
        result.m_radius = circle.getRadius() / scale;
        return result;
    }

    private static org.jbox2d.collision.shapes.Shape convertPolygon(final Polygon polygon, final float scale) {
        final org.jbox2d.collision.shapes.PolygonShape result = new org.jbox2d.collision.shapes.PolygonShape();
        final Vec2[] vertices = new Vec2[polygon.getPointCount()];
        for (int i = 0; i < polygon.getPointCount(); ++i) {
            vertices[i] = new Vec2(polygon.getPointX(i) / scale, polygon.getPointY(i) / scale);
        }

        result.set(vertices, vertices.length);
        return result;
    }

    private static org.jbox2d.collision.shapes.Shape convertPolygonalChain(final Polyline chain, final float scale) {
        final org.jbox2d.collision.shapes.ChainShape result = new org.jbox2d.collision.shapes.ChainShape();
        final Vec2[] vertices = new Vec2[chain.getPointCount()];
        for (int i = 0; i < chain.getPointCount(); ++i) {
            vertices[i] = new Vec2(chain.getPointX(i) / scale, chain.getPointY(i) / scale);
        }

        result.createChain(vertices, vertices.length);
        return result;
    }
}
