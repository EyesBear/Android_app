package csc207b07.ertriage.classes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

public class Nurse extends User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8562643701078024832L;
	private static final String ROLE = "Nurse";
	@SuppressWarnings("unused")
	private static PatientCollection patientCollection;

	/**
	 * Constructor for Nurse
	 * 
	 * @param name
	 * @param username
	 * @param password
	 */
	public Nurse(String[] name, String username, String password) {
		super(name, username, password, ROLE);
		if (Nurse.getPatients() == null) {
			Nurse.setPatients(new HashMap<String, Patient>());
		}
	}

	/**
	 * Constructor for Nurse
	 * 
	 * @param name
	 * @param username
	 * @param password
	 * @param context
	 */
	public Nurse(String[] name, String username, String password,
			Context context) {
		super(name, username, password, ROLE);
		try {
			load(context);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Constructor for Nurse
	 * 
	 * @param name
	 * @param username
	 * @param password
	 * @param patients
	 */
	public Nurse(String[] name, String username, String password,
			Map<String, Patient> patients) {
		super(name, username, password, ROLE);
		Nurse.setPatients(patients);
	}

	/**
	 * Edits a Patient using parameters
	 * 
	 * @param patient
	 */
	public void editPatient(Patient patient) {
		Patient temp = getPatients().get(patient.getHealthCardNum());
		temp.setDob(patient.getDob());
		temp.setName(patient.getName());
		temp.setHistory(patient.getPatientHistory());

	}

	/**
	 * gets a Patient by the Health Card Number
	 * 
	 * @param hcn
	 * @return Patient
	 */
	public Patient getPatient(String hcn) {
		return getPatients().get(hcn);
	}

	/**
	 * Removes a Patient from the Patient collection through a Health Card
	 * Number
	 * 
	 * @param hcn
	 */
	public void removePatient(String hcn) {
		getPatients().remove(hcn);
	}

	/**
	 * Creates a Patient
	 * 
	 * @param name
	 * @param dob
	 * @param hcn
	 * @param patientHist
	 */
	public void createPatient(String[] name, Date dob, String hcn,
			PatientHistory patientHist) {
		Patient patient = new Patient(name, dob, hcn, patientHist);
		getPatients().put(patient.getHealthCardNum(), patient);
	}

	/**
	 * gets a List of Patients sorted by their urgency, decreasing.
	 * 
	 * @return List<String> sortedReversed a List of Patients that haven't been
	 *         seen by a Physician, sorted and decreasing.
	 */
	public List<String> getPatientsByUrgency() {
		List<String> patientsNotSeen = new ArrayList<String>();
		List<String> sorted = new ArrayList<String>();
		List<String> sortedReversed = new ArrayList<String>();
		/*
		 * Call getPatientsNotSeen() helper function to get the list of Patients
		 * not seen by a Physician.
		 */
		patientsNotSeen = getPatientsNotSeen();
		// Call quicksort to sort patientsNotSeen by urgency.
		sorted = quicksort(patientsNotSeen);
		// Reverse List order to a decreasing order.
		for (int i = sorted.size() - 1; i >= 0; i--) {
			sortedReversed.add(sorted.get(i));
		}
		return sortedReversed;

	}

	/**
	 * gets Patients by arrival work in progress...
	 * 
	 * @return Map<String, Patient>
	 */
	public Map<String, Patient> getPatientsByArrival() {
		// Need to implement some sort of sorting
		return null;

	}

	/**
	 * sets that is has been seen
	 * 
	 * @param hcn
	 *            Patient's hcn
	 * @param doctor
	 *            Physician's name
	 */
	public void hasBeenSeen(String hcn, String[] doctor) {
		Patient patient = getPatients().get(hcn);
		Visit visit = patient.getPatientHistory().getVisits()
				.get(patient.getPatientHistory().getVisits().size() - 1);
		visit.setSeenByDoctor(true);
		visit.setDoctor(doctor);

	}

	/**
	 * get patients that have not yet been seen by a doctor.
	 * 
	 * @return List<String> patientsNotSeen which is a List of String hcn for
	 *         each Patient that hasn't been seen by a Physician.
	 */
	@SuppressWarnings("rawtypes")
	public List<String> getPatientsNotSeen() {
		List<String> patientsNotSeen = new ArrayList<String>();
		// Instantiate an Iterator object to iterate over the Map of Patients.
		Iterator it = getPatients().entrySet().iterator();

		// If flag isSeenByDoctor is false, add to a new list patientsNotSeen.
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Patient patient = (Patient) entry.getValue();
			// Check that there are Visits for this Patient.
			if (!patient.getPatientHistory().getVisits().isEmpty()) {
				Visit visit = patient
						.getPatientHistory()
						.getVisits()
						.get(patient.getPatientHistory().getVisits().size() - 1);
				if (!visit.isSeenByDoctor()) {
					patientsNotSeen.add(patient.getHealthCardNum());
				}
			}
		}
		return patientsNotSeen;
	}

	/**
	 * Records Vitals
	 * 
	 * @param hcn
	 *            or health card number
	 * @param temp
	 * @param bloodPreasure
	 * @param heartRate
	 * @param time
	 * @param symptoms
	 */
	public void recordVitals(String hcn, double temp, int[] bloodPreasure,
			int heartRate, Date time, String symptoms) {
		Patient patient = getPatients().get(hcn);
		Visit visit = patient.getPatientHistory().getVisit(
				patient.getPatientHistory().getVisits().size() - 1);
		visit.addVitalSign(new Vitals(temp, bloodPreasure, heartRate, time,
				symptoms));
	}

	/**
	 * Records Vitals and sets Seen by Doctor to true and stores the Doctor Name
	 * 
	 * @param hcn
	 *            or health card number
	 * @param temp
	 * @param bloodPreasure
	 * @param heartRate
	 * @param time
	 * @param symptoms
	 * @param docName
	 */
	public void recordVitals(String hcn, double temp, int[] bloodPreasure,
			int heartRate, Date time, String symptoms, String[] doctor) {
		Patient patient = getPatients().get(hcn);
		Visit visit = patient.getPatientHistory().getVisit(
				patient.getPatientHistory().getVisits().size() - 1);
		visit.setSeenByDoctor(true);
		visit.setDoctor(doctor);
		visit.addVitalSign(new Vitals(temp, bloodPreasure, heartRate, time,
				symptoms));
	}

	/**
	 * Saves the patients to the XML
	 * 
	 * @param a
	 *            instance of the context
	 * @return a boolean if successfully saved
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
	 * Loads the Patients from the XML
	 * 
	 * @param a
	 *            instance of the context
	 * @return a collection of Map<String, Patient> that represents all the
	 *         Patients and there history
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
	 * get a collection of Patient
	 * 
	 * @return Map<String, Patient> collection of all Patients; key hcn and
	 *         value Patient.
	 */
	public static Map<String, Patient> getPatients() {
		return PatientCollection.getPatients();
	}

	/**
	 * Set the collection of patients
	 * 
	 * @param patients
	 *            Map of key hcn and value Patient.
	 */
	public static void setPatients(Map<String, Patient> patients) {
		PatientCollection.setPatients(patients);
	}

	/**
	 * A quicksort implementation to sort the List of Patients that haven't been
	 * seen by a Physician.
	 * 
	 * @param patientsNotSeen
	 *            a List<String> of patients that haven't been seen by a
	 *            Physician.
	 * @return List<String> sorted a sorted List of Patients that haven't been
	 *         seen by a Physician ordered by their urgency parameter,
	 *         increasing.
	 */
	public static List<String> quicksort(List<String> patientsNotSeen) {
		if (patientsNotSeen.size() <= 1)
			return patientsNotSeen;
		String pivotHCN = patientsNotSeen.get(1);
		List<String> lesser = new ArrayList<String>();
		List<String> greater = new ArrayList<String>();
		List<String> sameAsPivot = new ArrayList<String>();
		// int sameAsPivot = 0;
		for (String hcn : patientsNotSeen) {
			Patient patient = getPatients().get(hcn);
			int patientUrgency = getPatients().get(hcn).getPatientHistory()
					.getVisits()
					.get(patient.getPatientHistory().getVisits().size() - 1)
					.getUrgency();
			int pivotUrgency = getPatients().get(pivotHCN).getPatientHistory()
					.getVisits()
					.get(patient.getPatientHistory().getVisits().size() - 1)
					.getUrgency();
			// getPatients().get(patientsNotSeen.get(pivot)).getPatientHistory().getVisits().get(patient.getPatientHistory().getVisits().size()
			// - 1).getUrgency();
			if (patientUrgency > pivotUrgency) {
				greater.add(hcn);
			} else if (patientUrgency < pivotUrgency) {
				lesser.add(hcn);
			} else {
				sameAsPivot.add(hcn);
			}
		}
		lesser = quicksort(lesser);
		for (String hcn : sameAsPivot)
			lesser.add(hcn);
		greater = quicksort(greater);
		List<String> sorted = new ArrayList<String>();
		for (String hcn : lesser)
			sorted.add(hcn);
		for (String hcn : greater)
			sorted.add(hcn);
		return sorted;
	}

}