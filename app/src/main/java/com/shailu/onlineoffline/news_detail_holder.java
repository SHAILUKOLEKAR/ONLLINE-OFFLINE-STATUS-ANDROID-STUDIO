package com.shailu.onlineoffline;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class news_detail_holder extends RecyclerView.ViewHolder {

    public TextView title , description;
    public ImageView image;

    public news_detail_holder(@NonNull View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.view_text_title_in_recycler);
        description=(TextView)itemView.findViewById(R.id.view_text_discription_in_recycler);
        image=(ImageView) itemView.findViewById(R.id.view_image_in_recycler);
    }

}
