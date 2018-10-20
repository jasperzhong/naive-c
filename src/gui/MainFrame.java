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
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.Label;
import javax.swing.UIManager;
import java.awt.SystemColor;
import java.awt.Toolkit;

import lexer.*;
import utils.*;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	
	private Lexer lexer = new Lexer(); 
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
		setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\Coding\\Java\\naive-c\\src\\gui\\tongji.png"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 804, 690);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(5, 76, 781, 364);
		panel.setBackground(Color.WHITE);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JTextArea display = new JTextArea();
		display.setFont(new Font("YaHei Consolas Hybrid", Font.PLAIN, 18));
		display.setWrapStyleWord(true);
		display.setLineWrap(true);
		display.setForeground(new Color(244, 164, 96));
		display.setBounds(0, 27, 781, 337);
		panel.add(display);
		
		Label label = new Label("|Code Editor");
		label.setBackground(UIManager.getColor("Button.background"));
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Arial", Font.BOLD, 14));
		label.setBounds(0, 0, 781, 25);
		panel.add(label);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel_2.setBounds(5, 442, 781, 201);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JTextPane lx = new JTextPane();
		lx.setForeground(UIManager.getColor("CheckBox.foreground"));
		lx.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 18));
		lx.setBounds(0, 23, 781, 178);
		panel_2.add(lx);
		
		Label label_2 = new Label("|Lexical Analysis");
		label_2.setForeground(Color.DARK_GRAY);
		label_2.setFont(new Font("Arial", Font.BOLD, 14));
		label_2.setBackground(SystemColor.menu);
		label_2.setBounds(0, 0, 781, 25);
		panel_2.add(label_2);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		toolBar.setBounds(5, 0, 781, 74);
		contentPane.add(toolBar);
		
		JButton btnNewButton = new JButton("Open");
		btnNewButton.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 24));
		toolBar.add(btnNewButton);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setIcon(new ImageIcon("D:\\Coding\\Java\\naive-c\\src\\gui\\open.jpg"));
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
				String input = display.getText();
				String[] lines = input.split("\n");
				String status = "";
				for(String line: lines) {
					
					status = lexer.lexAnalysis(line);
					if(status.equals("OK")) {
						continue;
					}else {
						lx.setText(status);
						return;
					}
				}
				
				lx.setText(lexer.getSymbolTable() + "\n" + lexer.getTokenList());
			}
		});
		
		
		JButton btnPreprocess = new JButton("Preprocess");
		btnPreprocess.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 24));
		btnPreprocess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// preprocess: trim and remove line comment 
				String input = display.getText();
				display.setText(preProcessor.preprocess(input));
			}
		});
		
		
		btnPreprocess.setIcon(new ImageIcon("D:\\Coding\\Java\\naive-c\\src\\gui\\pinterest.jpg"));
		btnPreprocess.setFont(new Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 24));
		btnPreprocess.setBackground(Color.WHITE);
		toolBar.add(btnPreprocess);
		toolBar.add(btnCompile);
		btnCompile.setIcon(new ImageIcon("D:\\Coding\\Java\\naive-c\\src\\gui\\compile.jpg"));
		btnCompile.setBackground(Color.WHITE);
	}
}
