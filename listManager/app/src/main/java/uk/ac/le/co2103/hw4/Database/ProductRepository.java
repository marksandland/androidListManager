package uk.ac.le.co2103.hw4.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ProductDao;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class ProductRepository {
    private ProductDao productDao;

    ProductRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        productDao = db.productDao();
    }

    void updateForeignKey(int productId, int x){
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.updateForeignKey(productId, x);
        });
    }

    LiveData<List<Product>> getLiveProducts(int id) {
        LiveData<List<Product>> products = productDao.getLiveProductsByShoppingListId(id);
        return products;
    }

    List<Product> getProducts(int id) {
        List<Product> products = productDao.getProductsByShoppingListId(id);
        return products;
    }

    void insert(Product product) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.insert(product);
        });
    }

    void delete(Product product) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.delete(product);
        });
    }

    void update(Product product) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            productDao.update(product);
        });
    }


}