/*
 * Copyright (c) 2018. Arash Hatami
 */
package mapper

interface Mapper<in From, out To> {

    fun map(from: From): To

}