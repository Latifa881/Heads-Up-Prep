package com.example.headsupprep

import com.google.gson.annotations.SerializedName


class Celebrity{

    var data: List<CelebrityDetails>? = null

    class CelebrityDetails {
        @SerializedName("pk")
        var pk: Int? = null

        @SerializedName("name")
        var name: String? = null
        @SerializedName("taboo1")
        var taboo1: String? = null
        @SerializedName("taboo2")
        var taboo2: String? = null
        @SerializedName("taboo3")
        var taboo3: String? = null

        constructor( name: String?,taboo1:String?,taboo2:String?,taboo3:String?) {
            this.name = name
            this.taboo1=taboo1
            this.taboo2=taboo2
            this.taboo3=taboo3

        }
        constructor(pk:Int? ,name: String?,taboo1:String?,taboo2:String?,taboo3:String?) {
            this.pk=pk
            this.name = name
            this.taboo1=taboo1
            this.taboo2=taboo2
            this.taboo3=taboo3

        }
    }
}