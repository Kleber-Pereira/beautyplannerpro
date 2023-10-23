package com.tcc.beautyplannerpro.agendamento

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.tcc.beautyplannerpro.R
import com.tcc.beautyplannerpro.fragment.FragmentCalendario


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var btnInsertData: Button
private lateinit var btnFetchData: Button
/**
 * A simple [Fragment] subclass.
 * Use the [FuncaoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
public class FuncoesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         //Inflate the layout for this fragment
         return inflater.inflate(R.layout.activity_funcoes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnInsertData = view.findViewById(R.id.btnInsertData)
        btnFetchData = view.findViewById(R.id.btnFetchData)

        btnInsertData.setOnClickListener{
            /*val intent = Intent(activity, FuncoesBuscarServicoActivity::class.java)
            startActivity(intent)*/
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

           // fragmentTransaction.replace(R.id.chamacontainer, FragmentCalendario()).commit()

        }
        btnFetchData.setOnClickListener{
            /*val intent = Intent(activity, FuncoesConsultarServicoActivity::class.java)//editar
            startActivity(intent)*/
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

          //  fragmentTransaction.replace(R.id.chamacontainer, FragmentCalendario()).commit()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FuncionarioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FuncoesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}