package ldkproductions.com.example.objects;

import android.content.Context;
import android.opengl.GLES20;
import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.VertexArray;
import ldkproductions.com.example.programs.ProgramManager;
import ldkproductions.com.example.programs.TextureShaderProgram;
import ldkproductions.com.example.util.PowOfTwoTex;
import ldkproductions.com.example.util.TextureHelper;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;
import static ldkproductions.com.example.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by Leo on 12.11.2015.
 */
public class GL_Texture extends GL_Shape {

    protected static final int POSITION_COMPONENT_COUNT = 2;
    protected static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    protected static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    protected int texture;

    //Virtual size
    protected int width;
    protected int height;

    //Extended pow2 size
    protected int pow2width;
    protected int pow2height;

    //Original picture size
    protected int originalWidth;
    protected int originalHeight;

    /**
     * Copy constructor
     * @param texture the texture to be copied
     */
    public GL_Texture(GL_Texture texture) {
        super(texture, true);
        this.width = texture.width;
        this.height = texture.height;
        this.pow2width = texture.pow2width;
        this.pow2height = texture.pow2height;
        this.originalWidth = texture.originalWidth;
        this.originalHeight = texture.originalHeight;
        this.texture = texture.texture;
    }

    public GL_Texture(Context context, int x, int y, int w, int h, int resourceId) {
        super(x, y);
        initTexture(context, w, h, resourceId);
    }

    protected GL_Texture(int x, int y) {
        super(x, y);
    }

    protected void initTexture(Context context, int w, int h, int resourceId) {
        PowOfTwoTex tex = TextureHelper.loadTexture(context, resourceId);

        this.texture = tex.getId();

        this.width = w;
        this.height = h;

        this.pow2width = tex.getPow2Width();
        this.pow2height = tex.getPow2Height();

        this.originalWidth = tex.getRealWidth();
        this.originalHeight = tex.getRealHeight();

        generateVertexArray();
    }

    public void loadNewTexture(Context context, int w, int h, int resourceId) {
        initTexture(context, w, h, resourceId);
    }

    protected void generateVertexArray() {
        float[] vertex_data = {
                // Order of coordinates: X, Y, S, T

                // Triangles

                0,         height,  0,                                 ((float) originalHeight)/pow2height,  //bottom left
                width,     height,  ((float) originalWidth)/pow2width, ((float) originalHeight)/pow2height,  //bottom right
                0,         0,       0,                                 0,                                   //top left
                width,     0,       ((float) originalWidth)/pow2width, 0,                                   //top right
        };

        vertexArray = new VertexArray(vertex_data);
    }

    public int getTexture() {
        return texture;
    }


    public int getMiddleX() {
        return pos.getX() + width/2;
    }

    public int getMiddleY() {
        return pos.getY() + height/2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public CGPoint getPosTopLeft() {
        return pos;
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(pos.x + width, pos.y + height);
    }

    protected boolean isPointInTexture(int x, int y) {
        return ((x >= pos.getX() && x <= pos.getX()+width) && (y>= pos.getY() && y <= pos.getY()+height));
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        return isPointInTexture(x, y);
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        return (pos.x <= x + w &&
                pos.x + width > x &&
                pos.y <= y + h &&
                height + pos.y > y);
    }

    public boolean hasBeenClicked() {
        return (clickState == CLICK_STATE_YES);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        bindData(vertexArray, textureProgram);
    }

    public void bindData(VertexArray vertArray, TextureShaderProgram textureProgram) {
        vertArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw(float[] projectionMatrix) {
        if(visible) {
            //Activate Alpha
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

            //Position matrix
            float[] p_matix = {
             //Order x,      y,      z,      w
                    1.f,    0.f,    0.f,    0.f ,
                    0.f,    1.f,    0.f,    0.f ,
                    0.f,    0.f,    1.f,    0.f,
                    pos.x,  pos.y,  0.f,    1.f
            };

            prepareToDraw(p_matix, projectionMatrix);

            glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

            GLES20.glDisable(GLES20.GL_BLEND); //Deactivate Alpha
        }
    }

    protected void prepareToDraw(float[] p_matix, float[] projectionMatrixFloats) {
        ProgramManager.useProgram(ProgramManager.textureShaderProgram);
        ProgramManager.setTextureShaderUniforms(projectionMatrixFloats, p_matix, texture);
        bindData(ProgramManager.textureShaderProgram);
    }

    protected void prepareToDraw(float[] p_matix, VertexArray vertArr, float[] projectionMatrix) {
        ProgramManager.useProgram(ProgramManager.textureShaderProgram);
        ProgramManager.setTextureShaderUniforms(projectionMatrix, p_matix, texture);
        bindData(vertArr, ProgramManager.textureShaderProgram);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
        generateVertexArray();
    }

}
