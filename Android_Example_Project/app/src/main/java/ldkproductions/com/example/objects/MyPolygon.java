package ldkproductions.com.example.objects;

import android.graphics.Color;

import ldkproductions.com.example.data.CGPoint;
import ldkproductions.com.example.data.Vector2D;

import java.util.ArrayList;

/**
 * This class only provides collision detection methods and its draw method should not be used.
 * The collision with circles do not work jet.
 */
public class MyPolygon {

    private CGPoint[] points;

    private CGPoint topLeft ;
    private int maxWidth;
    private int maxHeight;

    public MyPolygon(CGPoint[] points) {
        this.points = points;
        init();
    }

    public MyPolygon(ArrayList<CGPoint> points) {
        this.points  = new CGPoint[points.size()];

        for (int i = 0; i < points.size(); i++) {
            this.points[i] = points.get(i);
        }
        init();
    }

    private void init() {
        calculateTopLeft();
        calculateMaxDim(); //needs to be called after calculateTopLeft()
    }

    public CGPoint[] getPoints() {
        return points;
    }

    public Vector2D[] getVertices_concat() {
        int xOffset = getXOffset();
        int yOffset = getYOffset();
        Vector2D[] vertices = new Vector2D[points.length];
        for (int i=0; i<points.length; i++) {
            vertices[i] = new Vector2D(points[i].x-xOffset, points[i].y-yOffset);
        }
        return vertices;
    }

    public CGPoint[] getVertices_concat2() {
        int xOffset = getXOffset();
        int yOffset = getYOffset();
        CGPoint[] vertices = new CGPoint[points.length];
        for (int i=0; i<points.length; i++) {
            vertices[i] = new CGPoint(points[i].x-xOffset, points[i].y-yOffset);
        }
        return vertices;
    }

    public int getXOffset() {
        int minX = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if(points[i].getX() < minX)
                minX = points[i].getX();
        }
        return minX;
    }

    public int getYOffset() {
        int minY = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            if(points[i].getY() < minY)
                minY = points[i].getY();
        }
        return minY;
    }

    private void calculateTopLeft() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;

        for (int i=0; i<points.length; i++) {
            if (points[i].getX() < minX)
                minX = points[i].getX();
            if (points[i].getY() < minY)
                minY = points[i].getY();
        }

        topLeft = new CGPoint(minX, minY);
    }

    private void calculateMaxDim() {
        int maxX = 0;
        int maxY = 0;

        for (int i=0; i<points.length; i++) {
            if (points[i].getX() > maxX)
                maxX = points[i].getX();
            if (points[i].getY() > maxY)
                maxY = points[i].getY();
        }

        maxWidth = maxX - topLeft.getX();
        maxHeight = maxY - topLeft.getY();
    }

    public CGPoint getTopLeft() {
        return topLeft;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public boolean collidesWith(MyPolygon polygon) {
        if(! GL_Shape.alignedRectangleRectangleCollision(this.topLeft.getX(), this.topLeft.getY(), this.maxWidth, this.maxHeight,
                polygon.topLeft.getX(), polygon.topLeft.getY(), polygon.maxWidth, polygon.maxHeight))
            return false;
        return polygonPolygonCollision(this, polygon);
    }

    /**
     * From http://rocketmandevelopment.com/blog/separation-of-axis-theorem-for-collision-detection/
     * @param polygon1
     * @param polygon2
     * @return
     */
    public static boolean polygonPolygonCollision(MyPolygon polygon1, MyPolygon polygon2) {
        if (checkPolygonsForSAT(polygon1, polygon2) == false)
            return false;
        if (checkPolygonsForSAT(polygon2, polygon1) == false)
            return false;
        return true;
    }

    /*public static Vector2D polygonSAT(MyPolygon polygon1, MyPolygon polygon2) {
        double test1;        // numbers to use to test for overlap
        double test2;
        double testNum;      // number to test if its the new max/min
        double min1 = 0;     //current smallest(shape 1)
        double max1;         //current largest(shape 1)
        double min2;         //current smallest(shape 2)
        double max2 = 0;     //current largest(shape 2)
        Vector2D axis = null;//the normal axis for projection
        double offset;
        Vector2D vectorOffset;
        Vector2D[] vectors1; //the points
        Vector2D[] vectors2; //the points
        vectors1 = polygon1.getVertices_concat();//these functions are in my polygon class, all they do is return a Vector.<Vector2D> of the vertices of the polygon
        vectors2 = polygon2.getVertices_concat();

        // add a little padding to make the test work correctly
        if (vectors1.length == 2) {
            Vector2D temp = new Vector2D(-(vectors1[1].y - vectors1[0].y), vectors1[1].x - vectors1[0].x);
            temp.truncate(0.0000000001);

            Vector2D[] vectors = new Vector2D[3];
            vectors[0] = vectors1[0];
            vectors[1] = vectors1[1];
            vectors[2] = vectors1[1].cloneVector().add(temp);
        }
        if (vectors2.length == 2) {
            Vector2D temp = new Vector2D(-(vectors2[1].y - vectors2[0].y), vectors2[1].x - vectors2[0].x);
            temp.truncate(0.0000000001);

            Vector2D[] vectors = new Vector2D[3];
            vectors[0] = vectors2[0];
            vectors[1] = vectors2[1];
            vectors[2] = vectors2[1].cloneVector().add(temp);
        }

        // find vertical offset
        vectorOffset = new Vector2D(polygon1.getXOffset() - polygon2.getXOffset(),
                polygon1.getYOffset() - polygon2.getYOffset());

        // loop to begin projection
        for (int i = 0; i < vectors1.length; i++) {
            // get the normal axis, and begin projection
            axis = findNormalAxis(vectors1, i);
            // project polygon1
            min1 = axis.dotProduct(vectors1[0]);
            max1 = min1;//set max and min equal
            for (int j = 1; j < vectors1.length; j++) {
                testNum = axis.dotProduct(vectors1[j]);//project each point
                if (testNum < min1) min1 = testNum;//test for new smallest
                if (testNum > max1) max1 = testNum;//test for new largest
            }
            // project polygon2
            min2 = axis.dotProduct(vectors2[0]);
            max2 = min2;//set 2's max and min
            for (int j = 1; j < vectors2.length; j++) {
                testNum = axis.dotProduct(vectors2[j]);//project the point
                if (testNum < min2) min2 = testNum;//test for new min
                if (testNum > max2) max2 = testNum;//test for new max
            }
            // apply the offset to each max/min(no need for each point, max and min are all that matter)
            offset = axis.dotProduct(vectorOffset);//calculate offset
            min1 += offset;//apply offset
            max1 += offset;//apply offset

            // and test if they are touching

            test1 = min1 - max2;//test min1 and max2
            test2 = min2 - max1;//test min2 and max1
            if(test1 > 0 || test2 > 0){//if they are greater than 0, there is a gap
                return null;//just quit
            }
        }

        //if you're here, there is a collision

        return new Vector2D(axis.x*((max2-min1)*-1), axis.y*((max2-min1)*-1)); //return the separation, apply it to a polygon to separate the two shapes.
    }

    private static Vector2D findNormalAxis(Vector2D[] vertices, int index) {
        Vector2D vector1 = vertices[index];
        Vector2D vector2 = (index >= vertices.length - 1) ? vertices[0] : vertices[index + 1]; //make sure you get a real vertex, not one that is outside the length of the vector.
        Vector2D normalAxis = new Vector2D( -(vector2.y - vector1.y), vector2.x - vector1.x);//take the two vertices, make a line out of them, and find the normal of the line
        normalAxis.normalize();//normalize the line(set its length to 1)
        return normalAxis;
    }*/


    //Test

    /**
     * Checks for overlaps between polygons
     * @param	polygonA
     * @param	polygonB
     * @return
     */
    static private boolean checkPolygonsForSAT(MyPolygon polygonA, MyPolygon polygonB) {
        // working vars
        double min0, max0;
        double min1, max1;
        CGPoint vAxis;
        double sOffset;
        CGPoint vOffset;
        double d0;
        double d1;
        int i;
        int j;
        double t;
        CGPoint[] p1;	// array of vertices
        CGPoint[] p2;
        CGPoint[] ra;

        double shortestDist = Double.MAX_VALUE;
        double distmin;
        double distminAbs;

        /*var result: CollisionInfo = new CollisionInfo();
        result.shapeA = (flip) ? polygonB : polygonA;
        result.shapeB = (flip) ? polygonA : polygonB;
        result.shapeAContained = true;
        result.shapeBContained = true;*/

        // get the vertices
        p1 = polygonA.getVertices_concat2();
        p2 = polygonB.getVertices_concat2();

        /*
        // small hack here to deal with line segments - adds a small depth to make it act like a thing rectangle
        if (p1.length == 2) {
            ra = new Point(-(p1[1].y - p1[0].y), p1[1].x - p1[0].x);
            ra.normalize(0.0000001);
            p1.push(Point(p1[1]).add(ra));
        }
        if (p2.length == 2) {
            ra = new Point(-(p2[1].y - p2[0].y), p2[1].x - p2[0].x);
            ra.normalize(0.0000001);
            p2.push(Point(p2[1]).add(ra));
        }
        */

        // get the offset
        vOffset = new CGPoint(polygonA.getXOffset() - polygonB.getXOffset(), polygonA.getYOffset() - polygonB.getYOffset());

        // loop through all of the axis on the first polygon
        for (i = 0; i < p1.length; i++) {
            // find the axis that we will project onto
            vAxis = getAxisNormal(p1, i);

            // project polygon A
            min0 = vectorDotProduct(vAxis, p1[0]);
            max0 = min0;
            //
            for (j = 1; j < p1.length; j++) {
                t = vectorDotProduct(vAxis, p1[j]);
                if (t < min0) min0 = t;
                if (t > max0) max0 = t;
            }


            // project polygon B
            min1 = vectorDotProduct(vAxis, p2[0]);
            max1 = min1;
            //
            for (j = 1; j < p2.length; j++) {
                t = vectorDotProduct(vAxis, p2[j]);
                if (t < min1) min1 = t;
                if (t > max1) max1 = t;
            }

            // shift polygonA's projected points
            sOffset = vectorDotProduct(vAxis, vOffset);
            min0 += sOffset;
            max0 += sOffset;

            // test for intersections
            d0 = min0 - max1;
            d1 = min1 - max0;
            if (d0 > 0 || d1 > 0) {
                // gap found
                return false;
            }

            /*
            if(docalc) {
                // check for containment
                if (!flip) {
                    if (max0 > max1 || min0 < min1) result.shapeAContained = false;
                    if (max1 > max0 || min1 < min0) result.shapeBContained = false;
                } else {
                    if (max0 < max1 || min0 > min1) result.shapeAContained = false;
                    if (max1 < max0 || min1 > min0) result.shapeBContained = false;
                }


                distmin = (max1 - min0) * -1;  //Math.min(dist0, dist1);
                if (flip) distmin *= -1
                distminAbs = (distmin < 0) ? distmin * -1 : distmin;
                if (distminAbs < shortestDist) {
                    // this distance is shorter so use it...
                    result.distance = distmin;
                    result.vector = vAxis;
                    //
                    shortestDist = distminAbs;
                }
            }
            */

            //if (polygonA is Box && i == 1) break;

        }
        // if you are here then no gap was found
        return true;

    }

    /**
     * Returns the normal of a polygons side.
     * @param	vertexArray	Array of points
     * @param	pointIndex
     * @return
     */
    static private CGPoint getAxisNormal(CGPoint[] vertexArray, int pointIndex) {
        // grab the points
        CGPoint pt1 = vertexArray[pointIndex];
        CGPoint pt2 = (pointIndex >= vertexArray.length - 1) ? vertexArray[0] : vertexArray[pointIndex + 1];
        //
        CGPoint p = new CGPoint( -(pt2.y - pt1.y), pt2.x - pt1.x);
        p.normalize();
        return p;

    }

    /**
     * Returns the dor product of two vectors
     * @param	pt1
     * @param	pt2
     * @return
     */
    static private double vectorDotProduct(CGPoint pt1, CGPoint pt2) {
        return (pt1.x * pt2.x + pt1.y * pt2.y);
    }

    //Test End


    public MyPolygon draw(float[] projectionMatrix) { //false
        int color = Color.rgb(255,0,255);
        if(points.length > 3)
            color = Color.rgb(255,128,128);
        for (int i = 2; i < points.length;i++) {
            //new GL_Triangle(points[i-2], points[i-1], points[i], color).draw(projectionMatrix);
            CGPoint p1 = new CGPoint((float) getVertices_concat()[i-2].getX()+getXOffset(), (float) getVertices_concat()[i-2].getY()+getYOffset());
            CGPoint p2 = new CGPoint((float) getVertices_concat()[i-1].getX()+getXOffset(), (float) getVertices_concat()[i-1].getY()+getYOffset());
            CGPoint p3 = new CGPoint((float) getVertices_concat()[i-0].getX()+getXOffset(), (float) getVertices_concat()[i-0].getY()+getYOffset());
            new GL_Triangle(p1,p2,p3, color).draw(projectionMatrix);
        }
        return this;
    }



    /**
     * Checks whether this polygon collides with a circle.
     *
     * Based on SAT and article on:
     * https://bitlush.com/blog/circle-vs-polygon-collision-detection-in-c-sharp
     *
     * @param r The radius of the circle
     * @param cp The middle of the circle
     * @return true if the collide
     */
    public boolean collidesWithCircle(int r, CGPoint cp) {
        if(! GL_Shape.alignedRectangleRectangleCollision(this.topLeft.getX(), this.topLeft.getY(), this.maxWidth, this.maxHeight,
                cp.getX()-r, cp.getY()-r, 2*r, 2*r))
            return false;

        float radiusSquared = r * r;

        CGPoint vertex = points[points.length - 1];

        CGPoint circleCenter = cp;

        float nearestDistance = Float.MAX_VALUE;
        boolean nearestIsInside = false;
        int nearestVertex = -1;
        boolean lastIsInside = false;

        for (int i = 0; i < points.length; i++) {
            CGPoint nextVertex = points[i];

            CGPoint axis = new CGPoint(circleCenter.x - vertex.x, circleCenter.y - vertex.y);

            float distance = axis.x * axis.x + axis.y * axis.y - radiusSquared;

            if (distance <= 0) {
                return true;
            }

            boolean isInside = false;

            CGPoint edge = new CGPoint(nextVertex.x - vertex.x, nextVertex.y - vertex.y);

            float edgeLengthSquared = edge.x * edge.x + edge.y * edge.y;

            if (edgeLengthSquared != 0) {
                float dot = (float) vectorDotProduct(edge, axis);

                if (dot >= 0 && dot <= edgeLengthSquared) {
                    float tmp_mult = (dot / edgeLengthSquared);
                    CGPoint tmp_vec = new CGPoint(edge.x * tmp_mult, edge.y * tmp_mult);
                    CGPoint projection = new CGPoint(vertex.x + tmp_vec.x, vertex.y + tmp_vec.y);

                    axis = new CGPoint(projection.x - circleCenter.x, projection.y - circleCenter.y);

                    if (axis.x * axis.x + axis.y * axis.y <= radiusSquared) {
                        return true;
                    } else {
                        if (edge.x > 0) {
                            if (axis.x > 0) {
                                return false;
                            }
                        } else if (edge.x < 0) {
                            if (axis.x < 0) {
                                return false;
                            }
                        } else if (edge.x > 0) {
                            if (axis.x < 0) {
                                return false;
                            }
                        } else {
                            if (axis.x > 0) {
                                return false;
                            }
                        }

                        isInside = true;
                    }
                }
            }

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestIsInside = isInside || lastIsInside;
                nearestVertex = i;
            }

            vertex = nextVertex;
            lastIsInside = isInside;
        }

        if (nearestVertex == 0) {
            return nearestIsInside || lastIsInside;
        } else {
            return nearestIsInside;
        }
    }

    /**
     * Based on http://stackoverflow.com/a/402019/6128747
     *
     * There are only two cases when the circle intersects with the polygon:
     * - Either the circle's centre lies inside the polygon, or
     * - One of the edges of the polygon has a point in the circle
     *
     * @param circleM The middle of the circle
     * @param r The radius of the circle
     * @return true if they collide
     */
    public boolean collidesWithCircle(CGPoint circleM, int r) {

        if (isPointInside(circleM))
            return true;

        for (int i=1; i<points.length; i++) { //check if any edge is intersecting the circle
            if (lineCircleCollision(circleM, r, points[i-1], points[i]))
                return true;
        }

        if (lineCircleCollision(circleM, r, points[0], points[points.length-1])) //check if last edge is intersecting the circle
            return true;

        return false;
    }

    /**
     * Based on http://stackoverflow.com/a/15599478/6128747
     * Checks if a point is inside a polygon
     *
     * Node: Change the two > to >= and the < to <= to return true if it is on one of the line of the polygon
     *
     * @param point The point to be checked
     * @return true of point is inside.
     */
    boolean isPointInside(CGPoint point) {
        int i, j, nvert = points.length;
        boolean c = false;

        for(i = 0, j = nvert - 1; i < nvert; j = i++) {
            if( ( (points[i].y > point.y ) != (points[j].y > point.y) ) &&
                    (point.x < (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y) + points[i].x)
                    )
                c = !c;
        }

        return c;
    }

    /**
     * Based on http://devmag.org.za/2009/04/17/basic-collision-detection-in-2d-part-2/
     *
     * Checks weather a
     *
     * @param c_m
     * @param c_r
     * @param l_p1
     * @param l_p2
     * @return
     */
    public boolean lineCircleCollision(CGPoint c_m, int c_r, CGPoint l_p1, CGPoint l_p2) {

        // Transform to local coordinates
        CGPoint localP1 = new CGPoint(l_p1.x - c_m.x, l_p1.y - c_m.y);
        CGPoint localP2 = new CGPoint(l_p2.x - c_m.x, l_p2.y - c_m.y);
        CGPoint p2MinusP1 = new CGPoint(localP2.x - localP1.x, localP2.y - localP1.y);
        float a = p2MinusP1.x * p2MinusP1.x + p2MinusP1.y * p2MinusP1.y;
        float b = 2 * ((p2MinusP1.x * localP1.x) + (p2MinusP1.y * localP1.y));
        float c = (localP1.x * localP1.x) + (localP1.y * localP1.y) - (c_r * c_r);
        float delta = b * b - (4 * a * c);

        if (delta < 0) // No intersection
            return false;
        else if (delta == 0) // One intersection (just tangent)
            return false;

        //else two intersections --> collision

        /* //Original code (may be used to calculate the actual intersection points)
        Input
            LineP1	Point	First point describing the line
            LineP2	Point	Second point describing the line
            CircleCentre	Point	The centre of the circle
            Radius	Floating-point	The circle's radius
        Output
            The point(s) of the collision, or null if no collision exists.
        Method
            // Transform to local coordinates
            LocalP1 = LineP1 – CircleCentre
            LocalP2 = LineP2 – CircleCentre
            // Precalculate this value. We use it often
            P2MinusP1 = LocalP2 – LocalP1

            a = (P2MinusP1.X) * (P2MinusP1.X) + (P2MinusP1.Y) * (P2MinusP1.Y)
            b = 2 * ((P2MinusP1.X * LocalP1.X) + (P2MinusP1.Y * LocalP1.Y))
            c = (LocalP1.X * LocalP1.X) + (LocalP1.Y * LocalP1.Y) – (Radius * Radius)
            delta = b * b – (4 * a * c)
            if (delta < 0) // No intersection
                return null;
            else if (delta == 0) // One intersection
                u = -b / (2 * a)
            return LineP1 + (u * P2MinusP1)
		    //Use LineP1 instead of LocalP1 because we want our answer in global space, not the circle's local space
            else if (delta > 0) // Two intersections
            SquareRootDelta = sqrt(delta)

             u1 = (-b + SquareRootDelta) / (2 * a)
             u2 = (-b - SquareRootDelta) / (2 * a)

            return { LineP1 + (u1 * P2MinusP1) ; LineP1 + (u2 * P2MinusP1)}
        */

        return true;
    }



}
