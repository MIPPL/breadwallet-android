package com.biblepaywallet.tools.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biblepaywallet.R;
import com.biblepaywallet.presenter.customviews.BRText;
import com.biblepaywallet.presenter.entities.TitheUIHolder;
import com.biblepaywallet.tools.manager.BRSharedPrefs;
import com.biblepaywallet.tools.threads.executor.BRExecutor;
import com.biblepaywallet.tools.util.BRDateUtil;
import com.biblepaywallet.tools.util.CurrencyUtils;
import com.biblepaywallet.tools.util.Utils;
import com.biblepaywallet.wallet.WalletsMaster;
import com.biblepaywallet.wallet.abstracts.BaseWalletManager;
import com.platform.tools.KVStoreManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * BreadWallet
 * <p>
 * Created by Mihail Gutan <mihail@breadwallet.com> on 7/27/15.
 * Copyright (c) 2016 breadwallet LLC
 * <p>
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

public class TitheListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String TAG = TitheListAdapter.class.getName();

    private final Context mContext;
    private final int txResId;
    private final int promptResId;
    private List<TitheUIHolder> backUpFeed;
    private List<TitheUIHolder> itemFeed;
    //    private Map<String, TxMetaData> mds;

    private final int titheType = 0;
    private final int promptType = 1;
    private boolean updatingReverseTxHash;
    private boolean updatingData;

//    private boolean updatingMetadata;

    public TitheListAdapter(Context mContext, List<TitheUIHolder> items) {
        this.txResId = R.layout.tithe_item;
        this.promptResId = R.layout.prompt_item;
        this.mContext = mContext;
        items = new ArrayList<>();
        init(items);
//        updateMetadata();
    }

    public void setItems(List<TitheUIHolder> items) {
        init(items);
    }

    private void init(List<TitheUIHolder> items) {
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
                List<TitheUIHolder> newItems = new ArrayList<>(itemFeed);
                TitheUIHolder item;
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


    public List<TitheUIHolder> getItems() {
        return itemFeed;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        return new TitheHolder(inflater.inflate(txResId, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case titheType:
                holder.setIsRecyclable(false);
                setTexts((TitheHolder) holder, position);
                break;
            case promptType:
                //setPrompt((PromptHolder) holder);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return titheType;
    }

    @Override
    public int getItemCount() {
        return itemFeed.size();
    }

    private void setTexts(final TitheHolder convertView, int position) {
        BaseWalletManager wallet = WalletsMaster.getInstance(mContext).getCurrentWallet(mContext);
        TitheUIHolder item = itemFeed.get(position);

        convertView.titheName.setText( item.getName() );
        convertView.titheType.setText( item.getOrganizationType() );
        convertView.titheAddress.setText( item.getAddress() );
    }

    public void filterBy(String query, boolean[] switches) {
        filter(query, switches);
    }

    public void resetFilter() {
        itemFeed = backUpFeed;
        notifyDataSetChanged();
    }

    private void filter(final String query, final boolean[] switches) {
        long start = System.currentTimeMillis();
        String lowerQuery = query.toLowerCase().trim();
        if (Utils.isNullOrEmpty(lowerQuery) && !switches[0] && !switches[1] && !switches[2] && !switches[3])
            return;
        int switchesON = 0;
        for (boolean i : switches) if (i) switchesON++;

        final List<TitheUIHolder> filteredList = new ArrayList<>();
        TitheUIHolder item;
        for (int i = 0; i < backUpFeed.size(); i++) {
            item = backUpFeed.get(i);
            boolean matchesName = item.getName() != null && item.getName().toLowerCase().contains(lowerQuery);
            boolean matchesType = item.getOrganizationType().toLowerCase().contains(lowerQuery);
            boolean matchesAddress = item.getAddress() != null && item.getAddress().toLowerCase().contains(lowerQuery);
            if (matchesName || matchesAddress || matchesType) {
                if (switchesON == 0) {
                    filteredList.add(item);
                }
            }
        }
        itemFeed = filteredList;
        notifyDataSetChanged();

        Log.e(TAG, "filter: " + query + " took: " + (System.currentTimeMillis() - start));
    }

    private class TitheHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mainLayout;
        public ConstraintLayout constraintLayout;
        public TextView name;
        public TextView type;
        public TextView address;

        public BRText titheName;
        public BRText titheType;
        public BRText titheAddress;

        public TitheHolder(View view) {
            super(view);

            titheName = view.findViewById(R.id.tithe_name);
            titheType = view.findViewById(R.id.tithe_type);
            titheAddress = view.findViewById(R.id.tithe_address);
        }
    }

}