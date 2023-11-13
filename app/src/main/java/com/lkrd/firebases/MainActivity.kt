package com.lkrd.firebases

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lkrd.firebases.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    val database:FirebaseDatabase=FirebaseDatabase.getInstance()
    val myReference:DatabaseReference=database.reference.child("MyUsers")

    val userList=ArrayList<Users>()
    lateinit var usersAdapter: UserAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {
            val intent=Intent(this,AddUserActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                TODO("Not yet implemented")
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
             val id = usersAdapter.getUserId(viewHolder.adapterPosition)
                myReference.child(id).removeValue()

                Toast.makeText(applicationContext,"The user was deleted",Toast.LENGTH_SHORT).show()

            }

        }).attachToRecyclerView(mainBinding.recycleView)

        retrievDataFromDatabase()
    }
    fun retrievDataFromDatabase(){
        myReference.addValueEventListener(object: ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(Users::class.java)
                    if (user != null) {
                        println("userId: ${user.userId}")
                        println("userName: ${user.userName}")
                        println("userAge: ${user.userAge}")
                        println("***********************")

                        userList.add(user)

                    }
                    usersAdapter = UserAdapter(this@MainActivity, userList)

                    mainBinding.recycleView.layoutManager = LinearLayoutManager(this@MainActivity)

                    mainBinding.recycleView.adapter=usersAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_all,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.deleteAll){
            showDialoagMessage()
        }

        return super.onOptionsItemSelected(item)
    }
    fun showDialoagMessage(){

        val dialogMessage= AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Users")
        dialogMessage.setTitle("if click Yes,all users will be deleted," + "i" +
                "if you want to delete a specific user, you can swipe the item you want to delete right or left")
        dialogMessage.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        })
        dialogMessage.setPositiveButton("yes", DialogInterface.OnClickListener { dialogInterface, i ->
            myReference.removeValue().addOnCompleteListener { task ->
                if (task.isSuccessful){
                    usersAdapter.notifyDataSetChanged()

                    Toast.makeText(applicationContext,"All user were deleted",Toast.LENGTH_SHORT).show()
                }
            }
        })
        dialogMessage.create().show()
    }
}