/*
 * Copyright (c) 2018. Arash Hatami
 */
package feature.plus

data class PlusState(
        val upgraded: Boolean = false,
        val upgradePrice: String = "",
        val upgradeDonatePrice: String = "",
        val currency: String = ""
)