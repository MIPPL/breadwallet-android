package com.swyftwallet.presenter.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.swyftwallet.R;
import com.swyftwallet.presenter.activities.settings.BaseSettingsActivity;
import com.swyftwallet.presenter.customviews.BaseTextView;
import com.swyftwallet.presenter.entities.BRSettingsItem;
import com.swyftwallet.tools.adapter.SettingsAdapter;
import com.swyftwallet.wallet.WalletsMaster;
import com.swyftwallet.wallet.abstracts.BaseWalletManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by byfieldj on 2/5/18.
 */

public class CurrencySettingsActivity extends BaseSettingsActivity {

    private BaseTextView mTitle;
    private ListView mListView;
    public List<BRSettingsItem> mItems;

    @Override
    public int getLayoutId() {
        return R.layout.activity_currency_settings;
    }

    @Override
    public int getBackButtonId() {
        return R.id.back_button;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = findViewById(R.id.title);
        mListView = findViewById(R.id.settings_list);

        final BaseWalletManager wm = WalletsMaster.getInstance().getCurrentWallet(this);

        mTitle.setText(String.format("%s %s", wm.getName(), CurrencySettingsActivity.this.getString(R.string.Settings_title)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mItems == null) {
            mItems = new ArrayList<>();
        }
        mItems.clear();
        BaseWalletManager walletManager = WalletsMaster.getInstance().getCurrentWallet(this);
        mItems.addAll(walletManager.getSettingsConfiguration().getSettingsList());
        View view = new View(this);
        mListView.addFooterView(view, null, true);
        mListView.addHeaderView(view, null, true);
        mListView.setAdapter(new SettingsAdapter(this, R.layout.settings_list_item, mItems));
    }

}
