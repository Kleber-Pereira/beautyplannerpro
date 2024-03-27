package com.tcc.beautyplannerpro.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tcc.beautyplannerpro.R;
import com.tcc.beautyplannerpro.cliente.ClienteModel;
import com.tcc.beautyplannerpro.databinding.ActivityMainBinding;
import com.tcc.beautyplannerpro.modelo.Agendamento;
import com.tcc.beautyplannerpro.modelo.TwilioService;
import com.tcc.beautyplannerpro.util.DialogProgress;
import com.tcc.beautyplannerpro.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



import java.util.ArrayList;

public class AgendamentoServicoActivity extends AppCompatActivity implements View.OnClickListener  {



    private EditText editText_Nome;
    private EditText editText_NumeroContato;
    private CheckBox checkBox_WhatsApp;
    private EditText editText_Email;
    /*private CheckBox checkBox_Barba;
    private CheckBox checkBox_Cabelo;*/
    private TextView editText_Servico;
    private TextView editText_Funcionario;
    private CardView cardView_Agendar;

    private String funcoesservicoNome;
    private String funcoesfuncionarioNome;
    ActivityMainBinding binding;


    private ArrayList<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento_servico);



        data = getIntent().getStringArrayListExtra("data");
        funcoesservicoNome = getIntent().getStringExtra("servicoservico");
        funcoesfuncionarioNome = getIntent().getStringExtra("funcionarioNome");



        editText_Nome = (EditText)findViewById(R.id.editText_AgendamentoServico_Nome);
        editText_NumeroContato = (EditText)findViewById(R.id.textView_AgendamentoServico_Numero);
        /*final EditText editText_NumeroContato = (EditText) findViewById(R.id.textView_AgendamentoServico_Numero);
        editText_NumeroContato.addTextChangedListener(Mascara.insert("(##)#####-####", editText_NumeroContato));
*/
        checkBox_WhatsApp = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_WhatsApp);
        editText_Email = (EditText)findViewById(R.id.editText_AgendamentoServico_Email);
        /*checkBox_Barba = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_barba);
        checkBox_Cabelo = (CheckBox)findViewById(R.id.checkbox_AgendamentoServico_Cabelo);
       */
        editText_Servico = (TextView)findViewById(R.id.editText_Servico);
        editText_Funcionario = (TextView)findViewById(R.id.editText_Funcionario);
        cardView_Agendar = (CardView)findViewById(R.id.cardView_AgendamentoServico_Agendar);



        cardView_Agendar.setOnClickListener(this);

        editText_Servico.setText(funcoesservicoNome);
        editText_Funcionario.setText(funcoesfuncionarioNome);

    }


    //----------------------------------ACAO DE CLICK--------------------------------------------------

    @Override
    public void onClick(View view) {


        switch (view.getId()){


            case R.id.cardView_AgendamentoServico_Agendar:

                agendar();

                break;

        }

    }


    //----------------------------------AGENDAR NO FIREBASE-------------------------------------------------


    private void agendar(){


        String nome = editText_Nome.getText().toString();
        String contato = editText_NumeroContato.getText().toString();
        boolean whatsApp = checkBox_WhatsApp.isChecked();
        String email = editText_Email.getText().toString();
       /* boolean barba = checkBox_Barba.isChecked();
        boolean cabelo = checkBox_Cabelo.isChecked();*/
        String servico = editText_Servico.getText().toString();
        String funcionario = editText_Funcionario.getText().toString();





        if(!nome.isEmpty()){

            /*if (!cabelo && !barba){

                Toast.makeText(getBaseContext(),"Escolha qual serviço gostaria de Agendar.",Toast.LENGTH_LONG).show();

            }else{*/



                if (Util.statusInternet_MoWi(getBaseContext())){



                    agendarFirebase(nome,contato,whatsApp,email,servico,funcionario);


                }else{

                    Toast.makeText(getBaseContext(),"Erro - Verifique sua conexão com a internet.",Toast.LENGTH_LONG).show();

                }
            }

        else{

            Toast.makeText(getBaseContext(),"Insira seu nome para Agendar.",Toast.LENGTH_LONG).show();
        }

    }





    private void agendarFirebase(String nome,String contato, boolean whatsApp, String email, String servico, String funcionario){



        Agendamento agendamento = new Agendamento(nome,contato,whatsApp,email,servico,funcionario);
        ClienteModel clientemodel = new ClienteModel(nome,contato,email,servico);


        final DialogProgress dialogProgress = new DialogProgress();

        dialogProgress.show(getSupportFragmentManager(),"dialog");


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0)).child(funcoesservicoNome).child(funcoesfuncionarioNome);

        /*DatabaseReference reference = firebaseDatabase.getReference().child("BD").child("Calendario")
                .child("HorariosAgendados").child(data.get(2)).child("Mes").child(data.get(1))
                .child("dia").child(data.get(0));*/


        reference.child(data.get(3)).setValue(agendamento).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


                if (task.isSuccessful()){

                    DatabaseReference referencecliente = firebaseDatabase.getReference().child("Cliente").child("Servico")
                            .child(funcoesservicoNome).child(contato);
                    referencecliente.setValue(clientemodel);

                    //----twillo---


                    String telefone = contato;
                    String nome = editText_Nome.getText().toString();
                    String servico = editText_Servico.getText().toString();
                    String funcionario = editText_Funcionario.getText().toString();
                    String dia = (data.get(0) +"/"+data.get(1)+"/"+data.get(2));
                    String horario = (data.get(3));
                    // String accountSid = System.getenv("AC6abb957b4af10ab40428285f56f58add");
                    // String authToken = System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025");
                    /*Twilio.init(accountSid, authToken);
                    Message message = Message.creator(
                            new PhoneNumber(telefone),"MG3e0104f57d45974fd589e55519f447be",
                            nome + ", o serviço "+ servico + " com o profissional "+ funcionario +
                    " do dia " + data.get(0) +"/"+data.get(1)+"/"+data.get(2)+
                    " às "+ data.get(3) +" foi agendado com sucesso pelo BeautyPlanner!"
                    ).create();
                    System.out.println(message.getSid());*/

                   /* Twilio.init(System.getenv("AC6abb957b4af10ab40428285f56f58add"),
                            System.getenv("7226ace1dceb8c3f4f9edf35dd7fe025"));
                    Message message = Message.creator(
                            new PhoneNumber("+5513974230860"),
                            new PhoneNumber("+12563848116"),
                            "teste").create();


                    System.out.println(message.getSid());*/
                    String mensagem = (nome + ", o serviço "+ servico + " com o profissional "
                            + funcionario +
                            " no dia "+ dia +
                            " às "+ horario +" foi agendado com sucesso pelo BeautyPlanner!").toString();

                    TwilioService.sendSms(telefone, mensagem);

                   /*TwilioService.sendSms(telefone, nome + ", o serviço "+ servico + " com o profissional "+ funcionario +
                            " no dia "+ dia +
                            " às "+ horario +" foi agendado com sucesso pelo BeautyPlanner!");*/

                    //TwilioService.sendSms(telefone, "agendado com sucesso pelo BeautyPlanner!");



                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Sucesso ao Agendar",Toast.LENGTH_LONG).show();
                    finish();

                }else{

                    dialogProgress.dismiss();
                    Toast.makeText(getBaseContext(),"Falha ao Agendar",Toast.LENGTH_LONG).show();

                }



            }
        });

    }










}
