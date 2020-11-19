package com.wagerrwallet.presenter.entities;


import android.content.Context;

import com.wagerrwallet.R;
import com.wagerrwallet.WagerrApp;

/**
 * BreadWallet
 * <p>
 * Created by MIP on 11/14/20.
 * Copyright (c) 2020 Wagerr LTD
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

public class BetQuickGamesEntity extends BetEntity {
    public static final String TAG = BetQuickGamesEntity.class.getName();

    public enum BetQuickGameType {
        DICE(0x00),
        UNKNOWN(-1);

        private int type;
        BetQuickGameType(int type) {
            this.type = type;
        }

        public int getNumber()    {return type;}

        public static BetQuickGameType fromValue (int value) {
            // Just a linear search - easy, quick-enough.
            for (BetQuickGameType txType : BetQuickGameType.values())
                if (txType.type == value)
                    return txType;
            return UNKNOWN;
        }
    }

    public enum BetDiceGameType {
        EQUAL(0x00),
        NOT_EQUAL(0x01),
        TOTAL_OVER(0x02),
        TOTAL_UNDER(0x03),
        EVEN(0x04),
        ODDS(0x05),
        UNKNOWN(-1);

        private int type;
        BetDiceGameType(int type) {
            this.type = type;
        }

        public int getNumber()    {return type;}

        public static BetDiceGameType fromValue (int value) {
            // Just a linear search - easy, quick-enough.
            for (BetDiceGameType txType : BetDiceGameType.values())
                if (txType.type == value)
                    return txType;
            return UNKNOWN;
        }
    }

    // quick games bets properties
    protected BetQuickGameType gameType;
    protected BetDiceGameType diceGameType;
    protected long selectedOutcome;

    // constructor for DB
    public BetQuickGamesEntity(String txHash, BetTxType type, long version,
                               BetQuickGameType betQuickGameType, BetDiceGameType betDiceGameType,
                               long amount, long selectedOutcome,
                               long blockheight, long timestamp ) {
        this.blockheight = blockheight;
        this.timestamp = timestamp;
        this.txHash = txHash;
        this.gameType = betQuickGameType;
        this.diceGameType = betDiceGameType;
        this.version = version;
        this.type = type;

        this.selectedOutcome = selectedOutcome;
        this.amount = amount;
    }

    protected BetQuickGamesEntity() {
    }

    public BetQuickGameType getGameType() {
        return gameType;
    }

    public BetDiceGameType  getDiceGameType() {
        return diceGameType;
    }

    public String  getDiceGameTypeText() {
        String ret = "";
        Context ctx = WagerrApp.getBreadContext();
        switch (diceGameType)   {
            case EQUAL:
                ret = ctx.getResources().getString(R.string.Dice_Equal);
                break;
            case NOT_EQUAL:
                ret = ctx.getResources().getString(R.string.Dice_NotEqual);
                break;
            case TOTAL_OVER:
                ret = ctx.getResources().getString(R.string.Dice_TotalOver);
                break;
            case TOTAL_UNDER:
                ret = ctx.getResources().getString(R.string.Dice_TotalUnder);
                break;
            case EVEN:
                ret = ctx.getResources().getString(R.string.Dice_Even);
                break;
            case ODDS:
                ret = ctx.getResources().getString(R.string.Dice_Odds);
                break;
        }
        return ret;
    }

    public long getSelectedOutcome() {
        return selectedOutcome;
    }

    public String getSelectedOutcomeTx() {
        Context ctx = WagerrApp.getBreadContext();
        String ret="", outcome="";

        switch (diceGameType)   {
            case EQUAL:
            case NOT_EQUAL:
                outcome = String.format("%d", selectedOutcome );
                ret = String.format("%s = %s", ctx.getResources().getString(R.string.Dice_SelectedOutcome), outcome );
                break;
            case TOTAL_OVER:
            case TOTAL_UNDER:
                outcome = String.format("%d.5", selectedOutcome );
                ret = String.format("%s = %s", ctx.getResources().getString(R.string.Dice_SelectedOutcome), outcome );
                break;
            case EVEN:
            case ODDS:
                ret = "";
                break;
        }
        return ret;
    }
}
