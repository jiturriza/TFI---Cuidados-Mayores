package cuidadosmayores.tfi.iturrizj.tfiandroid

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cuidadosmayores.tfi.iturrizj.tfiandroid.UI.onItemClick

class SettingsItemAdapter(context: Context, items: List<SettingsItem>, listener: onItemClick) : RecyclerView.Adapter<SettingsItemAdapter.SettingsItemViewHolder>() {

    val items = items
    val context = context
    val listener = listener


    override fun onBindViewHolder(holder: SettingsItemViewHolder, position: Int) {
        val item: SettingsItem = items[position]
        if (holder != null) {
            holder.descTextview.text = item.descripcion
            holder.titleTextview.text = item.title
            holder.imageView.setImageResource(item.imgResource as Int)
            holder.itemView.setOnClickListener({ _ -> listener.onClick(position) })
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingsItemViewHolder {
        return SettingsItemViewHolder(LayoutInflater.from(context).inflate(R.layout.settings_item, parent, false))
    }


    inner class SettingsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var titleTextview: TextView
        internal var descTextview: TextView
        internal var imageView: ImageView

        init {
            this.titleTextview = itemView.findViewById(R.id.title)
            this.descTextview = itemView.findViewById(R.id.description)
            this.imageView = itemView.findViewById(R.id.imageView)
        }
    }

    class SettingsItem {

        constructor(title: String, descripcion: String, imgResource: Int?) {
            this.title = title
            this.descripcion = descripcion
            this.imgResource = imgResource
        }

        lateinit var title: String
        lateinit var descripcion: String
        var imgResource: Int? = 0

    }

}