/*
 * Copyright (c) 2018. Arash Hatami
 */
package filter

import model.Contact
import util.extensions.removeAccents
import javax.inject.Inject

class ContactFilter @Inject constructor(private val phoneNumberFilter: PhoneNumberFilter) : Filter<Contact>() {

    override fun filter(item: Contact, query: CharSequence): Boolean {
        return item.name.removeAccents().contains(query, true) || // Name
                item.numbers.map { it.address }.any { address -> phoneNumberFilter.filter(address, query) } // Number
    }

}