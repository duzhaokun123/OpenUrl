package io.github.duzhaokun123.openurl

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import io.github.duzhaokun123.openurl.module.Rule
import java.io.File

val rules = mutableListOf<Rule>()

val gson = Gson()

class Application : android.app.Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Application
    }

    init {
        instance = this
    }

    val ruleFile by lazy {
        val file = File(filesDir, "rules.json")
        if (file.exists().not()) {
            file.createNewFile()
            file.writeText("[]")
        }
        file
    }

    fun saveRules() {
        ruleFile.writeText(gson.toJson(rules))
        Toast.makeText(this, "rules saved", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate() {
        super.onCreate()
        runCatching {
            rules.addAll(gson.fromJson(ruleFile.readText(), Array<Rule>::class.java))
        }.onFailure {
            Log.e("Application", "onCreate: ", it)
            Toast.makeText(this, "load rules failed, try clear data", Toast.LENGTH_LONG).show()
        }
    }
}