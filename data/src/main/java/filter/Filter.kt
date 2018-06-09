/*
 * Copyright (c) 2018. Arash Hatami
 */
package filter

abstract class Filter<in T> {

    abstract fun filter(item: T, query: CharSequence): Boolean

}