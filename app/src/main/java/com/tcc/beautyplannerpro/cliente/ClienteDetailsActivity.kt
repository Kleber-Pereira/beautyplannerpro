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

    private lateinit var tvclienteNome: TextView
    private lateinit var tvclienteTelefone: TextView
    private lateinit var tvclienteEmail:  TextView


    private lateinit var funcoesservicoNome: String
    private lateinit var contato: String
    private lateinit var clienteNome: String
    private lateinit var clienteEmail: String

    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente_details)

        initView()
        setValuesToViews()

        funcoesservicoNome = intent.getStringExtra("servicoservico")!!
        contato = intent.getStringExtra("contato")!!
        clienteNome = intent.getStringExtra("nome")!!
        clienteEmail = intent.getStringExtra("email")!!

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("contato").toString()
               // intent.getStringExtra("clienteNome").toString()
            )
           // return@setOnClickListener


        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("contato").toString()
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
       // clienteId: String,
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

        etclienteNome.setText(intent.getStringExtra("nome").toString())
        etclienteTelefone.setText(intent.getStringExtra("contato").toString())
        etclienteEmail.setText(intent.getStringExtra("email").toString())
        //etclienteServico.setText(intent.getStringExtra("servico").toString())


        mDialog.setTitle("Atualizando $clienteNome ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateClienteData(
               // clienteId,
                etclienteNome.text.toString(),
                etclienteTelefone.text.toString(),
                etclienteEmail.text.toString(),
                funcoesservicoNome

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
       // clienteId: String,
        clienteNome: String,
        clienteContato: String,
        clienteEmail: String,
        clienteServico: String


    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child("Servico")
            .child(funcoesservicoNome).child(contato)
        val clienteInfo = ClienteModel(
             clienteNome, clienteContato, clienteEmail, clienteServico
        )
        dbRef.setValue(clienteInfo)
    }

    private fun initView() {
        //tvclienteId = findViewById(R.id.tvclienteId)
        tvclienteNome = findViewById(R.id.tvclienteNome)
        tvclienteTelefone = findViewById(R.id.tvclienteTelefone)
        tvclienteEmail = findViewById(R.id.tvclienteEmail)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        //tvclienteId.text = intent.getStringExtra("clienteId")
        tvclienteNome.text = intent.getStringExtra("nome")
        tvclienteTelefone.text = intent.getStringExtra("contato")
        tvclienteEmail.text = intent.getStringExtra("email")

    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child("Servico")
            .child(funcoesservicoNome).child(contato)
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
