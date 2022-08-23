package uk.ac.le.co2103.hw4.Database.domain;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ShoppingListDao {

    @Insert()
    void insert(ShoppingList shoppingList);

    @Transaction()
    @Query("SELECT * FROM shoppingLists WHERE listId =:id")
    ShoppingList getShoppingListById(int id);

    @Transaction
    @Insert()
    void insertShoppingListWithProducts(ShoppingList shoppingList, List<Product> products);

    @Transaction
    @Query("SELECT * FROM shoppingLists ORDER BY name ASC")
    LiveData<List<ShoppingList>> getLiveShoppingLists();

    @Transaction
    @Query("SELECT * FROM shoppingLists ORDER BY name ASC")
    List<ShoppingList> getShoppingLists();


    @Query("DELETE FROM shoppingLists")
    void deleteAll();

    @Query("DELETE FROM shoppingLists WHERE listId = :id")
    void deleteShoppingList(int id);


}
