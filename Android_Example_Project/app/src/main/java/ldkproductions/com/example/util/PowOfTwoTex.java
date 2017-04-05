package ldkproductions.com.example.util;

/**
 * Created by Leo on 30.11.2015.
 */
public class PowOfTwoTex {
    int texId;
    int realWidth;
    int realHeight;
    int pow2Width;
    int pow2Height;

    public PowOfTwoTex () {
        init(0, 0, 0, 0, 0);
    }

    public PowOfTwoTex (int texId, int realWidth, int realHeight, int pow2Width, int pow2Height) {
        init(texId, realWidth, realHeight, pow2Width, pow2Height);
    }

    private void init(int texId, int realWidth, int realHeight, int pow2Width, int pow2Height) {
        this.texId = texId;
        this.realWidth = realWidth;
        this.realHeight = realHeight;
        this.pow2Width = pow2Width;
        this.pow2Height = pow2Height;
    }

    public void setId(int texId) {
        this.texId = texId;
    }

    public void setRealWidth(int realWidth) {
        this.realWidth = realWidth;
    }

    public void setRealHeight(int realHeight) {
        this.realHeight = realHeight;
    }

    public void setRealDimensions(int realWidth, int realHeight) {
        this.realWidth = realWidth;
        this.realHeight = realHeight;
    }

    public void setPow2Width(int pow2Width) {
        this.pow2Width = pow2Width;
    }

    public void setPow2Height(int pow2Height) {
        this.pow2Height = pow2Height;
    }

    public void setPow2Dimensions(int pow2Width, int pow2Height) {
        this.pow2Width = pow2Width;
        this.pow2Height = pow2Height;
    }

    public int getId() {
        return texId;
    }

    public int getRealWidth() {
        return realWidth;
    }

    public int getRealHeight() {
        return realHeight;
    }

    public int getPow2Width() {
        return pow2Width;
    }

    public int getPow2Height() {
        return pow2Height;
    }
}
