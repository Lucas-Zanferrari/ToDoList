package br.senai.sp.informatica.todolist.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import br.senai.sp.informatica.todolist.dao.ListaDAO;
import br.senai.sp.informatica.todolist.modelo.ItemLista;
import br.senai.sp.informatica.todolist.modelo.Lista;

@RestController
public class ListaRestController {
	
	@Autowired
	private ListaDAO dao;
	
	@Transactional
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
			URI location = new URI("/todo/"+lista.getId());
			return ResponseEntity.created(location).body(lista);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
}
