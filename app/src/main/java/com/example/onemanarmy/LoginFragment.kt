package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private  lateinit var userType:String




    /**This function is called when the fragment is created
     * and its UI is being created. It inflates the fragment layout (fragment_login.xml)
     * and sets up the username, password, and firebaseAuth properties.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.login_fragment, container, false)

        username = view.findViewById(R.id.reg_email)
        password = view.findViewById(R.id.reg_password)
        firebaseAuth = FirebaseAuth.getInstance()


        /**
         *  button (btn_register) that navigates to the RegisterFragment.
         */
        view.findViewById<TextView>(R.id.btn_register).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(), false)

        }

        view.findViewById<Button>(R.id.test).setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this,SearchActivity::class.java))
            }

        }

        /**
         *  "Login" button (btn_login) that calls the validateForm function.
         */
        view.findViewById<Button>(R.id.btn_login).setOnClickListener {

            var query:Query = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("userName")
                .equalTo(username.text.toString())

            query.addListenerForSingleValueEvent(userListener)


        }
        return view
    }


    /**
     * function that checks if the username and password fields are empty or not.
     *  it displays an error message. If not, it calls the firebaseSignIn function
     *   to sign in the user with Firebase Authentication.
     */
    private fun validateForm(){

        when{
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.error = "Please Enter Email"
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.error = "Please Enter Password"
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() ->
            {
                if(username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if (userType == "client"){
                        clientSignIn()
                    }else{
                        ownerSignIn()
                    }
                }
                else{
                    Toast.makeText(context,"Please use email format", Toast.LENGTH_SHORT).show()
                }
                }
            }
        }

    /**
     *  function that signs in the user with Firebase Authentication using
     *   the FirebaseAuth class. If the sign-in is successful, it starts
     *   the OwnerDashboard activity. Otherwise, it displays an error message.
     */
    private fun ownerSignIn() {
        firebaseAuth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener{
            if (it.isSuccessful) {
                val intent = Intent(context, OwnerDashboard::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun clientSignIn() {
        firebaseAuth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener{
            if (it.isSuccessful) {
                val intent = Intent(context, ClientDashboard::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Login Failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val userListener = object:ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()){
                snapshot.child("Users")
                snapshot.children.forEach { child ->
                    userType = child.child("userType").value.toString()
                    validateForm()
                }
            }
        }
        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }

    }
}




//}