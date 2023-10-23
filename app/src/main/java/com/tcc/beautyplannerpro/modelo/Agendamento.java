package com.tcc.beautyplannerpro.modelo;

public class Agendamento {

    private String nome;
    private String contato;
    private boolean whatsApp;
    private String email;
    /*private boolean barba;
    private boolean cabelo;*/
    private String servico;
    private String funcionario;



    public Agendamento(){

    }


    public Agendamento(String nome, String contato, boolean whatsApp, String email,String servico, String funcionario ) {
        this.nome = nome;
        this.contato = contato;
        this.whatsApp = whatsApp;
        this.email = email;
        /*this.barba = barba;
        this.cabelo = cabelo;*/
        this.servico = servico;
        this.funcionario = funcionario;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public boolean isWhatsApp() {
        return whatsApp;
    }

    public void setWhatsApp(boolean whatsApp) {
        this.whatsApp = whatsApp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

}
