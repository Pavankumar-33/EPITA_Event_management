package fr.epita.android.myapplicationeventhandlerpart2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class userAdapter(clickListener: ClickListener): RecyclerView.Adapter<userAdapter.ViewHolder>() {

    private var userModelList : List<userModel> = arrayListOf()
    private lateinit var context : Context;
    private var clickListener: ClickListener = clickListener

    public fun setData(userModel: List<userModel>){

        this.userModelList = userModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context;
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_events,parent,false))
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var userModel = userModelList.get(position)
        var username  = userModel.username;
        var prefix = username.substring(0,1)

        holder.main_events_textview.text = username;

        holder.itemView.setOnClickListener{
            clickListener.clickedItem(userModel);
        }
    }



    override fun getItemCount(): Int {

        return userModelList.size
    }

    interface ClickListener{
        fun clickedItem(userModel: userModel);
    }


    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        var main_events_textview = itemView.findViewById<TextView>(R.id.main_events_textview)
    }


}