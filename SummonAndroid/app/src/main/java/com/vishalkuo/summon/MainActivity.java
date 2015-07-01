package com.vishalkuo.summon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {
    private Button refillBtn, checkBtn, orderBtn, customBtn;
    private Context c;
    private AlertDialog alertDialog;
    private String tableNo;
    private TextView welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableNo = CurrentTableSingleton.getInstance().getString();
        refillBtn = (Button)findViewById(R.id.refillBtn);
        checkBtn = (Button)findViewById(R.id.checkBtn);
        orderBtn = (Button)findViewById(R.id.orderBtn);
        customBtn = (Button)findViewById(R.id.customBtn);
        welcomeView = (TextView)findViewById(R.id.welcomeView);
        welcomeView.setText("You are currently at table: " + tableNo);
        c = this;

        /**
         * On click listeners
         */
        refillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReq(0, null);
            }
        });
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postReq(1, null);
            }
        });
        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(c);
                View promptsView = li.inflate(R.layout.dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setView(promptsView);

                final EditText req = (EditText)promptsView.findViewById(R.id.requestInput);
                builder.setTitle("Make your request!");
                builder.setMessage("Enter your custom request here and a server will be with you shortly");
                builder
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                               postReq(2, req.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(c, CustomerOrderActivity.class);
                startActivity(i);
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

    private void postReq(int code, String customString){
        String task = "";
        String reqCode = "0";
        switch (code){
            case 0:
                task = "Refills";
                reqCode = ConfigGlobals.REFILLINT;
                break;
            case 1:
                task = "Check";
                reqCode = ConfigGlobals.CHECKINT;
                break;
            case 2:
                task = customString;
                reqCode = ConfigGlobals.CUSTOMINT;
                break;
        }

        TableRequest tableRequest = new TableRequest(tableNo, task, reqCode);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ConfigGlobals.CONFIGURL)
                .build();

        RetroService service = restAdapter.create(RetroService.class);

        service.newPostTask(tableRequest, new Callback<String>() {
            @Override
            public void success(String s, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
