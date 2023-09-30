package com.tcc.beautyplannerpro.Servicos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.tcc.beautyplannerpro.R

class ServicosDetailsActivity : AppCompatActivity() {
    private lateinit var tvservicosId: TextView
    private lateinit var tvservicosServico: TextView
    private lateinit var tvservicosPreco: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicos_details)

        initView()
        setValuesToViews()

        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("servicosId").toString(),
                intent.getStringExtra("servicosServico").toString()
            )
           // return@setOnClickListener


        }
        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("servicosId").toString()
            )
            //return@setOnClickListener
        }


    }

    private fun openUpdateDialog(
        servicosId: String,
        servicosServico: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_servicos_dialog, null)

        mDialog.setView(mDialogView)

        val etservicosServico = mDialogView.findViewById<EditText>(R.id.etservicosServico)
        val etservicosPreco = mDialogView.findViewById<EditText>(R.id.etservicosPreco)


        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etservicosServico.setText(intent.getStringExtra("servicosServico").toString())
        etservicosPreco.setText(intent.getStringExtra("servicosPreco").toString())


        mDialog.setTitle("Atualizando $servicosServico ")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateServicosData(
                servicosId,
                etservicosServico.text.toString(),
                etservicosPreco.text.toString()

            )

            Toast.makeText(applicationContext, "Dados do servico atulizado", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews

            tvservicosServico.text = etservicosServico.text.toString()
            tvservicosPreco.text = etservicosPreco.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateServicosData(
        servicosId: String,
        servicosServico: String,
        servicosPreco: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Servicos").child(servicosId)
        val servicosInfo = ServicosModel(servicosId,servicosServico,servicosPreco)
        dbRef.setValue(servicosInfo)
    }

    private fun initView() {
        tvservicosId = findViewById(R.id.tvservicosId)
        tvservicosServico = findViewById(R.id.tvservicosServico)
        tvservicosPreco = findViewById(R.id.tvservicosPreco)
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvservicosId.text = intent.getStringExtra("servicosId")
        tvservicosServico.text = intent.getStringExtra("servicosServico")
        tvservicosPreco.text = intent.getStringExtra("servicosPreco")
    }
    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Servicos").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Dados do serviço excluídos", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ServicosBuscarActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Erro ao Excluir ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

}
