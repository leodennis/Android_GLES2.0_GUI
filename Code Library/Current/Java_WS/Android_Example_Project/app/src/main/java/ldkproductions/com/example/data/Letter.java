package ldkproductions.com.example.data;

public class Letter {
	
	private char c;
	
	private int drawX;
	private int drawY;
	private int xDiff;
	private int yDiff;
	private int width;
	private int height;
	private int spaceR;
	
	public static final int MIN_LETTER_DISTANCE = 1;
	
	public Letter(int c) {
		this.c = Character.toString((char)c).charAt(0);
		initValues(0, 0, 0, 0, 0, 0, 0);
	}
	
	public Letter(int c, int drawX, int drawY, int xDiff, int yDiff, int width, int height, int spaceR) {
		this.c = Character.toString((char)c).charAt(0);
		initValues(drawX, drawY, xDiff, yDiff, width, height, spaceR);
	}
	
	private void initValues(int drawX, int drawY, int xDiff, int yDiff, int width, int height, int spaceR) {
		this.drawX = drawX;
		this.drawY = drawY;
		this.xDiff = xDiff;
		this.yDiff = yDiff;
		this.width = width;
		this.height = height;
		this.spaceR = spaceR;
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

	public int getDrawY() {
		return drawY;
	}

	public int getRealX() {
		return drawX+xDiff;
	}

	public int getRealY() {
		return drawY+yDiff;
	}

	public int getSpaceR() {
		return spaceR;
	}

	public int getxDiff() {
		return xDiff;
	}

	public int getyDiff() {
		return yDiff;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}

