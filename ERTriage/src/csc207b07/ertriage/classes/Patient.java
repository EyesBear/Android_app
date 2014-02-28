package csc207b07.ertriage.classes;

import java.io.Serializable;
import java.util.Date;

public class Patient implements Serializable {

	/** Serializes the Patient class */
	private static final long serialVersionUID = 4772614411723853871L;
	private String name[], hCardNum;
	private Date dob;
	private int age;
	private PatientHistory history;

	/**
	 * Initializes a new Patient.
	 * 
	 * @param name
	 *            name of a Patient
	 * @param dob
	 *            date of birth of a Patient
	 * @param hCardNum
	 *            health card number of a Patient
	 * @param patientHist
	 *            history of a Patient
	 */
	public Patient(String[] name, Date dob, String hCardNum,
			PatientHistory patientHist) {
		this.name = name;
		this.dob = dob;
		this.hCardNum = hCardNum;
		this.history = patientHist;
		calcAge();
	}

	/**
	 * Returns the name of this Patient.
	 * 
	 * @return name of this Patient
	 */
	public String[] getName() {
		return name;
	}

	/**
	 * Sets the name of this Patient.
	 * 
	 * @param name
	 *            name of this Patient
	 */
	public void setName(String[] name) {
		this.name = name;
	}

	/**
	 * Returns this Patient's date of birth.
	 * 
	 * @return date of birth of this Patient
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets this Patient's date of birth
	 * 
	 * @param dob
	 *            this Patient's date of birth
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Returns the health card number of this Patient.
	 * 
	 * @return this Patient's health card number
	 */
	public String getHealthCardNum() {
		return hCardNum;
	}

	/**
	 * Sets the health card number of this Patient.
	 * 
	 * @param hCardNum
	 *            this Patient's health card number
	 */
	public void setHealthCardNum(String hCardNum) {
		this.hCardNum = hCardNum;
	}

	/**
	 * Returns the history of this Patient.
	 * 
	 * @return this Patient's history
	 */
	public PatientHistory getPatientHistory() {
		return history;
	}

	/**
	 * Sets the history of this Patient
	 * 
	 * @param history
	 *            this Patient's history
	 */
	public void setHistory(PatientHistory history) {
		this.history = history;
	}

	/**
	 * Returns the age of this Patient.
	 * 
	 * @return this Patient's age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Calculates the age of the Patient
	 */
	@SuppressWarnings("deprecation")
	private void calcAge() {
		Date today = new Date();
		if (dob.getMonth() > today.getMonth()) {
			age = today.getYear() - dob.getYear() - 1;
		} else {
			age = today.getYear() - dob.getYear();
		}
	}

}
