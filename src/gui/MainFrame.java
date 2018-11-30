package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.Label;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.Toolkit;

import parser.*;
import utils.*;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	
	
	private PreProcessor preProcessor = new PreProcessor();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		
		
		
		setTitle("\u949F\u94B0\u741B 1652817 \u7F16\u8BD1\u539F\u7406");
		setIconImage(Toolkit.getDefaultToolkit().getImage("src\\gui\\tongji.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1104, 775);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 76, 1067, 300);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextArea display = new JTextArea();
		display.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, 18));
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setForeground(new Color(244, 164, 96));
		display.setBounds(0, 27, 781, 337);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 27, 1068, 275);
		panel.add(scrollPane_1);
		scrollPane_1.setViewportView(display);
		
		
		Label label = new Label("|Code Editor");
		scrollPane_1.setRowHeaderView(label);
		label.setBackground(UIManager.getColor("Button.background"));
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(5, 376, 1067, 352);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JTextPane lx = new JTextPane();
		lx.setForeground(UIManager.getColor("CheckBox.foreground"));
		lx.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		lx.setBounds(0, 23, 781, 178);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 1067, 352);
		panel_2.add(scrollPane);
		scrollPane.setViewportView(lx);
		
		
		Label label_2 = new Label("Syntactic Analysis");
		scrollPane.setColumnHeaderView(label_2);
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Arial", Font.BOLD, 14));
		label_2.setBackground(SystemColor.menu);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		toolBar.setBounds(5, 0, 1067, 74);
		contentPane.add(toolBar);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 24));
		toolBar.add(btnNewButton);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon("src\\gui\\open.jpg"));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "Ñ¡Ôñ");
				
				File file = jfc.getSelectedFile();
				
				// Check whether the input file is like "xxx.cl".
				String filename = file.getName();
				if(!preProcessor.CheckExt(filename)) {
					display.setText("Fatal: The name of C-like source code file should be xxx.cl!");
					display.setForeground(SystemColor.RED);
					return;
				}
				
				// read xxx.cl line by line
				display.setForeground(new Color(244, 164, 96));
				StringBuilder stringBuilder = new StringBuilder();
				try {
					Scanner scanner = new Scanner(file);
					while(scanner.hasNext()) {
						stringBuilder.append(scanner.nextLine() + "\n");
					}
					display.setText(stringBuilder.toString());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		
		
		JButton btnCompile = new JButton("Compile");
		btnCompile.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 24));
		btnCompile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LL1 ll1 = new LL1(); 
				File file = new File("src\\parser\\grammar.txt");
				RandomAccessFile randomAccessFile;
				StringBuilder stringBuilder = new StringBuilder();
				String line;
				try {
					randomAccessFile = new RandomAccessFile(file, "r");
				
					while((line = randomAccessFile.readLine()) != null) {
						stringBuilder.append(line);
						stringBuilder.append("\n");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
				ll1.buildPredictMap(stringBuilder.toString());
				
				ll1.buildFirst();
				ll1.buildFollow();
				
				ll1.buildPredictMap();
				
				String code = display.getText();
				
				// Tokenizer give token list
				String result = ll1.lexer.lexAnalysis(code);
				if(!result.equals("OK")) {
					lx.setText(result);
					return;
				}
				
				if(ll1.syntacticAnalysis(ll1.lexer.token_list)) {
					ll1.display();
				}
				
				lx.setText(ll1.result.toString());
			}
		});
		toolBar.add(btnCompile);
		btnCompile.setIcon(new ImageIcon("src\\gui\\compile.jpg"));
		btnCompile.setBackground(Color.WHITE);
	}
}
