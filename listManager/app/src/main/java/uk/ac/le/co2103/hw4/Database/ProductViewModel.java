package uk.ac.le.co2103.hw4.Database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import uk.ac.le.co2103.hw4.Database.ProductRepository;
import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ProductDao;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository repo;

    public ProductViewModel(Application application) {
        super(application);
        repo = new ProductRepository(application);
    }

    public void update(Product product){repo.update(product);}

    public void insert(Product product){repo.insert(product);}

    public void updateForeignKey(int productId, int x) { repo.updateForeignKey(productId, x); }

    public void delete(Product product) {repo.delete(product);}

    public LiveData<List<Product>> getLiveProductsByShoppingListId(int id) {
        LiveData<List<Product>> items = repo.getLiveProducts(id);
        return items;
    }

    public List<Product> getProductsByShoppingListId(int id) {
        List<Product> items = repo.getProducts(id);
        return items;
    }

}