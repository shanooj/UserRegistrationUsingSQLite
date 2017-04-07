package com.karbaros.userregistrationusingsqlite;

import android.content.Context;
import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etUserName;
    private EditText etPassword;
    private TextInputLayout tilUserName;
    private TextInputLayout tilPassword;
    private Button btnLogin;
    private TextView tvNewUser;
    private ConstraintLayout rootLayout;

    private DBAdapter dbAdapter;
    private int id;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        userName = (String) bundle.getString("USE_NAME");

        initViews();
        dbAdapter = new DBAdapter(getBaseContext());

    }

    public void initViews() {
        rootLayout = (ConstraintLayout) findViewById(R.id.cloutLoginActivity);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tilUserName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        tilPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvNewUser = (TextView) findViewById(R.id.tvNewUser);

        etUserName.setText(userName);
        etPassword.requestFocus();

        btnLogin.setOnClickListener(this);
        tvNewUser.setOnClickListener(this);

    }

    public boolean validateData() {
        boolean flag;
        boolean flag2;
        if (etUserName.getText().toString().trim().equals("")) {
            flag = false;
            /*Drawable drawable = getBaseContext().getResources().getDrawable(R.drawable.ic_person);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, Color.GREEN);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);*/
            //etUserName.setCompoundDrawablesWithIntrinsicBounds(getBaseContext().getResources().getDrawable(R.drawable.pe), null, null, null);
            tilUserName.setErrorEnabled(true);
            tilUserName.setError("Enter UserName");
        } else {
            flag = true;

            tilUserName.setErrorEnabled(false);
            tilUserName.setError("");
        }

        if (etPassword.getText().toString().trim().equals("")) {
            flag2 = false;
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Enter Password");
        } else {
            flag2 = true;
            tilPassword.setErrorEnabled(false);
            tilPassword.setError("");
        }
        if (flag == true && flag2 == true)
            return true;
        else
            return false;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogin:
                if (validateData()) {
                    // code for disable key board
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    id = dbAdapter.loginCheck(etUserName.getText().toString().trim(), etPassword.getText().toString().trim());
                    if (id != -1) {
                        if (dbAdapter.updateData(Integer.toString(id), "ACTIVE")) {

                            Intent loginIntent = new Intent(getBaseContext(), HomeActivity.class);
                            loginIntent.putExtra("FROM","login");
                            loginIntent.putExtra("ID", id);
                            startActivity(loginIntent);
                        }
                    } else {
                        final Snackbar snackbar = Snackbar
                                .make(rootLayout, "Wrong Entry.. Try Again", Snackbar.LENGTH_LONG);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.show();
                    }
                }
                break;
            case R.id.tvNewUser:
                Intent regIntent = new Intent(getBaseContext(), UserRegActivity.class);
                startActivity(regIntent);
                break;
                /*// Snackbar.make(rootLayout, "new user", Snackbar.LENGTH_SHORT).show();

                final Snackbar snackbar = Snackbar
                        .make(rootLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();*/


        }

    }
}
