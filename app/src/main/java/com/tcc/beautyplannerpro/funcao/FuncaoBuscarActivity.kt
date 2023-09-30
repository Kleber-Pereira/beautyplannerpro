package com.tcc.beautyplannerpro.funcao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tcc.beautyplannerpro.R


class FuncaoBuscarActivity : AppCompatActivity() {
    private lateinit var funcaoRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var funcaoList: ArrayList<FuncaoModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_funcao)

        funcaoRecyclerView = findViewById(R.id.rvfuncaofuncionarioNome)
        funcaoRecyclerView.layoutManager = LinearLayoutManager(this)
        funcaoRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        funcaoList = arrayListOf<FuncaoModel>()

        getFuncaoData()
    }
    private fun getFuncaoData() {

        funcaoRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Funcao")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                funcaoList.clear()
                if (snapshot.exists()){
                    for (funcaoSnap in snapshot.children){
                        val funcionarioData = funcaoSnap.getValue(FuncaoModel::class.java)
                        funcaoList.add(funcionarioData!!)
                    }
                    val mAdapter = FuncaoAdapter(funcaoList)
                    funcaoRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : FuncaoAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FuncaoBuscarActivity, FuncaoDetailsActivity::class.java)

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

    }
}
