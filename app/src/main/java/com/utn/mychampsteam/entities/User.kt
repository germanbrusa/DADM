package com.utn.mychampsteam.entities

class User (uid: String, email: String, nickname: String, avatar: String) {

    var uid: String
    var email: String
    var nickname: String
    var avatar: String
//    var myTeam: MutableList<Champion> = ArrayList()

    constructor(): this("", "", "", "")

    init {
        this.uid = uid!!
        this.email = email!!
        this.nickname = nickname!!
        this.avatar = avatar!!
    }

/*    class userTeam {
        companion object Champion
    }*/

}