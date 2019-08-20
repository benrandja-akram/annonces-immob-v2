package dz.esi.immob.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dz.esi.immob.R
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.filter_dialog.*
import kotlinx.android.synthetic.main.filter_dialog.view.*

class FilterDialogFragment : DialogFragment() {
//    var contentView: View? = null
    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(this)[AnnoncesViewModel::class.java]
    }
    var wilaya: String? = null
    var category: String? = null
    var type: String? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
//            setupListeners(customView)

            builder.setTitle("Filter result")
                .setView(R.layout.filter_dialog)
                .setPositiveButton("Ok"
                ) { dialog, id ->
                    Log.i("FilterDialogFragment", "onpositive " + wilaya + category + type)
                    model.filter(wilaya, category, type)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, id ->
                    Log.i("FilterDialogFragment", "osetNegativeButton " + wilaya + category + type)
//                    model.cancelDialogFilter()
                }


            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }

    fun setupListeners(view: View) {
        Log.i("FilterDialogFragment", "onViewCreated " + wilaya + category + type)


        view.wilayaSpinner.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                wilaya = p0?.getItemAtPosition(pos) as? String
            }
        }

        view.categorySpinner.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                category = p0?.getItemAtPosition(pos) as? String
            }
        }

        view.typeSpinner.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                type = p0?.getItemAtPosition(pos) as? String
            }
        }

    }
}