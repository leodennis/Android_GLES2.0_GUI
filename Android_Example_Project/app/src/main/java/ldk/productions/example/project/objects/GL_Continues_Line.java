package ldk.productions.example.project.objects;

import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ldk.productions.example.project.data.CGPoint;

/**
 * Created by Leo on 12.04.2017.
 */

public class GL_Continues_Line extends GL_Colored_Shape {

    private float strokeWidth;
    private List<GL_Line> lines = new ArrayList<>();
    private List<GL_Circle> dots = new ArrayList<>();

    public GL_Continues_Line(float strokeWidth) {
        super(0,0);
        init(null, strokeWidth, Color.BLACK);
    }

    public GL_Continues_Line(float strokeWidth, int color) {
        super(0,0);
        init(null, strokeWidth, color);
    }

    public GL_Continues_Line(List<CGPoint> points, float strokeWidth) {
        super(0,0);
        init(points, strokeWidth, Color.BLACK);
    }

    public GL_Continues_Line(List<CGPoint> points, float strokeWidth, int color) {
        super(0,0);
        init(points, strokeWidth, color);
    }

    /**
     * Copy constructor
     * @param obj the GL_Continues_Line to be copied
     */
    public GL_Continues_Line(GL_Continues_Line obj) {
        super(obj);
        this.strokeWidth = obj.strokeWidth;

        for (GL_Line line : obj.lines) {
            this.lines.add((GL_Line) line.clone());
        }

        for (GL_Circle circle : obj.dots) {
            this.dots.add((GL_Circle) circle.clone());
        }
    }

    @Override
    public Object clone() {
        return new GL_Continues_Line(this);
    }

    private void init(List<CGPoint> points, float strokeWidth, int color) {
        setColor(color);
        this.strokeWidth = strokeWidth;
        if (points != null) {
            if (points.size() > 0) {
                setPosition(points.get(0).getX(), points.get(0).getY());
                dots.add(new GL_Circle(points.get(0), (int) (strokeWidth/2), color));
                for (int i = 1; i<points.size(); i++) {
                    dots.add(new GL_Circle(points.get(i), (int) (strokeWidth/2), color));
                    lines.add(new GL_Line(points.get(i), points.get(i-1), color, strokeWidth));
                }
            }
        }
    }

    public void addPoint(@NonNull CGPoint p) {
        if (dots.size() > 0) {
            lines.add(new GL_Line(p, dots.get(dots.size()-1).getPos(), color, strokeWidth));
        }
        dots.add(new GL_Circle(p, (int) (strokeWidth/2), color));
    }

    public void clearAllPoints() {
        lines.clear();
        dots.clear();
    }

    public int getLineCount() {
        return lines.size();
    }

    @Override
    protected void generateVertexArray() {
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (visible) {
            for (GL_Circle circle : dots)
                circle.draw(projectionMatrix);

            for (GL_Line line : lines)
                line.draw(projectionMatrix);

        }
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        for (GL_Circle circle : dots) {
            if (circle.collidesWithPoint(x, y))
                return true;
        }
        for (GL_Line line : lines) {
            if (line.collidesWithPoint(x, y))
                return true;
        }
        return false;
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        for (GL_Circle circle : dots) {
            if (circle.collidesWithRectangle(x, y, w, h))
                return true;
        }
        for (GL_Line line : lines) {
            if (line.collidesWithRectangle(x, y, w, h))
                return true;
        }
        return false;
    }

    public boolean collidesWithCircle(CGPoint circleM, int r) {
        for (GL_Circle circle : dots) {
            if (GL_Shape.circleCircleCollision(circle.getPosX(), circle.getPosY(), circle.getRadius(), circleM.getX(), circleM.getY(), r))
                return true;
        }
        for (GL_Line line : lines) {
            if (line.toPolygon().intersects(r, circleM))
                return true;
        }
        return false;
    }

    public void move(int x, int y) {
        for (GL_Circle circle : dots) {
            circle.setPosition(circle.getPosX() + x, circle.getPosY() + y);
        }
        for (GL_Line line : lines) {
            line.setPosition(line.getPosX() + x, line.getPosY() + y);
            line.setPos2(new CGPoint(line.getPos2().getX() + x, line.getPos2().getY() + y));
        }
    }

    @Override
    public CGPoint getPosTopLeft() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (GL_Circle circle : dots) {
            if (minX > circle.getPosTopLeft().getX())
                minX = circle.getPosTopLeft().getY();
            if (minY > circle.getPosTopLeft().getY())
                minY = circle.getPosTopLeft().getY();
        }
        return new CGPoint(minX, minY);
    }

    @Override
    public CGPoint getPosBottomRight() {
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (GL_Circle circle : dots) {
            if (maxX < circle.getPosBottomRight().getX())
                maxX = circle.getPosBottomRight().getX();
            if (maxY < circle.getPosBottomRight().getY())
                maxY = circle.getPosBottomRight().getY();
        }
        return new CGPoint(maxX, maxY);
    }

}
