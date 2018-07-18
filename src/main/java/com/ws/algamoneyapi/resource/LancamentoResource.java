package com.ws.algamoneyapi.resource;

import com.ws.algamoneyapi.event.RecursoCriadoEvent;
import com.ws.algamoneyapi.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.repository.LancamentoRepository;
import com.ws.algamoneyapi.repository.filter.LancamentoFilter;
import com.ws.algamoneyapi.service.LancamentoService;
import com.ws.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    private final LancamentoRepository lancamentoRepository;
    private final ApplicationEventPublisher publisher;
    private final LancamentoService lancamentoService;
    private final MessageSource messageSource;

    @Autowired
    public LancamentoResource(LancamentoRepository lancamentoRepository,
                              ApplicationEventPublisher publisher,
                              LancamentoService lancamentoService,
                              MessageSource messageSource) {
        this.lancamentoRepository = lancamentoRepository;
        this.publisher = publisher;
        this.lancamentoService = lancamentoService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public List<Lancamento> pesquisar(LancamentoFilter lancamentoFilter) {
        return lancamentoRepository.filtrar(lancamentoFilter);
    }

    @GetMapping("{codigo}")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Lancamento lancamento = lancamentoRepository.findOne(codigo);
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @ExceptionHandler({ PessoaInexistenteOuInativaException.class })
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource
                .getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());

        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }
}
