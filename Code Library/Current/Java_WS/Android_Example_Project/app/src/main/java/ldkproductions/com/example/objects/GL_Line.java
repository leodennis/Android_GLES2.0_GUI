package ldkproductions.com.example.objects;

import android.graphics.Color;
import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.VertexArray;
import ldkproductions.com.example.programs.ProgramManager;
import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Leo on 05.12.2015.
 */
public class GL_Line extends GL_Colored_Shape {

    protected CGPoint pos2;
    protected int color2;

    protected float strokeWidth;

    protected CGPoint[] points = new CGPoint[4];
    protected MyPolygon polygonSave = null;

    public GL_Line(CGPoint pos, CGPoint pos2) {
        super(pos);
        init(pos2, 1, Color.BLACK);
    }

    public GL_Line(int x1, int y1, int x2, int y2) {
        super(x1, y1);
        init(new CGPoint(x2, y2), 1, Color.BLACK);
    }

    public GL_Line(CGPoint pos, CGPoint pos2, int color) {
        super(pos);
        init(pos2, 1, color);
    }

    public GL_Line(int x1, int y1, int x2, int y2, int color) {
        super(x1, y1);
        init(new CGPoint(x2, y2), 1, color);
    }

    public GL_Line(CGPoint pos, CGPoint pos2, float strokeWidth) {
        super(pos);
        init(pos2, strokeWidth, Color.BLACK);
    }

    public GL_Line(int x1, int y1, int x2, int y2, float strokeWidth) {
        super(x1, y1);
        init(new CGPoint(x2, y2), strokeWidth, Color.BLACK);
    }

    public GL_Line(CGPoint pos, CGPoint pos2, int color, float strokeWidth) {
        super(pos);
        init(pos2, strokeWidth, color);
    }

    public GL_Line(int x1, int y1, int x2, int y2, int color, float strokeWidth) {
        super(x1, y1);
        init(new CGPoint(x2, y2), strokeWidth, color);
    }

    private void init(CGPoint pos2, float strokeWidth, int color) {
        this.pos2 = pos2;
        this.strokeWidth = strokeWidth;
        this.color = color;

        generateVertexArray();
    }

    /**
     * Calculates the vertex array based on 2 points and the stroke width
     * Based on answer of radical7 from http://stackoverflow.com/questions/14514543/opengl-es-2-0-dynamically-change-line-width
     */
    protected void generateVertexArray() {

        CGPoint v = new CGPoint(pos2.x-pos.x, pos2.y-pos.y);
        double v_length = Math.sqrt(v.x*v.x + v.y*v.y);

        // make it a unit vector
        v.x /= v_length;
        v.y /= v_length;

        // compute the vector perpendicular to v
        CGPoint vp = new CGPoint(-v.y, v.x);

        points[0] = new CGPoint(pos.x + strokeWidth/2 * vp.x, pos.y + strokeWidth/2 * vp.y);
        points[1] = new CGPoint(pos.x - strokeWidth/2 * vp.x, pos.y - strokeWidth/2 * vp.y);
        points[2] = new CGPoint(pos2.x + strokeWidth/2 * vp.x, pos2.y + strokeWidth/2 * vp.y);
        points[3] = new CGPoint(pos2.x - strokeWidth/2 * vp.x, pos2.y - strokeWidth/2 * vp.y);

        float r1 = Color.red(color)    / 255.f;
        float g1 = Color.green(color)  / 255.f;
        float b1 = Color.blue(color)   / 255.f;

        float r2;
        float g2;
        float b2;

        if (useGradient) {
            r2 = Color.red(color2)    / 255.f;
            g2 = Color.green(color2)  / 255.f;
            b2 = Color.blue(color2)   / 255.f;
        } else {
            r2 = r1;
            g2 = g1;
            b2 = b1;
        }

        float[] lineVertices = {
                //Order of coordinates x, y, r, g, b, a
                points[0].x,  points[0].y, r1, g1, b1, alpha,
                points[1].x,  points[1].y, r1, g1, b1, alpha,
                points[2].x,  points[2].y, r2, g2, b2, alpha,
                points[3].x,  points[3].y, r2, g2, b2, alpha
        };

        vertexArray = new VertexArray(lineVertices);
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (changed) {
            generateVertexArray();
            changed = false;
        }
        if (visible) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            ProgramManager.useProgram(ProgramManager.colorShaderProgram);
            ProgramManager.setColorShaderUniforms(projectionMatrix);

            bindData(ProgramManager.colorShaderProgram);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            glDisable(GL_BLEND);
        }
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        changed = true;
    }

    public CGPoint getPos2() {
        return pos2;
    }

    public void setPos2(CGPoint pos2) {
        this.pos2 = pos2;
        changed = true;
    }

    @Override
    public CGPoint getPosTopLeft() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        if (pos.getX() < minX)
            minX = pos.getX();
        if (pos2.getX() < minX)
            minX = pos2.getX();
        if (pos.getY() < minY)
            minY = pos.getY();
        if (pos2.getY() < minY)
            minY = pos2.getY();

        return new CGPoint(minX, minY);
    }

    @Override
    public CGPoint getPosBottomRight() {
        int maxX = 0;
        int maxY = 0;

        if (pos.getX() > maxX)
            maxX = pos.getX();
        if (pos2.getX() > maxX)
            maxX = pos2.getX();
        if (pos.getY() > maxY)
            maxY = pos.getY();
        if (pos2.getY() > maxY)
            maxY = pos2.getY();

        return new CGPoint(maxX, maxY);
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
        if (useGradient)
            changed = true;
    }

    public void setColors(int color, int color2, boolean useGradient) {
        setColor(color);
        setColor2(color2);
        useGradient(useGradient);
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        return pointRectangleCollision(new CGPoint(x, y), points[0], points[1], points[2]);
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

    public MyPolygon toPolygon() {
        if (polygonSave == null || changed) {
            polygonSave = new MyPolygon(points);
        }
        return polygonSave;
    }
}
