package com.caiolima.Forum.controller.dto;

import com.caiolima.Forum.model.Curso;
import com.caiolima.Forum.model.Topico;
import com.caiolima.Forum.repository.CursoRepository;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TopicoForm {

    @NotBlank
    @Size (min = 5, max = 100)
    private String titulo;

    @NotBlank
    @Size (min = 5, max = 255)
    private String mensagem;

    @NotBlank
    @Size (min = 2, max = 100)
    private String nomeDoCurso;


    public Topico converter(CursoRepository cursoRepository) {
        Curso curso = cursoRepository.findByNome(nomeDoCurso);

        return new Topico(titulo, mensagem, curso);
    }
}
