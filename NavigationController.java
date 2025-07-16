package com.aprendafinancas.controllers;

import com.aprendafinancas.model.Usuario;
import com.aprendafinancas.views.*;
import javafx.stage.Stage;
import java.lang.reflect.Constructor;

/**
 * Controlador centralizado para navegação entre telas
 * Implementa padrão de navegação consistente e rastreamento
 */
public class NavigationController {
    
    private Stage stageAtual;
    private Usuario usuario;
    
    public void navegarPara(Class<?> viewClass, Stage stage, Usuario usuario) {
        this.stageAtual = stage;
        this.usuario = usuario;
        
        try {
            // Instanciar view usando reflexão
            Object viewInstance = criarInstanciaView(viewClass);
            
            // Criar nova stage para a tela
            Stage novaStage = new Stage();
            
            // Replicar dimensões da janela atual
            replicarDimensoes(stage, novaStage);
            
            // Chamar método mostrar da view
            invocarMostrar(viewInstance, novaStage);
            
            // Fechar stage atual
            stage.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao navegar para: " + viewClass.getSimpleName(), e);
        }
    }
    
    private Object criarInstanciaView(Class<?> viewClass) throws Exception {
        // Tentar construtor com Usuario primeiro
        try {
            Constructor<?> construtor = viewClass.getConstructor(Usuario.class);
            return construtor.newInstance(usuario);
        } catch (NoSuchMethodException e) {
            // Fallback para construtor padrão
            return viewClass.getDeclaredConstructor().newInstance();
        }
    }
    
    private void invocarMostrar(Object viewInstance, Stage stage) throws Exception {
        // Usar reflexão para chamar o método mostrar
        viewInstance.getClass()
            .getMethod("mostrar", Stage.class)
            .invoke(viewInstance, stage);
    }
    
    private void replicarDimensoes(Stage origem, Stage destino) {
        destino.setWidth(origem.getWidth());
        destino.setHeight(origem.getHeight());
        destino.setX(origem.getX());
        destino.setY(origem.getY());
    }
}