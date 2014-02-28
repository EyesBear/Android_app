package csc207b07.ertriage.activities;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends Activity {

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		// The data passed from the previous Activity is stored.
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("userKey");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/*
		 * Inflate the menu; this adds items to the action bar if it is present.
		 */
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	/**
	 * Search a patient by it's hcn (health card number).
	 * 
	 * @param view
	 *            The View of the Activity.
	 */
	public void searchPatient(View view) {

		// Creating a new Intent, that would later pass to the next Activity.
		Intent nextIntent = new Intent(this, PatientRecordActivity.class);

		// Take the input data from the fields, and store as a String.
		EditText healthCardFieldText = (EditText) findViewById(R.id.health_card_field);
		String hcn = healthCardFieldText.getText().toString();

		if (hcn.matches("\\d{10}[A-Z]{2}") || hcn.matches("\\d{12}")) {

			/*
			 * Use hcn to check if such a Patient exists. If yes, continue to
			 * the next Activity, if no - Toast.
			 */
			if (Nurse.getPatients().containsKey(hcn)) {
				nextIntent.putExtra("patientKey", PatientCollection
						.getPatients().get(hcn));
				nextIntent.putExtra("userKey", user);
				startActivity(nextIntent);
			} else {
				String messgage = "No patient with the specified Health Card "
						+ "Number Exists";
				Toast.makeText(getApplicationContext(), messgage,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			String messgage = "Please enter a valid health card number";
			Toast.makeText(getApplicationContext(), messgage,
					Toast.LENGTH_SHORT).show();
		}
	}

}
