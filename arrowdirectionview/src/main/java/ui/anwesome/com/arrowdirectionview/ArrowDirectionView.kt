package ui.anwesome.com.arrowdirectionview

/**
 * Created by anweshmishra on 27/02/18.
 */
import android.view.*
import android.content.*
import android.graphics.*
class ArrowDirectionView(ctx : Context) : View(ctx) {
    val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer : Renderer = Renderer(this)
    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }
    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }
    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var jDir : Int = 1, var j : Int = 0) {
        val scales : Array<Float> = arrayOf(0f, 0f)
        fun update(stopcb : () -> Unit) {
            scales[j] += 0.1f * dir
            if(Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += jDir
                if(j == scales.size || j == -1) {
                    jDir *= -1
                    j += jDir
                    prevScale = scales[j]
                    if(j == 0) {
                        dir = 0f
                        stopcb()
                    }
                    else {
                        dir = -1f
                    }
                }
            }
        }
        fun startUpdating(startcb : () -> Unit) {
            if(dir == 0f) {
                dir = 1f
                startcb()
            }
        }
    }
    data class Animator(var view : View, var animated : Boolean = false) {
        fun animate(updatecb : () -> Unit) {
            if(animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                }
                catch(ex : Exception) {

                }
            }
        }
        fun start() {
            if(!animated) {
                animated = true
                view.postInvalidate()
            }
        }
        fun stop() {
            if(animated) {
                animated = false
            }
        }
    }
    data class ArrowDirection(var i : Int, val state : State = State()) {
        fun draw(canvas : Canvas, paint : Paint) {
            val w = canvas.width.toFloat()
            val h = canvas.height.toFloat()
            val size = Math.min(w, h)/15
            val l = Math.min(w, h)/5
            canvas.save()
            canvas.translate(w / 2, h / 2)
            paint.color = Color.WHITE
            for(i in 0..3) {
                canvas.save()
                canvas.rotate(90f * i * state.scales[0])
                canvas.translate(0f , -l * state.scales[1])
                canvas.drawTriangle(size, paint)
                canvas.restore()
            }
            canvas.restore()
        }
        fun update(stopcb : () -> Unit) {
            state.update(stopcb)
        }
        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }
    }
    data class Renderer(var view : ArrowDirectionView) {
        val arrowDirection : ArrowDirection = ArrowDirection(0)
        val animator : Animator = Animator(view)
        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            arrowDirection.draw(canvas, paint)
            animator.animate {
                arrowDirection.update {
                    animator.stop()
                }
            }
        }
        fun handleTap() {
            arrowDirection.startUpdating {
                animator.start()
            }
        }
    }
}
fun Canvas.drawTriangle(size : Float, paint : Paint) {
    val path = Path()
    path.moveTo(-size / 2, size / 2)
    path.lineTo(size / 2, size / 2)
    path.lineTo(0f, -size / 2)
    drawPath(path, paint)
}