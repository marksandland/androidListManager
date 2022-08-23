package uk.ac.le.co2103.hw4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Update;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.ProductViewModel;
import uk.ac.le.co2103.hw4.Database.ShoppingListViewModel;
import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class ShoppingListActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    public static final String SHOPPING_LIST_REF = "SHOPPING_LIST_REF";
    public static final String SHOPPING_LIST_NAME = "SHOPPING_LIST_NAME";
    public static final String PRODUCT_OBJ = "PRODUCT_OBJ";
    private static final String TAG = ShoppingListActivity.class.getSimpleName();
    RecyclerViewClickInterface recyclerViewClickInferface;
    private ShoppingListViewModel shoppingListViewModel;
    private ProductViewModel productViewModel;
    public static final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = 2;
    public static final int UPDATE_PRODUCT_ACTIVITY_REQUEST_CODE = 3;
    Intent intent;
    private LiveData<List<Product>> products;
    int shoppingListId;
    private ProductListAdapter adapter;
    ShoppingList shoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();

        try {
            shoppingList = (ShoppingList) intent.getExtras().get("SHOPPING_LIST_REF");
            shoppingListId = shoppingList.getId();
        }
        catch (Exception e) {
            if (savedInstanceState != null){
                shoppingListId = (int) savedInstanceState.get("SHOPPING_LIST_REF");
            }
        }

        setContentView(R.layout.activity_shopping_list);
        getSupportActionBar().setTitle(shoppingList.getName());

        FloatingActionButton fab = findViewById(R.id.fabAddProduct);
        fab.setOnClickListener( view -> {
            Intent intent = new Intent(ShoppingListActivity.this, AddProductActivity.class);
            intent.putExtra(SHOPPING_LIST_REF, shoppingListId);
            intent.putExtra(SHOPPING_LIST_NAME, shoppingList.getName());
            startActivityForResult(intent, ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview_products);
        adapter = new ProductListAdapter(new ProductListAdapter.ItemDiff(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        final Observer<String> productObserver = new Observer<String>(){
            @Override
            public void onChanged(String s) {
            }
        };

        productViewModel.getLiveProductsByShoppingListId(shoppingListId).observe(this, items -> {
            adapter.submitList(items);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // using extras, create product in db with the shopping list number
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == ADD_PRODUCT_ACTIVITY_REQUEST_CODE) {

                    String productName = data.getStringExtra(AddProductActivity.EXTRA_REPLY_NAME);
                    float productQuantity = (float) data.getExtras().get(AddProductActivity.EXTRA_REPLY_QUANTITY);
                    String productUnit = data.getStringExtra(AddProductActivity.EXTRA_REPLY_UNIT);
                    Product product = new Product(shoppingListId, productName, productQuantity, productUnit);
                    productViewModel.insert(product);
                }
                if (requestCode == UPDATE_PRODUCT_ACTIVITY_REQUEST_CODE) {


                    String productName = data.getStringExtra(AddProductActivity.EXTRA_REPLY_NAME);
                    float productQuantity = (float) data.getExtras().get(AddProductActivity.EXTRA_REPLY_QUANTITY);
                    String productUnit = data.getStringExtra(AddProductActivity.EXTRA_REPLY_UNIT);
                    Product oldProduct = (Product) data.getExtras().get(UpdateProductActivity.PRODUCT_OBJ_OLD);
                    oldProduct.setName(productName);
                    oldProduct.setQuantity(productQuantity);
                    oldProduct.setUnit(productUnit);
                    productViewModel.update(oldProduct);

                    Toast.makeText(
                            getApplicationContext(),
                            "product updated!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        }
        catch (Exception e) {
            Toast.makeText(
                    getApplicationContext(),
                    "Problem!",
                    Toast.LENGTH_LONG).show();
        }


        // load products


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("SHOPPING_LIST_REF", shoppingListId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(int position) {
        Product product = adapter.getCurrentList().get(position);

        new AlertDialog.Builder(this)
                .setTitle(product.getName())
                .setMessage("What do you want to do with this product?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        productViewModel.delete(product);
                    }
                })
                .setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ShoppingListActivity.this, UpdateProductActivity.class);
                        intent.putExtra(PRODUCT_OBJ, product);
                        intent.putExtra(SHOPPING_LIST_REF, shoppingListId);
                        intent.putExtra(SHOPPING_LIST_NAME, shoppingList.getName());
                        startActivityForResult(intent, UPDATE_PRODUCT_ACTIVITY_REQUEST_CODE);
                    }
                })
                .setNeutralButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onItemLongClick(int position) {

    }
}