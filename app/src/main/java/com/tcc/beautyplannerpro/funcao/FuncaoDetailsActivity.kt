package com.tcc.beautyplannerpro.funcao

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

class FuncaoDetailsActivity : AppCompatActivity() {
    private lateinit var tvfuncaoId: TextView
    private lateinit var tvfuncaofuncionarioNome: TextView
    private lateinit var tvfuncaoservicoNome: TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funcao_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("funcaoId").toString(),
                intent.getStringExtra("funcaofuncionarioNome").toString()
            )
           // return@setOnClickListener


        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("funcaoId").toString()
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
        funcaoId: String,
        funcaofuncionarioNome: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_funcao_dialog, null)

        mDialog.setView(mDialogView)

        val etfuncaofuncionarioNome = mDialogView.findViewById<EditText>(R.id.etfuncaofuncionarioNome)
        val etfuncaoservicoNome = mDialogView.findViewById<EditText>(R.id.etfuncaoServicoNome)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etfuncaofuncionarioNome.setText(intent.getStringExtra("funcaofuncionarioNome").toString())
        etfuncaoservicoNome.setText(intent.getStringExtra("funcaoservicoNome").toString())

        mDialog.setTitle("Atualizando $funcaofuncionarioNome ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateFuncionarioData(
                funcaoId,
                etfuncaofuncionarioNome.text.toString(),
                etfuncaoservicoNome.text.toString()
            )

            Toast.makeText(applicationContext, "Dados da função do funcionário atualizados", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvfuncaofuncionarioNome.text = etfuncaofuncionarioNome.text.toString()
            tvfuncaoservicoNome.text = etfuncaoservicoNome.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateFuncionarioData(
        funcaoId: String,
        funcaofuncionarioNome: String,
        funcaoservicoNome: String


    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcao").child(funcaoId)
        val funcaoInfo = FuncaoModel(
            funcaoId, funcaofuncionarioNome, funcaoservicoNome
        )
        dbRef.setValue(funcaoInfo)
    }

    private fun initView() {
        tvfuncaoId = findViewById(R.id.tvfuncaoId)
        tvfuncaofuncionarioNome = findViewById(R.id.tvfuncaofuncionarioNome)
        tvfuncaoservicoNome = findViewById(R.id.tvfuncaoservicoNome)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvfuncaoId.text = intent.getStringExtra("funcaoId")
        tvfuncaofuncionarioNome.text = intent.getStringExtra("funcaofuncionarioNome")
        tvfuncaoservicoNome.text = intent.getStringExtra("funcaoservicoNome")

    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Funcao").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados da função do funcionário excluídos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FuncaoBuscarActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Erro ao Excluir ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

}
