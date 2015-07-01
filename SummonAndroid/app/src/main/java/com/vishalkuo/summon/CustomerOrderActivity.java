package com.vishalkuo.summon;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CustomerOrderActivity extends Activity {
    private RecyclerView recyclerView;
    private Context c;
    private AlertDialog alertDialog;
    private ActionBar actionBar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)findViewById(R.id.spinner);

        recyclerView.setHasFixedSize(true);

        c = this;
        actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(null,c);
        recyclerView.setAdapter(menuItemAdapter);
        recyclerView.setLayoutManager(llm);

        new recycleInflater(new OnAsyncFinish() {
            @Override
            public void asyncDidFinish(MenuItemInfo result) {
                postRequest(result);
            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_order, menu);
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
        }else if (id == android.R.id.home){
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postRequest(final MenuItemInfo item){

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("Order: " + item.name);
        builder.setMessage("The price of this item is: " + item.basePrice + ". Click OK to order.");
        builder

                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TableRequest tableRequest = new TableRequest(CurrentTableSingleton.getInstance().getString(),
                                "Order: " + item.name, "0");

                        RestAdapter restAdapter = new RestAdapter.Builder()
                                .setEndpoint(ConfigGlobals.CONFIGURL)
                                .build();

                        RetroService service = restAdapter.create(RetroService.class);

                        service.newPostTask(tableRequest, new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {
                                Log.d("app", "here");
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
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

    /**
     * RECYCLER VIEW INFLATER
     */
    private class recycleInflater extends AsyncTask<String, Void, MenuItemAdapter>{
        private List<MenuItemInfo> itemInfo = new ArrayList<>();
        private MenuItemAdapter itemAdapter;
        public OnAsyncFinish delegate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        public recycleInflater(OnAsyncFinish delegate) {
            this.delegate = delegate;
        }

        @Override
        protected MenuItemAdapter doInBackground(String... strings) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ConfigGlobals.CONFIGURL)
                    .build();

            RetroGet retroTestro = adapter.create(RetroGet.class);
            List<MenuItemInfo> itemStuff = retroTestro.resultList();
            for (MenuItemInfo item : itemStuff) {
                itemInfo.add(item);
                itemAdapter = new MenuItemAdapter(itemInfo, getApplicationContext());

            }
            return itemAdapter;
        }

        @Override
        protected void onPostExecute(MenuItemAdapter s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(s);

            final GestureDetector GESTUREDETECTOR = new GestureDetector(c, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

                    if (child != null && GESTUREDETECTOR.onTouchEvent(e)){
                        int i = recyclerView.getChildPosition(child);
                        MenuItemInfo item = itemInfo.get(i);
                        delegate.asyncDidFinish(item);
                    }
                        return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }
            });

        }
    }
}
