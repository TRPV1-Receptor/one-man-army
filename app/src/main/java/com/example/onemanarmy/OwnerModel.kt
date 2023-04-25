package com.example.onemanarmy

data class OwnerModel(
    var userId: String? = null,
    var userName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var userType: String? = null,
    var businessName: String? = null,
    var businessAddress: String? = null,
    var businessEmail: String? = null,
    var businessPhone: String? = null,
    var serviceProvided: String? = null,
    var businessBio: String? = null,
    var businessSkills: String? = null,
    var profilePic: Int? = null,
) : java.io.Serializable
