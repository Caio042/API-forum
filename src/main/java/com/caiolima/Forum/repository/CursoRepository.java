package com.caiolima.Forum.repository;

import com.caiolima.Forum.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
    public Curso findByNome(String nome);
}
