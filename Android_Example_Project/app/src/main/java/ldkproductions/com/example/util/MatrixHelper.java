package ldkproductions.com.example.util;

public class MatrixHelper {
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect,
        float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;
        
        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;        
    }

// -----------------------


    private static final double PI = 3.1415926535897932384626433832795;
    private static final double PI_OVER_180 = 0.017453292519943295769236907684886;
    private static final double PI_OVER_360 = 0.0087266462599716478846184538424431;

    public static float[] gldMultMatrix(float[] matrixB,float[] matrixA) {
        float[] newMatrix = new float[16];

        for(int i = 0; i < 4; i++){ //Cycle through each vector of first matrix.
            newMatrix[i*4] = matrixA[i*4] * matrixB[0] + matrixA[i*4+1] * matrixB[4] + matrixA[i*4+2] * matrixB[8] + matrixA[i*4+3] * matrixB[12];
            newMatrix[i*4+1] = matrixA[i*4] * matrixB[1] + matrixA[i*4+1] * matrixB[5] + matrixA[i*4+2] * matrixB[9] + matrixA[i*4+3] * matrixB[13];
            newMatrix[i*4+2] = matrixA[i*4] * matrixB[2] + matrixA[i*4+1] * matrixB[6] + matrixA[i*4+2] * matrixB[10] + matrixA[i*4+3] * matrixB[14];
            newMatrix[i*4+3] = matrixA[i*4] * matrixB[3] + matrixA[i*4+1] * matrixB[7] + matrixA[i*4+2] * matrixB[11] + matrixA[i*4+3] * matrixB[15];
        }
        /*this should combine the matrixes*/

        return newMatrix;
    }

    public  static float[] gldTranslatef(float[] m,float x,float y) {
        float[] m2 = new float[16];

        m2[0] = 1;
        m2[1] = 0;
        m2[2] = 0;
        m2[3] = 0;

        m2[4] = 0;
        m2[5] = 1;
        m2[6] = 0;
        m2[7] = 0;

        m2[8] = 0;
        m2[9] = 0;
        m2[10] = 1;
        m2[11] = 0;

        m2[12] = x;
        m2[13] = y;
        m2[14] = 0;
        m2[15] = 1;

        return gldMultMatrix(m,m2);
    }

    public static float[] gldRotatef(float[] m, float a, float x,float y) {
        int z = 0;
        float angle=(float) (a*PI_OVER_180);
        float[] m2 = new float[16];

        m2[0] = (float) (1+(1-Math.cos(angle))*(x*x-1));
        m2[1] = (float) (-z*Math.sin(angle)+(1-Math.cos(angle))*x*y);
        m2[2] = (float) (y*Math.sin(angle)+(1-Math.cos(angle))*x*z);
        m2[3] = 0;

        m2[4] = (float) (z*Math.sin(angle)+(1-Math.cos(angle))*x*y);
        m2[5] = (float) (1+(1-Math.cos(angle))*(y*y-1));
        m2[6] = (float) (-x*Math.sin(angle)+(1-Math.cos(angle))*y*z);
        m2[7] = 0;

        m2[8] = (float) (-y*Math.sin(angle)+(1-Math.cos(angle))*x*z);
        m2[9] = (float) (x*Math.sin(angle)+(1-Math.cos(angle))*y*z);
        m2[10] = (float) (1+(1-Math.cos(angle))*(z*z-1));
        m2[11] = 0;

        m2[12] = 0;
        m2[13] = 0;
        m2[14] = 0;
        m2[15] = 1;

        return gldMultMatrix(m,m2);
    }
}

