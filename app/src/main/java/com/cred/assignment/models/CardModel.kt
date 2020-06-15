package com.cred.assignment.models

import io.realm.RealmObject

open class CardModel : RealmObject() {
    var name = ""
    var cardNumber = ""
    var month = ""
    var year = ""
    var security = ""
    var brand = ""
}