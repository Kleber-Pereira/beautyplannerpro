package com.tcc.beautyplannerpro.Servicos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.textclassifier.TextSelection
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannerpro.R

class ServicosInserirActivity : AppCompatActivity() {
    //private lateinit var servicosCategoria1:Spinner
    private lateinit var servicosCategoria: EditText
    private lateinit var servicosServico: EditText

    private lateinit var servicosTempo: EditText
    private lateinit var servicosPreco: EditText
    private lateinit var btnSalvarServicos: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_servicos)

        servicosServico = findViewById(R.id.servicosServico)
        servicosPreco = findViewById(R.id.servicosPreco)
        btnSalvarServicos = findViewById(R.id.btnSalvarServicos)

        dbRef = FirebaseDatabase.getInstance().getReference("Servicos")


        btnSalvarServicos.setOnClickListener {
            salvarDadosServicos()
            return@setOnClickListener
        }
    }

    private fun salvarDadosServicos() {

        val vservicosServico = servicosServico.text.toString()
        val vservicosPreco= servicosPreco.text.toString()


        if (vservicosServico.isEmpty()) {
            servicosServico.error = "Inserir Serviço"
        }

        else if (vservicosPreco.isEmpty()) {
            servicosPreco.error = "Inserir Preço médio"
        }

        else {
            val servicosId = dbRef.push().key!!

            val servicos = ServicosModel(
                servicosId,vservicosServico,
                vservicosPreco
            )

            dbRef.child(servicosId).setValue(servicos)
                .addOnCompleteListener {
                    Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show()

                    servicosServico.text.clear()
                    servicosPreco.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
}