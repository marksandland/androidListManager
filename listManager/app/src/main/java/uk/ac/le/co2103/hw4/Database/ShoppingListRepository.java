package uk.ac.le.co2103.hw4.Database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingListDao;

public class ShoppingListRepository {
    private ShoppingListDao itemDao;
    private LiveData<List<ShoppingList>> allLists;
    private LiveData<List<ShoppingList>> allListsWithProducts;

    ShoppingListRepository(Application application) {
        ShoppingListDB db = ShoppingListDB.getDatabase(application);
        itemDao = db.shoppingListDao();
    }

    LiveData<List<ShoppingList>> getLiveShoppingLists() {
        return itemDao.getLiveShoppingLists();
    }
    List<ShoppingList> getShoppingLists() {return itemDao.getShoppingLists();
    }

    ShoppingList getShoppingListById(int id) {return itemDao.getShoppingListById(id);}

    void insert(ShoppingList item) {
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            itemDao.insert(item);
        });
    }
    void insertShoppingListWithProducts(ShoppingList item, List<Product> products){
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            itemDao.insertShoppingListWithProducts(item, products);
        });
    }
    void deleteShoppingList(ShoppingList shoppingList){
        ShoppingListDB.databaseWriteExecutor.execute(() -> {
            itemDao.deleteShoppingList(shoppingList.getId());
        });
    }

}