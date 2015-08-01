
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.SpinnerDateModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class AddEventUI extends JDialog{
    
    private static final long serialVersionUID = 1L;
    private boolean cancelFlag = false;
    private String categoryResponse;
    private String descriptionResponse;
    private JComboBox<String> categoryEntry;
    private JTextField descriptionEntry;
    private JComboBox<String> caseEntry;
    JDatePickerImpl datePickerFirst;
    private String caseResponse;
    private Date firstDate;
    private JSpinner timeSpinnerStart;
    private JSpinner timeSpinnerEnd;
    private Date timeStart;
    private Date timeEnd;
    private Date startDateTime;
    
    public AddEventUI(JFrame parent, String title, String[] cases) {   
        super(parent, title);
        this.setModal(true);
        System.out.println("creating the window..");
        
        // set the position of the window
        Point p = new Point(400, 400);
        setLocation(p.x, p.y);
        // Create a message
        JLabel DescriptionLabel = new JLabel("Please describe this event.");
        JPanel messagePane = new JPanel();
        messagePane.add(DescriptionLabel);
        // get content pane, which is usually the
        // Container of all the dialog's components.
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        //GridBagConstraints gbc = new GridBagConstraints();
        getContentPane().add(messagePane);
        
        //create text entries
        JPanel entryPane = new JPanel();
        
        JLabel caseLabel = new JLabel("Case: ");
        JLabel date = new JLabel("Date: ");
        
        caseEntry = new JComboBox<String>(cases);
        //AutoCompleteDecorator.decorate(categoryEntry);
        caseEntry.setPreferredSize(new Dimension(200,30));
        
        UtilDateModel model1=new UtilDateModel();
        Properties pop = new Properties();
        pop.put("text.today", "Today");
        pop.put("text.month", "Month");
        pop.put("text.year", "Year");
        JDatePanelImpl datePanel1 = new JDatePanelImpl(model1,pop);
        datePickerFirst = new JDatePickerImpl(datePanel1, new DateLabelFormatter());
        datePickerFirst.setBounds(220,350,120,30);
        
        timeSpinnerStart = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(timeSpinnerStart, "HH:mm:ss");
        timeSpinnerStart.setEditor(timeEditor1);
        //timeSpinner.setValue(new Date());
        
        timeSpinnerEnd = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(timeSpinnerEnd, "HH:mm:ss");
        timeSpinnerEnd.setEditor(timeEditor2);
        
        JLabel timeS = new JLabel("Start Time: ");
        JLabel timeE = new JLabel("End Time: ");
        
        JLabel category = new JLabel("Category");
        JLabel description = new JLabel("Description");
        String[] categories = {"Court", "Hearing", "Meeting", "Phone Call", "Other"};
        categoryEntry = new JComboBox<String>(categories);
        AutoCompleteDecorator.decorate(categoryEntry);
        categoryEntry.setPreferredSize(new Dimension(200,30));
        descriptionEntry = new JTextField();
        descriptionEntry.setPreferredSize(new Dimension(200,30));
        entryPane.add(caseLabel);
        entryPane.add(caseEntry);
        entryPane.add(date);
        entryPane.add(datePickerFirst);
        entryPane.add(timeS);
        entryPane.add(timeSpinnerStart);
        entryPane.add(timeE);
        entryPane.add(timeSpinnerEnd);
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
            firstDate = (Date) datePickerFirst.getModel().getValue();
            caseResponse = (String) caseEntry.getSelectedItem();
            descriptionResponse = descriptionEntry.getText();
            categoryResponse = (String) categoryEntry.getSelectedItem();
            timeStart = (Date) timeSpinnerStart.getModel().getValue();
            timeEnd = (Date) timeSpinnerEnd.getModel().getValue();
            AddEventUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
    class CancelButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("cancelling the window..");
            cancelFlag = true;
            AddEventUI.this.setModal(false);
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
    
    public String getCaseResponse() {
        //System.out.println("CR: "+categoryResponse);
        return caseResponse;
    }
    
    public Date getStartTimeDate() {
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(timeStart);   // assigns calendar to given date  
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDate);
        cal.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND,calendar.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND,calendar.get(Calendar.MILLISECOND));

        startDateTime = cal.getTime();
        return startDateTime;
    }
    
    public int[] getTimeDuration() {
        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
        calendar.setTime(timeStart);
        Calendar calendar2 = Calendar.getInstance(); // creates a new calendar instance
        calendar2.setTime(timeEnd);
        int timerTextHours = calendar2.get(Calendar.HOUR_OF_DAY)-calendar.get(Calendar.HOUR_OF_DAY);
        int timerTextMins = calendar2.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE);
        int timerTextSecs = calendar2.get(Calendar.SECOND)-calendar.get(Calendar.SECOND);
        int[] timeDateEnd = {timerTextSecs, timerTextMins, timerTextHours};
        return timeDateEnd;
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