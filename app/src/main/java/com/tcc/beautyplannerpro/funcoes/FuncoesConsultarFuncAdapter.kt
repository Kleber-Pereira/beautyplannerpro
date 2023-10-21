package com.tcc.beautyplannerpro.funcoes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.tcc.beautyplannerpro.R

class FuncoesConsultarFuncAdapter (private val funcoesList: ArrayList<FuncoesModel>) :
    RecyclerView.Adapter<FuncoesConsultarFuncAdapter.ViewHolder>() {

        //var onItemClick : ((FuncoesModel) -> Unit)? = null

        private lateinit var mListener: onItemClickListener

        interface onItemClickListener{
            fun onItemClick(position: Int)

        }

        fun setOnItemClickListener(clickListener: onItemClickListener){
            mListener = clickListener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.listar_funcoes_buscar_func, parent, false)
            return ViewHolder(itemView, mListener)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentFuncoes = funcoesList[position]
            holder.tvfuncoesfuncionarioNome.text= currentFuncoes.vfuncoesfuncionarioNome


        }

        override fun getItemCount(): Int {
            return funcoesList.size
        }

        class ViewHolder(itemView: View, clickListener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {

            val tvfuncoesfuncionarioNome: TextView = itemView.findViewById(R.id.tvfuncoesfuncionarioNome)
            //val vfuncoesservicoNome : TextView =

            init {
                itemView.setOnClickListener {
                    clickListener.onItemClick(adapterPosition)
                }
            }

        }

    }