package com.example.sthiratestapi.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.sthiratestapi.R
import com.example.sthiratestapi.databinding.ItemJadwalBinding
import com.example.sthiratestapi.model.AttendancesItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class JadwalAdapter : RecyclerView.Adapter<JadwalAdapter.ViewHolder>(), Filterable {
    private val listData = arrayListOf<AttendancesItem?>()

    var filterList = ArrayList<AttendancesItem?>()

    init {
        filterList = listData
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: MutableList<AttendancesItem?>) {
        listData.clear()
        listData.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemJadwalBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: AttendancesItem) {
            with(binding) {

                val date = convertDate(data.date.toString(), "EEE,dd MMMM yyyy")
                val masuk = convertTime(data.attendanceList[0].masuk?.timestamp!!)
                val pulang = convertTime(data.attendanceList[0].pulang?.timestamp!!)
                var masuklembur = ""
                if (data.attendanceList.size > 1) {
                    masuklembur =
                        "Lembur : " + convertTime(data.attendanceList[1].masuk?.timestamp!!)
                }

                var pulanglembur = ""
                if (data.attendanceList.size > 1) {
                    pulanglembur = " to " + convertTime(data.attendanceList[1].pulang?.timestamp!!)
                    masuklembur =
                        "Lembur : " + convertTime(data.attendanceList[1].masuk?.timestamp!!)
                }

                txtMasuk.text = masuk.toString()
                txtPulang.text = " to " + pulang.toString()
                txtMasukLembur.text = masuklembur
                txtPulangLembur.text = pulanglembur
                txtNoteMasuk.text = data.attendanceList[0].masuk?.note!!
                txtNotePulang.text = data.attendanceList[0].pulang?.note!!
                txtDate.text = date
                txtJamker.text = times(masuk!!.toString(), pulang!!.toString()).toString() + " Jam"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_jadwal, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listData[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = listData.size

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTime(time: Long): LocalTime? {
        return Instant.ofEpochSecond(time)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: String, format: String): String {
        var formattedDate = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        try {
            val parseDate = sdf.parse(date)
            formattedDate = SimpleDateFormat(format).format(parseDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formattedDate
    }

    @SuppressLint("SimpleDateFormat")
    fun times(time1: String, time2: String): Long {
        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(time1)
        val date2 = format.parse(time2)
        return (date2.time - date1.time) / 3600000
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val search = p0.toString()
                filterList = if (search.isEmpty()) {
                    listData
                } else {
                    val results = ArrayList<AttendancesItem?>()
                    for (row in listData) {
                        if (row?.date?.toLowerCase(Locale.getDefault())
                                ?.contains(
                                    p0.toString().toLowerCase(Locale.getDefault())
                                ) == true
                        ) {
                            results.add(row)
                        }
                    }
                    results
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterList = p1?.values as ArrayList<AttendancesItem?>
                notifyDataSetChanged()
            }
        }
    }

}
