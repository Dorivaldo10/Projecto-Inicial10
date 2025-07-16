package com.aprendafinancas.services;

import com.aprendafinancas.model.Usuario;
import java.util.*;
import java.time.LocalDate;

/**
 * Serviço para gerenciar progresso e conquistas do usuário
 * Implementa gamificação e personalização baseada em dados
 */
public class ProgressoService {
    
    private Usuario usuario;
    private Map<String, Integer> progressoModulos;
    private Set<String> conquistasDesbloqueadas;
    private List<String> dicasDiarias;
    
    public ProgressoService(Usuario usuario) {
        this.usuario = usuario;
        this.progressoModulos = new HashMap<>();
        this.conquistasDesbloqueadas = new HashSet<>();
        inicializarDicas();
        carregarProgresso();
    }
    
    /**
     * Verifica se um módulo está liberado baseado no progresso
     */
    public boolean isModuloLiberado(int indiceModulo) {
        switch (indiceModulo) {
            case 0: // Conceitos Básicos
            case 1: // Vídeo Aulas  
            case 4: // Dicas Diárias
            case 5: // Conquistas
                return true; // Sempre liberados
                
            case 2: // Simulador
                return getProgressoModulo("MaterialView") >= 50;
                
            case 3: // Quiz
                return getProgressoModulo("VideoView") >= 30 && 
                       getProgressoModulo("SimuladorView") >= 20;
                
            default:
                return false;
        }
    }
    
    /**
     * Calcula progresso geral do usuário (0.0 a 1.0)
     */
    public double calcularProgressoGeral() {
        if (progressoModulos.isEmpty()) {
            return 0.0;
        }
        
        int totalProgresso = progressoModulos.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        
        int maxPossivel = progressoModulos.size() * 100;
        return (double) totalProgresso / maxPossivel;
    }
    
    /**
     * Registra acesso a um módulo e atualiza progresso
     */
    public void registrarAcessoModulo(String nomeModulo) {
        String chaveModulo = extrairChaveModulo(nomeModulo);
        int progressoAtual = getProgressoModulo(chaveModulo);
        
        // Incrementar progresso (máximo 100)
        int novoProgresso = Math.min(progressoAtual + 10, 100);
        progressoModulos.put(chaveModulo, novoProgresso);
        
        // Verificar conquistas
        verificarNovasConquistas(chaveModulo, novoProgresso);
        
        // Salvar automaticamente
        salvarProgresso();
    }
    
    /**
     * Obtém dica personalizada baseada no dia e progresso
     */
    public String obterDicaDoDia() {
        int indiceDia = LocalDate.now().getDayOfYear() % dicasDiarias.size();
        String dicaBase = dicasDiarias.get(indiceDia);
        
        // Personalizar baseado no progresso
        double progresso = calcularProgressoGeral();
        
        if (progresso < 0.2) {
            return "🌱 " + dicaBase + " (Comece devagar, você está no caminho certo!)";
        } else if (progresso < 0.5) {
            return "📈 " + dicaBase + " (Você está progredindo bem!)";
        } else if (progresso < 0.8) {
            return "🎯 " + dicaBase + " (Quase lá, continue assim!)";
        } else {
            return "🏆 " + dicaBase + " (Excelente progresso, você é um expert!)";
        }
    }
    
    /**
     * Obtém lista de conquistas do usuário
     */
    public Set<String> getConquistasDesbloqueadas() {
        return new HashSet<>(conquistasDesbloqueadas);
    }
    
    /**
     * Salva progresso (implementar persistência real)
     */
    public void salvarProgresso() {
        // TODO: Implementar salvamento em arquivo ou banco de dados
        System.out.println("Progresso salvo para: " + usuario.getNome());
        System.out.println("Módulos: " + progressoModulos);
        System.out.println("Conquistas: " + conquistasDesbloqueadas);
    }
    
    // Métodos privados auxiliares
    
    private void carregarProgresso() {
        // TODO: Implementar carregamento de arquivo ou banco
        // Por enquanto, inicializar com valores padrão
        progressoModulos.put("MaterialView", 20);
        progressoModulos.put("VideoView", 15);
        progressoModulos.put("SimuladorView", 0);
        progressoModulos.put("QuizView", 0);
    }
    
    private int getProgressoModulo(String chaveModulo) {
        return progressoModulos.getOrDefault(chaveModulo, 0);
    }
    
    private String extrairChaveModulo(String nomeModulo) {
        // Converter "📚 Conceitos Básicos" -> "MaterialView"
        Map<String, String> mapeamento = Map.of(
            "📚 Conceitos Básicos", "MaterialView",
            "🎥 Vídeo Aulas", "VideoView", 
            "🧮 Simulador", "SimuladorView",
            "📊 Avaliações", "QuizView",
            "💡 Dicas Diárias", "AlertasView",
            "🏆 Conquistas", "ConquistasView"
        );
        
        return mapeamento.getOrDefault(nomeModulo, nomeModulo);
    }
    
    private void verificarNovasConquistas(String modulo, int progresso) {
        List<String> novasConquistas = new ArrayList<>();
        
        // Conquista por progresso em módulo específico
        if (progresso >= 50 && !conquistasDesbloqueadas.contains("meio_" + modulo)) {
            novasConquistas.add("meio_" + modulo);
            conquistasDesbloqueadas.add("meio_" + modulo);
        }
        
        if (progresso >= 100 && !conquistasDesbloqueadas.contains("completo_" + modulo)) {
            novasConquistas.add("completo_" + modulo);
            conquistasDesbloqueadas.add("completo_" + modulo);
        }
        
        // Conquistas por progresso geral
        double progressoGeral = calcularProgressoGeral();
        if (progressoGeral >= 0.25 && !conquistasDesbloqueadas.contains("iniciante")) {
            novasConquistas.add("iniciante");
            conquistasDesbloqueadas.add("iniciante");
        }
        
        if (progressoGeral >= 0.50 && !conquistasDesbloqueadas.contains("intermediario")) {
            novasConquistas.add("intermediario");
            conquistasDesbloqueadas.add("intermediario");
        }
        
        if (progressoGeral >= 0.75 && !conquistasDesbloqueadas.contains("avancado")) {
            novasConquistas.add("avancado");
            conquistasDesbloqueadas.add("avancado");
        }
        
        if (progressoGeral >= 1.0 && !conquistasDesbloqueadas.contains("mestre")) {
            novasConquistas.add("mestre");
            conquistasDesbloqueadas.add("mestre");
        }
        
        // Mostrar notificação para novas conquistas
        if (!novasConquistas.isEmpty()) {
            mostrarNotificacaoConquista(novasConquistas);
        }
    }
    
    private void mostrarNotificacaoConquista(List<String> conquistas) {
        // TODO: Implementar notificação visual
        System.out.println("🏆 Novas conquistas desbloqueadas: " + conquistas);
    }
    
    private void inicializarDicas() {
        dicasDiarias = Arrays.asList(
            "Defina um orçamento mensal e tente seguir 80% dele",
            "Separe 10% da sua renda para uma reserva de emergência", 
            "Evite compras por impulso - espere 24h antes de decidir",
            "Negocie sempre: contas, cartão de crédito, financiamentos",
            "Use aplicativos para controlar seus gastos diários",
            "Invista em conhecimento - cursos podem aumentar sua renda",
            "Compare preços antes de fazer compras importantes",
            "Pague as contas em dia para evitar juros e multas",
            "Tenha objetivos financeiros claros e mensuráveis",
            "Diversifique seus investimentos para reduzir riscos",
            "Aprenda sobre diferentes tipos de investimento",
            "Use o cartão de crédito com responsabilidade",
            "Faça um planejamento para quitar dívidas",
            "Renda extra pode vir de hobbies e habilidades",
            "Economize energia para reduzir contas mensais"
        );
    }
}