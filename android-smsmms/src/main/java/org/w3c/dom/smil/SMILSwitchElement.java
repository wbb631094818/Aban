/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

import org.w3c.dom.Element;

/**
 *  Defines a block of content control. See the  switch element definition . 
 */
public interface SMILSwitchElement extends SMILElement {
    /**
     *  Returns the slected element at runtime. <code>null</code> if the 
     * selected element is not yet available. 
     * @return  The selected <code>Element</code> for thisd <code>switch</code>
     *    element. 
     */
    public Element getSelectedElement();

}

