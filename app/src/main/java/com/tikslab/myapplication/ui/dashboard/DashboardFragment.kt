package com.tikslab.myapplication.ui.dashboard

import android.animation.Animator
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.tikslab.myapplication.Adapter
import com.tikslab.myapplication.AdapterList
import com.tikslab.myapplication.R
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlin.math.abs
import kotlin.math.roundToLong


class DashboardFragment : Fragment() {


    private val data = arrayListOf(
        "Lesson 01", "Lesson 02", "Lesson 03", "Lesson 04", "Lesson 05",
        "Lesson 06", "Lesson 07", "Lesson 08", "Lesson 09", "Lesson 10",
        "Lesson 11", "Lesson 12", "Lesson 13", "Lesson 14", "Lesson 15"
    )

    private var isExpand = false
    private var startY = 0f
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            adapter = Adapter(data)
            layoutManager = LinearLayoutManager(this.context, HORIZONTAL, false)
        }
        layoutManager = LinearLayoutManager(this.context, VERTICAL, false)
        list.apply {
            adapter = AdapterList(data)
            layoutManager = this@DashboardFragment.layoutManager
        }

        isScroll(isExpand)
    }


    fun startAnim(speed: Long) {
        val dur = ((7f / speed) * 100000).roundToLong()
        if (!isExpand) {
            startY = cardView.y
            cardView.animate().apply {
                y(0f)
                duration = dur
                setListener(MyAnimatorListener())
            }
        } else {
            cardView.animate().apply {
                y(startY)
                duration = dur
                setListener(MyAnimatorListener())
            }
        }

    }

    fun isScroll(isScroll: Boolean) {
        val detector = GestureDetector(this.context, MyGestureListener())

        if (!isScroll)
            list.setOnTouchListener { _, event ->
                detector.onTouchEvent(event); true
            }
        else
            list.setOnTouchListener { _, event ->
                detector.onTouchEvent(event)
            }

    }

    inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            val y2 = e2?.y ?: 0f
            val y1 = e1?.y ?: 0f

            //swipe top to bot
            if (y1 > y2 && !isExpand) {
                startAnim(abs(velocityY.roundToLong()))
                isExpand = true
            } else {
                //swipe bot to top
                if (y1 < y2 && isExpand && layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    startAnim(abs(velocityY.roundToLong()))
                    isExpand = false

                }
            }

            return super.onFling(e1, e2, velocityX, velocityY)
        }

    }

    inner class MyAnimatorListener : Animator.AnimatorListener {

        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            isScroll(isExpand)
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }
    }
}