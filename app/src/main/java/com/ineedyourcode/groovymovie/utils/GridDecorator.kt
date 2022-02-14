package com.ineedyourcode.groovymovie.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridDecorator(val mSpanCount: Int, val mItemSize: Float) :
    RecyclerView.ItemDecoration() {

    @Override
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % mSpanCount
        val parentWidth = parent.width
        val spacing = (parentWidth - (mItemSize * mSpanCount)).toInt() / (mSpanCount + 1)
        outRect.left = spacing - column * spacing / mSpanCount
        outRect.right = (column + 1) * spacing / mSpanCount

        if (parent.getChildLayoutPosition(view) < mSpanCount) {
            outRect.top = spacing / 3
        }
        outRect.bottom = spacing / 3
    }
}