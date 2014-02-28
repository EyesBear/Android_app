package csc207b07.ertriage.classes;

import java.io.Serializable;
import java.util.Date;

/**
 * @author uri Class Vitals is a class that creates and holds the methods for
 *         Vitals.
 */
public class Vitals implements Serializable {

	private static final long serialVersionUID = 5515728655466985081L;
	/** A Vitals' temp */
	private double temp;
	/** A Vitals' heartRate */
	private int heartRate;
	/** A Vitals' bloodPressure */
	private int[] bloodPressure;
	/** A Vitals' time */
	private Date time;
	/** A Vitals' symptoms */
	private String symptoms;

	/**
	 * Constructs a Vitals object with temperature temp, blood pressure
	 * bloodPressure, heart rate heartRate and symptoms symptoms.
	 * 
	 * @param temp
	 *            the temperature of Vitals.
	 * @param bloodPressure
	 *            the blood pressure of Vitals
	 * @param heartRate
	 *            the heart rate of Vitals.
	 * @param symptoms
	 *            the symptoms of Vitals.
	 */
	public Vitals(double temp, int[] bloodPressure, int heartRate, Date time,
			String symptoms) {
		this.temp = temp;
		this.bloodPressure = bloodPressure;
		// Create a new Date object as the time.
		this.time = time;
		this.symptoms = symptoms;
		this.heartRate = heartRate;
	}

	/**
	 * Sets the temp of this Vitals.
	 * 
	 * @param temp
	 *            the new temp of this Vitals.
	 */
	public void setTemp(double temp) {
		this.temp = temp;
	}

	/**
	 * Returns this Vitals' temp.
	 * 
	 * @return the temp of this Vitals.
	 */
	public double getTemp() {
		return temp;
	}

	/**
	 * Sets this Vitals' bloodPressure.
	 * 
	 * @param bloodPressure
	 *            the new bloodPressure of this Vitals.
	 */
	public void setBloodPressure(int[] bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	/**
	 * Returns this Vitals' bloodPressure.
	 * 
	 * @return the bloodPressure of this Vitals.
	 */
	public int[] getBloodPressure() {
		return bloodPressure;
	}

	/**
	 * Sets this Vitals' heartRate.
	 * 
	 * @param heartRate
	 *            the new heartRate of this Vitals.
	 */
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	/**
	 * Returns this Vitals' heartRate.
	 * 
	 * @return the heartRate of this Vitals.
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * Sets this Vitals' time and date to the current time and date (Date
	 * object).
	 */
	public void setTime() {
		// Create a new Date object as the time.
		this.time = new Date();
	}

	/**
	 * Returns this Vitals' time (Date object).
	 * 
	 * @return the time (Date object) of this Vitals.
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Sets this Vitals' symptoms.
	 * 
	 * @param symptoms
	 *            the new symptoms of this Vitals.
	 */
	public void setSymptoms(String symptoms) {
		this.symptoms = symptoms;
	}

	/**
	 * Returns this Vitals' symptoms
	 * 
	 * @return the symptoms of this Vitals.
	 */
	public String getSymptoms() {
		return symptoms;
	}

}
