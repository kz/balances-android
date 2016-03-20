package in.iamkelv.balances.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.iamkelv.balances.R;
import in.iamkelv.balances.models.Purchase;

public class PurchasesAdapter extends RecyclerView.Adapter<PurchasesAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTextView;
        public TextView priceTextView;
        public TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            itemTextView = (TextView) itemView.findViewById(R.id.itemTextView);
            priceTextView = (TextView) itemView.findViewById(R.id.priceTextView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
        }
    }

    private List<Purchase> mPurchases;

    public PurchasesAdapter(List<Purchase> purchases) {
        mPurchases = purchases;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View purchaseView = inflater.inflate(R.layout.item_purchase, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(purchaseView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PurchasesAdapter.ViewHolder holder, int position) {
        Purchase purchase = mPurchases.get(position);

        TextView itemTextView = holder.itemTextView;
        TextView priceTextView = holder.priceTextView;
        TextView timeTextView = holder.timeTextView;
    }

    @Override
    public int getItemCount() {
        return mPurchases.size();
    }
}

