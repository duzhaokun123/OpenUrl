package io.github.duzhaokun123.openurl.module

import com.google.gson.annotations.SerializedName

data class Rule(
    @SerializedName("name")
    val name: String,
    @SerializedName("enabled")
    var enabled: Boolean = true,
    @SerializedName("matches")
    var matches: String = "",
    @SerializedName("regex")
    var regex: Boolean = false,
    @SerializedName("mode")
    var mode: Int = MODE_DIRECT,
    @SerializedName("targetPackage")
    var targetPackage: String = "",
    @SerializedName("targetActivity")
    var targetActivity: String = ""
) {
    companion object {
        const val MODE_DIRECT = 0
        const val MODE_ASK = 1
        const val MODE_CHOOSE = 2
        const val MODE_IGNORE = 3
    }
}
