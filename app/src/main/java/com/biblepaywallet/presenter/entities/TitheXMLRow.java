package com.biblepaywallet.presenter.entities;

/**
 * TitheXMLEntities
 * <p>
 * Created by MIP <mmobile@biblepay.org> on 12/26/19.
 * Copyright (c) 2019 Biblepay
 * <p>

 */
public class TitheXMLRow {

    public TitheXMLRow(String id, String address, String name, String organizationType)   {
        this.id = id;
        this.Address = address;
        this.Name = name;
        this.OrganizationType = organizationType;
    }

    private String id;
    private String Address;
    private String Name;
    private String OrganizationType;
    private String Added;
    private String Timestamp;
    private String CPK;
    private String Signature;
    private String Vote;

    public String getId() {
        return id;
    }

    public String getAddress() {
        return Address;
    }

    public String getName() {
        return Name;
    }

    public String getOrganizationType() {
        return OrganizationType;
    }

    public String getAdded() {
        return Added;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getCPK() {
        return CPK;
    }

    public String getSignature() {
        return Signature;
    }

    public String getVote() {
        return Vote;
    }
}

