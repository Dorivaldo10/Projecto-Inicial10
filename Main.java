package com.aprendafinancas;

import com.aprendafinancas.utils.FontePadrao;
import com.aprendafinancas.utils.EstiloPadrao;
import com.aprendafinancas.utils.LayoutPadrao;
import com.aprendafinancas.views.LoginView;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Classe principal da aplicação AprendaFinanças
 * Responsável por inicializar e gerenciar as telas iniciais
 * 
 * @version 1.0.0
 * @author Carlos Chaves
 * @since 2025
 */
public class Main extends Application {
    
    // ========== CONSTANTES ==========
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    
    // Dimensões da aplicação
    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 600;
    
    // Durações das animações
    private static final Duration WELCOME_DURATION = Duration.seconds(3);
    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private static final Duration SLIDE_DURATION = Duration.millis(350);
    private static final Duration SLIDE_BACK_DURATION = Duration.millis(250);
    
    // Tamanhos e valores
    private static final double SCALE_HOVER = 1.08;
    private static final double SCALE_NORMAL = 1.0;
    private static final double LOGO_OPACITY = 0.45;
    private static final double TOP_BAR_TRANSLATE = -50;
    private static final int PROGRESS_INDICATOR_SIZE = 60;
    private static final int DROP_SHADOW_RADIUS = 15;
    private static final int TOP_DETECTOR_HEIGHT = 40;
    
    // Strings da aplicação
    private static final String APP_NAME = "AprendaFinanças";
    private static final String WELCOME_TITLE = "Boas-vindas – " + APP_NAME;
    private static final String MAIN_TITLE = "Bem-vindo – " + APP_NAME;
    private static final String WELCOME_MESSAGE = "🎓 SEJA BEM-VINDO À MULTIMÉDIA EDUCATIVA\n" +
            "PARA A DISCIPLINA DE ANÁLISE E GESTÃO FINANCEIRA II";
    private static final String FOOTER_TEXT = "Versão 1.0 © 2025 | Desenvolvido por Carlos Chaves";
    private static final String LOGO_PATH = "/imagens/logo.png";
    
    // Cores e estilos
    private static final String WELCOME_GRADIENT = "-fx-background-color: linear-gradient(to bottom, #1e3c72, #2a5298);";
    private static final String MAIN_GRADIENT = "-fx-background-color: radial-gradient(center 50% 50%, radius 120%, #e8f5fe, #90caf9);";
    private static final String TOP_BAR_STYLE = "-fx-background-color: rgba(38,50,56,0.8);";
    private static final String GLOW_COLOR = "#00c6ff";
    
    // ========== MÉTODOS PRINCIPAIS ==========
    
    @Override
    public void start(Stage primaryStage) {
        try {
            logger.info("Iniciando aplicação {}", APP_NAME);
            
            configureStage(primaryStage);
            showWelcomeScreen(primaryStage);
            
        } catch (Exception e) {
            logger.error("Erro ao iniciar aplicação", e);
            showErrorDialog("Erro Crítico", "Não foi possível iniciar a aplicação: " + e.getMessage());
        }
    }
    
    /**
     * Configura as propriedades básicas do stage principal
     */
    private void configureStage(Stage stage) {
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setMinWidth(WINDOW_WIDTH * 0.8);
        stage.setMinHeight(WINDOW_HEIGHT * 0.8);
        
        // Adicionar ícone se disponível
        setApplicationIcon(stage);
    }
    
    // ========== TELA DE BOAS-VINDAS ==========
    
    /**
     * Exibe a tela de boas-vindas com animação
     */
    private void showWelcomeScreen(Stage stage) {
        logger.debug("Exibindo tela de boas-vindas");
        
        Scene welcomeScene = createWelcomeScene(stage);
        stage.setScene(welcomeScene);
        stage.setTitle(WELCOME_TITLE);
        stage.show();
        
        // Transição automática para tela principal
        scheduleMainScreenTransition(stage);
    }
    
    /**
     * Cria a cena da tela de boas-vindas
     */
    private Scene createWelcomeScene(Stage stage) {
        VBox messageLayout = createWelcomeMessageLayout();
        StackPane background = new StackPane(messageLayout);
        background.setStyle(WELCOME_GRADIENT);
        
        return new Scene(background, stage.getWidth(), stage.getHeight());
    }
    
    /**
     * Cria o layout da mensagem de boas-vindas
     */
    private VBox createWelcomeMessageLayout() {
        Label welcomeLabel = createWelcomeLabel();
        ProgressIndicator progressIndicator = createProgressIndicator();
        
        VBox layout = new VBox(30, welcomeLabel, progressIndicator);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        
        return layout;
    }
    
    /**
     * Cria o label de boas-vindas
     */
    private Label createWelcomeLabel() {
        Label label = new Label(WELCOME_MESSAGE);
        label.setFont(FontePadrao.getMontserrat(22));
        label.setStyle("-fx-text-fill: white; -fx-text-alignment: center;");
        label.setWrapText(true);
        return label;
    }
    
    /**
     * Cria o indicador de progresso
     */
    private ProgressIndicator createProgressIndicator() {
        ProgressIndicator indicator = new ProgressIndicator();
        indicator.setPrefSize(PROGRESS_INDICATOR_SIZE, PROGRESS_INDICATOR_SIZE);
        return indicator;
    }
    
    /**
     * Agenda a transição para a tela principal
     */
    private void scheduleMainScreenTransition(Stage stage) {
        PauseTransition transition = new PauseTransition(WELCOME_DURATION);
        transition.setOnFinished(e -> showMainScreen(stage));
        transition.play();
    }
    
    // ========== TELA PRINCIPAL ==========
    
    /**
     * Exibe a tela principal da aplicação
     */
    private void showMainScreen(Stage stage) {
        try {
            logger.debug("Exibindo tela principal");
            
            Scene mainScene = createMainScene(stage);
            stage.setScene(mainScene);
            stage.setTitle(MAIN_TITLE);
            
        } catch (Exception e) {
            logger.error("Erro ao criar tela principal", e);
            showErrorDialog("Erro", "Não foi possível carregar a tela principal");
        }
    }
    
    /**
     * Cria a cena principal da aplicação
     */
    private Scene createMainScene(Stage stage) {
        // Componentes principais
        Button accessButton = createAccessButton(stage);
        Region topDetector = createTopDetector();
        HBox topBar = createTopBar(stage);
        HBox footer = createFooter();
        ImageView backgroundLogo = createBackgroundLogo(stage);
        
        // Layout principal
        VBox centerContent = new VBox(30, accessButton);
        centerContent.setAlignment(Pos.CENTER);
        
        StackPane topArea = createTopArea(topDetector, topBar);
        
        BorderPane layout = new BorderPane();
        layout.setTop(topArea);
        layout.setCenter(centerContent);
        layout.setBottom(footer);
        
        StackPane root = new StackPane(backgroundLogo, layout);
        root.setStyle(MAIN_GRADIENT);
        
        return new Scene(root, stage.getWidth(), stage.getHeight());
    }
    
    // ========== COMPONENTES UI ==========
    
    /**
     * Cria o botão principal de acesso
     */
    private Button createAccessButton(Stage stage) {
        Button button = new Button("ACESSAR");
        EstiloPadrao.estilizarBotao(button);
        button.setFont(FontePadrao.getPoppins(16));
        
        // Efeitos visuais
        addButtonHoverEffect(button);
        addButtonGlowEffect(button);
        
        // Ação do botão
        button.setOnAction(e -> openLoginView(stage));
        
        return button;
    }
    
    /**
     * Adiciona efeito de hover ao botão
     */
    private void addButtonHoverEffect(Button button) {
        ScaleTransition hoverIn = createScaleTransition(button, SCALE_HOVER);
        ScaleTransition hoverOut = createScaleTransition(button, SCALE_NORMAL);
        
        button.setOnMouseEntered(e -> hoverIn.playFromStart());
        button.setOnMouseExited(e -> hoverOut.playFromStart());
    }
    
    /**
     * Cria uma transição de escala
     */
    private ScaleTransition createScaleTransition(Button button, double scale) {
        ScaleTransition transition = new ScaleTransition(ANIMATION_DURATION, button);
        transition.setToX(scale);
        transition.setToY(scale);
        return transition;
    }
    
    /**
     * Adiciona efeito de brilho ao botão
     */
    private void addButtonGlowEffect(Button button) {
        DropShadow glow = new DropShadow(DROP_SHADOW_RADIUS, Color.web(GLOW_COLOR));
        glow.setSpread(0.6);
        button.setEffect(glow);
    }
    
    /**
     * Abre a view de login
     */
    private void openLoginView(Stage currentStage) {
        try {
            logger.debug("Abrindo tela de login");
            
            Stage loginStage = new Stage();
            LayoutPadrao.replicarDimensoes(currentStage, loginStage);
            new LoginView().mostrar(loginStage);
            currentStage.close();
            
        } catch (Exception e) {
            logger.error("Erro ao abrir tela de login", e);
            showErrorDialog("Erro", "Não foi possível abrir a tela de login");
        }
    }
    
    /**
     * Cria a barra superior com animação
     */
    private HBox createTopBar(Stage stage) {
        Button profileButton = createProfileButton();
        Button exitButton = createExitButton(stage);
        
        HBox topBar = new HBox(15, profileButton, exitButton);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle(TOP_BAR_STYLE);
        
        // Efeitos visuais
        addTopBarShadow(topBar);
        configureTopBarAnimation(topBar);
        
        return topBar;
    }
    
    /**
     * Cria o detector da área superior
     */
    private Region createTopDetector() {
        Region detector = new Region();
        detector.setPrefHeight(TOP_DETECTOR_HEIGHT);
        return detector;
    }
    
    /**
     * Cria a área superior com detector e barra
     */
    private StackPane createTopArea(Region detector, HBox topBar) {
        StackPane topArea = new StackPane(detector, topBar);
        StackPane.setAlignment(topBar, Pos.TOP_RIGHT);
        
        // Configurar animações de entrada e saída
        configureTopAreaAnimations(detector, topBar);
        
        return topArea;
    }
    
    /**
     * Configura as animações da área superior
     */
    private void configureTopAreaAnimations(Region detector, HBox topBar) {
        detector.setOnMouseEntered(e -> showTopBar(topBar));
        topBar.setOnMouseExited(e -> hideTopBar(topBar));
    }
    
    /**
     * Mostra a barra superior com animação
     */
    private void showTopBar(HBox topBar) {
        topBar.setVisible(true);
        TranslateTransition slideIn = new TranslateTransition(SLIDE_DURATION, topBar);
        slideIn.setToY(0);
        slideIn.play();
    }
    
    /**
     * Esconde a barra superior com animação
     */
    private void hideTopBar(HBox topBar) {
        TranslateTransition slideOut = new TranslateTransition(SLIDE_BACK_DURATION, topBar);
        slideOut.setToY(TOP_BAR_TRANSLATE);
        slideOut.setOnFinished(e -> topBar.setVisible(false));
        slideOut.play();
    }
    
    /**
     * Adiciona sombra à barra superior
     */
    private void addTopBarShadow(HBox topBar) {
        DropShadow shadow = new DropShadow(10, Color.web(GLOW_COLOR));
        shadow.setOffsetY(4);
        shadow.setSpread(0.4);
        topBar.setEffect(shadow);
    }
    
    /**
     * Configura a animação inicial da barra superior
     */
    private void configureTopBarAnimation(HBox topBar) {
        topBar.setTranslateY(TOP_BAR_TRANSLATE);
        topBar.setVisible(false);
    }
    
    /**
     * Cria o botão de perfil
     */
    private Button createProfileButton() {
        Button button = new Button("👤 PERFIL");
        EstiloPadrao.estilizarBotao(button);
        button.setFont(FontePadrao.getPoppins(14));
        
        button.setOnAction(e -> showProfileDialog());
        
        return button;
    }
    
    /**
     * Cria o botão de sair
     */
    private Button createExitButton(Stage stage) {
        Button button = new Button("← SAIR");
        EstiloPadrao.estilizarBotao(button);
        button.setFont(FontePadrao.getPoppins(14));
        
        button.setOnAction(e -> exitApplication(stage));
        
        return button;
    }
    
    /**
     * Exibe o diálogo de perfil
     */
    private void showProfileDialog() {
        Alert profileAlert = new Alert(Alert.AlertType.INFORMATION);
        profileAlert.setTitle("Perfil do Usuário");
        profileAlert.setHeaderText("🚀 Área de Personalização");
        profileAlert.setContentText("Em breve, você poderá configurar seu avatar, " +
                "preferências e progresso financeiro.");
        profileAlert.show();
    }
    
    /**
     * Encerra a aplicação
     */
    private void exitApplication(Stage stage) {
        logger.info("Encerrando aplicação");
        stage.close();
    }
    
    /**
     * Cria o rodapé da aplicação
     */
    private HBox createFooter() {
        Text footerText = new Text(FOOTER_TEXT);
        footerText.setFont(FontePadrao.getRoboto(14));
        footerText.setStyle("-fx-fill: #0D47A1;");
        
        HBox footer = new HBox(footerText);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(12));
        
        return footer;
    }
    
    /**
     * Cria o logo de fundo
     */
    private ImageView createBackgroundLogo(Stage stage) {
        ImageView backgroundLogo = new ImageView();
        
        try {
            URL logoUrl = getClass().getResource(LOGO_PATH);
            if (logoUrl != null) {
                Image logo = new Image(logoUrl.toExternalForm());
                backgroundLogo.setImage(logo);
                backgroundLogo.fitWidthProperty().bind(stage.widthProperty());
                backgroundLogo.fitHeightProperty().bind(stage.heightProperty());
                backgroundLogo.setPreserveRatio(false);
                backgroundLogo.setOpacity(LOGO_OPACITY);
            } else {
                logger.warn("Logo não encontrado em: {}", LOGO_PATH);
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar logo", e);
        }
        
        return backgroundLogo;
    }
    
    // ========== MÉTODOS UTILITÁRIOS ==========
    
    /**
     * Define o ícone da aplicação
     */
    private void setApplicationIcon(Stage stage) {
        try {
            URL logoUrl = getClass().getResource(LOGO_PATH);
            if (logoUrl != null) {
                Image icon = new Image(logoUrl.toExternalForm());
                stage.getIcons().add(icon);
            }
        } catch (Exception e) {
            logger.warn("Não foi possível definir ícone da aplicação", e);
        }
    }
    
    /**
     * Exibe um diálogo de erro
     */
    private void showErrorDialog(String title, String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle(title);
        errorAlert.setHeaderText("⚠️ Ocorreu um problema");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
    
    /**
     * Método principal da aplicação
     */
    public static void main(String[] args) {
        try {
            logger.info("Iniciando {} versão 1.0", APP_NAME);
            launch(args);
        } catch (Exception e) {
            logger.error("Erro fatal na aplicação", e);
            System.exit(1);
        }
    }
}