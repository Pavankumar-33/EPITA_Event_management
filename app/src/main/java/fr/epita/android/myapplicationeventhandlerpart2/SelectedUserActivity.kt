package fr.epita.android.myapplicationeventhandlerpart2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class SelectedUserActivity : AppCompatActivity() {

    private lateinit var selected_user : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_user)
        initData();
    }

    private fun initData(){
        selected_user = findViewById(R.id.selected_user)
        getData();
    }

    private fun getData(){
        var intent = intent.extras

        var username = intent!!.getString("username")

        selected_user.text = username;
    }

}