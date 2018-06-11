package com.shaukan.gabriel.tinderclone.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaukan.gabriel.tinderclone.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ChatViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }
}
