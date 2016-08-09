package com.appspace.evybook.view.holder;

import android.view.View;
import android.widget.Button;

import com.appspace.evybook.R;

/**
 * Created by siwaweswongcharoen on 8/9/2016 AD.
 */
public class BookReadHolder extends BookHolder{

    public Button btnRead;
    public Button btnDelete;

    public BookReadHolder(View itemView) {
        super(itemView);

        btnRead = (Button) itemView.findViewById(R.id.btnRead);
        btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
    }
}
