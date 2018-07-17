package com.ws.algamoneyapi.resource;

import com.ws.algamoneyapi.event.RecursoCriadoEvent;
import com.ws.algamoneyapi.model.Pessoa;
import com.ws.algamoneyapi.repository.PessoaRepository;
import com.ws.algamoneyapi.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

    private final PessoaRepository pessoaRepository;
    private final ApplicationEventPublisher publisher;
    private final PessoaService pessoaService;

    @Autowired
    public PessoaResource(PessoaRepository pessoaRepository, ApplicationEventPublisher publisher, PessoaService pessoaService) {
        this.pessoaRepository = pessoaRepository;
        this.publisher = publisher;
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<Pessoa> cadastrar(@RequestBody @Valid Pessoa pessoa, HttpServletResponse response) {
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
    }

    @GetMapping("{codigo}")
    public ResponseEntity<Pessoa> buscarPeloCodigo(@PathVariable Long codigo) {
        Pessoa pessoa = pessoaRepository.findOne(codigo);
        return pessoa != null ? ResponseEntity.ok(pessoa) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long codigo) {
        pessoaRepository.delete(codigo);
    }

    @PutMapping("{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @RequestBody @Valid Pessoa pessoa) {
        Pessoa pessoaSalva = pessoaService.atualizar(codigo, pessoa);
        return ResponseEntity.ok(pessoaSalva);
    }

    @PutMapping("{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
        pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
    }
}
