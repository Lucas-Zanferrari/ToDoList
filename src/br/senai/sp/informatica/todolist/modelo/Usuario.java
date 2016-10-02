package br.senai.sp.informatica.todolist.modelo;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by lucaszanferrari on 10/1/16.
 */
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @NotNull
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String md5Password = encoder.encodePassword(senha, null);
        this.senha = md5Password;
    }
}
