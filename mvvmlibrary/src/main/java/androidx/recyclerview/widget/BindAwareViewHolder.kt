package androidx.recyclerview.widget

import android.view.View

/**
 *创建时间：2019/12/11
 *编写人：kanghb
 *功能描述：
 */
abstract class BindAwareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun onBind()

    abstract fun onUnBind()


     override fun setFlags(flags: Int, mask: Int) {
        val wasBound = isBound
        super.setFlags(flags, mask)
        notifyBinding(wasBound, isBound)
    }

    override fun addFlags(flags: Int) {
        val wasBound = isBound
        super.addFlags(flags)
        notifyBinding(wasBound,isBound)
    }

    override fun clearPayload() {
        val wasBound = isBound
        super.clearPayload()
        notifyBinding(wasBound,isBound)
    }

    override fun resetInternal() {
        val wasBound = isBound
        super.resetInternal()
        notifyBinding(wasBound,isBound)
    }
    private fun notifyBinding(previousBound: Boolean, currentBound: Boolean) {
        if (previousBound && !currentBound) {
            onUnBind()
        } else if (!previousBound && currentBound) {
            onBind()
        }
    }
}