package com.castellani.rpa_validador.controller;

import com.castellani.rpa_validador.model.Pedido;
import com.castellani.rpa_validador.repository.PedidoRepository;
import com.castellani.rpa_validador.service.RpaRoboService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/robo")
public class RpaController {

    private final RpaRoboService roboService;
    private final PedidoRepository repository;

    public RpaController(RpaRoboService roboService, PedidoRepository repository){
        this.roboService = roboService;
        this.repository = repository;
    }

    // POST servidor executa uma ação de processamento
    @PostMapping("/processar-lote")
    public ResponseEntity<RpaRoboService.RelatorioExecucao> dispararRobo() {
        var relatorio = roboService.processarArquivoCsv("lote_pedidos.csv");
        return ResponseEntity.ok(relatorio);
    }

    // GET para listar dados salvos
    @GetMapping("/processados")
    public ResponseEntity<List<Pedido>> listarProcessados() {
        List<Pedido> dadosSalvos = repository.findAll(); // Faz um "SELECT * FROM" automático
        return ResponseEntity.ok(dadosSalvos);
    }
}
