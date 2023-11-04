package br.com.brenoborges.todolist.task;

import java.util.UUID;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iTaskRepository extends JpaRepository<TaskModel, UUID> {
    List<TaskModel> findByIdUser(UUID idUser);
}
