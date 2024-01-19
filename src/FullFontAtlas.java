import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


public class FullFontAtlas {
	
	public static int additional_tolerance = 4; //Must be an even number and greater or equal MIN_LETTER_DISTANCE (of Letter)
	
	private String myFontName;
	private ArrayList<FontAtlas> fontAtlasList;
	private int maxTextHeight;
	private int maxDescent;
	private int normalCapHeight;
	private int wsWidth;
	private int spacing;
	
	private String chars;
	
	private JFrame context;
	private ProgressMonitorView progressMonitor;
	
	public static enum Modes {
		MODE_SINGLE,
		MODE_MULTIPLE,
		MODE_MULTIPLE_AUTO,
		MODE_AUTO,
		MODE_CUSTOM;
	}
	
	public FullFontAtlas(String chars, int atlasDim, Font font, Font backupFont, float fontSize, Modes mode, boolean auto, JFrame context) {
		this.chars = chars;
		this.context = context;
		this.progressMonitor = (ProgressMonitorView) context;
		
		
		new GenerateAtlas(atlasDim, font, backupFont, fontSize, mode, auto).execute();
	}
	
	public FullFontAtlas(String myFontName, ArrayList<FontAtlas> fontAtlasList, int maxTextHeight, int maxDescent, int normalCapHeight, int wsWidth, int spacing) {
		setValues(myFontName, fontAtlasList, maxTextHeight, maxDescent, normalCapHeight, wsWidth, spacing);
	}
	
	private void setValues(String myFontName, ArrayList<FontAtlas> fontAtlasList, int maxTextHeight, int maxDescent, int normalCapHeight, int wsWidth, int spacing) {
		this.myFontName = myFontName;
		this.fontAtlasList = fontAtlasList;
		this.maxTextHeight = maxTextHeight;
		this.maxDescent = maxDescent;
		this.normalCapHeight = normalCapHeight;
		this.wsWidth = wsWidth;
		this.spacing = spacing;
	}

	public String getMyFontName() {
		return myFontName;
	}

	public void setMyFontName(String myFontName) {
		this.myFontName = myFontName;
	}

	public ArrayList<FontAtlas> getFontAtlasList() {
		return fontAtlasList;
	}

	public void setFontAtlasList(ArrayList<FontAtlas> fontAtlasList) {
		this.fontAtlasList = fontAtlasList;
	}

	public int getMaxTextHeight() {
		return maxTextHeight;
	}

	public void setMaxTextHeight(int maxTextHeight) {
		this.maxTextHeight = maxTextHeight;
	}

	public int getMaxDescent() {
		return maxDescent;
	}

	public void setMaxDescent(int maxDescent) {
		this.maxDescent = maxDescent;
	}

	public int getWsWidth() {
		return wsWidth;
	}

	public void setWsWidth(int wsWidth) {
		this.wsWidth = wsWidth;
	}

	public int getSpacing() {
		return spacing;
	}

	public void setSpacing(int spacing) {
		this.spacing = spacing;
	}

	public int getNormalCapHeight() {
		return normalCapHeight;
	}

	public void setNormalCapHeight(int normalCapHeight) {
		this.normalCapHeight = normalCapHeight;
	}
	
	
	//------------------ Functions -----------------//
	
	private void reorderLetters(ArrayList<Letter> list) {
		Collections.sort(list, new LetterWidthComparator());
		Collections.sort(list, new LetterHeightComparator());
		
		for (Letter letter : list) {
			System.out.print(letter.getLetter());
		}
		
	}
	
	private ArrayList<FontAtlas> autoAlgorithm(ArrayList<FontAtlas> originalAtlasList, ArrayList<Letter> originalLetterList, int rep, int dim, int maxDim, int n, Font font, Font backupFont, int fontSize, int maxHmD, int maxHmDBackup) {
		if(dim > maxDim) {
			for (Letter letter : originalLetterList) {
				if (letter.isPlaced())
					return null;
			}
			return originalAtlasList;
		}
		
		ArrayList<FontAtlas> oAtlasListCp = new ArrayList<>(originalAtlasList);
		ArrayList<FontAtlas> nAtlasListCp = new ArrayList<>(originalAtlasList);
		ArrayList<FontAtlas> sAtlasListCp = new ArrayList<>(originalAtlasList);
		
		ArrayList<Letter> oLetterListCp = new ArrayList<Letter>(originalLetterList);
		ArrayList<Letter> nLetterListCp = new ArrayList<Letter>(originalLetterList);
		ArrayList<Letter> sLetterListCp = new ArrayList<Letter>(originalLetterList);
		
		oAtlasListCp = autoAlgorithm(oAtlasListCp, oLetterListCp, 0, dim*2, maxDim, n, font, backupFont, fontSize, maxHmD, maxHmDBackup);
		
		ArrayList<Letter> subList = repositionLetters(nLetterListCp, dim);
		FontAtlas fontAtlas = new FontAtlas(subList, dim, ++n, font, backupFont, fontSize, maxHmD, maxHmDBackup);
			
		nAtlasListCp.add(fontAtlas);
		
		nAtlasListCp = autoAlgorithm(nAtlasListCp, nLetterListCp, 0, dim*2, maxDim, n, font, backupFont, fontSize, maxHmD, maxHmDBackup);
		
		if(rep < 5) {
			sAtlasListCp = autoAlgorithm(sAtlasListCp, sLetterListCp, ++rep, dim, maxDim, n, font, backupFont, fontSize, maxHmD, maxHmDBackup);
		} else {
			sAtlasListCp = null;
		}
				
		if (getSize(nAtlasListCp) < getSize(oAtlasListCp)) {
			if(getSize(sAtlasListCp) < getSize(nAtlasListCp))
				return sAtlasListCp;
			else 
				return nAtlasListCp;
		}
		return oAtlasListCp;
	}
	
	private int getSize(ArrayList<FontAtlas> list) {
		if(list == null)
			return Short.MAX_VALUE; //return high value but not Integer.MAX_VALUE to prevent overflow
		int s = 0;
		for (FontAtlas fontAtlas : list) {
			s+=fontAtlas.getAtlasSize();
		}
		return s;
	}
	
	private int getPositionInValueArray(int n) {
		for (int i = 0; i < AtlasView.resolutionValues.length; i++) {
			if(AtlasView.resolutionValues[i] == n)
				return i;
		}
		return AtlasView.resolutionValues.length-1;
	}
	
	/**
	 * Not so efficient algorithm but it enough to place letters on the atlas.
	 *  
	 * @param letterList The letters to be positioned
	 * @param atlasDim The Dimension of the atlas
	 * @return All those characters who didn't fit on the atlas
	 */
	private ArrayList<Letter> repositionLetters(ArrayList<Letter> letterList, int atlasDim) {
		ArrayList<Letter> tmpLetterList = new ArrayList<Letter>();
		
		
		for (Iterator<Letter> it = letterList.iterator(); it.hasNext(); ) {
			Letter letter = it.next();
			if(!letter.isPlaced()) { //Letter has not been positioned
				tmpLetterList.add(letter);
				it.remove();
			}
		}
		
		for (int i = 0; i < tmpLetterList.size(); i++) {
			Letter l = tmpLetterList.get(i);	
			l.setRealPositions(Letter.MIN_LETTER_DISTANCE, Letter.MIN_LETTER_DISTANCE);
			l.setPlaced(true);
			boolean overlaps = true;
			while(overlaps) {
				overlaps = false;
				for (int j = 0; j < i; j++) {
					if(tmpLetterList.get(j).isPlaced()) {
						if(tmpLetterList.get(j).isOverlapping(l)) {
							 overlaps = true;
							 break;
						 }
					}
				}
				
				if(overlaps) {
					l.setRealX(l.getRealX()+1);
					if(!l.isInXRange(atlasDim)) {;
						l.setRealPositions(Letter.MIN_LETTER_DISTANCE, l.getRealY()+1);
					}
					
				} else {
					if(!l.isInYRange(atlasDim)) {
						l.setPlaced(false);
						break;
					} else if(!l.isInXRange(atlasDim)) { //Letter is too wide for dimension
						l.setPlaced(false);
						break;
					}
				}
			}
		}
		
		letterList.addAll(tmpLetterList);
		
		for (Iterator<Letter> it = tmpLetterList.iterator(); it.hasNext(); ) {
			Letter letter = it.next();
			if(!letter.isPlaced()) { //Letter has not been positioned
				it.remove();
			}
		}
		
		return tmpLetterList;
	}
	
	 private Rectangle getStringBounds(Graphics2D g2, String str, float x, float y) {
		FontRenderContext frc = g2.getFontRenderContext();
		GlyphVector gv = g2.getFont().createGlyphVector(frc, str);
		return gv.getPixelBounds(null, x, y);
	 }
	 
	 public void save(String path, String name) {
		 export(path, name, getFontAtlasList(), getMaxTextHeight(), getMaxDescent(), 
				 getNormalCapHeight(), getWsWidth(), getSpacing(), false);
		 for (FontAtlas fontAtlas : getFontAtlasList()) {
			 fontAtlas.save(path);
	      }
	 }
	 
	 private void export(String path, String myFontName, ArrayList<FontAtlas> fontAtlasList, int maxTextHeight, int maxDescent, int normalCapHeight, int wsWidth, int spacing, boolean tolerance) {
		 String fontName  = fontAtlasList.get(0).getFont().getName();
		 int fontSize = (int) fontAtlasList.get(0).getFontSize();
		 
		 String className = myFontName;
		 
		 if (tolerance) {
			 spacing -= additional_tolerance; //To compensate that each char width will be increased by additional_tolerance
			 maxTextHeight += additional_tolerance; //To compensate that each chars height will be increased by additional_tolerance
			 maxDescent += additional_tolerance/2;	////To compensate that each char width will be increased by additional_tolerance
		 }
			 
		 
		 StringBuilder sb = new StringBuilder();
		 
		 //Imports --> DIY
		 
		 //Copyright
		 String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
		 sb.append("/**\n" +
				 	"* This class renders the font: " + fontName + " with the font size " + fontSize +  ".\n" +
	 				"* Created with the " + AtlasView.toolName + " by LDK-Productions on " + date + ".\n" +
				 	"*/\n");
		 
		 //Class definition
		 sb.append("public class " + className + " extends MyFont {\n\n");
		 
		 //Constructor
		 sb.append("	public " + className + "(Context context) {\n" +
		 			"		super(context);\n" +
		 			"	}\n\n");
		 
		 //Overwrite InitFont
		 sb.append("	@Override\n");
		 sb.append("	protected void initFont() {\n");
		 sb.append("		maxTextHeight = " + maxTextHeight + ";\n");
		 sb.append("		maxDescent = " + maxDescent + ";\n");
		 sb.append("		normalCapHeight = " + normalCapHeight + ";\n");
		 sb.append("		whitespaceWidth = " + wsWidth + ";\n");
		 sb.append("		spaceBetweenChars = " + spacing + ";\n\n");
		 for (FontAtlas fontAtlas : fontAtlasList) {
			 sb.append(fontAtlas.declareItself(tolerance)); //declare with tolerance: width and height + additional_tolerance
			 sb.append("\n");
			 sb.append("		fontAtlasList.add(atlas" + fontAtlas.getAtlasNumber() + ");\n\n");
		 }
		 sb.append("	}\n");
		 
		 //Close Class
		 sb.append("}");
		 
		 
		 //Write to
		 String fileSeparator = System.getProperty("file.separator");
		 if(!path.endsWith(fileSeparator))
			 path = path + fileSeparator;
		 try {
			PrintWriter out = new PrintWriter(path + className + ".java");
			out.print(sb.toString());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
	 }
	 
	 
	 // ---------------------- Generate Atlas Algorithm ----------------------
	 
	 class GenerateAtlas extends SwingWorker<Void, Void> {
		 
		 private  int atlasDim;
		 private Font font;
		 private Font backupFont;
		 private float fontSize;
		 private Modes mode;
		 private boolean auto;
		 
		 GenerateAtlas(int atlasDim, Font font, Font backupFont, float fontSize, Modes mode, boolean auto) {
			 this.atlasDim = atlasDim;
			 this.font = font;
			 this.fontSize = fontSize;
			 this.backupFont = backupFont;
			 this.mode = mode;
			 this.auto = auto;
		 }

		@Override
		protected Void doInBackground() throws Exception {
			
			progressMonitor.updateProgress(0);
			
			// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
		      // into integer pixels
		      BufferedImage atlas = new BufferedImage(atlasDim, atlasDim, BufferedImage.TYPE_INT_ARGB);

		      Graphics2D ig2 = atlas.createGraphics();
		      ig2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //make text smooth

		      font = font.deriveFont(fontSize);
			  if (backupFont != null) {
				  backupFont = backupFont.deriveFont(fontSize);
			  }
			  ig2.setFont(font);

		      int whitespaceWidth = ig2.getFontMetrics().charWidth(' ');

		      Rectangle maxBounds = getStringBounds(ig2, chars, 0, 0);
		      int maxHeight = maxBounds.height;
		      int maxDescent = maxHeight - (maxBounds.y);

			  int maxHeightBackup = 0;
			  int maxDescentBackup = 0;

			  if (backupFont != null) {
				  ig2.setFont(backupFont);
				  Rectangle maxBoundsBackup = getStringBounds(ig2, chars, 0, 0);
				  maxHeightBackup = maxBoundsBackup.height;
				  maxDescentBackup = maxHeightBackup - (maxBoundsBackup.y);
				  ig2.setFont(font);
			  }

		      Rectangle bI = getStringBounds(ig2, "I", 0, 0);
		      int normalCapHeight = (int) bI.getHeight();		//FontMetric.getAscent() doesn't work for all fonts so the correct value is about the size of I, T or P

		      Rectangle b = getStringBounds(ig2, "AA", 0, 0);
		      Rectangle b1 = getStringBounds(ig2, "A", 0, 0);
		      int spacing = (int) (b.getWidth()-b1.getWidth()*2);

		      System.out.println("Spacing: " + spacing + ", WhiteSpaceW:" + whitespaceWidth);
		      
		      ArrayList<Letter> letterList = new ArrayList<>();
		      
		      for (int i = 0; i < chars.length(); i++) {
		    	  char c  = chars.charAt(i);
		    	  Letter letter = new Letter(c); //letter is not placed

				  ig2.setFont(getFont(c));
			      
			      Rectangle bounds;
				  if (ig2.getFont() == backupFont) {
					  bounds = getStringBounds(ig2, new String(letter.getLetterAsArray()), 0, maxHeightBackup - maxDescentBackup);
				  } else {
					  bounds = getStringBounds(ig2, new String(letter.getLetterAsArray()), 0, maxHeight - maxDescent);
				  }

			      letter.setDiff(bounds.x, bounds.y);
			      letter.setDimensions(bounds.width, bounds.height);  
			      int spaceR = (int) (ig2.getFontMetrics().charWidth(c) - bounds.getWidth() - letter.getxDiff());
			      letter.setSpaceR(spaceR);
			      
			      letterList.add(letter);
		      }
		      
		      ArrayList<FontAtlas> fontAtlasList = new ArrayList<FontAtlas>();
		      
		      switch (mode) {
		      	case MODE_SINGLE:
		      		
					repositionLetters(letterList, atlasDim);
					
					if (!Letter.areAllPlaced(letterList)) {
		      			JOptionPane.showMessageDialog(null, "There was not enogh space to place all characters.", "Not all placed", JOptionPane.INFORMATION_MESSAGE); 
	      			}
					
					for (Iterator<Letter> iterator = letterList.iterator(); iterator.hasNext();) {
						Letter letter = (Letter) iterator.next();
						if (!letter.isPlaced())
							iterator.remove();
						
					}
					
					FontAtlas fontAtlas = new FontAtlas(letterList, atlasDim, 1, font, backupFont, fontSize, maxHeight-maxDescent, maxHeightBackup-maxDescentBackup);
					
		      		fontAtlasList.add(fontAtlas);
					
		      		break;
		      	case MODE_MULTIPLE:
		      		
		      		int countM = 1;
		      		do {
		      			ArrayList<Letter> tmpLetterListM = repositionLetters(letterList, atlasDim);
		      			FontAtlas fontAtlasM = new FontAtlas(tmpLetterListM, atlasDim, countM, font, backupFont, fontSize, maxHeight-maxDescent, maxHeightBackup-maxDescentBackup);
			      		fontAtlasList.add(fontAtlasM);
			      		
			      		progressMonitor.updateProgress((int) (Letter.getPlacedCount(letterList)/((float)(letterList.size()))*100));
						
			      		countM++;
		      		} while (!Letter.areAllPlaced(letterList));
				
		      		break;
		      	case MODE_MULTIPLE_AUTO:
		      		
		      		ArrayList<FontAtlas> tmpFontAtlasListMA = new ArrayList<FontAtlas>();
		      		for (Integer autoAtlasDim : AtlasView.resolutionValues) {
		      			int count = 0;
		      			tmpFontAtlasListMA.clear();
		      			int placed = 0;
			      		do {
			      			count++;
			      			if(count >= 4 && !(autoAtlasDim == atlasDim))
				      			break;
			      			
			      			ArrayList<Letter> tmpLetterListM = repositionLetters(letterList, atlasDim);
			      			FontAtlas fontAtlasM = new FontAtlas(tmpLetterListM, atlasDim, count, font, backupFont, fontSize, maxHeight-maxDescent, maxHeightBackup-maxDescentBackup);
			      			tmpFontAtlasListMA.add(fontAtlasM);
			      			placed += tmpLetterListM.size();
				      		
				      		progressMonitor.updateProgress((int) (placed/((float)(letterList.size()))*100));
			      			
			      		} while (!Letter.areAllPlaced(letterList));
			      		
			      		if (count < 4 || (autoAtlasDim == atlasDim))
			      			break;
			      		
			      		for (Letter l : letterList) {
			      			l.setPlaced(false);
			      		}
					}
		      		
		      		fontAtlasList.addAll(tmpFontAtlasListMA);
				
		      		break;
		      		
		      		
		      	case MODE_CUSTOM:
		      		int countC = 0;
		      		
		      		for (Integer customAtlasDim : AtlasView.customResolutionValues) {
		      			countC++;
		      			ArrayList<Letter> subList = repositionLetters(letterList, customAtlasDim);
		      			FontAtlas fontAtlasC = new FontAtlas(subList, customAtlasDim, countC, font, backupFont, fontSize, maxHeight-maxDescent, maxHeightBackup-maxDescentBackup);
		      			fontAtlasList.add(fontAtlasC);
		      			progressMonitor.updateProgress((int) (countC/((float)(AtlasView.customResolutionValues.length))*100));
		      			if (Letter.areAllPlaced(letterList)) {
		      				break;
		      			}
					}
		      		
		      		if (!Letter.areAllPlaced(letterList)) {
		      			JOptionPane.showMessageDialog(null, "There was not enogh space to place all characters.", "Not all placed", JOptionPane.INFORMATION_MESSAGE); 
	      			}
				
		      		break;
		      		
		      	case MODE_AUTO:
		      		
		      		reorderLetters(letterList); //order letters highest to smallest
		      		
		      		CalculateAtlasList: {
		      			int tmpTopMax = getPositionInValueArray(atlasDim);
		      			int working = tmpTopMax+1;
		      			int lettersComplete = 0;
		      			
		      			boolean finished = false;
		      			boolean blockW = false;
		      			
		      			while (!finished) {
		      				for (int i = 0; i<= tmpTopMax; i++) {
			      				int dim = AtlasView.resolutionValues[i];
			      				ArrayList<Letter> subList = repositionLetters(letterList, dim);
			      				int size = subList.size();
			      				int cnt = 0;
			      				if(size != 0) {
			      					
			      					for (Letter letter2 : letterList) {
										if(!letter2.isPlaced()) {
											cnt++;
										}
									}
			      				}
			      					
			      				boolean wChanged = false;
			      				if(Letter.areAllPlaced(letterList)  && !blockW) {
			      					working = i;
			      					blockW = true;
			      					wChanged = true;
			      				}
			      				if (i >= working-1 && ! wChanged) {
			      					if(size == 0) {
			      						working++;
			      						blockW = true;
			      						continue;
			      					}
			      					//keep changes
			      					FontAtlas fontAtlasA = new FontAtlas(subList, dim, 0, font, backupFont, fontSize, maxHeight-maxDescent, maxHeightBackup-maxDescentBackup);
			    	      			fontAtlasList.add(fontAtlasA);
			    	      			//working = tmpTopMax+1;  ?
			    	      			//working = i;
			    	      			
			    	      			
			    	      			//Check if there are 4 of the same size
			    	      			if (fontAtlasList.size() > 3 && i != tmpTopMax) {
			    	      				int compSize = fontAtlasA.getAtlasSize();
			    	      				int[] atlasNumber = new int[3];
			    	      				int tmp = 0;
			    	      				
			    	      				for (int index = fontAtlasList.size()-2; index >= 0 ; index--) {	//run through the list backwards without the last
			    	      					FontAtlas fontAtlasS = fontAtlasList.get(index);
			    	      					if (fontAtlasS.getAtlasSize() == compSize) {
			    	      						atlasNumber[tmp] = index;
			    	      						tmp++;
			    	      						if (tmp >= atlasNumber.length) {
			    	      							break;
			    	      						}
			    	      					}
			    	      				}
			    	      				if (tmp >= atlasNumber.length) {
			    	      					FontAtlas fontAtlas4 = fontAtlasList.get(fontAtlasList.size()-1);
				    	      				FontAtlas fontAtlas3 = fontAtlasList.get(atlasNumber[0]);
				    	      				FontAtlas fontAtlas2 = fontAtlasList.get(atlasNumber[1]);
				    	      				FontAtlas fontAtlas1 = fontAtlasList.get(atlasNumber[2]);
				    	      				
				    	      				//4 times the same so put characters on next bigger
			    	      					for (Letter letter : fontAtlas1.getCharacters()) {
					      						letter.setPlaced(false);
			    	      					}
			    	      					for (Letter letter : fontAtlas2.getCharacters()) {
					      						letter.setPlaced(false);
			    	      					}
			    	      					for (Letter letter : fontAtlas3.getCharacters()) {
					      						letter.setPlaced(false);
			    	      					}
			    	      					for (Letter letter : fontAtlas4.getCharacters()) {
					      						letter.setPlaced(false);
			    	      					}
			    	      					fontAtlasList.remove(fontAtlas4);
			    	      					fontAtlasList.remove(fontAtlas3);
			    	      					fontAtlasList.remove(fontAtlas2);
			    	      					fontAtlasList.remove(fontAtlas1);
				    	      				
				    	      				working++;
				    	      				blockW = true;
				    	      				continue;
			    	      				}    	  		
			    	      			}
			    	      			
			    	      			working = tmpTopMax+1;
			    	      			blockW = false;
				      				
			    	      			lettersComplete += size;
			    	      			
			    	      			progressMonitor.updateProgress((int) (lettersComplete/((float)(letterList.size()))*100));
			    	      			
			      						
			    	      			if(Letter.areAllPlaced(letterList))
			    	      				break CalculateAtlasList;
			      					break;
			      				} else {
			      					//undo changes
			      					for (Letter letter : subList) {
			      						letter.setPlaced(false);
									}
			      					if(wChanged)
			      						break;
			      				}
			      			}
		      			}
		      		}
		      	
		      	//renumber
		      	for (int j = 0; j < fontAtlasList.size(); j++) {
					fontAtlasList.get(j).setAtlasNumber(j+1);
					
				}
		      	
		      	break;
		      }
		      
		      for (FontAtlas fontAtlas2 : fontAtlasList) {
		    	  fontAtlas2.show(context);
		    	  System.out.println(fontAtlas2.declareItself(false));
		      }
		      
		      System.out.println("\nMaxH: " + maxHeight + " MaxD:" + maxDescent + " normA:" + normalCapHeight);
			
		      setValues(font.getName(), fontAtlasList, maxHeight, maxDescent, normalCapHeight, whitespaceWidth, spacing);
			
		      /*
		      drawTestImage("AA Play", font, true);
		      drawTestImage("Johann dujohann", font, true);
		      drawTestImage("for the first xDD", font, true);
		      */
		      
		      progressMonitor.updateProgress(100);
			
			return null;
		}

		 private Font getFont(char c) {
			 if (!font.canDisplay(c) && backupFont != null) {
				 return backupFont;
			 }
			 return font;
		 }
		 
	 }
	 
	 
	 
	 //---------------------------------- Test ----------------------------------------
	
	private void drawTestImage(String s, Font font, boolean highliteBounds) {
			// TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
			// into integer pixels
			BufferedImage testImage = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = testImage.createGraphics();
			
		    g2d.setFont(font);
		    
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //make text smooth
			
			draw(s, g2d, highliteBounds);
			
			MyImageView.shwoImageModeless(context, testImage, Color.WHITE, null);
		}
		
	 
	public void draw(String s, Graphics2D g2d, boolean highliteBounds) {
		 
		int x = 64;
		int y = 64;
	 
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			Letter letter = null;
			
			if (c == ' ') {
				x += wsWidth;
				continue;
			} else if (c == '\t') {
				x += 8 * wsWidth;
				continue;
			}
			
			InitLetter: {
				for (FontAtlas fontAtlas : fontAtlasList) {
					for (Letter l : fontAtlas.getCharacters()) {
						if (l.getLetter() == c) {
							letter = l;
							letter.setRealPositions(0, letter.getyDiff());
							break InitLetter;
						}
					}
				}
				continue;
			}
			
			if (highliteBounds) {
				g2d.setPaint(Color.GREEN);
				g2d.drawRect(x + letter.getRealX() - letter.getxDiff(), y + letter.getRealY(), letter.getWidth(), letter.getHight());
				
				/*
				g2d.setPaint(Color.RED);
				g2d.drawRect(x + letter.getRealX() - letter.getxDiff(), y + letter.getRealY(), letter.getWidth()+letter.getSpaceR()+letter.getxDiff(), letter.getHight());
				*/
			}
			
			g2d.setPaint(Color.black);
			
			g2d.drawChars(letter.getLetterAsArray(), 0, 1, x + letter.getRealX(), y + letter.getDrawY() + fontAtlasList.get(0).getDrawingOffset());
			
			x += letter.getWidth() + letter.getxDiff() + letter.getSpaceR();
		}
		
		g2d.drawString(s, 64, y + maxTextHeight + 20 + fontAtlasList.get(0).getDrawingOffset());

	}
	 
}
