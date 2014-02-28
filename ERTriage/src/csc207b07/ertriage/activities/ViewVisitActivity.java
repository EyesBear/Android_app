package csc207b07.ertriage.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.Physician;
import csc207b07.ertriage.classes.Prescription;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.classes.Visit;
import csc207b07.ertriage.classes.Vitals;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class ViewVisitActivity extends Activity {

	private User user;
	private String hcn;
	private TextView docTimeOut, docNameOut, urgencyOut;
	private EditText docNameIn, dateIn, timeIn;
	private Visit visit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_visit);

		Intent intent = getIntent();
		user = (User) intent.getSerializableExtra("userKey");
		hcn = (String) intent.getSerializableExtra("hcnKey");
		int position = (Integer) intent.getSerializableExtra("posKey");
		
		Button recButton = (Button) findViewById(R.id.record_button);
		Button saveButton = (Button) findViewById(R.id.saving_button);
		docNameIn = (EditText) findViewById(R.id.doctor_name_field);
		dateIn = (EditText) findViewById(R.id.date_field);
		timeIn = (EditText) findViewById(R.id.time_field);
		TextView addDocText = (TextView) findViewById(R.id.add_doctor);
		List<Visit> visits = PatientCollection.getPatients().get(hcn).getPatientHistory().getVisits();
		visit = visits.get(position);

		// sets different features for different users and visits.
		if (user.getRole().equals("Nurse")) {
			user = (Nurse) user;
			recButton.setText("Record Vital Signs");
		} if (user.getRole().equals("Physician") || position != visits.size() - 1) {
			if (user.getRole().equals("Physician")) {
				user = (Physician) user;
				//if (visit.isSeenByDoctor() == false){
					//recButton.setVisibility(View.INVISIBLE);
				//}
				recButton.setText("Record Prescription");
			} if (position != visits.size() - 1 || visit.isSeenByDoctor() == false) {
				recButton.setVisibility(View.INVISIBLE);
			}
			saveButton.setVisibility(View.INVISIBLE);
			docNameIn.setVisibility(View.INVISIBLE);
			dateIn.setVisibility(View.INVISIBLE);
			timeIn.setVisibility(View.INVISIBLE);
			addDocText.setVisibility(View.INVISIBLE);
		}
		
		// Responds to the "record" button click.
		recButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (user.getRole().equals("Nurse")) {
					takeVitals(v);
				} else if (user.getRole().equals("Physician")) {
					addPrescription(v);
				}
			}
		});

		// Sets the "arrival time" output.
		TextView arrivalTimeOut = (TextView) findViewById(R.id.p_arrival_time);
		arrivalTimeOut.setText(new SimpleDateFormat("HH:mm zzz").format(visit
				.getTimeOfArrival()));

		// Sets the "urgency" output.
		urgencyOut = (TextView) findViewById(R.id.p_urgency);
		urgencyOut.setText(String.valueOf(visit.getUrgency()));

		// Sets the "time & date seen by doctor" output.
		docTimeOut = (TextView) findViewById(R.id.p_seen_by_doc);
		if (visit.getTimeSeenDoc() == null) {
			docTimeOut.setText("N/A");
		} else {
			docTimeOut.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm zzz")
					.format(visit.getTimeSeenDoc()));
		}

		// Sets the "doctor's name" output.
		docNameOut = (TextView) findViewById(R.id.p_doctor);
		docNameOutHelper();

		// Sets the "vital signs" output.
		vitalsOutHelper();
		
		// Sets the "prescriptions" output.
		prescriptionOutHelper();

		// Responds to the "doctor's name" + "date & time seen" input
		saveButton.setOnClickListener(new View.OnClickListener() {

			@SuppressWarnings("deprecation")
			public void onClick(View v) {

				String[] docName = docNameIn.getText().toString().split(" ");
				String[] timeStr = timeIn.getText().toString().split(":");
				String[] dateStr = dateIn.getText().toString().split("-");
					
				if (timeStr.length == 2 && dateStr.length == 3) {
					int[] time = {Integer.parseInt(timeStr[0]), 
								  Integer.parseInt(timeStr[1])};
					int[] date = {Integer.parseInt(dateStr[0]), 
								  Integer.parseInt(dateStr[1]), 
								  Integer.parseInt(dateStr[2])};
						
					if (time[0] >= 0 && time[0] <= 23 && time[1] >= 0 && time[1] <= 59 &&
						date[1] >= 1 && date[1] <= 12 && date[2] >= 1 && date[2] <= 31) {
						
						if (docName.length == 2 || docName.length == 3) {
							
							if (docName.length == 2) {
								visit.setDoctor(new String[] {docName[0], null, docName[1]});
							} else {
								visit.setDoctor(docName);
							}
								
							visit.setTimeSeenDoc(new Date(date[0] - 1900, date[1] - 1, date[2], 
												 time[0], time[1]));
							docNameOutHelper();
							docTimeOut.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm zzz")
							.format(visit.getTimeSeenDoc()));
							visit.setSeenByDoctor(true);
							((Nurse) user).save(getApplicationContext());
							
							Toast.makeText(getApplicationContext(), 
									"\"Doctor's name\" and \"time & date seen\" " +
									"have been added.", Toast.LENGTH_SHORT).show();
							
							} else {
								Toast.makeText(getApplicationContext(), 
										"Please enter name in the proper format " +
										"(e.g. John Quincy Adams).",
										Toast.LENGTH_SHORT).show();
								}
							} else {
								Toast.makeText(getApplicationContext(), 
										"Please enter date & time in the proper range " +
										"(Month: 1-12, Date: 1-31, Hour: 0-23, Min: 0-59).",
										Toast.LENGTH_LONG).show();
							}
					
					} else {
						Toast.makeText(getApplicationContext(), 
								"Please enter date & time in the proper format (YYYY-MM-DD HH:MM).",
								Toast.LENGTH_SHORT).show();
					}
			}	
		});
	}

	// Starts TakeVitalsActivity.
	public void takeVitals(View view) {
		Intent intent = new Intent(this, TakeVitalsActivity.class);
		intent.putExtra("userKey", user);
		intent.putExtra("hcnKey", hcn);
		startActivity(intent);
	}

	// Starts AddPrescriptionAcitivity.
	public void addPrescription(View view) {
		Intent intent = new Intent(this, AddPrescriptionActivity.class);
		intent.putExtra("userKey", user);
		intent.putExtra("hcnKey", hcn);
		startActivity(intent);
	}

	// Helper method for setting "doctor's name" output.
	private void docNameOutHelper() {
		if (visit.getDoctor() == null || visit.getDoctor()[0] == null) {
			docNameOut.setText("N/A");
		} else {
			if (visit.getDoctor()[1] == null) {
				docNameOut.setText(String.format("%s %s", visit.getDoctor()[0],
						visit.getDoctor()[2]));
			} else {
				docNameOut.setText(String.format("%s %s %s",
						visit.getDoctor()[0], visit.getDoctor()[1],
						visit.getDoctor()[2]));
			}
		}
	}
	
	// Helper method for setting "vital signs" output.
	private void vitalsOutHelper() {
		TextView vitalsOut = (TextView) findViewById(R.id.p_vitals);
		List<Vitals> vitals = new ArrayList<Vitals>();
		for (int i = visit.getVitals().size() - 1; i >= 0; i--) {
			vitals.add(visit.getVitals().get(i));
		}
		
		String record = "";
		for (Vitals v : vitals) {
			String time = new SimpleDateFormat("HH:mm zzz").format(v.getTime());
			String symptoms = v.getSymptoms();
			if (symptoms == null) { symptoms = "N/A"; }
			record += "\nTime Recorded: " + time + "\nBP: "
					+ v.getBloodPressure()[0] + ", " + v.getBloodPressure()[1]
					+ "\nTemp: " + v.getTemp() + "\nHR: " + v.getHeartRate()
					+ "\nSymptoms: " + symptoms + "\n";
			
		}
		vitalsOut.setText(record);
	}
	
	private void prescriptionOutHelper() {
		TextView presOut = (TextView) findViewById(R.id.p_prescriptions);
		List<Prescription> prescription = visit.getPrescriptions();
		String presStr = "";
		if (prescription.size() == 0) {
			presStr = "Medications: N/A\nInstructions: N/A";
		} else {
			String meds = "Medications: " + prescription.get(prescription.size() - 1).getName();
			String ins = "\nInstructions: " + prescription.get(prescription.size() - 1).getInstructions();
			
			for (int i = prescription.size() - 2; i >= 0; i--) {
				meds += "; " + prescription.get(i).getName();
				ins += "; " + prescription.get(i).getInstructions();
			}
			presStr = meds + ins;
		}
		presOut.setText(presStr);
	}

	// Updates the "vital signs", "prescriptions", and "urgency" output after change.
	@Override
	public void onResume(){
		super.onResume();
	    vitalsOutHelper();
	    prescriptionOutHelper();
	    urgencyOut.setText(String.valueOf(visit.getUrgency()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_visit, menu);
		return true;
	}

}