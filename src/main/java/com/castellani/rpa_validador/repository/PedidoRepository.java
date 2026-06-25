package com.castellani.rpa_validador.repository;

import com.castellani.rpa_validador.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}