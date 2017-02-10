package com.bizconnectivity.tismobile.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.view.View;

import com.bizconnectivity.tismobile.database.models.UserDetail;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.UserWS;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class LoginActivity extends AppCompatActivity {

	//region declaration
	@BindView(R.id.text_username)
	TextInputEditText mEditTextUsername;
	@BindView(R.id.text_password)
	TextInputEditText mEditTextPassword;

	Realm realm;
	UserDetail userDetail;
	SharedPreferences sharedPref;
	View focusView;
	String username, password;
	//endregion

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		ButterKnife.bind(this);

		//realm initial
		Realm.init(this);
		realm = Realm.getDefaultInstance();

		//action bar
		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

		// clear all values in shared preference
		sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.clear().apply();
	}

	@OnClick(R.id.button_submit)
	public void btnSignIn(View view) {

		attemptLogin();
	}

	@OnClick(R.id.button_cancel)
	public void btnSignInCancel(View view) {

		cancelLogin();
	}

	private void attemptLogin() {

		// Reset errors.
		mEditTextUsername.setError(null);
		mEditTextPassword.setError(null);

		// Store values at the time of the login attempt.
		username = mEditTextUsername.getText().toString();
		password = mEditTextPassword.getText().toString();

		if (TextUtils.isEmpty(username)) {

			mEditTextUsername.setError(ERR_MSG_USERNAME_REQUIRED);
			focusView = mEditTextUsername;
			focusView.requestFocus();

		} else if (TextUtils.isEmpty(password)) {

			mEditTextPassword.setError(ERR_MSG_PASSWORD_REQUIRED);
			focusView = mEditTextPassword;
			focusView.requestFocus();

		} else {

			if (isNetworkAvailable(getApplicationContext())) {

				if (username.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {

					//save login username
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(SHARED_PREF_LOGIN_NAME, username).apply();

					//navigate to dashboard activity
					Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
					finish();
					startActivity(intent);

				} else {

					//check with webservice
					new loginAsync(username, password).execute();
				}

			} else {

				if (username.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {

					//save login username
					SharedPreferences.Editor editor = sharedPref.edit();
					editor.putString(SHARED_PREF_LOGIN_NAME, username).apply();

					//navigate to dashboard activity
					Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
					finish();
					startActivity(intent);

				} else {

					//check with local database
					checkUserLogin(username, password);
				}
			}
		}
	}

	private void cancelLogin() {

		// Reset errors and clear value.
		mEditTextUsername.setError(null);
		mEditTextPassword.setError(null);

		if (mEditTextUsername.length() > 0) {

			TextKeyListener.clear(mEditTextUsername.getText());
		}
		if (mEditTextPassword.length() > 0) {

			TextKeyListener.clear(mEditTextPassword.getText());
		}

		focusView = mEditTextUsername;
		focusView.requestFocus();

	}

	private void checkUserLogin(String username, String password) {

		String encryptedPassword = "";

		try {
			//encrypt password
			encryptedPassword = AESCrypt.encrypt(password, KEY_ENCRYPT);

		} catch (GeneralSecurityException e) {

			e.printStackTrace();
		}

		if (realm.where(UserDetail.class).equalTo("username", username).equalTo("password", encryptedPassword).count() == 0) {

			//prompt error message
			shortToast(getApplicationContext(), ERR_MSG_LOGIN_INCORRECT);

		} else {

			//save login username
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(SHARED_PREF_LOGIN_NAME, username).apply();

			//navigate to dashboard
			Intent intent = new Intent(this, DashboardActivity.class);
			finish();
			startActivity(intent);
		}
	}

	private class loginAsync extends AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		String username, password;
		boolean response;

		private loginAsync(String username, String password) {

			this.username = username;
			this.password = password;
		}

		@Override
		protected void onPreExecute() {

			//start progress dialog
			progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait..", "Loading...", true);
		}

		@Override
		protected Void doInBackground(String... params) {

			response = UserWS.invokeLoginWS(username, password);

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(Void result) {

			if (response) {

				//save login username
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString(SHARED_PREF_LOGIN_NAME, username).apply();

				//insert or update login details to database
				try {
					//encrypt password
					final String encryptedPassword = AESCrypt.encrypt(password, KEY_ENCRYPT);

					realm.executeTransaction(new Realm.Transaction() {
						@Override
						public void execute(Realm realm) {

							userDetail = new UserDetail();
							userDetail.setUsername(username);
							userDetail.setPassword(encryptedPassword);

							realm.copyToRealmOrUpdate(userDetail);
						}
					});

				} catch (GeneralSecurityException e) {

					e.printStackTrace();
				}

				//close progress dialog
				progressDialog.dismiss();

				//navigate to dashboard activity
				Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
				finish();
				startActivity(intent);

			} else {

				//close progress dialog
				progressDialog.dismiss();

				//prompt error message
				shortToast(getApplicationContext(), ERR_MSG_LOGIN_INCORRECT);
			}
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		realm.close();
	}
}

