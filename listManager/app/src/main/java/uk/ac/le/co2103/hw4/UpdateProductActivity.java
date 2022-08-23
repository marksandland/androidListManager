package uk.ac.le.co2103.hw4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Update;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.ProductViewModel;
import uk.ac.le.co2103.hw4.Database.ShoppingListViewModel;
import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class UpdateProductActivity extends AppCompatActivity {

    private Product product;
    private TextView productNameView;
    private TextView productQuantityView;
    private Spinner productUnitSpinner;
    private Button productQuantityPlusView;
    private Button productQuantityMinusView;
    private Button productSaveView;
    public static final String PRODUCT_OBJ_OLD = "PRODUCT_OBJ_OLD";
    public static final int UPDATE_PRODUCT_ACTIVITY_RETURN_REQUEST_CODE = 4;
    private ProductViewModel productViewModel;
    ShoppingListViewModel shoppingListViewModel;
    public static final String EXTRA_REPLY_NAME = "uk.ac.le.co2103.REPLY_NAME";
    public static final String EXTRA_REPLY_QUANTITY = "uk.ac.le.co2103.REPLY_QUANTITY";
    public static final String EXTRA_REPLY_UNIT = "uk.ac.le.co2103.REPLY_UNIT";
    private int shoppingListId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);
        shoppingListId = Integer.parseInt(getIntent().getExtras().get(ShoppingListActivity.SHOPPING_LIST_REF).toString());
        product = (Product) getIntent().getExtras().get(ShoppingListActivity.PRODUCT_OBJ);

        productNameView = findViewById(R.id.editTextName);
        productQuantityView = findViewById(R.id.editTextQuantity);
        productUnitSpinner = findViewById(R.id.spinner);
        productQuantityPlusView = findViewById(R.id.button_plus);
        productQuantityMinusView = findViewById(R.id.button_minus);
        productSaveView = findViewById(R.id.button_save);

        getSupportActionBar().setTitle("Update " + product.getName() + " in " + getIntent().getStringExtra(ShoppingListActivity.SHOPPING_LIST_NAME));

        productNameView.setText(product.getName());
        productQuantityView.setText(String.valueOf(product.getQuantity()));
        Log.d("Test", product.getUnit());
        switch (product.getUnit()){
            case "Kg": productUnitSpinner.setSelection(1); break;
            case "Liter": productUnitSpinner.setSelection(2); break;
            default: productUnitSpinner.setSelection(0);
        }

        productQuantityPlusView.setOnClickListener(view -> {

            boolean validQuantity = true;
            float updatedQuantity = 1;

            try {
                updatedQuantity = Float.parseFloat((productQuantityView.getText().toString())) + (float) 1;
                if (updatedQuantity <= 0) {
                    validQuantity = false;
                }
            }
            catch (Exception e) {
                errorToast("Invalid quantity");
                validQuantity = false;
            }

            if (validQuantity == true) {
                updatedQuantity = Math.round(updatedQuantity);
                productQuantityView.setText(String.valueOf(updatedQuantity));
            }
        });

        productQuantityMinusView.setOnClickListener(view -> {

            boolean validQuantity = true;
            float updatedQuantity = 1;

            try {
                updatedQuantity = Float.parseFloat((productQuantityView.getText().toString())) - (float) 1;
                if (updatedQuantity <= 0) {
                    validQuantity = false;
                }
            }
            catch (Exception e) {
                errorToast("Invalid quantity");
                validQuantity = false;
            }

            if (validQuantity == true) {
                updatedQuantity = Math.round(updatedQuantity);
                productQuantityView.setText(String.valueOf(updatedQuantity));
            }
        });

        productSaveView.setOnClickListener( view -> {
            Intent replyIntent = new Intent(UpdateProductActivity.this, ShoppingListActivity.class);

            if (TextUtils.isEmpty(productNameView.getText()) ) {
                errorToast("Please enter a product name...");
            }
            else if (productNameView.getText().toString().length() >= 40) {
                errorToast("Please enter a shorter product name...");
            }
            else if (TextUtils.isEmpty(productQuantityView.getText())) {
                errorToast("Please enter a quantity...");
            }
            else {
                boolean validQuantity = true;

                try {
                    float x = Float.parseFloat((productQuantityView.getText().toString()));
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
                            Product productToRemove = null;
                            for (Product p : products){
                                if (p.getId() == product.getId()){
                                    productToRemove = p;
                                }
                            }
                            products.remove(productToRemove);
                            if (products != null){
                                for (Product p : products){
                                    if (p.getName().toLowerCase().equals(productNameView.getText().toString().toLowerCase())){
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
                                        String productName = productNameView.getText().toString();
                                        replyIntent.putExtra(EXTRA_REPLY_NAME, productName);

                                        float productQuantity = Float.parseFloat((productQuantityView.getText().toString()));
                                        replyIntent.putExtra(EXTRA_REPLY_QUANTITY, productQuantity);

                                        String productUnit = productUnitSpinner.getSelectedItem().toString();
                                        replyIntent.putExtra(EXTRA_REPLY_UNIT, productUnit);

                                        replyIntent.putExtra(PRODUCT_OBJ_OLD, product);
                                        setResult(RESULT_OK, replyIntent);
                                        finish();

                                    }
                                    else {
                                        errorToast("Product already exists in this list");
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
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