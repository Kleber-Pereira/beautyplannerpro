package com.tcc.beautyplannerpro.funcionario

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannerpro.R
import java.text.SimpleDateFormat
import java.util.*

class FuncionarioDetailsActivity : AppCompatActivity() {
    private lateinit var tvfuncionarioId: TextView
    private lateinit var tvfuncionarioNome: TextView
    private lateinit var tvfuncionarioTelefone: TextView
    private lateinit var tvfuncionarioEmail:  TextView
    private lateinit var tvfuncionarioServico:  TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcionario_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("funcionarioId").toString(),
                intent.getStringExtra("funcionarioNome").toString()
            )
           // return@setOnClickListener


        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("funcionarioId").toString()
            )
            //return@setOnClickListener
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


    private fun openUpdateDialog(
        funcionarioId: String,
        funcionarioNome: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_funcionario_dialog, null)

        mDialog.setView(mDialogView)

        val etfuncionarioNome = mDialogView.findViewById<EditText>(R.id.etfuncionarioNome)
        val etfuncionarioTelefone = mDialogView.findViewById<EditText>(R.id.etfuncionarioTelefone)
        val etfuncionarioEmail = mDialogView.findViewById<EditText>(R.id.etfuncionarioEmail)
        val etfuncionarioServico = mDialogView.findViewById<EditText>(R.id.etfuncionarioServico)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etfuncionarioNome.setText(intent.getStringExtra("funcionarioNome").toString())
        etfuncionarioTelefone.setText(intent.getStringExtra("funcionarioTelefone").toString())
        etfuncionarioEmail.setText(intent.getStringExtra("funcionarioEmail").toString())
        etfuncionarioServico.setText(intent.getStringExtra("funcionarioServico").toString())


        mDialog.setTitle("Atualizando $funcionarioNome ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateFuncionarioData(
                funcionarioId,
                etfuncionarioNome.text.toString(),
                etfuncionarioTelefone.text.toString(),
                etfuncionarioEmail.text.toString(),
                etfuncionarioServico.text.toString()

            )

            Toast.makeText(applicationContext, "Dados do funcionario atulizado", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvfuncionarioNome.text = etfuncionarioNome.text.toString()
            tvfuncionarioTelefone.text = etfuncionarioTelefone.text.toString()
            tvfuncionarioEmail.text = etfuncionarioEmail.text.toString()
            tvfuncionarioServico.text = etfuncionarioServico.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateFuncionarioData(
        funcionarioId: String,
        funcionarioNome: String,
        funcionarioTelefone: String,
        funcionarioEmail: String,
        funcionarioServico: String


    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios").child(funcionarioId)
        val funcionarioInfo = FuncionarioModel(
            funcionarioId, funcionarioNome, funcionarioTelefone, funcionarioEmail,
            funcionarioServico
        )
        dbRef.setValue(funcionarioInfo)
    }

    private fun initView() {
        tvfuncionarioId = findViewById(R.id.tvfuncionarioId)
        tvfuncionarioNome = findViewById(R.id.tvfuncionarioNome)
        tvfuncionarioTelefone = findViewById(R.id.tvfuncionarioTelefone)
        tvfuncionarioEmail = findViewById(R.id.tvfuncionarioEmail)
        tvfuncionarioServico = findViewById(R.id.tvfuncionarioServico)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvfuncionarioId.text = intent.getStringExtra("funcionarioId")
        tvfuncionarioNome.text = intent.getStringExtra("funcionarioNome")
        tvfuncionarioTelefone.text = intent.getStringExtra("funcionarioTelefone")
        tvfuncionarioEmail.text = intent.getStringExtra("funcionarioEmail")
        tvfuncionarioServico.text = intent.getStringExtra("funcionarioServico")

    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados do funcionario exluídos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FuncionarioBuscarActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Erro ao Excluir ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

}
