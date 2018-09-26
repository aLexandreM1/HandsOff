package com.android.projeto.handsoff.domain;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.Map;

public class Usuario {

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String celular;
    private String cidade;
    private long timestamp;
    private Map<String, String> MapMoment = ServerValue.TIMESTAMP;

    public Usuario(){}

    public Usuario(String nome, String email, String senha, String telefone, String celular, String cidade) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.celular = celular;
        this.cidade = cidade;
    }

    public Map<String, String> getMapMoment(){
        return MapMoment;
    }

    public void setMapMoment(long Moment){
        timestamp = Moment;
    }

    @Exclude
    public Date getMoment(){
        return new Date(timestamp);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
