package com.iptv.plugin.firetv;

import android.util.Log;
import android.util.Xml;

import com.utils.HttpHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class WasuPlugin extends AbstractPlugin {
    private static final String TAG = "WasuPlugin";

    private static final String SCHEME = "wasutv://";

    private static final String WASU_CATALOG_URL = "http://61.167.237.19:8080//wasu_catalog/catalog";

    public WasuPlugin() {
        super();
    }

    @Override
    public String getName() {
        return "华数TV";
    }

    @Override
    public boolean isSupported(String url) {
        if (url.startsWith(SCHEME)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected String decode(String url, Map<String, String> property) {
        if (url.startsWith(SCHEME)) {
            String contentCode = url.substring(SCHEME.length());

            return getPlayUrl(contentCode, property);
        }
        else {
            throw new IllegalArgumentException("invalid url");
        }
    }

    private String getPlayUrl(String contentCode, Map<String, String> property) {
        String url = "";

        byte[] content = HttpHelper.opPost(WASU_CATALOG_URL, createQueryXml(contentCode), property);
        if (content == null) {
            Log.e(TAG, "post xml fail");
        }
        else {
            XmlPullParser xmlParser = Xml.newPullParser();

            try {
                xmlParser.setInput(new ByteArrayInputStream(content), "utf-8");

                int eventType = xmlParser.getEventType();
                do {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT: {
                            break;
                        }
                        case XmlPullParser.START_TAG: {
                            String tagName = xmlParser.getName();

                            if (tagName.equalsIgnoreCase("playUrl")) {
                                int token = xmlParser.nextToken();
                                if (token == XmlPullParser.CDSECT) {
                                    url = xmlParser.getText();
                                }
                            }

                            break;
                        }
                        case XmlPullParser.END_TAG: {
                            break;
                        }
                        default: {
                            break;
                        }
                    }

                    eventType = xmlParser.next();
                }
                while (eventType != XmlPullParser.END_DOCUMENT);
            }
            catch (IOException e) {
                //ignore
            }
            catch (XmlPullParserException e) {
                Log.e(TAG, "parse xml fail, " + e.getMessage());
            }
        }

        return url;
    }

    private String createQueryXml(String contentCode) {
        StringBuffer xmlBuffer = new StringBuffer();

        xmlBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xmlBuffer.append("<message module=\"CATALOG_SERVICE\" version=\"1.0\">");
        xmlBuffer.append("<header action=\"REQUEST\" command=\"CONTENT_QUERY\" sequence=\"20121030212732_103861\" component-id=\"SYSTEM2\" component-type=\"THIRD_PARTY_SYSTEM\"/>");
        xmlBuffer.append("<body><contents><content>");
        xmlBuffer.append("<code>");
        xmlBuffer.append(contentCode);
        xmlBuffer.append("</code>");
        xmlBuffer.append("<site-code>1000689</site-code>");
        xmlBuffer.append("<items-index>-1</items-index>");
        xmlBuffer.append("<folder-code></folder-code>");
        xmlBuffer.append("<format>-1</format>");
        xmlBuffer.append("</content></contents></body>");
        xmlBuffer.append("</message>");

        return xmlBuffer.toString();
    }
}
