import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class SheetSelectorUI extends JDialog{
    private static final long serialVersionUID = 1L;
    private boolean cancelFlag = false;
    private String caseResponse;
    //private String descriptionResponse;
    private JComboBox<String> caseEntry;
    //private JTextField descriptionEntry;
    private Date firstDate;
    private Date lastDate;
    JDatePickerImpl datePickerFirst;
    JDatePickerImpl datePickerLast;
    
    public SheetSelectorUI(JFrame parent, String title, String[] cases) {   
        super(parent, title);
        this.setModal(true);
        System.out.println("creating the window..");
        
        // set the position of the window
        Point p = new Point(400, 400);
        setLocation(p.x, p.y);
        // Create a message
        JLabel DescriptionLabel = new JLabel("You would like to see a spreadsheet of events for a particular case.");
        JPanel messagePane = new JPanel();
        messagePane.add(DescriptionLabel);
        // get content pane, which is usually the
        // Container of all the dialog's components.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().add(messagePane);
        JLabel commandLabel = new JLabel("Please select the case and the relevant time frame.");
        JPanel commandPane = new JPanel();
        commandPane.add(commandLabel);
        getContentPane().add(commandPane);
        //create text entries
        JPanel entryPane = new JPanel();
        JLabel caseLabel = new JLabel("Case: ");
        JLabel startDate = new JLabel("Start Date: ");
        JLabel endDate = new JLabel("End Date: ");
        
        caseEntry = new JComboBox<String>(cases);
        //AutoCompleteDecorator.decorate(categoryEntry);
        caseEntry.setPreferredSize(new Dimension(200,30));
        
        UtilDateModel model1=new UtilDateModel();
        UtilDateModel model2=new UtilDateModel();
        Properties pop = new Properties();
        pop.put("text.today", "Today");
        pop.put("text.month", "Month");
        pop.put("text.year", "Year");
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1,pop);
        datePickerFirst = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        datePickerFirst.setBounds(220,350,120,30);
        
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2,pop);
        datePickerLast = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
        datePickerLast.setBounds(220,350,120,30);
        
        entryPane.add(caseLabel);
        entryPane.add(caseEntry);
        entryPane.add(startDate);
        entryPane.add(datePickerFirst);
        entryPane.add(endDate);
        entryPane.add(datePickerLast);
        getContentPane().add(entryPane);
        // Create a button
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("Show me the Spreadsheet!");
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
            firstDate = (Date) datePickerFirst.getModel().getValue();
            lastDate = (Date) datePickerLast.getModel().getValue();
            caseResponse = (String) caseEntry.getSelectedItem();
            SheetSelectorUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
    class CancelButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("cancelling the window..");
            cancelFlag = true;
            SheetSelectorUI.this.setModal(false);
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
    
    public String getCaseResponse() {
        //System.out.println("CR: "+categoryResponse);
        return caseResponse;
    }
    
    public Date getFirstDate() {
        //System.out.println("CR: "+descriptionResponse);
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDate);
        cal.set(Calendar.HOUR_OF_DAY,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);

        firstDate = cal.getTime();
        return firstDate;
    }
    
    public Date getLastDate() {
        //System.out.println("CR: "+descriptionResponse);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastDate);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,0);

        lastDate = cal.getTime();
        return lastDate;
    }
    
    public class DateLabelFormatter extends AbstractFormatter {

        private static final long serialVersionUID = 1L;
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }
}
