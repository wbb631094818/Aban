/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

/**
 *  Declares rendering surface for an element. See the  region attribute 
 * definition . 
 */
public interface SMILRegionInterface {
    /**
     */
    public SMILRegionElement getRegion();
    public void setRegion(SMILRegionElement region);

}

