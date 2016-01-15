import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;


public class Main {

    private static ScriptEngineManager mgr;
    private static ScriptEngine engine;
    
	public static void main(String[] args) throws ScriptException {
	    mgr = new ScriptEngineManager();
	    engine = mgr.getEngineByName("JavaScript");
	    
        List<String> valoresX = new ArrayList<String>();
        List<Float> valoresY = new ArrayList<Float>();
        for (int i = 1; i <= 10; i++) {
			valoresX.add(String.valueOf(i));
		}
        valoresY.add(0.67f);
        valoresY.add(2.00f);
        valoresY.add(4.00f);
        valoresY.add(6.67f);
        valoresY.add(10.00f);
        valoresY.add(14.00f);
        valoresY.add(18.67f);
        valoresY.add(24.00f);
        valoresY.add(30.00f);
        valoresY.add(36.67f);
        
        int epocas, alturaMax, qtPopulacao;
        List<NoOperacao> arvoresOrdenadas = new ArrayList<NoOperacao>();
        
        epocas = Integer.parseInt(JOptionPane.showInputDialog("Amount of epochs:"));
        qtPopulacao = Integer.parseInt(JOptionPane.showInputDialog("Amount of individuals in the initial population:"));
        alturaMax = Integer.parseInt(JOptionPane.showInputDialog("Max height of tree:"));
        
        List<NoOperacao> populacao = new ArrayList<NoOperacao>();
        geraPopulacao(populacao, alturaMax, qtPopulacao);
        
        for (int i = 0; i < epocas; i++) {
			ordenaPopulacao(valoresX, valoresY, arvoresOrdenadas, populacao);
            
            if (arvoresOrdenadas.size() == 1)
            	break;
            
            populacao = recombina(arvoresOrdenadas);
            System.out.println("Current epoch: " + i);
        }
		
        if (arvoresOrdenadas.size() != 1) {
            ordenaPopulacao(valoresX, valoresY, arvoresOrdenadas, populacao);
        }
        
        System.out.println("Best individual: " + arvoresOrdenadas.get(0).geraExpressao(arvoresOrdenadas.get(0), true));
        System.out.println("Fitness: " + arvoresOrdenadas.get(0).getFitness());
	}

	private static void ordenaPopulacao(List<String> valoresX, List<Float> valoresY, List<NoOperacao> arvoresOrdenadas, List<NoOperacao> populacao) throws ScriptException {
	    List<Float> fitnessOrdenada = new ArrayList<Float>();
		for (NoOperacao noOperacao : populacao) {
			noOperacao.setPontoCorteAtual(0);
			float fit = calculaFitness(noOperacao.geraExpressao(noOperacao, true), valoresX, valoresY);
			noOperacao.setFitness(fit);
			fitnessOrdenada.add(fit);
		}
		Collections.sort(fitnessOrdenada);
		
		int cont = 0;
		do {
		    if (fitnessOrdenada.isEmpty()) {
		        break;
		    }
		    
		    if (fitnessOrdenada.get(0) == populacao.get(cont).getFitness()) {
		        if (fitnessOrdenada.get(0) == 0f) {
		            arvoresOrdenadas.add(populacao.get(cont));
		            return;
		        }
		        arvoresOrdenadas.add(populacao.get(cont));
		        populacao.remove(cont);
		        fitnessOrdenada.remove(0);
		        cont = 0;
	            continue;
		    }
		    cont++;
		} while (!fitnessOrdenada.isEmpty());
	}

    private static List<NoOperacao> recombina(List<NoOperacao> arvoresOrdenadas) {
        NoOperacao filho1, filho2;
        List<NoOperacao> filhos = new ArrayList<NoOperacao>();
        
        if (arvoresOrdenadas.size() % 2 == 1) {
        	arvoresOrdenadas.remove(arvoresOrdenadas.size() - 1);
        }
        
        do {
        	filho1 = null;
			filho1 = arvoresOrdenadas.get(0);
            arvoresOrdenadas.remove(0);
            
            filho2 = arvoresOrdenadas.get(0);
            arvoresOrdenadas.remove(0);
            
            No pai1 = filho1.encontraGeneParaRecombinar(filho1, filho1.geraPontoCorte());
            No pai2 = filho2.encontraGeneParaRecombinar(filho2, filho2.geraPontoCorte());
            
            filho1.recombina(filho1, pai2, true);
            filho2.recombina(filho2, pai1, true);
            
            filho1.limpaCoringa(filho1);
            filho2.limpaCoringa(filho2);
            
            filhos.add(filho1);
            filhos.add(filho2);
        } while (!arvoresOrdenadas.isEmpty());
        
        return filhos;
    }		
		
	private static void geraPopulacao(List<NoOperacao> populacao, int alturaMax, int qtPopulacao) {
	    NoOperacao arvore = null;
	    for (int i = 0; i < qtPopulacao; i++) {
	        arvore = new NoOperacao();
	        arvore.setRaiz(true);
	        arvore.geraCromossomo(arvore, alturaMax, 0);
	        populacao.add(arvore);
        }
	}
	
	private static float calculaFitness(String expressao, List<String> valoresX, List<Float> valoresY) throws ScriptException {
	    String validacao = "", valorComputado = "";
	    float valorComputadoReal, fitness = 0f;
	    
	    for (int i = 0; i < valoresX.size(); i++) {
            validacao = expressao.replaceAll("x", valoresX.get(i));
            valorComputado =  String.valueOf(engine.eval(validacao));
            if (valorComputado.contains("Infinity") || valorComputado.contains("NaN")) {
                valorComputadoReal = 9999f;
            } else {
                valorComputadoReal = Float.parseFloat(valorComputado);
            }
            
            fitness += valorComputadoReal - valoresY.get(i);
        }
	    
	    return (float) (Math.sqrt(Math.pow(fitness, 2)) / valoresX.size());
	}
}