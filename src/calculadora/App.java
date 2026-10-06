package calculadora;

import calculadora.ui.CalculadoraPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class App {
    private App() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame janela = new JFrame("Calculadora");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setContentPane(new CalculadoraPanel());
            janela.pack();
            janela.setMinimumSize(janela.getSize());
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
