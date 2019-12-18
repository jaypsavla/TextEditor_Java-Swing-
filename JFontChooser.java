import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;

public class JFontChooser {
	
	static String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	static String[] styles = {"Normal", "Italic", "Bold", "Bold Italic"};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static JList fontNames = new JList(fonts);
	static JScrollPane selectFont = new JScrollPane(fontNames);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static JComboBox fontStyles = new JComboBox(styles);
	
	static SpinnerNumberModel sizes = new SpinnerNumberModel(12, 6, 120, 1);
	static JSpinner spn = new JSpinner(sizes);
	
	static JLabel sampleText = new JLabel("Sample Text");
	static Font returnFont = null;
	
	public static Font showDialog(JFrame parent, Font font) {
		// TODO Auto-generated method stub
		JDialog jdlg = new JDialog(parent, "SELECT FONT", true);
		JButton select = new JButton("Select");
		
		jdlg.getContentPane().setLayout(new FlowLayout());
		
		fontNames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (fontNames.getSelectedValue() == null ) {
			fontNames.setSelectedValue("Courier New", true);
		}
		else {
			fontNames.setSelectedValue(fontNames.getSelectedValue(), true);
		}	
		
		fontNames.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				updateSampleText();
			}	
		});
		
		fontStyles.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				updateSampleText();
			}
		});
		
		spn.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				updateSampleText();
			}	
		});	
		
		select.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (fontNames.getSelectedIndex() != -1 && fontStyles.getSelectedIndex() != -1) {
					returnFont = sampleText.getFont();
					jdlg.setVisible(false);
				}
			}
		});
		
		jdlg.add(selectFont);
		jdlg.add(fontStyles);
		jdlg.add(spn);
		jdlg.add(sampleText);
		jdlg.add(select);
		
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - jdlg.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - jdlg.getHeight()) / 2);
	    jdlg.setLocation(x, y);
		jdlg.setSize(750, 450);
		jdlg.setLocationRelativeTo(null);
		
		jdlg.setVisible(true);
		
		return returnFont;	
	}	
	
	private static void updateSampleText() {
		selectFont.setColumnHeaderView(new JLabel((String) fontNames.getSelectedValue()));	
		int currentFontStyle = fontStyles.getSelectedIndex();
		if (fontNames.getSelectedIndex() != -1 && currentFontStyle != -1) {
			switch (currentFontStyle) {
			case 0:
				sampleText.setFont(new Font(fonts[fontNames.getSelectedIndex()], Font.PLAIN, (Integer) spn.getValue()));
				break;
			case 1:
				sampleText.setFont(new Font(fonts[fontNames.getSelectedIndex()], Font.ITALIC, (Integer) spn.getValue()));
				break;
			case 2:
				sampleText.setFont(new Font(fonts[fontNames.getSelectedIndex()], Font.BOLD, (Integer) spn.getValue()));
				break;
			case 3:
				sampleText.setFont(new Font(fonts[fontNames.getSelectedIndex()], Font.BOLD + Font.ITALIC, (Integer) spn.getValue()));
				break;
			}
		}
	}
}