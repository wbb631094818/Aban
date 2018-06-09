/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.dom.smil.parser;

import com.google.android.mms.MmsException;
import org.w3c.dom.smil.SMILDocument;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;

public class SmilXmlParser {
    private XMLReader mXmlReader;
    private SmilContentHandler mContentHandler;

    public SmilXmlParser() throws MmsException {
        //FIXME: Now we don't have the SAXParser wrapped inside,
        //       use the Driver class temporarily.
        System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");

        try {
            mXmlReader = XMLReaderFactory.createXMLReader();
            mContentHandler = new SmilContentHandler();
            mXmlReader.setContentHandler(mContentHandler);
        } catch (SAXException e) {
            throw new MmsException(e);
        }
    }

    public SMILDocument parse(InputStream in) throws IOException, SAXException {
        mContentHandler.reset();

        mXmlReader.parse(new InputSource(in));

        SMILDocument doc = mContentHandler.getSmilDocument();
        validateDocument(doc);

        return doc;
    }

    private void validateDocument(SMILDocument doc) {
        /*
         * Calling getBody() will create "smil", "head", and "body" elements if they
         * are not present. It will also initialize the SequentialTimeElementContainer
         * member of SMILDocument, which could not be set on creation of the document.
         * @see com.android.mms.dom.smil.SmilDocumentImpl#getBody()
         */
        doc.getBody();

        /*
         * Calling getLayout() will create "layout" element if it is not present.
         * @see com.android.mms.dom.smil.SmilDocumentImpl#getLayout()
         */
        doc.getLayout();
    }
}
