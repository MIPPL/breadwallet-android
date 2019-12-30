package com.biblepaywallet.tools.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.biblepaywallet.R;
import com.biblepaywallet.presenter.activities.TitheActivity;
import com.biblepaywallet.presenter.entities.CryptoRequest;
import com.biblepaywallet.presenter.entities.TitheUIHolder;
import com.biblepaywallet.presenter.entities.TitheXMLRow;
import com.biblepaywallet.presenter.entities.TitheXMLTable;
import com.biblepaywallet.tools.adapter.TitheListAdapter;
import com.biblepaywallet.tools.animation.BRAnimator;
import com.biblepaywallet.tools.listeners.RecyclerItemClickListener;
import com.biblepaywallet.tools.threads.executor.BRExecutor;
import com.biblepaywallet.tools.util.TitheXMLParser;
import com.biblepaywallet.wallet.WalletsMaster;
import com.biblepaywallet.wallet.abstracts.BaseWalletManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * BreadWalletP
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 7/19/17.
 * Copyright (c) 2017 breadwallet LLC
 * <p/>
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
public class TitheManager {

    private static final String TAG = TitheManager.class.getName();
    private static TitheManager instance;
    private RecyclerView txList;
    public TitheListAdapter adapter;

    public static TitheManager getInstance() {
        if (instance == null) instance = new TitheManager();
        return instance;
    }

    public void init(final TitheActivity app) {
        txList = app.findViewById(R.id.tx_list);
        txList.setLayoutManager(new CustomLinearLayoutManager(app));
        txList.addOnItemTouchListener(new RecyclerItemClickListener(app,
                txList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {

                TitheUIHolder item = adapter.getItems().get(position);
                CryptoRequest cr = new CryptoRequest();
                cr.address = item.getAddress();
                cr.message = "Tithe for "+item.getName();
                BRAnimator.showSendFragment(app, cr);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        if (adapter == null)
            adapter = new TitheListAdapter(app, null);
        txList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //setupSwipe(app);
    }

    private TitheManager() {
    }

    public void onResume(final Activity app) {
        crashIfNotMain();
    }

    @WorkerThread
    public synchronized void updateTitheList(final Context app) {
        BRExecutor.getInstance().forBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                String xmlString = BRApiManager.urlGET(app, "https://web.biblepay.org/BMS/PARTNERS");

                // parse church list
                final List<TitheUIHolder> items = new ArrayList<>();
                try {
                    xmlString = removeUTF8BOM(xmlString);
                    TitheXMLParser parser = new TitheXMLParser();
                    List<TitheXMLRow> table = parser.parse(xmlString);
                    for (TitheXMLRow row: table)    {
                        TitheUIHolder item = new TitheUIHolder(row.getId(), row.getAddress(), row.getName(), row.getOrganizationType() );
                        items.add(item);
                    }
                    updateItems(app, items);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private synchronized void updateItems(final Context app, final List<TitheUIHolder> items)   {
        if (adapter != null && !((TitheActivity)app).isSearchActive()) {
            ((Activity) app).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.setItems(items);
                    txList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    public static final String UTF8_BOM = "\uFEFF";
    private String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        s = s.replace("\"", "");
        return s;
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
