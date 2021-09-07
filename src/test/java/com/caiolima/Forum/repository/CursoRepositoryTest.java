package com.caiolima.Forum.repository;


import com.caiolima.Forum.model.Curso;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles ("test")
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void deveriaCarregarUmCursoPeloNome(){

        Curso html = new Curso();
        html.setNome("html");
        html.setCategoria("web");
        entityManager.persist(html);

        Curso cursoRetiradoDoDb = cursoRepository.findByNome("html");

        Assertions.assertNotNull(cursoRetiradoDoDb);
        Assertions.assertEquals("html", cursoRetiradoDoDb.getNome());
    }

    @Test
    public void naoDeveriaCarregarUmCursoQueNaoExiste(){
        Curso curso = cursoRepository.findByNome("java");

        Assertions.assertNull(curso);
    }
}
