package com.tcc.beautyplannerpro.funcionario

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannerpro.R
import java.text.SimpleDateFormat
import java.util.*

class FuncionarioInserirActivity : AppCompatActivity() {
    private lateinit var funcionarioNome: EditText
    private lateinit var funcionarioTelefone: EditText
    private lateinit var funcionarioEmail: EditText
    private lateinit var funcionarioServico: EditText
    private lateinit var funcionarioStatus: EditText
    private lateinit var checkBox_funcionarioStatus: CheckBox

    private lateinit var btnSalvarFuncionario: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inserir_funcionario)

        funcionarioNome = findViewById(R.id.funcionarioNome)
        funcionarioTelefone = findViewById(R.id.funcionarioTelefone)
        funcionarioEmail = findViewById(R.id.funcionarioEmail)
        funcionarioServico = findViewById(R.id.funcionarioServico)
        funcionarioStatus = findViewById(R.id.funcionarioStatus)
        checkBox_funcionarioStatus = findViewById(R.id.checkbox_funcionarioStatus)

        btnSalvarFuncionario = findViewById(R.id.btnSalvarFuncionario)

        dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios")

        btnSalvarFuncionario.setOnClickListener {
            salvarDadosFuncionario()
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


    private fun salvarDadosFuncionario() {

        //getting values
        val vfuncionarioNome = funcionarioNome.text.toString()
        val vfuncionarioTelefone = funcionarioTelefone.text.toString()
        val vfuncionarioEmail = funcionarioEmail.text.toString()
        val vfuncionarioServico = funcionarioServico.text.toString()
        val vfuncionarioStatus = funcionarioStatus.text.toString()
        val vcheckbox_funcionarioStatus = checkBox_funcionarioStatus.isChecked


        if (vfuncionarioNome.isEmpty()) {
            funcionarioNome.error = "Inserir Nome"
        }

        else if (vfuncionarioTelefone.isEmpty()) {
            funcionarioTelefone.error = "Inserir Telefone"
        }
        else if (vfuncionarioEmail.isEmpty()) {
            funcionarioEmail.error = "Inserir E-mail"
        }
        else if (vfuncionarioServico.isEmpty()) {
            funcionarioServico.error = "Inserir Função"
        }

        else if (vfuncionarioStatus.isEmpty()) {
            funcionarioStatus.error = "Inserir Status"
        }

        else {
            val funcionarioId = dbRef.push().key!!

            val funcionario = FuncionarioModel(
                funcionarioId,
                vfuncionarioNome,
                vfuncionarioTelefone,
                vfuncionarioEmail,
                vfuncionarioServico,
                vfuncionarioStatus,
                vcheckbox_funcionarioStatus

                )

            dbRef.child(funcionarioId).setValue(funcionario)
                .addOnCompleteListener {
                    Toast.makeText(this, "Dados inseridos com sucesso", Toast.LENGTH_LONG).show()

                    funcionarioNome.text.clear()
                    funcionarioTelefone.text.clear()
                    funcionarioEmail.text.clear()
                    funcionarioServico.text.clear()
                    funcionarioStatus.text.clear()
                    checkBox_funcionarioStatus.isChecked


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }

    }
}