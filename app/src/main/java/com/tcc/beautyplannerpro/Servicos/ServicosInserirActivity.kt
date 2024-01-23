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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            //val servicosId = dbRef.push().key!! //ponto de criação do token
            val servicosId = vservicosServico
            val servicoRef = dbRef.child(servicosId)
            servicoRef.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if(dataSnapshot.exists()){
                        // The token already exists, you can handle this case here
                        Toast.makeText(
                            this@ServicosInserirActivity,
                            "Serviço já cadastrado.",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        val servicos = ServicosModel(
                            servicosId,vservicosServico,
                            vservicosPreco
                        )

                        dbRef.child(servicosId).setValue(servicos)
                            .addOnCompleteListener {
                                Toast.makeText(this@ServicosInserirActivity, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show()

                                servicosServico.text.clear()
                                servicosPreco.text.clear()


                            }.addOnFailureListener { err ->
                                Toast.makeText(this@ServicosInserirActivity, "Error ${err.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@ServicosInserirActivity,
                        "Erro lendo banco de dados: ${databaseError.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })


        }

    }
}