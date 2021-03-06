import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hallert.voteforreddit.R

abstract class SwipeVoteCallBack(
    private val context: Context?,
    private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val swipeDistance = 200f
        var newDx: Float = dX
        if (newDx >= swipeDistance) {
            newDx = swipeDistance
        } else if (newDx <= -swipeDistance) {
            newDx = -swipeDistance
        }

        val itemView = viewHolder.itemView

        val upvoteBackground = context?.resources?.getColor(R.color.upvoteColor, null)?.let {
            ColorDrawable(
                it
            )
        }
        val downvoteBackground = context?.resources?.getColor(R.color.downvoteColor, null)?.let {
            ColorDrawable(
                it
            )
        }

        val upvoteIcon = context?.let {
            ResourcesCompat.getDrawable(
                it.resources,
                R.drawable.ic_swipe_upvote,
                null
            )
        }
        val downvoteIcon = context?.let {
            ResourcesCompat.getDrawable(
                it.resources,
                R.drawable.ic_swipe_downvote,
                null
            )
        }

        when {
            // swipe to the right
            dX > 0 -> {
                var backgroundRight = swipeDistance.toInt()

                var iconLeft =
                    itemView.left + (swipeDistance.toInt() / 2) - upvoteIcon!!.intrinsicWidth
                val iconTop = itemView.top + (itemView.height / 2) - (upvoteIcon.intrinsicHeight)
                var iconRight =
                    itemView.left + (swipeDistance.toInt() / 2) + upvoteIcon.intrinsicWidth
                val iconBottom =
                    itemView.top + (itemView.height / 2) + (upvoteIcon.intrinsicHeight / 2)

                if (dX <= swipeDistance) {
                    backgroundRight = itemView.left + dX.toInt()

                    if (dX <= upvoteIcon.intrinsicWidth) {
                        iconLeft = 0
                        iconRight = 0
                    } else {
                        iconLeft = itemView.left + (dX.toInt() / 2) - upvoteIcon.intrinsicWidth
                        iconRight = itemView.left + (dX.toInt() / 2) + upvoteIcon.intrinsicWidth
                    }
                }

                upvoteBackground?.setBounds(
                    itemView.left,
                    itemView.top,
                    backgroundRight,
                    itemView.bottom
                )

                upvoteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
            // swipe to the left
            dX < 0 -> {
                var backgroundLeft = itemView.right - swipeDistance.toInt()

                var iconLeft =
                    itemView.right - (swipeDistance.toInt() / 2) - downvoteIcon!!.intrinsicWidth
                val iconTop = itemView.top + (itemView.height / 2) - (downvoteIcon.intrinsicHeight)
                var iconRight =
                    itemView.right - (swipeDistance.toInt() / 2) + downvoteIcon.intrinsicWidth
                val iconBottom =
                    itemView.top + (itemView.height / 2) + (downvoteIcon.intrinsicHeight / 2)

                if (dX >= -swipeDistance) {
                    backgroundLeft = itemView.right + dX.toInt()

                    if (dX >= -downvoteIcon.intrinsicWidth) {
                        iconLeft = 0
                        iconRight = 0
                    } else {
                        iconLeft = itemView.right + (dX.toInt() / 2) - upvoteIcon!!.intrinsicWidth
                        iconRight = itemView.right + (dX.toInt() / 2) + upvoteIcon.intrinsicWidth
                    }
                }

                downvoteBackground?.setBounds(
                    backgroundLeft,
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                downvoteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }
        }

        downvoteBackground?.draw(c)
        upvoteBackground?.draw(c)

        upvoteIcon?.draw(c)
        downvoteIcon?.draw(c)

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            newDx,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.25f
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            onSwipeRight(viewHolder.adapterPosition)
        } else if (direction == ItemTouchHelper.LEFT) {
            onSwipeLeft(viewHolder.adapterPosition)
        }

        adapter.notifyItemChanged(viewHolder.adapterPosition)
    }


    fun swipeAnimation(recyclerView: RecyclerView) {
        //recyclerView.itemAnimator!!.changeDuration = 0

        // val animator = recyclerView.itemAnimator as SimpleItemAnimator
        // animator.supportsChangeAnimations = false
    }


    abstract fun onSwipeLeft(position: Int)
    abstract fun onSwipeRight(position: Int)
}