package csc207b07.ertriage.activities;

import java.text.SimpleDateFormat;
import csc207b07.ertriage.classes.Physician;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.Patient;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.activities.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PatientRecordActivity extends Activity {

	private Patient patient;
	private User user;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_record);

		// Sets up the display texts to match the Patient's record.
		Intent intent = getIntent();
		patient = (Patient) intent.getSerializableExtra("patientKey");
		user = (User) intent.getSerializableExtra("userKey");
		
		if (user.getRole().equals("Nurse")) {
			user = (Nurse) user;
		} else if (user.getRole().equals("Physician")) {
			user = (Physician) user;
			Button button = (Button) findViewById(R.id.add_visit);
			button.setVisibility(View.INVISIBLE);
		}

		TextView nameText = (TextView) findViewById(R.id.patient_name);
		if (patient.getName()[1] == null) {
			nameText.setText(String.format("%s %s", patient.getName()[0], 
					   								patient.getName()[2]));
		} else {
			nameText.setText(String.format("%s %s %s", patient.getName()[0], 
													   patient.getName()[1],
												       patient.getName()[2]));
		}
		
		TextView ageText = (TextView) findViewById(R.id.patient_age);
		ageText.setText(String.valueOf(patient.getAge()));

		TextView hcnText = (TextView) findViewById(R.id.patient_hcn);
		hcnText.setText(patient.getHealthCardNum());

		TextView dobText = (TextView) findViewById(R.id.patient_dob);
		dobText.setText(new SimpleDateFormat("yyyy/MM/dd").format(patient
				.getDob()));
	}

	public void getVisit(View view) {
		int i = Nurse.getPatients().get(patient.getHealthCardNum())
				.getPatientHistory().getVisits().size();
		if (i > 0) {
			Intent intent = new Intent(this, GetVisitActivity.class);
			intent.putExtra("hcnKey", patient.getHealthCardNum());
			intent.putExtra("userKey", user);
			startActivity(intent);
		} else {
			String messgage = "No Visits exist for this Patient";
			Toast.makeText(getApplicationContext(), messgage,
					Toast.LENGTH_SHORT).show();
		}
	}

	public void addVisit(View view) {
		Intent intent = new Intent(this, AddVisitActivity.class);
		intent.putExtra("hcnKey", patient.getHealthCardNum());
		intent.putExtra("nurseKey", user);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_record, menu);
		return true;
	}

}