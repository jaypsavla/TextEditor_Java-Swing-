//
// Name: Savla, Jay
// Project: 5 (Final)
// Due: December 7th, 2018
// Course: CS-2450-01-F18
//
// Description: Creates a text editor that mimics Notepad in function.
// 				 
import java.util.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Utilities;

public class JNotepad {
	
	JFrame jfrm = new JFrame("Untitled - Notepad");
	JTextArea typeHere = new JTextArea();
	JLabel findWhat = new JLabel("Find what");
	JLabel replaceWhat = new JLabel("Replace with");
	JRadioButton up = new JRadioButton("Up");
	JRadioButton down = new JRadioButton("Down", true);
	JTextField toFind = new JTextField(20);
	JTextField replaceWith = new JTextField(20);
	JCheckBox matchCase = new JCheckBox("Match Case");
	JButton findNextButton = new JButton("Find Next");
	JButton replaceButton = new JButton("Replace");
	JButton replaceAllButton = new JButton("Replace All");
	JButton cancel = new JButton("Cancel");	
	int findIndex = 0;
	JMenuItem cut, copy, delete, find, findNext, cutPopup, copyPopup;
	int lineNumber = 1;
	int columnNumber = 1;
	JLabel caretLocation = new JLabel("Ln: " + lineNumber + " Col: " + columnNumber);
	private boolean contentChanged = false;
	private String fileName;
	
	public JNotepad() {
		
		jfrm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		BorderLayout bl = new BorderLayout();
		jfrm.setLayout(bl);
		jfrm.pack();
		jfrm.setSize(800, 600);
		jfrm.setLocationRelativeTo(null);
		jfrm.setIconImage(new ImageIcon("JNotepad.png").getImage());
	
		typeHere.setInheritsPopupMenu(true);
		typeHere.setBackground(Color.WHITE);
		typeHere.setFont(new Font("Courier New", Font.PLAIN, 12));
		typeHere.setLineWrap(true);
		typeHere.setWrapStyleWord(true);
		
		JScrollPane textArea = new JScrollPane(typeHere);

		JMenuBar jmb = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem newFile = new JMenuItem("New");
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		JMenuItem open = new JMenuItem("Open...");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		JMenuItem saveAs = new JMenuItem("Save As...", KeyEvent.VK_A);
		JMenuItem pageSetup = new JMenuItem("Page Setup...", KeyEvent.VK_U);
		JMenuItem print = new JMenuItem("Print...");
		print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
		
		file.add(newFile);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(pageSetup);
		file.add(print);
		file.addSeparator();
		file.add(exit);
		
		JMenu edit = new JMenu("Edit");
		edit.setMnemonic(KeyEvent.VK_E);
		
		JMenuItem undo = new JMenuItem("Undo", KeyEvent.VK_U);
		cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		JMenuItem paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
		delete = new JMenuItem("Delete");
		delete.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
		find = new JMenuItem("Find...");
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		findNext = new JMenuItem("Find Next", KeyEvent.VK_N);
		JMenuItem replace = new JMenuItem("Replace...");
		replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
		JMenuItem goTo = new JMenuItem("Go To...");
		goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
		JMenuItem selectAll = new JMenuItem("Select All");
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
		JMenuItem timeDate = new JMenuItem("Time/Date");
		timeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
		
		edit.add(undo);
		edit.addSeparator();
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.add(delete);
		edit.addSeparator();
		edit.add(find);
		edit.add(findNext);
		edit.add(replace);
		edit.add(goTo);
		edit.addSeparator();
		edit.add(selectAll);
		edit.add(timeDate);
		
		JMenu format = new JMenu("Format");
		format.setMnemonic(KeyEvent.VK_O);
		
		JCheckBoxMenuItem wordWrap = new JCheckBoxMenuItem("Word Wrap", true);
		wordWrap.setMnemonic(KeyEvent.VK_W);
		JMenuItem font = new JMenuItem("Font...", KeyEvent.VK_F);
		
		format.add(wordWrap);
		format.add(font);
		
		JMenu view = new JMenu("View");
		view.setMnemonic(KeyEvent.VK_V);
		
		JCheckBoxMenuItem statusBar = new JCheckBoxMenuItem("Status Bar", false);
		statusBar.setMnemonic(KeyEvent.VK_S);
		statusBar.setEnabled(false);
	
		view.add(statusBar);
		
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem viewHelp = new JMenuItem("View Help", KeyEvent.VK_H);
		JMenuItem about = new JMenuItem("About JNotepad", KeyEvent.VK_A);
		
		help.add(viewHelp);
		help.addSeparator();
		help.add(about);
		
		pageSetup.setEnabled(false);
		print.setEnabled(false);
		undo.setEnabled(false);
		goTo.setEnabled(false);
		
		jmb.add(file);
		jmb.add(edit);
		jmb.add(format);
		jmb.add(view);
		jmb.add(help);
		
		cut.setEnabled(false);
		copy.setEnabled(false);
		delete.setEnabled(false);
		find.setEnabled(false);
		findNext.setEnabled(false);
		
		caretLocation.setHorizontalAlignment(SwingConstants.RIGHT);
		jfrm.setJMenuBar(jmb);
		jfrm.add(textArea, BorderLayout.CENTER);
		jfrm.add(caretLocation, BorderLayout.SOUTH);
		caretLocation.setVisible(false);
		
		JPopupMenu popup = new JPopupMenu();
		cutPopup = new JMenuItem("Cut", KeyEvent.VK_T);
		copyPopup = new JMenuItem("Copy", KeyEvent.VK_C);
		JMenuItem pastePopup = new JMenuItem("Paste", KeyEvent.VK_P);
		popup.add(cutPopup);
		popup.add(copyPopup);
		popup.add(pastePopup);
		
		cutPopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.cut();
			}
		});
		
		copyPopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.copy();
			}
		});
		
		pastePopup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.paste();
			}
		});
		
		typeHere.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				if (me.isPopupTrigger()) {
					popup.show(me.getComponent(), me.getX(), me.getY());
				}
			}
			public void mouseReleased(MouseEvent me) {
				if (me.isPopupTrigger()) {
					popup.show(me.getComponent(), me.getX(), me.getY());
				}
			}
		});
		
		typeHere.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				findIndex = typeHere.getCaretPosition();
				JTextArea changedArea = (JTextArea) e.getSource();
				contentChanged = true;
				try {
					int caretPosition = changedArea.getCaretPosition();
					lineNumber = changedArea.getLineOfOffset(caretPosition) + 1;
					columnNumber = caretPosition - Utilities.getRowStart(changedArea, caretPosition) + 1;
					
				}
				catch (Exception ex ) {}
				updateStatus(lineNumber, columnNumber);	
			}	
		});
		
		typeHere.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void removeUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void changedUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void checkForInput() {
				if (typeHere.getText().length() == 0) {
					cut.setEnabled(false);
					copy.setEnabled(false);
					delete.setEnabled(false);
					find.setEnabled(false);
					findNext.setEnabled(false);
				}
				else {
					cut.setEnabled(true);
					copy.setEnabled(true);
					delete.setEnabled(true);
					find.setEnabled(true);
					findNext.setEnabled(true);
				}
			}
		});
		
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.setText("");
				jfrm.setTitle("Untitled - Notepad");
			}
		});
		
		class JavaFilter extends FileFilter {
			public boolean accept(File f) {
				if (f.getName().endsWith(".java") || f.getName().endsWith(".txt") || f.isDirectory()) {
					return true;
				}
				return false;
			}
			public String getDescription() {
				return "Java source code and txt files";
			}
		}
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				typeHere.setText(null);
				JavaFilter javaFilter = new JavaFilter();
				JFileChooser jfc = new JFileChooser();
				jfc.setFileFilter(javaFilter);
				int result = jfc.showOpenDialog(jfrm); 
				if (result == JFileChooser.APPROVE_OPTION && javaFilter.accept(jfc.getSelectedFile())) {
					String input;
					Scanner inputFile = null;
					try {
						inputFile = new Scanner(jfc.getSelectedFile());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					do {
						input = inputFile.nextLine();
						typeHere.append(input+ "\n");
					}
					while(inputFile.hasNext());
					int point = jfc.getSelectedFile().getName().lastIndexOf('.');
					String f = jfc.getSelectedFile().getName().substring(0, point);
					jfrm.setTitle(f + " - Notepad");
					typeHere.setCaretPosition(0);
					fileName = jfc.getSelectedFile().getPath();
				}
				else {
					typeHere.setText("No java source file selected.");
				}
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 JFileChooser jfc = new JFileChooser();
				 String name = jfrm.getTitle();
				 String saveToFile = typeHere.getText();
				 if (name.equals("Untitled - Notepad")) {
					 int result = jfc.showSaveDialog(jfrm);
						if (result == JFileChooser.APPROVE_OPTION) {
							try {
					            FileWriter fw = new FileWriter(fileName);
					            fw.write(saveToFile);
					            fw.close();
					            int point = jfc.getSelectedFile().getName().lastIndexOf('.');
								String f = jfc.getSelectedFile().getName().substring(0, point);
								jfrm.setTitle(f + " - Notepad");
					        } catch (Exception ex) {
					            ex.printStackTrace();
					        }	
						}
				 }
				 else {
					 try {
						 PrintWriter pw = new PrintWriter(fileName);
				         pw.write(saveToFile);
				         pw.close();
				     } catch (Exception ex) {
				            ex.printStackTrace();
				        }	
				 }
			}
		});
		
		saveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				int result = jfc.showSaveDialog(jfrm);
				if (result == JFileChooser.APPROVE_OPTION) {
					String saveToFile = typeHere.getText();
					try {
			            FileWriter fw = new FileWriter(jfc.getSelectedFile()+".txt");
			            fw.write(saveToFile);
			            fw.close();
			            int point = jfc.getSelectedFile().getName().lastIndexOf('.');
						String f = jfc.getSelectedFile().getName().substring(0, point);
						jfrm.setTitle(f + " - Notepad");
			        } catch (Exception ex) {
			            ex.printStackTrace();
			        }	
				}
			}
		});
		
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitActionListener();
			}
		});
		
		jfrm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				exitActionListener();
			}
		});
		
		cut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.cut();
			}
		});
		
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.copy();
			}
		});
		
		paste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.paste();
			}
		});
		
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.replaceSelection("");
			}
		});
		
		find.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findDialog();
			}
		});
				
		findNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (findIndex == 0) {
					findDialog();
				}			
				else {
					find(findIndex + 1);
				}
			}	
		});
		
		replace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				replaceDialog();
			}
		});
				
		selectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.selectAll();
			}
		});
		
		timeDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date date = new Date(); 
				DateFormat dateFormat = new SimpleDateFormat("hh:mm a MM/dd/YYYY");
				String formatteddateTime = dateFormat.format(date);
				typeHere.insert(formatteddateTime, typeHere.getCaretPosition());
			}
		});
		
		wordWrap.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				int response = e.getStateChange();
				if (response == ItemEvent.SELECTED) {
					typeHere.setLineWrap(true);
					typeHere.setWrapStyleWord(true);
					caretLocation.setVisible(false);
					statusBar.setEnabled(false);
				}
				if (response == ItemEvent.DESELECTED) {
					typeHere.setLineWrap(false);
					typeHere.setWrapStyleWord(false);
					statusBar.setEnabled(true);
				}
			}
		});
		
		font.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Font fontSel = JFontChooser.showDialog(jfrm, null);
				typeHere.setFont(fontSel);
			}
		});
		
		statusBar.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				caretLocation.setVisible(true);
				int response = e.getStateChange();
				if (response == ItemEvent.SELECTED) {
					caretLocation.setVisible(true);
				}
				if (response == ItemEvent.DESELECTED) {
					caretLocation.setVisible(false);
				}
			}	
		});
		
		viewHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
			        Desktop.getDesktop().browse(new URL("https://www.bing.com/search?q=get+help+with+notepad+in+windows+10&filters=guid:%224466414-en-dia%22%20lang:%22en%22&form=T00032&ocid=HelpPane-BingIA").toURI());
			    } catch (Exception ex) {
			        ex.printStackTrace();
			    }
			}
		});
		
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(jfrm, "<html>This program is made by  (c) Jay Savla.<br>It is functionally identical to notepad.<br>"
						+ "There is no copyright. Feel free to copy whatever you want.", "ABOUT", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		jfrm.setVisible(true);	
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new JNotepad();
			}
		});
	}
	
	private boolean find(int start) {
		String findWithin = typeHere.getText();
		String search = toFind.getText();
		boolean toReturn = true;
		int downIndex = 0;
		int upIndex = 0;
		if (down.isSelected()) {
			if (matchCase.isSelected()) {
				downIndex = findWithin.indexOf(search, start);
				if (downIndex == -1) {
					JOptionPane.showMessageDialog(jfrm, "Cannot find " + "\"" + search + "\"", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(downIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = downIndex;
				}		
			}
			if (!matchCase.isSelected()) {
				downIndex = findWithin.toLowerCase().indexOf(search.toLowerCase(), start);
				if (downIndex == -1) {
					JOptionPane.showMessageDialog(jfrm, "Cannot find " + "\"" + search + "\"", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(downIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = downIndex;
				}	
			}
		}
		
		if (up.isSelected()) {
			if (matchCase.isSelected()) {
				upIndex = findWithin.lastIndexOf(search, start);
				if (upIndex == -1) {
					JOptionPane.showMessageDialog(jfrm, "Cannot find " + "\"" + search + "\"", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(upIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = upIndex - toFind.getText().length();
				}		
			}
			if (!matchCase.isSelected()) {
				upIndex = findWithin.toLowerCase().lastIndexOf(search.toLowerCase(), start);
				if (upIndex == -1) {
					JOptionPane.showMessageDialog(jfrm, "Cannot find " + "\"" + search + "\"", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(upIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = upIndex - toFind.getText().length();
				}	
			}
		}
		typeHere.requestFocusInWindow();
		return toReturn;
	}
	
	private void findDialog() {
		JDialog jdlg = new JDialog(jfrm, "Find", false);
		jdlg.pack();
		jdlg.setLocationRelativeTo(null);
		jdlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jdlg.setSize(400, 125);
		jdlg.getContentPane().setLayout(new FlowLayout());
		Container content = jfrm.getContentPane();
	    content.setLayout(new BorderLayout());
	    ButtonGroup bg = new ButtonGroup();
	    bg.add(up);
	    bg.add(down);
	    findNextButton.setEnabled(false);
	    
	    toFind.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void removeUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void changedUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void checkForInput() {
				if (toFind.getText().length() == 0) {
					findNextButton.setEnabled(false);
				}
				else {
					findNextButton.setEnabled(true);
				}
			}
		});
		
		findNextButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if (findIndex == 0) {
					find(findIndex);
				}
				else {
					find(findIndex + 1);
				}	
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jdlg.dispose();
			}
		});
		
		jdlg.add(findWhat);
		jdlg.add(toFind);
		jdlg.add(findNextButton);
		jdlg.add(matchCase);
		jdlg.add(up);
		jdlg.add(down);
		jdlg.add(cancel);
		jdlg.setVisible(true);
	}
	
	private boolean findForReplaceAll(int start) {
		String findWithin = typeHere.getText();
		String search = toFind.getText();
		boolean toReturn = true;
		int downIndex = 0;
		int upIndex = 0;
		if (down.isSelected()) {
			if (matchCase.isSelected()) {
				downIndex = findWithin.indexOf(search, start);
				if (downIndex == -1) {
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(downIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = downIndex;
				}		
			}
			if (!matchCase.isSelected()) {
				downIndex = findWithin.toLowerCase().indexOf(search.toLowerCase(), start);
				if (downIndex == -1) {
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(downIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = downIndex;
				}	
			}
		}
		
		if (up.isSelected()) {
			if (matchCase.isSelected()) {
				upIndex = findWithin.lastIndexOf(search, start);
				if (upIndex == -1) {
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(upIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = upIndex - toFind.getText().length();
				}		
			}
			if (!matchCase.isSelected()) {
				upIndex = findWithin.toLowerCase().lastIndexOf(search.toLowerCase(), start);
				if (upIndex == -1) {
					toReturn = false;
				}
				else {
					typeHere.setCaretPosition(upIndex);
					typeHere.moveCaretPosition(typeHere.getCaretPosition() + toFind.getText().length());
					findIndex = upIndex - toFind.getText().length();
				}	
			}
		}
		typeHere.requestFocusInWindow();
		return toReturn;
	}
	
	private void replaceDialog() {
		JDialog rdlg = new JDialog(jfrm, "Replace", false);
		rdlg.pack();
		rdlg.setLocationRelativeTo(null);
		rdlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		rdlg.setSize(450, 150);
		rdlg.getContentPane().setLayout(new FlowLayout());
		Container content = jfrm.getContentPane();
	    content.setLayout(new BorderLayout());	
	    findNextButton.setEnabled(false);
	    replaceButton.setEnabled(false);
	    replaceAllButton.setEnabled(false);
	    
	    findNextButton.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				if (findIndex == 0) {
					find(findIndex);
				}
				else {
					find(findIndex + 1);
				}	
			}
		});
	    
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rdlg.dispose();
			}
		});
		
		replaceWith.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void removeUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void changedUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void checkForInput() {
				if (replaceWith.getText().length() == 0) {
					replaceButton.setEnabled(false);
					replaceAllButton.setEnabled(false);
				}
				else {
					replaceButton.setEnabled(true);
					replaceAllButton.setEnabled(true);
				}
			}
		});
		
		toFind.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void removeUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void changedUpdate(DocumentEvent e) {
				checkForInput();
			}
			public void checkForInput() {
				if (toFind.getText().length() == 0) {
					findNextButton.setEnabled(false);
				}
				else {
					findNextButton.setEnabled(true);
				}
			}
		});
		
		replaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (toFind.getText().length() > 0 && replaceWith.getText().length() > 0) {
					find(findIndex);
					typeHere.replaceRange(replaceWith.getText(), typeHere.getCaretPosition() - toFind.getText().length(), typeHere.getCaretPosition());
					find(findIndex);
				}		
			}
		});
		
		replaceAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeHere.setCaretPosition(0);
				while (findForReplaceAll(findIndex)) {
					findForReplaceAll(findIndex);
					typeHere.replaceRange(replaceWith.getText(), typeHere.getCaretPosition() - toFind.getText().length(), typeHere.getCaretPosition());
				}
			}
		});
	    
	    rdlg.add(findWhat);
		rdlg.add(toFind);
		rdlg.add(findNextButton);
		rdlg.add(replaceWhat);
		rdlg.add(replaceWith);
		rdlg.add(replaceButton);
		rdlg.add(matchCase);
		rdlg.add(replaceAllButton);
		rdlg.add(cancel);
		rdlg.setVisible(true);
	}
	
	private void updateStatus(int lineNumber, int ColumnNumber) {
		caretLocation.setText("Ln: " + lineNumber + " Col: " + columnNumber);
	}
	
	private void exitActionListener() {
		if (contentChanged) {
			String[] options = {"Save", "Don't Save", "Cancel"};
			int response = JOptionPane.showOptionDialog(jfrm, "Do you want to save changes to Untitled?", "Notepad", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, "Save");
			switch(response) {
			case 0: 
				JFileChooser jfc = new JFileChooser();
				 String name = jfrm.getTitle();
				 String saveToFile = typeHere.getText();
				 if (name.equals("Untitled - Notepad")) {
					 int result = jfc.showSaveDialog(jfrm);
						if (result == JFileChooser.APPROVE_OPTION) {
							try {
					            FileWriter fw = new FileWriter(fileName);
					            fw.write(saveToFile);
					            fw.close();
					            int point = jfc.getSelectedFile().getName().lastIndexOf('.');
								String f = jfc.getSelectedFile().getName().substring(0, point);
								jfrm.setTitle(f + " - Notepad");
					        } catch (Exception ex) {
					            ex.printStackTrace();
					        }	
						}
				 }
				 else {
					 try {
						 PrintWriter pw = new PrintWriter(fileName);
						 System.out.println(new File(fileName).exists());
				         pw.write(saveToFile);
				         pw.close();
				     } catch (Exception ex) {
				            ex.printStackTrace();
				        }	
				 }
				 break;
				
			case 1:
				System.exit(0);
				break;
			
			case 2: 
				break;	
			}
		}
		else {
			System.exit(0);
		}
	}
}