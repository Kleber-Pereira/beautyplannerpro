package com.tcc.beautyplannerpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tcc.beautyplannerpro.fragment.FragmentCalendario;
import com.tcc.beautyplannerpro.fragment.FragmentHome;
import com.tcc.beautyplannerpro.funcoes.FuncoesFragment;
import com.tcc.beautyplannerpro.util.Permissao;
import com.tcc.beautyplannerpro.funcionario.FuncionarioFragment;
import com.tcc.beautyplannerpro.Servicos.ServicosFragment;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;

    private FragmentHome fragmentHome;
    private FragmentCalendario fragmentCalendario;
    private ServicosFragment fragmentServico;
    private FuncionarioFragment fragmentFuncionario;
    private FuncoesFragment fragmentFuncao;


    private Fragment fragment;
    private FragmentManager fragmentManager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);


        navigationBottom();


        fragmentHome = new FragmentHome();
        fragmentCalendario = new FragmentCalendario();
        fragmentServico= new ServicosFragment();
        fragmentFuncionario = new FuncionarioFragment();
        fragmentFuncao = new FuncoesFragment();

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.frameLayout_Fragment,fragmentHome).commit();

        permissao();

    }

    private void permissao(){


        String permissoes[] = new String[]{

                android.Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_CONTACTS
        };


        Permissao.validate(this,333,permissoes);


    }

    private void navigationBottom(){


        onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.item_navegacao_home:

                        fragment = fragmentHome;
                        break;

                    case R.id.item_navegacao_calendario:

                        fragment = fragmentCalendario;
                        break;

                    case R.id.item_navegacao_Servicos:

                        fragment = fragmentServico;
                        break;

                    case R.id.item_navegacao_Funcionario:

                        fragment = fragmentFuncionario;
                        break;

                    case R.id.item_navegacao_Funcao:

                        fragment = fragmentFuncao;
                        break;


                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_Fragment,fragment).commit();
                return true;
            }
        };

        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


    }
}