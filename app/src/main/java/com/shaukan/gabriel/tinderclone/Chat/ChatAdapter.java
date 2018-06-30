package com.shaukan.gabriel.tinderclone.Chat;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shaukan.gabriel.tinderclone.R;

import java.util.Date;
import java.util.List;
import java.util.Calendar;

//Creating ChatAdapter class
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

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder((layoutView));


        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder holder, int position) {


        holder.mSentTime.setText(chatList.get(position).getCurrentTime());
        holder.mMessage.setText(chatList.get(position).getMessage());

        if (chatList.get(position).getCurrentUser()) {
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTextColor(Color.parseColor("#000000"));
            holder.mBubble.setCardBackgroundColor(Color.parseColor("#9EBC9E"));
            holder.mMasterContainer.setGravity(Gravity.END);
            holder.mContainer.setGravity(Gravity.END);
        } else {
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#000000"));
            holder.mMasterContainer.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
