package csc207b07.ertriage.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.util.Xml;

/**
 * @author Lucas
 * 
 *         The class handles anything regarding reading the XML Files or writing
 *         them (all I/O). Much of the code here was created using
 *         "http://developer.android.com/training/basics/network-ops/xml.html#
 *         instantiate" for reference.
 */
/**
 * @author Lucas
 * 
 */
public class XMLParser {

	private static final String ns = null;

	/**
	 * The method will parse the Patients XML file held within local storage
	 * that contains all saved patients.
	 * 
	 * We don't use namespaces
	 * 
	 * @param in
	 * @return Map<String, Patient> representation of the Patients with the
	 *         health card number as the key
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public Map<String, Patient> parse(Context context)
			throws XmlPullParserException, IOException {
		FileInputStream in = null;
		File file = new File(context.getApplicationContext().getFilesDir(),
				"Patients.xml");
		try {
			in = new FileInputStream(file.getPath());
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Parses at the Patients level.
	 * 
	 * @param parser
	 * @return Map<String, Patient> representation of the Patients with the
	 *         health card number as the key
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private Map<String, Patient> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Map<String, Patient> patients = new HashMap<String, Patient>();

		parser.require(XmlPullParser.START_TAG, ns, "patients");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("patient")) {
				Patient patient = readPatient(parser);
				patients.put(patient.getHealthCardNum(), patient);
			} else {
				skip(parser);
			}
		}
		return patients;
	}

	/**
	 * Parses the contents of an entry. If it encounters a name, dob,
	 * healthCardNum, or a patientHistory it hands them off to their respective
	 * &quot;read&quot; methods for processing. Otherwise, skips the tag.
	 * 
	 * @param parser
	 * @return A Patient object
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private Patient readPatient(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "patient");
		String[] name = null;
		Date dob = null;
		String hcn = null;
		PatientHistory patientHist = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("name")) {
				name = readName(parser);
			} else if (tag.equals("dob")) {
				dob = new Date(readElement(parser, "dob"));
			} else if (tag.equals("healthCardNum")) {
				hcn = readElement(parser, "healthCardNum");
			} else if (tag.equals("patientHistory")) {
				patientHist = readPatientHist(parser);
			} else {
				skip(parser);
			}
		}
		return new Patient(name, dob, hcn, patientHist);
	}

	/**
	 * Processes tags in the feed.
	 * 
	 * @param parser
	 * @param tag
	 * @return String text value of an element in the XML file
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readElement(XmlPullParser parser, String tag)
			throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, tag);
		String var = readText(parser);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
		}
		return var;
	}

	/**
	 * Parses the name element.
	 * 
	 * @param parser
	 * @return String[] representation of the first, middle and last name
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private String[] readName(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String[] name = new String[3];

		parser.require(XmlPullParser.START_TAG, ns, "name");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			// Starts by looking for the entry tag
			if (tag.equals("fName")) {
				name[0] = readElement(parser, "fName");
			} else if (tag.equals("mName")) {
				name[1] = readElement(parser, "mName");
			} else if (tag.equals("lName")) {
				name[2] = readElement(parser, "lName");
			} else {
				skip(parser);
			}
		}
		return name;
	}

	/**
	 * Parses at the PatientHistory level
	 * 
	 * @param parser
	 * @return A PatientHistory object
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private PatientHistory readPatientHist(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		PatientHistory patientHist = new PatientHistory();

		parser.require(XmlPullParser.START_TAG, ns, "patientHistory");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("visit")) {
				patientHist.addVisit(readVisit(parser));
			} else {
				skip(parser);
			}
		}
		return patientHist;

	}

	/**
	 * Parses at the Visit level
	 * 
	 * @param parser
	 * @return a Visit object
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private Visit readVisit(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "visit");
		Date timeOfArrival = null;
		Date timeSeenDoc = null;
		boolean seenByDoc = false;
		String[] doctor = new String[3];
		List<Vitals> vitals = new ArrayList<Vitals>();
		List<Prescription> prescriptions = new ArrayList<Prescription>();
		int urgency = 0;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("timeOfArrival")) {
				timeOfArrival = new Date(readElement(parser, "timeOfArrival"));
			} else if (tag.equals("seenByDr")) {
				seenByDoc = Boolean
						.parseBoolean(readElement(parser, "seenByDr"));
			} else if (tag.equals("doctor")) {
				parser.nextTag();
				doctor = readName(parser);
				parser.nextTag();
			} else if (tag.equals("timeSeenDoc")) {
				timeSeenDoc = new Date(readElement(parser, "timeSeenDoc"));
			} else if (tag.equals("urgency")) {
				urgency = Integer.parseInt(readElement(parser, "urgency"));
			} else if (tag.equals("vitals")) {
				vitals = readVitals(parser);
			} else if (tag.equals("prescriptions")) {
				prescriptions = readPrescriptions(parser);
			} else {
				skip(parser);
			}
		}
		return new Visit(seenByDoc, timeOfArrival, doctor, vitals, urgency,
				prescriptions, timeSeenDoc);
	}

	/**
	 * Parses at the Prescriptions level
	 * 
	 * @param parser
	 * @return a List of Prescription
	 */
	private List<Prescription> readPrescriptions(XmlPullParser parser) {
		List<Prescription> prescriptions = new ArrayList<Prescription>();

		try {
			parser.require(XmlPullParser.START_TAG, ns, "prescriptions");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();
				if (name.equals("prescription")) {
					prescriptions.add(readPrescription(parser));
				} else {
					skip(parser);
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prescriptions;
	}

	/**
	 * Parses at the Prescription level
	 * 
	 * @param parser
	 * @return a Prescription object
	 */
	private Prescription readPrescription(XmlPullParser parser) {
		String name = null;
		String instructions = null;
		try {
			parser.require(XmlPullParser.START_TAG, ns, "prescription");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String tag = parser.getName();
				if (tag.equals("name")) {
					name = readElement(parser, "name");
				} else if (tag.equals("instructions")) {
					instructions = readElement(parser, "instructions");
				} else {
					skip(parser);
				}
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Prescription(name, instructions);
	}

	/**
	 * Parses at the Vitals level
	 * 
	 * @param parser
	 * @return A List<Vitals>
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private List<Vitals> readVitals(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Vitals> vitals = new ArrayList<Vitals>();

		parser.require(XmlPullParser.START_TAG, ns, "vitals");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("vital")) {
				vitals.add(readVital(parser));
			} else {
				skip(parser);
			}
		}
		return vitals;
	}

	/**
	 * Parses at the Vital level
	 * 
	 * @param parser
	 * @return Vitals
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private Vitals readVital(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "vital");
		double temp = 0;
		int[] bloodPressure = new int[2];
		int heartRate = 0;
		Date time = null;
		String symptoms = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("temperature")) {
				temp = Double.parseDouble(readElement(parser, "temperature"));
			} else if (tag.equals("bloodPressure")) {
				bloodPressure = readBloodPressure(parser);
			} else if (tag.equals("heartRate")) {
				heartRate = Integer.parseInt(readElement(parser, "heartRate"));
			} else if (tag.equals("time")) {
				time = new Date(readElement(parser, "time"));
			} else if (tag.equals("symptoms")) {
				symptoms = readElement(parser, "symptoms");
			} else {
				skip(parser);
			}
		}

		return new Vitals(temp, bloodPressure, heartRate, time, symptoms);
	}

	/**
	 * Parses the blood pressure (Systolic, Diastolic)
	 * 
	 * @param parser
	 * @return int[] representation of blood pressure (systolic, diastolic)
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private int[] readBloodPressure(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		int[] bloodPressure = new int[2];

		parser.require(XmlPullParser.START_TAG, ns, "bloodPressure");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("systolic")) {
				bloodPressure[0] = Integer.parseInt(readElement(parser,
						"systolic"));
			} else if (name.equals("diastolic")) {
				bloodPressure[1] = Integer.parseInt(readElement(parser,
						"diastolic"));
			} else {
				skip(parser);
			}
		}
		return bloodPressure;
	}

	/**
	 * For the tags and extracts their text values.
	 * 
	 * @param parser
	 * @return String Text value of element
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			// parser.nextTag();
		}
		return result;
	}

	/**
	 * Skips tags the parser isn't interested in. Uses depth to handle nested
	 * tags. i.e., if the next tag after a START_TAG isn't a matching END_TAG,
	 * it keeps going until it finds the matching END_TAG (as indicated by the
	 * value of "depth" being 0). This method was taken from
	 * developer.android.com, the full link was mentioned above.
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

	/**
	 * Parses the User XML file and returns the password associated with the
	 * userName
	 * 
	 * @param in
	 * @param userName
	 * @return String representation of the password
	 * @throws NoUserFoundException
	 * @throws IOException
	 */
	public String getPass(InputStream in, String userName)
			throws NoUserFoundException, IOException {
		String password = null;
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			User user = getUsers(parser).get(userName);
			if (user != null) {
				password = user.getPassword();
			} else {
				throw new NoUserFoundException();
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
		return password;
	}

	/**
	 * Parses the User XML file and returns a collection of Users
	 * 
	 * @param parser
	 * @return Map<String, User> representation of the Users with the user name
	 *         as a key
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public Map<String, User> getUsers(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		Map<String, User> users = new HashMap<String, User>();

		parser.require(XmlPullParser.START_TAG, ns, "users");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("user")) {
				User user = readUser(parser);
				users.put(user.getUsername(), user);
			} else {
				skip(parser);
			}
		}
		return users;
	}

	/**
	 * Parses at the User level
	 * 
	 * @param parser
	 * @return User
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private User readUser(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "user");
		String[] name = null;
		String userName = null;
		String password = null;
		String role = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String tag = parser.getName();
			if (tag.equals("name")) {
				name = readName(parser);
			} else if (tag.equals("userName")) {
				userName = readElement(parser, "userName");
			} else if (tag.equals("password")) {
				password = readElement(parser, "password");
			} else if (tag.equals("role")) {
				role = readElement(parser, "role");
			} else {
				skip(parser);
			}
		}
		if (role.equals("Nurse")) {
			return new Nurse(name, userName, password);
		} else {
			return new Physician(name, userName, password);
		}
	}

	/**
	 * Adds a new User to the XML file containing the collection of Users
	 * 
	 * @param in
	 * @param user
	 * @return String representation of the XML file
	 */
	public String writeUserXML(InputStream in, User newUser) {
		XmlPullParser parser = Xml.newPullParser();
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			Map<String, User> users = getUsers(parser);
			users.put(newUser.getUsername(), newUser);

			List<User> listUsers = new ArrayList<User>(users.values());

			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "users");
			for (User user : listUsers) {
				writeUser(user, serializer);
			}
			serializer.endTag("", "users");
			serializer.endDocument();

		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return writer.toString();

	}

	/**
	 * writes a String representation of an XML file for a user
	 * 
	 * @param user
	 * @param xml
	 */
	private void writeUser(User user, XmlSerializer serializer) {
		try {
			serializer.startTag("", "user");
			serializer.startTag("", "userName");
			serializer.text(String.valueOf(user.getUsername()));
			serializer.endTag("", "userName");
			serializer.startTag("", "password");
			serializer.text(String.valueOf(user.getPassword()));
			serializer.endTag("", "password");
			serializer.startTag("", "name");
			serializer.startTag("", "fName");
			serializer.text(String.valueOf(user.getName()[0]));
			serializer.endTag("", "fName");
			if (user.getName()[1] != null) {
				serializer.startTag("", "mName");
				serializer.text(String.valueOf(user.getName()[1]));
				serializer.endTag("", "mName");
			}
			serializer.startTag("", "lName");
			serializer.text(String.valueOf(user.getName()[2]));
			serializer.endTag("", "lName");
			serializer.endTag("", "name");
			serializer.startTag("", "role");
			serializer.text(user.getRole());
			serializer.endTag("", "role");
			serializer.endTag("", "user");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Writes a Patient collection as a String in the form of a XML File
	 * 
	 * @param patients
	 * @return String representation of the XML file
	 */
	public String writePatientXML(Map<String, Patient> patients) {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		List<Patient> listPatients = new ArrayList<Patient>(patients.values());
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "patients");
			for (Patient patient : listPatients) {
				writePatient(patient, serializer);
			}
			serializer.endTag("", "patients");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a String representation of a patient
	 * 
	 * @param patient
	 * @param serializer
	 */
	public void writePatient(Patient patient, XmlSerializer serializer) {
		try {
			serializer.startTag("", "patient");
			serializer.startTag("", "name");
			serializer.startTag("", "fName");
			serializer.text(patient.getName()[0]);
			serializer.endTag("", "fName");
			if ((patient.getName()[1] != null)
					&& (!patient.getName()[1].equals(""))) {
				serializer.startTag("", "mName");
				serializer.text(patient.getName()[1]);
				serializer.endTag("", "mName");
			}
			serializer.startTag("", "lName");
			serializer.text(patient.getName()[2]);
			serializer.endTag("", "lName");
			serializer.endTag("", "name");
			serializer.startTag("", "dob");
			serializer.text(patient.getDob().toString());
			serializer.endTag("", "dob");
			serializer.startTag("", "healthCardNum");
			serializer.text(patient.getHealthCardNum());
			serializer.endTag("", "healthCardNum");
			writePatientHistory(patient.getPatientHistory(), serializer);
			serializer.endTag("", "patient");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a String representation of a patient History
	 * 
	 * @param patientHist
	 * @param serializer
	 */
	private void writePatientHistory(PatientHistory patientHist,
			XmlSerializer serializer) {
		try {
			serializer.startTag("", "patientHistory");
			for (Visit visit : patientHist.getVisits()) {
				writeVisit(visit, serializer);
			}
			serializer.endTag("", "patientHistory");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates a String representation of a Visit
	 * 
	 * @param visit
	 * @param serializer
	 */
	private void writeVisit(Visit visit, XmlSerializer serializer) {
		try {
			serializer.startTag("", "visit");
			serializer.startTag("", "timeOfArrival");
			serializer.text(visit.getTimeOfArrival().toString());
			serializer.endTag("", "timeOfArrival");
			serializer.startTag("", "seenByDr");
			serializer.text(String.valueOf(visit.isSeenByDoctor()));
			serializer.endTag("", "seenByDr");
			if (visit.isSeenByDoctor()) {
				serializer.startTag("", "doctor");
				serializer.startTag("", "name");
				serializer.startTag("", "fName");
				serializer.text(visit.getDoctor()[0]);
				serializer.endTag("", "fName");
				if (visit.getDoctor()[1] != null) {
					serializer.startTag("", "mName");
					serializer.text(visit.getDoctor()[1]);
					serializer.endTag("", "mName");
				}
				serializer.startTag("", "lName");
				serializer.text(visit.getDoctor()[2]);
				serializer.endTag("", "lName");
				serializer.endTag("", "name");
				serializer.endTag("", "doctor");
				serializer.startTag("", "timeSeenDoc");
				serializer.text(visit.getTimeSeenDoc().toString());
				serializer.endTag("", "timeSeenDoc");
			}
			serializer.startTag("", "vitals");

			for (Vitals vitals : visit.getVitals()) {
				writeVitals(vitals, serializer);
			}

			serializer.endTag("", "vitals");
			serializer.startTag("", "urgency");
			serializer.text(String.valueOf(visit.getUrgency()));
			serializer.endTag("", "urgency");

			if (visit.getPrescriptions().size() != 0) {
				serializer.startTag("", "prescriptions");
				for (Prescription prescription : visit.getPrescriptions()) {
					writePrescription(prescription, serializer);
				}
				serializer.endTag("", "prescriptions");
			}
			serializer.endTag("", "visit");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Creates a String representation of a Prescription
	 * 
	 * @param prescription
	 * @param serializer
	 */
	private void writePrescription(Prescription prescription,
			XmlSerializer serializer) {
		try {
			serializer.startTag("", "prescription");
			serializer.startTag("", "name");
			serializer.text(String.valueOf(prescription.getName()));
			serializer.endTag("", "name");
			serializer.startTag("", "instructions");
			serializer.text(prescription.getInstructions());
			serializer.endTag("", "instructions");
			serializer.endTag("", "prescription");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Creates a String representation of a Vital
	 * 
	 * @param vitals
	 * @param serializer
	 */
	private void writeVitals(Vitals vitals, XmlSerializer serializer) {
		try {
			serializer.startTag("", "vital");
			serializer.startTag("", "temperature");
			serializer.text(String.valueOf(vitals.getTemp()));
			serializer.endTag("", "temperature");
			serializer.startTag("", "bloodPressure");
			serializer.startTag("", "systolic");
			serializer.text(String.valueOf(vitals.getBloodPressure()[0]));
			serializer.endTag("", "systolic");
			serializer.startTag("", "diastolic");
			serializer.text(String.valueOf(vitals.getBloodPressure()[1]));
			serializer.endTag("", "diastolic");
			serializer.endTag("", "bloodPressure");
			serializer.startTag("", "heartRate");
			serializer.text(String.valueOf(vitals.getHeartRate()));
			serializer.endTag("", "heartRate");
			serializer.startTag("", "time");
			serializer.text(vitals.getTime().toString());
			serializer.endTag("", "time");
			if ((vitals.getSymptoms() != null)
					&& (!vitals.getSymptoms().equals(""))) {
				serializer.startTag("", "symptoms");
				serializer.text(vitals.getSymptoms());
				serializer.endTag("", "symptoms");
			}
			serializer.endTag("", "vital");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
