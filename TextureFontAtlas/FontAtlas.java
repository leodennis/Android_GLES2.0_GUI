import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class FontAtlas {
	
	private List<Letter> characters;
	private int atlasSize;
	private int atlasNumber;

	private Font font;
	private float fontSize;
	
	private int drawingOffset;
	
	public FontAtlas(List<Letter> chars, int atlasSize, int atlasNumber) {
		initValues(chars, atlasSize, atlasNumber, null, 1, 0);
	}
	
	public FontAtlas(List<Letter> chars, int atlasSize, int atlasNumber, Font font, float fontSize) {
		initValues(chars, atlasSize, atlasNumber, font, fontSize, 0);
	}
	
	public FontAtlas(List<Letter> list, int atlasSize, int atlasNumber, Font font, float fontSize, int drawingOffset) {
		initValues(list, atlasSize, atlasNumber, font, fontSize, drawingOffset);
	}
	
	public FontAtlas(List<Letter> chars, int atlasSize, int atlasNumber, int drawingOffset) {
		initValues(chars, atlasSize, atlasNumber, null, 1, drawingOffset);
	}
	
	private void initValues(List<Letter> chars, int atlasSize, int atlasNumber, Font font, float fontSize, int drawingOffset) {
		this.characters = chars;
		this.atlasSize = atlasSize;
		this.atlasNumber = atlasNumber;
		this.font = font;
		this.fontSize = fontSize;
		this.drawingOffset = drawingOffset;
	}
	
	public void setFontInfo(Font font, int fontSize) {
		this.font = font;
		this.fontSize = fontSize;
	}
	
	public void setDrawingOffset(int drawingOffset) {
		this.drawingOffset = drawingOffset;
	}
	
	public void draw(Graphics2D g2d, boolean highliteBounds) {
		for (int i = 0; i < characters.size(); i++) {
			Letter letter = characters.get(i);
			
			if (highliteBounds) {
				g2d.setPaint(Color.GREEN);
				g2d.drawRect(letter.getRealX(), letter.getRealY(), letter.getWidth(), letter.getHight());
				
				/*
				g2d.setPaint(Color.RED);
				g2d.drawRect(letter.getRealX() - letter.getxDiff(), letter.getRealY(), letter.getWidth()+letter.getSpaceR()+letter.getxDiff(), letter.getHight());
				*/
			}
			
			g2d.setPaint(Color.black);
			g2d.drawChars(letter.getLetterAsArray(), 0, 1, letter.getDrawX(), letter.getDrawY() + drawingOffset);
		}
	}
	
	private BufferedImage drawOnImage(boolean highliteBounds) {
		// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
		// into integer pixels
		BufferedImage fontAtlas = new BufferedImage(atlasSize, atlasSize, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = fontAtlas.createGraphics();
		font = font.deriveFont(fontSize);
	    g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //make text smooth
		
		g2d.setPaint(Color.black);
		draw(g2d, highliteBounds);
		
		return fontAtlas;
	}
	
	public void show(JFrame context) {
		MyImageView.shwoImageModeless(context, drawOnImage(true) , Color.WHITE, null);
	}
	
	public void save(String path) {
		try {
			String fileSeparator = System.getProperty("file.separator");
			if(!path.endsWith(fileSeparator))
				path = path + fileSeparator;
			ImageIO.write(drawOnImage(false), "PNG", new File(path + getNameID()+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	//---------------------------------------- Other getters and setters ---------------------------------------------//
	
	
	public int getDrawingOffset() {
		return drawingOffset;
	}

	public List<Letter> getCharacters() {
		return characters;
	}

	public void setCharacters(List<Letter> characters) {
		this.characters = characters;
	}

	public int getAtlasSize() {
		return atlasSize;
	}

	public void setAtlasSize(int atlasSize) {
		this.atlasSize = atlasSize;
	}

	public int getAtlasNumber() {
		return atlasNumber;
	}

	public void setAtlasNumber(int atlasNumber) {
		this.atlasNumber = atlasNumber;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	
	private String getNameID() {
		return font.getName().toLowerCase().replaceAll("\\W", "") + "_" + ((int)fontSize) + "_" + atlasNumber;
	}
	
	public String declareItself(boolean withTolerance) {
		StringBuilder text = new StringBuilder();
		
		String name = "atlas" + atlasNumber;
		String arrayName = "letterList" + atlasNumber; 
		
		text.append("		FontAtlas " + name + " = new FontAtlas(context," + atlasSize + ",R.drawable." + getNameID() + ");\n");
		text.append("		Letter[] " + arrayName + " = new Letter[" + characters.size() + "];\n\n");
		
		for (int i=0; i<characters.size(); i++) {
			Letter l = characters.get(i);
			text.append("		" + arrayName + "[" + i + "] = " + l.declareItself(withTolerance) + ";\n");
		}
		
		text.append("\n		" + name + ".setLetterList(" + arrayName + ");\n\n");
		
		return text.toString();
		
	}

	
}
