package com.tcc.beautyplannerpro.cliente

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tcc.beautyplannerpro.R
import java.text.SimpleDateFormat
import java.util.*

class ClienteInserirActivity : AppCompatActivity() {
    private lateinit var clienteNome: EditText
    private lateinit var clienteTelefone: EditText
    private lateinit var clienteEmail: EditText

    private lateinit var btnSalvarCliente: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_cliente)

        clienteNome = findViewById(R.id.clienteNome)
        clienteTelefone = findViewById(R.id.clienteTelefone)
        clienteEmail = findViewById(R.id.clienteEmail)

        btnSalvarCliente = findViewById(R.id.btnSalvarCliente)

        dbRef = FirebaseDatabase.getInstance().getReference("Cliente")

        btnSalvarCliente.setOnClickListener {
            salvarDadosCliente()
            return@setOnClickListener
        }

    }

    private fun EditText.transformIntoDatePicker(
        context: Context,
        format: String,
        maxDate: Date? = null
    ) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.UK)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }


    private fun salvarDadosCliente() {

        //getting values
        val vclienteNome = clienteNome.text.toString()
        val vclienteTelefone = clienteTelefone.text.toString()
        val vclienteEmail = clienteEmail.text.toString()


        if (vclienteNome.isEmpty()) {
            clienteNome.error = "Inserir Nome"
        } else if (vclienteTelefone.isEmpty()) {
            clienteTelefone.error = "Inserir Telefone"
        } else if (vclienteEmail.isEmpty()) {
            clienteEmail.error = "Inserir E-mail"
        } else {
            //val clienteId = dbRef.push().key!! //criação do token unico
            val clienteId = vclienteNome //aqui iguala ao que for ser usado como chave
            val clienteRef = dbRef.child(clienteId)
            clienteRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // The token already exists, you can handle this case here
                        Toast.makeText(
                            this@ClienteInserirActivity,
                            "Cliente já cadastrado.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // The token doesn't exist, so you can proceed to create the entry
                        val cliente = ClienteModel(
                            clienteId,
                            vclienteNome,
                            vclienteTelefone,
                            vclienteEmail
                        )

                        dbRef.child(clienteId).setValue(cliente)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    this@ClienteInserirActivity,
                                    "Dados inseridos com sucesso",
                                    Toast.LENGTH_LONG
                                ).show()

                                clienteNome.text.clear()
                                clienteTelefone.text.clear()
                                clienteEmail.text.clear()
                            }.addOnFailureListener { err ->
                                Toast.makeText(
                                    this@ClienteInserirActivity,
                                    "Error ${err.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors that occur during the database read operation
                    Toast.makeText(
                        this@ClienteInserirActivity,
                        "Erro lendo banco de dados: ${databaseError.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}