package com.tcc.beautyplannerpro.funcoes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannerpro.R
import com.tcc.beautyplannerpro.funcionario.FuncionarioModel


class FuncoesConsultarFuncionarioActivity : AppCompatActivity() {
    private lateinit var funcoesRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
   // private lateinit var funcoesList: ArrayList<FuncoesModel>


    private lateinit var funcoesList: ArrayList<FuncionarioModel>

    private lateinit var dbRef: DatabaseReference
   // private lateinit var dbReffunc: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_funcionario_funcoes)

        funcoesRecyclerView = findViewById(R.id.rvfuncionario)
        funcoesRecyclerView.layoutManager = LinearLayoutManager(this)
        funcoesRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        //funcoesList = arrayListOf<FuncoesModel>()
        funcoesList = arrayListOf<FuncionarioModel>()




        getServicosData()
    }

    private fun getServicosData() {

        funcoesRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE




        dbRef = FirebaseDatabase.getInstance().getReference("Funcionarios")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcoesList.clear()
                if (snapshot.exists()){
                    for (funcoesSnap in snapshot.children){
                        val funcoesData = funcoesSnap.getValue(FuncionarioModel::class.java)
                        funcoesList.add(funcoesData!!)
                    }
                    val mAdapter = FuncoesConsultarFuncAdapter(funcoesList)
                    funcoesRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncoesConsultarFuncAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {


                            //val intent = Intent(this@FuncoesConsultarFuncionarioActivity, FuncoesDetailsActivity::class.java)
                            val intent = Intent(this@FuncoesConsultarFuncionarioActivity, FuncoesConsultarServicoActivity::class.java)

                            //put extras
                            intent.putExtra("funcionarioId", funcoesList[position].funcionarioId)
                            intent.putExtra("funcoesfuncionarioNome", funcoesList[position].vfuncionarioNome)
                            //intent.putExtra("funcoesservicoNome", funcoesList[position].vfuncoesservicoNome)

                            startActivity(intent)

                        }

                    }

                    )

                    funcoesRecyclerView.visibility = View.VISIBLE
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
