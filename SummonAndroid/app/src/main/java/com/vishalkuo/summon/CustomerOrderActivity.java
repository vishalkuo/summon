package com.vishalkuo.summon;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CustomerOrderActivity extends Activity {
    private RecyclerView recyclerView;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        c = this;
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        new recycleInflater(new OnAsyncFinish() {
            @Override
            public void asyncDidFinish(String result) {
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void postRequest(String item){
        TableRequest tableRequest = new TableRequest(CurrentTableSingleton.getInstance().getString(),
                "Order: " + item, "0");

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

    /**
     * RECYCLER VIEW INFLATER
     */
    private class recycleInflater extends AsyncTask<String, Void, MenuItemAdapter>{
        private List<rInfo> itemInfo = new ArrayList<>();
        private MenuItemAdapter itemAdapter;
        public OnAsyncFinish delegate = null;

        public recycleInflater(OnAsyncFinish delegate) {
            this.delegate = delegate;
        }

        @Override
        protected MenuItemAdapter doInBackground(String... strings) {
            RestAdapter adapter = new RestAdapter.Builder()
                    .setEndpoint(ConfigGlobals.CONFIGURL)
                    .build();

            RetroGet retroTestro = adapter.create(RetroGet.class);
            List<rInfo> itemStuff = retroTestro.resultList();
            for (rInfo item : itemStuff) {
                itemInfo.add(item);
                itemAdapter = new MenuItemAdapter(itemInfo, getApplicationContext());

            }
            return itemAdapter;
        }

        @Override
        protected void onPostExecute(MenuItemAdapter s) {
            super.onPostExecute(s);

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
                        String item = itemInfo.get(i).name;
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
