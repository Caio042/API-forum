package com.caiolima.Forum.repository;

import com.caiolima.Forum.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    List<Topico> findByCurso_Nome(String nomeCurso);
}
