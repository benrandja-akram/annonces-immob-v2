package dz.esi.immob

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import dz.esi.immob.databinding.ActivityAnnonceDetailsBinding
import dz.esi.immob.repositories.AnnoncesRepo
import dz.esi.immob.view.ShareFragment
import kotlinx.android.synthetic.main.activity_annonce_details.*

class AnnonceDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress)
        intent?.getStringExtra("id")?.apply {
            AnnoncesRepo.instance.findAnnonceById(this){annonce ->
                Log.i("annoncedetails", annonce.toString())
                val binding: ActivityAnnonceDetailsBinding=
                    DataBindingUtil.setContentView(this@AnnonceDetails, R.layout.activity_annonce_details)
                binding.annonce = annonce
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                toolbar?.title = annonce?.title

                Glide.with(this@AnnonceDetails)
                    .load(annonce?.image)
                    .fitCenter()
                    .into(binding.image)


                sms.setOnClickListener {
                    ShareFragment(annonce?.link ?: annonce?.id!!, 0).show(supportFragmentManager, "sharefragment")
                }
                mail.setOnClickListener{
                    ShareFragment(annonce?.link ?: annonce?.id!!, 1).show(supportFragmentManager, "sharefragment")

                }

            }
        }
//
//        share.setOnClickListener{
//
//        }
    }


}
