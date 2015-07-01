package com.vishalkuo.summon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {
    private Button refillBtn, checkBtn, orderBtn, customBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refillBtn = (Button)findViewById(R.id.refillBtn);


        /**
         * On click listeners
         */
        refillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestAdapter restAdapter = new RestAdapter.Builder()
                        .setEndpoint(ConfigGlobals.CONFIGURL)
                        .build();

                RetroService service = restAdapter.create(RetroService.class);

                 service.newPostTask(new TableRequest("1", "Refills", ConfigGlobals.REFILLINT), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
