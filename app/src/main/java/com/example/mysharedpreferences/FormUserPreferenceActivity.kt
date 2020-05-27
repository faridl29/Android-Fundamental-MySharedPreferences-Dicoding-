package com.example.mysharedpreferences

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_form_user_preference.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btn_save

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_user_preference)

        userModel = intent.getParcelableExtra("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when(formType){
            TYPE_ADD -> {
                actionBarTitle = "Tambah baru"
                btnTitle = "simpan"
            }

            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_save.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showPreferenceInForm() {
        edt_name.setText(userModel.name)
        edt_email.setText(userModel.email)
        edt_age.setText(userModel.age.toString())
        edt_phone.setText(userModel.phoneNumber)
        if (userModel.isLove) {
            rb_yes.isChecked = true
        } else {
            rb_no.isChecked = true
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.btn_save) {
            val name = edt_name.text.toString().trim()
            val email = edt_email.text.toString().trim()
            val age = edt_age.text.toString().trim()
            val phoneNumber = edt_phone.text.toString().trim()
            val isLoveMU = rg_love_mu.checkedRadioButtonId == R.id.rb_yes

            if(name.isEmpty()) {
                edt_name.error = FIELD_REQUIRED
                return
            }

            if(email.isEmpty()) {
                edt_email.error = FIELD_REQUIRED
                return
            }

            if(!isValidEmail(email)) {
                edt_email.error = FIELD_IS_NOT_VALID
                return
            }

            if(age.isEmpty()) {
                edt_age.error = FIELD_REQUIRED
                return
            }

            if(phoneNumber.isEmpty()) {
                edt_phone.error = FIELD_REQUIRED
                return
            }

            if((!TextUtils.isDigitsOnly(phoneNumber))) {
                edt_phone.error = FIELD_DIGIT_ONLY
                return
            }

            saveUser(name, email, age, phoneNumber, isLoveMU)

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)
            setResult(RESULT_CODE, resultIntent)

            finish()
        }
    }

    private fun saveUser(name: String, email: String, age: String, phoneNumber: String, isLoveMU: Boolean) {
        val userPreference = UserPreferences(this)

        userModel.name = name
        userModel.email = email
        userModel.age = age.toInt()
        userModel.phoneNumber = phoneNumber
        userModel.isLove = isLoveMU

        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()

    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}