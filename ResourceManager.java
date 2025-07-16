package com.aprendafinancas.utils;

import javafx.scene.image.Image;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Gerenciador centralizado de recursos (imagens, ícones, etc.)
 * Implementa cache e tratamento seguro de recursos
 */
public class ResourceManager {
    
    private static final Logger logger = Logger.getLogger(ResourceManager.class.getName());
    private static final ConcurrentHashMap<String, Image> imageCache = new ConcurrentHashMap<>();
    
    /**
     * Carrega uma imagem de forma segura com cache
     * @param caminho Caminho do recurso (ex: "/imagens/logo.png")
     * @return Optional contendo a imagem ou empty se não encontrada
     */
    public static Optional<Image> carregarImagem(String caminho) {
        if (caminho == null || caminho.trim().isEmpty()) {
            logger.warning("Caminho de imagem inválido: " + caminho);
            return Optional.empty();
        }
        
        // Verificar cache primeiro
        Image imagemCache = imageCache.get(caminho);
        if (imagemCache != null) {
            return Optional.of(imagemCache);
        }
        
        try {
            URL urlRecurso = ResourceManager.class.getResource(caminho);
            
            if (urlRecurso == null) {
                logger.warning("Recurso não encontrado: " + caminho);
                return Optional.empty();
            }
            
            Image imagem = new Image(urlRecurso.toExternalForm());
            
            // Verificar se a imagem carregou corretamente
            if (imagem.isError()) {
                logger.warning("Erro ao carregar imagem: " + caminho);
                return Optional.empty();
            }
            
            // Adicionar ao cache para uso futuro
            imageCache.put(caminho, imagem);
            
            logger.info("Imagem carregada com sucesso: " + caminho);
            return Optional.of(imagem);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exceção ao carregar imagem: " + caminho, e);
            return Optional.empty();
        }
    }
    
    /**
     * Carrega uma imagem com tamanho específico
     */
    public static Optional<Image> carregarImagem(String caminho, double largura, double altura) {
        return carregarImagem(caminho, largura, altura, true, true);
    }
    
    /**
     * Carrega uma imagem com configurações avançadas
     */
    public static Optional<Image> carregarImagem(String caminho, double largura, double altura, 
                                               boolean preservarRazao, boolean suavizar) {
        String chaveCache = String.format("%s_%s_%s_%s_%s", 
                                         caminho, largura, altura, preservarRazao, suavizar);
        
        // Verificar cache primeiro
        Image imagemCache = imageCache.get(chaveCache);
        if (imagemCache != null) {
            return Optional.of(imagemCache);
        }
        
        try {
            URL urlRecurso = ResourceManager.class.getResource(caminho);
            
            if (urlRecurso == null) {
                logger.warning("Recurso não encontrado: " + caminho);
                return Optional.empty();
            }
            
            Image imagem = new Image(urlRecurso.toExternalForm(), 
                                   largura, altura, preservarRazao, suavizar);
            
            if (imagem.isError()) {
                logger.warning("Erro ao carregar imagem redimensionada: " + caminho);
                return Optional.empty();
            }
            
            // Adicionar ao cache
            imageCache.put(chaveCache, imagem);
            
            return Optional.of(imagem);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exceção ao carregar imagem redimensionada: " + caminho, e);
            return Optional.empty();
        }
    }
    
    /**
     * Verifica se um recurso existe
     */
    public static boolean recursoExiste(String caminho) {
        try {
            URL url = ResourceManager.class.getResource(caminho);
            return url != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Limpa o cache de imagens (útil para gerenciamento de memória)
     */
    public static void limparCache() {
        imageCache.clear();
        logger.info("Cache de imagens limpo");
    }
    
    /**
     * Obtém estatísticas do cache
     */
    public static String getEstatisticasCache() {
        return String.format("Cache de imagens: %d itens carregados", imageCache.size());
    }
    
    /**
     * Pré-carrega recursos importantes para melhor performance
     */
    public static void preCarregarRecursosComuns() {
        String[] recursosComuns = {
            "/imagens/logo.png",
            "/imagens/icone-usuario.png", 
            "/imagens/fundo-pattern.png"
        };
        
        for (String recurso : recursosComuns) {
            carregarImagem(recurso);
        }
        
        logger.info("Recursos comuns pré-carregados");
    }
}