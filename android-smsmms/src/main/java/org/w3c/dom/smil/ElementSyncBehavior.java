/*
 * Copyright (c) 2018. Arash Hatami
 */

package org.w3c.dom.smil;

/**
 *  The synchronization behavior extension. 
 */
public interface ElementSyncBehavior {
    /**
     *  The runtime synchronization behavior for an element. 
     */
    public String getSyncBehavior();

    /**
     *  The sync tolerance for the associated element. It has an effect only if
     *  the element has <code>syncBehavior="locked"</code> . 
     */
    public float getSyncTolerance();

    /**
     *  Defines the default value for the runtime synchronization behavior for 
     * an element, and all descendents. 
     */
    public String getDefaultSyncBehavior();

    /**
     *  Defines the default value for the sync tolerance for an element, and 
     * all descendents. 
     */
    public float getDefaultSyncTolerance();

    /**
     *  If set to true, forces the time container playback to sync to this 
     * element.  
     */
    public boolean getSyncMaster();

}

