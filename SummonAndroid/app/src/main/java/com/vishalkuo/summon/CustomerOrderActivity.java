package com.vishalkuo.summon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class CustomerOrderActivity extends Activity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        new recycleInflater().execute();
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

    private class recycleInflater extends AsyncTask<String, Void, MenuItemAdapter>{
        private List<rInfo> itemInfo = new ArrayList<>();
        private MenuItemAdapter itemAdapter;

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

        }
    }
}
