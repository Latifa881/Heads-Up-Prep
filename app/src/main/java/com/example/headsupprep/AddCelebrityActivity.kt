package com.example.headsupprep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCelebrityActivity : AppCompatActivity() {
    lateinit var etCelebrityName:EditText
    lateinit var  etTaboo1:EditText
    lateinit var  etTaboo2:EditText
    lateinit var  etTaboo3:EditText
    lateinit var btAddCelebrity:Button
    lateinit var btBack:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_celebrity)
        etCelebrityName=findViewById(R.id.etCelebrityName)
        etTaboo1=findViewById(R.id.etTaboo1)
        etTaboo2=findViewById(R.id.etTaboo2)
        etTaboo3=findViewById(R.id.etTaboo3)
        btAddCelebrity=findViewById(R.id.btAddCelebrity)
        btBack=findViewById(R.id.btBack)

        btAddCelebrity.setOnClickListener {
            val name=etCelebrityName.text.toString()
            val taboo1=etTaboo1.text.toString()
            val taboo2=etTaboo2.text.toString()
            val taboo3=etTaboo3.text.toString()
            if(name.isNotEmpty()&& taboo1.isNotEmpty()&&taboo2.isNotEmpty()&&taboo3.isNotEmpty()){
                addDetails(name,taboo1,taboo2, taboo3)
            }else{
                Toast.makeText(this,"Enter all the information",Toast.LENGTH_SHORT).show()
            }
        }
        btBack.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun addDetails(name:String,taboo1:String,taboo2 :String,taboo3:String ) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.addCelebrityDetails(Celebrity.CelebrityDetails(name,taboo1,taboo2 ,taboo3 ))
                .enqueue(object : Callback<List<Celebrity.CelebrityDetails>> {
                    override fun onResponse(
                        call: Call<List<Celebrity.CelebrityDetails>>,
                        response: Response<List<Celebrity.CelebrityDetails>>
                    ) {
                        Toast.makeText(applicationContext,"Added successfully!",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(call: Call<List<Celebrity.CelebrityDetails>>, t: Throwable){
                        call.cancel()
                    }
                })

        }
    }
}