package de.syntax.androidabschluss.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.syntax.androidabschluss.data.model.open.response.ForecastResponseApi
import de.syntax.androidabschluss.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class ForeCastAdapter : RecyclerView.Adapter<ForeCastAdapter.ViewHolder>() {
    private lateinit var binding: ForecastViewholderBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ForecastViewholderBinding.inflate(inflater,parent,false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ForeCastAdapter.ViewHolder, position: Int) {
        val binding = ForecastViewholderBinding.bind(holder.itemView)

        // Das Datum im String-Format aus der aktuellen Liste zum dateobjekt umwandeln
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        //Instance von calender erstellen
        val calendar = Calendar.getInstance()
        calendar.time = date // setzt das umgewandelte date im Kalender


        // wochentage in kurzform ermitteln
        val dayOfWeekName = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Su"
            2 -> "Mo"
            3 -> "Tu"
            4 -> "We"
            5 -> "Th"
            6 -> "Fr"
            7 -> "Sa"
            else -> "--"
        }

        binding.nameDayTxt.text = dayOfWeekName

        // stunden im 24h format setzten
        val hour24 = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)
        // hier setzt man die stunden in die textview im stunden inuten format
        binding.hourTxt.text = String.format("%02d:%02d", hour24, minutes) // formatiert Zeit

        //temperatur abrufen
        binding.tempTxt.text = differ.currentList[position].main?.temp?.let { Math.round(it) }.toString() + "°"

        //name des wettersymbols bestimmen und den resourcen zuordnen
        val icon=when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d", "01n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "03d", "03n" -> "cloudy_sunny"
            "04d", "04n" -> "cloudy"
            "09d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"
        }
        //hier wird die resource id anhand von icon ermittel
        val drwableResourceId: Int = binding.root.resources.getIdentifier(
            icon,
            "drawable", binding.root.context.packageName
        )
        //hier wird das bild dann reingeladen
        Glide.with(binding.root.context)
            .load(drwableResourceId)
            .into(binding.pic)

        }


    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    // anzahl der elemente zurückgeben
    override fun getItemCount() = differ.currentList.size

    //callback um die lsite immer zu aktuallisieren
    private val differCallback = object : DiffUtil.ItemCallback<ForecastResponseApi.data>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(
            oldItem: ForecastResponseApi.data,
            newItem: ForecastResponseApi.data
        ): Boolean {
            return oldItem == newItem
        }

    }
    // asynchrones dient zur optimierung in der Recyclerview
    val differ = AsyncListDiffer(this, differCallback)

}