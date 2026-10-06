package calculadora;

import calculadora.model.Calculadora;
import calculadora.model.Operacao;
import java.math.BigDecimal;

/** Testes executáveis sem bibliotecas externas. */
public final class CalculadoraTest {
    private static final Calculadora CALCULADORA = new Calculadora();
    private static int verificacoes;

    public static void main(String[] args) {
        conferir("0,3", "0,1", "0,2", Operacao.SOMA);
        conferir("-5", "2", "7", Operacao.SUBTRACAO);
        conferir("3,75", "1,5", "2.5", Operacao.MULTIPLICACAO);
        conferir("2,5", "5", "2", Operacao.DIVISAO);
        conferir("0,3333333333333333", "1", "3", Operacao.DIVISAO);
        conferir("100000000000000000001", "100000000000000000000", "1", Operacao.SOMA);
        igual("-0,5", CALCULADORA.formatar(CALCULADORA.lerNumero("  -0,50  ")));
        igual("0,5", CALCULADORA.formatar(CALCULADORA.lerNumero(".5")));
        for (String entrada : new String[] {"", " ", "abc", "NaN", "Infinity", "1,2.3", "1.000,50", null}) {
            rejeitar(() -> CALCULADORA.lerNumero(entrada));
        }
        rejeitar(() -> CALCULADORA.calcular(BigDecimal.ONE, BigDecimal.ZERO, Operacao.DIVISAO));
        rejeitar(() -> CALCULADORA.calcular(BigDecimal.ONE, new BigDecimal("-0.00"), Operacao.DIVISAO));
        System.out.println(verificacoes + " verificações passaram.");
    }

    private static void conferir(String esperado, String a, String b, Operacao operacao) {
        igual(esperado, CALCULADORA.formatar(CALCULADORA.calcular(
                CALCULADORA.lerNumero(a), CALCULADORA.lerNumero(b), operacao)));
    }

    private static void igual(String esperado, String atual) {
        if (!esperado.equals(atual)) throw new AssertionError(esperado + " != " + atual);
        verificacoes++;
    }

    private static void rejeitar(Runnable acao) {
        try { acao.run(); }
        catch (IllegalArgumentException esperado) { verificacoes++; return; }
        throw new AssertionError("Entrada inválida foi aceita.");
    }
}
