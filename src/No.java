import java.util.Random;

public class No implements Cloneable {
	private No pai;
	private No filhoEsquerda;
	private No filhoDireita;
	public Integer pontoCorteAtual = 0;
	
	public int geraPontoCorte() {
		int pontoCorte = new Random().nextInt(this.qtNos(this) - 1);
		
		if (pontoCorte == 0) {
			pontoCorte = geraPontoCorte();
		}
		
		return pontoCorte;
	}
	
	public No encontraGeneParaRecombinar(No noAtual, int pontoCorte) {
		if (noAtual == null) {
			pontoCorteAtual--;
			return null;
		}
		
		if (pontoCorteAtual == pontoCorte) {
			try {
				No noRetorno = (No) noAtual.clone();
				NoOperacao no1 = null;
				NoConstante no2 = null;
				if (noAtual instanceof NoOperacao) {
					no1 = (NoOperacao) noAtual;
					no1.setOperador('$');
				} else {
					no2 = (NoConstante) noAtual;
					no2.setValor("$");
				}
				
				return noRetorno;
			} catch (CloneNotSupportedException e) {
				System.out.println("Erro ao clonar objeto na posição " + pontoCorteAtual);
			}
		}
		
		No noRetorno = null;
		if (pontoCorteAtual < pontoCorte) {
		    ++pontoCorteAtual;
			noRetorno = encontraGeneParaRecombinar(noAtual.getFilhoEsquerda(), pontoCorte);
			if (noRetorno == null) {
			    ++pontoCorteAtual;
				noRetorno = encontraGeneParaRecombinar(noAtual.getFilhoDireita(), pontoCorte);
			}
		}
		
		return noRetorno;
	}

	public int qtNos(No noAtual) {
		if (noAtual == null) {
			return 1;
		}
		
		int he = qtNos(noAtual.getFilhoEsquerda());
		int hd = qtNos(noAtual.getFilhoDireita());
		
		return he + hd;
	}
	
	public void recombina(No noAtual, No geneParaTroca, boolean esquerda) {
		if (noAtual == null) {
			return;
		}
		
		NoOperacao no1 = null;
		NoConstante no2 = null;
		if (noAtual instanceof NoOperacao) {
			no1 = (NoOperacao) noAtual;
			if (no1.getOperador() == '$') {
				if (esquerda) {
					noAtual.getPai().setFilhoEsquerda(geneParaTroca);
				} else {
					noAtual.getPai().setFilhoDireita(geneParaTroca);
				}
				return;
			}
		} else {
			no2 = (NoConstante) noAtual;
			if (no2.getValor().equals("$")) {
				if (esquerda) {
					noAtual.getPai().setFilhoEsquerda(geneParaTroca);
				} else {
					noAtual.getPai().setFilhoDireita(geneParaTroca);
				}
				return;
			}
		}
		
		recombina(noAtual.getFilhoEsquerda(), geneParaTroca, true);
		recombina(noAtual.getFilhoDireita(), geneParaTroca, false);
	}
	
	public void limpaCoringa(No noAtual) {
	    if (noAtual == null) {
            return;
        }
	    
	    if (noAtual.getFilhoEsquerda() != null) {
	        if (noAtual instanceof NoOperacao) {
	            if (((NoOperacao) noAtual.getFilhoEsquerda().getPai()).getOperador() == '$') {
	                noAtual.getFilhoEsquerda().setPai(noAtual);
	            }
	        }
	    }
	    if (noAtual.getFilhoDireita() != null) {
	        if (noAtual instanceof NoOperacao) {
	            if (((NoOperacao) noAtual.getFilhoDireita().getPai()).getOperador() == '$') {
	                noAtual.getFilhoDireita().setPai(noAtual);
	            }
	        }
	    }
	    
	    limpaCoringa(noAtual.getFilhoEsquerda());
	    limpaCoringa(noAtual.getFilhoDireita());
	}
	
	public int altura(No noAtual, int altura) {
		if (noAtual != null) {
			if (noAtual instanceof NoOperacao) {
				int he, hd;
				
				he = altura(noAtual.getFilhoEsquerda(), altura + 1);
				hd = altura(noAtual.getFilhoDireita(), altura + 1);
				
				return Math.max(he, hd);
			} else {
				return altura;
			}
		}
		
		return 0;
	}
	
	public void geraCromossomo(No noAtual, int alturaMaxima, int alturaAtual) {
		int r1 = new Random().nextInt(2);
		int r2 = new Random().nextInt(2);
		
		No filhoEsquerda;
		No filhoDireita;
		if (r1 + r2 == 2 || alturaAtual == (alturaMaxima - 1)) {
			filhoEsquerda = new NoConstante();
			filhoDireita = new NoConstante();
			
			noAtual.setFilhoEsquerda(filhoEsquerda);
			noAtual.setFilhoDireita(filhoDireita);
			return;
		}
		
		if (r1 == 0) {
			//filho esquerda
			filhoEsquerda = new NoOperacao();
			noAtual.setFilhoEsquerda(filhoEsquerda);
			geraCromossomo(filhoEsquerda, alturaMaxima, alturaAtual + 1);
		} else {
			filhoEsquerda = new NoConstante();
			noAtual.setFilhoEsquerda(filhoEsquerda);
		}
		
		if (r2 == 0) {
			//filho direita
			filhoDireita = new NoOperacao();
			noAtual.setFilhoDireita(filhoDireita);
			geraCromossomo(filhoDireita, alturaMaxima, alturaAtual + 1);
		} else {
			filhoDireita = new NoConstante();
			noAtual.setFilhoDireita(filhoDireita);
		}
	}
	
	public String geraExpressao(No noAtual, boolean esquerda) {
		if (noAtual == null) {
			return "";
		}
		
		String expressao = "";
		
		if (noAtual instanceof NoOperacao) {
			if (noAtual.getFilhoEsquerda() instanceof NoOperacao) {
				((NoOperacao) noAtual).setAdd(true);
			}
			expressao += "(";
		} else {
			if (esquerda) {
				expressao += ((NoConstante) noAtual).getValor() + ((NoOperacao) noAtual.getPai()).getOperador();
			} else {
				expressao += ((NoConstante) noAtual).getValor() + ")";
			}
		}
		
		expressao += geraExpressao(noAtual.getFilhoEsquerda(), true);
		expressao += geraExpressao(noAtual.getFilhoDireita(), false);
		if (noAtual instanceof NoOperacao) {
			if (!esquerda) {
				if (!((NoOperacao) noAtual).isRaiz()) {
					expressao += ")";
				}
			} else {
				if (!((NoOperacao) noAtual).isRaiz()) {
					if (((NoOperacao) ((NoOperacao) noAtual).getPai()).isRaiz()) {
						expressao += ((NoOperacao) ((NoOperacao) noAtual).getPai()).getOperador();
					} else {
						expressao += ((NoOperacao) noAtual).getOperador();
						((NoOperacao) noAtual).setAdd(false);
					}
				}
			}
		}
		
		return expressao;
	}
	
	public void setPai(No pai) {
	    this.pai = pai;
	}
	
	public void setFilhoEsquerda(No fe) {
	    fe.setPai(this);
	    filhoEsquerda = fe;
	}
	
	public void setFilhoDireita(No fd) {
	    fd.setPai(this);
	    filhoDireita = fd;
	}
	
	public No getPai() {
		return pai;
	}

	public No getFilhoEsquerda() {
		return filhoEsquerda;
	}

	public No getFilhoDireita() {
		return filhoDireita;
	}

    public Integer getPontoCorteAtual() {
        return pontoCorteAtual;
    }

    public void setPontoCorteAtual(Integer pontoCorteAtual) {
        this.pontoCorteAtual = pontoCorteAtual;
    }
}