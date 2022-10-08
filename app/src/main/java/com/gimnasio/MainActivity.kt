package com.gimnasio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gimnasio.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        setContentView(R.layout.activity_main)

        binding.logearse.setOnClickListener {
            haceLogearse();
        }

        binding.registro.setOnClickListener {
            haceregistro();
        }


    }

    private fun haceLogearse() {
        //recuperamos la informacion que ingreso el usuario
        val email =binding.email.text.toString()
        val Contraseña = binding.password.text.toString()
        // se hace el registro
        auth.signInWithEmailAndPassword(email,Contraseña)
            .addOnCompleteListener(this) {task ->

                var user: FirebaseUser? =null
                if (task.isSuccessful) {//si pudo crear el usuario
                    Log.d("autenticando", "autenticado")
                    user = auth.currentUser//recupero la info del usuario creado

                }else{
                    Log.d("autenticando", "fallo")

                }
                actualiza(user)
            }
    }



    private fun actualiza(user: FirebaseUser?) {
        // si hay un suario definido ... se pasa a la panatalla principal(Activity)
        if (user != null) {
            // se pasa a la otra pantalla
            val intent = Intent(this, Principio::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        val usuario = auth.currentUser
        actualiza(usuario)
    }

    private fun haceregistro() {
        val email = binding.email.text.toString()
        val Contraseña = binding.password.text.toString()

        auth.createUserWithEmailAndPassword(email, Contraseña)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("en proceso", "se ha registrado exitosamente")
                    val user = auth.currentUser
                    actualiza(user)
                } else {
                    Log.d("en proceso", "fallo al intentar crear el usuario")
                    Toast.makeText(baseContext, "fallo al intentar crear ", Toast.LENGTH_LONG)
                        .show()
                    actualiza(null)
                }
            }


    }
}


