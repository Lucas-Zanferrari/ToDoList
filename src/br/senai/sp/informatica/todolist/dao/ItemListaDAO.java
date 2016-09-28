package br.senai.sp.informatica.todolist.dao;

import br.senai.sp.informatica.todolist.modelo.ItemLista;
import br.senai.sp.informatica.todolist.modelo.Lista;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ItemListaDAO {
    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public void marcarFeito(Long idItem, boolean valor){
        ItemLista item = manager.find(ItemLista.class, idItem);
        item.setFeito(valor);
        manager.merge(item);
    }

    @Transactional
    public void inserir(Long idLista, ItemLista item){
        item.setLista(manager.find(Lista.class, idLista));
        manager.persist(item);
    }

    @Transactional
    public ItemLista buscaItemPorID(Long idItem){
        return manager.find(ItemLista.class, idItem);
    }
}
