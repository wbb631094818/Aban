/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

import org.w3c.dom.NodeList;

/**
 *  This is a placeholder - subject to change. This represents generic 
 * timelines. 
 */
public interface ElementTimeContainer extends ElementTime {
    /**
     *  A NodeList that contains all timed childrens of this node. If there are
     *  no timed children, the <code>Nodelist</code> is empty.  An iterator 
     * is more appropriate here than a node list but it requires Traversal 
     * module support. 
     */
    public NodeList getTimeChildren();

    /**
     *  Returns a list of child elements active at the specified invocation. 
     * @param instant  The desired position on the local timeline in 
     *   milliseconds. 
     * @return  List of timed child-elements active at instant. 
     */
    public NodeList getActiveChildrenAt(float instant);

}

