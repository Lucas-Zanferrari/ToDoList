package br.senai.sp.informatica.todolist.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import br.senai.sp.informatica.todolist.dao.ListaDAO;
import br.senai.sp.informatica.todolist.modelo.ItemLista;
import br.senai.sp.informatica.todolist.modelo.Lista;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;

@RestController
public class ListaRestController {
	
	@Autowired
	private ListaDAO dao;

	@RequestMapping(value="/lista", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_UTF8_VALUE) 
	public ResponseEntity<Lista> inserir(@RequestBody String strLista) {

		//Criação das referências que serão utilizadas durante a tentativa de salvar a lista no BD
		Lista lista = new Lista();
		List<ItemLista> itens = new ArrayList<>();

		try {
			JSONObject jsonObj = new JSONObject(strLista);
			lista.setTitulo(jsonObj.getString("titulo"));
			JSONArray jsonItens = jsonObj.getJSONArray("itens");
			
			for(int i=0; i<jsonItens.length(); i++){
				ItemLista item = new ItemLista();
				item.setDescricao(jsonItens.getString(i));
				item.setLista(lista);
				itens.add(item);
			}

			//insere a lista de tarefas no banco de dados
			lista.setItens(itens);
			dao.inserir(lista);

			//preparando a resposta nos padrões de URI RESTful
			URI location = new URI("/lista/"+lista.getId());
			return ResponseEntity.created(location).body(lista);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/lista", method=RequestMethod.GET)
	public List<Lista> listar(){
		return dao.listar();
	}

	@RequestMapping(value = "/lista/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> excluir(@PathVariable("id") Long idLista){
		dao.excluir(idLista);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/item/{idItem}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluirItem(@PathVariable Long idItem){
		dao.excluirItem(idItem);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(value = "/lista/{idLista}", method = RequestMethod.GET)
	public Lista buscarListaPorID(@PathVariable Long idLista){
		Lista lista = dao.buscaListaPorID(idLista);
		return lista;
	}
}
