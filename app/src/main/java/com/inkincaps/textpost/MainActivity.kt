package com.inkincaps.textpost

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inkincaps.textpost.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), FontStyleAdapter.Callback, ColorAdapter.Callback {

    val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.apply {

            btnPost.setOnClickListener {
                binding.ivOutPut.setImageBitmap(binding.etPost.loadBitmapFromView())
            }

            fontStyleRv.also {
                it.layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                it.adapter = FontStyleAdapter(Font.fontList, this@MainActivity)
            }

            colorRv.also {
                it.layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                it.adapter = ColorAdapter(Color.colorList, this@MainActivity)
            }

        }

    }

    override fun onFontSelection(font: Font) {
        binding.etPost.typeface = resources.getFont(font.id)
    }

    override fun onColorSelection(color: Color) {
        binding.etPost.setBackgroundColor(resources.getColor(color.backGroundColor))
        binding.etPost.setTextColor(color.fontColor)
//        Toast.makeText(this, "$color", Toast.LENGTH_SHORT).show()
    }
}

fun View.loadBitmapFromView(): Bitmap {
    if (measuredHeight <= 0) {
        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val b = Bitmap.createBitmap(
            measuredWidth,
            measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        layout(0, 0, measuredWidth, measuredHeight)
        draw(c)
        return b
    }
    val b = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
    val c = Canvas(b)
    layout(left, top, right, bottom)
    draw(c)
    return b
}

data class Font(@IdRes val id: Int) {
    companion object {
        val fontList = listOf(
            Font(R.font.aka_dora),
            Font(R.font.arial),
            Font(R.font.eutemia),
            Font(R.font.greennpil),
            Font(R.font.grinched),
            Font(R.font.helvetica),
            Font(R.font.libertango),
            Font(R.font.metal_macabre),
            Font(R.font.parry_hotter),
            Font(R.font.scriptin),
        )
    }
}

data class Color(val backGroundColor: Int, val fontColor: Int) {
    companion object {
        val colorList = listOf(
            Color(R.color.font_color_one, android.graphics.Color.WHITE),
            Color(R.color.font_color_two, android.graphics.Color.WHITE),
            Color(R.color.font_color_six, android.graphics.Color.WHITE),
            Color(R.color.font_color_three, android.graphics.Color.BLACK),
            Color(R.color.font_color_four, android.graphics.Color.BLACK),
            Color(R.color.font_color_five, android.graphics.Color.BLACK)
        )
    }
}

class FontStyleAdapter(val fontList: List<Font>, val callBack: Callback) : RecyclerView.Adapter<FontStyleAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textHolder: TextView = v.findViewById(R.id.tvText)
        val checkView: ImageView = v.findViewById(R.id.ivFont)
    }

    private var attachedCandidateList = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.font_view_holder,
                parent,
                false
            )
        )
    }

    private var selected = -1

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        attachedCandidateList.add(holder)
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        attachedCandidateList.remove(holder)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkView.beVisibleIf(position == selected)
        holder.textHolder.typeface = holder.textHolder.context.resources.getFont(fontList[position].id)
        holder.apply {
            holder.itemView.setOnClickListener {
                holder.checkView
                attachedCandidateList.forEach {
                    if(it != holder){
                        it.checkView.beInVisible()
                    }
                }
                holder.checkView.beVisible()
                selected = position
                callBack.onFontSelection(fontList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return fontList.size
    }

    interface Callback {
        fun onFontSelection(font: Font)
    }
}

class ColorAdapter(val colorList: List<Color>, val callBack: Callback) : RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val textHolder:ImageView = v.findViewById(R.id.ivColor)
        val checkView:ImageView = v.findViewById(R.id.ivCheck)
        val countView:TextView = v.findViewById(R.id.tvCount)
    }

    private var attachedCandidateList = mutableListOf<ViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.color_view_holder,
                parent,
                false
            )
        )
    }

    private var selected = -1

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        attachedCandidateList.add(holder)
        attachedCandidateList.forEach {
            if(it.adapterPosition == selected) {
                it.checkView.beVisible()
            }else {
                it.checkView.beInVisible()
            }
        }
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        attachedCandidateList.remove(holder)
        super.onViewDetachedFromWindow(holder)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkView.beVisibleIf(position == selected)
        holder.textHolder.setColorFilter(holder.textHolder.context.resources.getColor(colorList[position].backGroundColor))
        holder.apply {
            holder.itemView.setOnClickListener {
                attachedCandidateList.forEach {
                    if(it != holder){
                        it.checkView.beInVisible()
                    }
                }
                holder.checkView.beVisible()
                selected = position
                callBack.onColorSelection(colorList[position])
            }
        }
    }
    override fun getItemCount(): Int {
        return colorList.size
    }

    interface Callback {
        fun onColorSelection(color: Color)
    }
}