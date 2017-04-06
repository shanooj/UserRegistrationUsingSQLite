package com.karbaros.userregistrationusingsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DBAdapter dbAdapter;
    private UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbAdapter = new DBAdapter(getBaseContext());
        userDetail = new UserDetail();

        //Log.i("user",dbAdapter.getLatestUser());

        checkForActiveUser();
    }

    public void checkForActiveUser() {

        Cursor user = dbAdapter.checkActiveUser();
        if (user.getCount() > 0) {
            user.moveToFirst();
            do {
                userDetail.setId(Integer.parseInt(user.getString(0)));
                userDetail.setName(user.getString(1));
                userDetail.setEmail(user.getString(2));
                userDetail.setPassword(user.getString(3));
                userDetail.setNumber(Long.parseLong(user.getString(4)));
                userDetail.setStatus(user.getString(5));
            } while (user.moveToNext());
            Intent homeIntent = new Intent(getBaseContext(), HomeActivity.class);
            homeIntent.putExtra("FROM","main");
            homeIntent.putExtra("USER", userDetail);
            startActivity(homeIntent);
        } else {
            Intent loginIntent = new Intent(getBaseContext(), LoginActivity.class);
            loginIntent.putExtra("USE_NAME",dbAdapter.getLatestUser());
            startActivity(loginIntent);

        }
    }
}
