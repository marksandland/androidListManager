package uk.ac.le.co2103.hw4.Database.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(indices = {@Index("shoppingListId")}, tableName = "products", foreignKeys = @ForeignKey(entity = ShoppingList.class, parentColumns = "listId", childColumns = "shoppingListId", onDelete = CASCADE))
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public int shoppingListId;

    @NonNull
    public String name;

    @NonNull
    private float quantity;

    @NonNull
    private String unit;

    @Ignore
    public Product(){
    }

    public Product(int shoppingListId, String name, float quantity, String unit) {
        this.shoppingListId = shoppingListId;
        this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}