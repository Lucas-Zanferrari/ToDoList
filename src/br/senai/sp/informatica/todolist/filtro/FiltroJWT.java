package br.senai.sp.informatica.todolist.filtro;

import br.senai.sp.informatica.todolist.controller.UsuarioRestController;
import com.auth0.jwt.JWTVerifier;

import java.io.IOException;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

/**
 * Created by lucaszanferrari on 10/1/16.
 */

@WebFilter("/*")
public class FiltroJWT implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, javax.servlet.FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        //libera o acesso ao login
        if(request.getRequestURI().contains("login")) {
            chain.doFilter(req, resp);
            return ;
        }

        String token = request.getHeader("Authorization");

        try {
            JWTVerifier verifier = new JWTVerifier(UsuarioRestController.SECRET);
            Map<String, Object> claims = verifier.verify(token);
            System.out.println(claims);
            chain.doFilter(req, resp);
        }
        catch(Exception e) {
            if (token == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
            }
            else {
                response.sendError(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Override
    public void destroy() {

    }
}
