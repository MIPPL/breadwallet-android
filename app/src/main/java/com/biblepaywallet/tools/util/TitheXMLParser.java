package com.biblepaywallet.tools.util;


import android.renderscript.ScriptGroup;

import com.biblepaywallet.presenter.entities.TitheXMLRow;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static android.content.Context.FINGERPRINT_SERVICE;


/**
 * Biblepay
 * <p/>
 * Created by MIP
 * Copyright (c) 2019 Biblepay
 * <p/>
 */

public class TitheXMLParser {
    // We don't use namespaces
    private static final String ns = null;

    public List parse(String xml) throws XmlPullParserException, IOException {
        InputStream in = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser parser = factory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readTable(parser);
        } finally {
            in.close();
        }
    }

    private List readTable(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "TABLE");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("ROW")) {
                entries.add(readRow(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private TitheXMLRow readRow(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "ROW");
        String id = null;
        String Address = null;
        String Name = null;
        String OrganizationType = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.toLowerCase().equals("id")) {
                id = readTag(parser, "id");
            } else if (name.toLowerCase().equals("address")) {
                Address = readTag(parser, "Address");
            } else if (name.toLowerCase().equals("name")) {
                Name = readTag(parser, "Name");
            } else if (name.toLowerCase().equals("organizationtype")) {
                OrganizationType = readTag(parser, "OrganizationType");
            }
            else {
                skip(parser);
            }
        }
        return new TitheXMLRow(id, Address, Name, OrganizationType);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    private String readTag(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tagName);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tagName);
        return title;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}