package csc207b07.ertriage.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class Physician extends User {

	private static final long serialVersionUID = 5818107081252590315L;
	private static final String ROLE = "Physician";
	@SuppressWarnings("unused")
	private static PatientCollection patientCollection;

	/**
	 * Constructs a new Physician made up with his/her name, username and
	 * password.
	 * 
	 * @param name
	 *            name of the physician.
	 * @param username
	 *            username for login.
	 * @param password
	 *            password for login.
	 */

	public Physician(String[] name, String username, String password) {
		super(name, username, password, ROLE);
		if (Physician.getPatients() == null) {
			Physician.setPatients(new HashMap<String, Patient>());
		}
	}

	public Physician(String[] name, String username, String password,
			Context context) {
		super(name, username, password, ROLE);
		try {
			load(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Physician(String[] name, String username, String password,
			Map<String, Patient> patients) {
		super(name, username, password, ROLE);
		Physician.setPatients(patients);
	}

	/**
	 * records Prescription for the patient.
	 * 
	 * @param hcn
	 * @param temp
	 * @param bloodPreasure
	 * @param heartRate
	 * @param time
	 * @param symptoms
	 */
	public void recordPrescription(String hcn, String medicine,
			String instructions) {
		Patient patient = getPatients().get(hcn);
		Visit visit = patient.getPatientHistory().getVisit(
				patient.getPatientHistory().getVisits().size() - 1);
		visit.setSeenByDoctor(true);
		visit.setDoctor(getName());
		visit.addPrescritpion(new Prescription(medicine, instructions));
	}

	/**
	 * Loads the Patients from the XML
	 * 
	 * @param context
	 * @return Map<String, Patient>
	 * @throws IOException
	 */
	public Map<String, Patient> load(Context context) throws IOException {
		try {
			XMLParser xmlparser = new XMLParser();
			setPatients(xmlparser.parse(context));

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getPatients();
	}

	/**
	 * Saves the patients to the XML
	 * 
	 * @param context
	 * @return boolean
	 */
	public boolean save(Context context) {
		XMLParser xmlparser = new XMLParser();

		try {
			FileOutputStream fos = context.openFileOutput("Patients.xml",
					Context.MODE_PRIVATE);
			String text = xmlparser.writePatientXML(getPatients());
			fos.write(text.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * Gets the collection of Patients.
	 * 
	 * @return the collection of patients.
	 */
	public static Map<String, Patient> getPatients() {
		return PatientCollection.getPatients();
	}

	/**
	 * Sets the collection of Patients.
	 * 
	 * @param patients
	 */
	public static void setPatients(Map<String, Patient> patients) {
		PatientCollection.setPatients(patients);
	}

}
