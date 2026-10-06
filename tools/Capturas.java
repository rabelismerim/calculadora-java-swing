import calculadora.model.Operacao;
import calculadora.ui.CalculadoraPanel;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

/** Renderiza o painel Swing real, sem exigir uma janela ou capturar a área de trabalho. */
public final class Capturas {
    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            try {
                CalculadoraPanel painel = new CalculadoraPanel();
                painel.setSize(painel.getPreferredSize());
                salvar(painel, "01-inicial");
                painel.definirValores("12,5", "2");
                painel.calcular(Operacao.MULTIPLICACAO);
                salvar(painel, "02-calculo");
                painel.definirValores("12,5", "0");
                painel.calcular(Operacao.DIVISAO);
                salvar(painel, "03-validacao");
            } catch (IOException erro) { throw new RuntimeException(erro); }
        });
    }

    private static void layout(Component componente) {
        if (componente instanceof java.awt.Container) {
            java.awt.Container container = (java.awt.Container) componente;
            container.doLayout();
            for (Component filho : container.getComponents()) layout(filho);
        }
    }

    private static void salvar(CalculadoraPanel painel, String nome) throws IOException {
        layout(painel);
        BufferedImage imagem = new BufferedImage(painel.getWidth(), painel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D grafico = imagem.createGraphics();
        grafico.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        painel.printAll(grafico);
        grafico.dispose();
        File pasta = new File("docs/screenshots");
        if (!pasta.isDirectory() && !pasta.mkdirs()) throw new IOException("Não foi possível criar " + pasta);
        ImageIO.write(imagem, "png", new File(pasta, nome + ".png"));
    }
}
