package com.ineedyourcode.groovymovie.view.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ineedyourcode.groovymovie.R
import com.ineedyourcode.groovymovie.databinding.FragmentContactsBinding

const val REQUEST_CODE = 13

class ContactsFragment : Fragment() {
    val TAG_FOR_BACKSTACK = "ContactsFragment"

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }

                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(getString(R.string.alert_dialog_contacts_permission_title))
                        .setMessage(getString(R.string.alert_dialog_contacts_permission_message))
                        .setPositiveButton(getString(R.string.alert_dialog_contacts_permission_positive)) { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton(getString(R.string.alert_dialog_contacts_permission_negative)) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    getContacts()
                }
                return
            }
        }
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursor ->
                while (cursor.moveToNext()) {
                    val name =
                        cursorWithContacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                            .let { it1 -> cursor.getString(it1) }
                    name?.let { it1 -> addView(it, it1) }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun addView(context: Context, textToShow: String) {
        binding.containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            setTextColor(resources.getColor(R.color.white, context.theme))
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
