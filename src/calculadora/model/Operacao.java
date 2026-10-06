package calculadora.model;

public enum Operacao {
    SOMA("+"), SUBTRACAO("−"), MULTIPLICACAO("×"), DIVISAO("÷");

    private final String simbolo;

    Operacao(String simbolo) { this.simbolo = simbolo; }

    public String getSimbolo() { return simbolo; }
}
