package ldkproductions.com.example.data;

/**
 * Created by Leo on 17.11.2015.
 */
public class CGPoint {
    public float x;
    public float y;

    public CGPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CGPoint() {
        this.x = 0;
        this.y = 0;
    }

    public int getX(){
        return (int) this.x;
    }

    public int getY(){
        return (int) this.y;
    }

    public void normalize() {
        double len = Math.sqrt(this.x*this.x + this.y*this.y);
        if(len == 0){
            this.x = 0;
            this.y = 0;
        } else {
            this.x /= len;
            this.y /= len;
        }
    }

    public boolean equalToPoint(CGPoint p) {
        return p.x == this.x && p.y == this.y;
    }
}
