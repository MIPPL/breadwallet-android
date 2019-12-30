package com.biblepaywallet.presenter.entities;


/**
 * BreadWallet
 * <p>
 * Created by Mihail Gutan <mihail@breadwallet.com> on 1/13/16.
 * Copyright (c) 2016 breadwallet LLC
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

public class TitheUIHolder {
    public static final String TAG = TitheUIHolder.class.getName();
    private String churchID;
    private String address;
    private String name;
    private String organizationType;
    private long dateAdded;
    private long timeStamp;
    private String CPK;
    private String signature;
    private String vote;

    private TitheUIHolder() {
    }

    public TitheUIHolder(String churchID, String address, String name, String organizationType) {
        this.timeStamp = timeStamp;
        this.churchID = churchID;
        this.address = address;
        this.name = name;
        this.organizationType = organizationType;
    }

    public TitheUIHolder(String churchID, String address, String name, String organizationType, long dateAdded, long timeStamp, String CPK, String signature, String vote) {
        this.timeStamp = timeStamp;
        this.churchID = churchID;
        this.address = address;
        this.name = name;
        this.organizationType = organizationType;
        this.dateAdded = dateAdded;
        this.CPK = CPK;
        this.signature = signature;
        this.vote = vote;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getChurchID() {
        return churchID;
    }

    public void setChurchID(String churchID) {
        this.churchID = churchID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCPK() {
        return CPK;
    }

    public void setCPK(String CPK) {
        this.CPK = CPK;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }


}
