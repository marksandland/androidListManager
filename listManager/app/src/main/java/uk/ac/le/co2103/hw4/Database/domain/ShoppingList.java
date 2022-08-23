package uk.ac.le.co2103.hw4.Database.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "shoppingLists", indices = {@Index(value = "name", unique = true)})
public class ShoppingList implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int listId;

    @NonNull
    public String name;
    private String image;

    @Ignore
    public ShoppingList(String name) {
        this.name = name;
        this.image = null;
    }

    public ShoppingList(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return listId;
    }

    public void setId(int listId) {
        this.listId = listId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShoppingList() {
        return name;
    }

}