package dz.esi.immob.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dz.esi.immob.R
import dz.esi.immob.view.viewmodel.AnnoncesViewModel
import kotlinx.android.synthetic.main.filter_dialog.*

class FilterDialogFragment : DialogFragment() {

    lateinit var model: AnnoncesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProviders.of(activity!!)[AnnoncesViewModel::class.java]
    }
    var wilaya: String? = null
    var category: String? = null
    var type: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.setTitle("Filter")
        Log.i("FilterDialogFragment", "onViewCreated " + wilaya + category + type + view)

        wilayaSpinner?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                wilaya = p0?.getItemAtPosition(pos) as? String
            }
        }
        categorySpinner?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                category = p0?.getItemAtPosition(pos) as? String
            }
        }
        typeSpinner?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                type = p0?.getItemAtPosition(pos) as? String
            }
        }
        ok.setOnClickListener{
            model.filter(wilaya, category, type)
            dialog?.cancel()
            Log.i("FilterDialogFragment", "onViewCreated " + wilaya + category + type + view)
        }
        cancel.setOnClickListener{
            model.cancelFilter()
            dialog?.cancel()
            Log.i("FilterDialogFragment", "onViewCreated " + wilaya + category + type + view)
        }
    }

}