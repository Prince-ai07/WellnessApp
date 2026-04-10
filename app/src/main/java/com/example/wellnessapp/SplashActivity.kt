package com.example.wellnessapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.wellnessapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Full screen immersive
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSplashSequence()
    }

    private fun startSplashSequence() {
        // ── Step 1: Decorative circles slide in (0ms) ──────────────────────
        animateDecorCircles()

        // ── Step 2: Icon ring scales + fades in (300ms delay) ──────────────
        handler.postDelayed({ animateIconRing() }, 300)

        // ── Step 3: Center content fades up (600ms delay) ──────────────────
        handler.postDelayed({ animateCenterContent() }, 600)

        // ── Step 4: Divider line expands (1050ms delay) ────────────────────
        handler.postDelayed({ animateDividerLine() }, 1050)

        // ── Step 5: Loader appears + dots pulse (1400ms delay) ─────────────
        handler.postDelayed({ animateLoader() }, 1400)

        // ── Step 6: Navigate to MainActivity after 3.6s ────────────────────
        handler.postDelayed({ navigateToMain() }, 3600)
    }

    // ── Decorative circles: slide in from edges ──────────────────────────────
    private fun animateDecorCircles() {
        val topCircle = binding.decorCircleTopLeft
        val bottomCircle = binding.decorCircleBottomRight

        topCircle.translationX = -100f
        topCircle.translationY = -100f
        topCircle.alpha = 0f

        bottomCircle.translationX = 100f
        bottomCircle.translationY = 100f
        bottomCircle.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(topCircle, "translationX", -100f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(topCircle, "translationY", -100f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(topCircle, "alpha", 0f, 1f).setDuration(900),
                ObjectAnimator.ofFloat(bottomCircle, "translationX", 100f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(bottomCircle, "translationY", 100f, 0f).setDuration(900),
                ObjectAnimator.ofFloat(bottomCircle, "alpha", 0f, 1f).setDuration(900)
            )
            interpolator = DecelerateInterpolator(1.5f)
            start()
        }
    }

    // ── Icon ring: scale from 0 with overshoot ───────────────────────────────
    private fun animateIconRing() {
        val ring = binding.iconRing
        ring.scaleX = 0f
        ring.scaleY = 0f
        ring.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(ring, "scaleX", 0f, 1f).setDuration(600),
                ObjectAnimator.ofFloat(ring, "scaleY", 0f, 1f).setDuration(600),
                ObjectAnimator.ofFloat(ring, "alpha", 0f, 1f).setDuration(400)
            )
            interpolator = OvershootInterpolator(1.8f)
            start()
        }

        // Slow continuous rotation on the icon
        ObjectAnimator.ofFloat(binding.splashIcon, "rotation", 0f, 360f).apply {
            duration = 8000
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    // ── Center content: fade + slide up ─────────────────────────────────────
    private fun animateCenterContent() {
        val content = binding.centerContent
        content.translationY = 50f
        content.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(content, "alpha", 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(content, "translationY", 50f, 0f).setDuration(700)
            )
            interpolator = DecelerateInterpolator(2f)
            start()
        }
    }

    // ── Divider line: expand from 0 width to 180dp ───────────────────────────
    private fun animateDividerLine() {
        val divider = binding.dividerLine
        val targetWidth = (180 * resources.displayMetrics.density).toInt()

        ValueAnimator.ofInt(0, targetWidth).apply {
            duration = 600
            interpolator = DecelerateInterpolator()
            addUpdateListener { animator ->
                val params = divider.layoutParams
                params.width = animator.animatedValue as Int
                divider.layoutParams = params
            }
            start()
        }
    }

    // ── Loader: fade in, then animate 3 dots with staggered pulse ────────────
    private fun animateLoader() {
        val container = binding.loaderContainer
        container.translationY = 30f
        container.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(container, "alpha", 0f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(container, "translationY", 30f, 0f).setDuration(500)
            )
            interpolator = DecelerateInterpolator()
            start()
        }

        // Staggered dot pulse
        val dots = listOf(binding.dot1, binding.dot2, binding.dot3)
        dots.forEachIndexed { index, dot ->
            handler.postDelayed({
                startDotPulse(dot)
            }, index * 160L)
        }
    }

    private fun startDotPulse(dot: View) {
        val scaleUp = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(dot, "scaleX", 1f, 1.6f).setDuration(400),
                ObjectAnimator.ofFloat(dot, "scaleY", 1f, 1.6f).setDuration(400),
                ObjectAnimator.ofFloat(dot, "alpha", 0.5f, 1f).setDuration(400)
            )
            interpolator = AccelerateDecelerateInterpolator()
        }
        val scaleDown = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(dot, "scaleX", 1.6f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(dot, "scaleY", 1.6f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(dot, "alpha", 1f, 0.5f).setDuration(400)
            )
            interpolator = AccelerateDecelerateInterpolator()
        }

        val pulseSet = AnimatorSet()
        pulseSet.playSequentially(scaleUp, scaleDown)
        pulseSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                // Loop the pulse while on screen
                if (!isFinishing) pulseSet.start()
            }
        })
        pulseSet.start()
    }

    // ── Navigate ─────────────────────────────────────────────────────────────
    private fun navigateToMain() {
        // Fade the whole screen out
        ObjectAnimator.ofFloat(binding.root, "alpha", 1f, 0f).apply {
            duration = 400
            interpolator = AccelerateDecelerateInterpolator()
            addListener(object : android.animation.AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: android.animation.Animator) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            })
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}