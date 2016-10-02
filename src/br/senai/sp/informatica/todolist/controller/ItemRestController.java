package br.senai.sp.informatica.todolist.controller;

import br.senai.sp.informatica.todolist.dao.ItemListaDAO;
import br.senai.sp.informatica.todolist.modelo.ItemLista;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * Created by lucaszanferrari on 9/24/16.
 */
@RestController
public class ItemRestController {

    @Autowired
    private ItemListaDAO dao;

    @RequestMapping(value="/item/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> marcarFeito(@PathVariable("id") Long idItem, @RequestBody String strFeito) {
        try {
            JSONObject jsonObj = new JSONObject(strFeito);
            boolean feito = jsonObj.getBoolean("feito");
            dao.marcarFeito(idItem, feito);

            //criando cabeçalho que estará na resposta
            HttpHeaders responseHeader = new HttpHeaders();
            URI location = new URI("/item/"+idItem);
            responseHeader.setLocation(location);

            return new ResponseEntity<>(responseHeader, HttpStatus.OK);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/lista/{idLista}/item", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ItemLista> addItem(@PathVariable Long idLista, @RequestBody ItemLista item) {
        try {
            dao.inserir(idLista, item);
            URI location = new URI("/item/"+item.getId());
            return ResponseEntity.created(location).body(item);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/item/{idItem}", method = RequestMethod.GET)
    public ItemLista buscarListaPorID(@PathVariable Long idItem){
        ItemLista item = dao.buscaItemPorID(idItem);
        return item;
    }
}
