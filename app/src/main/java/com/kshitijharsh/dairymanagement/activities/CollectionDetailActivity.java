package com.kshitijharsh.dairymanagement.activities;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.kshitijharsh.dairymanagement.R;
import com.kshitijharsh.dairymanagement.adapters.CollectionAdapter;
import com.kshitijharsh.dairymanagement.database.DBQuery;
import com.kshitijharsh.dairymanagement.database.DatabaseClass;
import com.kshitijharsh.dairymanagement.model.Collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CollectionDetailActivity extends AppCompatActivity implements CollectionAdapter.CollectionAdapterListener {

    private RecyclerView recyclerView;
    DBQuery dbQuery;
    List<Collection> collectionList;
    DatabaseClass db;
    CollectionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Collection Details");

        dbQuery = new DBQuery(this);
        dbQuery.open();

        db = new DatabaseClass(this);
        db.getReadableDatabase();

        recyclerView = findViewById(R.id.collection_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        collectionList = new ArrayList<>();

        getCollDetails();
    }

    public void getCollDetails() {
        Cursor cursor = db.getAllCollection();
        int count = cursor.getCount();
        if (count == 0)
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String cb, me;
            if (cursor.getString(4).equals("C"))
                cb = "Cow";
            else
                cb = "Buffalo";
            if (cursor.getString(5).equals("1"))
                me = "Morning";
            else
                me = "Evening";
            Collection collection = new Collection(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cb, me, cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), cursor.getString(12));
            collectionList.add(collection);
//            Log.e("ERROR:mE deg zoonCode", "_id " + cursor.getString(0) + " trnDate " + cursor.getString(1) + " membCode " + cursor.getString(2) + " memName " + cursor.getString(3) + " cobf " + cursor.getString(4) + " " + cb + " " + me + " liters " + cursor.getString(7) + " fat " + cursor.getString(8) + " rate " + cursor.getString(9) + " amount " + cursor.getString(10) + " snf " + cursor.getString(12));
            cursor.moveToNext();
        }
        cursor.close();
        mAdapter = new CollectionAdapter(collectionList, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filter, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
//                Toast.makeText(MemberDetailActivity.this, "Filtered", Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
//                Toast.makeText(MemberDetailActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_filter) {
            return true;
        }
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCollectionSelected(Collection collection) {

    }
}
