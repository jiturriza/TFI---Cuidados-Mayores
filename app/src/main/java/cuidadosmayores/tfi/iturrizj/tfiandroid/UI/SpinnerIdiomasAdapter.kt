package cuidadosmayores.tfi.iturrizj.tfiandroid.UI

import android.app.Activity
import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.widget.SpinnerAdapter
import cuidadosmayores.tfi.iturrizj.tfiandroid.BE.Idioma
import cuidadosmayores.tfi.iturrizj.tfiandroid.R
import kotlinx.android.synthetic.main.item_spinner_idioma.view.*

class SpinnerIdiomasAdapter(pIdiomas:List<Idioma>, pActivity: Activity) : SpinnerAdapter {

    val idiomas : List<Idioma>
    val activity : Activity

    init {
        idiomas = pIdiomas
        activity = pActivity
    }

    override fun isEmpty(): Boolean {
        return idiomas.isEmpty()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var result : View
        result = activity.layoutInflater.inflate(R.layout.item_spinner_idioma, null)
        result.nombre.text = idiomas[position].Nombre
        return result
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    override fun getItem(position: Int): Any {
        return idiomas[position]
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var result : View
        result = activity.layoutInflater.inflate(R.layout.item_spinner_idioma, null)
        result.nombre.text = idiomas[position].Nombre
        return result
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getCount(): Int {
        return idiomas.size
    }
}