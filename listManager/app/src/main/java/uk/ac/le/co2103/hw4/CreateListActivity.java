package uk.ac.le.co2103.hw4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import uk.ac.le.co2103.hw4.Database.ShoppingListViewModel;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class CreateListActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "uk.ac.le.co2103.REPLY_TEXT";
    public static final String EXTRA_REPLY_URI = "uk.ac.le.co2103.REPLY_IMAGE_URI";
    private EditText editTextItem;
    private ImageView imageView;
    private Uri imageUri;
    private Uri fileUri;
    public static final int PICK_IMAGE = 1;
    ShoppingListViewModel shoppingListViewModel;
    private static final String TAG_EXCEPTION = "Exception";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        shoppingListViewModel = new ViewModelProvider(this).get(ShoppingListViewModel.class);

        editTextItem = findViewById(R.id.edit_new_item);
        imageView = findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.list_default);
        imageUri = null;
        fileUri = null;


        Button chooseImageButton = findViewById(R.id.chooseImage);
        chooseImageButton.setOnClickListener( view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTextItem.getText())) {
                errorToast("Please enter a name for the list...");
            } else {
                Bitmap image = null;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    String x = "";
                    x = saveToInternalStorage(bitmap);
                    fileUri = Uri.fromFile(new File(x));
                }
                catch (Exception e){}

                String itemName = editTextItem.getText().toString();
                ShoppingList item = new ShoppingList(itemName);
                if (fileUri == null) {
                    item.setImage(null);
                }
                else {
                    item.setImage(fileUri.toString());
                }

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<ShoppingList> shoppingListList = shoppingListViewModel.getShoppingLists();
                        boolean listExists = false;
                        if (shoppingListList != null) {
                            for (ShoppingList shoppingList : shoppingListList){
                                if (shoppingList.getName().toLowerCase().equals(item.getName().toLowerCase())){
                                    listExists = true;
                                    break;
                                }
                            }
                        }
                        boolean finalListExists = listExists;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (finalListExists == false) {
                                    shoppingListViewModel.insert(item);
                                    setResult(RESULT_OK, replyIntent);
                                    finish();
                                }
                                else {
                                    errorToast("Please enter a unique name");
                                }
                            }
                        });
                    }
                });
                if (!thread1.isAlive()) {
                    thread1.start();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null){
                imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "no valid image",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        boolean saved = false;
        while (saved == false){
            try{
                String randomName = "";
                String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
                Random random = new Random();
                for (int i=0; i < 10; i++){
                    int randomNumber = random.nextInt(26);
                    randomName += alphabet[randomNumber];
                }
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("images", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath=new File(directory,randomName+".jpg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                saved = true;
                return directory.getAbsolutePath()+"/"+randomName+".jpg";
            }
            catch (Exception e){
                saved = false;
            }
        }
        return "";
    }

    private void errorToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}