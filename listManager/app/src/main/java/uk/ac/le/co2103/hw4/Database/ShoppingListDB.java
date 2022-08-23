package uk.ac.le.co2103.hw4.Database;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.constraintlayout.solver.PriorityGoalRow;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ProductDao;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingListDao;

@Database(entities = {ShoppingList.class, Product.class}, version = 1, exportSchema = false)
public abstract class ShoppingListDB extends RoomDatabase {

    public abstract ShoppingListDao shoppingListDao();
    public abstract ProductDao productDao();

    private static volatile ShoppingListDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ShoppingListDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ShoppingListDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingListDB.class, "shoppingcart_db")
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public ShoppingListDao getShoppingListDao() {
        return this.shoppingListDao();
    }

//    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onCreate(@NonNull SupportSQLiteDatabase db) {
//            super.onCreate(db);
//
//            databaseWriteExecutor.execute(() -> {
//                ShoppingListDao shoppingDao = INSTANCE.shoppingListDao();
//                ProductDao productDao = INSTANCE.productDao();
//                shoppingDao.deleteAll();
//
//                ShoppingList item = new ShoppingList("firstList");
//                Product product1 = new Product(item.getId(),"product 1", 9, "Kg");
//                Product product2 = new Product(item.getId(),"product 2", 9, "Kg");
//                List<Product> productsForItem = new ArrayList<>();
//                productsForItem.add(product1);
//                productsForItem.add(product2);
//
//
//                shoppingDao.insertShoppingListWithProducts(item, productsForItem);
//                int x = 1;
////                productDao.updateForeignKey(2, x);
////                productDao.updateForeignKey(1, x);
//
//                ShoppingList item2 = new ShoppingList("list2");
//                shoppingDao.insert(item2);
//
//
//            });
//        }
//    };
}