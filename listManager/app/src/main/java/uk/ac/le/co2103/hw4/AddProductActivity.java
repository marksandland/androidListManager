    package uk.ac.le.co2103.hw4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.ac.le.co2103.hw4.Database.ProductViewModel;
import uk.ac.le.co2103.hw4.Database.ShoppingListViewModel;
import uk.ac.le.co2103.hw4.Database.domain.Product;

public class AddProductActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_NAME = "uk.ac.le.co2103.REPLY_NAME";
    public static final String EXTRA_REPLY_QUANTITY = "uk.ac.le.co2103.REPLY_QUANTITY";
    public static final String EXTRA_REPLY_UNIT = "uk.ac.le.co2103.REPLY_UNIT";
    private EditText editTextName;
    private EditText editTextQuantity;
    private Spinner spinner;
    private int  shoppingListId;
    private ProductViewModel productViewModel;
    private ShoppingListViewModel shoppingListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        shoppingListId = Integer.parseInt(getIntent().getExtras().get(ShoppingListActivity.SHOPPING_LIST_REF).toString());
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        editTextName = findViewById(R.id.editTextName);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinner = findViewById(R.id.spinner);

        String shoppingListName = getIntent().getStringExtra(ShoppingListActivity.SHOPPING_LIST_NAME);
        Log.d("hi",shoppingListName);
        getSupportActionBar().setTitle("Add Product to " + shoppingListName);


        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();

            if (TextUtils.isEmpty(editTextName.getText()) ) {
                errorToast("Please enter a product name...");
            }
            else if (editTextName.getText().toString().length() >= 40) {
                errorToast("Please enter a shorter product name...");
            }
            else if (TextUtils.isEmpty(editTextQuantity.getText())) {
                errorToast("Please enter a quantity...");
            }
            else {
                boolean validQuantity = true;

                try {
                    float x = Float.parseFloat((editTextQuantity.getText().toString()));
                    if (x <= 0) {
                        errorToast("Please enter a positive quantity");
                        validQuantity = false;
                    }
                }
                catch (Exception e) {
                    errorToast("Please enter a valid quantity, can only be whole or decimal");
                    validQuantity = false;
                }
                if (validQuantity == true){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // DO your work here
                            boolean productExists = false;
                            List<Product> products = null;
                            products = productViewModel.getProductsByShoppingListId(shoppingListId);
                            if (products != null){
                                for (Product p : products){
                                    if (p.getName().toLowerCase().equals(editTextName.getText().toString().toLowerCase())){
                                        productExists = true;
                                        break;
                                    }
                                }
                            }
                            boolean finalProductExists = productExists;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (finalProductExists == false){
                                        String productName = editTextName.getText().toString();
                                        replyIntent.putExtra(EXTRA_REPLY_NAME, productName);

                                        float productQuantity = Float.parseFloat((editTextQuantity.getText().toString()));
                                        replyIntent.putExtra(EXTRA_REPLY_QUANTITY, productQuantity);

                                        String productUnit = spinner.getSelectedItem().toString();
                                        replyIntent.putExtra(EXTRA_REPLY_UNIT, productUnit);
                                        setResult(RESULT_OK, replyIntent);
                                        finish();

                                    }
                                    else {
                                        errorToast("Product already exists");
                                    }
                                }
                            });


                        }
                    }).start();


                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void errorToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}