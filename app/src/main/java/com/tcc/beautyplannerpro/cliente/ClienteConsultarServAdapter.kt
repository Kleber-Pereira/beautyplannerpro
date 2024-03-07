package com.tcc.beautyplannerpro.cliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tcc.beautyplannerpro.R

class ClienteConsultarServAdapter (private val clienteList: ArrayList<ClienteModel>) :
    RecyclerView.Adapter<ClienteConsultarServAdapter.ViewHolder>() {

        //var onItemClick : ((FuncoesModel) -> Unit)? = null

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener{
            fun onItemClick(position: Int)

        }

        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listar_funcoes_buscar_cliente, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentFuncoes = clienteList[position]
            holder.tvclienteNome.text= currentFuncoes.nome


        }

        override fun getItemCount(): Int {
            return clienteList.size
        }

        class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

            val tvclienteNome: TextView = itemView.findViewById(R.id.tvclienteNome)
            //val vfuncoesservicoNome : TextView =

            init {
                itemView.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }

        }

    }