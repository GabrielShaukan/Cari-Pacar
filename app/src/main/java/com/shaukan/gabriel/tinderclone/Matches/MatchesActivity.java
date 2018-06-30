package com.shaukan.gabriel.tinderclone.Matches;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shaukan.gabriel.tinderclone.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MatchesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        getUserMatchId();

    }

    //Gets the userId of the match
    private void getUserMatchId() {

        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot match : dataSnapshot.getChildren()) {
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Fetches user name, profile picture, and userId
    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        String userId = "";
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String userId = dataSnapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";



                    if (dataSnapshot.child("Name").getValue() != null) {
                        name = dataSnapshot.child("Name").getValue().toString();
                    }

                    if (dataSnapshot.child("connections").child("matches").child(currentUserID).child("ChatId").getValue() != null) {
                        String chatId = dataSnapshot.child("connections").child("matches").child(currentUserID).child("ChatId").getValue().toString();
                        Query latestChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatId).orderByKey().limitToLast(1);
                    }

                    if (dataSnapshot.child("profileImageUrl").getValue() != null) {
                        profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                    }



                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<MatchesObject>();
    private List<MatchesObject> getDataSetMatches() {
        return resultsMatches;
    }
}
