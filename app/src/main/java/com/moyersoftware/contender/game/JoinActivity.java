package com.moyersoftware.contender.game;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moyersoftware.contender.R;
import com.moyersoftware.contender.game.adapter.JoinPagerAdapter;
import com.moyersoftware.contender.game.data.Game;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class JoinActivity extends AppCompatActivity {

    // Views
    @Bind(R.id.join_pager)
    ViewPager mPager;
    @Bind(R.id.join_tab_layout)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        ButterKnife.bind(this);

        initPager();
        initTabs();
        initDatabase();
    }

    private void initPager() {
        mPager.setAdapter(new JoinPagerAdapter(getSupportFragmentManager()));
    }

    private void initTabs() {
        mTabLayout.setupWithViewPager(mPager);
    }

    private void initDatabase() {
    }

    /**
     * Required for the calligraphy library.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onBackButtonClicked(View view) {
        finish();
    }

    public void joinGame(final String gameId) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        // Retrieve current list of players for the game user wants to join
        database.child("games").child(gameId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get game value
                        Game game = dataSnapshot.getValue(Game.class);

                        if (firebaseUser != null) {
                            final String id = firebaseUser.getUid();
                            ArrayList<String> players = game.getPlayers();
                            if (players == null) players = new ArrayList<>();

                            if (!players.contains(id)) players.add(id);

                            database.child("games").child(gameId).child("players")
                                    .setValue(players);

                            startActivity(new Intent(JoinActivity.this, GameBoardActivity.class)
                                    .putExtra(GameBoardActivity.EXTRA_GAME_ID, gameId));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }
}
