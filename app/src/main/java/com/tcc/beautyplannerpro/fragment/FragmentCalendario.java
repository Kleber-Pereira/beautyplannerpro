package com.tcc.beautyplannerpro.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.tcc.beautyplannerpro.R;
import com.tcc.beautyplannerpro.activity.HorariosActivity;
import com.tcc.beautyplannerpro.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class FragmentCalendario extends Fragment implements CalendarView.OnDateChangeListener {

    private CalendarView calendarView;
    private int dia_Atual;
    private int mes_Atual;
    private int ano_Atual;



    public FragmentCalendario() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_calendario, container, false);

        calendarView = (CalendarView) view.findViewById(R.id.calendarView_Calendario);

        obterDataAtual();


        calendarView.setOnDateChangeListener(this);


        return view;
    }


    //---------------------------------------- OBTER DATA ATUAL ----------------------------------------



    private void obterDataAtual(){

        long dataLong = calendarView.getDate(); //123456465465213

        Locale locale = new Locale("pt","BR");

        SimpleDateFormat dia = new SimpleDateFormat("dd",locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM",locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy",locale);




        dia_Atual = Integer.parseInt(dia.format(dataLong));

        mes_Atual = Integer.parseInt(mes.format(dataLong));

        ano_Atual = Integer.parseInt(ano.format(dataLong));



    }



    //---------------------------------------- CLICK CALENDARIO ----------------------------------------

    @Override
    public void onSelectedDayChange(CalendarView calendarView,
                                    int anoSelecionado, int mesSelecionado, int diaSelecionado) {


        int mes = mesSelecionado + 1;



        dataSelecionada(diaSelecionado,mes,anoSelecionado);

    }


    private void dataSelecionada(int diaSelecionado,int mesSelecionado,int anoSelecionado){



        Locale locale = new Locale("pt","BR");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",locale);

        Calendar data = Calendar.getInstance();


        try {
            data.setTime(simpleDateFormat.parse(diaSelecionado+"/"+mesSelecionado+"/"+anoSelecionado));

             boolean disponivelAgendamento ;


                if (Util.statusInternet_MoWi(getContext())){

                    String dia = String.valueOf(diaSelecionado);
                    String mes = String.valueOf(mesSelecionado);
                    String ano = String.valueOf(anoSelecionado);

                    ArrayList<String> dataList = new ArrayList<String>();

                    dataList.add(dia);// posicao 0
                    dataList.add(mes);// posicao 1
                    dataList.add(ano);// posicao 2

                    Intent intent = new Intent(getContext(), HorariosActivity.class);
                    intent.putExtra("data",dataList);

                    startActivity(intent);

                }else{

                    Toast.makeText(getContext(),"Erro - Sem conexão com a internet",Toast.LENGTH_LONG).show();
                }



        } catch (ParseException e) {
            e.printStackTrace();
        }


    }







}