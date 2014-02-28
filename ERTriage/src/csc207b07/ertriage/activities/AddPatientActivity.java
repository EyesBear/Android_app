package csc207b07.ertriage.activities;

import java.util.Date;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.PatientHistory;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddPatientActivity extends Activity {

	private Nurse nurse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_patient);
		Intent intent = getIntent();
		nurse = (Nurse) intent.getSerializableExtra("userKey");
	}

	@SuppressWarnings("deprecation")
	/**
	 * Creates a new patient with is name, healthcard num, date of birth. 
	 * @throw NumberFormatException if the input dob is not reasonable, 
	 */
	public void addPatient(View view) {
		Intent newIntent = new Intent(this, PatientRecordActivity.class);
		// Gets the name
		TextView fNameText = (TextView) findViewById(R.id.first_name_field);
		TextView mNameText = (TextView) findViewById(R.id.middle_name_field);
		TextView lNameText = (TextView) findViewById(R.id.last_name_field);
		String[] name = { fNameText.getText().toString(),
				mNameText.getText().toString(), lNameText.getText().toString() };
		if (name[1].equals("")) {
			name[1] = null;
		}

		// Gets the hcn
		TextView hcnText = (TextView) findViewById(R.id.hcn_field);
		String hcn = hcnText.getText().toString();

		if (hcn.matches("\\d{10}[A-Z]{2}") || hcn.matches("\\d{12}")) {

			try {

				// Gets the dob
				TextView dobText = (TextView) findViewById(R.id.dob_field);
				String[] dobString = dobText.getText().toString().split("-");
				int[] time = { Integer.parseInt(dobString[0]),
						Integer.parseInt(dobString[1]),
						Integer.parseInt(dobString[2]) };

				// if dob input is invalid.
				if (time[1] < 1 || time[1] > 12 || time[2] < 1 || time[2] > 31) {
					throw (new NumberFormatException());
				}

				Date dob = new Date(time[0] - 1900, time[1] - 1, time[2]);

				if (!Nurse.getPatients().containsKey(hcn)) {

					// Creates a new patient and appends to the patient list
					PatientHistory history = new PatientHistory();
					nurse.createPatient(name, dob, hcn, history);

					// Toast message for patient successfully added.
					Toast.makeText(
							getApplicationContext(),
							String.format("%s %s has been added.", name[0],
									name[2]), Toast.LENGTH_SHORT).show();

					nurse.save(this);
					newIntent.putExtra("patientKey", nurse.getPatient(hcn));
					newIntent.putExtra("userKey", nurse);
					startActivity(newIntent);
					finish();
				} else {
					String messgage = "A Patient with that Health Card Number already exists";
					Toast.makeText(getApplicationContext(), messgage,
							Toast.LENGTH_SHORT).show();
				}

				// If dob input in incorrectly formatted.
			} catch (ArrayIndexOutOfBoundsException e) {
				Toast.makeText(getApplicationContext(),
						"Please add hyphens(-) between units of time.",
						Toast.LENGTH_LONG).show();

				// If input is invalid.
			} catch (NumberFormatException e) {
				Toast.makeText(getApplicationContext(),
						"Please input valid information.", Toast.LENGTH_LONG)
						.show();
			}
		} else {
			String messgage = "Please enter a valid health card number: 12 "
					+ "digits or 10 and 2 upper case letters";
			Toast.makeText(getApplicationContext(), messgage, Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_patient, menu);
		return true;
	}

}