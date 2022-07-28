package com.example.epi_event

import android.app.Activity
import android.graphics.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class EventRecyclerViewAdapter(
    var data: MutableList<EventObject>,
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
        val tvEventLocation: TextView = itemView.findViewById(R.id.event_item_tv_event_location)

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
        holder.tvEventLocation.text = event.eventLocation

        //Get Image

//        getImage(event, holder, position)

    }

    override fun getItemCount(): Int {
        return data.size
        Log.d("Size", "" + data.size)
    }

    private fun getImage(event: EventObject, holder: ViewHolder, position: Int) {
        val refStorage =
            FirebaseStorage.getInstance("gs://epita-event-signup.appspot.com")
                .reference.child("event-image/${event.eventName}")
        val localFile = File.createTempFile(event.eventName, "jpg")

        refStorage.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            val imageRounded =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig())

//            holder.ivImage.setImageBitmap(bitmap)
            val canvas = Canvas(imageRounded)
            val mpaint = Paint()
            mpaint.setAntiAlias(true)
            mpaint.setShader(BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
            canvas.drawRoundRect(RectF(0F, 0F, bitmap.getWidth().toFloat(),
                bitmap.getHeight().toFloat()),
                400F,
                400F,
                mpaint) // Round Image Corner 100 100 100 100

            holder.ivImage.setImageBitmap(imageRounded)


        }
    }

}