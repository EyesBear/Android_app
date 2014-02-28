package csc207b07.ertriage.activities;

import java.util.List;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.Patient;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListByUrgencyActivity extends Activity {

	private Nurse user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_by_urgency);
		Intent intent = getIntent();
		user = (Nurse) intent.getSerializableExtra("userKey");

		/*
		 * gets the sorted list of Patients that hasn't been seen by a
		 * Physician.
		 */
		List<String> sortedPatients = user.getPatientsByUrgency();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, sortedPatients);

		// Instantiate a new ListView and assign ArrayAdapter to it.
		final ListView listView = (ListView) findViewById(R.id.sort_by_urgency);
		listView.setAdapter(adapter);

		// Set a Listener to find out which Patient is clicked.
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent nextIntent = new Intent(ListByUrgencyActivity.this,
						PatientRecordActivity.class);
				/*
				 * Pass data to Intent and go to next Intent depending on user
				 * click.
				 */
				String hcn = (String) listView.getItemAtPosition(position);
				Patient patient = user.getPatient(hcn);
				nextIntent.putExtra("patientKey", patient);
				nextIntent.putExtra("userKey", user);
				ListByUrgencyActivity.this.startActivity(nextIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_by_urgency, menu);
		return true;
	}

}