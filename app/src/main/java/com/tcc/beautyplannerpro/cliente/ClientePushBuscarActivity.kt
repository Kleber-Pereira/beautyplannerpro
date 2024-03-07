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


class ClientePushBuscarActivity : AppCompatActivity() {
    private lateinit var clienteRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var clienteList: ArrayList<ClienteModel>
    private lateinit var servicoNome: String
    private lateinit var contato: String


   // private lateinit var funcionariosList: ArrayList<FuncionarioModel>

    private lateinit var dbRef: DatabaseReference
   // private lateinit var dbReffunc: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_servicos_clientes)

        clienteRecyclerView = findViewById(R.id.rvservicosCliente)
        clienteRecyclerView.layoutManager = LinearLayoutManager(this)
        clienteRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        clienteList = arrayListOf<ClienteModel>()

        servicoNome = intent.getStringExtra("servicosServico").toString()


        getServicosData()
    }

    private fun getServicosData() {

        clienteRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE




        dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child("Servico")
            .child(servicoNome);

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                clienteList.clear()
                if (snapshot.exists()){
                    for (clienteSnap in snapshot.children){
                        val clienteData = clienteSnap.getValue(ClienteModel::class.java)
                        clienteList.add(clienteData!!)
                       /*// if (servicoNome == clienteData!!.servico){
                            clienteList.add(clienteData!!)
                       // }
                       // funcoesList.add(funcoesData!!)*/
                    }
                    val mAdapter = ClienteConsultarServAdapter(clienteList)
                    clienteRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : ClienteConsultarServAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {


                            val intent = Intent(this@ClientePushBuscarActivity, EnviarPushActivity::class.java)

                            //put extras
                            intent.putExtra("contato", clienteList[position].contato)
                            intent.putExtra("nome", clienteList[position].nome)
                            intent.putExtra("servicoservico", clienteList[position].servico)
                            intent.putExtra("email", clienteList[position].email)
                            intent.putExtra("clientelista",clienteList)
                            intent.putExtra("servicoservico", clienteList[position].servico)


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



    /*private fun getFuncaoData() {

        funcaoRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Funcao")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcaoList.clear()
                if (snapshot.exists()){
                    for (funcaoSnap in snapshot.children){
                        val funcaoData = funcaoSnap.getValue(FuncaoModel::class.java)
                        funcaoList.add(funcaoData!!)
                    }
                    val mAdapter = FuncaoAdapter(funcaoList)
                    funcaoRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncaoAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FuncoesInsBuscarActivity, FuncaoDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("funcaoId", funcaoList[position].funcaoId)
                            intent.putExtra("funcaofuncionarioNome", funcaoList[position].vfuncaofuncionarioNome)
                            intent.putExtra("funcaoservicoNome", funcaoList[position].vfuncaoservicoNome)

                            startActivity(intent)

                        }

                    }

                    )

                    funcaoRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }*/
}
