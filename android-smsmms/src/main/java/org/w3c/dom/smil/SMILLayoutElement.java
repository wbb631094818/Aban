/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

import org.w3c.dom.NodeList;

/**
 *  Declares layout type for the document. See the  LAYOUT element definition .
 *
 */
public interface SMILLayoutElement extends SMILElement {
    /**
     *  The mime type of the layout langage used in this layout element.The
     * default value of the type attribute is "text/smil-basic-layout".
     */
    public String getType();

    /**
     *  <code>true</code> if the player can understand the mime type,
     * <code>false</code> otherwise.
     */
    public boolean getResolved();

    /**
     * Returns the root layout element of this document.
     */
    public SMILRootLayoutElement getRootLayout();

    /**
     * Return the region elements of this document.
     */
    public NodeList getRegions();
}

