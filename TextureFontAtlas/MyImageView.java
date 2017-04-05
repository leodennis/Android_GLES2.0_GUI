import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


public class MyImageView extends JPanel {
	private static final long serialVersionUID = 1L;
	private ImageIcon image = null;
	private Dimension dimensions = new Dimension();
	
	public MyImageView() {
		super();
		this.setSize(new Dimension(400,800));
		this.setPreferredSize(new Dimension(400,800));
	}
	
	public MyImageView(ImageIcon image) {
		this(image, new Dimension(image.getIconWidth(),image.getIconHeight()));
	}
	
	public MyImageView(ImageIcon image, int width, int height) {
		this(image, new Dimension(width,height));
	}
	
	public MyImageView(ImageIcon image, Dimension d) {
		super();
		this.image = image;
		this.setSize(d);
		this.setPreferredSize(d);
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		dimensions = d;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		dimensions = new Dimension(width, height);
	}
	
	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}
	
	public void setImage(ImageIcon image, boolean updateSize) {
		this.image = image;
		if (updateSize)
			updateSize();
	}

	public Dimension getDimensions() {
		return dimensions;
	}

	public void setDimensions(Dimension dimensions) {
		this.dimensions = dimensions;
	}
	
	public void updateSize() {
		setSize(image.getIconWidth(), image.getIconHeight());
		setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	        RenderingHints.VALUE_ANTIALIAS_ON);

	    g2.drawImage(image.getImage(), 0, 0, dimensions.width, dimensions.height, this);
	}
	
	public static JDialog shwoImage(Frame frame, BufferedImage img) {
		return MyImageView.shwoImage(frame, img, null);
	}
	
	/**
	 * Shows an image in a dilog, useful for testing etc.
	 * 
	 * @param frame - the Frame from which the dialog is displayed
	 * @param img - the image to be shown
	 * @param backgroundColor - background color of dialog and image
	 */
	public static JDialog shwoImage(Frame frame, BufferedImage img, Color backgroundColor) {
		return shwoImageZoomed(frame, img, 1, backgroundColor, null, false);
	}
	
	public static JDialog shwoImageModeless(Frame frame, BufferedImage img, Color backgroundColor, Point pos) {
		return shwoImageZoomed(frame, img, 1, backgroundColor, pos, true);
	}
	
	public static JDialog shwoImageZoomed(Frame frame, BufferedImage img, int zoom) {
		return shwoImageZoomed(frame, img, zoom, null, null, false);
	}
	
	public static JDialog shwoImageZoomed(Frame frame, BufferedImage img, int zoom, Color backgroundColor, Point pos, boolean modless) {
		JDialog dialog = new JDialog(frame, "Image", true);
	    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    dialog.setName(MyImageView.class.getName()+"Dialog");
	    if(modless)
	    	dialog.setModalityType(ModalityType.MODELESS); //Doesn't block any top-level windows
	    if (backgroundColor != null)
	    	dialog.setBackground(backgroundColor);
	    
	    Container cp = dialog.getContentPane();
		MyImageView imageView = new MyImageView(new ImageIcon(img), img.getWidth()*zoom, img.getHeight()*zoom);
		JScrollPane scrollPane = new JScrollPane(imageView);
		GridBagConstraints gbc = new GridBagConstraints(); // take a GridBagConstraints manager object
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(1, 1, 1, 1);
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.fill = GridBagConstraints.BOTH;
		GridBagLayout gbl = new GridBagLayout();
		cp.setLayout(gbl);
		
		gbl.setConstraints(scrollPane, gbc);
		cp.add(scrollPane);
	   
	    dialog.pack();
	    if(pos == null) {
	    	dialog.setLocationRelativeTo(null);
		} else {
			dialog.setLocation(pos.x, pos.y);
		}
	    
		dialog.setVisible(true);
		return dialog;
	}
	
}

