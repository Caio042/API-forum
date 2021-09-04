package com.caiolima.Forum.controller;

import com.caiolima.Forum.controller.dto.AtualizacaoTopicoForm;
import com.caiolima.Forum.controller.dto.TopicoDetalhadoDto;
import com.caiolima.Forum.controller.dto.TopicoDto;
import com.caiolima.Forum.controller.dto.TopicoForm;
import com.caiolima.Forum.model.Topico;
import com.caiolima.Forum.repository.CursoRepository;
import com.caiolima.Forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping ("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable (value = "listaDeTopicos")
    public Page<TopicoDto> listar(@RequestParam(required = false) String nomeCurso,
                                  @PageableDefault(sort = "dataCriacao", direction = Sort.Direction.ASC,
                                  page = 0, size = 10) Pageable paginacao){
        //TODO tratar erros de paginação
        Page<Topico> topicos;

        if(nomeCurso == null) {
            topicos = topicoRepository.findAll(paginacao);
        }
        else{
            topicos = topicoRepository.findByCurso_Nome(nomeCurso, paginacao);
        }
        return TopicoDto.converter(topicos);
    }


    @GetMapping ("/{id}")
    public ResponseEntity<TopicoDetalhadoDto> detalhar(@PathVariable Long id){

        return topicoRepository.findById(id)
                .map(topico -> ResponseEntity.ok(new TopicoDetalhadoDto(topico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    @CacheEvict (value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuild){
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);

        URI uri = uriBuild.path("/topicos/{id}")
                .buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDto(topico));
    }

    @PutMapping ("/{id}")
    @Transactional
    @CacheEvict (value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, AtualizacaoTopicoForm form){
        if (topicoRepository.existsById(id)) {
            Topico topico = form.atualizar(id, topicoRepository);
            return ResponseEntity.ok(new TopicoDto(topico));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping ("/{id}")
    @Transactional
    @CacheEvict (value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        if (topicoRepository.existsById(id)){
            topicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
