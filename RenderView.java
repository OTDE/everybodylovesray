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
	
	private JButton exportButton;
	private JButton returnButton;
	
	/**
	 * Constructor for RenderView, set default values 
	 * for the JFrame and connects this view to the 
	 * controller.
	 * @param renderController
	 */
	public RenderView(RenderController rCon) {
		this.rendCon = rCon;
		
		buildView();
		
		setTitle("Ray Tracing");
		setSize(1024,768);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setVisible(true);

	}// RenderView

	/**
	 * Builds the JFrame components of the GUI
	 */
	private void buildView() {
		
		JPanel panelMid = new JPanel();
		JPanel panelSouth = new JPanel();
		
		getContentPane().add(panelMid);
		getContentPane().add(panelSouth, "South");
		
		displayImage = new ImageIcon();
		JLabel imageLabel = new JLabel(displayImage);
		panelMid.add(imageLabel);
		
		returnButton = new JButton("Render Another Image");
		exportButton = new JButton("Export");
		
		panelSouth.add(returnButton);
		panelSouth.add(exportButton);
		
	}// buildView

}
