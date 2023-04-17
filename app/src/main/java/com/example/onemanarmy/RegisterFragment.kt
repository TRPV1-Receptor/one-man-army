package com.example.onemanarmy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Api.Client
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var cnfPassword: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var custType: RadioGroup
    private lateinit var busName: EditText
    private lateinit var busAddress: EditText
    private lateinit var busPhone: EditText
    private lateinit var busEmail: EditText
    private lateinit var service: Spinner
    private lateinit var items: Array<String>
    private lateinit var radioGroup: RadioGroup

    private lateinit var auth: FirebaseAuth

    //DB variable
    private lateinit var dbRef: DatabaseReference

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    /**
     * It inflates the layout for the fragment and initializes the necessary views and variables
     *  such as username, password, cnfPassword, and auth.
     *  It also sets click listeners for the login and register buttons
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register_1, container, false)
        val view1 = inflater.inflate(R.layout.fragment_register_2, container, false)

        radioGroup = view.findViewById(R.id.cust_type)

        username = view.findViewById(R.id.reg_email)
        password = view.findViewById(R.id.reg_password)
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
        custType = view.findViewById(R.id.cust_type)
        cnfPassword = view.findViewById(R.id.reg_cnf_password)

        busName = view1.findViewById(R.id.bus_name)
        busAddress = view1.findViewById(R.id.bus_address)
        busEmail = view1.findViewById(R.id.bus_email)
        busPhone = view1.findViewById(R.id.bus_number)

        service = view1.findViewById(R.id.service)

        items = resources.getStringArray(R.array.services)
        val buttonNext = view.findViewById<Button>(R.id.btn_next_1)
        var userType = "owner"

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.client -> {
                    userType = "client"
                    buttonNext.text = "Finish"
                }
                R.id.owner -> {
                    userType = "owner"
                    buttonNext.text = "Next"
                }
            }
        }

        val spinnerAdapter = object : ArrayAdapter<String>(this.requireContext(),android.R.layout.simple_spinner_item,items){
            override fun isEnabled(position: Int): Boolean {
                return position!=0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0){
                    view.setTextColor(Color.WHITE)
                }else{

                }

                return view
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        service.adapter = spinnerAdapter

        service.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                postion: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(postion).toString()
                if (value == items[0]){
                    (view as TextView).setTextColor(Color.WHITE)
                }else{
                    (view as TextView).setTextColor(Color.WHITE)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
            // Authentication initialize
            auth = FirebaseAuth.getInstance()

            //Initialize db reference
            dbRef = FirebaseDatabase.getInstance().getReference("Users")


            view.findViewById<TextView>(R.id.btn_prev).setOnClickListener {
                val navRegister = activity as FragmentNavigation
                navRegister.navigateFrag(LoginFragment(), false)

            }
            view.findViewById<Button>(R.id.btn_next_1).setOnClickListener {
                if(userType == "owner"){
                    if(validateEmptyForm()){
                        container?.removeAllViews()
                        container?.addView(view1)

                    }

                }else{
                    requireActivity().run {
                        if (validateEmptyForm()){
                            startActivity(Intent(this, ClientDashboard::class.java))
                            finish()
                        }

                    }

                }

            }

            view1.findViewById<Button>(R.id.btn_prev_1).setOnClickListener {
                container?.removeAllViews()
                container?.addView(view)
            }

            view1.findViewById<Button>(R.id.btn_next_2).setOnClickListener {
                requireActivity().run {
                    firebaseSignUp()

                }
            }


            return view

        }




        /**
         * allowing users to sign up using their email address and password.
         */
        private fun firebaseSignUp() {
            val user = hashMapOf(
                "username" to username.text.toString().trim(),
                "password" to password.text.toString().trim(),
                "cnfPassword" to cnfPassword.text.toString().trim()
            )

            auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        bundle.putSerializable("user", user)
                        val navRegister = activity as FragmentNavigation
                        navRegister.navigateFrag(LoginFragment(), false)
                    } else {
                        Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }



        }

        /**
         *  logic to ensure that the user enters valid input values before registering their account
         */
        private fun validateEmptyForm(): Boolean {
            when {
                TextUtils.isEmpty(firstName.text.toString().trim()) -> {
                    firstName.error = "Please Enter First Name"
                    return false
                }
                TextUtils.isEmpty(lastName.text.toString().trim()) -> {
                    lastName.error = "Please Enter Last Name"
                    return false
                }
                TextUtils.isEmpty(username.text.toString().trim()) -> {
                    username.error = "Please Enter Email"
                    return false

                }
                TextUtils.isEmpty(password.text.toString().trim()) -> {
                    password.error = "Please Enter Password"
                    return false

                }
                TextUtils.isEmpty(cnfPassword.text.toString().trim()) -> {
                    cnfPassword.error = "Please Enter Password Again"
                    return false
                }


                username.text.toString().isNotEmpty()
                        && password.text.toString().isNotEmpty()
                        && cnfPassword.text.toString().isNotEmpty()
                -> {
                    if (username.text.toString()
                            .matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
                    ) {
                        if (password.text.toString().length >= 5) {
                            return if (password.text.toString() == cnfPassword.text.toString()) {
                                firebaseSignUp()
                                saveUserData()
                                Toast.makeText(
                                    context,
                                    "Registration Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                true
                            } else {
                                password.error = "Password didn't match"
                                false
                            }
                        } else {
                            password.error = "Please Enter at least 5 characters"
                            return false
                        }
                    }else{
                       username.error = "Please Enter Valid Email Id"
                        return false
                    }
                }
            }
            return true
        }

        private fun saveUserData() {

            //getting values from user input
            val userName = username.text.toString()

            val userId = dbRef.push().key!!

            val user = UserModel(userId, userName)

            dbRef.child(userId).setValue(user)
                .addOnCompleteListener {
                    Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }

        }
    }



