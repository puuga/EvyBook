package com.appspace.evybook.view.holder;

import android.view.View;
import android.widget.Button;

import com.appspace.evybook.R;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class BookDownloadHolder extends BookHolder {

    public Button btnDownload;

    public BookDownloadHolder(View itemView) {
        super(itemView);

        btnDownload = (Button) itemView.findViewById(R.id.btnDownload);
    }
}
