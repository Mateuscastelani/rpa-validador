package com.castellani.rpa_validador.service;

import com.castellani.rpa_validador.model.Pedido;
import com.castellani.rpa_validador.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class RpaRoboService {

    private static final Logger logger = LoggerFactory.getLogger(RpaRoboService.class);
    private final PedidoRepository repository;

    public RpaRoboService(PedidoRepository repository) {
        this.repository = repository;
    }

    // Record objeto de resposta
    public record RelatorioExecucao(int totalLido, int salvosComSucesso, int falhas, List<String> logErros) {}

    public RelatorioExecucao processarArquivoCsv(String caminhoArquivo) {
        int lidos = 0;
        int salvos = 0;
        int falhas = 0;
        List<String> erros = new ArrayList<>();

        logger.info("Iniciando leitura do arquivo: {}", caminhoArquivo);

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                lidos++;
                String[] colunas = linha.split(",");

                // Bloco Try-Catch INTERNO Se esta linha der erro, o loop continua na prox
                try {
                    String codigo = colunas[0].trim();
                    String cliente = colunas[1].trim();
                    String produto = colunas[2].trim();

                    if (produto.isEmpty()) {
                        throw new IllegalArgumentException("O nome do produto veio em branco.");
                    }

                    BigDecimal valor = new BigDecimal(colunas[3].trim());
                    if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new IllegalArgumentException("Valor inválido: " + valor + ". O preço deve ser maior que zero.");
                    }

                    Pedido pedido = new Pedido(codigo, cliente, produto, valor);
                    repository.save(pedido);
                    salvos++;

                } catch (Exception e) {
                    falhas++;
                    String msgErro = "Erro na linha " + lidos + " (" + colunas[0] + "): " + e.getMessage();
                    logger.warn(msgErro);
                    erros.add(msgErro);
                }
            }

        } catch (Exception e) {
            logger.error("Falha catastrófica de I/O ao tentar abrir o arquivo.", e);
            erros.add("Arquivo não encontrado ou inacessível.");
        }

        logger.info("Lote finalizado. Sucesso: {} | Falhas: {}", salvos, falhas);
        return new RelatorioExecucao(lidos, salvos, falhas, erros);
    }
}