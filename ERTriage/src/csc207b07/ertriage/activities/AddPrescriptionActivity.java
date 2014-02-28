package csc207b07.ertriage.activities;

import java.util.Map;

import csc207b07.ertriage.classes.Patient;
import csc207b07.ertriage.classes.PatientCollection;
import csc207b07.ertriage.classes.Physician;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class AddPrescriptionActivity extends Activity {

	private Physician physician;
	private String hcn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_prescription);
		
		@SuppressWarnings("unused")
		Map<String, Patient> test = PatientCollection.getPatients();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_prescription, menu);
		return true;
	}
	
	/**
	 * Save the Prescription that were input by the user, and create a new Prescription
	 * object for this Visit.
	 * 
	 * @param view
	 *            The View of the Activity.
	 */
	public void takePrescription(View view) {
		// Get the Intent passed from the previous activity.
		Intent intent = getIntent();

		// Create both local versions of the data passed from previous activity.
		physician = (Physician) intent.getSerializableExtra("userKey");
		hcn = (String) intent.getSerializableExtra("hcnKey");

		/*
		 * Take the input data from the fields, and convert it to String, int
		 * and int[] as necessary for the method recordVitals in nurse.
		 */
		EditText medicineText = (EditText) findViewById(R.id.medicine_field);
		String medicine = medicineText.getText().toString();

		EditText instructionText = (EditText) findViewById(R.id.instructions_field);
		String instructions = instructionText.getText().toString();
		
		//record the input prescription.
		physician.recordPrescription(hcn, medicine, instructions);
		
		//save it the our xml file.
		physician.save(this);
		finish();
		}
	}

