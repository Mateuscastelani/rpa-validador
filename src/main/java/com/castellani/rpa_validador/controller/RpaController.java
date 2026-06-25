package com.castellani.rpa_validador.controller;

import com.castellani.rpa_validador.service.RpaRoboService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/robo")
public class RpaController {

    private final RpaRoboService roboService;

    public RpaController(RpaRoboService roboService) {
        this.roboService = roboService;
    }

    // POST servidor *executar uma ação de processamento*
    @PostMapping("/processar-lote")
    public ResponseEntity<RpaRoboService.RelatorioExecucao> dispararRobo() {
        var relatorio = roboService.processarArquivoCsv("lote_pedidos.csv");
        return ResponseEntity.ok(relatorio);
    }
}