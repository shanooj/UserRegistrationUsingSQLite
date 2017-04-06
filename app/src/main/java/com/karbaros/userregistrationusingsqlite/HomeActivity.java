package com.karbaros.userregistrationusingsqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private Button btnLogOut;
    private DBAdapter dbAdapter;
    private String from ;
    private UserDetail userDetail;
    int id;
    private ConstraintLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbAdapter = new DBAdapter(getBaseContext());
        userDetail = new UserDetail();

        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        rootLayout = (ConstraintLayout) findViewById(R.id.cloutHomeActivity);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        from = (String) bundle.get("FROM");
        if (from.equals("login")) {
            id = (int) bundle.getInt("ID");
        } else if (from.equals("main")) {
            userDetail = (UserDetail) bundle.getSerializable("USER");
            id = userDetail.getId();
        } else {
            final Snackbar snackbar = Snackbar
                    .make(rootLayout, "Oooops! Something Wrong", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            snackbar.show();
        }


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbAdapter.updateData(Integer.toString(id), "INACTIVE")) {

                    Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
                    loginIntent.putExtra("USE_NAME",dbAdapter.getLatestUser());
                    startActivity(loginIntent);
                }

            }
        });
    }

}
