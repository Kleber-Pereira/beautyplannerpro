package com.tcc.beautyplannerpro.funcao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.tcc.beautyplannerpro.R

class FuncaoActivity : AppCompatActivity() {
    //private lateinit var btnInsertData: Button
   // private lateinit var btnFetchData: Button



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario)


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