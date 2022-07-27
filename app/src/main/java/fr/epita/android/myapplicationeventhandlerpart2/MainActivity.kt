package fr.epita.android.myapplicationeventhandlerpart2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), userAdapter.ClickListener {

    private lateinit var main_recyclerview : RecyclerView
    private lateinit var userAdapter: userAdapter;
//    private lateinit var toolbar: Toolbar;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData();
    }


    private fun initData(){
        main_recyclerview = findViewById(R.id.main_recyclerview)
        initRecyclerView();
    }

    private fun initRecyclerView(){
        main_recyclerview.layoutManager = LinearLayoutManager(this)
        main_recyclerview.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        userAdapter = userAdapter(this);
        main_recyclerview.adapter = userAdapter;
        showData();
    }

    private fun populateUser(): List<userModel>{

        var userList = ArrayList<userModel>()
        userList.add(userModel("Pavankumar"))
        userList.add(userModel("Djamal"))
        userList.add(userModel("Anshul"))
        userList.add(userModel("Fashan"))
        userList.add(userModel("Thoshith"))
        userList.add(userModel("Rajkumar"))
        userList.add(userModel("Jatin"))
        userList.add(userModel("Thomas"))
        userList.add(userModel("Pavankumar1"))
        userList.add(userModel("Djamal1"))
        userList.add(userModel("Anshul1"))
        userList.add(userModel("Fashan1"))
        userList.add(userModel("Thoshith1"))
        userList.add(userModel("Rajkumar1"))
        userList.add(userModel("Jatin1"))
        userList.add(userModel("Thomas1"))

        return userList;
    }

    private fun showData(){
        userAdapter.setData(populateUser())
    }

    override fun clickedItem(userModel: userModel) {
        startActivity(Intent(this,SelectedUserActivity::class.java).putExtra("username", userModel.username))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        var menuItem = menu!!.findItem(R.id.search_view)
        var searchView: SearchView = menuItem.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                userAdapter.filter.filter(newText)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}