package com.ws.algamoneyapi.service;

import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.model.Pessoa;
import com.ws.algamoneyapi.repository.LancamentoRepository;
import com.ws.algamoneyapi.repository.PessoaRepository;
import com.ws.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento) {
        Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        if (pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }
        return lancamentoRepository.save(lancamento);
    }
}
