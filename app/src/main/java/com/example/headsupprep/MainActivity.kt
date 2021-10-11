package com.example.headsupprep

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var rvMain:RecyclerView
    lateinit var btAddCelebrity:Button
    lateinit var etCelebrityName:EditText
    lateinit var btSubmit:Button
    val details = arrayListOf<Celebrity.CelebrityDetails>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain=findViewById(R.id.rvMain)
        btAddCelebrity=findViewById(R.id.btAddCelebrity)
        etCelebrityName=findViewById(R.id.etCelebrityName)
        btSubmit=findViewById(R.id.btSubmit)

        rvMain.adapter = RecyclerViewAdapter(details)
        rvMain.layoutManager = LinearLayoutManager(applicationContext)
        getDetails()

        btAddCelebrity.setOnClickListener {
            val intent= Intent(this,AddCelebrityActivity::class.java)
            startActivity(intent)
        }
        btSubmit.setOnClickListener {
           val name= etCelebrityName.text.toString()
            if(name.isNotEmpty()){
                checkIfCelebrityNameExist()
            }
            else{
                Toast.makeText(this,"Enter a name",Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun getDetails() {
        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setMessage("Please wait")
        progressDialog.show()

        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCelebrityDetails()?.enqueue(object : Callback<List<Celebrity.CelebrityDetails>> {
                override fun onResponse(
                    call: Call<List<Celebrity.CelebrityDetails>>,
                    response: Response<List<Celebrity.CelebrityDetails>>
                ) {
                    progressDialog.dismiss()
                    Log.d("TAG", response.code().toString() + "")
                    for (User in response.body()!!) {
                        val pk=User.pk
                        val name = User.name
                        val taboo1=User.taboo1
                        val taboo2=User.taboo2
                        val taboo3=User.taboo3
                        details.add(Celebrity.CelebrityDetails(pk,name,taboo1,taboo2,taboo3))
                    }
                    rvMain.adapter!!.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<Celebrity.CelebrityDetails>>, t: Throwable) {
                    // Toast.makeText(applicationContext, ""+t.message, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss()
                    call.cancel()
                }
            })
        }


    }
    fun checkIfCelebrityNameExist() {
        var celebrityID = 0
        var counter=0
        for(celebrity in details){
            //Toast.makeText(this, "${celebrity.name}  found", Toast.LENGTH_LONG).show()
            if(etCelebrityName.text.toString().capitalize() == celebrity.name){
                celebrityID = celebrity.pk.toString().toInt()
                val intent = Intent(applicationContext, UpdateDeleteActivity::class.java)
                intent.putExtra("celebrityID", celebrityID)
            //    Toast.makeText(this, "${celebrityID} ID", Toast.LENGTH_LONG).show()
                startActivity(intent)
            }else{
                counter++
            }
            if(counter==details.size){
                Toast.makeText(this, "${etCelebrityName.text.toString().capitalize()} not found", Toast.LENGTH_LONG).show()
            }
        }

    }
}