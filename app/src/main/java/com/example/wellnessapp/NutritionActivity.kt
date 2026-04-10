package com.example.wellnessapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class NutritionActivity : AppCompatActivity() {

    var rewardedAd: RewardedAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nutrition)

//       Invoke the function to load the reward ad
        loadRewardedAd(this)

//        Find the button by use of its ID
        val rewardButton = findViewById<Button>(R.id.rewardAd)
        rewardButton.setOnClickListener {
            showRewardedAd(this)
        }





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun loadRewardedAd(context: Context) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(context,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            })
    }

    fun showRewardedAd(activity: Activity) {
        if (rewardedAd != null) {
            rewardedAd?.show(activity) { rewardItem ->
                // Give reward to user
                val rewardAmount = rewardItem.amount
                val rewardType = rewardItem.type

                println("User earned: $rewardAmount $rewardType")
            }
        } else {
            println("Ad not ready")
        }
    }
}