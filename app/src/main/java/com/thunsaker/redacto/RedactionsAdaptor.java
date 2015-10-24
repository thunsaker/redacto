package com.thunsaker.redacto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thunsaker.redacto.models.Redaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RedactionsAdaptor extends RecyclerView.Adapter<RedactionsAdaptor.ViewHolder> {

    private ArrayList<Redaction> mRedactions;
    private Context mContext;

    static SimpleDateFormat dateFormatWithYear;

    public RedactionsAdaptor(Context context, ArrayList<Redaction> redactions) {
        mContext = context;
        mRedactions = redactions;
    }

    public void add(int position, Redaction item) {
        mRedactions.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(Redaction item) {
        int position = mRedactions.indexOf(item);
        mRedactions.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.redaction_compact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Redaction redaction = mRedactions.get(position);
        Picasso mPicasso = Picasso.with(mContext);
        mPicasso.load(redaction.ImageUri)
                .placeholder(R.drawable.redacto_placeholder)
                .into(holder.imageView);
        // Date with Short Month an Year
        dateFormatWithYear = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        holder.textViewDate.setText(dateFormatWithYear.format(redaction.DateCreated));
    }

    @Override
    public int getItemCount() {
        return mRedactions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textViewDate;

        public ViewHolder(View view) {
            super(view);

            imageView = (ImageView) view.findViewById(R.id.imageViewRedaction);
            textViewDate = (TextView) view.findViewById(R.id.textViewRedactionDate);
        }
    }
}
