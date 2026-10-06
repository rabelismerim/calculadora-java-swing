package calculadora.model;

import java.math.BigDecimal;
import java.math.MathContext;

/** Cálculos decimais e validação independentes da interface. */
public final class Calculadora {
    public BigDecimal lerNumero(String texto) {
        String valor = texto == null ? "" : texto.trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Preencha os dois valores para calcular.");
        }
        if (!valor.matches("[+-]?(?:[0-9]+(?:[.,][0-9]+)?|[.,][0-9]+)")) {
            throw new IllegalArgumentException("Digite um número válido, como 12,5 ou -3.");
        }
        return new BigDecimal(valor.replace(',', '.'));
    }

    public BigDecimal calcular(BigDecimal primeiro, BigDecimal segundo, Operacao operacao) {
        switch (operacao) {
            case SOMA: return primeiro.add(segundo);
            case SUBTRACAO: return primeiro.subtract(segundo);
            case MULTIPLICACAO: return primeiro.multiply(segundo);
            case DIVISAO:
                if (segundo.signum() == 0) {
                    throw new IllegalArgumentException("Não é possível dividir por zero.");
                }
                return primeiro.divide(segundo, MathContext.DECIMAL64);
            default: throw new IllegalArgumentException("Operação desconhecida.");
        }
    }

    public String formatar(BigDecimal numero) {
        return numero.stripTrailingZeros().toPlainString().replace('.', ',');
    }
}
