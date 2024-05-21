package com.amar.photo.custom_views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.amar.photo.R

class CustomAppBar  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private var ivTopImg: AppCompatImageView
    private var ivBack: AppCompatImageView

    init {
        inflate(context, R.layout.layout_header, this)

        ivTopImg = findViewById(R.id.iv_top_img)
        ivBack = findViewById(R.id.iv_back)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomAppBar,
            0, 0
        ).apply {
            try {
                val topImageResId = getResourceId(R.styleable.CustomAppBar_topImageSrc, 0)
                if (topImageResId != 0) {
                    ivTopImg.setImageResource(topImageResId)
                }

                val backImageResId = getResourceId(R.styleable.CustomAppBar_backImageSrc, 0)
                if (backImageResId != 0) {
                    ivBack.setImageResource(backImageResId)
                }
            } finally {
                recycle()
            }
        }
    }

    fun setTopImage(resourceId: Int) {
        ivTopImg.setImageResource(resourceId)
    }

    fun setBackImage(resourceId: Int) {
        ivBack.setImageResource(resourceId)
    }

    fun setOnBackIconClickListener(listener: OnClickListener){
        ivBack.setOnClickListener(listener)
    }

}