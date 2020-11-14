package com.wagerrwallet.tools.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wagerrwallet.R;
import com.wagerrwallet.presenter.activities.DiceActivity;
import com.wagerrwallet.presenter.entities.DiceUiHolder;
import com.wagerrwallet.tools.adapter.DiceListAdapter;
import com.wagerrwallet.tools.animation.BRAnimator;
import com.wagerrwallet.tools.listeners.RecyclerItemClickListener;
import com.wagerrwallet.wallet.WalletsMaster;
import com.wagerrwallet.wallet.abstracts.BaseWalletManager;

import java.util.List;


/**
 * BreadWalletP
 * <p/>
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
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class DiceManager {

    private static final String TAG = DiceManager.class.getName();
    private static DiceManager instance;
    private RecyclerView txList;
    public DiceListAdapter adapter;

    public static DiceManager getInstance() {
        if (instance == null) instance = new DiceManager();
        return instance;
    }

    public void init(final DiceActivity app) {
        txList = app.findViewById(R.id.tx_list);
        txList.setLayoutManager(new CustomLinearLayoutManager(app));
        txList.addOnItemTouchListener(new RecyclerItemClickListener(app,
                txList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                try {
                    DiceUiHolder item = adapter.getItems().get(position);
                    //BRAnimator.showSwapDetails(app, item, position);
                }
                catch (ArrayIndexOutOfBoundsException e)    {

                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        if (adapter == null)
            adapter = new DiceListAdapter(app, null);
        txList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //setupSwipe(app);
    }

    private DiceManager() {
    }

    public void onResume(final Activity app) {
        crashIfNotMain();
    }

    @WorkerThread
    public synchronized void updateDiceList(final Context app) {
        long start = System.currentTimeMillis();
        BaseWalletManager wallet = WalletsMaster.getInstance(app).getCurrentWallet(app);
        if (wallet == null) {
            Log.e(TAG, "updateDiceList: wallet is null");
            return;
        }

        final List<DiceUiHolder> items = wallet.getDiceUiHolders( app );

        long took = (System.currentTimeMillis() - start);
        if (took > 500)
            Log.e(TAG, "updateSwapList: took: " + took);
        if (adapter != null && !((DiceActivity)app).isSearchActive() ) {
            ((DiceActivity) app).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (adapter!=null && items!=null) {
                        final List<DiceUiHolder> currentItems = adapter.getItems();
                        currentItems.clear();
                        for (DiceUiHolder item : items) {
                            currentItems.add(item);
                        }
                        //adapter.setItems(betItems);
                        //txList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    private class CustomLinearLayoutManager extends LinearLayoutManager {

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        /**
         * Disable predictive animations. There is a bug in RecyclerView which causes views that
         * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
         * adapter size has decreased since the ViewHolder was recycled.
         */
        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }

    private void crashIfNotMain() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalAccessError("Can only call from main thread");
        }
    }

}
