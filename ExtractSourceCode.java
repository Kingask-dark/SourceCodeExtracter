package SourceCodeExtracter;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ExtractSourceCode {
    JFrame frame;
    JTextArea textArea;
    JTextField textField,errField;
    JButton button,saveButton,clearButton;
    JLabel label1,label2;
    JScrollPane scrollPane;
    JFileChooser jFileChooser;


    public ExtractSourceCode(){
        frame = new JFrame("Source Code Extracter");
        frame.setSize(1024, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label1 = new JLabel("Enter the url:");
        frame.add(label1);
        textField = new JTextField(50);
        frame.add(textField);

        button = new JButton("Extract");
        frame.add(button);

        clearButton = new JButton("Clear");
        frame.add(clearButton);

        label2 = new JLabel("Source Code:");
        frame.add(label2);
        textArea = new JTextArea(30,90);
        frame.add(textArea);
        scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true);
        textArea.setAutoscrolls(true);
        frame.add(scrollPane);

        saveButton = new JButton("Save");
        frame.add(saveButton);

        errField = new JTextField(10);
        frame.add(errField);
        errField.setVisible(false);

    
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetUrl();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Saveme();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClearMe();
            }
        });
    }

    public void GetUrl(){
       try {
            String input = textField.getText();
            URL url = new URL(input);
            URLConnection urlconn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
            String inputline;
            while((inputline = in.readLine())!= null){
                textArea.append(inputline+"\n");
            }
            in.close();

       } catch (Exception e) {
           textArea.setText(e.getMessage());
       }

    }

    public void Saveme(){
        try {
            jFileChooser = new JFileChooser();
            jFileChooser.setAcceptAllFileFilterUsed(false);
           
            FileFilter textFilter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (f.isDirectory()) {
                        return true;
                    }
                    String name = f.getName();
                    return name.endsWith(".html");
                }
            
                @Override
                public String getDescription() {
                    return "HTML files (*.html)";
                }
            };
            jFileChooser.addChoosableFileFilter(textFilter);
            jFileChooser.setFileFilter(textFilter);

            int returnval = jFileChooser.showSaveDialog(null);
            if(returnval == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex == -1) {
                    // No extension was provided, so add the desired extension
                    fileName += ".html";
                    file = new File(file.getParentFile(), fileName);
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PrintWriter printWriter = new PrintWriter(fileOutputStream);

                String input = textField.getText();
                URL url = new URL(input);
                URLConnection urlconn = url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlconn.getInputStream()));
                String inputline;
                while((inputline = in.readLine())!= null){
                    printWriter.println(inputline);
                }
                printWriter.close();
                in.close();
                fileOutputStream.close();

                
            }
        } catch (Exception e) {
            errField.setVisible(true);
           errField.setText(e.getMessage());

        }
    }

    public void ClearMe(){
        textArea.setText(null);
        textField.setText(null);
    }


    public static void main(String[] args) {
        ExtractSourceCode eSourceCode = new ExtractSourceCode();
        eSourceCode.frame.setLayout(new FlowLayout(FlowLayout.CENTER,100,40));
        eSourceCode.frame.setVisible(true);
        eSourceCode.frame.setResizable(false);
        eSourceCode.frame.setLocationRelativeTo(null);
    }
}
