import java.util.Random;

public class NoConstante extends No {
    private String valor;
    private static final String[] range = {"x", "(-10)", "(-9)", "(-8)", "(-7)", "(-6)", "(-5)", "(-4)", "(-3)", "(-2)", "(-1)", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    public NoConstante() {
    	int posicaoRange = new Random().nextInt(range.length);
    	valor = range[posicaoRange];
    }

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}