package uk.ac.le.co2103.hw4.Database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class ShoppingListViewModel extends AndroidViewModel {
    private ShoppingListRepository repo;

    public ShoppingListViewModel (Application application) {
        super(application);
        repo = new ShoppingListRepository(application);
    }

    public LiveData<List<ShoppingList>> getLiveShoppingLists() { return repo.getLiveShoppingLists();}
    public List<ShoppingList> getShoppingLists() { return repo.getShoppingLists();}
    public ShoppingList getShoppingListById(int id) {return repo.getShoppingListById(id);}

    public void insert(ShoppingList item) { repo.insert(item); }
    public void insertShoppingListWithProducts(ShoppingList item, List<Product> products) { repo.insertShoppingListWithProducts(item, products); }
    public void deleteShoppingList(ShoppingList shoppingList){repo.deleteShoppingList(shoppingList);}
}