package io.github.duzhaokun123.openurl

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment

class SettingsActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager.beginTransaction().replace(android.R.id.content, PrefsFragment()).commit()
    }

    class PrefsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings_activity)
            findPreference("hide_icon").onPreferenceChangeListener = this
            findPreference("rules").onPreferenceClickListener = this
        }

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
            when(preference.key) {
                "hide_icon" -> {
                    val hideIcon = newValue as Boolean
                    val pm = activity.packageManager
                    val componentName = ComponentName(activity, SettingsActivity::class.java.name + "Alias")
                    val newState = if (hideIcon) PackageManager.COMPONENT_ENABLED_STATE_DISABLED else PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    pm.setComponentEnabledSetting(componentName, newState, PackageManager.DONT_KILL_APP)
                }
            }
            return true
        }

        override fun onPreferenceClick(preference: Preference): Boolean {
            when(preference.key) {
                "rules" -> {
                    startActivity(Intent(activity, RulesActivity::class.java))
                }
            }
            return true
        }

    }
}