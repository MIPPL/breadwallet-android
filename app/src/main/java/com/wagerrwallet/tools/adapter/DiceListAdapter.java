package com.wagerrwallet.tools.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wagerrwallet.R;
import com.wagerrwallet.WagerrApp;
import com.wagerrwallet.presenter.customviews.BRText;
import com.wagerrwallet.presenter.entities.BetEventEntity;
import com.wagerrwallet.presenter.entities.BetQuickGamesEntity;
import com.wagerrwallet.presenter.entities.DiceUiHolder;
import com.wagerrwallet.tools.threads.executor.BRExecutor;
import com.wagerrwallet.tools.util.BRConstants;
import com.wagerrwallet.tools.util.BRDateUtil;
import com.wagerrwallet.wallet.WalletsMaster;
import com.wagerrwallet.wallet.abstracts.BaseWalletManager;

import java.util.ArrayList;
import java.util.List;

/**
 * BreadWallet
 * <p>
 * Created by MIP on 11/14/20.
 * Copyright (c) 2020 Wagerr LTD
 * <p>
 *
 * (c) Wagerr Betting platform 2020
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class DiceListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = DiceListAdapter.class.getName();

    private final Context mContext;
    private final int txResId;
    private final int promptResId;
    private List<DiceUiHolder> backUpFeed;
    private List<DiceUiHolder> itemFeed;
    //    private Map<String, TxMetaData> mds;
    public boolean[] filterSwitches = { false, false, false };
    public String filterQuery="";

    private final int txType = 0;
    private final int promptType = 1;
    private boolean updatingReverseTxHash;
    private boolean updatingData;

//    private boolean updatingMetadata;

    public DiceListAdapter(Context mContext, List<DiceUiHolder> items) {
        this.txResId = R.layout.dice_item;
        this.promptResId = R.layout.prompt_item;
        this.mContext = mContext;
        items = new ArrayList<>();
        init(items);
//        updateMetadata();
    }

    public void setItems(List<DiceUiHolder> items) {
        init(items);
    }

    private void init(List<DiceUiHolder> items) {
        if (items == null) items = new ArrayList<>();
        if (itemFeed == null) itemFeed = new ArrayList<>();
        if (backUpFeed == null) backUpFeed = new ArrayList<>();
        this.itemFeed = items;
        this.backUpFeed = items;
    }

    public void updateData() {
        if (updatingData) return;
        BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                long s = System.currentTimeMillis();
                List<DiceUiHolder> newItems = new ArrayList<>(itemFeed);
                DiceUiHolder item;
                for (int i = 0; i < newItems.size(); i++) {
                    item = newItems.get(i);
                }
                backUpFeed = newItems;
                String log = String.format("newItems: %d, took: %d", newItems.size(), (System.currentTimeMillis() - s));
                Log.e(TAG, "updateData: " + log);
                updatingData = false;
            }
        });

    }


    public List<DiceUiHolder> getItems() {
        return itemFeed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        return new DiceHolder(inflater.inflate(txResId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case txType:
                holder.setIsRecyclable(false);
                setTexts((DiceHolder) holder, position);
                break;
            case promptType:
                //setPrompt((PromptHolder) holder);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return txType;
    }

    @Override
    public int getItemCount() {
        return itemFeed.size();
    }

    private void setTexts(final DiceHolder convertView, int position) {
        BaseWalletManager wallet = WalletsMaster.getInstance(mContext).getCurrentWallet(mContext);
        DiceUiHolder item = itemFeed.get(position);
        Context ctx = WagerrApp.getBreadContext();

        String shortDate = BRDateUtil.getShortDate(item.getTimestamp());
        convertView.transactionTimestamp.setText(shortDate);
        convertView.diceType.setText(item.getDiceGameTypeText());
        convertView.betAmount.setText(String.format("%s = %d WGR", ctx.getResources().getString(R.string.Dice_Bet), item.getAmount() / BRConstants.ONE_BITCOIN));
        convertView.dice1.setText(String.format("%s 1 = %d", ctx.getResources().getString(R.string.Dice_Dice), item.getDice1() ));
        convertView.dice2.setText(String.format("%s 2 = %d", ctx.getResources().getString(R.string.Dice_Dice), item.getDice2() ));
        convertView.diceTotal.setText(item.getSelectedOutcomeTx() );
        convertView.diceResult.setText(String.format("%s = %d", ctx.getResources().getString(R.string.Dice_Result), item.getDice1() + item.getDice2() ));
        convertView.dicePayout.setText(String.format("%d WGR", item.getPayoutAmount() ));
    }

    public void resetFilter() {
        itemFeed = backUpFeed;
        notifyDataSetChanged();
    }

    public void filterBy(String query, boolean[] switches) {
        filter(query, switches);
    }

    private void filter(final String query, final boolean[] switches) {
        BaseWalletManager wallet = WalletsMaster.getInstance(mContext).getCurrentWallet(mContext);

        long start = System.currentTimeMillis();
        String lowerQuery = query.toLowerCase().trim();
        // undesired behavior: no filter = rebuild whole list instead return
        //if (Utils.isNullOrEmpty(lowerQuery) && !switches[0] && !switches[1] && !switches[2] && !switches[3])
        //    return;
        int switchesON = 0;
        for (boolean i : switches) if (i) switchesON++;

        final List<DiceUiHolder> filteredList = new ArrayList<>();
        DiceUiHolder item;
        for (int i = 0; i < backUpFeed.size(); i++) {
            item = backUpFeed.get(i);
            boolean matchesId = item.getTxHash() != null && item.getTxHash().toLowerCase().contains(lowerQuery);

            if (matchesId) {
                if (switchesON == 0) {
                    filteredList.add(item);
                } else {
                    boolean willAdd = false;
                    if (switches[0] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.EQUAL)) {
                        willAdd = true;
                    }
                    if (switches[1] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.NOT_EQUAL)) {
                        willAdd = true;
                    }
                    if (switches[2] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.TOTAL_OVER)) {
                        willAdd = true;
                    }
                    if (switches[3] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.TOTAL_UNDER)) {
                        willAdd = true;
                    }
                    if (switches[4] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.EVEN)) {
                        willAdd = true;
                    }
                    if (switches[5] && (item.getDiceGameType() != BetQuickGamesEntity.BetDiceGameType.ODDS)) {
                        willAdd = true;
                    }

                    if (willAdd) filteredList.add(item);
                }
            }
        }
        filterSwitches = switches;
        filterQuery = query;
        itemFeed = filteredList;
        notifyDataSetChanged();

        Log.e(TAG, "filter: " + query + " took: " + (System.currentTimeMillis() - start));
    }

    private class DiceHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mainLayout;
        public ConstraintLayout constraintLayout;

        public BRText diceType;
        public BRText betAmount;
        public BRText dice1;
        public BRText dice2;
        public BRText diceTotal;
        public BRText diceResult;
        public BRText transactionTimestamp;
        public BRText dicePayout;

        public DiceHolder(View view) {
            super(view);

            transactionTimestamp = view.findViewById(R.id.tx_timestamp);
            diceType = view.findViewById(R.id.tx_dice_type);
            betAmount = view.findViewById(R.id.tx_bet_amount);
            dice1 = view.findViewById(R.id.tx_dice1);
            dice2 = view.findViewById(R.id.tx_dice2);
            diceTotal = view.findViewById(R.id.tx_total);
            diceResult = view.findViewById(R.id.tx_result);
            dicePayout = view.findViewById(R.id.tx_payout_amount);
        }
    }

}