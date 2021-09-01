package com.caiolima.Forum.controller;

import com.caiolima.Forum.controller.dto.TopicoDto;
import com.caiolima.Forum.controller.dto.TopicoForm;
import com.caiolima.Forum.model.Curso;
import com.caiolima.Forum.model.Topico;
import com.caiolima.Forum.repository.CursoRepository;
import com.caiolima.Forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping ("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<TopicoDto> listar(String nomeCurso){

        List<Topico> topicos;

        if(nomeCurso == null) {
            topicos = topicoRepository.findAll();
        }
        else{
            topicos = topicoRepository.findByCurso_Nome(nomeCurso);
        }
        return TopicoDto.converter(topicos);
    }

    @PostMapping
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm topicoForm, UriComponentsBuilder uriBuild){
        Topico topico = topicoForm.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuild.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }
}
