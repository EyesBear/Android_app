package csc207b07.ertriage.activities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.Visit;
import csc207b07.ertriage.classes.Vitals;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddVisitActivity extends Activity {

	private Nurse nurse;
	private String hcn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_visit);
		Intent intent = getIntent();
		nurse = (Nurse) intent.getSerializableExtra("nurseKey");
		hcn = (String) intent.getSerializableExtra("hcnKey");
	}

	@SuppressWarnings("deprecation")
	public void addVisit(View view) {

		// Gets symptoms
		EditText symptomsText = (EditText) findViewById(R.id.symptoms_field);
		String symptoms = symptomsText.getText().toString();

		try {

			// Converts the "time of arrival" input into a Date type in order to
			// use it as a param in the Visit constuctor.
			EditText timeText = (EditText) findViewById(R.id.time_field);
			String[] timeString = timeText.getText().toString().split(":");
			int[] time = { Integer.parseInt(timeString[0]),
					Integer.parseInt(timeString[1]) };

			if (time[0] < 0 || time[0] > 23 || time[1] < 0 || time[1] > 59) {
				throw new NumberFormatException();
			}

			Date arrivalTime = new Date();
			arrivalTime.setHours(time[0]);
			arrivalTime.setMinutes(time[1]);

			// Gets temperature
			EditText tempText = (EditText) findViewById(R.id.temp_field);
			Double temp = Double.parseDouble(tempText.getText().toString());

			// Gets heart rate
			EditText heartRateText = (EditText) findViewById(R.id.heart_rate_field);
			int heartRate = Integer
					.parseInt(heartRateText.getText().toString());

			// Gets blood pressures
			EditText sysText = (EditText) findViewById(R.id.sys_field);
			EditText diaText = (EditText) findViewById(R.id.dia_field);
			int[] bloodPressure = {
					Integer.parseInt(sysText.getText().toString()),
					Integer.parseInt(diaText.getText().toString()) };

			List<Vitals> vitals = new ArrayList<Vitals>();
			vitals.add(new Vitals(temp, bloodPressure, heartRate, arrivalTime,
					symptoms));

			// Adds a new visit to PatientHistory
			Visit visit = new Visit(arrivalTime, vitals, PatientCollection
					.getPatients().get(hcn).getAge());
			PatientCollection.getPatients().get(hcn).getPatientHistory()
					.addVisit(visit);

			Toast.makeText(getApplicationContext(), "A new visit is added.",
					Toast.LENGTH_LONG).show();
			nurse.save(this);

			Intent newIntent = new Intent(this, ViewVisitActivity.class);
			newIntent.putExtra("userKey", nurse);
			newIntent.putExtra("hcnKey", hcn);
			newIntent.putExtra("posKey",
					PatientCollection.getPatients().get(hcn)
							.getPatientHistory().getVisits().size() - 1);
			startActivity(newIntent);
			finish();

		} catch (ArrayIndexOutOfBoundsException e) {
			Toast.makeText(getApplicationContext(),
					"Please add a colon(:) between units of time.",
					Toast.LENGTH_LONG).show();
		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					"Please input valid information.", Toast.LENGTH_LONG)
					.show();
		}
	}
}