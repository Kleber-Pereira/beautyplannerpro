package com.tcc.beautyplannerpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.tcc.beautyplannerpro.R;
import com.tcc.beautyplannerpro.adapter.AdapterRecyclerView;
import com.tcc.beautyplannerpro.util.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.beautyplannerpro.adapter.AdapterRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HorariosActivity extends AppCompatActivity implements AdapterRecyclerView.ClickItemRecyclerView {



    private RecyclerView recyclerView;
    private AdapterRecyclerView adapterRecyclerView;

    private List<String> horarios= new ArrayList<String>();

    private List<String> horarios_Temp= new ArrayList<String>();


    private ArrayList<String> data = new ArrayList<String>();




    //firebase
    private FirebaseDatabase database;

    private DatabaseReference referenceBuscarHorario;
    private ChildEventListener childEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);




        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);


        database = FirebaseDatabase.getInstance();

        data = getIntent().getStringArrayListExtra("data");



        configurarRecyclerView();

        carregarHorarioFuncionamento();




    }


    //-------------------------------------CONFIGURAR configurarRecyclerView-------------------------------------------------

    private void configurarRecyclerView(){


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterRecyclerView = new AdapterRecyclerView(this,horarios,this);

        recyclerView.setAdapter(adapterRecyclerView);



    }




    //-------------------------------------CARREGAR HORARIOS EMPRESA-------------------------------------------------


    private void carregarHorarioFuncionamento(){



        DatabaseReference reference = database.getReference().
                child("BD").child("Calendario").child("HorariosFuncionamento");



        reference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists()){

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){


                        String horario = snapshot.getValue(String.class);

                        horarios.add(horario);

                        horarios_Temp.add(horario);


                    }

                     adapterRecyclerView.notifyDataSetChanged();
                  //  adapterListView.notifyDataSetChanged();

                    buscarHorariosReservados();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    //-------------------------------------BUSCA HORARIOS AGENDADOS-------------------------------------------------

    //ESSE METODO É CHAMADO SOMENTE DEPOIS QUE O METODO ACIMA FOR EXECUTADO

    private void buscarHorariosReservados(){


        referenceBuscarHorario = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").child(data.get(1)).child("dia").child(data.get(0));



        if (childEventListener == null){


            childEventListener = new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                    String chave = dataSnapshot.getKey();  // le o nome da pasta

                    int index = horarios.indexOf(chave);



                    String horario = chave + " - Reservado";



                    horarios.set(index,horario);



                    adapterRecyclerView.notifyDataSetChanged();
                   // adapterListView.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {



                    String chave = dataSnapshot.getKey();  // le o nome da pasta


                    String horario = chave + " - Reservado";


                    int index = horarios.indexOf(horario);



                    horarios.set(index,chave);


                    adapterRecyclerView.notifyDataSetChanged();
                  //  adapterListView.notifyDataSetChanged();


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            referenceBuscarHorario.addChildEventListener(childEventListener);

        }

    }


    //-------------------------------------CLICK ITEM DA LISTA------------------------------------------------


    @Override
    public void clickItem(String horario, int posicao) {


        if (Util.statusInternet_MoWi(getBaseContext())) {
            consultarHorarioSelecionadoBanco(horario, posicao);

        }else{

            Toast.makeText(getBaseContext(),"Erro - Sem conexão com a Internet",Toast.LENGTH_LONG).show();

        }

    }

    private void consultarHorarioSelecionadoBanco(final String horario, final int posicao){

        DatabaseReference reference = database.getReference().
                child("BD").child("Calendario").child("HorariosAgendados").
                child(data.get(2)).child("Mes").
                child(data.get(1)).child("dia").child(data.get(0)).child(horarios_Temp.get(posicao));

        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    Intent intent = new Intent(getBaseContext(),AlterarRemoverServicoActivity.class);
                    String horarioSelecionado = horarios_Temp.get(posicao);

                    data.add(3,horarioSelecionado);

                    intent.putExtra("data",data);

                    startActivity(intent);


                }else{


                    Intent intent = new Intent(getBaseContext(), AgendamentoServicoActivity.class);

                    //  data.add(dia);// posicao 0
                    //  data.add(mes);// posicao 1
                    //  data.add(ano);// posicao 2
                    //  data.add(horario);// posicao 3

                    data.add(3,horario);


                    intent.putExtra("data",data);

                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }








    //-------------------------------------CLICO DE VIDA ACTIVITY------------------------------------------------



    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (childEventListener == null){


            referenceBuscarHorario.removeEventListener(childEventListener);

          //  childEventListener = null;

        }


    }
}
