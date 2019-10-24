package com.swyftwallet.wallet.wallets.ethereum;

import android.content.Context;

import com.swyftwallet.core.ethereum.BREthereumAmount;
import com.swyftwallet.core.ethereum.BREthereumEWM;
import com.swyftwallet.core.ethereum.BREthereumTransfer;
import com.swyftwallet.core.ethereum.BREthereumWallet;
import com.swyftwallet.presenter.entities.BRSettingsItem;
import com.swyftwallet.presenter.entities.TxUiHolder;
import com.swyftwallet.tools.manager.BRSharedPrefs;
import com.swyftwallet.tools.util.Utils;
import com.swyftwallet.wallet.abstracts.BaseWalletManager;
import com.swyftwallet.wallet.abstracts.BalanceUpdateListener;
import com.swyftwallet.wallet.abstracts.OnTxListModified;
import com.swyftwallet.wallet.abstracts.SyncListener;
import com.swyftwallet.wallet.wallets.WalletManagerHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseEthereumWalletManager implements BaseWalletManager {
    private static final String ETHEREUM_ADDRESS_PREFIX = "0x";
    static final int SCALE = 8;

    private WalletManagerHelper mWalletManagerHelper;
    protected String mAddress;

    public BaseEthereumWalletManager() {
        mWalletManagerHelper = new WalletManagerHelper();
    }

    protected WalletManagerHelper getWalletManagerHelper() {
        return mWalletManagerHelper;
    }

    //TODO Not used by ETH, ERC20
    @Override
    public int getForkId() {
        return -1;
    }

    @Override
    public synchronized String getAddress(Context context) { //todo context is not used, refactor
        if (mAddress == null) {
            throw new IllegalArgumentException("Address cannot be null.  Make sure it is set in the constructor.");
        }

        // TODO: Test of we can remove the caching in memory and always call core directly.
        return mAddress;
    }

    public abstract BREthereumWallet getWallet();

    @Override
    public boolean isAddressValid(String address) {
        return !Utils.isNullOrEmpty(address) && address.startsWith(ETHEREUM_ADDRESS_PREFIX) && BREthereumEWM.addressIsValid(address);
    }

    @Override
    public void addBalanceChangedListener(BalanceUpdateListener listener) {
        mWalletManagerHelper.addBalanceChangedListener(listener);
    }

    @Override
    public void removeBalanceChangedListener(BalanceUpdateListener listener) {
        mWalletManagerHelper.removeBalanceChangedListener(listener);
    }

    @Override
    public void onBalanceChanged(BigDecimal balance) {
        mWalletManagerHelper.onBalanceChanged(getCurrencyCode(), balance);
    }

    // TODO not used by ETH, ERC20
    @Override
    public void addSyncListener(SyncListener listener) {
    }

    // TODO not used by ETH, ERC20
    @Override
    public void removeSyncListener(SyncListener listener) {
    }

    @Override
    public void addTxListModifiedListener(OnTxListModified listener) {
        mWalletManagerHelper.addTxListModifiedListener(listener);
    }

    @Override
    public void removeTxListModifiedListener(OnTxListModified listener) {
        mWalletManagerHelper.removeTxListModifiedListener(listener);
    }

    //TODO Not used by ETH, ERC20
    @Override
    public void refreshAddress(Context app) {
    }

    protected abstract WalletEthManager getEthereumWallet();

    @Override
    public List<TxUiHolder> getTxUiHolders(Context app) {
        BREthereumTransfer[] txs = getWallet().getTransfers();
        int blockHeight = (int) getEthereumWallet().getBlockHeight();
        if (app != null && blockHeight != Integer.MAX_VALUE && blockHeight > 0) {
            BRSharedPrefs.putLastBlockHeight(app, getCurrencyCode(), blockHeight);
        }
        if (txs != null && txs.length > 0) {
            List<TxUiHolder> uiTxs = new ArrayList<>();
            for (int i = txs.length - 1; i >= 0; i--) { //revere order
                BREthereumTransfer tx = txs[i];
                if (tx.isSubmitted()) {
                    BREthereumAmount.Unit feeUnit = getCurrencyCode().equalsIgnoreCase(WalletEthManager.ETH_CURRENCY_CODE)
                            ? BREthereumAmount.Unit.ETHER_WEI : BREthereumAmount.Unit.ETHER_GWEI;
                    uiTxs.add(new TxUiHolder(tx, tx.getTargetAddress().equalsIgnoreCase(getEthereumWallet().getWallet().getAccount().getPrimaryAddress()),
                            tx.getBlockTimestamp(), (int) tx.getBlockNumber(),
                            Utils.isNullOrEmpty(tx.getOriginationTransactionHash()) ? null : tx.getOriginationTransactionHash().getBytes(),
                            tx.getOriginationTransactionHash(), new BigDecimal(tx.getFee(feeUnit)),
                            tx.getTargetAddress(), tx.getSourceAddress(), null, 0,
                            new BigDecimal(tx.getAmount(getUnit())), true,
                            tx.isErrored(), tx.getErrorDescription()));
                }
            }
            return uiTxs;
        } else {
            return null;
        }
    }

    @Override
    public boolean checkConfirmations(int conformations) {
        return mWalletManagerHelper.checkConfirmations(conformations);
    }

    public List<BRSettingsItem> getSettingsList(Context context) {
        return Collections.emptyList();
    }
}
