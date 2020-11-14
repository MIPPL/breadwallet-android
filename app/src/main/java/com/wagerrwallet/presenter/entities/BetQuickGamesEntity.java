package com.wagerrwallet.presenter.entities;


/**
 * BreadWallet
 * <p>
 * Created by MIP on 1/13/20.
 * Copyright (c) 2019 Wagerr LTD
 * <p>
 *
 * (c) Wagerr Betting platform 2019
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

    public BetDiceGameType getDiceGameType() {
        return diceGameType;
    }

    public long getSelectedOutcome() {
        return selectedOutcome;
    }
}
