package com.ws.algamoneyapi.dto;

import com.ws.algamoneyapi.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoEstatisticaDia {

    private TipoLancamento tipo;
    private LocalDate dia;
    private BigDecimal total;

    public LancamentoEstatisticaDia(TipoLancamento tipo, LocalDate dia, BigDecimal total) {
        this.tipo = tipo;
        this.dia = dia;
        this.total = total;
    }

    public TipoLancamento getTipo() {
        return tipo;
    }

    public LocalDate getDia() {
        return dia;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
