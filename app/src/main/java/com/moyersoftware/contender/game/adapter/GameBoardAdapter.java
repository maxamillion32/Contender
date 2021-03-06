package com.moyersoftware.contender.game.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moyersoftware.contender.R;
import com.moyersoftware.contender.game.GameBoardActivity;
import com.moyersoftware.contender.game.data.SelectedSquare;
import com.moyersoftware.contender.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameBoardAdapter extends RecyclerView.Adapter<GameBoardAdapter.ViewHolder> {

    // Constants
    private final static int CELLS_COUNT = 100;

    // Usual variables
    private GameBoardActivity mActivity;
    private HashMap<Integer, SelectedSquare> mSelectedPositions = new HashMap<>();
    private Boolean mLive = false;
    private boolean mPrintMode = false;
    private Integer mHomeScore = null;
    private Integer mAwayScore = null;
    private ArrayList<Integer> mRowNumbers = new ArrayList<>();
    private ArrayList<Integer> mColumnNumbers = new ArrayList<>();

    public GameBoardAdapter(GameBoardActivity activity) {
        mActivity = activity;
    }

    public void setLive(Boolean live) {
        mLive = live;
    }

    public void setScore(int home, int away) {
        mHomeScore = home;
        mAwayScore = away;
    }

    public void setRowNumbers(ArrayList<Integer> rowNumbers) {
        mRowNumbers = rowNumbers;
    }

    public void setColumnNumbers(ArrayList<Integer> columnNumbers) {
        mColumnNumbers = columnNumbers;
    }

    public void refresh(ArrayList<SelectedSquare> selectedSquares) {
        mSelectedPositions.clear();
        for (SelectedSquare selectedSquare : selectedSquares) {
            try {
                mSelectedPositions.put(selectedSquare.getPosition(), selectedSquare);
            } catch (Exception e) {
                // Can't add square
            }
        }
        notifyDataSetChanged();
    }

    public void refresh(ArrayList<SelectedSquare> selectedSquares, int position) {
        mSelectedPositions.clear();
        for (SelectedSquare selectedSquare : selectedSquares) {
            mSelectedPositions.put(selectedSquare.getPosition(), selectedSquare);
        }
        try {
            notifyItemChanged(position);
        } catch (Exception e) {
            notifyDataSetChanged();
        }
    }

    public void setPrintMode(boolean printMode) {
        mPrintMode = printMode;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_game_board, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mSelectedPositions.containsKey(position)) {
            SelectedSquare selectedSquare = mSelectedPositions.get(position);
            holder.nameTxt.setText(selectedSquare.getAuthorName());

            if (!mPrintMode) {
                holder.img.setVisibility(View.VISIBLE);
                Picasso.with(mActivity).load(selectedSquare.getAuthorPhoto()).placeholder
                        (R.drawable.avatar_placeholder).centerCrop().fit().into(holder.img);
                holder.nameTxt.setTextColor(Color.WHITE);
                holder.nameTxt.setEllipsize(TextUtils.TruncateAt.END);
                holder.nameTxt.setMaxLines(1);
            } else {
                holder.img.setVisibility(View.GONE);
                holder.nameTxt.setTextColor(Color.BLACK);
                holder.nameTxt.setEllipsize(null);
                holder.nameTxt.setShadowLayer(0, 0, 0, 0);
                holder.nameTxt.setMaxLines(2);
            }
        } else {
            holder.img.setVisibility(View.GONE);
            holder.nameTxt.setText("");
        }

        holder.itemView.setClickable(!mLive);

        if (mHomeScore != null && mAwayScore != null) {
            String lastHomeDigit;
            if (String.valueOf(mHomeScore).length() == 1) {
                lastHomeDigit = String.valueOf(mHomeScore);
            } else {
                lastHomeDigit = (String.valueOf(mHomeScore).substring(String
                        .valueOf(mHomeScore).length() - 1, String.valueOf(mHomeScore).length()));
            }
            String lastAwayDigit;
            if (String.valueOf(mAwayScore).length() == 1) {
                lastAwayDigit = String.valueOf(mAwayScore);
            } else {
                lastAwayDigit = (String.valueOf(mAwayScore).substring(String
                        .valueOf(mAwayScore).length() - 1, String.valueOf(mAwayScore).length()));
            }
            int column = position;
            int row = 0;
            while (column >= 10) {
                row++;
                column -= 10;
            }

            //Util.Log("lastHomeDigit = " + lastHomeDigit + ", " + lastAwayDigit);

            if (lastAwayDigit.equals(String.valueOf(mColumnNumbers.get(row)))
                    && lastHomeDigit.equals(String.valueOf(mRowNumbers.get(column)))) {
                Util.Log("row = " + row + ", row2 = " + mColumnNumbers.get(row));
                Util.Log("column = " + column + ", column2 = " + mRowNumbers.get(column));
                holder.winningView.setVisibility(View.VISIBLE);
            } else {
                holder.winningView.setVisibility(View.GONE);
            }
        } else {
            holder.winningView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return CELLS_COUNT;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {

        @Bind(R.id.cell_name_txt)
        TextView nameTxt;
        @Bind(R.id.cell_img)
        ImageView img;
        @Bind(R.id.cell_winning_view)
        View winningView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (!mLive) mActivity.selectSquare(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mLive) return true;

            if (mSelectedPositions.containsKey(getAdapterPosition())) {
                SelectedSquare selectedSquare = mSelectedPositions.get(getAdapterPosition());

                if (selectedSquare.getAuthorId().equals(mActivity.getCurrentId())) {
                    mActivity.openRemoveMenu(selectedSquare);
                }
            }
            return true;
        }
    }
}