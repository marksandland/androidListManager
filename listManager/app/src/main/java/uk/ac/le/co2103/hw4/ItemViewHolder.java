package uk.ac.le.co2103.hw4;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextView;
    public final ImageView itemImageView;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    private ItemViewHolder(View itemView) {
        super(itemView);
        itemTextView = itemView.findViewById(R.id.textViewName);
        itemImageView = itemView.findViewById(R.id.imageView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerViewClickInterface.onItemClick(getAdapterPosition());
            }

        });
    }

    public void bind(String text) {
        itemTextView.setText(text);
    }

    static ItemViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new ItemViewHolder(view);
    }
}
