package dz.esi.immob.view

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dz.esi.immob.R
import dz.esi.immob.adapters.ContactsAdapter
import kotlinx.android.synthetic.main.contact_list.view.*
import me.everything.providers.android.contacts.ContactsProvider
import android.Manifest.permission
import android.Manifest.permission.READ_CONTACTS
import androidx.core.app.ActivityCompat
import android.content.DialogInterface
import android.os.Build
import android.annotation.TargetApi
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.filter_dialog.*
import me.everything.providers.android.contacts.Contact
import me.everything.providers.core.AbstractProvider


class ShareFragment(val link: String, val type: Int) : DialogFragment() {
    val PERMISSION_REQUEST_CONTACT = 12
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.contact_list, container , false )
    }

    override fun onStart() {
        super.onStart()
        askForContactPermission()
    }


    fun askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    READ_CONTACTS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity!!,
                        READ_CONTACTS
                    )
                ) {
                    val builder = AlertDialog.Builder(activity)
                    builder.setTitle("Contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("please confirm Contacts access")//TODO put real question
                    builder.setOnDismissListener(DialogInterface.OnDismissListener {
                        requestPermissions(
                            arrayOf(READ_CONTACTS), PERMISSION_REQUEST_CONTACT
                        )
                    })
                    builder.show()
                } else {

                    ActivityCompat.requestPermissions(
                        activity!!,
                        arrayOf(READ_CONTACTS),
                        PERMISSION_REQUEST_CONTACT
                    )
                }
            } else {
                getContact()
            }
        } else {
            getContact()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CONTACT -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact()
                } else {
                    Toast.makeText(activity!!, "No permission for contacts", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }


    fun getContact(){
        val contactsProvider = ContactsProvider(context)
        view?.recyclerContact?.adapter = ContactsAdapter(
            if(type == 0)contactsProvider.contacts?.list else EmailProvider(context!!).getContacts()).also{
            it.onContactSelectedListener =  if(type == 0 )onSmsContactSelectedListener else onMailContactSelectedListener
        }

    }



    private fun sendSMS(numberSMS: String, bodySMS: String) {
        val smsIntent = Intent(Intent.ACTION_VIEW)
        smsIntent.type = "vnd.android-dir/mms-sms"
        smsIntent.putExtra("address", numberSMS)
        smsIntent.putExtra("sms_body", bodySMS)
        try {
            startActivity(smsIntent)
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "No application found that can handle this action",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun sendEmail(recieverMail: String, body: String) {
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(recieverMail))
        i.putExtra(Intent.EXTRA_SUBJECT, "Real Estate you might like !")
        i.putExtra(Intent.EXTRA_TEXT, body)
        try {
            startActivity(Intent.createChooser(i, "Send mail..."))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast
                .makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT)
                .show()
        }
    }


    val onSmsContactSelectedListener =  object: ContactsAdapter.OnContactSelectedListener {
        override fun onClicked(contact: Contact) {
            sendSMS(contact.phone!!, link)
            dialog?.cancel()
        }
    }

    val onMailContactSelectedListener =  object: ContactsAdapter.OnContactSelectedListener {
        override fun onClicked(contact: Contact) {
            sendEmail(contact.email, link)
            dialog?.cancel()
        }
    }
}


class EmailProvider(context: Context) : AbstractProvider(context) {
    fun getContacts(): MutableList<Contact> {
        return getContentTableData(Contact.uriEmail, Contact::class.java).list
    }
}