package com.digiwagewallet.tools.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.support.annotation.WorkerThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.digiwagewallet.DigiWageApp;
import com.digiwagewallet.R;
import com.digiwagewallet.presenter.activities.PlatformActivity;
import com.digiwagewallet.presenter.activities.util.ActivityUTILS;
import com.digiwagewallet.presenter.entities.DealUiHolder;
import com.digiwagewallet.presenter.entities.TxUiHolder;
import com.digiwagewallet.tools.adapter.DealListAdapter;
import com.digiwagewallet.tools.animation.BRAnimator;
import com.digiwagewallet.tools.listeners.RecyclerItemClickListener;
import com.digiwagewallet.tools.util.Utils;
import com.digiwagewallet.wallet.WalletsMaster;
import com.digiwagewallet.wallet.abstracts.BaseWalletManager;
import com.platform.APIClient;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


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
public class PlatformManager {

    private static final String URL_PLATFORM = "https://dev.digiwage.org/api/wallet";

    private static final String TAG = PlatformManager.class.getName();
    private static PlatformManager instance;
    private RecyclerView txList;
    public DealListAdapter adapter;

    public static PlatformManager getInstance() {
        if (instance == null) instance = new PlatformManager();
        return instance;
    }

    public void init(final PlatformActivity app) {
        txList = app.findViewById(R.id.tx_list);
        txList.setLayoutManager(new CustomLinearLayoutManager(app));
        txList.addOnItemTouchListener(new RecyclerItemClickListener(app,
                txList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {

                DealUiHolder item = adapter.getItems().get(position);
                //BRAnimator.showTransactionDetails(app, item, position);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        if (adapter == null)
            adapter = new DealListAdapter(app, null);
        txList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //setupSwipe(app);
    }

    private PlatformManager() {
    }

    public void onResume(final Activity app) {
        crashIfNotMain();
    }

    @WorkerThread
    public synchronized void updateDealList(final Context app) {
        long start = System.currentTimeMillis();
        /*BaseWalletManager wallet = WalletsMaster.getInstance(app).getCurrentWallet(app);
        if (wallet == null) {
            Log.e(TAG, "updateTxList: wallet is null");
            return;
        }*/
        final List<DealUiHolder> items = getDealUiHolders( app );

        long took = (System.currentTimeMillis() - start);
        if (took > 500)
            Log.e(TAG, "updateDealList: took: " + took);
        if (adapter != null) {
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

    public List<DealUiHolder> getDealUiHolders( Context app )
    {
        List<DealUiHolder> uiDeals = new ArrayList<>();
        uiDeals.add(new DealUiHolder( "DealId", 100000,"TEST Jobtitle","ReceiverUserName", "EscrowAddress",
            "Type", "EscrowTxId", 0.0f, "RedeemScript",
            "PaymentSignature1", "SellerPubAddress", "PendingType") );

/*
        for (int i = txs.length - 1; i >= 0; i--) { //revere order
            BRCoreTransaction tx = txs[i];
            uiTxs.add(new TxUiHolder(tx.getTimestamp(), (int) tx.getBlockHeight(), tx.getHash(),
                    tx.getReverseHash(), getWallet().getTransactionAmountSent(tx),
                    getWallet().getTransactionAmountReceived(tx), getWallet().getTransactionFee(tx),
                    tx.getOutputAddresses(), tx.getInputAddresses(),
                    getWallet().getBalanceAfterTransaction(tx), (int) tx.getSize(),
                    getWallet().getTransactionAmount(tx), getWallet().transactionIsValid(tx)));
        }
*/
        return uiDeals;
    }

    public String updateAddress( Context app, String username, String address, String pubkey )
    {
        String url = String.format("%s/updateAddress?username=%s&address=%s&publicKey=%s", URL_PLATFORM, username, address, pubkey);
        String ret = urlSend( app, url, "POST");
        return ret;
    }

    public static String urlSend(Context app, String myURL, String method) {
//        System.out.println("Requested URL_EA:" + myURL);
        if (ActivityUTILS.isMainThread()) {
            Log.e(TAG, "urlGET: network on main thread");
            throw new RuntimeException("network on main thread");
        }
        Map<String, String> headers = DigiWageApp.getBreadHeaders();

        Request.Builder builder = new Request.Builder()
                .url(myURL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("User-agent", Utils.getAgentString(app, "android/HttpURLConnection"));

        if (method == "GET")
        {
            builder.get();
        }
        else
        {
            //RequestBody requestBody;
            builder.post( null );
        }
        Iterator it = headers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            builder.header((String) pair.getKey(), (String) pair.getValue());
        }

        Request request = builder.build();
        String response = null;
        Response resp = APIClient.getInstance(app).sendRequest(request, false, 0);

        try {
            if (resp == null) {
                Log.e(TAG, "urlGET: " + myURL + ", resp is null");
                return null;
            }
            response = resp.body().string();
            String strDate = resp.header("date");
            if (strDate == null) {
                Log.e(TAG, "urlGET: strDate is null!");
                return response;
            }
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            Date date = formatter.parse(strDate);
            long timeStamp = date.getTime();
            BRSharedPrefs.putSecureTime(app, timeStamp);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        } finally {
            if (resp != null) resp.close();

        }
        return response;
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
