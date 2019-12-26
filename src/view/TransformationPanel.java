package view;

import javax.swing.*;
import java.awt.CardLayout;
import java.util.HashMap;
import java.util.Map;

/**
 * Instance třídy {@code TransformationPanel}
 *
 * @author Stanislav Čapek
 */
public class TransformationPanel extends JPanel {
    private final Map<String, JSlider> rotationSliders = new HashMap<>();
    private final Map<String, JSlider> scaleSliders = new HashMap<>();
    private final Map<String, JSpinner> moveJspinner = new HashMap<>();
    private final ButtonGroup buttonGroup;
    private final String[] axis = new String[]{"X", "Y", "Z"};

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================

    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================

    public TransformationPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel btnGroupPanel = new JPanel();
        buttonGroup = new ButtonGroup();

        final JRadioButton rbMove = new JRadioButton("Pohyb");
        rbMove.setSelected(true);
        rbMove.setActionCommand("move");

        final JRadioButton rbRotation = new JRadioButton("Rotace");
        rbRotation.setActionCommand("rotation");

        final JRadioButton rbScale = new JRadioButton("Škálování");
        rbScale.setActionCommand("scale");

        buttonGroup.add(rbMove);
        buttonGroup.add(rbRotation);
        buttonGroup.add(rbScale);
        btnGroupPanel.add(rbMove);
        btnGroupPanel.add(rbRotation);
        btnGroupPanel.add(rbScale);
        this.add(btnGroupPanel);

        final JPanel cards = new JPanel(new CardLayout());
        final JPanel movePanel = getMovePanel2();
        final JPanel rotationPanel = getRotationPanel(0, 360, 90, 30);
        final JPanel scalePanel = getScalePanel(1, 199, 50, 10);


        cards.add(movePanel, "move");
        cards.add(rotationPanel, "rotation");
        cards.add(scalePanel, "scale");
        this.add(cards);

        rbMove.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, "move"));
        rbRotation.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, "rotation"));
        rbScale.addActionListener(e -> ((CardLayout) cards.getLayout()).show(cards, "scale"));
    }


    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================


    public Map<String, JSlider> getRotationSliders() {
        return rotationSliders;
    }

    public Map<String, JSlider> getScaleSliders() {
        return scaleSliders;
    }


    public Map<String, JSpinner> getMoveJspinner() {
        return moveJspinner;
    }

    public String[] getAxis() {
        return axis;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================
    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================

    private JPanel getMovePanel2() {
        JPanel movePanel = new JPanel();
        movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.PAGE_AXIS));

        JPanel panel = new JPanel();
        for (int i = 0; i < axis.length; i++) {
            final JLabel lbl = new JLabel(axis[i]);
            final JSpinner jSpinner = getjSpinner();
            lbl.setLabelFor(jSpinner);
            panel.add(lbl);
            panel.add(jSpinner);
            moveJspinner.put(axis[i], jSpinner);
        }
        movePanel.add(panel);

        return movePanel;
    }

    private JSpinner getjSpinner() {
        final SpinnerNumberModel numberModel = new SpinnerNumberModel(
                0d,
                -1000d,
                1000d,
                1d
        );
        final JSpinner jSpinner = new JSpinner(numberModel);
        final JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(
                jSpinner,
                "#0.00"
        );
        jSpinner.setEditor(numberEditor);
        return jSpinner;
    }

    private JPanel getRotationPanel(int min, int max, int majorTickSpacing, int minorTickSpacing) {
        final JPanel rotationPanel = new JPanel();
        rotationPanel.setLayout(new BoxLayout(rotationPanel, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < axis.length; i++) {
            final JPanel panelWithSlider = getPanelWithSlider(min, max, majorTickSpacing, minorTickSpacing, axis[i]);
            rotationPanel.add(panelWithSlider);
        }

        return rotationPanel;
    }

    private JPanel getScalePanel(int min, int max, int majorTickSpacing, int minorTickSpacing) {
        return getRotationPanel(min, max, majorTickSpacing, minorTickSpacing);
    }

    private JPanel getPanelWithSlider(int min, int max, int majorTickSpacing, int minorTickSpacing, String axis) {
        final JPanel panel = new JPanel();
        final JSlider jSlider = new JSlider(min, max);
        jSlider.setMajorTickSpacing(majorTickSpacing);
        jSlider.setMinorTickSpacing(minorTickSpacing);
        jSlider.setPaintLabels(true);
        jSlider.setPaintTicks(true);
        final JLabel label = new JLabel();
        final String textFormat = String.format("%s: %s", axis, jSlider.getValue());
        label.setText(textFormat);
        jSlider.addChangeListener(e -> label.setText(String.format("%s: %s", axis, jSlider.getValue())));
        panel.add(label);
        panel.add(jSlider);

//        první naplníme mapu pro rotaci poté mapu pro škálování
        if (!rotationSliders.containsKey(axis)) {
            rotationSliders.put(axis, jSlider);
        } else {
            scaleSliders.put(axis, jSlider);
        }
        return panel;
    }
    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
