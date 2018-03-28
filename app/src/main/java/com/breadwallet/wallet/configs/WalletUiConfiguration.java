package com.breadwallet.wallet.configs;

/**
 * BreadWallet
 * <p/>
 * Created by Mihail Gutan on <mihail@breadwallet.com> 1/25/18.
 * Copyright (c) 2018 breadwallet LLC
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
public class WalletUiConfiguration {
    public String colorHex;

    public boolean sendVisible;
    public boolean receiveVisible;
    public boolean buyVisible;

    public boolean showEconomyFee;
    public boolean showLiveFee;
    public boolean showRequestAnAmount;

    //todo refactor to receive objects tied to specific screens
    public WalletUiConfiguration(String colorHex, boolean sendVisible, boolean receiveVisible, boolean buyVisible, boolean showEconomyFee, boolean showLiveFee, boolean showRequestAnAmount) {
        this.colorHex = colorHex;
        this.sendVisible = sendVisible;
        this.receiveVisible = receiveVisible;
        this.buyVisible = buyVisible;
        this.showEconomyFee = showEconomyFee;
        this.showLiveFee = showLiveFee;
        this.showRequestAnAmount = showRequestAnAmount;
    }

}