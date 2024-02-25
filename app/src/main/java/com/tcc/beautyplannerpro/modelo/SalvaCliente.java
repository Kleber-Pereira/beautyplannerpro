package com.tcc.beautyplannerpro.modelo;

public class SalvaCliente {
    private String nome;
    private String contato;
    private String email;
    private String servico;


    public SalvaCliente(){}

    public SalvaCliente(String nome, String contato, String email, String servico) {
        this.nome = nome;
        this.contato = contato;
        this.email = email;
        this.servico = servico;

    }

   /* public SalvaCliente(String nome, String contato, String email, String servico) {
    }*/


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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String email) {
        this.servico = servico;
    }




}
