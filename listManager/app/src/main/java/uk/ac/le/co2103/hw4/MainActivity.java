package uk.ac.le.co2103.hw4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import uk.ac.le.co2103.hw4.Database.ShoppingListViewModel;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ShoppingListViewModel shoppingListViewModel;
    public static final int ADD_ITEM_ACTIVITY_REQUEST_CODE = 1;
    ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
            startActivityForResult(intent, ADD_ITEM_ACTIVITY_REQUEST_CODE);
        });


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new ItemListAdapter(new ItemListAdapter.ItemDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        shoppingListViewModel.getLiveShoppingLists().observe(this, items -> {
            adapter.submitList(items);
        });

        recyclerView.setOnClickListener( view -> {});

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int position) {

        ShoppingList shoppingList = adapter.getCurrentList().get(position);

        Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
        intent.putExtra("SHOPPING_LIST_REF", shoppingList);
        startActivityForResult(intent, ADD_ITEM_ACTIVITY_REQUEST_CODE);

    }

    @Override
    public void onItemLongClick(int position) {

        ShoppingList shoppingList = adapter.getCurrentList().get(position);

        new AlertDialog.Builder(this)
                .setTitle("Delete " + shoppingList.getName())
                .setMessage("Are you sure you want to delete this shopping list?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingListViewModel.deleteShoppingList(shoppingList);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}