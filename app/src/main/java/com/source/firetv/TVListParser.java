package com.source.firetv;

import android.util.Xml;

import com.source.Channel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

final class TVListParser {
    public static List<Channel> parse(File file) {
        List<Channel> channels;

        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            channels = parse(input);
        }
        catch (IOException e) {
            channels = null;
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    //ignore
                }
            }
        }

        return channels;
    }

    public static List<Channel> parse(InputStream input) throws IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(input, "utf-8");

            parser.nextTag();
            return readChannels(parser);
        }
        catch (XmlPullParserException e) {
            throw new IOException("parse fail");
        }
    }

    private static List<Channel> readChannels(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "tvlists");

        List<Channel> channels = new LinkedList<Channel>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals("tvlist")) {
                Channel channel = readChannel(parser);
                channels.add(channel);
            }
            else {
                skip(parser);
            }
        }

        return channels;
    }

    private static Channel readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "tvlist");

        Channel channel = new Channel();

        String value = parser.getAttributeValue(null, "id");
        if (value.contains("|")) {
            String[] results = value.split("|");

            for (int i = 0; i < results.length; i++) {
                channel.addGroupId(results[i]);
            }
        }
        else {
            channel.addGroupId(value);
        }

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tagName = parser.getName();
            if (tagName.equals("tvname")) {
                String text = readName(parser);

                channel.setName(text);
            }
            else if (tagName.equals("tvaddr")) {
                String text = readSource(parser);

                if (text.contains("|")) {
                    String[] results = text.split("|");

                    for (int i = 0; i < results.length; i++) {
                        channel.addSource(results[i]);
                    }
                }
                else {
                    channel.addSource(text);
                }
            }
            else {
                skip(parser);
            }
        }

        return channel;
    }

    private static String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "tvname");

        String name = "";
        if (parser.next() == XmlPullParser.TEXT) {
            name = parser.getText().trim();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, "tvname");

        return name;
    }

    private static String readSource(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "tvaddr");

        String source = "";
        if (parser.next() == XmlPullParser.TEXT) {
            source = parser.getText().trim();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, "tvaddr");

        return source;
    }

    private static void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
        int depth = 1;

        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG: {
                    depth--;
                    break;
                }
                case XmlPullParser.START_TAG: {
                    depth++;
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
}
