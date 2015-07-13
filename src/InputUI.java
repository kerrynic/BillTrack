
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class InputUI extends JDialog{
    
    private static final long serialVersionUID = 1L;
    private boolean cancelFlag = false;
    private String categoryResponse;
    private String descriptionResponse;
    private JComboBox<String> categoryEntry;
    private JTextField descriptionEntry;
    
    public InputUI(JFrame parent, String title, String LastName, String FirstName, int[] TimeLength) {   
        super(parent, title);
        this.setModal(true);
        System.out.println("creating the window..");
        
        // set the position of the window
        Point p = new Point(400, 400);
        setLocation(p.x, p.y);
        // Create a message
        JLabel DescriptionLabel = new JLabel("You just worked on "+LastName+", "+FirstName+" for "+TimeLength[2]+" hours, "+TimeLength[1]+" minutes, and "+TimeLength[0]+" seconds.");
        JLabel QuestionLabel = new JLabel("What did you work on?");
        JPanel messagePane = new JPanel();
        messagePane.add(DescriptionLabel);
        messagePane.add(QuestionLabel);
        // get content pane, which is usually the
        // Container of all the dialog's components.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().add(messagePane);
        
        //create text entries
        JPanel entryPane = new JPanel();
        JLabel category = new JLabel("Category");
        JLabel description = new JLabel("Description");
        String[] categories = {"Court", "Hearing", "Meeting", "Phone Call", "Other"};
        categoryEntry = new JComboBox<String>(categories);
        AutoCompleteDecorator.decorate(categoryEntry);
        categoryEntry.setPreferredSize(new Dimension(200,30));
        descriptionEntry = new JTextField();
        descriptionEntry.setPreferredSize(new Dimension(200,30));
        entryPane.add(category);
        entryPane.add(categoryEntry);
        entryPane.add(description);
        entryPane.add(descriptionEntry);
        getContentPane().add(entryPane);
        // Create a button
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("I'm Done");
        JButton cancelButton = new JButton("Cancel Action");
        buttonPane.add(button);
        buttonPane.add(cancelButton);
        cancelButton.addActionListener(new CancelButtonActionListener());
        // set action listener on the button
        button.addActionListener(new ButtonActionListener());
        getContentPane().add(buttonPane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300, 150);
        pack();
        setVisible(true);
    }
    
    class ButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("disposing the window..");
            descriptionResponse = descriptionEntry.getText();
            categoryResponse = (String) categoryEntry.getSelectedItem();
            InputUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
    class CancelButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("cancelling the window..");
            cancelFlag = true;
            InputUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
//    class CategoryActionListener implements ActionListener {
//        //close and dispose of the window.
//        public void actionPerformed(ActionEvent e) {
//            categoryResponse = (String) categoryEntry.getSelectedItem();
//        }
//    }
    
//    class DescriptionActionListener implements ActionListener {
//        //close and dispose of the window.
//        public void actionPerformed(ActionEvent e) {
//            descriptionResponse = descriptionEntry.getText();
//        }
//    }
    
    
    public boolean getCancelFlag() {
        return cancelFlag;
    }
    
    public String getCategoryResponse() {
        System.out.println("CR: "+categoryResponse);
        return categoryResponse;
    }
    
    public String getDescriptionResponse() {
        System.out.println("CR: "+descriptionResponse);
        return descriptionResponse;
    }
}
