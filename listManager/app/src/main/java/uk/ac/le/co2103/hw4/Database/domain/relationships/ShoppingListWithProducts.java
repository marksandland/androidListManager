package uk.ac.le.co2103.hw4.Database.domain.relationships;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class ShoppingListWithProducts implements Serializable {
    @Embedded
    public ShoppingList shoppingList;
    @Relation(
            parentColumn = "listId",
            entityColumn = "shoppingListId",
            entity = Product.class
    )
    public List<Product> products;
}