package ldkproductions.com.example.data;

/**
 * Vector2D class from http://rocketmandevelopment.com/blog/separation-of-axis-theorem-for-collision-detection/
 * file: http://rocketmandevelopment.com/static/otherfiles/2010/03/Vector2D.as
 * Adapted to Java by Leo on 21.12.2015.
 */
public class Vector2D {

    public double x;
    public double y;

    /**
     * Default Constructor
     */
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates an exact copy of this Vector2D
     * @return Vector2D A copy of this Vector2D
     */
    public Vector2D cloneVector() {
        return new Vector2D(this.x, this.y);
    }

    /**
     * Makes x and y zero.
     * @return Vector2D This vector.
     */
    public Vector2D zeroVector() {
        this.x = 0;
        this.y = 0;
        return this;
    }

    /**
     * Is this vector zeroed?
     * @return Boolean Returns true if zeroed, else returns false.
     */
    public boolean isZero() {
        return (this.x == 0 && this.y == 0);
    }

    /**
     * Is the vector's length = 1?
     * @return Boolean If length is 1, true, else false.
     */
    public boolean isNormalized() {
        return getLength() == 1.0;
    }

    /**
     * Does this vector have the same location as another?
     * @param vector2 The vector to test.
     * @return Boolean True if equal, false if not.
     */
    public boolean equals(Vector2D vector2) {
        return (this.x == vector2.x && this.y == vector2.y);
    }

    /**
     * Sets the length which will change x and y, but not the angle.
     */
    public void setLength(double value) {
        double angle = getAngle();
        this.x = Math.cos(angle) * value;
        this.y = Math.sin(angle) * value;
        if(Math.abs(this.x) < 0.00000001) this.x = 0;
        if(Math.abs(this.y) < 0.00000001) this.y = 0;
    }
    /**
     * Returns the length of the vector.
     **/
    public double getLength() {
        return Math.sqrt(getLengthSquared());
    }

    /**
     * Returns the length of this vector, before square root. Allows for a faster check.
     */
    public double getLengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    /**
     * Changes the angle of the vector. X and Y will change, length stays the same.
     */
    public void setAngle(double value) {
        double len = getLength();
        this.x = Math.cos(value) * len;
        this.y = Math.sin(value) * len;
    }
    /**
     * Get the angle of this vector.
     **/
    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }

    /**
     * Sets the vector's length to 1.
     * @return Vector2D This vector.
     */
    public Vector2D normalize() {
        if(getLength() == 0){
            this.x = 0;
            this.y = 0;
            return this;
        }
        double len = getLength();
        this.x /= len;
        this.y /= len;
        return this;
    }

    /**
     * Sets the vector's length to len.
     * @param len The length to set it to.
     * @return Vector2D This vector.
     */
    public Vector2D normalcate(double len) {
        setLength(len);
        return this;
    }

    /**
     * Sets the length under the given value. Nothing is done if the vector is already shorter.
     * @param max The max length this vector can be.
     * @return Vector2D This vector.
     */
    public Vector2D truncate(double max) {
        setLength(Math.min(max, getLength()));
        return this;
    }

    /**
     * Makes the vector face the opposite way.
     * @return Vector2D This vector.
     */
    public Vector2D reverse() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    /**
     * Calculate the dot product of this vector and another.
     * @param vector2 Another vector2D.
     * @return double The dot product.
     */
    public double dotProduct(Vector2D vector2) {
        return this.x * vector2.x + this.y * vector2.y;
    }

    /**
     * Calculate the cross product of this and another vector.
     * @param vector2 Another Vector2D.
     * @return double The cross product.
     */
    public double crossProd(Vector2D vector2) {
        return this.x * vector2.y - this.y * vector2.x;
    }

    /**
     * Calculate angle between any two vectors.
     * @param vector1 First vector2d.
     * @param vector2 Second vector2d.
     * @return double Angle between vectors.
     */
    public static double angleBetween(Vector2D vector1, Vector2D vector2) {
        if(!vector1.isNormalized()) vector1 = vector1.cloneVector().normalize();
        if(!vector2.isNormalized()) vector2 = vector2.cloneVector().normalize();
        return Math.acos(vector1.dotProduct(vector2));
    }

    /**
     * Is the vector to the right or left of this one?
     * @param vector2 The vector to test.
     * @return Boolean If left, returns true, if right, false.
     */
    public boolean sign(Vector2D vector2) {
        return getPerpendicular().dotProduct(vector2) < 0 ? false : true;
    }
    /* With int
    public int sign(Vector2D vector2) {
        return getPerpendicular().dotProduct(vector2) < 0 ? -1 : 1;
    } */

    /**
     * Get the vector that is perpendicular.
     * @return Vector2D The perpendicular vector.
     */
    public Vector2D getPerpendicular() {
        return new Vector2D(-y, x);
    }

    /**
     * Calculate between two vectors.
     * @param vector2 The vector to find distance.
     * @return double The distance.
     */
    public double distance(Vector2D vector2) {
        return Math.sqrt(distSQ(vector2));
    }

    /**
     * Calculate squared distance between vectors. Faster than distance.
     * @param vector2 The other vector.
     * @return double The squared distance between the vectors.
     */
    public double distSQ(Vector2D vector2) {
        double dx = vector2.x - x;
        double dy = vector2.y - y;
        return dx * dx + dy * dy;
    }

    /**
     * Add a vector to this vector.
     * @param vector2 The vector to add to this one.
     * @return Vector2D This vector.
     */
    public Vector2D add(Vector2D vector2) {
        this.x += vector2.x;
        this.y += vector2.y;
        return this;
    }

    /**
     * Subtract a vector from this one.
     * @param vector2 The vector to subtract.
     * @return Vector2D This vector.
     */
    public Vector2D subtract(Vector2D vector2) {
        this.x -= vector2.x;
        this.y -= vector2.y;
        return this;
    }

    /**
     * Mutiplies this vector by another one.
     * @param scalar The scalar to multiply by.
     * @return Vector2D This vector, multiplied.
     */
    public Vector2D multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    /**
     * Divide this vector by a scalar.
     * @param scalar The scalar to divide by.
     * @return Vector2D This vector.
     */
    public Vector2D divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    /**
     * Set and get y component.
     */
    public void setY(double value) {
        this.y = value;
    }
    public double getY() {
        return this.y;
    }

    /**
     * Set and get x component.
     */
    public void setX(double value) {
        this.x = value;
    }
    public double getX() {
        return this.x;
    }

    /**
     * Create a vector2D from a string
     * @param string The string to turn into a vector. Must be in the toString format.
     * @return Vector2D The vector from the string.
     **/
    public Vector2D fromString(String string) {
        Vector2D vector = new Vector2D();
        double tx;
        double ty;
        tx = Integer.valueOf(string.substring(string.indexOf("x:"), string.indexOf(",")));
        ty = Integer.valueOf(string.substring(string.indexOf("y:")));
        vector.x = tx;
        vector.y = ty;
        return vector;
    }

    /**
     * Turn this vector into a string.
     * @return String This vector in string form.
     */
    public String toString() {
        return "Vector2D x:" + this.x + ", y:" + this.y;
    }


//    /**
//     * Draw vector, good to see where its pointing.
//     * @param graphicsForDrawing The graphics to draw the vector.
//     * @param drawingColor The color to draw the vector in.
//     */
//    public void drawVector(graphicsForDrawing:Graphics, drawingColor:uint = 0x00FF00) {
//        graphicsForDrawing.lineStyle(0, drawingColor);
//        graphicsForDrawing.moveTo(0, 0);
//        graphicsForDrawing.lineTo(this.x, this.y);
//    }
}
