package com.biblepaywallet.presenter.entities;


/**
 * TitheXMLEntities
 * <p>
 * Created by MIP <mmobile@biblepay.org> on 12/26/19.
 * Copyright (c) 2019 Biblepay
 * <p>

 */

public class TitheXMLTable {

    TitheXMLTable() { }

    private String description;
    private TitheXMLRow[] rows;
    public TitheXMLRow[] getRows()  {   return rows; }
}

