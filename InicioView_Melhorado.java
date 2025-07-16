package com.aprendafinancas.views;

import com.aprendafinancas.Main;
import com.aprendafinancas.model.Usuario;
import com.aprendafinancas.model.Usuario.TipoPerfil;
import com.aprendafinancas.utils.FontePadrao;
import com.aprendafinancas.utils.EstiloPadrao;
import com.aprendafinancas.utils.LayoutPadrao;
import com.aprendafinancas.controllers.NavigationController;
import com.aprendafinancas.services.ProgressoService;
import com.aprendafinancas.utils.ResourceManager;

import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.Optional;

/**
 * Tela principal do aplicativo AprendaFinanças
 * Implementa princípios de design instrucional e acessibilidade
 */
public class InicioView {
    
    // Constantes para internacionalização
    private static final String TITULO_PRINCIPAL = "AprendaFinanças - Sua Jornada Financeira";
    private static final String BOAS_VINDAS = "Bem-vindo à sua jornada de aprendizado financeiro!";
    
    private Stage stageAtual;
    private Usuario usuario;
    private ProgressoService progressoService;
    private NavigationController navigationController;
    
    // Componentes da interface
    private GridPane menuPrincipal;
    private VBox containerProgresso;
    private Label lblProgresso;
    private ProgressBar barraProgresso;

    public InicioView(Usuario usuario) {
        this.usuario = usuario;
        this.progressoService = new ProgressoService(usuario);
        this.navigationController = new NavigationController();
    }

    public void mostrar(Stage stage) {
        this.stageAtual = stage;
        
        // Configurações iniciais da janela
        configurarJanela();
        
        // Criar layout principal
        Scene scene = criarLayoutPrincipal();
        
        // Aplicar cena e exibir
        stage.setScene(scene);
        stage.show();
        
        // Animação de entrada suave
        aplicarAnimacaoEntrada();
    }

    private void configurarJanela() {
        stageAtual.setTitle(TITULO_PRINCIPAL);
        stageAtual.setMinWidth(800);
        stageAtual.setMinHeight(600);
        
        // Ícone da aplicação
        Optional<Image> icone = ResourceManager.carregarImagem("/imagens/logo.png");
        icone.ifPresent(img -> stageAtual.getIcons().add(img));
    }

    private Scene criarLayoutPrincipal() {
        // Layout principal com áreas bem definidas
        BorderPane layoutPrincipal = new BorderPane();
        
        // Cabeçalho com informações do usuário e navegação
        HBox cabecalho = criarCabecalho();
        layoutPrincipal.setTop(cabecalho);
        
        // Área central com menu de módulos
        VBox areaCentral = criarAreaCentral();
        layoutPrincipal.setCenter(areaCentral);
        
        // Rodapé com progresso e dicas
        VBox rodape = criarRodape();
        layoutPrincipal.setBottom(rodape);
        
        // Container principal com fundo
        StackPane containerPrincipal = new StackPane();
        
        // Fundo com imagem sutil
        Optional<ImageView> fundoLogo = criarFundoSutil();
        if (fundoLogo.isPresent()) {
            containerPrincipal.getChildren().add(fundoLogo.get());
        }
        
        containerPrincipal.getChildren().add(layoutPrincipal);
        containerPrincipal.setStyle("-fx-background-color: linear-gradient(to bottom right, #e3f2fd, #f3e5f5);");
        
        return new Scene(containerPrincipal, 1000, 700);
    }

    private HBox criarCabecalho() {
        HBox cabecalho = new HBox(15);
        cabecalho.setAlignment(Pos.CENTER_LEFT);
        cabecalho.setPadding(new Insets(15, 20, 15, 20));
        cabecalho.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 0 0 15 15;");
        
        // Avatar e saudação personalizada
        Label saudacao = new Label("Olá, " + usuario.getNome() + "! 👋");
        saudacao.setFont(FontePadrao.getMontserrat(18));
        saudacao.setStyle("-fx-text-fill: #1976d2; -fx-font-weight: bold;");
        
        // Spacer para empurrar botões para direita
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Botões de ação do cabeçalho
        Button btnNotificacoes = criarBotaoNotificacao();
        Button btnAjuda = criarBotaoAjuda();
        Button btnConfiguracoes = criarBotaoConfiguracoes();
        Button btnSair = criarBotaoSair();
        
        cabecalho.getChildren().addAll(saudacao, spacer, btnNotificacoes, btnAjuda, btnConfiguracoes, btnSair);
        
        return cabecalho;
    }

    private VBox criarAreaCentral() {
        VBox areaCentral = new VBox(30);
        areaCentral.setAlignment(Pos.CENTER);
        areaCentral.setPadding(new Insets(30));
        
        // Título motivacional
        Label titulo = new Label(BOAS_VINDAS);
        titulo.setFont(FontePadrao.getMontserrat(24));
        titulo.setStyle("-fx-text-fill: #0d47a1; -fx-font-weight: bold;");
        
        // Menu principal de módulos
        menuPrincipal = criarMenuModulos();
        
        // Container de progresso
        containerProgresso = criarContainerProgresso();
        
        areaCentral.getChildren().addAll(titulo, menuPrincipal, containerProgresso);
        
        return areaCentral;
    }

    private GridPane criarMenuModulos() {
        GridPane menu = new GridPane();
        menu.setAlignment(Pos.CENTER);
        menu.setHgap(30);
        menu.setVgap(30);
        menu.setPadding(new Insets(20));
        
        // Módulos educativos com metadados
        ModuloEducativo[] modulos = {
            new ModuloEducativo("📚 Conceitos Básicos", "Aprenda os fundamentos", MaterialView.class, true),
            new ModuloEducativo("🎥 Vídeo Aulas", "Conteúdo audiovisual", VideoView.class, true),
            new ModuloEducativo("🧮 Simulador", "Pratique com simulações", SimuladorView.class, progressoService.isModuloLiberado(2)),
            new ModuloEducativo("📊 Avaliações", "Teste seus conhecimentos", QuizView.class, progressoService.isModuloLiberado(3)),
            new ModuloEducativo("💡 Dicas Diárias", "Lembretes inteligentes", AlertasView.class, true),
            new ModuloEducativo("🏆 Conquistas", "Suas medalhas e progresso", ConquistasView.class, true)
        };
        
        for (int i = 0; i < modulos.length; i++) {
            Button btnModulo = criarBotaoModulo(modulos[i]);
            
            int coluna = i % 3;
            int linha = i / 3;
            menu.add(btnModulo, coluna, linha);
        }
        
        return menu;
    }

    private Button criarBotaoModulo(ModuloEducativo modulo) {
        Button btn = new Button();
        
        // Layout interno do botão
        VBox layoutBotao = new VBox(8);
        layoutBotao.setAlignment(Pos.CENTER);
        layoutBotao.setPadding(new Insets(15));
        
        Label iconeLabel = new Label(modulo.getIcone());
        iconeLabel.setFont(FontePadrao.getPoppins(28));
        
        Label tituloLabel = new Label(modulo.getTitulo().substring(2)); // Remove emoji do título
        tituloLabel.setFont(FontePadrao.getPoppins(16));
        tituloLabel.setStyle("-fx-font-weight: bold;");
        
        Label descricaoLabel = new Label(modulo.getDescricao());
        descricaoLabel.setFont(FontePadrao.getPoppins(12));
        descricaoLabel.setStyle("-fx-text-fill: #666;");
        descricaoLabel.setWrapText(true);
        descricaoLabel.setMaxWidth(180);
        descricaoLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        layoutBotao.getChildren().addAll(iconeLabel, tituloLabel, descricaoLabel);
        
        // Estilo baseado no status do módulo
        if (modulo.isLiberado()) {
            btn.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #ffffff, #f5f5f5);
                -fx-border-color: #e0e0e0;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-border-width: 1;
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);
                -fx-cursor: hand;
                """);
            
            btn.setOnAction(e -> navegarParaModulo(modulo));
            
            // Animação hover
            btn.setOnMouseEntered(e -> aplicarAnimacaoHover(btn, 1.05));
            btn.setOnMouseExited(e -> aplicarAnimacaoHover(btn, 1.0));
            
        } else {
            btn.setStyle("""
                -fx-background-color: #f5f5f5;
                -fx-border-color: #bdbdbd;
                -fx-border-radius: 15;
                -fx-background-radius: 15;
                -fx-border-width: 1;
                -fx-opacity: 0.6;
                """);
            
            // Adicionar cadeado para módulos bloqueados
            Label cadeado = new Label("🔒");
            cadeado.setFont(FontePadrao.getPoppins(20));
            layoutBotao.getChildren().add(0, cadeado);
            
            btn.setOnAction(e -> mostrarModuloBloqueado(modulo));
        }
        
        btn.setGraphic(layoutBotao);
        btn.setPrefSize(200, 150);
        btn.setMaxSize(200, 150);
        
        // Acessibilidade
        btn.setAccessibleText(modulo.getTitulo() + ". " + modulo.getDescricao() + 
                            (modulo.isLiberado() ? " Disponível" : " Bloqueado"));
        
        return btn;
    }

    private VBox criarContainerProgresso() {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20));
        container.setStyle("""
            -fx-background-color: rgba(255, 255, 255, 0.8);
            -fx-background-radius: 15;
            -fx-border-radius: 15;
            -fx-border-color: #e0e0e0;
            -fx-border-width: 1;
            """);
        
        lblProgresso = new Label("Seu Progresso Geral");
        lblProgresso.setFont(FontePadrao.getMontserrat(16));
        lblProgresso.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        
        barraProgresso = new ProgressBar();
        barraProgresso.setPrefWidth(300);
        barraProgresso.setPrefHeight(15);
        barraProgresso.setStyle("""
            -fx-accent: linear-gradient(to right, #4caf50, #8bc34a);
            -fx-background-radius: 10;
            """);
        
        double progresso = progressoService.calcularProgressoGeral();
        barraProgresso.setProgress(progresso);
        
        Label percentual = new Label(String.format("%.0f%% Concluído", progresso * 100));
        percentual.setFont(FontePadrao.getPoppins(14));
        percentual.setStyle("-fx-text-fill: #666;");
        
        container.getChildren().addAll(lblProgresso, barraProgresso, percentual);
        
        return container;
    }

    private VBox criarRodape() {
        VBox rodape = new VBox(10);
        rodape.setAlignment(Pos.CENTER);
        rodape.setPadding(new Insets(15));
        rodape.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7);");
        
        // Dica do dia personalizada
        String dicaDoDia = progressoService.obterDicaDoDia();
        Label lblDica = new Label("💡 Dica do Dia: " + dicaDoDia);
        lblDica.setFont(FontePadrao.getPoppins(14));
        lblDica.setStyle("-fx-text-fill: #1976d2;");
        lblDica.setWrapText(true);
        lblDica.setMaxWidth(600);
        lblDica.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        rodape.getChildren().add(lblDica);
        
        return rodape;
    }

    // Métodos auxiliares para criação de botões
    private Button criarBotaoNotificacao() {
        Button btn = new Button("🔔");
        EstiloPadrao.estilizarBotaoCircular(btn);
        btn.setOnAction(e -> mostrarNotificacoes());
        btn.setTooltip(new Tooltip("Notificações e alertas"));
        return btn;
    }

    private Button criarBotaoAjuda() {
        Button btn = new Button("❓");
        EstiloPadrao.estilizarBotaoCircular(btn);
        btn.setOnAction(e -> mostrarAjudaModal());
        btn.setTooltip(new Tooltip("Ajuda e tutorial"));
        return btn;
    }

    private Button criarBotaoConfiguracoes() {
        Button btn = new Button("⚙️");
        EstiloPadrao.estilizarBotaoCircular(btn);
        btn.setOnAction(e -> abrirConfiguracoes());
        btn.setTooltip(new Tooltip("Configurações"));
        return btn;
    }

    private Button criarBotaoSair() {
        Button btn = new Button("🚪");
        EstiloPadrao.estilizarBotaoCircular(btn);
        btn.setOnAction(e -> confirmarSaida());
        btn.setTooltip(new Tooltip("Sair do aplicativo"));
        return btn;
    }

    // Métodos de navegação e ações
    private void navegarParaModulo(ModuloEducativo modulo) {
        try {
            // Salvar progresso antes de navegar
            progressoService.registrarAcessoModulo(modulo.getTitulo());
            
            // Usar navigationController para navegação consistente
            navigationController.navegarPara(modulo.getViewClass(), stageAtual, usuario);
            
        } catch (Exception ex) {
            mostrarErro("Erro ao abrir módulo", "Não foi possível acessar o módulo selecionado.");
            ex.printStackTrace();
        }
    }

    private void mostrarModuloBloqueado(ModuloEducativo modulo) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Módulo Bloqueado");
        alert.setHeaderText("🔒 Ops! Este módulo ainda não está disponível");
        alert.setContentText("Complete os módulos anteriores para desbloquear: " + modulo.getTitulo());
        
        // Personalizar botões
        alert.getButtonTypes().setAll(ButtonType.OK);
        Button btnOk = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        btnOk.setText("Entendi");
        
        alert.showAndWait();
    }

    private void mostrarAjudaModal() {
        // Modal em vez de nova janela para melhor UX
        Dialog<Void> ajudaDialog = new Dialog<>();
        ajudaDialog.setTitle("Ajuda e Suporte");
        ajudaDialog.initModality(Modality.APPLICATION_MODAL);
        
        VBox conteudo = new VBox(15);
        conteudo.setPadding(new Insets(20));
        conteudo.setAlignment(Pos.CENTER_LEFT);
        
        Label titulo = new Label("🎓 Como usar o AprendaFinanças");
        titulo.setFont(FontePadrao.getMontserrat(18));
        titulo.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        
        VBox faq = new VBox(10);
        String[] perguntas = {
            "• Como navegar pelos módulos?",
            "• Como usar o simulador financeiro?",
            "• Como acompanhar meu progresso?",
            "• Como desbloquear novos conteúdos?",
            "• Onde encontro minhas conquistas?"
        };
        
        for (String pergunta : perguntas) {
            Label item = new Label(pergunta);
            item.setFont(FontePadrao.getPoppins(14));
            faq.getChildren().add(item);
        }
        
        Label contato = new Label("\n📧 Suporte: suporte@aprendafinancas.com");
        contato.setFont(FontePadrao.getPoppins(12));
        contato.setStyle("-fx-text-fill: #666;");
        
        conteudo.getChildren().addAll(titulo, faq, contato);
        
        ajudaDialog.getDialogPane().setContent(conteudo);
        ajudaDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        Button btnFechar = (Button) ajudaDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
        btnFechar.setText("Fechar");
        
        ajudaDialog.showAndWait();
    }

    private void confirmarSaida() {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Saída");
        confirmacao.setHeaderText("Deseja realmente sair?");
        confirmacao.setContentText("Seu progresso será salvo automaticamente.");
        
        confirmacao.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        ((Button) confirmacao.getDialogPane().lookupButton(ButtonType.YES)).setText("Sim, sair");
        ((Button) confirmacao.getDialogPane().lookupButton(ButtonType.NO)).setText("Continuar");
        
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        
        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            // Salvar progresso antes de sair
            progressoService.salvarProgresso();
            
            // Animação de despedida
            mostrarDespedida();
        }
    }

    private void mostrarDespedida() {
        Stage despedidaStage = new Stage();
        despedidaStage.initModality(Modality.APPLICATION_MODAL);
        
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #0f2027, #203a43, #2c5364);");
        
        Label mensagem = new Label("Obrigado por aprender conosco! 🎓\n" +
                                 "Continue sua jornada financeira amanhã!");
        mensagem.setFont(FontePadrao.getMontserrat(20));
        mensagem.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        mensagem.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        ProgressIndicator indicador = new ProgressIndicator();
        indicador.setPrefSize(50, 50);
        
        layout.getChildren().addAll(mensagem, indicador);
        
        Scene scene = new Scene(layout, 400, 250);
        despedidaStage.setScene(scene);
        despedidaStage.setTitle("Até logo!");
        despedidaStage.show();
        
        // Fechar após 3 segundos
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            despedidaStage.close();
            stageAtual.close();
        });
        pause.play();
    }

    // Métodos de animação
    private void aplicarAnimacaoEntrada() {
        menuPrincipal.setOpacity(0);
        containerProgresso.setOpacity(0);
        
        // Fade in sequencial
        PauseTransition delay1 = new PauseTransition(Duration.millis(200));
        delay1.setOnFinished(e -> {
            javafx.animation.FadeTransition fade1 = new javafx.animation.FadeTransition(Duration.millis(600), menuPrincipal);
            fade1.setFromValue(0);
            fade1.setToValue(1);
            fade1.play();
        });
        
        PauseTransition delay2 = new PauseTransition(Duration.millis(400));
        delay2.setOnFinished(e -> {
            javafx.animation.FadeTransition fade2 = new javafx.animation.FadeTransition(Duration.millis(600), containerProgresso);
            fade2.setFromValue(0);
            fade2.setToValue(1);
            fade2.play();
        });
        
        delay1.play();
        delay2.play();
    }

    private void aplicarAnimacaoHover(Button botao, double escala) {
        ScaleTransition scale = new ScaleTransition(Duration.millis(100), botao);
        scale.setToX(escala);
        scale.setToY(escala);
        scale.play();
    }

    private Optional<ImageView> criarFundoSutil() {
        return ResourceManager.carregarImagem("/imagens/logo.png")
                .map(img -> {
                    ImageView imageView = new ImageView(img);
                    imageView.setPreserveRatio(true);
                    imageView.setOpacity(0.05);
                    imageView.setFitWidth(300);
                    return imageView;
                });
    }

    // Métodos auxiliares
    private void mostrarErro(String titulo, String mensagem) {
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle(titulo);
        erro.setHeaderText(null);
        erro.setContentText(mensagem);
        erro.showAndWait();
    }

    private void mostrarNotificacoes() {
        // Implementar sistema de notificações personalizadas
        Alert notif = new Alert(Alert.AlertType.INFORMATION);
        notif.setTitle("Notificações");
        notif.setHeaderText("📢 Suas atualizações");
        notif.setContentText("• Novo vídeo sobre investimentos disponível\n" +
                            "• Lembrete: Complete o quiz de orçamento\n" +
                            "• Parabéns! Você desbloqueou uma nova conquista");
        notif.showAndWait();
    }

    private void abrirConfiguracoes() {
        Stage configStage = new Stage();
        LayoutPadrao.replicarDimensoes(stageAtual, configStage);
        new ConfiguracoesView(usuario).mostrar(configStage);
        stageAtual.close();
    }

    // Classe interna para representar módulos educativos
    private static class ModuloEducativo {
        private final String titulo;
        private final String descricao;
        private final Class<?> viewClass;
        private final boolean liberado;

        public ModuloEducativo(String titulo, String descricao, Class<?> viewClass, boolean liberado) {
            this.titulo = titulo;
            this.descricao = descricao;
            this.viewClass = viewClass;
            this.liberado = liberado;
        }

        public String getTitulo() { return titulo; }
        public String getDescricao() { return descricao; }
        public Class<?> getViewClass() { return viewClass; }
        public boolean isLiberado() { return liberado; }
        
        public String getIcone() {
            return titulo.substring(0, 2); // Extrai o emoji
        }
    }
}