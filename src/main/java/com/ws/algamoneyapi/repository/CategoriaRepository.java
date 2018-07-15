package com.ws.algamoneyapi.repository;

import com.ws.algamoneyapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
