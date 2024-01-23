package com.tcc.beautyplannerpro.funcionario

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
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

class FuncionarioInserirActivity : AppCompatActivity() {
    private lateinit var funcionarioNome: EditText
    private lateinit var funcionarioTelefone: EditText
    private lateinit var funcionarioEmail: EditText

    private lateinit var btnSalvarFuncionario: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_funcionario)

        funcionarioNome = findViewById(R.id.funcionarioNome)
        funcionarioTelefone = findViewById(R.id.funcionarioTelefone)
        funcionarioEmail = findViewById(R.id.funcionarioEmail)

        btnSalvarFuncionario = findViewById(R.id.btnSalvarFuncionario)

        dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios")

        btnSalvarFuncionario.setOnClickListener {
            salvarDadosFuncionario()
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


    private fun salvarDadosFuncionario() {

        //getting values
        val vfuncionarioNome = funcionarioNome.text.toString()
        val vfuncionarioTelefone = funcionarioTelefone.text.toString()
        val vfuncionarioEmail = funcionarioEmail.text.toString()


        if (vfuncionarioNome.isEmpty()) {
            funcionarioNome.error = "Inserir Nome"
        } else if (vfuncionarioTelefone.isEmpty()) {
            funcionarioTelefone.error = "Inserir Telefone"
        } else if (vfuncionarioEmail.isEmpty()) {
            funcionarioEmail.error = "Inserir E-mail"
        } else {
            //val funcionarioId = dbRef.push().key!! //criação do token unico
            val funcionarioId = vfuncionarioNome //aqui iguala ao que for ser usado como chave
            val funcionarioRef = dbRef.child(funcionarioId)
            funcionarioRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // The token already exists, you can handle this case here
                        Toast.makeText(
                            this@FuncionarioInserirActivity,
                            "Funcionario já cadastrado.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // The token doesn't exist, so you can proceed to create the entry
                        val funcionario = FuncionarioModel(
                            funcionarioId,
                            vfuncionarioNome,
                            vfuncionarioTelefone,
                            vfuncionarioEmail
                        )

                        dbRef.child(funcionarioId).setValue(funcionario)
                            .addOnCompleteListener {
                                Toast.makeText(
                                    this@FuncionarioInserirActivity,
                                    "Dados inseridos com sucesso",
                                    Toast.LENGTH_LONG
                                ).show()

                                funcionarioNome.text.clear()
                                funcionarioTelefone.text.clear()
                                funcionarioEmail.text.clear()
                            }.addOnFailureListener { err ->
                                Toast.makeText(
                                    this@FuncionarioInserirActivity,
                                    "Error ${err.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors that occur during the database read operation
                    Toast.makeText(
                        this@FuncionarioInserirActivity,
                        "Erro lendo banco de dados: ${databaseError.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}