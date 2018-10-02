package com.android.projeto.handsoff.domain;

import com.google.firebase.database.Exclude;

public class Usuario {

    private String userId;
    private String name;
    private String email;
    private String senha;
    private String telefone;
    private String celular;
    private String estado;
    private String status;
    /*private long timestamp;
    private Map<String, String> MapMoment = ServerValue.TIMESTAMP;*/

    public Usuario() {
    }

    /*    public List<Status> getStatus() {
            return status;
        }

        public void setStatus(List<Status> status) {
            this.status = status;
        }

        public Map<String, String> getMapMoment() {
            return MapMoment;
        }

        public void setMapMoment(long Moment) {
            timestamp = Moment;
        }

        @Exclude
        public Date getMoment() {
            return new Date(timestamp);
        }*/

    public Usuario(String userId, String name, String email, String senha, String telefone, String celular, String estado, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.celular = celular;
        this.estado = estado;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
