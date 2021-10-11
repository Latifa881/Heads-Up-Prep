package com.example.headsupprep

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateDeleteActivity : AppCompatActivity() {
    lateinit var etCelebrityName: EditText
    lateinit var etTaboo1: EditText
    lateinit var etTaboo2: EditText
    lateinit var etTaboo3: EditText
    lateinit var btBack: Button
    lateinit var btDeleteCelebrity: Button
    lateinit var btUpdateCelebrity: Button
   // private val apiInterface by lazy { APIClient().getClient()?.create(APIInterface::class.java) }
    var celebrityID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_delete)
        etCelebrityName = findViewById(R.id.etCelebrityName)
        etTaboo1 = findViewById(R.id.etTaboo1)
        etTaboo2 = findViewById(R.id.etTaboo2)
        etTaboo3 = findViewById(R.id.etTaboo3)
        btBack = findViewById(R.id.btBack)
        btUpdateCelebrity = findViewById(R.id.btUpdateCelebrity)
        btDeleteCelebrity = findViewById(R.id.btDeleteCelebrity)
        btDeleteCelebrity.setOnClickListener {
            conformationDialog(0, celebrityID)
        }
        btUpdateCelebrity.setOnClickListener {
            if (etCelebrityName.text.toString().isNotEmpty() &&
                etTaboo1.text.toString().isNotEmpty() &&
                etTaboo2.text.toString().isNotEmpty() &&
                etTaboo3.text.toString().isNotEmpty()
            ) {
                conformationDialog(1, celebrityID)
            } else {
                Toast.makeText(this, "Enter all the information", Toast.LENGTH_SHORT).show()
            }

        }
        btBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val intent=getIntent()
                celebrityID = intent.getIntExtra("celebrityID", 0)
       // Toast.makeText(this, "${celebrityID}  ID yes", Toast.LENGTH_LONG).show()
        getCelebrityDetails()
    }

    fun getCelebrityDetails() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.getOneCelebrityDetails(celebrityID).enqueue(object : Callback<Celebrity.CelebrityDetails> {
                    override fun onResponse(
                        call: Call<Celebrity.CelebrityDetails>,
                        response: Response<Celebrity.CelebrityDetails>
                    ) {
                        val celebrity = response.body()!!

                            etCelebrityName.setText(celebrity.name)
                            etTaboo1.setText(celebrity.taboo1)
                            etTaboo2.setText(celebrity.taboo2)
                            etTaboo3.setText(celebrity.taboo3)

                    }

                    override fun onFailure(
                        call: Call<Celebrity.CelebrityDetails>,
                        t: Throwable
                    ) {
                        Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("HHHHHHHHH",t.toString())
                        call.cancel()
                    }

                })
        }
    }
    fun conformationDialog(code: Int, id: Int) {
        //code: 0->Delete , 1->Update
        var text = ""
        var title = ""
        var message = ""
        when {
            code == 0 -> {
                text = "Delete"
                title = "Deleting Celebrity Details"
                message = "Are you sure you want to delete this celebrity details?"
            }
            code == 1 -> {
                text = "Update"
                title = "Updating Celebrity Details"
                message = "Are you sure you want to update this celebrity details?"
            }
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        builder.setMessage(message)
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton(text) { dialogInterface, which ->
            if (code == 0) {
                delete(id)
            } else if (code == 1) {
                update(
                    id,
                    etCelebrityName.text.toString(),
                    etTaboo1.text.toString(),
                    etTaboo2.text.toString(),
                    etTaboo3.text.toString()
                )
            }
        }

        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            dialogInterface.cancel()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun update(id: Int, name: String, taboo1: String, taboo2: String, taboo3: String) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.updateCelebrityDetails(
            id,
            Celebrity.CelebrityDetails(name, taboo1, taboo2, taboo3)
        )?.enqueue(
            object : Callback<Celebrity.CelebrityDetails> {
                override fun onResponse(
                    call: Call<Celebrity.CelebrityDetails>,
                    response: Response<Celebrity.CelebrityDetails>
                ) {
                    Toast.makeText(
                        this@UpdateDeleteActivity,
                        "Updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onFailure(call: Call<Celebrity.CelebrityDetails>, t: Throwable) {

                    Toast.makeText(this@UpdateDeleteActivity, "Updated failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            })


    }
    fun delete(id: Int) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.deleteCelebrityDetails(id)?.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Toast.makeText(applicationContext, "Deleted successfully!", Toast.LENGTH_SHORT)
                    .show()
                val intent= Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                call.cancel()
            }
        })
    }
}