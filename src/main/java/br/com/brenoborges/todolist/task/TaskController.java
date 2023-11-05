package br.com.brenoborges.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.brenoborges.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private iTaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        // HttpServletRequest sendo chamado para recuperar o atributo que passei na
        // Request.
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser); // Setando o idUser na taskModel fazendo um casting.

        var currentDate = LocalDateTime.now();
        // Verifica se a data atual é maior que a data de inicio ou menor que a data de
        // término da task
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data atual superior a data de inicio ou inferior a de término!");
        }

        // Verifica se a data de inicio é menor que a data de término da task
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Data de início deve ser menor que a data de término!");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    /**
     * Método que lista as tarefas de um usuário especifico.
     * 
     * @param request
     * @return lista de tasks
     */
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);

        return tasks;
    }

    /**
     * Método para atualizar uma task
     * {id} será substituido pela id da task no PathVariable que passo nos
     * parametros do metodo.
     * 
     * @param taskModel
     * @param request
     * @param id
     */
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request) {
        // Procuro o id usando o método na classe Utils e se não retornar nada, mando um
        // null.
        var task = this.taskRepository.findById(id).orElse(null);

        // Verifica se a tarefa existe
        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada!");
        }

        var idUser = request.getAttribute("idUser");
        // Verifica se o idUsuario é diferente do idUsuario que está tentando alterar a
        // task.
        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário sem permissão para alterar essa tarefa!");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }
}
