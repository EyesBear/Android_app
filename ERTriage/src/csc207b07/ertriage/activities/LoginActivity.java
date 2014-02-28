package csc207b07.ertriage.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import csc207b07.ertriage.classes.NoUserFoundException;
import csc207b07.ertriage.classes.Nurse;
import csc207b07.ertriage.classes.Physician;
import csc207b07.ertriage.classes.User;
import csc207b07.ertriage.classes.XMLParser;
import csc207b07.ertriage.activities.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void Login(View view) {
		Intent intent = new Intent(this, MainActivity.class);
		XMLParser xmlparser = new XMLParser();
		InputStream in;
		User user;

		EditText userText = (EditText) findViewById(R.id.username_field);
		String username = userText.getText().toString();

		EditText passText = (EditText) findViewById(R.id.password_field);
		String password = passText.getText().toString();

		if (username.matches("\\w{1,16}") && password.matches(".{1,16}")) {
			try {
				if (login(username, password)) {
					in = getResources().getAssets().open("Users.xml");
					XmlPullParser parser = Xml.newPullParser();
					parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
							false);
					parser.setInput(in, null);
					parser.nextTag();

					user = xmlparser.getUsers(parser).get(username);

					if (user.getRole().equals("Nurse")) {
						intent.putExtra("userKey", new Nurse(user.getName(),
								user.getUsername(), user.getPassword(), this));
					} else if (user.getRole().equals("Physician")) {
						intent.putExtra(
								"userKey",
								new Physician(user.getName(), user
										.getUsername(), user.getPassword(),
										this));
					}
					startActivity(intent);
				} else {
					String messgage = "Username or password are incorrect";
					Toast.makeText(getApplicationContext(), messgage,
							Toast.LENGTH_SHORT).show();
				}

			} catch (NoUserFoundException e) {
				String messgage = "Username or password are incorrect";
				Toast.makeText(getApplicationContext(), messgage,
						Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			String message = "Please enter a valid username or password";
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public boolean login(String username, String password)
			throws NoUserFoundException {
		boolean check = false;
		FileInputStream in = null;
		File file = new File(getApplicationContext().getFilesDir(), "Users.xml");

		try {
			in = new FileInputStream(file.getPath());
			String pass = new XMLParser().getPass(in, username);
			if (pass.equals(password))
				check = true;
		} catch (NoUserFoundException e) {
			throw new NoUserFoundException();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return check;

	}

}
