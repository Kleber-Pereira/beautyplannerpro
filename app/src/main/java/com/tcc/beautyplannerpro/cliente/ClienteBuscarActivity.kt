package com.tcc.beautyplannerpro.cliente

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannerpro.R

class ClienteBuscarActivity : AppCompatActivity() {
    private lateinit var clienteRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var clienteList: ArrayList<ClienteModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_cliente)

        clienteRecyclerView = findViewById(R.id.rvcliente)
        clienteRecyclerView.layoutManager = LinearLayoutManager(this)
        clienteRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        clienteList = arrayListOf<ClienteModel>()

        getClienteData()
    }
    private fun getClienteData() {

        clienteRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Cliente")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clienteList.clear()
                if (snapshot.exists()){
                    for (clienteSnap in snapshot.children){
                        val clienteData = clienteSnap.getValue(ClienteModel::class.java)
                        clienteList.add(clienteData!!)
                    }
                    val mAdapter = ClienteAdapter(clienteList)
                    clienteRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ClienteAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@ClienteBuscarActivity, ClienteDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("clienteId", clienteList[position].clienteId)
                            intent.putExtra("clienteNome", clienteList[position].vclienteNome)
                            intent.putExtra("clienteTelefone", clienteList[position].vclienteTelefone)
                            intent.putExtra("clienteEmail", clienteList[position].vclienteEmail)

                            startActivity(intent)

                        }

                    }

                    )

                    clienteRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}
