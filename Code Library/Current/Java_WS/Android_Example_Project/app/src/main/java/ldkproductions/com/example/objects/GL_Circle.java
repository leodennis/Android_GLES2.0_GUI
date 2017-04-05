package ldkproductions.com.example.objects;

import android.graphics.Color;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.VertexArray;
import ldkproductions.com.example.programs.ProgramManager;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Leo on 07.01.2016.
 */
public class GL_Circle extends GL_Colored_Shape {

    private int radius;
    private int numberOfSegments;

    private int colorEdge;

    public GL_Circle(CGPoint pos, int r) {
        super(pos);
        init(r, Color.BLACK);
    }

    public GL_Circle(CGPoint pos, int r, int color) {
        super(pos);
        init(r, color);
    }

    public GL_Circle(int x, int y, int r) {
        super(x, y);
        init(r, Color.BLACK);
    }

    public GL_Circle(int x, int y, int r, int color) {
        super(x, y);
        init(r, color);
    }

    private void init(int r, int color) {
        this.radius = r;
        this.color = color;

        generateVertexArray();
    }

    /**
     * Based on method to approximate circle found on http://slabode.exofire.net/circle_draw.shtml by SiegeLord's
     */
    @Override
    protected void generateVertexArray() {
        int num_segments = getNumCircleSegments();
        double theta = 2 * 3.1415926 / num_segments;
        double tangetial_factor = Math.tan(theta);//calculate the tangential factor

        double radial_factor = Math.cos(theta);//calculate the radial factor

        double x = radius;//we start at angle = 0

        double y = 0;

        float r1 = Color.red(color)    / 255.f;
        float g1 = Color.green(color)  / 255.f;
        float b1 = Color.blue(color)   / 255.f;

        float r2, g2, b2;
        if (useGradient) {
            r2 = Color.red(colorEdge)    / 255.f;
            g2 = Color.green(colorEdge)  / 255.f;
            b2 = Color.blue(colorEdge)   / 255.f;
        } else {
            r2 = r1;
            g2 = g1;
            b2 = b1;
        }

        float[] circleVertices = new float[(num_segments+2)*6];

        circleVertices[0] = pos.x;
        circleVertices[1] = pos.y;
        circleVertices[2] = r1;
        circleVertices[3] = g1;
        circleVertices[4] = b1;
        circleVertices[5] = alpha;

        //glBegin(GL_LINE_LOOP);
        for(int ii = 6; ii < circleVertices.length-7; ii+=6)
        {
            circleVertices[ii] = (float) (x + pos.x);
            circleVertices[ii+1] = (float) (y + pos.y);
            circleVertices[ii+2] = r2;
            circleVertices[ii+3] = g2;
            circleVertices[ii+4] = b2;
            circleVertices[ii+5] = alpha;

            if (ii == 6) {
                circleVertices[circleVertices.length-6] = (float) (x + pos.x);
                circleVertices[circleVertices.length-5] = (float) (y + pos.y);
                circleVertices[circleVertices.length-4] = r2;
                circleVertices[circleVertices.length-3] = g2;
                circleVertices[circleVertices.length-2] = b2;
                circleVertices[circleVertices.length-1] = alpha;
            }


            //calculate the tangential vector
            //remember, the radial vector is (x, y)
            //to get the tangential vector we flip those coordinates and negate one of them

            double tx = -y;
            double ty = x;

            //add the tangential vector

            x += tx * tangetial_factor;
            y += ty * tangetial_factor;

            //correct using the radial factor

            x *= radial_factor;
            y *= radial_factor;
        }

        vertexArray = new VertexArray(circleVertices);
    }

    private int getNumCircleSegments()  {
        return numberOfSegments = (int) (100 * Math.sqrt(this.radius)); //change the 10 to a smaller/bigger number as needed
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

            glDrawArrays(GL_TRIANGLE_FAN, 0, numberOfSegments+2);

            glDisable(GL_BLEND);
        }
    }

    @Override
    public CGPoint getPosTopLeft() {
        return new CGPoint(pos.getX() - radius, pos.getY() - radius);
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(pos.getX() + radius, pos.getY() + radius);
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        return GL_Shape.isPointInCircle(x, y, pos.getX(), pos.getY(), radius);
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return GL_Shape.alignedRectangleCircleCollision(x, y, w, h, pos.getX(), pos.getY(), radius);
    }

    private void setColorEdge(int colorEdge) {
        this.colorEdge = colorEdge;
        if (useGradient)
            changed = true;
    }

    public void setColors(int colorMiddle, int colorEdge, boolean useGradient) {
        setColor(colorMiddle);
        setColorEdge(colorEdge);
        useGradient(useGradient);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int r) {
        this.radius = r;
    }

    private boolean collidesWithTiltedRectangle(CGPoint c1, CGPoint c2, CGPoint c3, CGPoint c4) {



        return true;
    }
}
