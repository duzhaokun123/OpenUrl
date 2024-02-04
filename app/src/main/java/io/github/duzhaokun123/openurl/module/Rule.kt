package io.github.duzhaokun123.openurl.module

data class Rule(
    val name: String,
    var enabled: Boolean = true,
    var matches: String = "",
    var regex: Boolean = false,
    var mode: Int = MODE_DIRECT,
    var targetPackage: String = "",
    var targetActivity: String = ""
) {
    companion object {
        const val MODE_DIRECT = 0
        const val MODE_ASK = 1
        const val MODE_CHOOSE = 2
        const val MODE_IGNORE = 3
    }
}
