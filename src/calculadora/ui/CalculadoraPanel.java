package calculadora.ui;

import calculadora.model.Calculadora;
import calculadora.model.Operacao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/** Painel reutilizável, também usado para as capturas da documentação. */
public final class CalculadoraPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Color FUNDO = new Color(242, 245, 250);
    private static final Color AZUL = new Color(40, 83, 185);
    private final Calculadora calculadora = new Calculadora();
    private final JTextField primeiro = campo("Primeiro valor");
    private final JTextField segundo = campo("Segundo valor");
    private final JTextField resultado = campo("Resultado");
    private final JLabel mensagem = new JLabel("Informe os valores e escolha uma operação.");
    private final JLabel expressao = new JLabel("Seu resultado aparecerá aqui");
    private Operacao ultimaOperacao = Operacao.SOMA;

    public CalculadoraPanel() {
        super(new BorderLayout(0, 22));
        setBackground(FUNDO);
        setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));
        setPreferredSize(new Dimension(540, 540));

        JPanel cabecalho = bloco(new GridLayout(2, 1, 0, 8));
        JLabel titulo = new JLabel("Calculadora");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(AZUL);
        cabecalho.add(titulo);
        cabecalho.add(new JLabel("Quatro operações. Um cálculo simples."));
        add(cabecalho, BorderLayout.NORTH);

        JPanel corpo = bloco(new BorderLayout(0, 20));
        JPanel entradas = bloco(new GridLayout(1, 2, 16, 0));
        entradas.add(entrada("Primeiro valor", primeiro));
        entradas.add(entrada("Segundo valor", segundo));
        corpo.add(entradas, BorderLayout.NORTH);

        JPanel operacoes = bloco(new GridLayout(1, 4, 12, 0));
        for (Operacao operacao : Operacao.values()) {
            JButton botao = new JButton(operacao.getSimbolo());
            botao.setFont(new Font("SansSerif", Font.BOLD, 24));
            botao.setForeground(AZUL);
            botao.setBackground(Color.WHITE);
            botao.setFocusPainted(true);
            botao.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(207, 216, 232)),
                    BorderFactory.createEmptyBorder(10, 8, 10, 8)));
            botao.setToolTipText(operacao.name());
            botao.getAccessibleContext().setAccessibleName(operacao.name());
            botao.addActionListener(evento -> calcular(operacao));
            operacoes.add(botao);
        }
        corpo.add(operacoes, BorderLayout.CENTER);

        JPanel saida = bloco(new BorderLayout(0, 8));
        saida.add(new JLabel("RESULTADO"), BorderLayout.NORTH);
        resultado.setEditable(false);
        resultado.setForeground(AZUL);
        resultado.setFont(new Font("SansSerif", Font.BOLD, 26));
        saida.add(resultado, BorderLayout.CENTER);
        saida.add(expressao, BorderLayout.SOUTH);
        corpo.add(saida, BorderLayout.SOUTH);
        add(corpo, BorderLayout.CENTER);

        JPanel rodape = bloco(new BorderLayout(0, 14));
        mensagem.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rodape.add(mensagem, BorderLayout.NORTH);
        JButton limpar = new JButton("Limpar  ·  Esc");
        limpar.addActionListener(evento -> limpar());
        rodape.add(limpar, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
        primeiro.addActionListener(evento -> calcular(ultimaOperacao));
        segundo.addActionListener(evento -> calcular(ultimaOperacao));
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "limpar");
        getActionMap().put("limpar", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            @Override public void actionPerformed(ActionEvent evento) { limpar(); }
        });
    }

    private static JTextField campo(String nome) {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 20));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(207, 216, 232)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        campo.getAccessibleContext().setAccessibleName(nome);
        return campo;
    }

    private static JPanel bloco(java.awt.LayoutManager layout) {
        JPanel painel = new JPanel(layout);
        painel.setOpaque(false);
        return painel;
    }

    private static JPanel entrada(String nome, JTextField campo) {
        JPanel painel = bloco(new BorderLayout(0, 8));
        JLabel rotulo = new JLabel(nome);
        rotulo.setLabelFor(campo);
        painel.add(rotulo, BorderLayout.NORTH);
        painel.add(campo, BorderLayout.CENTER);
        return painel;
    }

    public void definirValores(String valor1, String valor2) {
        primeiro.setText(valor1);
        segundo.setText(valor2);
    }

    public void calcular(Operacao operacao) {
        ultimaOperacao = operacao;
        try {
            BigDecimal valor1 = calculadora.lerNumero(primeiro.getText());
            BigDecimal valor2 = calculadora.lerNumero(segundo.getText());
            resultado.setText(calculadora.formatar(calculadora.calcular(valor1, valor2, operacao)));
            expressao.setText(calculadora.formatar(valor1) + " " + operacao.getSimbolo()
                    + " " + calculadora.formatar(valor2));
            mensagem.setForeground(new Color(35, 115, 75));
            mensagem.setText("Cálculo concluído. Enter repete a operação.");
        } catch (IllegalArgumentException erro) {
            resultado.setText("");
            expressao.setText("Revise os valores e tente novamente.");
            mensagem.setForeground(new Color(175, 40, 40));
            mensagem.setText(erro.getMessage());
        }
    }

    public void limpar() {
        definirValores("", "");
        resultado.setText("");
        expressao.setText("Seu resultado aparecerá aqui");
        mensagem.setForeground(Color.DARK_GRAY);
        mensagem.setText("Informe os valores e escolha uma operação.");
        ultimaOperacao = Operacao.SOMA;
        primeiro.requestFocusInWindow();
    }
}
