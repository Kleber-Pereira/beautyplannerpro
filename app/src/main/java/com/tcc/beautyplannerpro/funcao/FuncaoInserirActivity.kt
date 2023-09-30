package com.tcc.beautyplannerpro.funcao

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannerpro.R
import java.text.SimpleDateFormat
import java.util.*

class FuncaoInserirActivity : AppCompatActivity() {
    private lateinit var funcaofuncionarioNome: EditText
    private lateinit var funcaoservicoNome: EditText

    private lateinit var btnSalvarFuncao: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_funcao)

        funcaofuncionarioNome = findViewById(R.id.funcaofuncionarioNome)
        funcaoservicoNome = findViewById(R.id.funcaoservicoNome)

        btnSalvarFuncao= findViewById(R.id.btnSalvarFuncao)

        dbRef = FirebaseDatabase.getInstance().getReference("Funcao")

        btnSalvarFuncao.setOnClickListener {
            salvarDadosFuncao()
            return@setOnClickListener
        }

    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
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


    private fun salvarDadosFuncao() {

        //getting values
        val vfuncaofuncionarioNome = funcaofuncionarioNome.text.toString()
        val vfuncaoservicoNome = funcaoservicoNome.text.toString()


        if (vfuncaofuncionarioNome.isEmpty()) {
            funcaofuncionarioNome.error = "Inserir Nome"
        }

        else if (vfuncaoservicoNome.isEmpty()) {
            funcaoservicoNome.error = "Inserir Telefone"
        }

        else {
            val funcaoId = dbRef.push().key!!

            val funcao = FuncaoModel(
                funcaoId,
                vfuncaofuncionarioNome,
                vfuncaoservicoNome )

            dbRef.child(funcaoId).setValue(funcao)
                .addOnCompleteListener {
                    Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show()

                    funcaofuncionarioNome.text.clear()
                    funcaoservicoNome.text.clear()

                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
}