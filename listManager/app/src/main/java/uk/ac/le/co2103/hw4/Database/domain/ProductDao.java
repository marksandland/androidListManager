package uk.ac.le.co2103.hw4.Database.domain;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("UPDATE products SET shoppingListId = :x WHERE id = :productId")
    void updateForeignKey(int productId, int x);

    @Transaction
    @Query("SELECT * FROM products WHERE shoppingListId = :id")
    LiveData<List<Product>> getLiveProductsByShoppingListId(int id);

    @Transaction
    @Query("SELECT * FROM products WHERE shoppingListId = :id")
    List<Product> getProductsByShoppingListId(int id);

}
