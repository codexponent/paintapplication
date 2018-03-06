import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DrawPanel extends JPanel{
	
	private BufferedImage memoryImage;
	/**
	 * To Draw the panel
	 * @param memoryImage image that is ready to be drawn
	 */
	public DrawPanel(BufferedImage memoryImage){
		this.memoryImage = memoryImage;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(memoryImage, 30, 30, this);
	}

}
