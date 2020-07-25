package com.utn.mychampsteam.entities

class Champion (name: String, userID: String, type: String, stars: String, power: String,
                singularAbility: String, urlImgPortrait: String, urlImgFeatured: String,
                urlBiography: String, urlVideo: String) {

    var name: String
    var userID: String
    var type: String
    var stars: String
    var power: String
    var singularAbility: String
    var urlImgPortrait: String
    var urlImgFeatured: String
    var urlBiography: String
    var urlVideo: String

    constructor(): this("","","","","","","","","","")

    init {
        this.name = name!!
        this.userID = userID!!
        this.type = type!!
        this.stars = stars!!
        this.power = power!!
        this.singularAbility = singularAbility!!
        this.urlImgPortrait = urlImgPortrait!!
        this.urlImgFeatured = urlImgFeatured!!
        this.urlBiography = urlBiography!!
        this.urlVideo = urlVideo!!
    }
}