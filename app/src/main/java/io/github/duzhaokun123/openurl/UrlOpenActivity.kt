package io.github.duzhaokun123.openurl

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import io.github.duzhaokun123.openurl.module.Rule

class UrlOpenActivity : Activity() {
    companion object {
        const val TAG = "UrlOpenActivity"
    }

    lateinit var openables: List<ResolveInfo>
    lateinit var showOpenables: List<ResolveInfo>
    lateinit var labels: Array<CharSequence>

    private val pm by lazy { packageManager }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.data == null) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
            finishAndRemoveTask()
            return
        }

        val domain = intent.data!!.host

        var matchedRule: Rule? = null

        for (rule in rules) {
            if (matchedRule != null) break
            if (rule.enabled.not()) continue
            val matches = rule.matches.split("\n").map { it.trim() }.filter { it.isNotBlank() }
            for (match in matches) {
                if (rule.regex) {
                    if (Regex(match).matches(domain)) {
                        matchedRule = rule
                        break
                    }
                } else {
                    if (domain == match) {
                        matchedRule = rule
                        break
                    }
                }
            }
        }

        if (matchedRule != null) {
//            Toast.makeText(this, "Matched rule: ${matchedRule.name}", Toast.LENGTH_SHORT).show()
            if (matchedRule.mode == Rule.MODE_DIRECT
                && matchedRule.targetPackage.isNotBlank()
                && matchedRule.targetActivity.isNotBlank()
            ) {
                open(ComponentName(matchedRule.targetPackage, matchedRule.targetActivity))
                return
            }
        }

        val openIntent = Intent(intent.action, intent.data)
        openables = pm.queryIntentActivities(openIntent, PackageManager.MATCH_ALL)
            .filter { it.activityInfo.componentNameCompat != componentName }
        val dontShowPackages = preferences.getString("dont_show_packages", "")?.split("\n")?.map { it.trim() } ?: emptyList()
        showOpenables = openables.filter { it.activityInfo.packageName !in dontShowPackages }
        labels = showOpenables.map { it.activityInfo.loadLabel(pm) }.toTypedArray()

        if (matchedRule != null) {
            val matchedOpenable = openables.find { it.activityInfo.packageName == matchedRule.targetPackage }
            when(matchedRule.mode) {
                Rule.MODE_DIRECT -> {
                    matchedOpenable?.let { open(it.activityInfo.componentNameCompat) }
                        ?: run { choose() }
                }
                Rule.MODE_ASK -> {
                    matchedOpenable?.let { ask(it.activityInfo.componentNameCompat, it.activityInfo.loadLabel(pm).toString()) }
                        ?: run { choose() }
                }
                Rule.MODE_CHOOSE -> choose()
                Rule.MODE_IGNORE -> finishAndRemoveTask()
                else -> {
                    Log.e(TAG, "Unknown mode: ${matchedRule.mode}")
                    Toast.makeText(this, "Unknown mode: ${matchedRule.mode}", Toast.LENGTH_SHORT).show()
                    finishAndRemoveTask()
                }
            }
            return
        }

        choose()
    }

    private fun choose() {
        AlertDialog.Builder(this)
            .setTitle(intent.dataString)
            .setItems(labels) { _, which ->
                val openable = showOpenables[which]
                open(openable.activityInfo.componentNameCompat)
            }
            .setOnCancelListener { finishAndRemoveTask() }
            .show()
    }

    private fun open(componentName: ComponentName) {
        val openableIntent = intent.apply {
            setComponent(componentName)
            setPackage(componentName.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(openableIntent)
        finishAndRemoveTask()
    }

    private fun ask(componentName: ComponentName, label: String, ) {
        AlertDialog.Builder(this)
            .setTitle("Open with $label")
            .setMessage(intent.dataString)
            .setPositiveButton(android.R.string.ok) { _, _ -> open(componentName) }
            .setNegativeButton(android.R.string.cancel) { _, _ -> finishAndRemoveTask() }
            .setNeutralButton("Choose") { _, _ -> choose() }
            .setOnCancelListener { finishAndRemoveTask() }
            .show()
    }
}