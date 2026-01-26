package ldk.productions.example.project.objects;

import ldk.productions.example.project.data.CGPoint;
import ldk.productions.example.project.programs.ColorShaderProgram;

import static ldk.productions.example.project.Constants.BYTES_PER_FLOAT;

/**
 * Created by Leo on 10.12.2015.
 */
public abstract class GL_Colored_Shape extends GL_Shape {

    protected static final int POSITION_COMPONENT_COUNT = 2; // x,y
    protected static final int COLOR_COMPONENT_COUNT = 4;    // r,g,b,a
    protected static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    protected static final float MAXIMUM_RBG_COLOR_VALUE = 255.f;

    protected int color;
    protected float alpha = 1.0f;
    protected boolean useGradient = false;

    public GL_Colored_Shape(CGPoint pos) {
        super(pos);
    }

    public GL_Colored_Shape(int x, int y) {
        super(x, y);
    }

    /**
     * Copy constructor
     * @param obj the GL_Colored_Shape to be copied
     */
    public GL_Colored_Shape(GL_Colored_Shape obj) {
        super(obj, true);
        this.color = obj.color;
        this.alpha = obj.alpha;
        this.useGradient = obj.useGradient;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    @Override
    public void setPosX(int x) {
        super.setPosX(x);
        generateVertexArray();
    }

    @Override
    public void setPosY(int y) {
        super.setPosY(y);
        generateVertexArray();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        generateVertexArray();
    }

    public void useGradient(boolean useGradient) {
        this.useGradient = useGradient;
        generateVertexArray();
    }

    public boolean isGradientUsed() {
        return useGradient;
    }

    public float getAlpha() {
        return alpha;
    }

    /**
     * Sets the transparency of the object.
     * @param alpha Value between 0.0 (total transparency) and 1.0 (not transparent).
     */
    public void setAlpha(float alpha) {
        this.alpha = alpha;
        generateVertexArray();
    }
}
