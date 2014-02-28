package csc207b07.ertriage.classes;

import java.io.Serializable;
import java.util.Map;

public class PatientCollection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7294408807917275753L;
	private static final PatientCollection patientCollection = new PatientCollection();
	private static Map<String, Patient> patients;

	/**
	 * Gets the collection of patients.
	 * 
	 * @return the collection of patients.
	 */
	public static Map<String, Patient> getPatients() {
		return patients;
	}

	/**
	 * Sets the collection of patients.
	 * 
	 * @param patient
	 */
	public static void setPatients(Map<String, Patient> patient) {
		PatientCollection.patients = patient;
	}

	/**
	 * Gets the object of PatientCollection.
	 * 
	 * @return the object of PatientCollection.
	 */
	public static PatientCollection getPatientcollection() {
		return patientCollection;
	}

}
