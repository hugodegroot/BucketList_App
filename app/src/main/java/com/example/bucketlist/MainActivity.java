package com.example.bucketlist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, itemListener {

    private List<Item> ItemList;
    private ItemAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static final int REQUESTCODE = 1234;
    public static final String EXTRA_ITEM = "Item";

    private ItemRoomDatabase db;

    private GestureDetector mGestureDetector;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, UpdateActivity.class), REQUESTCODE);
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        ItemList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new ItemAdapter(ItemList, this);
        mRecyclerView.setAdapter(mAdapter);

        // Delete an item from the shopping list on long press.
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null) {
                    int adapterPosition = mRecyclerView.getChildAdapterPosition(child);
                    deleteItem(ItemList.get(adapterPosition));
                }
            }
        });

        mRecyclerView.addOnItemTouchListener(this);
        db = ItemRoomDatabase.getDatabase(this);
        getAllItems();

    }

    private void updateUI(List<Item> items) {
        ItemList.clear();
        ItemList.addAll(items);
        mAdapter.notifyDataSetChanged();
    }

    private void getAllItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<Item> items = db.itemDao().getAllItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(items);
                    }
                });
            }
        });
    }

    private void insertItem(final Item item) {

        executor.execute(new Runnable() {

            @Override

            public void run() {

                db.itemDao().insert(item);

                getAllItems();
            }
        });
    }


    private void deleteItem(final Item item) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(item);

                getAllItems();
            }
        });
    }

    private void deleteAllItems(final List<Item> items) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().delete(items);
                getAllItems();
            }
        });
    }

    private void updateBucketitem(final Item bucketitem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.itemDao().updateBucketitem(bucketitem);
                getAllItems(); // Because the Room database has been modified we need to get the new list of reminders.
            }
        });
    }

    @Override
    public void onItemClick(Item item) {
        item.setCompleted(!item.isCompleted());
        updateBucketitem(item);
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
        if (id == R.id.action_delete_item) {
            deleteAllItems(ItemList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {




            if (resultCode == RESULT_OK) {
                Item updatedItem = data.getParcelableExtra(MainActivity.EXTRA_ITEM);

                // New timestamp: timestamp of update
                insertItem(updatedItem);


            }

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
