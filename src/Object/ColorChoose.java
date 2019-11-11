package Object;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Frame.WhiteBoardClient;

/**
 * @author Internet source
 *
 */
@SuppressWarnings("serial")
public class ColorChoose extends JFrame implements ChangeListener {
	
    public static JColorChooser tcc;
    public ColorChoose() {
    	setTitle("Choose Color");
    	setVisible(true);
    	setSize(600, 400);
    	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel bannerPanel = new JPanel(new BorderLayout());

        tcc = new JColorChooser();
        tcc.getSelectionModel().addChangeListener(this);

        this.add(bannerPanel, BorderLayout.CENTER);
        this.add(tcc, BorderLayout.NORTH);
        JButton btn = new JButton("OK");
        btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
        bannerPanel.add(btn, BorderLayout.SOUTH);
    }

    public void stateChanged(ChangeEvent e) {
        WhiteBoardClient.selectColor = tcc.getColor();
    }
}
