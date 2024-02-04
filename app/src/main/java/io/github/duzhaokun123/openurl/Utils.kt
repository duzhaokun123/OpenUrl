package io.github.duzhaokun123.openurl

import android.content.ComponentName
import android.content.pm.ActivityInfo
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.addAfterTextChangedListener(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            listener(s.toString())
        }
    })
}

val ActivityInfo.componentNameCompat: ComponentName
    get() = ComponentName(packageName, name)