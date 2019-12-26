package view;

import model.Drawable;

import javax.swing.*;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Instance třídy {@code ListOfObjectPanel}
 *
 * @author Stanislav Čapek
 */
public class ListOfDrawablePanel extends JPanel {

    //== KONSTANTNÍ ATRIBUTY TŘÍDY =================================================
    //== PROMĚNNÉ ATRIBUTY TŘÍDY ===================================================
    //== STATICKÝ INICIALIZAČNÍ BLOK - STATICKÝ KONSTRUKTOR ========================
    //== KONSTANTNÍ ATRIBUTY INSTANCÍ ==============================================
    private final JList<Drawable> view;
    private final ButtonGroup buttonsOfViewObjects = new ButtonGroup();
    private final JButton btnAddPoint;
    //== PROMĚNNÉ ATRIBUTY INSTANCÍ ================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ TŘÍDY ========================================
    //== OSTATNÍ NESOUKROMÉ METODY TŘÍDY ===========================================

    //== KONSTRUKTORY A TOVÁRNÍ METODY =============================================
    public ListOfDrawablePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        view = new JList<>();
        view.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        view.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                final Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                final Drawable element = ((Drawable) list.getModel().getElementAt(index));
                component.setForeground(element.getColor());

                return component;
            }
        });

        final JRadioButton rBtnSolids = new JRadioButton("Solids");
        rBtnSolids.setActionCommand("solids");
        final JRadioButton rBtnCurves = new JRadioButton("Curves");
        rBtnCurves.setActionCommand("curves");
        buttonsOfViewObjects.add(rBtnSolids);
        buttonsOfViewObjects.add(rBtnCurves);

        final JScrollPane sp = new JScrollPane(view);
        final Dimension prefDimension = new Dimension(200, 300);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Objekty ve scéně"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        this.setPreferredSize(prefDimension);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(rBtnSolids);
        panel.add(rBtnCurves);
        this.add(panel);
        this.add(sp);

        btnAddPoint = new JButton("přidat bod");
        btnAddPoint.setActionCommand("add");
        panel = new JPanel();
        panel.add(btnAddPoint);
        this.add(panel);

        this.add(Box.createVerticalGlue());
    }

    //== ABSTRAKTNÍ METODY =========================================================
    //== PŘÍSTUPOVÉ METODY VLASTNOSTÍ INSTANCÍ =====================================


    public JList<Drawable> getView() {
        return view;
    }

    public ButtonGroup getButtonsOfViewObjects() {
        return buttonsOfViewObjects;
    }

    public JButton getBtnAddPoint() {
        return btnAddPoint;
    }

    //== OSTATNÍ NESOUKROMÉ METODY INSTANCÍ ========================================}
    //== SOUKROMÉ A POMOCNÉ METODY TŘÍDY ===========================================
    //== SOUKROMÉ A POMOCNÉ METODY INSTANCÍ ========================================
    //== INTERNÍ DATOVÉ TYPY =======================================================
    //== TESTY A METODA MAIN =======================================================

}
