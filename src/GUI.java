import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class GUI extends JFrame{
	
	private BufferedImage memoryImage;
	DrawPanel drawPanel;
	JTextArea textArea;
	JTextArea messageArea;
	
	public GUI(){
		setTitle("CUI Drawing Application");
		setPreferredSize(new Dimension(1266, 668));
		setLocation(50, 50);													//Sets location w.r.t to the screen
		createMenu();															//Creates Menu Bar
		memoryImage = new BufferedImage(800, 450, BufferedImage.TYPE_INT_RGB);  //Creating a BufferImage for saving purpose's
		drawPanel = new DrawPanel(memoryImage);
		textArea = new JTextArea();												//The right side window for the instructions
		messageArea = new JTextArea();											//A seperate console like window for the success/errors
		messageArea.setEditable(false);											//Doesn't let us edit the messageArea
		createWindows();														//Creates the window(s)(3)
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");											//File
		file.setMnemonic('F');
		menuBar.add(file);
		
		JMenuItem load = new JMenuItem("Load");									//Load
		load.setIcon(new ImageIcon("img/load.png"));
		load.setMnemonic('L');
		load.addActionListener(new LoadListener());
		
		JMenuItem save = new JMenuItem("Save");									//Save
		save.setIcon(new ImageIcon("img/save.png"));
		save.setMnemonic('S');
		save.addActionListener(new SaveListener());
		
		JMenuItem exit = new JMenuItem("Exit");									//Exit
		exit.setIcon(new ImageIcon("img/exit.png"));
		exit.setMnemonic('X');
		exit.addActionListener(new ExitListener());
		
		file.add(load);
		file.add(save);
		file.addSeparator();
		file.add(exit);
		
		JMenu run = new JMenu("Run");											//Run
		run.setMnemonic('R');
		menuBar.add(run);
		run.addMenuListener(new RunListener());
		
		JMenu help = new JMenu("Help");											//Help
		help.setMnemonic('H');
		menuBar.add(help);
		
		JMenuItem about = new JMenuItem("About");								//About
		about.setIcon(new ImageIcon("img/about.png"));
		about.setMnemonic('A');
		about.addActionListener(new AboutListener());
		
		help.add(about);
		
		setJMenuBar(menuBar);													//Adding the whole menu on the Frame
	}
	
	private void createWindows(){
		JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);			//Splitting the Pane Vertically
		
		JScrollPane scrollPane = new JScrollPane();											//For Scrolling
		scrollPane.setViewportView(textArea);												//Defining Scrolling for the textArea
		
		TextLineNumber tln = new TextLineNumber(textArea);									//Adding a TextLineNumber Class only for the line number
		scrollPane.setRowHeaderView(tln);													//Using TextLineNumber classs
		
		JSplitPane splitPaneHorizontal = new JSplitPane(JSplitPane.VERTICAL_SPLIT);			//Splitting the Pane Horizontally
		JScrollPane scrollPaneBox = new JScrollPane();										//For Scrolling
		scrollPaneBox.setViewportView(messageArea);											//Defining Scrolling for the messageArea
		
		splitPaneHorizontal.setLeftComponent(drawPanel);									//Occupying Top Panel for Drawing
		splitPaneHorizontal.setRightComponent(scrollPaneBox);								//Occupying Bottom Panel for messageArea 
		splitPaneHorizontal.setResizeWeight(0.9);											//Ration 0:9
		
		splitPaneVertical.setLeftComponent(splitPaneHorizontal);							//Occupying Left Panel for Drawing
		splitPaneVertical.setRightComponent(scrollPane);									//Occupying Right Panel for textArea
		splitPaneVertical.setResizeWeight(0.7);												//Ratio 0:7
		
		splitPaneHorizontal.setContinuousLayout(true);										//For sensibly Resizing
		splitPaneVertical.setContinuousLayout(true);										//For sensibly Resizing
		
		add(splitPaneVertical);																//Final pane to add from frame
	}
	
	public class ExitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			GUI.this.dispose();																//Disposing the object before exiting
			System.exit(0);	
		}
		
	}
	
	public class SaveListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showSaveDialog(GUI.this);
			
			if (option == JFileChooser.APPROVE_OPTION){
				File path = fileChooser.getSelectedFile();											//Getting the path for the selected location
				try {
					ImageIO.write(memoryImage, "png", new File(path.getAbsolutePath() + ".png"));	//Saving the file on that location
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
			
		}
		
	}
	
	public class AboutListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(null, "CUI Drawing Application.\nMade by Sulabh Shrestha.");
		}
		
	}
	
	public class LoadListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showOpenDialog(null);
			
			if (option == JFileChooser.APPROVE_OPTION){
				try {
					FileReader fileReader = new FileReader(fileChooser.getSelectedFile());
					String currentLine;
					BufferedReader bufferedReader = new BufferedReader(fileReader);
					textArea.setText("");												//Initializing the text to null before reading the file 
					while ((currentLine = bufferedReader.readLine()) != null){			//readLine() reads the whole single line in a text
						textArea.append(currentLine + "\n");							//Appending the text to textArea
					}
					bufferedReader.close();
					fileReader.close();
					
					Validation validation = new Validation(messageArea);				//Sending messageArea to write down the errors while validating
					if (validation.verify(textArea.getText())){							//Validating the file, returns 1 if validated else 0
						messageArea.append("Validations Complete \n");
						InstructionExecution ie = new InstructionExecution(memoryImage, drawPanel);		//sends the bufferedImage and drawPanel to draw
						messageArea.append("Instructions Processing... \n");
						ie.process(textArea.getText());									//Sending the raw file for execution as this step only works after validation
						messageArea.append("Instructions Processed \n");
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		
	}
	
	public class RunListener implements MenuListener{

		@Override
		public void menuSelected(MenuEvent e) {
			// TODO Auto-generated method stub
			Validation validation = new Validation(messageArea);
			if (validation.verify(textArea.getText())){
				messageArea.append("Validations Complete \n");
				InstructionExecution ie = new InstructionExecution(memoryImage, drawPanel);
				messageArea.append("Instructions Processing... \n");
				ie.process(textArea.getText());
				messageArea.append("Instructions Processed \n");
			}
		}

		@Override
		public void menuDeselected(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void menuCanceled(MenuEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
	
}
