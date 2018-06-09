/*
 * Copyright (c) 2018. Arash Hatami
 */

package com.android.mms.transaction;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * An interface to represent the state of an observable Transaction.
 */
public abstract class Observable {
    private final ArrayList<Observer> mObservers;
    private Iterator<Observer> mIterator;

    public Observable() {
        mObservers = new ArrayList<Observer>();
    }

    /**
     * This method is implemented by the observable to represent its
     * current state.
     *
     * @return A TransactionState object.
     */
    abstract public TransactionState getState();

    /**
     * Attach an observer to this object.
     *
     * @param observer The observer object to be attached to.
     */
    public void attach(Observer observer) {
        mObservers.add(observer);
    }

    /**
     * Detach an observer from this object.
     *
     * @param observer The observer object to be detached from.
     */
    public void detach(Observer observer) {
        if (mIterator != null) {
            mIterator.remove();
        } else {
            mObservers.remove(observer);
        }
    }

    /**
     * Notify all observers that a status change has occurred.
     */
    public void notifyObservers() {
        mIterator = mObservers.iterator();
        try {
            while (mIterator.hasNext()) {
                mIterator.next().update(this);
            }
        } finally {
            mIterator = null;
        }
    }
}
