package br.ufrpe.wanderlustapp.usuario.dominio;

import br.ufrpe.wanderlustapp.pessoa.dominio.Pessoa;

public class Usuario {
    private long id;
    private String email;
    private String senha;
    private Pessoa pessoa;
    private TipoUsuario tipoUsuario;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoUsuario() {
        return tipoUsuario.getTipoUsuario();
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario.setTipoUsuario(tipoUsuario);
    }
}
