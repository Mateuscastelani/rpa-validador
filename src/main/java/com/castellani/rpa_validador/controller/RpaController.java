package com.castellani.rpa_validador.controller;

import com.castellani.rpa_validador.model.Pedido;
import com.castellani.rpa_validador.repository.PedidoRepository;
import com.castellani.rpa_validador.service.RpaRoboService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //PUT atualizar pedido pelo id
    @PutMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido dadosNovos) {
        return repository.findById(id)
                .map(pedidoExistente -> {
                    // Atualiza os campos do objeto que veio do banco
                    pedidoExistente.setCodigo(dadosNovos.getCodigo());
                    pedidoExistente.setCliente(dadosNovos.getCliente());
                    pedidoExistente.setProduto(dadosNovos.getProduto());
                    pedidoExistente.setValor(dadosNovos.getValor());

                    // Salva no banco de dados
                    Pedido pedidoAtualizado = repository.save(pedidoExistente);

                    // resultado embrulhado no ResponseEntity
                    return ResponseEntity.ok(pedidoAtualizado);
                })
                    // Se o ID não existir no banco, retorna 404 Not Found
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<?> excluirPedido(@PathVariable Long id) {
        return repository.findById(id)
                .map(pedidoExistente -> {
                    // encontra e deleta o pedido no banco
                    repository.delete(pedidoExistente);

                    // retorna o status 204
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}

