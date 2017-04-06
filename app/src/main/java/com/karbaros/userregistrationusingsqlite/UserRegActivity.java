package com.karbaros.userregistrationusingsqlite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.karbaros.userregistrationusingsqlite.AppSupport.getPassword;

public class UserRegActivity extends AppCompatActivity {

    private ConstraintLayout rootLayout;
    private TextInputLayout tilUName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilNumber;
    private EditText etUName;
    private EditText etEmail;
    private EditText etNumber;
    private Button btnRegister;

    private String password;
    private UserDetail userDetail;
    private DBAdapter dbAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reg);
        dbAdapter = new DBAdapter(getBaseContext());

        initViews();
    }

    public void initViews() {
        rootLayout = (ConstraintLayout) findViewById(R.id.cloutRegActivity);
        tilUName = (TextInputLayout) findViewById(R.id.textInputLayoutUName);
        tilEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        tilNumber = (TextInputLayout) findViewById(R.id.textInputLayoutNumber);
        etUName = (EditText) findViewById(R.id.etUName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etNumber = (EditText) findViewById(R.id.etNumber);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // code for disable key board
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (validaInputs()) {
                    password = getPassword().toString();

                    if (!dbAdapter.checkUser(etEmail.getText().toString().trim())) {
                        userDetail = new UserDetail();

                        userDetail.setName(etUName.getText().toString().trim());
                        userDetail.setEmail(etEmail.getText().toString().trim());
                        userDetail.setNumber(Long.parseLong(etNumber.getText().toString().trim()));
                        userDetail.setPassword(password);
                        userDetail.setStatus("INACTIVE");

                        if (dbAdapter.insertData(userDetail)) {
                            //send password as mail
                            String subject = " Password ";
                            SendMail sendMail = new SendMail(rootLayout, getBaseContext(), etEmail.getText().toString().trim(), subject, password);
                            sendMail.execute();

                            final Snackbar snackbar = Snackbar
                                    .make(rootLayout, "Registration Success! Check Mail For Password", Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            snackbar.show();

                            Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                            loginIntent.putExtra("USE_NAME",dbAdapter.getLatestUser());
                            startActivity(loginIntent);

                        } else {

                            final Snackbar snackbar = Snackbar
                                    .make(rootLayout, "Registration Failed.. Try Again..", Snackbar.LENGTH_SHORT);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                            snackbar.show();

                        }


                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(rootLayout, "Email Already Exist!", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                                loginIntent.putExtra("USE_NAME",dbAdapter.getLatestUser());
                                startActivity(loginIntent);
                                snackbar.dismiss();
                            }
                        });
                        // Changing message text color
                        snackbar.setActionTextColor(Color.GREEN);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.DKGRAY);
                        snackbar.show();
                    }


                }
            }
        });
    }

    public boolean validaInputs() {
        boolean flagName, flagEmail, flagNumber;

        if (etUName.getText().toString().trim().equals("")) {
            flagName = false;
            tilUName.setErrorEnabled(true);
            tilUName.setError("Enter Name");
        } else {
            flagName = true;
            tilUName.setErrorEnabled(false);
            tilUName.setError("");
        }

        if (etEmail.getText().toString().trim().equals("")) {
            flagEmail = false;
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Enter Email");
        } else {
            if (isValidEmailAddress(etEmail.getText().toString().trim())) {
                flagEmail = true;
                tilEmail.setErrorEnabled(false);
                tilEmail.setError("");
            } else {
                flagEmail = false;
                tilEmail.setErrorEnabled(true);
                tilEmail.setError("Enter Proper Email");
            }
        }
        if (etNumber.getText().toString().trim().equals("") || etNumber.getText().toString().trim().length() < 10) {
            flagNumber = false;
            tilNumber.setErrorEnabled(true);
            tilNumber.setError("Enter Number");
        } else {
            flagNumber = true;
            tilNumber.setErrorEnabled(false);
            tilNumber.setError("");
        }
        if (flagName && flagEmail && flagNumber)
            return true;
        else
            return false;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
