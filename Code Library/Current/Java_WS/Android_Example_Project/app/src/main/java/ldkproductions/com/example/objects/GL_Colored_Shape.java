package ldkproductions.com.example.objects;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.programs.ColorShaderProgram;

import static ldkproductions.com.example.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Leo on 10.12.2015.
 */
public abstract class GL_Colored_Shape extends GL_Shape {

    protected static final int POSITION_COMPONENT_COUNT = 2; //x,y
    protected static final int COLOR_COMPONENT_COUNT = 4;    //r,g,b,a
    protected static final int STRIDE = (POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    protected int color;
    protected float alpha = 1.0f;
    protected boolean useGradient = false;

    protected boolean changed = false;

    public GL_Colored_Shape(CGPoint pos) {
        super(pos);
    }

    public GL_Colored_Shape(int x, int y) {
        super(x, y);
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
        changed = true;
    }

    @Override
    public void setPosY(int y) {
        super.setPosY(y);
        changed = true;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        changed = true;
    }

    public void useGradient(boolean useGradient) {
        this.useGradient = useGradient;
        this.changed = true;
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
        changed = true;
    }
}
