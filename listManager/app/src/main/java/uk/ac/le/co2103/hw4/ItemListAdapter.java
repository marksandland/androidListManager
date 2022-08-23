package uk.ac.le.co2103.hw4;

import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import uk.ac.le.co2103.hw4.Database.domain.ShoppingList;


public class ItemListAdapter extends ListAdapter<ShoppingList, ItemViewHolder> {

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public ItemListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingList> diffCallback, RecyclerViewClickInterface recyclerViewClickInterface) {
        super(diffCallback);
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ItemViewHolder.create(parent);
    }


    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ShoppingList current = getItem(position);
        holder.bind(current.getShoppingList());

        try{
            Uri uri = Uri.parse(current.getImage());
            holder.itemImageView.setImageURI(uri);
        }
        catch (Exception e) {
            holder.itemImageView.setImageResource(R.drawable.list_default);
        }

        holder.itemView.setOnClickListener(view -> {
            recyclerViewClickInterface.onItemClick(position);
        });
        holder.itemView.setOnLongClickListener(view -> {
            recyclerViewClickInterface.onItemLongClick(position);
            return true;
        });
    }

    static class ItemDiff extends DiffUtil.ItemCallback<ShoppingList> {

        @Override
        public boolean areItemsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingList oldItem, @NonNull ShoppingList newItem) {
            return oldItem.getShoppingList().equals(newItem.getShoppingList());
        }
    }



}