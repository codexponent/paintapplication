import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class DrawPanel extends JPanel{
	
	private BufferedImage memoryImage;
	
	public DrawPanel(BufferedImage memoryImage){
		this.memoryImage = memoryImage;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(memoryImage, 30, 30, this);
	}

}
