package com.ws.algamoneyapi.repository.lancamento;

import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
}
