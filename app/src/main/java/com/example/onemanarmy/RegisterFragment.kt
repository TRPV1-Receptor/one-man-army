package com.example.onemanarmy

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


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
    private lateinit var busName: EditText
    private lateinit var busAddress: EditText
    private lateinit var busPhone: EditText
    private lateinit var busEmail: EditText
    private lateinit var service: Spinner
    private lateinit var items: Array<String>
    private lateinit var userType: RadioGroup
    private var flag = true
    //private lateinit var userType: String


    //DB variable
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var usrType: String

    //private lateinit var binding : FragmentRegister1Binding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

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

        username = view.findViewById(R.id.reg_email)
        password = view.findViewById(R.id.reg_password)
        firstName = view.findViewById(R.id.first_name)
        lastName = view.findViewById(R.id.last_name)
        cnfPassword = view.findViewById(R.id.reg_cnf_password)
        userType = view.findViewById(R.id.cust_type)

        busName = view1.findViewById(R.id.bus_name)
        busAddress = view1.findViewById(R.id.bus_address)
        busEmail = view1.findViewById(R.id.bus_email)
        busPhone = view1.findViewById(R.id.bus_number)
        service = view1.findViewById(R.id.service)

        items = resources.getStringArray(R.array.services)

        val buttonNext = view.findViewById<Button>(R.id.btn_next_1)

        //"Already have an account"? button on Register Screen
        view.findViewById<TextView>(R.id.btn_prev).setOnClickListener {
            val navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LoginFragment(), false)
        }
        //Previous button on business register
        view1.findViewById<TextView>(R.id.bus_reg_back).setOnClickListener {
            val navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LoginFragment(), false)
        }
        //next button on business register
        view1.findViewById<Button>(R.id.btn_next_2).setOnClickListener {
            if (validateBusinessRegisterForm()){
                requireActivity().run {
                    firebaseSignUp()
                    saveOwnerData()
                    Toast.makeText(context,"Hello There", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,ProfileSetupActivity::class.java))

                }
            }

        }

        //Register Next button, directs to which page by usertype
        view.findViewById<Button>(R.id.btn_next_1).setOnClickListener {
            if(usrType == "owner"){
                if(validateRegisterForm()){
                    searchUsername(username.text.toString()) { exists ->
                        if (exists) {
                            username.error = "Username already in use"
                        }
                        else{
                            container?.removeAllViews()
                            container?.addView(view1)
                        }
                    }


                }
            }else{
                requireActivity().run {
                    if (validateRegisterForm()){
                        firebaseSignUp()
                        saveCustomerData()
                        startActivity(Intent(this, ClientDashboard::class.java))
                        finish()
                    }
                }
            }
        }

        //Initializing base user type as owner
        usrType = "owner"

        this.userType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.client -> {
                    usrType = "client"
                    buttonNext.text = "Finish"
                }
                R.id.owner -> {
                    usrType = "owner"
                    buttonNext.text = "Next"
                }
            }
        }

        //Adapter for the spinner
        val spinnerAdapter = object : ArrayAdapter<String>(this.requireContext(),android.R.layout.simple_spinner_item,items){
            override fun isEnabled(position: Int): Boolean {
                return position!=0
            }
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup)
            : View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0){
                    view.setTextColor(Color.WHITE)
                }else{ }
                return view
            }
        }

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        service.adapter = spinnerAdapter

        service.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, postion: Int, id: Long)
            {
                val value = parent!!.getItemAtPosition(postion).toString()
                if (value == items[0]){
                    (view as TextView).setTextColor(Color.WHITE)
                }else{
                    (view as TextView).setTextColor(Color.WHITE)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
            // Authentication initialize
            auth = FirebaseAuth.getInstance()

            //Initialize db reference
            dbRef = FirebaseDatabase.getInstance().getReference("Users")

            return view
        }


    // allowing users to sign up using their email address and password.
    private fun firebaseSignUp() {
        val user = hashMapOf(
            "username" to username.text.toString().trim(),
            "password" to password.text.toString().trim(),
            "cnfPassword" to cnfPassword.text.toString().trim()
        )

        auth.createUserWithEmailAndPassword(username.text.toString(), password.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val bundle = Bundle()
                    bundle.putSerializable("user", user)
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }


            }
    }


    //logic to ensure that the user enters valid input values before registering their account
    private fun validateRegisterForm(): Boolean {
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
            password.text.toString().length <= 5 -> {
                password.error = "Please Enter at least 5 characters"
                return false
            }
            password.text.toString() != cnfPassword.text.toString() -> {
                password.error = "Password did not match"
                return false
            }
        }
        return if (!username.text.toString().matches(emailPattern)) {
            username.error = "Please Enter Valid Email Id"
            false
        } else {
            true
        }
    }

    private fun validateBusinessRegisterForm(): Boolean{
        when{
            TextUtils.isEmpty(busName.text.toString().trim()) -> {
                busName.error = "Required Field"
                return false
            }

            TextUtils.isEmpty(busAddress.text.toString().trim()) -> {
                busAddress.error = "Required Field"
                return false
            }

            TextUtils.isEmpty(busEmail.text.toString().trim()) -> {
                busEmail.error = "Required Field"
                return false
            }

            TextUtils.isEmpty(busPhone.text.toString().trim()) -> {
                busPhone.error = "Required Field"
                return false
            }
        }
        if(service.selectedItem == "Service Provided"){
            Toast.makeText(context,"Please select service provided", Toast.LENGTH_SHORT).show()
            return false
        }

        if(!busEmail.text.toString().matches(emailPattern)){
            busEmail.error = "Please Enter Valid Email"
            return false
        }

        return true
    }

        //To be used with Customer Registration Page
         private fun saveCustomerData() {

            //getting values from user input
            val userName = username.text.toString()
            val firstName = firstName.text.toString()
            val lastName = lastName.text.toString()
            val password = password.text.toString()
            val userType = usrType

            val userId = dbRef.push().key!!
            val user = UserModel(userId, userName, firstName, lastName, password, userType)

            dbRef.child(userId).setValue(user)
                .addOnCompleteListener {
                Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

        //To be used with Owner Registration Page
        private fun saveOwnerData() {

            //getting values from user input
            val userName = username.text.toString()
            val firstName = firstName.text.toString()
            val lastName = lastName.text.toString()
            val password = password.text.toString()
            val usrType = usrType

            val businessName = busName.text.toString()
            val busAddress = busAddress.text.toString()
            val busEmail = busEmail.text.toString()
            val busPhone = busPhone.text.toString()
            val service = service.selectedItem.toString()

            val userId = dbRef.push().key!!

            val user = OwnerModel(userId,userName,firstName,lastName,usrType,businessName,busAddress,busEmail,busPhone,service)

            dbRef.child(userId).setValue(user)
                .addOnCompleteListener {
                    Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { err ->
                    Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    private fun createOwner(){
        val userId = dbRef.push().key!!
        val user = OwnerModel(
            userId,
            "ehaz@yahoo.com",
            "Elias",
            "Hazboun",
            "owner",
            "Eli's Corner",
            "1920 Exchange Drive",
            "eliscorner@corner.com",
            "252-683-5694",
            "Assassination Services",
            "Cheap prices, swift death, pictures for proof. Money up front. Physical proof will be extra charge.",
            "Knifework",
        )
        dbRef.child(userId).setValue(user)
            .addOnCompleteListener {
            }.addOnFailureListener { err ->
                Toast.makeText(context, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
    private fun searchUsername(username: String, callback: (Boolean) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("Users")

        usersRef.orderByChild("userName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Username exists
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                    println("The read failed:" + databaseError.code)
                    callback(false)
                }
            })
    }

}





