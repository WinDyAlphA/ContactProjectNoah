package com.contactproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AddEmailDialog: DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(android.R.color.darker_gray);
        return inflater.inflate(R.layout.dialog_email, container, false)
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()

        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        val btnok = view?.findViewById<View>(R.id.ok)
        val btncancel = view?.findViewById<View>(R.id.annuler)

        btnok?.setOnClickListener {
            val email = view?.findViewById<EditText>(R.id.email)?.text.toString()
            if (email.isNotEmpty()) {
                (activity as addContactActivity).addEmail(email)
                dismiss()
            }
        }
        btncancel?.setOnClickListener {
            dismiss()
        }
    }

}