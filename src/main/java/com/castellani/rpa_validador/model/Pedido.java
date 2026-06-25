package com.castellani.rpa_validador.model;

import jakarta.persistence.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.math.BigDecimal;

@Entity
@Table(name = "pedidos_processados")

public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String cliente;
    private String produto;
    private BigDecimal valor;

    public Pedido() {}

    public Pedido(String codigo, String cliente, String produto, BigDecimal valor) {
        this.codigo = codigo;
        this.cliente = cliente;
        this.produto = produto;
        this.valor = valor;
    }

    public Long getId() {return id;}
    public String getCodigo() { return codigo; }
    public String getCliente() { return cliente; }
    public String getProduto() { return produto; }
    public BigDecimal getValor() { return valor; }

}