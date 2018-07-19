package com.ws.algamoneyapi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permissao")
@Getter
@Setter
@EqualsAndHashCode(of = "codigo")
public class Permissao {

    @Id
    private Long codigo;

    private String descricao;
}
