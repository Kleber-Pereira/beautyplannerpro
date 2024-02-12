package com.tcc.beautyplannerpro.cliente

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

class ClienteDetailsActivity : AppCompatActivity() {
    private lateinit var tvclienteId: TextView
    private lateinit var tvclienteNome: TextView
    private lateinit var tvclienteTelefone: TextView
    private lateinit var tvclienteEmail:  TextView

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("clienteId").toString(),
                intent.getStringExtra("clienteNome").toString()
            )
           // return@setOnClickListener


        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("clienteId").toString()
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
        clienteId: String,
        clienteNome: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_cliente_dialog, null)

        mDialog.setView(mDialogView)

        val etclienteNome = mDialogView.findViewById<EditText>(R.id.etclienteNome)
        val etclienteTelefone = mDialogView.findViewById<EditText>(R.id.etclienteTelefone)
        val etclienteEmail = mDialogView.findViewById<EditText>(R.id.etclienteEmail)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etclienteNome.setText(intent.getStringExtra("clienteNome").toString())
        etclienteTelefone.setText(intent.getStringExtra("clienteTelefone").toString())
        etclienteEmail.setText(intent.getStringExtra("clienteEmail").toString())


        mDialog.setTitle("Atualizando $clienteNome ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateClienteData(
                clienteId,
                etclienteNome.text.toString(),
                etclienteTelefone.text.toString(),
                etclienteEmail.text.toString()

            )

            Toast.makeText(applicationContext, "Dados do funcionario atulizado", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvclienteNome.text = etclienteNome.text.toString()
            tvclienteTelefone.text = etclienteTelefone.text.toString()
            tvclienteEmail.text = etclienteEmail.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateClienteData(
        clienteId: String,
        clienteNome: String,
        clienteTelefone: String,
        clienteEmail: String,


    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child(clienteId)
        val clienteInfo = ClienteModel(
            clienteId, clienteNome, clienteTelefone, clienteEmail
        )
        dbRef.setValue(clienteInfo)
    }

    private fun initView() {
        tvclienteId = findViewById(R.id.tvclienteId)
        tvclienteNome = findViewById(R.id.tvclienteNome)
        tvclienteTelefone = findViewById(R.id.tvclienteTelefone)
        tvclienteEmail = findViewById(R.id.tvclienteEmail)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvclienteId.text = intent.getStringExtra("clienteId")
        tvclienteNome.text = intent.getStringExtra("clienteNome")
        tvclienteTelefone.text = intent.getStringExtra("clienteTelefone")
        tvclienteEmail.text = intent.getStringExtra("clienteEmail")

    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados do cliente excluídos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ClienteBuscarActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Erro ao Excluir ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

}
