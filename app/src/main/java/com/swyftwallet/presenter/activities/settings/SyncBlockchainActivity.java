package com.swyftwallet.presenter.activities.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.swyftwallet.R;
import com.swyftwallet.presenter.activities.util.BRActivity;
import com.swyftwallet.presenter.customviews.BRDialogView;
import com.swyftwallet.tools.animation.UiUtils;
import com.swyftwallet.tools.animation.BRDialog;
import com.swyftwallet.tools.manager.BRSharedPrefs;
import com.swyftwallet.tools.threads.executor.BRExecutor;
import com.swyftwallet.tools.util.BRConstants;
import com.swyftwallet.wallet.WalletsMaster;
import com.swyftwallet.wallet.abstracts.BaseWalletManager;


public class SyncBlockchainActivity extends BRActivity {
    private static final String TAG = SyncBlockchainActivity.class.getName();
    private Button mRescanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_blockchain);

        ImageButton faq = findViewById(R.id.faq_button);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) {
                    return;
                }
                BaseWalletManager wm = WalletsMaster.getInstance().getCurrentWallet(SyncBlockchainActivity.this);
                UiUtils.showSupportFragment(SyncBlockchainActivity.this, BRConstants.FAQ_RESCAN, wm);
            }
        });

        mRescanButton = findViewById(R.id.button_scan);
        mRescanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UiUtils.isClickAllowed()) {
                    return;
                }
                BRDialog.showCustomDialog(SyncBlockchainActivity.this, getString(R.string.ReScan_alertTitle),
                        getString(R.string.ReScan_footer), getString(R.string.ReScan_alertAction), getString(R.string.Button_cancel),
                        new BRDialogView.BROnClickListener() {
                            @Override
                            public void onClick(BRDialogView brDialogView) {
                                brDialogView.dismissWithAnimation();
                                rescanClicked();
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

    private void rescanClicked() {
        BRExecutor.getInstance().forLightWeightBackgroundTasks().execute(new Runnable() {
            @Override
            public void run() {
                Activity thisApp = SyncBlockchainActivity.this;
                BRSharedPrefs.putStartHeight(thisApp, BRSharedPrefs.getCurrentWalletCurrencyCode(thisApp), 0);
                BRSharedPrefs.putAllowSpend(thisApp, BRSharedPrefs.getCurrentWalletCurrencyCode(thisApp), false);
                WalletsMaster.getInstance().getCurrentWallet(thisApp).rescan(thisApp);
                UiUtils.startBreadActivity(thisApp, false);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

}
