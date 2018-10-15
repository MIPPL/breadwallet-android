package com.digiwagewallet.presenter.entities;

/**
 * Digiwage Wallet
 * <p>
 * Created by MIPPL on 10/08/2018.
 * <p>
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

public class DealUiHolder {
    public static final String TAG = DealUiHolder.class.getName();
    public String DealId;
    public long EscrowAmount;
    public String JobTitle;
    public String ReceiverUserName;
    public String EscrowAddress;
    public String Type;
    public String EscrowTxId;
    public float MediatedPercentage;
    public String RedeemScript;
    public String PaymentSignature1;
    public String SellerPubAddress;
    public String PendingType;

    private DealUiHolder() {
    }

    public DealUiHolder(String DealId, long EscrowAmount, String JobTitle, String ReceiverUserName, String EscrowAddress,
                        String Type, String EscrowTxId, float MediatedPercentage, String RedeemScript,
                        String PaymentSignature1, String SellerPubAddress, String PendingType ) {
        this.DealId = DealId;
        this.EscrowAmount = EscrowAmount;
        this.ReceiverUserName = ReceiverUserName;
        this.JobTitle = JobTitle;
        this.EscrowAddress = EscrowAddress;
        this.Type = Type;
        this.EscrowTxId = EscrowTxId;
        this.MediatedPercentage = MediatedPercentage;
        this.RedeemScript = RedeemScript;
        this.PaymentSignature1 = PaymentSignature1;
        this.SellerPubAddress = SellerPubAddress;
        this.PendingType = PendingType;
    }
}
