public class InstructionInformation {		//This class is for storing a string and a number of parameters
	private String command;
	private int numberOfParameters;
	
	public InstructionInformation(String command, int numberOfParameters) {
		this.command = command;
		this.numberOfParameters = numberOfParameters;
	}

	public String getCommand() {
		return command;
	}

	public int getNumberOfParameters() {
		return numberOfParameters;
	}	
	
}
