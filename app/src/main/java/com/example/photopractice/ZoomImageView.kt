package com.example.photopractice

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
//import android.support.v4.widget.DrawerLayout.LayoutParams
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.drawerlayout.widget.DrawerLayout

class ZoomImageView : View, OnTouchListener {
    private val ANDROID_NAMESPACE = "http://schemas.android.com/apk/res/android"
    private val DRAW_MODE_FIT = 0
    private val DRAW_MODE_MAX = 1
    private val DRAW_MODE_MIN = 2
    private var mContext: Context
    private var bitmap: Bitmap? = null
    private var paint: Paint? = null
    var imageRect: Rect? = null
        private set
    private var layoutWidthParam = 0
    private var oldPointsDistance = 0
    private var viewLength = 0
    private var viewWidth = 0
    private var viewHeight = 0
    private var drawMode = 0
    private var doubleTapDuration = 300
    var imageScale = 1f
        private set
    private var maxScale = 2f
    private var minScale = 0.5f
    private var actionDownTimes: Pair<Long, Long>? = null

    constructor(c: Context) : super(c) {
        mContext = c
        init()
    }

    constructor(c: Context, attrs: AttributeSet) : super(c, attrs) {
        mContext = c
        init()
        val layoutWidthParam = attrs.getAttributeIntValue(ANDROID_NAMESPACE, "layout_width", 0)
        width = layoutWidthParam
        val srcResourceId = attrs.getAttributeResourceValue(ANDROID_NAMESPACE, "src", 0)
        if (srcResourceId > 0) {
            setImageResourceId(srcResourceId)
        }
    }

    fun setWidth(width: Int) {
        layoutWidthParam = width
    }

    private fun init() {
        imageRect = Rect()
        paint = Paint()
        actionDownTimes = Pair(0L, 0L)
        setOnTouchListener(this)
    }

    fun setImageBitmap(bm: Bitmap?) {
        bitmap = bm
        refresh()
    }

    fun setImageResourceId(resourceId: Int) {
        val resources = mContext.resources
        bitmap = BitmapFactory.decodeResource(resources, resourceId)
        refresh()
    }

    val imagePoint: Point
        get() = Point(bitmapX, bitmapY)

    fun setMaxScale(scale: Float) {
        maxScale = scale
    }

    fun setMinScale(scale: Float) {
        minScale = scale
    }

    fun setDoubleTapDuration(duration: Int) {
        doubleTapDuration = duration
    }

    private fun refresh() {
        bitmapY = 0
        bitmapX = bitmapY
        imageScale = 1f
    }

    private var bitmapX = 0
    private var bitmapY = 0
    private var oldX = 0f
    private var oldY = 0f
    private var lastMultiX = 0f
    private var lastMultiY = 0f
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val pointCount = event.pointerCount
        var x = event.rawX
        var y = event.rawY
        if (oldX == 0f && oldY == 0f) {
            oldX = x
            oldY = y
        }
        if (lastMultiX != 0f || lastMultiY != 0f) {
            x = x - (x - lastMultiX)
            y = y - (y - lastMultiY)
        }
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (pointCount == 1) {
                    bitmapX += (x - oldX).toInt()
                    bitmapY += (y - oldY).toInt()
                    clearPinchDistance()
                } else if (pointCount == 2) {
                    val pointsdefference = getPointsDifference(
                        PointF(event.getX(0), event.getY(0)),
                        PointF(event.getX(1), event.getY(1))
                    )
                    if (oldPointsDistance == 0) {
                        oldPointsDistance = pointsdefference
                    }
                    val moveDifference =
                        (pointsdefference - oldPointsDistance).toFloat()
                    imageScale += moveDifference / viewLength
                    oldPointsDistance = pointsdefference
                    if (imageScale < minScale) {
                        imageScale = minScale
                    }
                } else {
                    clearPinchDistance()
                }
                invalidate()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                lastMultiX = x
                lastMultiY = y
            }
            MotionEvent.ACTION_UP -> {
                clearLastMultiXY()
                val firstTime = actionDownTimes!!.first
                val secondTime = actionDownTimes!!.second
                if (firstTime > 0 && secondTime > 0 && secondTime - firstTime < doubleTapDuration && System.currentTimeMillis() - secondTime < doubleTapDuration
                ) {
                    onDoubleTap()
                    invalidate()
                }
            }
            MotionEvent.ACTION_DOWN -> {
                clearLastMultiXY()
                actionDownTimes = Pair(
                    actionDownTimes!!.second,
                    System.currentTimeMillis()
                )
            }
        }
        oldX = x
        oldY = y
        return true
    }

    private fun clearPinchDistance() {
        oldPointsDistance = 0
    }

    private fun clearLastMultiXY() {
        lastMultiY = 0f
        lastMultiX = lastMultiY
    }

    private fun getPointsDifference(point1: PointF, point2: PointF): Int {
        return Math.sqrt((point2.x - point1.x) * (point2.x - point1.x) + (point2.y - point1.y) * (point2.y - point1.y).toDouble())
            .toInt()
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measureWidth = MeasureSpec.getSize(widthMeasureSpec)
        val bitmapWidth = bitmap!!.width
        val bitmapHeight = bitmap!!.height
        if (layoutWidthParam == DrawerLayout.LayoutParams.MATCH_PARENT || bitmapWidth > measureWidth) {
            val ratio = bitmapHeight.toFloat() / bitmapWidth.toFloat()
            viewWidth = measureWidth
            viewHeight = (viewWidth.toFloat() * ratio).toInt()
            if (bitmapWidth > measureWidth) {
                bitmap = Bitmap.createScaledBitmap(bitmap!!, viewWidth, viewHeight, false)
            }
        } else {
            viewWidth = bitmapWidth
            viewHeight = bitmapHeight
        }
        viewLength = ((viewWidth.toFloat() + viewHeight.toFloat()) / 2f).toInt()
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val correctedWidth = viewWidth * imageScale - viewWidth
        val correctedHeight = viewHeight * imageScale - viewHeight
        var correctedWidthHalf = 0
        var correctedHeightHalf = 0
        if (correctedWidth != 0f) {
            correctedWidthHalf = (correctedWidth / 2).toInt()
        }
        if (correctedHeight != 0f) {
            correctedHeightHalf = (correctedHeight / 2).toInt()
        }
        val startX1 = bitmapX - correctedWidthHalf
        val startY1 = bitmapY - correctedHeightHalf
        val startX2 = canvas.width + bitmapX + correctedWidthHalf
        val startY2 = canvas.height + bitmapY + correctedHeightHalf
        imageRect!![startX1, startY1, startX2] = startY2
        canvas.drawBitmap(bitmap!!, null, imageRect!!, paint)
    }

    private fun onDoubleTap() {
        when (drawMode) {
            DRAW_MODE_FIT -> {
                run {
                    bitmapY = 0
                    bitmapX = bitmapY
                }
                imageScale = 1f
            }
            DRAW_MODE_MAX -> imageScale = maxScale
            DRAW_MODE_MIN -> imageScale = minScale
            else -> {
            }
        }
        drawMode++
        if (drawMode > DRAW_MODE_MIN) {
            drawMode = 0
        }
    }
}