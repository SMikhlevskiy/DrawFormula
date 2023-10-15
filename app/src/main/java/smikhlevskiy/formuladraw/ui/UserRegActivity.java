/** <h1>UserRegActivity</h1>
 *  Activity for registration user || add new User    
 *  @author  SMikhlevskiy
 *  @version 1.0
 *  @since   2015-03
 */
package smikhlevskiy.formuladraw.ui;



import smikhlevskiy.formuladraw.R;



import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class UserRegActivity extends AppCompatActivity {
	private Button buttonNewUserReg;
	private Button butNewUserCansel;
	private Button butNewUserOK;
	private Button butCansel;
	private Button butOK;
	private TableLayout layoutNewUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userreg);
		
		layoutNewUser = (TableLayout) findViewById(R.id.LayoutNewUser);
		buttonNewUserReg = (Button) findViewById(R.id.butNewReg);

		butNewUserCansel = (Button) findViewById(R.id.butNewUserCansel);
		butNewUserOK = (Button) findViewById(R.id.butNewUserOK);

		butCansel = (Button) findViewById(R.id.butCansel);
		butOK = (Button) findViewById(R.id.butOK);

		layoutNewUser.setVisibility(View.INVISIBLE);
		// --------Button OK--------------------------
		butOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String login = ((EditText) findViewById(R.id.editText1)).getText().toString();
				String password = ((EditText) findViewById(R.id.editText2)).getText().toString();

//				ParseUser.logInInBackground(login, password, new LogInCallback() {
//					public void done(ParseUser user, ParseException e) {
//						if (user != null) {
//
//							Toast.makeText(getApplicationContext(), getString(R.string.regOK), Toast.LENGTH_LONG)
//									.show();
//							finish();
//						} else {
//							Toast.makeText(getApplicationContext(), getString(R.string.regBad) + e.toString(),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//
//				});
			}

		});
		// -----------------Button Cansel-------------
		butCansel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// ---------------Button New User Registration
		buttonNewUserReg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutNewUser.setVisibility(View.VISIBLE);
			}
		});
		// ---------------Button New User Registration OK----
		butNewUserOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String usernametxt = ((EditText) findViewById(R.id.editTextLogin)).getText().toString();
				String passwordtxt = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
				String emailtxt = ((EditText) findViewById(R.id.editTextEMale)).getText().toString();

				if (usernametxt.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.errorLogin), Toast.LENGTH_LONG).show();
					return;
				}
				if (passwordtxt.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.errorPassword), Toast.LENGTH_LONG)
							.show();
					return;
				}
				if (emailtxt.equals("")) {
					Toast.makeText(getApplicationContext(), getString(R.string.errorEmail), Toast.LENGTH_LONG).show();
					return;
				}

				//
//				ParseUser user = new ParseUser();
//				user.setUsername(usernametxt);
//				user.setPassword(passwordtxt);
//				user.setEmail(emailtxt);
//
//				user.signUpInBackground(new SignUpCallback() {
//					public void done(ParseException e) {
//						if (e == null) {
//
//							Toast.makeText(getApplicationContext(), getString(R.string.newUserRegOK), Toast.LENGTH_LONG)
//									.show();
//						} else {
//							Toast.makeText(getApplicationContext(), getString(R.string.newUserRegBad) + e.toString(),
//									Toast.LENGTH_LONG).show();
//						}
//					}
//				});

				layoutNewUser.setVisibility(View.INVISIBLE);
			}
		});
		// ------------------Button New User Registration Cansel---
		butNewUserCansel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layoutNewUser.setVisibility(View.INVISIBLE);
			}
		});

	}
}
