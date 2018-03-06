import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class InstructionExecution {
	BufferedImage memoryImage;
	DrawPanel drawPanel;
	int moveX, moveY, textDifference;
	Graphics2D g;					//As bufferedImage doesn't take graphics, we had to take Graphics2D
	
	/**
	 * Executing the commands from parameters
	 * @param memoryImage the image that is needed to be drawn
	 * @param drawPanel panel where the image will be drawn
	 */
	public InstructionExecution(BufferedImage memoryImage, DrawPanel drawPanel){		//Getting the BufferedImage and drawPanel from GUI
		this.memoryImage = memoryImage;
		this.drawPanel = drawPanel;
		moveX = 0;
		moveY = 0;
		textDifference = 15;
		g = (Graphics2D) memoryImage.getGraphics();										//g gets the Graphics2D and ready to draw 
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		//Fixes rendering quality
	}
	/**
	 * Processes the raw Data for further ease
	 * @param rawData string that comes from keyboard
	 */
	public void process(String rawData){												//gets the RAW BUT VALIDATED data from textArea
		String[] lines = rawData.split("\n");											//Splitting on the basics of (\n)
		for (int i = 0; i < lines.length; i++){
			execute(lines[i]);															//Executing Single Line
		}
	}
	/**
	 * Executes the raw Data into drawing code
	 * @param information parsed data that is sent through process
	 */
	private void execute(String information){											//The information contains only a single string of information
		String[] data = information.split(" ");											//Splitting on the basics of Spaces
		String instruction = data[0];													//Saving the instruction name on the data[0]
		
		switch (instruction){
			case "MOVE":
				moveX = Integer.parseInt(data[1]);
				moveY = Integer.parseInt(data[2]);
				break;
			case "LINE":
				int x2 = Integer.parseInt(data[1]);
				int y2 = Integer.parseInt(data[2]);
				g.drawLine(moveX, moveY, x2, y2);
				drawPanel.repaint();
				moveX = x2;
				moveY = y2;
				break;
			case "CIRCLE":
				int radius = Integer.parseInt(data[1]);
				g.drawArc(moveX-radius, moveY-radius, 2 * radius, 2 * radius, 0, 360);
				drawPanel.repaint();
				break;
			case "SOLID_CIRCLE":
				int solidradius = Integer.parseInt(data[1]);
				g.fillArc(moveX-solidradius, moveY-solidradius, 2 * solidradius, 2 * solidradius, 0, 360);
				drawPanel.repaint();
				break;
			case "CLEAR":
				g.clearRect(0, 0, memoryImage.getWidth(), memoryImage.getHeight());
				drawPanel.repaint();
				moveX = 0;
				moveY = 0;
				break;
			case "COLOUR":
				int red = Integer.parseInt(data[1]);
				int green = Integer.parseInt(data[2]);
				int blue = Integer.parseInt(data[3]);
				g.setColor(new Color(red, green , blue));
				drawPanel.repaint();
				break;
			case "TEXT":
				g.drawString(information.substring(5), moveX, moveY);
				drawPanel.repaint();
				moveY += textDifference;												//The Height Difference Between Texts (Tested)
				break;
			case "RECTANGLE":
				int width = Integer.parseInt(data[1]);
				int height = Integer.parseInt(data[2]);
				g.drawRect(moveX, moveY, width, height);
				drawPanel.repaint();
				moveX += width;
				moveY += height;
				break;
			case "ARC":
				int arcWidth = Integer.parseInt(data[1]);
				int arcHeight = Integer.parseInt(data[2]);
				int arcStartAngle = Integer.parseInt(data[3]);
				int arcEndAngle = Integer.parseInt(data[4]);
				g.drawArc(moveX, moveY, arcWidth, arcHeight, arcStartAngle, arcEndAngle);
				drawPanel.repaint();
				break;
			case "STROKE":
				int stroke = Integer.parseInt(data[1]);
				g.setStroke(new BasicStroke(stroke));
				drawPanel.repaint();
				break;
			case "ROUND_RECTANGLE":
				int rwidth = Integer.parseInt(data[1]);
				int rheight = Integer.parseInt(data[2]);
				int arcwidth = Integer.parseInt(data[3]);
				int archeight = Integer.parseInt(data[4]);
				g.drawRoundRect(moveX, moveY, rwidth, rheight, arcwidth, archeight);
				drawPanel.repaint();
				moveX += rwidth;
				moveY += rheight;
				break;
			case "STAR":
				int[] x = {3, 1, 0, -1, -3, -1, 0, 1};
				int[] y = {0, 1, 3, 1, 0, -1, -3, -1};
				int scale = Integer.parseInt(data[1]);
				for (int i = 0; i < x.length; i++) {
					x[i] *= scale;
					y[i] *= scale;
					x[i] += moveX;
					y[i] += moveY;
				}
				g.drawPolygon(x, y, 8);
				drawPanel.repaint();
				break;
		}
		
	}
	
}
