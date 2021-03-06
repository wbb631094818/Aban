/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

import org.w3c.dom.DOMException;

/**
 *  Defines the test attributes interface. See the  Test attributes definition 
 * . 
 */
public interface ElementTest {
    /**
     *  The  systemBitrate value. 
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this attribute is readonly. 
     */
    public int getSystemBitrate();
    public void setSystemBitrate(int systemBitrate)
                                      throws DOMException;

    /**
     *  The  systemCaptions value. 
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this attribute is readonly. 
     */
    public boolean getSystemCaptions();
    public void setSystemCaptions(boolean systemCaptions)
                                      throws DOMException;

    /**
     *  The  systemLanguage value. 
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this attribute is readonly. 
     */
    public String getSystemLanguage();
    public void setSystemLanguage(String systemLanguage)
                                      throws DOMException;

    /**
     *  The result of the evaluation of the  systemRequired attribute. 
     */
    public boolean getSystemRequired();

    /**
     *  The result of the evaluation of the  systemScreenSize attribute. 
     */
    public boolean getSystemScreenSize();

    /**
     *  The result of the evaluation of the  systemScreenDepth attribute. 
     */
    public boolean getSystemScreenDepth();

    /**
     *  The value of the  systemOverdubOrSubtitle attribute. 
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this attribute is readonly. 
     */
    public String getSystemOverdubOrSubtitle();
    public void setSystemOverdubOrSubtitle(String systemOverdubOrSubtitle)
                                      throws DOMException;

    /**
     *  The value of the  systemAudioDesc attribute. 
     * @exception DOMException
     *    NO_MODIFICATION_ALLOWED_ERR: Raised if this attribute is readonly. 
     */
    public boolean getSystemAudioDesc();
    public void setSystemAudioDesc(boolean systemAudioDesc)
                                      throws DOMException;

}

