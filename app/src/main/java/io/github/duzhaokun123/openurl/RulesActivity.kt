package io.github.duzhaokun123.openurl

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.LinearLayout
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import io.github.duzhaokun123.openurl.module.Rule

class RulesActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rules_activity)
        reloadRules()
        findViewById<Button>(R.id.btn_add).setOnClickListener {
            val etName = EditText(this).apply {
                hint = "Rule Name"
            }
            AlertDialog.Builder(this)
                .setTitle("Add Rule")
                .setView(etName)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    val name = etName.text.toString()
                    if (name.isNotBlank()) {
                        rules.add(Rule(name))
                        reloadRules()
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
        findViewById<Button>(R.id.btn_save).setOnClickListener {
            Application.instance.saveRules()
        }
    }

    private fun reloadRules() {
        val llRules = findViewById<LinearLayout>(R.id.ll_rules)
        llRules.removeAllViews()
        rules.forEach { rule ->
            val ruleItem = layoutInflater.inflate(R.layout.rule_item, llRules, false)
            ruleItem.findViewById<TextView>(R.id.tv_name).text = rule.name
            ruleItem.findViewById<Button>(R.id.btn_delete).setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle("Delete Rule")
                    .setMessage("Are you sure to delete rule ${rule.name}?")
                    .setPositiveButton(android.R.string.ok) { _, _ ->
                        rules.remove(rule)
                        reloadRules()
                    }
                    .setNegativeButton(android.R.string.cancel, null)
                    .show()
            }
            ruleItem.findViewById<Button>(R.id.btn_up).setOnClickListener {
                val index = rules.indexOf(rule)
                if (index > 0) {
                    rules.removeAt(index)
                    rules.add(index - 1, rule)
                    reloadRules()
                }
            }
            ruleItem.findViewById<Button>(R.id.btn_down).setOnClickListener {
                val index = rules.indexOf(rule)
                if (index < rules.size - 1) {
                    rules.removeAt(index)
                    rules.add(index + 1, rule)
                    reloadRules()
                }
            }
            ruleItem.findViewById<CheckBox>(R.id.cb_enabled).apply {
                isChecked = rule.enabled
                setOnCheckedChangeListener { _, isChecked ->
                    rule.enabled = isChecked
                }
            }
            ruleItem.findViewById<CheckBox>(R.id.cb_regex).apply {
                isChecked = rule.regex
                setOnCheckedChangeListener { _, isChecked ->
                    rule.regex = isChecked
                }
            }
            ruleItem.findViewById<EditText>(R.id.et_matches).apply {
                setText(rule.matches)
                addAfterTextChangedListener {
                    rule.matches = it
                }
            }
            ruleItem.findViewById<EditText>(R.id.et_mode).apply {
                setText(rule.mode.toString())
                addAfterTextChangedListener {
                    rule.mode = it.toIntOrNull() ?: 0
                }
            }
            ruleItem.findViewById<EditText>(R.id.et_targetPackage).apply {
                setText(rule.targetPackage)
                addAfterTextChangedListener {
                    rule.targetPackage = it
                }
            }
            ruleItem.findViewById<EditText>(R.id.et_targetActivity).apply {
                setText(rule.targetActivity)
                addAfterTextChangedListener {
                    rule.targetActivity = it
                }
            }
            llRules.addView(ruleItem, MATCH_PARENT, WRAP_CONTENT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Application.instance.saveRules()
    }
}