import javax.swing.JTextArea;

public class Validation {

	InstructionInformation[] commands = {new InstructionInformation("MOVE", 2),
										new InstructionInformation("LINE", 2),
										new InstructionInformation("CIRCLE", 1),
										new InstructionInformation("SOLID_CIRCLE", 1),
										new InstructionInformation("CLEAR", 0),
										new InstructionInformation("COLOUR", 3),
										new InstructionInformation("TEXT", 1),
										new InstructionInformation("RECTANGLE", 2),
										new InstructionInformation("ARC", 4),
										new InstructionInformation("STROKE", 1),
										new InstructionInformation("ROUND_RECTANGLE", 4),
										new InstructionInformation("STAR", 1)
											};		//If we want to add a new set of instruction, just add here in the commands section
	JTextArea textArea;								//For error Messages
	int errorLineNumber = 0;
	
	public Validation(JTextArea textArea){			//Getting the messageArea for error Messages
		this.textArea = textArea;
	}
	
	public boolean verify(String source){
		
		String[] temp = source.split("\n");			//Splits the whole file on the basis of (\n)
		for (int i = 0; i < temp.length; i++){
			errorLineNumber = i + 1;
			if (line(temp[i]) == false){			//Checks for the instructions and parameters on the next function called line
				return false;						//If returned false, then validation.verify() becomes 0 
			}
		}
		return true;								//If returned true, it means that the file is validated 
		
	}
	
	public boolean line(String source){
		
		String[] temp = source.split(" ");			//For 1 line, splits the line via spaces(" ")
		int index = -1;
		boolean isValid = false;
		for (int i = 0; i < commands.length; i++){
			if (temp[0].compareTo(commands[i].getCommand()) == 0){		//Only checking for the instructions name
				isValid = true;
				index = i;
				break;
			}
		}
		
		if (!isValid){
			textArea.append("Instruction not valid on line number: " + errorLineNumber +"\n");
		}
		
		if (isValid){ 															//If Instructions are valid
			InstructionInformation inf = commands[index]; 						//Referencing the Object via commands
			if(inf.getCommand() == "TEXT"){										//Checking the text first due to the ambigious datatype for its parameter
				isValid = true;
			}else{
				int parameterLength = temp.length - 1; 							//-1, Removing the first line, i.e instruction name
				if (parameterLength != inf.getNumberOfParameters()){			//If parameter number doesnt match up
					isValid = false;
					textArea.append("Parameter length should be " + inf.getNumberOfParameters() + " but found to be " + parameterLength + 
							" on line number: " + errorLineNumber + "\n");
				}else{
					int[] values = new int[parameterLength];					//Proper Number for parameter numbers
					for (int i = 0; i < parameterLength; i++){
						try {
							values[i] = Integer.parseInt(temp[i + 1]);			//Parsing the numbers as they are at string
						} catch (NumberFormatException e) {	
							isValid = false;									//If !found number, then throws a error message
							textArea.append("The value should be integer but found to be " + temp[i + 1] + 
									" on line number: " + errorLineNumber + "\n");
							break;
						}
					}
					if (inf.getCommand() == "COLOUR" && isValid == true){		//Checking specific for the COLOUR as it has barriers i.e (0-255)
						if (!(checkColourRange(values[0]) && checkColourRange(values[1]) && checkColourRange(values[2]))){
							isValid = false;
							textArea.append("Parameter range should be (0-255) on line number: " + errorLineNumber + "\n");
						}
					}
				}
			}
		}
		
		return isValid;				//If everything checks out, then true
	}
	
	private boolean checkColourRange(int value){
		if (value >= 0 && value <= 255){				//Checking the range (0-255)
			return true;
		}
		return false;
	}
	
}
