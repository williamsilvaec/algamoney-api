package com.ws.algamoneyapi.dto;

import com.ws.algamoneyapi.model.Categoria;

import java.math.BigDecimal;

public class LancamentoEstatisticaCategoria {

    private Categoria categoria;
    private BigDecimal total;

    public LancamentoEstatisticaCategoria(Categoria categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
