package com.example.juan.surfeetac;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SpotXmlParser {

    private static final String ns = null;

    // We don't use namespaces

    public List<Spot> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            return readList(parser);

        } finally {
            in.close();
        }
    }

    private List<Spot> readList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Spot> entries = new ArrayList<Spot>();
        //System.out.println(parser.toString());
        parser.require(XmlPullParser.START_TAG, ns, "list");
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombre = parser.getName();
            // Starts by looking for the entry tag
            //System.out.println(nombre);
            if (nombre.equals("models.Spot")) {
                entries.add(readSpot(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // This class represents a single user in the XML users.
    // It includes the data members "email," "password," and "fullname."
    public static class Spot {

        public final String name;
        public final String description;

        private Spot(String name, String description) {
            this.name = name;
            this.description = description;

        }
    }

    // Parses the contents of an models.User. If it encounters a email, password, or fullname, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Spot readSpot(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "models.Spot");
        String name = null;
        String description = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String nombre = parser.getName();
            if (nombre.equals("name")) {
                name = readName(parser);
            } else if (nombre.equals("description")) {
                description = readDescription(parser);
            }
            else {
                skip(parser);
            }
        }
        return new Spot(name, description);
    }

    // Processes email tags in the feed.
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String email = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        return email;
    }

    // Processes password tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String password = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return password;
    }

    // For the tags email, password and fullname, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
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
}