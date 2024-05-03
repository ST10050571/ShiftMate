package com.example.shiftmateOPSC

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity() {
    private lateinit var focusButton: Button
    private lateinit var addTaskButton: Button
    private lateinit var clockInButton: Button
    private lateinit var myGoalsButton: Button
    private lateinit var viewTotHrsButton: Button
    private lateinit var leaderBoardButton: Button
    private lateinit var profileButton: Button
    private lateinit var chatButton: Button
    private lateinit var categoriesLayout: LinearLayout
    private lateinit var mAuth: FirebaseAuth
    private lateinit var timeLogRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        mAuth = FirebaseAuth.getInstance()
        timeLogRef = FirebaseDatabase.getInstance().getReference("TimeLog")

        categoriesLayout = findViewById(R.id.linear_layout_tasks)

        myGoalsButton = findViewById(R.id.myGoalsButton)
        viewTotHrsButton = findViewById(R.id.viewTotHrsButton)
        focusButton = findViewById(R.id.focusButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        clockInButton = findViewById(R.id.clockInBut)
        leaderBoardButton = findViewById(R.id.leaderBoardButton)
        profileButton = findViewById(R.id.profileButton)
        chatButton = findViewById(R.id.chatButton)

        // Set click listeners
        myGoalsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, MinMaxGoalAct::class.java)
            startActivity(intent)
        }

        focusButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, FocusActivity::class.java)
            startActivity(intent)
        }

        viewTotHrsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, com.example.shiftmateOPSC.View::class.java)
            startActivity(intent)
        }

        addTaskButton.setOnClickListener {
            // Start the AddTask activity
            val intent = Intent(this@DashboardActivity,AddTask::class.java)
            startActivity(intent)
        }

        clockInButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, ClockInActivity::class.java)
            startActivity(intent)
        }

        leaderBoardButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, LeaderBoard::class.java)
            startActivity(intent)
        }

        profileButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity,Profile::class.java)
            startActivity(intent)
        }

        chatButton.setOnClickListener{
            val intent = Intent(this@DashboardActivity, Chat::class.java)
            startActivity(intent)
        }

        fetchUserData()
    }

    private fun fetchUserData() {
        val currentUserID = mAuth.currentUser?.uid
        currentUserID?.let { uid ->
            timeLogRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    categoriesLayout.removeAllViews() // Clear previous views

                    for (data in snapshot.children) {
                        val categoryName = data.child("category").getValue(String::class.java) ?: ""
                        val date = data.child("date").getValue(String::class.java) ?: ""
                        val description = data.child("description").getValue(String::class.java) ?: ""
                        val endTime = data.child("endTime").getValue(String::class.java) ?: ""
                        val startTime = data.child("startTime").getValue(String::class.java) ?: ""


                        if (categoryName.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty()
                            && startTime.isNotEmpty() && endTime.isNotEmpty()) {
                            val formattedText = "Category: $categoryName\nDate: $date\nDescription: $description\nStart Time: $startTime\nEnd Time: $endTime\n\n"

                            // Create a new TextView programmatically
                            val textView = android.widget.TextView(this@DashboardActivity)
                            textView.text = formattedText
                            textView.textSize = 16f
                            val layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            layoutParams.setMargins(0, 0, 0, 16)
                            textView.layoutParams = layoutParams

                            // Add the TextView to the categoriesLayout
                            categoriesLayout.addView(textView)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }
}
