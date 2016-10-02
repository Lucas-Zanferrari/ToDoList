package br.senai.sp.informatica.todolist.controller;

import br.senai.sp.informatica.todolist.dao.UsuarioDAO;
import br.senai.sp.informatica.todolist.modelo.Usuario;
import com.auth0.jwt.JWTSigner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.HashMap;

/**
 * Created by lucaszanferrari on 10/1/16.
 */
@RestController
public class UsuarioRestController {

    @Autowired
    private UsuarioDAO dao;

    public static final String SECRET = "todolistsenai";
    public static final String ISSUER = "http://www.sp.senai.br";

    @RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Usuario> inserir(@RequestBody Usuario usuario) {
        try {
            dao.inserir(usuario);
            URI location = new URI("/item/"+usuario.getId());
            return ResponseEntity.created(location).body(usuario);
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> logar(@RequestBody Usuario usuario) {
        try {
            usuario = dao.logar(usuario);
            if(usuario != null) {
                //issued at...
                Long iat = System.currentTimeMillis() / 1000;
                //expiry time...
                Long exp = iat + 60;

                //objeto para gerar o token utilizando o SECRET global dessa classe
                JWTSigner signer = new JWTSigner(SECRET);

                HashMap<String, Object> claims = new HashMap<>();
                claims.put("iss", ISSUER);
                claims.put("exp", exp);
                claims.put("iat", iat);
                claims.put("id_usuario", usuario.getId());

                //gera o token
                String jwt = signer.sign(claims);

                JSONObject output = new JSONObject();
                output.put("token", jwt);

                return ResponseEntity.ok(output.toString());
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

