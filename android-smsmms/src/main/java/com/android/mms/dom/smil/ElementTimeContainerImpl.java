/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.dom.smil;

import org.w3c.dom.smil.ElementTimeContainer;
import org.w3c.dom.smil.SMILElement;

public abstract class ElementTimeContainerImpl extends ElementTimeImpl implements
        ElementTimeContainer {

    /*
     * Internal Interface
     */

    ElementTimeContainerImpl(SMILElement element) {
        super(element);
    }
}
