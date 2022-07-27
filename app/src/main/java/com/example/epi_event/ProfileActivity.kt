package com.example.epi_event

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.epi_event.authentication_login_signup.ChangePasswordActivity
import com.example.epi_event.authentication_login_signup.LoginActivity
import com.example.epi_event.create_event.CreateEventActivity
import com.example.epi_event.qr_code.QrScan
import com.example.epi_event.user_profile.UserProfile
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    //ViewBinding

    private lateinit var navigationView: NavigationView
    private lateinit var tvEmailVerification: TextView
    private lateinit var tvEmailResend: TextView
    private lateinit var tvEmail: TextView
    private lateinit var getUserEmail: String

    //Admin lists
    private var adminList: MutableList<String> = mutableListOf()
    private lateinit var userEmail: String


    //ActionBar
    private lateinit var actionBar: ActionBar
    private lateinit var drawer: DrawerLayout


    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    //Database Reference
    private lateinit var databaseReference: DatabaseReference

    //Recycler View
    private lateinit var eventRecyclerView: RecyclerView

    //Array List
    private lateinit var eventsArrayList: ArrayList<EventObject>
    private lateinit var tempEventArrayList: ArrayList<EventObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Adding admin

        addAdmin()

        //Get admin email
        userEmail = intent.getStringExtra("userEmailPass").toString()
        Log.d("getEmail", userEmail)


        bindActivity()

//        setSupportActionBar(findViewById(R.id.app_bar_main_toolbar))


        //Check if user or admin

        if (adminList.contains(userEmail)) {
            navigationView.menu.clear()
            navigationView.inflateMenu(R.menu.menu_sidebar_admin)
//            menuInflater.inflate(R.menu.menu_sidebar_admin,menu)

        } else {
            navigationView.menu.clear()
            navigationView.inflateMenu(R.menu.menu_sidebar_user)
//            menuInflater.inflate(R.menu.menu_sidebar_user, menu)

        }

    }

    private fun addAdmin() {
        //adding admin

        adminList.add("gam3ame@gmail.com")
        adminList.add("anshulbrana@gmail.com")
    }


    private fun bindActivity() {

        //find id
        tvEmailVerification = findViewById(R.id.activity_profile_tv_email_not_verified)
        tvEmailResend = findViewById(R.id.activity_profile_tv_click_to_verify)
        tvEmail = findViewById(R.id.activity_profile_tv_email)
//        displayUserName = findViewById(R.id.userNameDisplay)


//Navigation view
        val toolbar: Toolbar = findViewById(R.id.app_bar_main_toolbar)
        setSupportActionBar(findViewById(R.id.app_bar_main_toolbar))

        navigationView = findViewById(R.id.nav_view)


        //Put color on navigation menu
        navigationView.setItemIconTintList(null);

        drawer = findViewById(R.id.drawer_layout)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)


        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //put email on header

        var headerView: View = navigationView.getHeaderView(0)
        var navUserName: TextView = headerView.findViewById(R.id.userNameDisplay)
        navUserName.setText(getUserEmail)


        //For email verification

        confirmEmailVerification()

        //For getting event data From Firebase

        eventRecyclerView = findViewById(R.id.activity_show_all_events_rv_events)
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        eventRecyclerView.hasFixedSize()

        eventsArrayList = arrayListOf<EventObject>()
        tempEventArrayList = arrayListOf<EventObject>()
        getEventData()


    }

    private fun getEventData() {
        databaseReference =
            FirebaseDatabase.getInstance("https://epita-event-signup-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Events")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {

                        val event = userSnapshot.getValue(EventObject::class.java)
                        eventsArrayList.add(event!!)
                    }

                    tempEventArrayList.addAll(eventsArrayList)

                    // For on click response on recycler view
                    val onItemClickListener = View.OnClickListener {
                        val position: Int = it.tag as Int
                        val clickedEvent = eventsArrayList[position]
                        val eventNameClicked = clickedEvent.eventName

                        Toast.makeText(this@ProfileActivity, "" + eventNameClicked,
                            Toast.LENGTH_SHORT).show()

                        val intent: Intent = Intent(this@ProfileActivity, EventDetail::class.java)
                        intent.putExtra("eventNamePass", eventNameClicked)
                        intent.putExtra("userNamePass", getUserEmail)
                        Log.d("testUsername", getUserEmail)
                        if (adminList.contains(userEmail)) {
                            intent.putExtra("canEdit", "true")
                        } else {
                            intent.putExtra("canEdit", "false")
                        }

                        startActivity(intent)

                    }



                    eventRecyclerView.adapter =
                        EventRecyclerViewAdapter(tempEventArrayList,
                            this@ProfileActivity,
                            onItemClickListener)


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    //Email verification

    private fun confirmEmailVerification() {
        val firebaseUserEmail = firebaseAuth.currentUser

        Log.d("userNameFirebase", "" + firebaseUserEmail)

        if (firebaseUserEmail != null) {
            if (firebaseUserEmail.isEmailVerified) {

                Log.d("insideCheck", "done")

                tvEmailVerification.visibility = View.GONE
                tvEmailResend.visibility = View.GONE

            } else {

                //Resend mail verification on click on link

                tvEmailVerification.setOnClickListener {
                    val firebaseUserEmail = firebaseAuth.currentUser

                    Log.d("checkVerification", firebaseUserEmail.toString())

                    firebaseUserEmail!!.sendEmailVerification().addOnSuccessListener {

                        Toast.makeText(this,
                            "Verification email has been send to your mail",
                            Toast.LENGTH_SHORT).show()
                        firebaseAuth.signOut()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()

                    }.addOnFailureListener { e ->
                        Toast.makeText(this,
                            "Email verification failed due to ${e.message}",
                            Toast.LENGTH_SHORT).show()

                    }

                }

            }
        }
    }

    private fun checkUser() {
        //If user is already logged in goto profile activity
        //get current user

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //User is already logged in
            getUserEmail = firebaseUser.email.toString()

            //set username
            tvEmail.text = getUserEmail

        } else {
            //user is null so not signed in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

//    For navigation

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        // Handle navigation view item clicks here.
        val id = item.itemId


        if (id == R.id.nav_user_profile) {
            //For user profile
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_user_sign_out) {
            //For signout
            signout()
        } else if (id == R.id.nav_user_change_password) {
            //For changing password
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_admin_change_password) {
            //For changing password
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_admin_profile) {
            //For user profile
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_admin_add_event) {
            //For adding event
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_admin_qr_code_scanner) {
            //For opening scanner
            val intent = Intent(this, QrScan::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_admin_sign_out) {
            signout()
        }

        val drawer: DrawerLayout = findViewById(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    //For signout

    private fun signout() {
        firebaseAuth.signOut()
        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu?.findItem(R.id.search_view)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempEventArrayList.clear()
                val searchText = newText!!.toLowerCase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    eventsArrayList.forEach {
                        if (it.eventName!!.toLowerCase(Locale.getDefault()).contains(searchText)) {
                            tempEventArrayList.add(it)
                        }
                    }
                    eventRecyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    tempEventArrayList.clear()
                    tempEventArrayList.addAll(eventsArrayList)
                    eventRecyclerView.adapter!!.notifyDataSetChanged()


                }
                return false
            }


        })
        return true
    }

    //Show dialogue box for exit

    override fun onBackPressed() {
        drawer = findViewById(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes,
                    DialogInterface.OnClickListener { arg0, arg1 -> super@ProfileActivity.onBackPressed() })
                .create().show()
        }
    }


}