import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Ethan Wiederspan and Seth Chapman
 * 
 * GUI that gives the user a display of the image 
 * as it is rendering, gives options to return to 
 * file select and to export the image.
 *
 */
public class RenderView extends javax.swing.JFrame {
	
	private RenderController rendCon;
	private ImageIcon displayImage;
	
	private JPanel panelMid;
	private JPanel panelSouth;
	
	private JButton exportButton;
	private JButton returnButton;
	
	private JLabel imageLabel;
	
	/**
	 * Constructor for RenderView, set default values 
	 * for the JFrame and connects this view to the 
	 * controller.
	 * @param height 
	 * @param width 
	 * @param renderController
	 */
	public RenderView(RenderController rCon, int width, int height) {
		this.rendCon = rCon;
		
		buildView();
		
		setTitle("Ray Tracing");
		setSize(width,(height+60));
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);

	}// RenderView

	/**
	 * Builds the JFrame components of the GUI
	 */
	private void buildView() {
		
		panelMid = new JPanel();
		panelSouth = new JPanel();
		
		getContentPane().add(panelMid);
		getContentPane().add(panelSouth, "South");
		
		displayImage = new ImageIcon();
		imageLabel = new JLabel(displayImage);
		panelMid.add(imageLabel);
		
		returnButton = new JButton("Render Another Image");
		exportButton = new JButton("Export");
		
		panelSouth.add(returnButton);
		panelSouth.add(exportButton);
		
	}// buildView
	
	/**
	 * Called every 2 seconds by the RenderController to update 
	 * the image display to the User via the Render GUI
	 * 
	 * @param newImage the updated BufferedIMage to be displayed
	 */
	public void updateView(BufferedImage newImage) {
		
		displayImage = new ImageIcon(newImage);
		imageLabel.setIcon(displayImage);
		panelMid.add(imageLabel);
		
		System.out.println("View updated!");
		
	}// updateView

}
