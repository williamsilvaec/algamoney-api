package com.ws.algamoneyapi.repository;

import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
