package com.lkrd.firebases

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lkrd.firebases.databinding.ActivityAddUserBinding
import com.lkrd.firebases.databinding.ActivityMainBinding

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUserBinding

    val database:FirebaseDatabase=FirebaseDatabase.getInstance()
    val myReference:DatabaseReference=database.reference.child("MyUsers")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addUserBinding= ActivityAddUserBinding.inflate(layoutInflater)
        val view=addUserBinding.root
        setContentView(view)

        supportActionBar?.title="Add User"

        addUserBinding.buttonAddUser.setOnClickListener {
            addUserToDatabase()
        }
    }
    fun addUserToDatabase(){
        val name:String=addUserBinding.editTextName.text.toString()
        val age:Int=addUserBinding.editTextAge.text.toString().toInt()
        val email:String=addUserBinding.editTextEmail.text.toString()

        val id:String=myReference.push().key.toString()

        val user=Users(id,name,age,email)
        myReference.child(id).setValue(user).addOnCompleteListener { task ->

            if (task.isSuccessful){

                Toast.makeText(this,
                    "The new user has added to the database",
                    Toast.LENGTH_LONG).show()

                finish()
            }else
            {
                Toast.makeText(this,
                task.exception.toString(),
                Toast.LENGTH_LONG).show()
            }
        }
    }
}