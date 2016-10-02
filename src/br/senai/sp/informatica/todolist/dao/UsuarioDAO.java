package br.senai.sp.informatica.todolist.dao;

import br.senai.sp.informatica.todolist.modelo.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by lucaszanferrari on 10/1/16.
 */
@Repository
public class UsuarioDAO {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public void inserir(Usuario usuario) {
        manager.persist(usuario);
    }

    @Transactional
    public Usuario logar(Usuario usuario) {
        /*:
        <nome_da_variavel>, como em :login e :senha, insere uma variavel na query que terá seu valor
        atribuído mais para frente
         */
        TypedQuery<Usuario> query = manager.createQuery("select u from Usuario u where u.login = :login and u.senha = :senha", Usuario.class);
        query.setParameter("login", usuario.getLogin());
        query.setParameter("senha", usuario.getSenha());
        try {
            return query.getSingleResult();
        }
        catch(NullPointerException e) {
            //o query.getSingleResult() retorna NullPointerException se a query não retornar nenhum resultado do banco de dados
            return null;
        }
    }


}
