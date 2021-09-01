package com.caiolima.Forum.controller.dto;

import com.caiolima.Forum.model.Topico;
import com.caiolima.Forum.repository.TopicoRepository;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AtualizacaoTopicoForm {

    @NotBlank
    @Size(min = 5, max = 100)
    private String titulo;

    @NotBlank
    @Size (min = 5, max = 255)
    private String mensagem;


    public Topico atualizar(Long id, TopicoRepository topicoRepository) {
        Topico topico = topicoRepository.findById(id).get();
        topico.setTitulo(this.titulo);
        topico.setMensagem(this.mensagem);

        return topico;
    }
}
