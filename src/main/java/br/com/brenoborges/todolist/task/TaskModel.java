package br.com.brenoborges.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;
    @Column(length = 50) // Limitando o tamanho máximo da coluna title em 50 caracteres
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;

    /**
     * Metodo que trata exceção de titulo.
     * Quando passo a exceção na declaração do método, sempre que chamam esse método
     * tem que fazer a tratativa.
     * 
     * @param title
     * @throws Exception
     */
    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("O campo de titulo deve conter no máx 50 caracteres!");
        }
        this.title = title;
    }
}
