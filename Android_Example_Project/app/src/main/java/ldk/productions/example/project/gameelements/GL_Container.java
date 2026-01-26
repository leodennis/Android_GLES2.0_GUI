package ldk.productions.example.project.gameelements;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ldk.productions.example.project.data.CGPoint;
import ldk.productions.example.project.objects.GL_Shape;

/**
 * Created by Leo K., LDK-Productions on 28.09.2021.
 */
public class GL_Container extends GL_Shape {

    private ArrayList<GL_Shape> children = new ArrayList<>();

    public GL_Container() {
        super(0, 0);
    }

    /**
     * Copy constructor
     * @param obj the GL_Container to be copied
     */
    public GL_Container(GL_Container obj) {
        super(obj, true);

        for (GL_Shape element : obj.children) {
            this.children.add((GL_Shape) element.clone());
        }
    }

    @Override
    public Object clone() {
        return new GL_Container(this);
    }

    public void add(@NonNull GL_Shape child) {
        if (!children.contains(child)) {
            children.add(child);
        }
    }

    public void remove(GL_Shape child) {
        children.remove(child);
    }

    @Override
    protected void generateVertexArray() {
        // abstract method which had to be implemented - not used
    }

    @Override
    public boolean checkTouchDown(int x, int y) {
        boolean touchWithinElement = false;

        for (GL_Shape child : children) {
            if (child.checkTouchDown(x, y)) {
                touchWithinElement = true;
            }
        }

        return touchWithinElement;
    }

    @Override
    public boolean checkTouchUp(int x, int y) {
        boolean touchWithinElement = false;

        for (GL_Shape child : children) {
            if (child.checkTouchUp(x, y)) {
                touchWithinElement = true;
            }
        }

        return touchWithinElement;
    }

    @Override
    public boolean checkTouchMove(int x, int y) {
        boolean touchWithinElement = false;

        for (GL_Shape child : children) {
            if (child.checkTouchMove(x, y)) {
                touchWithinElement = true;
            }
        }

        return touchWithinElement;
    }

    public void draw(float[] projectionMatrix) {
        if (visible) {
            for (GL_Shape child : children) {
                child.draw(projectionMatrix);
            }
        }
    }

    @Override
    public boolean collidesWithPoint(int x, int y) {
        // TODO: update to API level 24
        // return children.stream().anyMatch(child -> child.collidesWithPoint(x, y));

        for (GL_Shape child : children) {
            if (child.collidesWithPoint(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean collidesWithRectangle(int x, int y, int w, int h) {
        for (GL_Shape child : children) {
            if (child.collidesWithRectangle(x, y, w, h)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CGPoint getPosTopLeft() {
        return pos;
    }

    @Override
    public CGPoint getPosBottomRight() {
        return new CGPoint(pos.x, pos.y);
    }

}
