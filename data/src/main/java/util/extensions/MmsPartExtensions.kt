/*
 * Copyright (c) 2018. Arash Hatami
 */
package util.extensions

import com.google.android.mms.ContentType
import model.MmsPart

fun MmsPart.isSmil() = ContentType.APP_SMIL == type

fun MmsPart.isImage() = ContentType.isImageType(type)

fun MmsPart.isVideo() = ContentType.isVideoType(type)

fun MmsPart.isText() = ContentType.isTextType(type)

fun MmsPart.hasThumbnails() = isImage() || isVideo()