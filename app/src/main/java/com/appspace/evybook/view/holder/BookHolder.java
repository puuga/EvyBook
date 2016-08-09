package com.appspace.evybook.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appspace.evybook.R;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class BookHolder extends RecyclerView.ViewHolder  {

    public ImageView ivBookCover;
    public TextView tvBookTitle;
    public TextView tvPublisher;

    public BookHolder(View itemView) {
        super(itemView);

        ivBookCover = (ImageView) itemView.findViewById(R.id.ivBookCover);
        tvBookTitle = (TextView) itemView.findViewById(R.id.tvBookTitle);
        tvPublisher = (TextView) itemView.findViewById(R.id.tvPublisher);
    }
}
