package br.com.brenoborges.todolist.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Annotation @RequestController serve para informar que a classe tem um
 * controller.
 * Annotation @RequesMapping serve para definir qual a rota dessa classe.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Annotation @AutoWired serve para informar ao Spring para ele gerenciar a
     * questão da instância do objeto, ciclo de vida, quando é new por exemplo.
     */
    @Autowired
    private iUsersRepository usersRepository;

    /**
     * Annotation @PostMapping serve para direcionar a rota do método POST.
     * ResponseEntity serve para ter retornos diferentes dentro da mesma requisição.
     * 
     * @param userModel
     */
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        /**
         * Váriavel que recebe o método de pesquisa se o usuário já existe
         */
        var user = this.usersRepository.findByUsername(userModel.getUsername());

        /**
         * Se o usuário pesquisado for nulo, quer dizer que já existe no banco
         */
        if (user != null) {
            /**
             * Retornar:
             * Mensagem de erro e Status Code de erro
             */
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado!");
        }

        /**
         * Biblioteca BCrypt usada para criptografar as senhas.
         * Variável de senha recebendo o método da biblioteca que passa como parâmetro
         * cost 12 que é da documentação e a senha sendo transformada em um Char.
         */
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);

        /**
         * Usuário criado recebe o método save que vem da interface iUsersRepository,
         * onde extende a classe JpaRepository que possui diversos métodos default.
         */
        var userCreated = this.usersRepository.save(userModel);

        // Retornando Status Code de usuário criado com sucesso.
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
