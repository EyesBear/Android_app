package csc207b07.ertriage.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.Physician;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetVisitActivity extends Activity {

	private User user;
	private String hcn;
	private int visitsLen;

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_visit);
		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("userKey");
		hcn = (String) intent.getSerializableExtra("hcnKey");
		visitsLen = PatientCollection.getPatients().get(hcn)
				.getPatientHistory().getVisits().size();

		if (user.getRole().equals("Nurse")) {
			user = (Nurse) user;
		} else if (user.getRole().equals("Physician")) {
			user = (Physician) user;
		}

		List<String> datesList = new ArrayList<String>();

		// Converts all dates to "YYYY/MM/DD HH:MM TMZ" strings
		for (int i = visitsLen - 1; i >= 0; i--) {
			String date = new SimpleDateFormat("yyyy/MM/dd HH:mm zzz")
					.format(PatientCollection.getPatients().get(hcn)
							.getPatientHistory().getVisit(i).getTimeOfArrival());
			datesList.add(date);
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, datesList);

		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);

		// Executes when a date is clicked
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intentN = new Intent(GetVisitActivity.this,
						ViewVisitActivity.class);
				intentN.putExtra("posKey", visitsLen - 1 - position);
				intentN.putExtra("hcnKey", hcn);
				intentN.putExtra("userKey", user);
				GetVisitActivity.this.startActivity(intentN);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_visit, menu);
		return true;
	}

}