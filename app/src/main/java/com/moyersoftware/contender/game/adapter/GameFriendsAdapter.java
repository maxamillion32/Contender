package com.moyersoftware.contender.game.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moyersoftware.contender.R;
import com.moyersoftware.contender.game.GameBoardActivity;
import com.moyersoftware.contender.menu.data.Friend;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameFriendsAdapter extends RecyclerView.Adapter<GameFriendsAdapter.ViewHolder> {

    // Usual variables
    private GameBoardActivity mActivity;
    private ArrayList<Friend> mFriends = new ArrayList<>();
    private ArrayList<String> mInvitedFriendIds = new ArrayList<>();

    public GameFriendsAdapter(GameBoardActivity activity, ArrayList<Friend> friends,
                              ArrayList<String> invitedFriendIds) {
        mActivity = activity;
        mFriends = friends;
        mInvitedFriendIds = invitedFriendIds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_invite_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Friend friend = mFriends.get(position);
        holder.nameTxt.setText(friend.getName());
        holder.sendBtn.setEnabled(!mInvitedFriendIds.contains(friend.getId()));
        holder.sendBtn.setText(!mInvitedFriendIds.contains(friend.getId()) ? "Invite" : "Invited");
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.friend_invite_name_txt)
        TextView nameTxt;
        @Bind(R.id.friend_invite_send_btn)
        Button sendBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            sendBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mActivity.inviteFriend(mFriends.get(getAdapterPosition()));
        }
    }
}