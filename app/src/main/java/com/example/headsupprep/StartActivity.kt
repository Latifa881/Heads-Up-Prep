package com.example.headsupprep

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import org.w3c.dom.Text
import pl.droidsonroids.gif.GifImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.timer

class StartActivity : AppCompatActivity() {
    //
    lateinit var btStart: GifImageView
    lateinit var llStart: LinearLayout
    lateinit var tvTimer: TextView
    lateinit var tvRotate: TextView
    lateinit var llCelebrity: LinearLayout
    lateinit var tvName: TextView
    lateinit var tvTaboo1: TextView
    lateinit var tvTaboo2: TextView
    lateinit var tvTaboo3: TextView
    lateinit var clMainGam: ConstraintLayout
    lateinit var ivSettings: ImageView
    lateinit var ivEdit: ImageView
    lateinit var ivTimer: ImageView
    lateinit var ivSound: ImageView
    lateinit var tvCounter3to1: TextView
    lateinit var llCounter3to1: LinearLayout
    lateinit var soundSharedPreferences: SharedPreferences
    lateinit var scoreSharedPreferences: SharedPreferences
    lateinit var tvHighScore:TextView
    var isGameActive = false
    var isNewGame = false
    var isSoundOn = true
    var counter = 0
    var score = 0
    var timerSeconds: Long = 60000 //60 seconds
    var toggle = true
    val details = arrayListOf<Celebrity.CelebrityDetails>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        btStart = findViewById(R.id.btStart)
        llStart = findViewById(R.id.llStart)
        tvTimer = findViewById(R.id.tvTimer)
        tvRotate = findViewById(R.id.tvRotate)
        llCelebrity = findViewById(R.id.llCelebrity)
        tvName = findViewById(R.id.tvName)
        tvTaboo1 = findViewById(R.id.tvTaboo1)
        tvTaboo2 = findViewById(R.id.tvTaboo2)
        tvTaboo3 = findViewById(R.id.tvTaboo3)
        clMainGam = findViewById(R.id.clMainGam)
        ivSettings = findViewById(R.id.ivSettings)
        ivEdit = findViewById(R.id.ivEdit)
        ivTimer = findViewById(R.id.ivTimer)
        ivSound = findViewById(R.id.ivSound)
        tvCounter3to1 = findViewById(R.id.tvCounter3to1)
        llCounter3to1 = findViewById(R.id.llCounter3to1)
        tvHighScore=findViewById(R.id.tvHighScore)

        updateState(-1)
        getCelebrities()
        tvHighScore.setText("High Score:"+ getHighScore())
        isSoundOn = getSoundPreference()
        if (getSoundPreference())//Sound is on
        {
            ivSound.setImageResource(R.drawable.sound_on)
        } else {
            ivSound.setImageResource(R.drawable.sound_off)
        }
        btStart.setOnClickListener {
            updateState(0)
            val sound=MediaPlayer.create(this@StartActivity, R.raw.countdown_sound)
             object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvCounter3to1.setText("" + ((millisUntilFinished / 1000)+1))
                    if(isSoundOn)
                    { sound.start()}
                }

                override fun onFinish() {
                    isNewGame = true
                    if(isSoundOn)
                    { sound.pause()}
                    updateState(4)
                }
            }.start()

        }
        ivSettings.setOnClickListener {
            toggle = if (toggle) {
                ivTimer.visibility = View.VISIBLE
                ivEdit.visibility = View.VISIBLE
                ivSound.visibility = View.VISIBLE
                ivTimer.animate().xBy(0F).xBy(-120F)
                ivEdit.animate().xBy(0F).xBy(-230F)
                ivSound.animate().xBy(0F).xBy(-350F)

                false
            } else {
                ivTimer.visibility = View.GONE
                ivEdit.visibility = View.GONE
                ivSound.visibility = View.GONE
                ivTimer.animate().xBy(0F).xBy(120F)
                ivEdit.animate().xBy(0F).xBy(230F)
                ivSound.animate().xBy(0F).xBy(350F)
                true
            }
        }
        ivEdit.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        ivTimer.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Chang the timer")

            builder.setMessage("Choose the timer duration for the game!")
            builder.setIcon(R.drawable.timer)

            builder.setPositiveButton("30 sec") { dialogInterface, which ->
                timerSeconds = 30000
            }
            builder.setNegativeButton("60 sec") { dialogInterface, which ->
                timerSeconds = 60000
            }
            builder.setNeutralButton("2 min") { dialogInterface, which ->
                timerSeconds = 180000
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
        ivSound.setOnClickListener {
            if (isSoundOn) {
                isSoundOn = false
                setSoundPreference()
                ivSound.setImageResource(R.drawable.sound_off)
            } else {
                isSoundOn = true
                setSoundPreference()
                ivSound.setImageResource(R.drawable.sound_on)
            }

        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            //  Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT).show()
            if (isNewGame) {
                playGame()
                isNewGame = false
            }
            if (isGameActive) {
                updateState(1)
            }

        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            //Toast.makeText(this, "PORTRAIT", Toast.LENGTH_SHORT).show()
            if (isGameActive) {
                updateState(2)
            }

        }
    }

    fun playGame() {
        var sound = MediaPlayer.create(this@StartActivity, R.raw.tick_tock_clock_timer)
        if (!isGameActive) {
            isGameActive = true
            var timer = object : CountDownTimer(timerSeconds, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tvTimer.setText("Timer: " + (millisUntilFinished / 1000))
                    if ((millisUntilFinished / 1000).toInt() == 10 && isSoundOn) {
                        sound.start()
                    }
                }
                override fun onFinish() {
                    tvTimer.setText("Timer:${timerSeconds/100}")
                    isGameActive = false
                    isNewGame = true
                    setHighScore()
                    tvHighScore.setText("High Score:"+ getHighScore())
                    counter=0
                    sound.pause()
                    updateState(3)
                }
            }.start()
        }
    }

    fun getCelebrities() {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCelebrityDetails()
                ?.enqueue(object : Callback<List<Celebrity.CelebrityDetails>> {
                    override fun onResponse(
                        call: Call<List<Celebrity.CelebrityDetails>>,
                        response: Response<List<Celebrity.CelebrityDetails>>
                    ) {
                        Log.d("TAG", response.code().toString() + "")
                        for (User in response.body()!!) {
                            val pk = User.pk
                            val name = User.name
                            val taboo1 = User.taboo1
                            val taboo2 = User.taboo2
                            val taboo3 = User.taboo3
                            details.add(
                                Celebrity.CelebrityDetails(
                                    pk,
                                    name,
                                    taboo1,
                                    taboo2,
                                    taboo3
                                )
                            )
                        }
                        details.shuffle()
                    }

                    override fun onFailure(
                        call: Call<List<Celebrity.CelebrityDetails>>,
                        t: Throwable
                    ) {
                        call.cancel()
                    }
                })
        }
    }

    fun setCelebrityDetails() {

        if (counter < details.size) {
            tvName.text = details[counter].name
            tvTaboo1.text = details[counter].taboo1
            tvTaboo2.text = details[counter].taboo2
            tvTaboo3.text = details[counter].taboo3
            counter++// Next Celebrity
        }
    }

    fun updateState(code: Int) {
        //code: -1-> Initial state
        //       0-> CountDown Timer
        //       1-> Game is active & Landscape orientation
        //       2- >Game is active & PORTRAIT orientation
        //       3-> Timer is finished
        //       4-> Start new game
        when (code) {
            -1 -> {

                llStart.visibility = View.VISIBLE
                clMainGam.visibility = View.GONE
                llCounter3to1.visibility = View.GONE
            }
            0 -> {
                llStart.visibility = View.GONE
                clMainGam.visibility = View.GONE
                tvRotate.visibility = View.GONE
                llCounter3to1.visibility = View.VISIBLE

            }
            1 -> {
                setCelebrityDetails()
                llCelebrity.visibility = View.VISIBLE
                tvRotate.visibility = View.GONE
                llCounter3to1.visibility = View.GONE
            }
            2 -> {
                llCelebrity.visibility = View.GONE
                tvRotate.visibility = View.VISIBLE
                llCounter3to1.visibility = View.GONE
            }
            3 -> {
                llStart.visibility = View.VISIBLE
                clMainGam.visibility = View.GONE
                tvRotate.visibility = View.GONE
                llCounter3to1.visibility = View.GONE
            }
            4 -> {
                llStart.visibility = View.GONE
                clMainGam.visibility = View.VISIBLE
                tvRotate.visibility = View.VISIBLE
                llCounter3to1.visibility = View.GONE
            }
        }
    }

    fun setSoundPreference()//shared preferences
    {
        soundSharedPreferences = this.getSharedPreferences("HeadsUp sound", Context.MODE_PRIVATE)
        // We can save data with the following code
        with(soundSharedPreferences.edit()) {
            putBoolean("sound on", isSoundOn)
            apply()
        }
    }

    fun getSoundPreference(): Boolean {
        soundSharedPreferences =
            this.getSharedPreferences("HeadsUp sound", Context.MODE_PRIVATE)
        var myScore = soundSharedPreferences.getBoolean(
            "sound on",
            true
        ) // --> retrieves data from Shared Preferences
        return myScore
    }

    fun setHighScore()//shared preferences
    {
        score = getHighScore()

        if (counter - 1 > score) {
            scoreSharedPreferences = this.getSharedPreferences("HeadsUp Score", Context.MODE_PRIVATE)
            // We can save data with the following code
            with(scoreSharedPreferences.edit()) {
                putInt("High Score", counter-1)
                apply()
            }

        }
    }

    fun getHighScore(): Int {
        scoreSharedPreferences =
            this.getSharedPreferences("HeadsUp Score", Context.MODE_PRIVATE)
        var myScore =
            scoreSharedPreferences.getInt("High Score",0) // --> retrieves data from Shared Preferences
        return myScore
    }
}