package com.caiolima.Forum.controller.dto;

import com.caiolima.Forum.model.Curso;
import com.caiolima.Forum.model.Topico;
import com.caiolima.Forum.repository.CursoRepository;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicoForm {

    private String titulo;

    private String mensagem;

    private String nomeDoCurso;


    public Topico converter(CursoRepository cursoRepository) {
        Curso curso = cursoRepository.findByNome(nomeDoCurso);

        return new Topico(titulo, mensagem, curso);
    }
}
