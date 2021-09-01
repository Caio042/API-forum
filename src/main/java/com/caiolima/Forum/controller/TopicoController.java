package com.caiolima.Forum.controller;

import com.caiolima.Forum.controller.dto.AtualizacaoTopicoForm;
import com.caiolima.Forum.controller.dto.TopicoDetalhadoDto;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    @Transactional
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuild){
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuild.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @GetMapping ("/{id}")
    public ResponseEntity<TopicoDetalhadoDto> detalhar(@PathVariable Long id){

        return topicoRepository.findById(id)
                .map(topico -> ResponseEntity.ok(new TopicoDetalhadoDto(topico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping ("/{id}")
    @Transactional
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, AtualizacaoTopicoForm form){
        if (topicoRepository.existsById(id)) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping ("/{id}")
    @Transactional
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if (topicoRepository.existsById(id)){
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
