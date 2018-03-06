public class InstructionInformation {		//This class is for storing a string and a number of parameters
	private String command;
	private int numberOfParameters;
	/**
	 * All the information for each commands
	 * @param command the name of the command
	 * @param numberOfParameters the parameters number that is included on that specific command
	 */
	public InstructionInformation(String command, int numberOfParameters) {
		this.command = command;
		this.numberOfParameters = numberOfParameters;
	}
	/**
	 * sends the command name 
	 * @return returns the command string
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * sends the number of parameters
	 * @return returns the number of parameters
	 */
	public int getNumberOfParameters() {
		return numberOfParameters;
	}	
	
}
