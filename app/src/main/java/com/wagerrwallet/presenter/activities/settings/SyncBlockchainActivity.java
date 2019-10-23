package com.wagerrwallet.presenter.activities.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wagerrwallet.R;
import com.wagerrwallet.presenter.activities.util.BRActivity;
import com.wagerrwallet.presenter.customviews.BRDialogView;
import com.wagerrwallet.tools.animation.BRAnimator;
import com.wagerrwallet.tools.animation.BRDialog;
import com.wagerrwallet.tools.manager.BRSharedPrefs;
import com.wagerrwallet.tools.sqlite.BetEventTxDataStore;
import com.wagerrwallet.tools.sqlite.BetMappingTxDataStore;
import com.wagerrwallet.tools.sqlite.BetResultTxDataStore;
import com.wagerrwallet.tools.sqlite.BetTxDataStore;
import com.wagerrwallet.tools.threads.executor.BRExecutor;
import com.wagerrwallet.tools.util.BRConstants;
import com.wagerrwallet.wallet.WalletsMaster;


public class SyncBlockchainActivity extends BRActivity {
    private static final String TAG = SyncBlockchainActivity.class.getName();
    private Button scanButton;
    public static boolean appVisible = false;
    private static SyncBlockchainActivity app;

    public static SyncBlockchainActivity getApp() {
        return app;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_blockchain);

        ImageButton faq = (ImageButton) findViewById(R.id.faq_button);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRAnimator.showSupportFragment(app, BRConstants.reScan);
            }
        });

        scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BRAnimator.isClickAllowed()) return;
                BRDialog.showCustomDialog(SyncBlockchainActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                final String iso = BRSharedPrefs.getCurrentWalletIso(SyncBlockchainActivity.this);
                                BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        BRSharedPrefs.putStartHeight(SyncBlockchainActivity.this, iso, 0);
                                        BRSharedPrefs.putAllowSpend(SyncBlockchainActivity.this, iso, false);
/*
                                        if (iso.toLowerCase()=="wgr") {
                                            BetResultTxDataStore.getInstance(SyncBlockchainActivity.this).deleteAllTransactions(app, iso);
                                            BetEventTxDataStore.getInstance(SyncBlockchainActivity.this).deleteAllTransactions(app, iso);
                                            BetTxDataStore.getInstance(SyncBlockchainActivity.this).deleteAllTransactions(app, iso);
                                            BetMappingTxDataStore.getInstance(SyncBlockchainActivity.this).deleteAllTransactions(app, iso);
                                        }
*/
                                        WalletsMaster.getInstance(SyncBlockchainActivity.this).getCurrentWallet(SyncBlockchainActivity.this).getPeerManager().rescan();
                                        BRAnimator.startBreadActivity(SyncBlockchainActivity.this, false);

                                    }
                                });
                            }
                        }, new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                            }
                        }, null, 0);



            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        appVisible = true;
        app = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appVisible = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}