/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

/**
 *  This interface define the set of animation target extensions. 
 */
public interface ElementTargetAttributes {
    /**
     *  The name of the target attribute. 
     */
    public String getAttributeName();
    public void setAttributeName(String attributeName);

    // attributeTypes
    public static final short ATTRIBUTE_TYPE_AUTO       = 0;
    public static final short ATTRIBUTE_TYPE_CSS        = 1;
    public static final short ATTRIBUTE_TYPE_XML        = 2;

    /**
     *  A code representing the value of the  attributeType attribute, as 
     * defined above. Default value is <code>ATTRIBUTE_TYPE_CODE</code> . 
     */
    public short getAttributeType();
    public void setAttributeType(short attributeType);

}

