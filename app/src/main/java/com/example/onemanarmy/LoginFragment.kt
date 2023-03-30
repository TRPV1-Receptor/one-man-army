package com.example.onemanarmy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth

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

    /**This function is called when the fragment is created
     * and its UI is being created. It inflates the fragment layout (fragment_login.xml)
     * and sets up the username, password, and firebaseAuth properties.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.log_username)
        password = view.findViewById(R.id.log_password)
        firebaseAuth = FirebaseAuth.getInstance()
        /**
         *  button (btn_register) that navigates to the RegisterFragment.
         */
        view.findViewById<Button>(R.id.btn_register).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(), false)

        }
        /**
         *  "Test" button (test) that starts an activity (OwnerDashboard).
         */
        val btnopen = view.findViewById<Button>(R.id.test)
        btnopen.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, Availibility::class.java))
                finish()
            }
        }

        /**
         *  "Login" button (btn_login) that calls the validateForm function.
         */
        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            validateForm()
        }
        return view
    }


    /**
     * function that checks if the username and password fields are empty or not.
     *  it displays an error message. If not, it calls the firebaseSignIn function
     *   to sign in the user with Firebase Authentication.
     */
    private fun validateForm(){
        val icon = AppCompatResources.getDrawable(requireContext(),
            R.drawable.errorsymbol)

        icon?.setBounds(0,0, icon.intrinsicWidth,icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.setError("Please Enter Username",icon)
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.setError("Please Enter Password",icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() ->
            {
                if(username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    firebaseSignIn()
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context,"Please use email format", Toast.LENGTH_SHORT).show()
                }

                    //The commented out lines are causing the text in the username box to
                    //disappear after pressing login  -Elias
                  //else{
                    //username.setError("Please Enter Valid Email Id",icon)
                }
            }
        }

    /**
     *  function that signs in the user with Firebase Authentication using
     *   the FirebaseAuth class. If the sign-in is successful, it starts
     *   the OwnerDashboard activity. Otherwise, it displays an error message.
     */
    private fun firebaseSignIn() {
        firebaseAuth.signInWithEmailAndPassword(username.text.toString(), password.text.toString()).addOnCompleteListener{
            if (it.isSuccessful) {
                val intent = Intent(context, OwnerDashboard::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    }
    }




//}