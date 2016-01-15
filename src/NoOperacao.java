public class NoOperacao extends No {
	private char operador;
	private boolean raiz;
	private boolean add;
	private float fitness;

	public NoOperacao() {
		double prob = Math.random();

		if (prob <= .25) {
			operador = '+';
		} else if (prob > .25 && prob <= .50) {
			operador = '-';
		} else if (prob > .50 && prob <= .75) {
			operador = '*';
		} else {
			operador = '/';
		}
	}

	public void setOperador(char op) {
		operador = op;
	}

	public char getOperador() {
		return operador;
	}

	public boolean isRaiz() {
		return raiz;
	}

	public void setRaiz(boolean raiz) {
		this.raiz = raiz;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

    public float getFitness() {
        return fitness;
    }

    public void setFitness(float fitness) {
        this.fitness = fitness;
    }
}