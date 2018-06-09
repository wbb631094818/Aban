/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

/**
 *  This interface represents the  set element. 
 */
public interface SMILSetElement extends ElementTimeControl, ElementTime, ElementTargetAttributes, SMILElement {
    /**
     *  Specifies the value for the attribute during the duration of this 
     * element. 
     */
    public String getTo();
    public void setTo(String to);

}

