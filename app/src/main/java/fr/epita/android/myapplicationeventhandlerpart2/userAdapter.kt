package fr.epita.android.myapplicationeventhandlerpart2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class userAdapter(clickListener: ClickListener): RecyclerView.Adapter<userAdapter.ViewHolder>(), Filterable {

    private var userModelList : List<userModel> = arrayListOf()
    private var userModelListFiltered : List<userModel> = arrayListOf()
    private lateinit var context : Context;
    private var clickListener: ClickListener = clickListener

    public fun setData(userModel: List<userModel>){

        this.userModelList = userModel
        this.userModelListFiltered = userModel
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        context = parent.context;
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_events,parent,false))
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var userModel = userModelList[position]
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

    override fun getFilter(): Filter {

        var filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                var filterResults = FilterResults();

                if (p0 == null || p0.isEmpty()){
                    filterResults.values = userModelListFiltered
                    filterResults.count = userModelListFiltered.size
                }
                else{
                    var searchChar = p0.toString().toLowerCase();

                    var filterResults = ArrayList<userModel>()

                    for (um in userModelListFiltered){
                        if (um.username.toLowerCase().contains(searchChar)){
                            filterResults.add(um)
                        }
                    }

                    filterResults.values = filterResults
                    filterResults.count = filterResults.size

                }

                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                userModelList = p1!!.values as List<userModel>
                notifyDataSetChanged()
            }

        }

        return filter;
    }


}