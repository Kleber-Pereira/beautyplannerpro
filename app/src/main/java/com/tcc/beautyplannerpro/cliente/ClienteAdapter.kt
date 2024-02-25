package com.tcc.beautyplannerpro.cliente

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tcc.beautyplannerpro.R

class ClienteAdapter (private val clienteList: ArrayList<ClienteModel>) :
    RecyclerView.Adapter<ClienteAdapter.ViewHolder>() {

        //var onItemClick : ((clienteModel) -> Unit)? = null

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener{
            fun onItemClick(position: Int)

        }

        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listar_cliente, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentFuncionario = clienteList[position]
            holder.tvclienteNome.text = currentFuncionario.nome


        }

        override fun getItemCount(): Int {
            return clienteList.size
        }

        class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

            val tvclienteNome : TextView = itemView.findViewById(R.id.tvclienteNome)

            init {
                itemView.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }

        }

    }