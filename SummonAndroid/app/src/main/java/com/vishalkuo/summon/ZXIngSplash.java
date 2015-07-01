package com.vishalkuo.summon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ZXIngSplash extends Activity {

    private Button scanBtn, nahBtn;
    private Context c;
    private AlertDialog alertDialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode,
                resultCode, data);

        if (scanningResult != null){
            String scanContent = scanningResult.getContents();
            CurrentTableSingleton.getInstance().setString(scanContent);
            Intent i = new Intent(c, MainActivity.class);
            startActivity(i);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_splash);

        scanBtn = (Button)findViewById(R.id.scanBtn);

        c = this;

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) c);
                scanIntegrator.initiateScan();
            }
        });

        nahBtn = (Button)findViewById(R.id.nahBtn);
        nahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(c);
                View promptsView = li.inflate(R.layout.nahlog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setView(promptsView);

                final EditText req = (EditText)promptsView.findViewById(R.id.nahLog);
                builder.setTitle("Enter a table number");
                builder.setMessage("Sort of beta");
                builder
                        .setCancelable(false)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                CurrentTableSingleton.getInstance().setString(req.getText().toString());
                                Intent i = new Intent(c, MainActivity.class);
                                startActivity(i);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_zxing_splash, menu);
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
            Toast.makeText(c, "Comic-Sans not enabled yet (ultra-beta)", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
