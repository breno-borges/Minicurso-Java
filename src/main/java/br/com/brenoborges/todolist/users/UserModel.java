package br.com.brenoborges.todolist.users;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * @Data sendo usado para criar todos getters e setters da classe.
 * @Entity sendo usado para definir que essa classe é uma entidade no BD.
 */
@Data
@Entity(name = "tb_users")
public class UserModel {

    /**
     * @Id sendo usado para definir que esse atributo é o Id.
     * @GeneretadeValue ser para que o JPA gere o Id de forma automática.
     */
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    /**
     * Definindo que a cluna username é tipo unique no BD
     */
    @Column(unique = true)
    private String username;
    private String name;
    private String password;

    /**
     * Serve para quando o dado for gerado, o banco de dados tenha a informação de
     * quando foi criado.
     */
    @CreationTimestamp
    private LocalDateTime createdAt;

}
