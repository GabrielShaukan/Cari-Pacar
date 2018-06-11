package com.shaukan.gabriel.tinderclone.Chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.shaukan.gabriel.tinderclone.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> matchesList, Context context) {
        this.chatList = matchesList;
        this.context = context;
    };

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_matches, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder((layoutView));


        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
