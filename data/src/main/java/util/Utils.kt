/*
 * Copyright (c) 2018. Arash Hatami
 */
package util

fun <T> tryOrNull(body: () -> T?): T? {
    return try {
        body()
    } catch (e: Exception) {
        null
    }
}