package com.ws.algamoneyapi.repository.lancamento;

import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.repository.filter.LancamentoFilter;
import com.ws.algamoneyapi.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LancamentoRepositoryQuery {

    Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

    Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
