package com.tcc.beautyplannerpro.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.tcc.beautyplannerpro.databinding.ActivityLoginBinding
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.tcc.beautyplannerpro.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = FirebaseAuth.getInstance()

        checkIfUserIsLogged()

        binding.btnLogin.setOnClickListener{
            logUserIn()
        }

        binding.btnRegister.setOnClickListener{
            registerUser()
        }
    }

    private fun checkIfUserIsLogged(){

        if(user.currentUser != null){
            startActivity(
                Intent(
                    this,
                    MainActivity::class.java
                )
            )
            finish()
        }
    }

    private fun logUserIn(){

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            user.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { mTask ->

                    if(mTask.isSuccessful){
                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    }else{
                        Toast.makeText(
                            this,
                            mTask.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Email e Password não podem ser vazios",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun registerUser() {

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {

            user.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity()) { task ->

                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Registrado com sucesso",
                            Toast.LENGTH_SHORT
                        ).show()

                        startActivity(
                            Intent(
                                this,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    } else {

                        user.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { mTask ->

                                if(mTask.isSuccessful){
                                    startActivity(
                                        Intent(
                                            this,
                                            MainActivity::class.java
                                        )
                                    )
                                    finish()
                                }else{
                                    Toast.makeText(
                                        this,
                                        task.exception!!.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Email e Password não podem ser vazios",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}