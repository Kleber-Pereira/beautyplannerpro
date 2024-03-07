package com.tcc.beautyplannerpro.cliente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tcc.beautyplannerpro.R;
import com.tcc.beautyplannerpro.modelo.Agendamento;
import com.tcc.beautyplannerpro.modelo.TwilioService;
import com.tcc.beautyplannerpro.util.DialogProgress;
import com.tcc.beautyplannerpro.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EnviarPushActivity extends AppCompatActivity implements View.OnClickListener  {


    private TextView editText_Servico;
    private TextView editText_Mensagem;
    private CardView cardView_EnviarPush;

    private RecyclerView clienteRecyclerView;

    private String funcoesservicoNome;
    private DatabaseReference dbRef;
    //ArrayList<ClienteModel> clienteList = new ArrayList<>();
    private ArrayList<ClienteModel> clientelista = new ArrayList<>();


    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviar_push);



        funcoesservicoNome = getIntent().getStringExtra("servicosServico");
       // clientelista= (ArrayList<ClienteModel>) getIntent().getSerializableExtra("clientelista");


        editText_Servico = (TextView)findViewById(R.id.editText_Servico);
        editText_Mensagem = (TextView)findViewById(R.id.textView_Mensagem);
        cardView_EnviarPush = (CardView)findViewById(R.id.cardView_EnviarPush);

        cardView_EnviarPush.setOnClickListener(this);

        editText_Servico.setText(funcoesservicoNome);

    }


    //----------------------------------ACAO DE CLICK--------------------------------------------------

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case R.id.cardView_EnviarPush:

                enviarpush();

                break;

        }

    }


    //----------------------------------AGENDAR NO FIREBASE-------------------------------------------------


    private void enviarpush(){



        String servico = editText_Servico.getText().toString();
        String mensagem = editText_Mensagem.getText().toString();
        SimpleDateFormat formatadata = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataatual = formatadata.format(data);


        if(!mensagem.isEmpty()){

            /*if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{*/


                if (Util.statusInternet_MoWi(getBaseContext())){



                    enviarFirebase(servico,mensagem,dataatual);


                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
            }

        else{

            Toast.makeText(getBaseContext(),"Insira uma mensagem para envio de push.",Toast.LENGTH_LONG).show();
        }

    }





    private void enviarFirebase(String servico,String mensagem, String dataatual){

/// a lógica tem q estar aqui, procurar fazer um loop enviando mensagens

       // Agendamento agendamento = new Agendamento(nome,contato,whatsApp,email,servico,funcionario);
        PushModel pushModel = new PushModel(servico, mensagem, dataatual);


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"dialog");



        //loop cliente

        //for(int count=10 ; count >= 1; count--)

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference referencepush = firebaseDatabase.getReference().child("Push").child("Data")
                .child(dataatual).child("Serviço").child(servico);


        dbRef = FirebaseDatabase.getInstance().getReference("Cliente").child("Servico")
                .child(servico);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                clientelista.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot clienteSnap : snapshot.getChildren()) {
                        ClienteModel clienteData = clienteSnap.getValue(ClienteModel.class);
                        clientelista.add(clienteData);
                    }
                }
                for (int position=0;position<clientelista.size();position++) {
                    String contato= String.valueOf(clientelista.get(position).getContato());

                    TwilioService.sendSms(contato, mensagem);

                    if (position==(clientelista.size()-1)){

                        referencepush.setValue(pushModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    dialogProgress.dismiss();
                                    Toast.makeText(getBaseContext(),"Sucesso ao Enviar Push",Toast.LENGTH_LONG).show();
                                    finish();

                                }else{

                                    dialogProgress.dismiss();
                                    Toast.makeText(getBaseContext(),"Falha ao Enviar Push",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle onCancelled event
            }
        });




    }



        //loop cliente



    }











