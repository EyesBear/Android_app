package csc207b07.ertriage.classes;

import java.io.Serializable;

public class Prescription implements Serializable {

	private static final long serialVersionUID = -8329094232947546669L;
	private String Name, Instructions;

	/**
	 * Constructs a new Prescription made up with its medicine name and
	 * instructions for the medicine.
	 * 
	 * @param name
	 *            name of medicine.
	 * @param instructions
	 *            instructions for the medicine.
	 */
	public Prescription(String name, String instructions) {
		super();
		Name = name;
		Instructions = instructions;
	}

	/**
	 * Gets the name of the medication.
	 * 
	 * @return the name of the medication.
	 */
	public String getName() {
		return Name;
	}

	/**
	 * Sets the name of the medication.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		Name = name;
	}

	/**
	 * Gets the instructions of the medication.
	 * 
	 * @return the instructions for the medication.
	 */
	public String getInstructions() {
		return Instructions;
	}

	/**
	 * Sets the instructions of the medication.
	 * 
	 * @param instructions
	 */
	public void setInstructions(String instructions) {
		Instructions = instructions;
	}
}
