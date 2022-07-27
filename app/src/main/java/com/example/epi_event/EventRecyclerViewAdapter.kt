package com.example.epi_event

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.epi_event.databinding.ActivitySignUpBinding
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class EventRecyclerViewAdapter(
    val data: MutableList<EventObject>,
    val context: Activity,
    val onItemClickListener: View.OnClickListener,
) :
    RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivImage: ImageView = itemView.findViewById(R.id.event_item_iv_image)
        val tvEventName: TextView = itemView.findViewById(R.id.event_item_tv_event_name)
        val tvEventRegistration: TextView =
            itemView.findViewById(R.id.event_item_tv_event_registration)
        val tvEventTime: TextView = itemView.findViewById(R.id.event_item_tv_event_time)
        val tvEventDate: TextView = itemView.findViewById(R.id.event_item_tv_event_date)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val rowView: View =
            LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)

        //For click response of recycler view
        rowView.setOnClickListener(onItemClickListener)

        return ViewHolder(rowView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val event: EventObject = data[position]

        holder.tvEventName.text = event.eventName
        holder.tvEventRegistration.text = event.eventPreRegister
        holder.tvEventTime.text = event.eventTime
        holder.tvEventDate.text = event.eventDate
        holder.itemView.tag = position

        val refStorage =
            FirebaseStorage.getInstance("gs://epita-event-signup.appspot.com")
                .reference.child("event-image/${event.eventName}")
        val localFile = File.createTempFile(event.eventName, "jpg")

        refStorage.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            holder.ivImage.setImageBitmap(bitmap)


        }

    }


    override fun getItemCount(): Int {
        return data.size
        Log.d("Size", "" + data.size)
    }
}