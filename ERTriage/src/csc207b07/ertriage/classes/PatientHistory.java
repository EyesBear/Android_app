package csc207b07.ertriage.classes;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class PatientHistory implements Serializable {

	/** Serializes PatientHistory */
	private static final long serialVersionUID = 4265405259685162073L;
	private List<Visit> visits;

	/**
	 * Initializes a new PatientHistory.
	 */
	public PatientHistory() {
		this.visits = new ArrayList<Visit>();
	}

	/**
	 * Returns a Visit corresponding to the index in this PatientHistory's list
	 * of visits.
	 * 
	 * @param index
	 *            index of Visit in the list of Visits
	 * @return Visit Visit
	 */
	public Visit getVisit(int index) {
		return visits.get(index);
	}

	/**
	 * Adds a Visit to this PatientHistory's list of visits.
	 * 
	 * @param newVisit
	 *            Visit
	 */
	public void addVisit(Visit newVisit) {
		visits.add(newVisit);
	}

	/**
	 * Sets the list of visits in this PatientHistory.
	 * 
	 * @param visits
	 *            list of Visit
	 */
	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	/**
	 * Returns the list of visits in this PatientHistory.
	 * 
	 * @return list of Visit
	 */
	public List<Visit> getVisits() {
		return visits;
	}

}
