package csc207b07.ertriage.activities;

import java.util.Date;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.Patient;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TakeVitalsActivity extends Activity {

	private Nurse nurse;
	private String hcn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_vitals);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.take_vitals, menu);
		return true;
	}

	/**
	 * Save the Vitals that were input by the user, and create a new Vitals
	 * object for this Visit.
	 * 
	 * @param view
	 *            The View of the Activity.
	 */
	public void saveVitals(View view) {
		// Get the Intent passed from the previous activity.
		Intent intent = getIntent();

		// Create both local versions of the data passed from previous activity.
		nurse = (Nurse) intent.getSerializableExtra("userKey");
		hcn = (String) intent.getSerializableExtra("hcnKey");

		/*
		 * Take the input data from the fields, and convert it to String, int
		 * and int[] as necessary for the method recordVitals in nurse.
		 */
		EditText tempText = (EditText) findViewById(R.id.temp_field);
		int temp = Integer.parseInt(tempText.getText().toString());

		EditText heartRateText = (EditText) findViewById(R.id.heartRate_field);
		int heartRate = Integer.parseInt(heartRateText.getText().toString());

		EditText sysText = (EditText) findViewById(R.id.bloodPressureSys_field);
		EditText diaText = (EditText) findViewById(R.id.bloodPressureDia_field);
		int sys = Integer.parseInt(sysText.getText().toString());
		int dia = Integer.parseInt(diaText.getText().toString());
		int[] bloodPressure = {sys, dia};

		EditText symptomsText = (EditText) findViewById(R.id.symptoms_field);
		String symptoms = symptomsText.getText().toString();

		// Check when to the input values are reasonable, if not give Toasts.
		if ((heartRate < 0) || (heartRate > 350)) {
			String message = "Please enter a valid heart rate";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();
		} else if ((temp < 20) || (temp > 47)) {
			String message = "Please enter a valid temperature";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();
		} else if ((bloodPressure[0] < 50) || (bloodPressure[0] > 220)
				|| (bloodPressure[1] < 20) || (bloodPressure[1] > 120)) {
			String message = "Please enter a valid blood pressure";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();
		} else {

			// Sets urgency and saves new information.
			nurse.recordVitals(hcn, temp, bloodPressure, heartRate, new Date(),
					symptoms);
			Patient patient = PatientCollection.getPatients().get(hcn);
			patient.getPatientHistory()
					.getVisit(patient.getPatientHistory().getVisits().size() - 1)
							.setUrgency(patient.getAge());
			nurse.save(this);
			finish();
			
			Toast.makeText(getApplicationContext(), "New vital signs has been added.",
					Toast.LENGTH_SHORT).show();
		}
	}

}