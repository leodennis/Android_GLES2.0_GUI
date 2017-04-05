package ldkproductions.com.example.data;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static ldkproductions.com.example.util.Constants.BYTES_PER_FLOAT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VertexArray {    
    private final FloatBuffer floatBuffer;
    private int length;

    public VertexArray(float[] vertexData) {
        floatBuffer = ByteBuffer
            .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData);
        this.length = vertexData.length;
    }
        
    public void setVertexAttribPointer(int dataOffset, int attributeLocation,
        int componentCount, int stride) {        
        floatBuffer.position(dataOffset);        
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, 
            false, stride, floatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        
        floatBuffer.position(0);
    }

    public int getLength() {
        return length;
    }
}
