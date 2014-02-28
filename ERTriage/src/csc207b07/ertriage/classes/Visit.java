package csc207b07.ertriage.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Visit implements Serializable {

	private static final long serialVersionUID = -5624561805568953338L;
	private boolean seenByDoctor;
	private Date timeOfArrival, timeSeenDoc;
	private String[] doctor;
	private List<Vitals> vitalSign;
	private int urgency;
	private List<Prescription> prescriptions;

	public Visit(Date timeOfArrival, List<Vitals> vitalSign, int age) {
		this.seenByDoctor = false;
		this.timeOfArrival = timeOfArrival;
		this.doctor = null;
		this.vitalSign = vitalSign;
		this.prescriptions = new ArrayList<Prescription>();
		this.timeSeenDoc = null;
		setUrgency(age);
	}

	/**
	 * Constructs a new Visit made up with its seenbydoctor status, time of
	 * arrival, doctor's name, vital signs and urgency level.
	 * 
	 * @param seenByDoctor
	 *            Whethere the patient has been seen by a doctor.
	 * @param timeOfArrival
	 *            The patient's time of arrival.
	 * @param doctor
	 *            The name of the doctor.
	 * @param vitalSign
	 *            The list of the patient's vital sign.
	 * @param urgency
	 *            The urgency level of the patient.
	 */
	public Visit(boolean seenByDoctor, Date timeOfArrival, String[] doctor,
			List<Vitals> vitalSign, int urgency,
			List<Prescription> prescriptions, Date timeSeenDoc) {
		this.seenByDoctor = seenByDoctor;
		this.timeOfArrival = timeOfArrival;
		this.doctor = doctor;
		this.vitalSign = vitalSign;
		this.urgency = urgency;
		this.prescriptions = prescriptions;
		this.timeSeenDoc = timeSeenDoc;
	}

	/**
	 * Returns whether the patient has been seen by a doctor.
	 * 
	 * @return A boolean representation of whether the patient has been seen by
	 *         a doctor.
	 */
	public boolean isSeenByDoctor() {
		return seenByDoctor;
	}

	/**
	 * Sets whether the patient has been seen by a doctor.
	 * 
	 * @param seenByDoctor
	 */
	public void setSeenByDoctor(boolean seenByDoctor) {
		this.seenByDoctor = seenByDoctor;
	}

	/**
	 * Returns the patient's time of arrival.
	 * 
	 * @return A Date representation of the patient's time of arrival.
	 */
	public Date getTimeOfArrival() {
		return timeOfArrival;
	}

	/**
	 * Sets the patient's arrival time.
	 * 
	 * @param timeOfArrival
	 */
	public void setTimeOfArrival(Date timeOfArrival) {
		this.timeOfArrival = timeOfArrival;
	}

	/**
	 * Returns the name of the patient's doctor.
	 * 
	 * @return A string representation of the name of the patient's doctor.
	 */
	public String[] getDoctor() {
		return doctor;
	}

	/**
	 * Sets the name of the patient's doctor.
	 */
	public void setDoctor(String[] doctor) {
		this.doctor = doctor;
	}

	/**
	 * Adds a vital sign to the patient's vitalSign list.
	 * 
	 * @param vitalSign
	 */
	public void addVitalSign(Vitals vitalSign) {
		this.vitalSign.add(vitalSign);
	}

	/**
	 * Adds a prescription to the patient's prescriptions list.
	 * 
	 * @param prescription
	 */
	public void addPrescritpion(Prescription prescription) {
		this.prescriptions.add(prescription);
	}

	/**
	 * Returns the patient's urgency level.
	 * 
	 * @return A int representation of the patient's urgency level.
	 */
	public int getUrgency() {
		return urgency;
	}

	/**
	 * Gets the Prescription list of the patient.
	 * 
	 * @return the collection of prescriptions.
	 */
	public List<Prescription> getPrescriptions() {
		return prescriptions;
	}

	/**
	 * Sets the Prescription list of the patient.
	 * 
	 * @param prescriptions
	 */
	public void setPrescriptions(List<Prescription> prescriptions) {
		this.prescriptions = prescriptions;
	}

	/**
	 * Sets the urgency level of a patient based on the rules of the hospital.
	 * 
	 * @param age
	 */
	public void setUrgency(int age) {
		Vitals vitals = vitalSign.get(vitalSign.size() - 1);
		urgency = 0;
		if (age < 2) {
			urgency++;
		}
		if (vitals.getTemp() >= 39.0) {
			urgency++;
		}
		if (vitals.getBloodPressure()[0] >= 140
				|| vitals.getBloodPressure()[1] >= 90) { // Two cases for
															// bloodpressure.
			urgency++;
		}
		if (vitals.getHeartRate() >= 100 || vitals.getHeartRate() <= 50) { // Two
																			// cases
																			// for
																			// heart
																			// rate.
			urgency++;
		}
	}

	/**
	 * Returns the list of vital signs of a patient.
	 * 
	 * @return A list representation of the patient's vital signs.
	 */
	public List<Vitals> getVitals() {
		return vitalSign;
	}

	/**
	 * Sets the patient's vitalSign list.
	 * 
	 * @param vitalSign
	 */
	public void setVitals(List<Vitals> vitalSign) {
		this.vitalSign = vitalSign;
	}

	/**
	 * Gets the time the patient last seen by a doctor.
	 * 
	 * @return the time the patient last seen by a doctor.
	 */
	public Date getTimeSeenDoc() {
		return timeSeenDoc;
	}

	/**
	 * Sets the time the patient last seen by a doctor.
	 * 
	 * @param timeSeenDoc
	 */
	public void setTimeSeenDoc(Date timeSeenDoc) {
		this.timeSeenDoc = timeSeenDoc;
	}

}
