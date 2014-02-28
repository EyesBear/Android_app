package csc207b07.ertriage.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import csc207b07.ertriage.classes.Patient;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.Physician;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.classes.Visit;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("userKey");
		TextView hello = (TextView) findViewById(R.id.main_intro);
		if (user.getRole().equals("Nurse")) {
			String name = user.getName()[0];
			hello.setText("Hello Nurse " + name + "!");
		} else if (user.getRole().equals("Physician")) {
			String name = user.getName()[user.getName().length - 1];
			hello.setText("Hello Dr. " + name + "!");
		}

		Button urgencyButton = (Button) findViewById(R.id.view_by_urgency_button);
		Button addButton = (Button) findViewById(R.id.add_patient_button);

		if (user.getRole().equals("Physician")) {
			user = (Physician) user;
			urgencyButton.setVisibility(View.INVISIBLE);
			addButton.setVisibility(View.INVISIBLE);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// If Search Patient button is clicked continue to SearchPatientActivity.
	public void searchPatient(View view) {
		Intent newIntent = new Intent(this, SearchActivity.class);
		newIntent.putExtra("userKey", user);
		startActivity(newIntent);
	}

	// If Add Patient button is clicked continue to AddPatientActivity.
	public void addPatient(View view) {
		Intent newIntent = new Intent(this, AddPatientActivity.class);
		newIntent.putExtra("userKey", user);
		startActivity(newIntent);
	}

	@SuppressWarnings("rawtypes")
	public void viewByUrgency(View view) {
		List<String> notSeen = new ArrayList<String>();
		Iterator it = PatientCollection.getPatients().entrySet().iterator();

		/*
		 * Check if there are patients that haven't been seen by a physician. If
		 * there aren't Toast.
		 */
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
					notSeen.add(patient.getHealthCardNum());
				}
			}
		}
		if (notSeen.isEmpty()) {
			String message = "There are no patients that have not been seen"
					+ " by a physician";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
					.show();
		} else {
			Intent nextIntent = new Intent(this, ListByUrgencyActivity.class);
			nextIntent.putExtra("userKey", user);
			startActivity(nextIntent);
		}
	}

}
