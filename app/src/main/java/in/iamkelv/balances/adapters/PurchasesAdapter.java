package in.iamkelv.balances.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.iamkelv.balances.R;
import in.iamkelv.balances.models.DbPurchase;

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

    private List<DbPurchase> mDbPurchases;
    private Context mContext;

    public PurchasesAdapter(Context context, List<DbPurchase> dbPurchases) {
        mContext = context;
        mDbPurchases = dbPurchases;
    }

    public void clearData() {
        int size = this.mDbPurchases.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mDbPurchases.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    public void add(DbPurchase item, int position) {
        position = position == -1 ? getItemCount() : position;
        mDbPurchases.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        if (position < getItemCount()) {
            mDbPurchases.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View purchaseView = inflater.inflate(R.layout.item_purchase, parent, false);

        // Return a new holder instance
        return new ViewHolder(purchaseView);
    }

    @Override
    public void onBindViewHolder(PurchasesAdapter.ViewHolder holder, int position) {
        DbPurchase dbPurchase = mDbPurchases.get(position);

        TextView itemTextView = holder.itemTextView;
        TextView priceTextView = holder.priceTextView;
        TextView timeTextView = holder.timeTextView;

        itemTextView.setText(dbPurchase.getItem());
        Double price = ((double) dbPurchase.getPrice()) / 100;
        priceTextView.setText(String.format(mContext.getString(R.string.main_list_price),
                String.format("%.2f", price)));
        timeTextView.setText(dbPurchase.getTime());
    }

    @Override
    public int getItemCount() {
        return mDbPurchases.size();
    }
}

