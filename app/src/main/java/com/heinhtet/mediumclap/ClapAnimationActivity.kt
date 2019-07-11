package com.heinhtet.mediumclap

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView

/**
 * Created by heinhtet on 10,July,2019
 */
class ClapAnimationActivity : AppCompatActivity() {


    private val handler = Handler(Looper.getMainLooper())
    private val runnalbe = Runnable {
        hideClapCount()
    }
    val clapCountFrame by lazy {
        findViewById<FrameLayout>(R.id.clap_count_frame)
    }

    private fun hideClapCount() {
        ObjectAnimator.ofFloat(1f, 0f)
            .apply {
                addUpdateListener {
                    clapCountFrame.alpha = it.animatedValue as Float
                }

                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        clapCountFrame.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
            }.start()

    }

    private var clapCount = 0


    val clapIv by lazy {
        findViewById<ImageView>(R.id.clap_iv)
    }

    val clapTv by lazy {
        findViewById<AppCompatTextView>(R.id.clap_count_tv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clap_layout)
        makeClapShrink()
        setClapCount()

        clapIv.setOnClickListener {
            this.clapCount++
            removeHandler()
            setClapCount()
            makeClapShrink()
            showCircleCount()
        }
    }

    private fun setClapCount() {
        clapTv.text = "${clapCount}+"

    }

    private fun showCircleCount() {
        clapCountFrame.visibility = View.VISIBLE
        val clapObjectAnimator = ValueAnimator.ofFloat(0f, 1f)
            .apply {
                duration = 400
                interpolator = AccelerateDecelerateInterpolator()
            }
        clapObjectAnimator.addUpdateListener {
            clapCountFrame.alpha = it.animatedValue as Float
        }
        val transactionY = ObjectAnimator.ofFloat(clapCountFrame, "translationY", 120f, 0f).apply {
            duration = 1000
            interpolator = BounceInterpolator()
        }
        //we play together for alpha and y values
        AnimatorSet().apply {
            play(clapObjectAnimator).with(transactionY)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    removeHandler()
                    handler.postDelayed(runnalbe, 600)
                }

                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {
                }
            })
        }.start()
    }

    private fun removeHandler() {
        if (handler.hasCallbacks(runnalbe)) {
            handler.removeCallbacks(runnalbe)
        }
    }

    private fun makeClapShrink() {
        val startValue = 0.5f
        val endValue = 1f
        val scaleX = ObjectAnimator.ofFloat(clapIv, "scaleX", startValue, endValue)
        val scaleY = ObjectAnimator.ofFloat(clapIv, "scaleY", startValue, endValue)
        AnimatorSet().apply {
            play(scaleX).with(scaleY)
        }.start()
    }
}