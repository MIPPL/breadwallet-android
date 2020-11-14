package com.wagerrwallet.presenter.entities;


/**
 * BreadWallet
 * <p>
 * Created by MIP (2020)
 * Copyright (c) 2016 breadwallet LLC
 * <p>
 *
 * (c) Wagerr Betting platform 2020
 *
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

public class DiceUiHolder extends BetQuickGamesEntity {
    public static final String TAG = DiceUiHolder.class.getName();

    protected int dice1;
    protected int dice2;
    protected int diceResult;
    protected long payoutAmount;
    protected String payoutTxHash;

    public DiceUiHolder(String txHash, BetTxType type, long version,
                        BetQuickGameType betQuickGameType, BetDiceGameType betDiceGameType,
                        long amount, long selectedOutcome, long blockheight, long timestamp,
                        int dice1, int dice2, int diceResult, long payoutAmount, String payoutTxHash) {
        super( txHash, type, version, betQuickGameType, betDiceGameType, amount, selectedOutcome, blockheight, timestamp);

        this.dice1 = dice1;
        this.dice2 = dice2;
        this.diceResult = diceResult;
        this.payoutAmount = payoutAmount;
        this.payoutTxHash = payoutTxHash;
    }

    public DiceUiHolder()    {}

    public int getDice1() {
        return dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public int getDiceResult() {
        return diceResult;
    }
    public long getPayoutAmount() {
        return payoutAmount;
    }

    public String getPayoutTxHash() {
        return payoutTxHash;
    }
}
