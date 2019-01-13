package com.ws.algamoneyapi.resource;

import com.ws.algamoneyapi.dto.LancamentoEstatisticaCategoria;
import com.ws.algamoneyapi.dto.LancamentoEstatisticaDia;
import com.ws.algamoneyapi.event.RecursoCriadoEvent;
import com.ws.algamoneyapi.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.ws.algamoneyapi.model.Lancamento;
import com.ws.algamoneyapi.repository.LancamentoRepository;
import com.ws.algamoneyapi.repository.filter.LancamentoFilter;
import com.ws.algamoneyapi.repository.projection.ResumoLancamento;
import com.ws.algamoneyapi.service.LancamentoService;
import com.ws.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
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

    @GetMapping("/estatisticas/por-categoria")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<LancamentoEstatisticaCategoria> porCategoria() {
        return lancamentoRepository.porCategoria(LocalDate.now());
    }

    @GetMapping("/estatisticas/por-dia")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public List<LancamentoEstatisticaDia> porDia() {
        return lancamentoRepository.porDia(LocalDate.now());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    @GetMapping("{codigo}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
    public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo) {
        Lancamento lancamento = lancamentoRepository.findOne(codigo);
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> cadastrar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @DeleteMapping("{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('write')")
    public void remover(@PathVariable Long codigo) {
        lancamentoRepository.delete(codigo);
    }

    @ExceptionHandler({ PessoaInexistenteOuInativaException.class })
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagemUsuario = messageSource
                .getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());

        String mensagemDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
        return ResponseEntity.badRequest().body(erros);
    }

    @PutMapping("{codigo}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long codigo, @RequestBody @Valid Lancamento lancamento) {
        Lancamento lancamentoSalvo = lancamentoService.atualizar(codigo, lancamento);
        return ResponseEntity.ok(lancamentoSalvo);
    }
}
