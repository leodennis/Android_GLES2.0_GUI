package ldk.productions.example.project.objects;

import android.graphics.Color;

import java.util.ArrayList;

import ldk.productions.example.project.data.CGPoint;
import ldk.productions.example.project.data.VertexArray;
import ldk.productions.example.project.programs.ProgramManager;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Leo on 07.12.2015.
 */
public class GL_Triangle extends GL_Colored_Shape {

    protected CGPoint point2;
    protected CGPoint point3;

    protected int color2;
    protected int color3;
    protected Integer colorM = null;

    protected MyPolygon polygonSave = null;

    public GL_Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        super(x1, y1);
        init(new CGPoint(x2, y2), new CGPoint(x3, y3), Color.BLACK);
    }

    public GL_Triangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        super(x1, y1);
        init(new CGPoint(x2, y2), new CGPoint(x3, y3), color);
    }

    public GL_Triangle(CGPoint p1, CGPoint p2, CGPoint p3) {
        super(p1);
        init(p2, p3, Color.BLACK);
    }

    public GL_Triangle(CGPoint p1, CGPoint p2, CGPoint p3, int color) {
        super(p1);
        init(p2, p3, color);
    }

    /**
     * Copy constructor
     * @param obj the GL_Triangle to be copied
     */
    public GL_Triangle(GL_Triangle obj) {
        super(obj);
        this.point2 = new CGPoint(obj.point2.x, obj.point2.y);
        this.point3 = new CGPoint(obj.point3.x, obj.point3.y);
        this.color2 = obj.color2;
        this.color3 = obj.color3;
        this.regeneratePolygon();
    }

    @Override
    public Object clone() {
        return new GL_Triangle(this);
    }

    private void init(CGPoint point2, CGPoint point3, int color) {
        this.point2 = point2;
        this.point3 = point3;
        this.color = color;

        generateVertexArray();
    }

    protected void generateVertexArray() {
        float r1 = Color.red(color) / MAXIMUM_RBG_COLOR_VALUE;
        float g1 = Color.green(color) / MAXIMUM_RBG_COLOR_VALUE;
        float b1 = Color.blue(color) / MAXIMUM_RBG_COLOR_VALUE;

        float r2;
        float g2;
        float b2;

        float r3;
        float g3;
        float b3;

        if (useGradient) {
            r2 = Color.red(color2) / MAXIMUM_RBG_COLOR_VALUE;
            g2 = Color.green(color2) / MAXIMUM_RBG_COLOR_VALUE;
            b2 = Color.blue(color2) / MAXIMUM_RBG_COLOR_VALUE;

            r3 = Color.red(color3) / MAXIMUM_RBG_COLOR_VALUE;
            g3 = Color.green(color3) / MAXIMUM_RBG_COLOR_VALUE;
            b3 = Color.blue(color3) / MAXIMUM_RBG_COLOR_VALUE;
        } else {
            r2 = r1;
            g2 = g1;
            b2 = b1;

            r3 = r1;
            g3 = g1;
            b3 = b1;
        }

        if (useGradient && colorM != null) {
            float rM = Color.red(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float gM = Color.green(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float bM = Color.blue(colorM) / MAXIMUM_RBG_COLOR_VALUE;

            float centerX = (pos.x + point2.x + point3.x) / 3;
            float centerY = (pos.y + point2.y + point3.y) / 3;

            float[] triangleVertices = {
                    //Order of coordinates x, y, r, g, b, a
                    centerX, centerY,    rM, gM, bM, alpha,
                    pos.x,     pos.y,    r1, g1, b1, alpha,
                    point2.x,  point2.y, r2, g2, b2, alpha,
                    point3.x,  point3.y, r3, g3, b3, alpha,
                    pos.x,     pos.y,    r1, g1, b1, alpha
            };

            vertexArray = new VertexArray(triangleVertices);

        } else {
            float[] triangleVertices = {
                    //Order of coordinates x, y, r, g, b, a
                    pos.x,     pos.y,    r1, g1, b1, alpha,
                    point2.x,  point2.y, r2, g2, b2, alpha,
                    point3.x,  point3.y, r3, g3, b3, alpha,
                    point3.x,  point3.y, r3, g3, b3, alpha,
                    point3.x,  point3.y, r3, g3, b3, alpha

            };
            vertexArray = new VertexArray(triangleVertices);
        }

        regeneratePolygon();
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (visible) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            ProgramManager.useProgram(ProgramManager.colorShaderProgram);
            ProgramManager.setColorShaderUniforms(projectionMatrix);

            bindData(ProgramManager.colorShaderProgram);

            glDrawArrays(GL_TRIANGLE_FAN, 0, 5);

            glDisable(GL_BLEND);
        }
    }



    @Override
    public boolean collidesWithPoint(int x, int y) {
        return isPointInTriangle(new CGPoint(x, y), pos, point2, point3);
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        CGPoint[] rect = new CGPoint[4];
        rect[0] = new CGPoint(x, y);
        rect[1] = new CGPoint(x + w, y);
        rect[2] = new CGPoint(x, y + h);
        rect[3] = new CGPoint(x + w, y + h);
        return toPolygon().collidesWith(new MyPolygon(rect));
    }

    public CGPoint getPoint1() {
        return pos;
    }

    public void setPoint1(CGPoint point1) {
        pos = point1;
        generateVertexArray();
    }

    public CGPoint getPoint2() {
        return point2;
    }

    public void setPoint2(CGPoint point2) {
        this.point2 = point2;
        generateVertexArray();
    }

    public CGPoint getPoint3() {
        return point3;
    }

    public void setPoint3(CGPoint point3) {
        this.point3 = point3;
        generateVertexArray();
    }

    public void setPoints(CGPoint point1, CGPoint point2, CGPoint point3) {
        setPoint1(point1);
        setPoint2(point2);
        setPoint3(point3);
    }

    public void setPoints(int x1, int y1, int x2, int y2, int x3, int y3) {
        setPoint1(new CGPoint(x1, y1));
        setPoint2(new CGPoint(x2, y2));
        setPoint3(new CGPoint(x3, y3));
    }

    @Override
    public CGPoint getPosTopLeft() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        if (pos.getX() < minX) {
            minX = pos.getX();
        }
        if (point2.getX() < minX) {
            minX = point2.getX();
        }
        if (point3.getX() < minX) {
            minX = point3.getX();
        }
        if (pos.getY() < minY) {
            minY = pos.getY();
        }
        if (point2.getY() < minY) {
            minY = point2.getY();
        }
        if (point3.getY() < minY) {
            minY = point3.getY();
        }

        return new CGPoint(minX, minY);
    }

    @Override
    public CGPoint getPosBottomRight() {
        int maxX = 0;
        int maxY = 0;

        if (pos.getX() > maxX) {
            maxX = pos.getX();
        }
        if (point2.getX() > maxX) {
            maxX = point2.getX();
        }
        if (point3.getX() > maxX) {
            maxX = point3.getX();
        }
        if (pos.getY() > maxY) {
            maxY = pos.getY();
        }
        if (point2.getY() > maxY) {
            maxY = point2.getY();
        }
        if (point3.getY() > maxY) {
            maxY = point3.getY();
        }

        return new CGPoint(maxX, maxY);
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
        if (useGradient) {
            generateVertexArray();
        }
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
        if (useGradient) {
            generateVertexArray();
        }
    }

    public void setColors(int color, int color2, int color3, boolean useGradient) {
        setColor(color);
        setColor2(color2);
        setColor3(color3);
        useGradient(useGradient);
    }

    public void setColors(int color, int color2, int color3, Integer colorM, boolean useGradient) {
        setColor(color);
        setColor2(color2);
        setColor3(color3);
        setColorM(colorM);
        useGradient(useGradient);
    }

    public Integer getColorM() {
        return colorM;
    }

    /**
     * Defines the color of the center of the triangle.
     * @param colorM If not null and useGradient then this will be the color of the center of the triangle.
     */
    public void setColorM(Integer colorM) {
        this.colorM = colorM;
    }

    public MyPolygon toPolygon() {
        return polygonSave;
    }

    private void regeneratePolygon() {
        ArrayList<CGPoint> points = new ArrayList<>(3);
        points.add(pos);
        points.add(point2);
        points.add(point3);

        polygonSave = new MyPolygon(points);
    }
}
