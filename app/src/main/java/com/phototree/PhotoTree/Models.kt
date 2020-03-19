package com.phototree.PhotoTree

class Folder{
    var _folderID : Int = 0
    var folderName : String = ""
}

class Photo{
    var _photoID : Int = 0
    var affiliationID : Int = 0
    var memo : String = ""
    var date: String = ""
    var time: String = ""
    var image: ByteArray? = null
}