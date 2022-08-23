package uk.ac.le.co2103.hw4;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.text.DecimalFormat;

import uk.ac.le.co2103.hw4.Database.domain.Product;
import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;

public class ProductListAdapter extends ListAdapter<Product, ProductViewHolder> {

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public ProductListAdapter(@NonNull DiffUtil.ItemCallback<Product> diffCallback, RecyclerViewClickInterface recyclerViewClickInterface) {
        super(diffCallback);
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ProductViewHolder.create(parent);
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product current = getItem(position);
        holder.bind(current.getName());


        String quantityString = String.valueOf(current.getQuantity()).replaceAll("[0]*$","");
        if (quantityString.substring(quantityString.length()-1).equals(".")){
            quantityString = quantityString.substring(0,quantityString.length() - 1);
        }
        holder.bindQuantity(quantityString);
        String unitString = current.getUnit();
        holder.bindUnit(unitString);

        holder.itemView.setOnClickListener(view -> {
            recyclerViewClickInterface.onItemClick(position);
        });
    }

    static class ItemDiff extends DiffUtil.ItemCallback<Product> {

        @Override
        public boolean areItemsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Product oldItem, @NonNull Product newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }



}