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
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Leo on 07.12.2015.
 */
public class GL_Rectangle extends GL_Colored_Shape {

    // virtual size
    protected int width;
    protected int height;

    protected int colorTR;
    protected int colorBL;
    protected int colorBR;
    protected Integer colorM = null;
    protected boolean middle_x_gradient = true;
    protected boolean middle_y_gradient = true;

    public GL_Rectangle(int x, int y, int width, int height) {
        super(x, y);
        init(width, height, Color.BLACK);
    }

    public GL_Rectangle(int x, int y, int width, int height, int color) {
        super(x, y);
        init(width, height, color);
    }

    public GL_Rectangle(CGPoint pos, int width, int height) {
        super(pos);
        init(width, height, Color.BLACK);
    }

    public GL_Rectangle(CGPoint pos, int width, int height, int color) {
        super(pos);
        init(width, height, color);
    }

    /**
     * Copy constructor
     * @param obj the GL_Rectangle to be copied
     */
    public GL_Rectangle(GL_Rectangle obj) {
        super(obj);
        this.width = obj.width;
        this.height = obj.height;
        this.colorTR = obj.colorTR;
        this.colorBL = obj.colorBL;
        this.colorBR = obj.colorBR;
        this.colorM = obj.colorM;
        this.middle_x_gradient = obj.middle_x_gradient;
        this.middle_y_gradient = obj.middle_y_gradient;
    }

    @Override
    public Object clone() {
        return new GL_Rectangle(this);
    }

    private void init(int width, int height, int color) {
        this.height = height;
        this.width = width;
        this.color = color;

        generateVertexArray();
    }

    @Override
    protected void generateVertexArray() {
        float r1 = Color.red(color) / MAXIMUM_RBG_COLOR_VALUE;
        float g1 = Color.green(color) / MAXIMUM_RBG_COLOR_VALUE;
        float b1 = Color.blue(color) / MAXIMUM_RBG_COLOR_VALUE;

        float r2, r3, r4;
        float g2, g3, g4;
        float b2, b3, b4;

        if (useGradient) {
            r2 = Color.red(colorTR) / MAXIMUM_RBG_COLOR_VALUE;
            g2 = Color.green(colorTR) / MAXIMUM_RBG_COLOR_VALUE;
            b2 = Color.blue(colorTR) / MAXIMUM_RBG_COLOR_VALUE;
            r3 = Color.red(colorBL) / MAXIMUM_RBG_COLOR_VALUE;
            g3 = Color.green(colorBL) / MAXIMUM_RBG_COLOR_VALUE;
            b3 = Color.blue(colorBL) / MAXIMUM_RBG_COLOR_VALUE;
            r4 = Color.red(colorBR) / MAXIMUM_RBG_COLOR_VALUE;
            g4 = Color.green(colorBR) / MAXIMUM_RBG_COLOR_VALUE;
            b4 = Color.blue(colorBR) / MAXIMUM_RBG_COLOR_VALUE;

        } else {
            r4 = r3 = r2 = r1;
            g4 = g3 = g2 = g1;
            b4 = b3 = b2 = b1;
        }

        if (useGradient && colorM != null && middle_x_gradient && middle_y_gradient) {

            float rM = Color.red(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float gM = Color.green(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float bM = Color.blue(colorM) / MAXIMUM_RBG_COLOR_VALUE;

            if (width == height) { //divide in 4 triangles
                CGPoint pM = new CGPoint(pos.x+width/2.0f, pos.y+height/2.0f);

                float[] triangleVertices = {
                        //Order of coordinates x, y, r, g, b, a

                        pM.x,         pM.y,            rM, gM, bM, alpha,
                        pos.x,        pos.y+height,    r3, g3, b3, alpha,
                        pos.x+width,  pos.y+height,    r4, g4, b4, alpha,
                        pos.x+width,  pos.y,           r2, g2, b2, alpha,
                        pos.x,        pos.y,           r1, g1, b1, alpha,
                        pos.x,        pos.y+height,    r3, g3, b3, alpha
                };
                vertexArray = new VertexArray(triangleVertices);

            } else if (width > height){ //divide in 8 triangles
                CGPoint pML = new CGPoint(pos.x+height/2.0f, pos.y+height/2.0f);
                CGPoint pMR = new CGPoint(pos.x+width-height/2.0f, pos.y+height/2.0f);
                CGPoint pMT = new CGPoint(pos.x+width/2.0f, pos.y);
                CGPoint pMB = new CGPoint(pos.x+width/2.0f, pos.y+height);

                float rMT = (r1+r2)/2.f;
                float gMT = (g1+g2)/2.f;
                float bMT = (b1+b2)/2.f;

                float rMB = (r3+r4)/2.f;
                float gMB = (g3+g4)/2.f;
                float bMB = (b3+b4)/2.f;

                float[] triangleVertices = {
                        //Order of coordinates x, y, r, g, b, a
                        pos.x,        pos.y+height,    r3,  g3,  b3,  alpha, //BL
                        pML.x,        pML.y,           rM,  gM,  bM,  alpha, //ML
                        pMB.x,        pMB.y,           rMB, gMB, bMB, alpha, //MB
                        pMR.x,        pMR.y,           rM,  gM,  bM,  alpha, //MR
                        pos.x+width,  pos.y+height,    r4,  g4,  b4,  alpha, //BR
                        pos.x+width,  pos.y,           r2,  g2,  b2,  alpha, //TR

                        pos.x+width,  pos.y,           r2,  g2,  b2,  alpha, //TR
                        pMR.x,        pMR.y,           rM,  gM,  bM,  alpha, //MR
                        pMT.x,        pMT.y,           rMT, gMT, bMT, alpha, //MT
                        pML.x,        pML.y,           rM,  gM,  bM,  alpha, //ML
                        pos.x,        pos.y,           r1,  g1,  b1,  alpha, //TL
                        pos.x,        pos.y+height,    r3,  g3,  b3,  alpha, //BL

                };

                vertexArray = new VertexArray(triangleVertices);

            } else { //divide in 8 triangles
                CGPoint pMT = new CGPoint(pos.x+width/2.0f, pos.y+width/2.0f);
                CGPoint pMB = new CGPoint(pos.x+width/2.0f, pos.y+height-width/2.0f);
                CGPoint pML = new CGPoint(pos.x, pos.y+height/2.0f);
                CGPoint pMR = new CGPoint(pos.x+width, pos.y+height/2.0f);

                float rMR = (r2+r4)/2.f;
                float gMR = (g2+g4)/2.f;
                float bMR = (b2+b4)/2.f;

                float rML = (r1+r3)/2.f;
                float gML = (g1+g3)/2.f;
                float bML = (b1+b3)/2.f;

                float[] triangleVertices = {
                        //Order of coordinates x, y, r, g, b, a
                        pos.x,        pos.y+height,    r3,  g3,  b3,  alpha, //BL
                        pos.x+width,  pos.y+height,    r4,  g4,  b4,  alpha, //BR
                        pMB.x,        pMB.y,           rM,  gM,  bM,  alpha, //MB
                        pMR.x,        pMR.y,           rMR, gMR, bMR, alpha, //MR
                        pMT.x,        pMT.y,           rM,  gM,  bM,  alpha, //MT
                        pos.x+width,  pos.y,           r2,  g2,  b2,  alpha, //TR


                        pos.x+width,  pos.y,           r2,  g2,  b2,  alpha, //TR
                        pos.x,        pos.y,           r1,  g1,  b1,  alpha, //TL
                        pMT.x,        pMT.y,           rM,  gM,  bM,  alpha, //MT
                        pML.x,        pML.y,           rML, gML, bML, alpha, //ML
                        pMB.x,        pMB.y,           rM,  gM,  bM,  alpha, //MB
                        pos.x,        pos.y+height,    r3,  g3,  b3,  alpha  //BL
                };

                vertexArray = new VertexArray(triangleVertices);
            }
        } else if(useGradient && colorM != null && (middle_x_gradient || middle_y_gradient)) {
            CGPoint pM = new CGPoint(pos.x+width/2.0f, pos.y+height/2.0f);
            CGPoint pMT = new CGPoint(pos.x+width/2.0f, pos.y);
            CGPoint pMB = new CGPoint(pos.x+width/2.0f, pos.y+height);
            CGPoint pML = new CGPoint(pos.x, pos.y+height/2.0f);
            CGPoint pMR = new CGPoint(pos.x+width, pos.y+height/2.0f);
            float rMT, rMB, rML, rMR;
            float gMT, gMB, gML, gMR;
            float bMT, bMB, bML, bMR;

            float rM = Color.red(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float gM = Color.green(colorM) / MAXIMUM_RBG_COLOR_VALUE;
            float bM = Color.blue(colorM) / MAXIMUM_RBG_COLOR_VALUE;

            if (middle_x_gradient) {
                rMT = rMB = rM;
                gMT = gMB = gM;
                bMT = bMB = bM;

                rMR = (r2+r4)/2.f;
                gMR = (g2+g4)/2.f;
                bMR = (b2+b4)/2.f;

                rML = (r1+r3)/2.f;
                gML = (g1+g3)/2.f;
                bML = (b1+b3)/2.f;
            } else {
                rML = rMR = rM;
                gML = gMR = gM;
                bML = bMR = bM;

                rMT = (r1+r2)/2.f;
                gMT = (g1+g2)/2.f;
                bMT = (b1+b2)/2.f;

                rMB = (r3+r4)/2.f;
                gMB = (g3+g4)/2.f;
                bMB = (b3+b4)/2.f;
            }

            float[] triangleVertices = {
                    //Order of coordinates x, y, r, g, b, a

                    pM.x,         pM.y,            rM,  gM,  bM,  alpha, //M
                    pos.x,        pos.y+height,    r3,  g3,  b3,  alpha, //BL
                    pMB.x,        pMB.y,           rMB, gMB, bMB, alpha, //MB
                    pos.x+width,  pos.y+height,    r4,  g4,  b4,  alpha, //BR
                    pMR.x,        pMR.y,           rMR, gMR, bMR, alpha, //MR
                    pos.x+width,  pos.y,           r2,  g2,  b2,  alpha, //TR
                    pMT.x,        pMT.y,           rMT, gMT, bMT, alpha, //MT
                    pos.x,        pos.y,           r1,  g1,  b1,  alpha, //TL
                    pML.x,        pML.y,           rML, gML, bML, alpha, //ML
                    pos.x,        pos.y+height,    r3,  g3,  b3,  alpha  //BL
            };
            vertexArray = new VertexArray(triangleVertices);

        } else {
            float[] triangleVertices = {
                    //Order of coordinates x, y, r, g, b, a
                    pos.x,         pos.y+height,    r3, g3, b3, alpha,
                    pos.x+width,   pos.y+height,    r4, g4, b4, alpha,
                    pos.x,         pos.y,           r1, g1, b1, alpha,
                    pos.x + width, pos.y,           r2, g2, b2, alpha
            };

            vertexArray = new VertexArray(triangleVertices);
        }
    }

    @Override
    public void draw(float[] projectionMatrix) {
        if (visible) {
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            ProgramManager.useProgram(ProgramManager.colorShaderProgram);
            ProgramManager.setColorShaderUniforms(projectionMatrix);

            bindData(ProgramManager.colorShaderProgram);

            if (vertexArray.getLength() > 61) {
                glDrawArrays(GL_TRIANGLE_STRIP, 0, 6);
                glDrawArrays(GL_TRIANGLE_STRIP, 6, 6);
            } else if (vertexArray.getLength() > 37) {
                glDrawArrays(GL_TRIANGLE_FAN, 0, 10);
            } else if(vertexArray.getLength() > 25) {
                glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
            } else {
                glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
            }

            glDisable(GL_BLEND);
        }
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        return ((x >= pos.getX() && x <= pos.getX()+width) && (y>= pos.getY() && y <= pos.getY()+height));
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return (pos.x <= x + w &&
                pos.x + width > x &&
                pos.y <= y + h &&
                height + pos.y > y);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        generateVertexArray();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        generateVertexArray();
    }

    @Override
    public CGPoint getPosTopLeft() {
        return pos;
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(pos.x + width, pos.y + height);
    }

    public int getColorTR() {
        return colorTR;
    }

    public void setColorTR(int colorTR) {
        this.colorTR = colorTR;
        if (useGradient) {
            generateVertexArray();
        }
    }

    public int getColorBL() {
        return colorBL;
    }

    public void setColorBL(int colorBL) {
        this.colorBL = colorBL;
        if (useGradient) {
            generateVertexArray();
        }
    }

    public int getColorBR() {
        return colorBR;
    }

    public void setColorBR(int colorBR) {
        this.colorBR = colorBR;
        if (useGradient) {
            generateVertexArray();
        }
    }

    public void setColors(int color, int colorTR, int colorBL, int colorBR, boolean useGradient) {
        setColor(color);
        setColorTR(colorTR);
        setColorBL(colorBL);
        setColorBR(colorBR);
        useGradient(useGradient);
    }

    public void setColors(int color, int colorTR, int colorBL, int colorBR, Integer colorM, boolean useGradient) {
        setColors(color, colorTR, colorBL, colorBR, colorM, true, true, useGradient);
    }

    public void setColors(int color, int colorTR, int colorBL, int colorBR, Integer colorM, boolean x_gradient, boolean y_gradient, boolean useGradient) {
        setColor(color);
        setColorTR(colorTR);
        setColorBL(colorBL);
        setColorBR(colorBR);
        setColorM(colorM, x_gradient, y_gradient);
        useGradient(useGradient);
    }

    public Integer getColorM() {
        return colorM;
    }

    /**
     * Defines the color of the center of the rectangle.
     * @param colorM If not null and useGradient then this will be the color of the center of the rectangle.
     */
    public void setColorM(Integer colorM) {
        this.colorM = colorM;
        this.middle_x_gradient = true;
        this.middle_y_gradient = true;
    }

    /**
     * Defines the color of the center of the rectangle.
     * @param colorM If not null and useGradient then this will be the color of the center of the rectangle.
     * @param x_gradient If true the color will affect the x-gradient. True if not specified.
     * @param y_gradient If true the color will affect the y-gradient. True if not specified.
     */
    public void setColorM(Integer colorM, boolean x_gradient, boolean y_gradient) {
        this.colorM = colorM;
        this.middle_x_gradient = x_gradient;
        this.middle_y_gradient = y_gradient;
    }

    public MyPolygon toPolygon() {
        ArrayList<CGPoint> points = new ArrayList<CGPoint>(4);
        points.add(pos);
        points.add(new CGPoint(pos.x, pos.y + height));
        points.add(new CGPoint(pos.x + width, pos.y));
        points.add(new CGPoint(pos.x + width, pos.y + height));
        return new MyPolygon(points);
    }
}
