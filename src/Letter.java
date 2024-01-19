import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;


public class Letter {
	
	private char c;
	
	private int drawX;
	private int drawY;
	private int xDiff;
	private int yDiff;
	private int width;
	private int hight;
	private int spaceR;
	
	public static final int MIN_LETTER_DISTANCE = 5;
	
	private boolean placed = false;
	
	public Letter(char c) {
		this.c = c;
		initValues(0, 0, 0, 0, 0, 0, 0);
	}
	
	public Letter(char c, int drawX, int drawY, int xDiff, int yDiff, int width, int hight, int spaceR, boolean placed) {
		this.c = c;
		initValues(drawX, drawY, xDiff, yDiff, width, hight, spaceR);
		this.placed = placed;
	}
	
	public Letter(char c, int drawX, int drawY, int xDiff, int yDiff, int width, int hight, int spaceR) {
		this.c = c;
		initValues(drawX, drawY, xDiff, yDiff, width, hight, spaceR);
	}
	
	private void initValues(int drawX, int drawY, int xDiff, int yDiff, int width, int hight, int spaceR) {
		this.drawX = drawX;
		this.drawY = drawY;
		this.xDiff = xDiff;
		this.yDiff = yDiff;
		this.width = width;
		this.hight = hight;
		this.spaceR = spaceR;
	}
	
	public void setUnplaced() {
		this.drawX = Short.MIN_VALUE;
		this.drawY = Short.MIN_VALUE;
	}
	
	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
	
	public boolean isPlaced() {
		return placed;
	}
	
	public char getLetter() {
		return c;
	}
	
	public char[] getLetterAsArray() {
		char[] array = new char[1];
		array[0] = c;
		return array;
	}

	public int getDrawX() {
		return drawX;
	}

	public void setDrawX(int drawX) {
		this.drawX = drawX;
	}

	public int getDrawY() {
		return drawY;
	}

	public void setDrawY(int drawY) {
		this.drawY = drawY;
	}
	
	public void setDrawPositions(int drawX, int drawY) {
		this.drawX = drawX;
		this.drawY = drawY;
	}
	
	public void setRealPositions(int realX, int realY) {
		this.drawX = realX-xDiff;
		this.drawY = realY-yDiff;
	}
	
	public void setRealX(int realX) {
		this.drawX = realX-xDiff;
	}
	
	public int getRealX() {
		return drawX+xDiff;
	}
	
	public void setRealY(int realY) {
		this.drawY = realY-yDiff;
	}

	public int getRealY() {
		return drawY+yDiff;
	}

	public int getxDiff() {
		return xDiff;
	}

	public void setxDiff(int xDiff) {
		this.xDiff = xDiff;
	}

	public int getyDiff() {
		return yDiff;
	}

	public void setyDiff(int yDiff) {
		this.yDiff = yDiff;
	}

	public void setDiff(int xDiff, int yDiff) {
		this.xDiff = xDiff;
		this.yDiff = yDiff;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}
	
	public void setSpaceR(int spaceR) {
		this.spaceR = spaceR;
	}
	
	public int getSpaceR() {
		return spaceR;
	}
	
	public void setDimensions(int width, int hight) {
		this.width = width;
		this.hight = hight;
	}
	
	public boolean isInXRange(int xDim) {
		return(getRealX()+width + MIN_LETTER_DISTANCE < xDim);
	}
	
	public boolean isInYRange(int yDim) {
		return(getRealY()+hight + MIN_LETTER_DISTANCE < yDim);
	}
	
	/**
	 * Checks if another Letter is overlapping with this (MIN_LETTER_DISTANCE added).
	 * @param l The other letter
	 * @return true if overlapping
	 */
	public boolean isOverlapping(Letter l) {
		int x1 = getRealX() - MIN_LETTER_DISTANCE/2;
		int y1 = getRealY() - MIN_LETTER_DISTANCE/2;
		int x3 = l.getRealX() - MIN_LETTER_DISTANCE/2;
		int y3 = l.getRealY() - MIN_LETTER_DISTANCE/2;
		int w1 = width + MIN_LETTER_DISTANCE;
		int h1 = hight + MIN_LETTER_DISTANCE;
		int w2 = l.getWidth() + MIN_LETTER_DISTANCE;
		int h2 = l.getHight() + MIN_LETTER_DISTANCE;
		
		int x2 = x1+w1;
		int y2 = y1+h1;
		int x4 = x3+w2;
		int y4 = y3+h2;
		
		return(x2>=x3 && x4>=x1 && y2>=y3 && y4>=y1);	
	}
	
	public String declareItself(boolean withTolerance) {
		int tol = FullFontAtlas.additional_tolerance;
		String unicode = "0x" + Integer.toHexString(c | 0x10000).substring(1) ;
		if (withTolerance)
			return "new Letter(" + unicode + "," + String.valueOf(drawX-tol/2) + "," + String.valueOf(drawY-tol/2) + "," + String.valueOf(xDiff+tol/2) + "," + String.valueOf(yDiff+tol/2) + "," + String.valueOf(width+tol) + "," + String.valueOf(hight+tol) + "," + spaceR +")";
		else
			return "new Letter(" + unicode + "," + drawX + "," + drawY + "," + xDiff + "," + yDiff + "," + width + "," + hight + "," + spaceR + ")";
	}
	
	
	
	
	
	// --------------------------------- some static helper methods --------------------------------- //
	
	public static boolean areAllPlaced(ArrayList<Letter> list) {
		for (Letter letter : list) {
			if(!letter.isPlaced())
				return false;
		}
		return true;
	}
	
	public static int getPlacedCount(ArrayList<Letter> list) {
		int cnt = 0;
		for (Letter letter : list) {
			if(letter.isPlaced())
				cnt++;
		}
		return cnt;
	}
	
	public static int getUnplacedCount(ArrayList<Letter> list) {
		int cnt = 0;
		for (Letter letter : list) {
			if(!letter.isPlaced())
				cnt++;
		}
		return cnt;
	}
	
}
