package br.com.brenoborges.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.brenoborges.todolist.users.iUsersRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * Toda classe que o Spring vai gerenciar preciso colocar a Annotation, seja
 * Component, RestController, etc.
 * 
 * Toda requisição antes de ir para a rota vai passar primeiro pelo filtro que é
 * essa classe.
 */
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private iUsersRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        // Direciona para a rota que eu quero, se não for ela, vida que segue.
        if (servletPath.startsWith("/tasks/")) {
            /**
             * Pegar a autenticação (usuario e senha)
             */
            var authorization = request.getHeader("Authorization");

            // Substring utilizado para extrair uma parte de um texto/conteúdo.
            // Após pegar o conteúdo que quero tirar, o .trim() serve para remover todos os
            // espaços restantes.
            var authEncoded = authorization.substring("Basic".length()).trim();

            // Convertendo váriavel extraida em um array de byte
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            // Convertemdo o array em uma String.
            var AuthString = new String(authDecoded);

            // Divite a String em : e ai posso pegar o usuario e senha dentro do array.
            String[] credentials = AuthString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            /**
             * Validar se o usuario existe
             */
            var user = this.iUserRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
            } else {
                /**
                 * Validar a senha
                 */
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                // .verified retorna um boolean
                if (passwordVerify.verified) {

                    /**
                     * Segue viagem
                     */

                    // Setando o atributo idUser na request com o id do usuário.
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }

            }
        } else {
            filterChain.doFilter(request, response);
        }

    }

}
