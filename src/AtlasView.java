
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This program will generate a font atlas of a font, which you will be able to use.
 * @author LDK-Productions
 */
public class AtlasView extends JFrame implements ActionListener, ProgressMonitorView{

	private static final long serialVersionUID = 1L;
	private static final String version = "V1.0";
	public static final String toolName = "Texture Font Atlas - Tool";
	
	//All pre-definded characters
	private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String frenchChars = "ÀÂÆÇÈÉÊËÎÏÔŒÙÛÜŸàâæçèéêëîïôœùûüÿ";
	private static final String germanChars = "ÄÖÜäöüß";
	private static final String allNumbers = "0123456789";
	private static final String simpleMaths = "+-*/:÷.,=";
	private static final String advancedMaths = "<>%≠#^±≥≤π∞";
	private static final String commonPunctuationMarks = ".,\"„“!?;:-_'()*";
	private static final String morePunctuationMarksAndOthers = "&$€@#°»«–—·/\\+[]<>{}=…|´";
	//private static final String spanischChars = "¡¿";
	//private static final String moreCharacters = "→";
	
	//GUI - Elements
	private JLabel lblCharactersHead = new JLabel("<html>&nbsp;<b>Characters:</b>&nbsp;</html>");
	private JLabel lblCharacters = new JLabel("<html>&nbsp;ABCDEFGHIJKLMNOPQRSTUVWXYZ<br>&nbsp;abcdefghijklmnopqrstuvwxyz&nbsp;</html>");
	private JButton butEditChars = new JButton("Edit characters");
	private final String[] cobResolutionStrings = { "32x32", "64x64", "128x128", "256x256", "512x512", "1024x1024", "2048x2048" , "4096x4096",  "Auto"};
	private JComboBox<String> cobResolution = new JComboBox<String>(cobResolutionStrings);
	private JComboBox<String> cobFonts;
	private JComboBox<String> cobBackupFonts;
	private JTextField txtSize = new JTextField();
	private JButton butRun = new JButton("Run");
	private JButton butSave = new JButton("Save");
	private static final String butSaveToolTipEnabled = "Saves the font class and the font atlas images in a directory you choose.";
	private static final String butSaveToolTipDisabled = "Values changed, click run again!";
	private JRadioButton rbSingle = new JRadioButton("Single");
	private JRadioButton rbMultiple = new JRadioButton("Multiple");
	private JRadioButton rbMultipleAuto = new JRadioButton("Auto Multiple");
	private JRadioButton rbAuto = new JRadioButton("Auto");
	private JRadioButton rbCustom = new JRadioButton("Custom (recommended)");
	
	private JLabel lblResolution = new JLabel(" Max resolution: ");
	private JLabel lblAtlasCount = new JLabel(" Number of pictures to use: ");
	private JLabel lblFont = new JLabel(" Font: ");
	private JLabel lblBackupFont = new JLabel(" Backup Font: ");
	private JLabel lblFontSize = new JLabel(" Font - size: ");
	
	private JLabel lblDistance0 = new JLabel(" ");
	private JLabel lblDistance1 = new JLabel(" ");
	private JLabel lblDistance2 = new JLabel(" ");
	private JLabel lblDistance3 = new JLabel(" ");
	private JLabel lblDistance4 = new JLabel(" ");
	private JLabel lblDistance5 = new JLabel(" ");
	
	//Dialog edit chars
	private JDialog editCharsDialog = new JDialog(this, "Edit characters", true);
	private JButton butAddFromFile = new JButton("Add all characters from a file...");
	private JButton butAddAlphabet = new JButton("Add alphabet");
	private JButton butAddGermanChars = new JButton("Add special German characters");
	private JButton butAddFrenchChars = new JButton("Add special French characters");
	private JButton butAddNumbers = new JButton("Add numbers");
	private JButton butAddSimpleMaths = new JButton("Add simple math symbols");
	private JButton butAddAdvancedMaths = new JButton("Add advanced math symbols");
	private JButton butAddCommonPunctuationMarks = new JButton("Add common punctuation marks");
	private JButton butAddMorePunctuationMarksAndOthers = new JButton("Add more punctuation marks and other");
	private JButton butEditClear = new JButton("Clear");
	private JLabel lblEditCharactersHead = new JLabel(" Used characters: ");
	private JTextArea textAreaChars = new JTextArea();
	private JScrollPane cpTextAreaChars = new JScrollPane(textAreaChars);
	private JButton butEditCancel = new JButton("Cancel");
	private JButton butEditSave = new JButton("Save");
	
	//Dialog custom mode
	private JButton butEditRes = new JButton("Edit custom resolution list");
	private JLabel lblCustomResListMain = new JLabel(" List is empty, please edit the list! ");
	private JDialog editCustomResDialog = new JDialog(this, "Edit resolutions", true);
	private final String[] cobCustomResStirngs = { "32x32", "64x64", "128x128", "256x256", "512x512", "1024x1024", "2048x2048" , "4096x4096"};
	private JButton butCustomResAdd = new JButton("Add");
	private JButton butCustomResClear = new JButton("Clear");
	private JComboBox<String> cobCustomRes = new JComboBox<String>(cobCustomResStirngs);
	private JLabel lblCustomResListHead = new JLabel(" Custom resolution list: ");
	private JLabel lblCustomResList = new JLabel();
	private JButton butCustomResCancel = new JButton("Cancel");
	private JButton butCustomResSave = new JButton("Save");
	
	//Dialog export (save)
	private JDialog exportDialog = new JDialog(this, "Save everything", true);
	private JLabel lblExportName = new JLabel(" Name: ");
	private JTextField txtExportName = new JTextField();
	private JCheckBox cbExportName = new JCheckBox("If checked name will be modified to: Font[Name]fontsize");
	private JLabel lblExportNamePreview = new JLabel();
	private JLabel lblExportPath = new JLabel(" Path of destination folder: ");
	private JTextField txtExportPath = new JTextField();
	private JButton butExportPath = new JButton("Change");
	private JLabel lblExportOS = new JLabel(" Source code target: ");
	private JRadioButton rbExportAndroid = new JRadioButton("Android (Java)");
	private JRadioButton rbExportIOS = new JRadioButton("IOS (Objective-C)");
	private JButton butExportCancel = new JButton("Cancel");
	private JButton butExportSave = new JButton("Save");

	
	//Progrss bar
	private ProgressMonitor progressMonitor;
	
	public static final Integer[] resolutionValues = {32,64,128,256,512,1024,2048,4096}; // the values form cobResolutionStrings as Integers
	public static Integer[] customResolutionValues = null;
	private Integer[] customResolutionValuesBackup = null;

	private String chars = alphabet;
			// Test String :"QWERTZUIOPÜ+ASDFGHJKLÖÄ#<>YXCVBNM,.-1234567890ßmqwertzuiopüasdfghjklöäyxcvbn;:_'*!\"§$€%&/())=?ß@[]|{}`´°^"; 
			//Erst hoehe, dann breite dann rest
	
	private FullFontAtlas fullAtlasTmp = null;
	
	
	public AtlasView() {
		super(toolName + " " + version + "  (© by LDK-Productions)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationByPlatform(true);
		
	    initGUI();
	    
	    chars = "";
	    chars = addChars(chars, "&$€@#°–—·/\\+<>.,\"„“!?;:-_'()*%^=0123456789ÀÂÆÇÈÉÊËÎÏÔŒÙÛÜŸàâæçèéêëîïôœùûüÿÄÖäößABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	    /*
	    chars = addChars(chars, alphabet);
	    chars = addChars(chars, germanChars);
	    chars = addChars(chars, frenchChars);
	    chars = addChars(chars, allNumbers);
	    chars = addChars(chars, simpleMaths);
	    chars = addChars(chars, commonPunctuationMarks);
	    */
	    updateLblCharacters();
		
		this.pack();
		//this.setLocationRelativeTo(null); //This would center it but that will make the dialogs appear before it...
	}
	
	private void initGUI() {
		//Get all system fonts and fill JCombobox
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    String[] fontNames = new String[fonts.length];
	    
	    for (int i = 0; i < fonts.length; i++) {
			fontNames[i] = fonts[i].getName();
	    }
	    
	    cobFonts = new JComboBox<String>(fontNames);
	    cobBackupFonts = new JComboBox<String>(fontNames);
	    cobResolution.setSelectedIndex(4); //set default to 512x512
	    
	    butSave.setToolTipText(butSaveToolTipDisabled);
	    
	    //addActionListeners
	    butRun.addActionListener(this);
	    butSave.addActionListener(this);
	    rbAuto.addActionListener(this);
	    rbSingle.addActionListener(this);
	    rbMultiple.addActionListener(this);
	    rbMultipleAuto.addActionListener(this);
	    rbCustom.addActionListener(this);
	    
	    cobResolution.addActionListener(this);
	    cobFonts.addActionListener(this);
	    
	    butEditChars.addActionListener(this);
	    butEditRes.addActionListener(this);
	    
	    txtSize.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					changed();					
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					changed();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					changed();
				}
				
				private void changed() {
					closeImageViews();
					fullAtlasTmp = null;
					butSave.setEnabled(false);
					butSave.setToolTipText(butSaveToolTipDisabled);
				}
			
	    	});
	    
	    txtSize.setText("128"); //Test default
	    
	    initRadioButtons();
	    
	    initEditCharsDialog();
	    initCustomResolutionDialog();
	    initExportDialog();
	    
	    lblCustomResListMain.setVisible(false);
		butEditRes.setVisible(false);
	    
	    //Close all image views when focus gained or form clicked 
	    this.addMouseListener(new MouseAdapter() {
			private boolean mouseInside = true;
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseInside = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseInside = true;
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (mouseInside) {
					closeImageViews();
				}
			}
		});
	    
		setPositions();
		
		progressMonitor = new ProgressMonitor(this, "Generating Font Atlas", "", 0, 100);
		progressMonitor.setProgress(0);
	}
	
	private void closeImageViews() {
		Window[] windows = AtlasView.this.getOwnedWindows();
		for (int i = 0; i < windows.length; i++) {
			if(windows[i].getName().equals(MyImageView.class.getName()+"Dialog")) {
				((JDialog) windows[i]).dispose();
			}
		}
	}
	
	private void setPositions() {
		setPositions(lblCharactersHead, 0, 1, 4, 1, 0, 0);
		setPositions(lblCharacters, 0, 2, 4, 1, 0, 0);
		setPositions(butEditChars, 1, 3, 3, 1, 0, 0);
		setPositions(lblDistance0, 0, 4, 0, 1);
		setPositions(lblResolution, 0, 5, 1, 1, 0, 0);
		setPositions(cobResolution, 1, 5, 3, 1, 0, 0);
		setPositions(lblCustomResListMain, 0, 6, 4, 1, 0, 0);
		setPositions(butEditRes, 1, 7, 3, 1, 0, 0);
		setPositions(lblDistance1, 0, 8, 0, 1);
		setPositions(lblAtlasCount, 0, 9, 4, 1, 0, 1);
		setPositions(rbAuto, 1, 10, 1, 1, 0, 0);
		setPositions(rbCustom, 2, 10, 2, 1, 0, 0);
		setPositions(rbSingle, 1, 11);
		setPositions(rbMultiple, 2, 11);
		setPositions(rbMultipleAuto, 3, 11);
		setPositions(lblDistance2, 0, 12, 0, 1);
		setPositions(lblFont, 0, 13);
		setPositions(cobFonts, 1, 13, 3, 1, 0, 0);
		setPositions(lblBackupFont, 0, 14);
		setPositions(cobBackupFonts, 1, 14, 3, 1, 0, 0);
		setPositions(lblDistance3, 0, 15, 0, 1);
		setPositions(lblFontSize, 0, 16);
		setPositions(txtSize, 1, 16, 2, 1, 0, 0);
		setPositions(lblDistance4, 0, 17, 0, 1);
		setPositions(butRun, 1, 18, 3, 1, 1, 1);
		setPositions(lblDistance5, 0, 19, 0, 1);
		setPositions(butSave, 1, 20, 3, 1, 1, 1);
	}
	
	private void initRadioButtons() {		 
	    rbAuto.setSelected(true);

	    ButtonGroup group = new ButtonGroup();
	    group.add(rbSingle);
	    group.add(rbMultiple);
	    group.add(rbMultipleAuto);
	    group.add(rbAuto);
	    group.add(rbCustom);
	}
	
	private void initEditCharsDialog() {
		setPositionsDialogAddChars(editCharsDialog);
		
		String preText = "Adds: ";
		butAddFromFile.setToolTipText("Adds all used characters of a file");
		butAddAlphabet.setToolTipText(preText + alphabet);
		butAddGermanChars.setToolTipText(preText + germanChars);
		butAddFrenchChars.setToolTipText(preText + frenchChars);
		butAddNumbers.setToolTipText(preText + allNumbers);
		butAddSimpleMaths.setToolTipText(preText + simpleMaths);
		butAddAdvancedMaths.setToolTipText(preText + advancedMaths);
		butAddCommonPunctuationMarks.setToolTipText(preText + commonPunctuationMarks);
		butAddMorePunctuationMarksAndOthers.setToolTipText(preText + morePunctuationMarksAndOthers);
		butEditClear.setToolTipText("Clears all characters");
		
		butAddFromFile.addActionListener(this);
		butAddAlphabet.addActionListener(this);
		butAddGermanChars.addActionListener(this);
		butAddFrenchChars.addActionListener(this);
		butAddNumbers.addActionListener(this);
		butAddSimpleMaths.addActionListener(this);
		butAddAdvancedMaths.addActionListener(this);
		butAddCommonPunctuationMarks.addActionListener(this);
		butAddMorePunctuationMarksAndOthers.addActionListener(this);
		butEditClear.addActionListener(this);
		butEditCancel.addActionListener(this);
		butEditSave.addActionListener(this);
		
		textAreaChars.setToolTipText("<html>The list of available Unicode characters for the font.<br><b>Dublicates and whitespace characters will be ignored!</b></html>");
		butEditSave.setToolTipText("<html>Replaces the character list with your used character list.<br><b>Dublicates and whitespace characters will be ignored!</b></html>");
		
		editCharsDialog.setMinimumSize(new Dimension(330, 500));
		editCharsDialog.pack();
		editCharsDialog.setLocationRelativeTo(null);
		editCharsDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	private void setPositionsDialogAddChars(JDialog dialog) {
		// place(posGridX,posGidY,GridAnzahlX,GridAnzahlY,WeightX,WeightY);
		Container cp = dialog.getContentPane();
		
		GridBagLayout gbl = new GridBagLayout();
		dialog.setLayout(gbl);
		GridBagConstraints gbc;
		
		gbc = place(0, 1, 2, 1, 0, 0);
		gbl.setConstraints(butAddFromFile, gbc);
		cp.add(butAddFromFile);
		
		gbc = place(0, 2, 2, 1, 0, 0);
		gbl.setConstraints(butAddAlphabet, gbc);
		cp.add(butAddAlphabet);
		
		gbc = place(0, 3, 2, 1, 0, 0);
		gbl.setConstraints(butAddGermanChars, gbc);
		cp.add(butAddGermanChars);
		
		gbc = place(0, 4, 2, 1, 0, 0);
		gbl.setConstraints(butAddFrenchChars, gbc);
		cp.add(butAddFrenchChars);
		
		gbc = place(0, 5, 2, 1, 0, 0);
		gbl.setConstraints(butAddNumbers, gbc);
		cp.add(butAddNumbers);
		
		gbc = place(0, 6, 2, 1, 0, 0);
		gbl.setConstraints(butAddSimpleMaths, gbc);
		cp.add(butAddSimpleMaths);
		
		gbc = place(0, 7, 2, 1, 0, 0);
		gbl.setConstraints(butAddAdvancedMaths, gbc);
		cp.add(butAddAdvancedMaths);
		
		gbc = place(0, 8, 2, 1, 0, 0);
		gbl.setConstraints(butAddCommonPunctuationMarks, gbc);
		cp.add(butAddCommonPunctuationMarks);
		
		gbc = place(0, 9, 2, 1, 0, 0);
		gbl.setConstraints(butAddMorePunctuationMarksAndOthers, gbc);
		cp.add(butAddMorePunctuationMarksAndOthers);
		
		gbc = place(0, 10, 2, 1, 0, 0);
		gbl.setConstraints(butEditClear, gbc);
		cp.add(butEditClear);
		
		gbc = place(0, 11, 2, 1, 0, 0);
		gbl.setConstraints(lblEditCharactersHead, gbc);
		cp.add(lblEditCharactersHead);
		
		gbc = place(0, 12, 2, 1, 1, 1);
		gbl.setConstraints(cpTextAreaChars, gbc);
		cp.add(cpTextAreaChars);
		
		gbc = place(0, 13, 1, 1, 1, 0);
		gbl.setConstraints(butEditCancel, gbc);
		cp.add(butEditCancel);
		
		gbc = place(1, 13, 1, 1, 1, 0);
		gbl.setConstraints(butEditSave, gbc);
		cp.add(butEditSave);
	}
	
	private void initCustomResolutionDialog() {
		setPositionsDialogCustomRes(editCustomResDialog);
		
		butCustomResAdd.addActionListener(this);
		butCustomResClear.addActionListener(this);
		butCustomResCancel.addActionListener(this);
		butCustomResSave.addActionListener(this);
		
		updateCustomResolutionList(false);
		
		editCustomResDialog.setMinimumSize(new Dimension(330, 300));
		editCustomResDialog.pack();
		editCustomResDialog.setLocationRelativeTo(null);
		editCustomResDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
	}
	
	private void updateCustomResolutionList(boolean mainMenue) {
		if (customResolutionValues != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("<html>");
			for (int i = 0; i<customResolutionValues.length; i++ ) {
				int size = customResolutionValues[i];
				if (mainMenue) {
					sb.append("&nbsp;").append(size).append("x").append(size);
					if (i != customResolutionValues.length-1) {
						sb.append(",&nbsp;");
						if ((i+1)%4 == 0)
							sb.append("<br>");
					}
				} else 
					sb.append("&nbsp;").append(size).append("x").append(size).append("&nbsp;<br>");
			}
			sb.append("</html>");
			if (mainMenue)
				lblCustomResListMain.setText(sb.toString());
			else
				lblCustomResList.setText(sb.toString());
		} else {
			if(mainMenue)
				lblCustomResListMain.setText(" List is empty, please edit the list! ");
			else
				lblCustomResList.setText("");
		}
	}
	
	private void setPositionsDialogCustomRes(JDialog dialog) {
		// place(posGridX,posGidY,GridAnzahlX,GridAnzahlY,WeightX,WeightY);
		Container cp = dialog.getContentPane();
		
		GridBagLayout gbl = new GridBagLayout();
		dialog.setLayout(gbl);
		GridBagConstraints gbc;
		
		gbc = place(0, 1, 1, 1, 0, 0);
		gbl.setConstraints(cobCustomRes, gbc);
		cp.add(cobCustomRes);
		
		gbc = place(1, 1, 1, 1, 0, 0);
		gbl.setConstraints(butCustomResAdd, gbc);
		cp.add(butCustomResAdd);
		
		gbc = place(1, 2, 1, 1, 0, 0);
		gbl.setConstraints(butCustomResClear, gbc);
		cp.add(butCustomResClear);
		
		gbc = place(0, 3, 2, 1, 0, 1);
		gbl.setConstraints(lblCustomResListHead, gbc);
		cp.add(lblCustomResListHead);
		
		gbc = place(0, 4, 2, 1, 0, 1);
		gbl.setConstraints(lblCustomResList, gbc);
		cp.add(lblCustomResList);
		
		gbc = place(0, 5, 1, 1, 1, 0);
		gbl.setConstraints(butCustomResCancel, gbc);
		cp.add(butCustomResCancel);
		
		gbc = place(1, 5, 1, 1, 1, 0);
		gbl.setConstraints(butCustomResSave, gbc);
		cp.add(butCustomResSave);
	}
	
	private void initExportDialog() {
		setPositionsDialogExport(exportDialog);
		
		cbExportName.setSelected(true);
		
		butExportPath.addActionListener(this);
		butExportCancel.addActionListener(this);
		butExportSave.addActionListener(this);
		cbExportName.addActionListener(this);
		rbExportAndroid.addActionListener(this);
		rbExportIOS.addActionListener(this);
		
		rbExportIOS.setEnabled(false);
		
		txtExportName.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateExportNamePreview(false);
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateExportNamePreview(false);
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateExportNamePreview(false);
			}
			
		});
		
		rbExportAndroid.setSelected(true);

	    ButtonGroup group = new ButtonGroup();
	    group.add(rbExportAndroid);
	    group.add(rbExportIOS);
		
		exportDialog.setMinimumSize(new Dimension(400, 245));
		exportDialog.pack();
		exportDialog.setLocationRelativeTo(null);
		exportDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	private String updateExportNamePreview(boolean readOnly) {
		String className;
		if (cbExportName.isSelected()) {
			className = "Font" + txtExportName.getText() + ((int)fullAtlasTmp.getFontAtlasList().get(0).getFontSize());
		} else {
			className = txtExportName.getText();
		}
		if(!readOnly)
			lblExportNamePreview.setText("Class name: " + className);
		return className;
	}
	
	private void setPositionsDialogExport(JDialog dialog) {
		// place(posGridX,posGidY,GridAnzahlX,GridAnzahlY,WeightX,WeightY);
		Container cp = dialog.getContentPane();
		
		GridBagLayout gbl = new GridBagLayout();
		dialog.setLayout(gbl);
		GridBagConstraints gbc;
		
		gbc = place(0, 1, 4, 1, 0, 0);
		gbl.setConstraints(lblExportName, gbc);
		cp.add(lblExportName);
		
		gbc = place(0, 2, 2, 1, 0, 0);
		gbl.setConstraints(txtExportName, gbc);
		cp.add(txtExportName);
		
		gbc = place(2, 2, 2, 1, 0, 0);
		gbl.setConstraints(lblExportNamePreview, gbc);
		cp.add(lblExportNamePreview);
		
		gbc = place(0, 3, 4, 1, 0, 0);
		gbl.setConstraints(cbExportName, gbc);
		cp.add(cbExportName);
		
		gbc = place(0, 4, 4, 1, 0, 0);
		gbl.setConstraints(lblExportPath, gbc);
		cp.add(lblExportPath);
		
		gbc = place(0, 5, 3, 1, 0, 0);
		gbl.setConstraints(txtExportPath, gbc);
		cp.add(txtExportPath);
		
		gbc = place(3, 5, 1, 1, 0, 0);
		gbl.setConstraints(butExportPath, gbc);
		cp.add(butExportPath);
		
		gbc = place(0, 6, 4, 1, 0, 0);
		gbl.setConstraints(lblExportOS, gbc);
		cp.add(lblExportOS);
		
		gbc = place(0, 7, 4, 1, 0, 0);
		gbl.setConstraints(rbExportAndroid, gbc);
		cp.add(rbExportAndroid);
		
		gbc = place(0, 8, 4, 1, 0, 0);
		gbl.setConstraints(rbExportIOS, gbc);
		cp.add(rbExportIOS);
		
		gbc = place(0, 9, 2, 1, 1, 0);
		gbl.setConstraints(butExportCancel, gbc);
		cp.add(butExportCancel);
		
		gbc = place(2, 9, 2, 1, 1, 0);
		gbl.setConstraints(butExportSave, gbc);
		cp.add(butExportSave);
	}
	
	
	@Override
	public boolean updateProgress(int progressPercentage) {
		if (progressMonitor.isCanceled() && progressPercentage != 0) {
			fullAtlasTmp = null;
			return false;
		}
		String message = String.format("Completed %d%%.\n", progressPercentage);
		progressMonitor.setNote(message);
		progressMonitor.setProgress(progressPercentage);
		if (progressPercentage >= 100) {
			progressMonitor.close();
		}
		return true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		closeImageViews(); //close ImageViews if open
		if (e.getSource().equals(butRun)) {
			if (checkValues()) {	//check if all input is correct and start calculation
				butSave.setEnabled(true);
				butSave.setToolTipText(butSaveToolTipEnabled);
			}
		} else if (e.getSource().equals(butSave)) {
			if (fullAtlasTmp != null) {
				updateExportNamePreview(false);
				exportDialog.setVisible(true);
			 } else {
				 JOptionPane.showMessageDialog(null, "Values changed, click run again!", "Can't export", JOptionPane.INFORMATION_MESSAGE);
			 }
		} else if (e.getSource().equals(rbAuto) ||  e.getSource().equals(rbMultipleAuto)) {
			if ((((JRadioButton) e.getSource()).isSelected())) {
				lblResolution.setText(" Max resolution: ");
				cobResolution.setSelectedIndex(4);
				cobResolution.setVisible(true);
				lblCustomResListMain.setVisible(false);
				butEditRes.setVisible(false);
				this.pack();
			}
			fullAtlasTmp = null;
			butSave.setEnabled(false);
			butSave.setToolTipText(butSaveToolTipDisabled);
		} else if (e.getSource().equals(rbSingle) || e.getSource().equals(rbMultiple)) {
			if((((JRadioButton) e.getSource()).isSelected())) {
				lblResolution.setText("      Resolution: ");
				if (cobResolution.getSelectedIndex() == cobResolutionStrings.length-1)
					cobResolution.setSelectedIndex(4);
				cobResolution.setVisible(true);
				lblCustomResListMain.setVisible(false);
				butEditRes.setVisible(false);
				this.pack();
			}
			fullAtlasTmp = null;
			butSave.setEnabled(false);
			butSave.setToolTipText(butSaveToolTipDisabled);
		} else if (e.getSource().equals(rbCustom)) {
			if (rbCustom.isSelected()) {
				lblResolution.setText("      Resolutions: ");
				cobResolution.setVisible(false);
				lblCustomResListMain.setVisible(true);
				butEditRes.setVisible(true);
				this.pack();
			}
			fullAtlasTmp = null;
			butSave.setEnabled(false);
			butSave.setToolTipText(butSaveToolTipDisabled);
		} else if (e.getSource() instanceof JComboBox){
			fullAtlasTmp = null;
			butSave.setEnabled(false);
			butSave.setToolTipText(butSaveToolTipDisabled);
		} else if (e.getSource().equals(butEditChars)) {
			textAreaChars.setText(chars);
			editCharsDialog.setVisible(true);
		} else if (e.getSource().equals(butAddFromFile)) {
			File file = chooseFile(false);
			if (file != null) {
				try {
					String text = getFileText(file);
					textAreaChars.setText(addChars(textAreaChars.getText(), text));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getSource().equals(butAddAlphabet)) {
			textAreaChars.setText(alphabet + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0); //makes the scroll bar to scroll to top left
		} else if (e.getSource().equals(butAddGermanChars)) {
			textAreaChars.setText(germanChars + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddFrenchChars)) {
			textAreaChars.setText(frenchChars + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddNumbers)) {
			textAreaChars.setText(allNumbers + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddSimpleMaths)) {
			textAreaChars.setText(simpleMaths + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddAdvancedMaths)) {
			textAreaChars.setText(advancedMaths + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddCommonPunctuationMarks)) {
			textAreaChars.setText(commonPunctuationMarks + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butAddMorePunctuationMarksAndOthers)) {
			textAreaChars.setText(morePunctuationMarksAndOthers + "\n" + textAreaChars.getText());
			textAreaChars.setCaretPosition(0);
		} else if (e.getSource().equals(butEditClear)) {
			textAreaChars.setText("");
		} else if (e.getSource().equals(butEditCancel)) {
			editCharsDialog.dispose();
		} else if (e.getSource().equals(butEditSave)) {
			chars = addChars(new String(), textAreaChars.getText());
			updateLblCharacters();
			fullAtlasTmp = null;
			butSave.setEnabled(false);
			butSave.setToolTipText(butSaveToolTipDisabled);
			editCharsDialog.dispose();
			this.pack();
		} else if (e.getSource().equals(butEditRes)) {
			customResolutionValuesBackup = customResolutionValues;
			editCustomResDialog.pack();
			editCustomResDialog.setVisible(true);
		} else if (e.getSource().equals(butCustomResAdd)) {
			int size = resolutionValues[cobCustomRes.getSelectedIndex()];
			if (customResolutionValues != null) {
				Integer[] newList = new Integer[customResolutionValues.length+1];
				for (int i = 0; i < customResolutionValues.length; i++) {
					newList[i] = customResolutionValues[i];
				}
				newList[newList.length-1] = size;
				Arrays.sort(newList, Collections.reverseOrder());
				customResolutionValues = newList;
				updateCustomResolutionList(false);
			} else {
				customResolutionValues = new Integer[1];
				customResolutionValues[0] = size;
				updateCustomResolutionList(false);
			}
			editCustomResDialog.pack();
		} else if (e.getSource().equals(butCustomResClear)) {
			customResolutionValues = null;
			updateCustomResolutionList(false);
			editCustomResDialog.pack();
		} else if (e.getSource().equals(butCustomResCancel)) {
			customResolutionValues = customResolutionValuesBackup;
			editCustomResDialog.dispose();
		} else if (e.getSource().equals(butCustomResSave)) {
			editCustomResDialog.dispose();
			updateCustomResolutionList(true);
			this.pack();
		}
		
		// ------------ Export dialog -----------------//
		
		else if (e.getSource().equals(cbExportName)) {
			updateExportNamePreview(false);
		} else if (e.getSource().equals(butExportPath)) {
			File path = chooseFile(true);
			if (path != null) {
				txtExportPath.setText(path.getPath());
			}
		} else if (e.getSource().equals(rbExportAndroid)) {
		} else if (e.getSource().equals(rbExportIOS)) {
		} else if (e.getSource().equals(butExportCancel)) {
			exportDialog.dispose();
		} else if (e.getSource().equals(butExportSave)) {
			exportDialog.dispose();
			fullAtlasTmp.save(txtExportPath.getText(), updateExportNamePreview(true));
		}
	}
	

	
	private void updateLblCharacters() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		for (int i = 0; i < chars.length(); i=i+26) {
			int end = i+26;
			if(end > chars.length())
				end = chars.length();
			sb.append("&nbsp;" + chars.subSequence(i, end) + "&nbsp;<br>");
		}
		sb.append("</html>");
		lblCharacters.setText(sb.toString());
	}
	
	
	public static Font getFontByName(String name) {
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    for (int i = 0; i < fonts.length; i++) {
			if(fonts[i].getName().equals(name))
				return fonts[i];
		}
	    return null;
	}
	
	private String getFileText(File file) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while((line = in.readLine()) != null)  {
			sb.append(line).append('\n');
		}
		return sb.toString();
	}
	
	private File chooseFile(boolean justDir) {
		JFileChooser fc = new JFileChooser();
		if(justDir)
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		else
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
		return null;
	}
	
	private boolean checkValues() {
		int atlasDim;
		boolean auto = false;
		
		if (cobResolution.getSelectedIndex() == cobResolutionStrings.length-1) { //If Auto
			if (rbMultiple.isSelected() || rbSingle.isSelected()) { 
				JOptionPane.showMessageDialog(null, "Can't choose Auto-Resolution in Single or Multiple mode", "Error", JOptionPane.ERROR_MESSAGE); 
				return false;
			}
			atlasDim = resolutionValues[resolutionValues.length-1];
			auto = true;
		} else {
			atlasDim = resolutionValues[cobResolution.getSelectedIndex()];
		}
		
		if (rbCustom.isSelected()) {
			if (customResolutionValues == null || customResolutionValues.length == 0) {
				JOptionPane.showMessageDialog(null, "Custom list is empty, please edit the list!", "Error", JOptionPane.ERROR_MESSAGE); 
				return false;
			} else {
				Arrays.sort(customResolutionValues, Collections.reverseOrder());
			}
		}
		
		Font font = getFontByName((String) cobFonts.getSelectedItem());
		if (font == null) {
			JOptionPane.showMessageDialog(null, "Font not found", "Error", JOptionPane.ERROR_MESSAGE); 
			return false;
		}

		Font backupFont = getFontByName((String) cobBackupFonts.getSelectedItem());
		
		float fontSize;
		try {
			fontSize = Float.parseFloat(txtSize.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error while reading font size!\nOnly use numbers and use a dot as decimal mark!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		
		//Start calculation based on selected mode
		if(rbSingle.isSelected()) {
			fullAtlasTmp = new FullFontAtlas(chars, atlasDim, font, backupFont, fontSize, FullFontAtlas.Modes.MODE_SINGLE, auto, this);
		} else if(rbMultiple.isSelected()) {
			fullAtlasTmp = new FullFontAtlas(chars, atlasDim, font, backupFont, fontSize, FullFontAtlas.Modes.MODE_MULTIPLE, auto, this);
		} else if(rbMultipleAuto.isSelected()) {
			fullAtlasTmp = new FullFontAtlas(chars, atlasDim, font, backupFont, fontSize, FullFontAtlas.Modes.MODE_MULTIPLE_AUTO, auto, this);
		} else if(rbCustom.isSelected()) {
			fullAtlasTmp = new FullFontAtlas(chars, atlasDim, font, backupFont, fontSize, FullFontAtlas.Modes.MODE_CUSTOM, auto, this);
		} else {
			fullAtlasTmp = new FullFontAtlas(chars, atlasDim, font, backupFont, fontSize, FullFontAtlas.Modes.MODE_AUTO, auto, this);
		}
		return true;
	}
	
	private String addChars(String target, String source) {
		char[] cs = source.toCharArray();
		for (int i=0; i<cs.length; i++) {
			char c = cs[i];
			if(target.indexOf(c) < 0  &&  source.indexOf(c) == i && ! Character.isWhitespace(c))
				target = target + c;
		}
		return target;
	}
	
	
	// ----------------------------------------- GUI position functions ----------------------------------------- //
	
		private void setPositions(JComponent component, int gridx, int gridy, int gridwidth,
				int gridheight, int weightx, int weighty) {
			// place(posGridX,posGidY,GridAnzahlX,GridAnzahlY,WeightX,WeightY);
			
			if(this.getContentPane().getLayout() == null || !(this.getContentPane().getLayout() instanceof GridBagLayout)) {
				this.setLayout(new GridBagLayout());
			}
			
			GridBagConstraints gbc = place(gridx, gridy, gridwidth, gridheight, weightx, weighty);
			((GridBagLayout) this.getContentPane().getLayout()).setConstraints(component, gbc);
			this.getContentPane().add(component);
		}
		
		private void setPositions(JComponent component, int gridx, int gridy) {
			setPositions(component, gridx, gridy, 1, 1, 0, 0);
		}
		
		private void setPositions(JComponent component, int gridx, int gridy, int weightx, int weighty) {
			setPositions(component, gridx, gridy, 1, 1, weightx, weighty);
		}

		private GridBagConstraints place(int gridx, int gridy, int gridwidth,
				int gridheight, int weightx, int weighty) {
			// determine positions and dimensions
			GridBagConstraints gbc = new GridBagConstraints(); // take a
																// GridBagConstraints
																// manager object
			gbc.gridx = gridx;
			gbc.gridy = gridy;
			gbc.gridwidth = gridwidth;
			gbc.gridheight = gridheight;
			gbc.insets = new Insets(1, 1, 1, 1);
			gbc.weightx = weightx;
			gbc.weighty = weighty;
			gbc.fill = GridBagConstraints.BOTH;
			return gbc;
		}

}
