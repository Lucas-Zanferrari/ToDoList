package br.senai.sp.informatica.todolist.dao;

import br.senai.sp.informatica.todolist.modelo.ItemLista;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Repository;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import br.senai.sp.informatica.todolist.modelo.Lista;
import java.util.List;

@Repository
public class ListaDAO {

	@PersistenceContext
	private EntityManager manager;

	@Transactional
	public void inserir(Lista lista) {
		manager.persist(lista);
	}

	public List<Lista> listar() {
		TypedQuery<Lista> query = manager.createQuery("select l from Lista l", Lista.class);
		return query.getResultList();
	}

	@Transactional
	public void excluir(Long idLista) {
		//busca uma entrada no banco através de sua chave primária
		Lista lista = manager.find(Lista.class, idLista);
		manager.remove(lista);
	}

	@Transactional
	public void excluirItem(Long idItem) {
		ItemLista item = manager.find(ItemLista.class, idItem);
		Lista lista = item.getLista();
		lista.getItens().remove(item);
		manager.merge(lista);
	}

	@Transactional
	public Lista buscaListaPorID(Long idLista) {
		return manager.find(Lista.class, idLista);
	}
}
