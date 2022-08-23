package uk.ac.le.co2103.hw4;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemTextViewName;
    private final TextView itemTextViewQuantity;
    private final TextView itemTextViewUnit;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    private ProductViewHolder(View itemView) {
        super(itemView);
        itemTextViewName = itemView.findViewById(R.id.textViewName);
        itemTextViewQuantity = itemView.findViewById(R.id.textViewQuantity);
        itemTextViewUnit = itemView.findViewById(R.id.textViewUnit);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                recyclerViewClickInterface.onItemClick(getAdapterPosition());
            }
        });
    }

    public void bind(String text) {
        itemTextViewName.setText(text);
    }
    public void bindQuantity(String text) {itemTextViewQuantity.setText(text); }
    public void bindUnit(String text) {itemTextViewUnit.setText(text);}

    static ProductViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_product, parent, false);
        return new ProductViewHolder(view);
    }
}
