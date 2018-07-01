package com.shaukan.gabriel.tinderclone.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaukan.gabriel.tinderclone.Chat.ChatActivity;
import com.shaukan.gabriel.tinderclone.R;

//create MatchesViewHolder class
public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mMatchId, mMatchName, mMostRecentChat;
    public ImageView mMatchImage;

    public MatchesViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMostRecentChat = (TextView) itemView.findViewById(R.id.mostRecentChat);
        mMatchId = (TextView) itemView.findViewById(R.id.Matchid);
        mMatchName = (TextView) itemView.findViewById(R.id.MatchName);
        mMatchImage = (ImageView) itemView.findViewById(R.id.MatchImage);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}
