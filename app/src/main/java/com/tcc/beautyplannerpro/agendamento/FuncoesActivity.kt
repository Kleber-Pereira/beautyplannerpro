package com.tcc.beautyplannerpro.agendamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.tcc.beautyplannerpro.R

class FuncoesActivity : AppCompatActivity() {
    //private lateinit var btnInsertData: Button
   // private lateinit var btnFetchData: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcoes)


       // btnInsertData = findViewById(R.id.btnInsertData)
       // btnFetchData = findViewById(R.id.btnFetchData)

       /* btnInsertData.setOnClickListener{
            val intent = Intent(this, InserirActivity::class.java)
            startActivity(intent)
        }*/

        /*btnFetchData.setOnClickListener {
            val intent = Intent(this, BuscarActivity::class.java)
            startActivity(intent)
        }*/

    }
}