package br.com.brenoborges.todolist.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JPaRepository sendo usado pois já tem alguns métodos por padrão para se
 * comunicar com o BD.
 */
public interface iUsersRepository extends JpaRepository<UserModel, UUID> {

    /**
     * Método que vai buscar se o username já existe no BD
     * 
     * @param username
     * @return UserModel
     */
    UserModel findByUsername(String username);
}
