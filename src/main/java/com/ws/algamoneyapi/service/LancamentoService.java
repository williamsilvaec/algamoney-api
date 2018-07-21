package com.ws.algamoneyapi.service;

import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.model.Pessoa;
import com.ws.algamoneyapi.repository.LancamentoRepository;
import com.ws.algamoneyapi.repository.PessoaRepository;
import com.ws.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LancamentoService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private LancamentoRepository lancamentoRepository;

    public Lancamento salvar(Lancamento lancamento) {
        validarPessoa(lancamento);

        return lancamentoRepository.save(lancamento);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
        Lancamento lancamentoSalvo = buscarLancamentoPeloCodigo(codigo);

        validarPessoa(lancamento);

        BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
        return lancamentoRepository.save(lancamentoSalvo);
    }

    private void validarPessoa(Lancamento lancamento) {
        Pessoa pessoa = null;

        if (lancamento.getPessoa().getCodigo() != null) {
            pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
        }

        if (pessoa == null || pessoa.isInativa()) {
            throw new PessoaInexistenteOuInativaException();
        }
    }

    private Lancamento buscarLancamentoPeloCodigo(Long codigo) {
        Lancamento lancamento = lancamentoRepository.findOne(codigo);
        if (lancamento == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return lancamento;
    }
}
