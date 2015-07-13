import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class TimingUI extends JDialog {
    
    private static final long serialVersionUID = 1L;
    //    private JButton saveCloseBtn = new JButton("Save Changes and Close");
//    private JButton closeButton = new JButton("Exit Without Saving");
//    private JFrame frame=new JFrame("Courses taught by this examiner");
//    private JTextArea textArea = new JTextArea();
    private Timer timer;
    private boolean cancelFlag = false;
    private int timerTextSecs = 0;
    private int timerTextMins = 0;
    private int timerTextHours = 0;
    private Date startTime;

    public TimingUI(JFrame parent, String title) {   
        super(parent, title);
        this.setModal(true);
        System.out.println("creating the window..");
        startTime = new Date();
        //startTime = timeFormat.format(date);
        JLabel timerLabel = new JLabel();
        timerLabel.setText("You have been working for " +timerTextHours+ " hours, " +timerTextMins+ " minutes, and " +timerTextSecs+ " seconds.");
        timerTextSecs = 1;
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timerLabel.setText("You have been working for " +timerTextHours+ " hours, " +timerTextMins+ " minutes, and " +timerTextSecs+ " seconds.");
                timerTextSecs ++;
                if (timerTextSecs == 60) {
                    timerTextSecs = 0;
                    timerTextMins++;
                }
                if (timerTextMins == 60) {
                    timerTextMins = 0;
                    timerTextHours++;
                }
            }
        });
        timer.start();
        
        // set the position of the window
        Point p = new Point(400, 400);
        setLocation(p.x, p.y);
        // Create a message
        JPanel messagePane = new JPanel();
        messagePane.add(timerLabel);
        // get content pane, which is usually the
        // Container of all the dialog's components.
        getContentPane().add(messagePane);
 
        // Create a button
        JPanel buttonPane = new JPanel();
        JButton button = new JButton("I'm Done");
        JButton cancelButton = new JButton("Cancel Action");
        buttonPane.add(button);
        buttonPane.add(cancelButton);
        
        // set action listener on the button
        button.addActionListener(new ButtonActionListener());
        cancelButton.addActionListener(new CancelButtonActionListener());
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(300, 150);
        pack();
        setVisible(true);

    }
    
    // override the createRootPane inherited by the JDialog, to create the rootPane.
    // create functionality to close the window when "Escape" button is pressed
//    public JRootPane createRootPane() {
//        JRootPane rootPane = new JRootPane();
//        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
//        Action action = new AbstractAction() {
//             
//            private static final long serialVersionUID = 1L;
// 
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("escaping..");
//                setVisible(false);
//                dispose();
//            }
//        };
//        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//        inputMap.put(stroke, "ESCAPE");
//        rootPane.getActionMap().put("ESCAPE", action);
//        return rootPane;
//    }
 
    // an action listener to be used when an action is performed
    // (e.g. button is pressed)
    class ButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("disposing the window..");
            timer.stop();
            TimingUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
    class CancelButtonActionListener implements ActionListener {
        //close and dispose of the window.
        public void actionPerformed(ActionEvent e) {
            System.out.println("cancelling the window..");
            timer.stop();
            cancelFlag = true;
            TimingUI.this.setModal(false);
            setVisible(false);
            dispose();
        }
    }
    
    public int[] getTime() {
        int[] time = {timerTextSecs, timerTextMins, timerTextHours};
        return time;
    }
    
    public Date getStartTime() {
        return startTime;
    }
    
    public boolean getCancelFlag() {
        return cancelFlag;
    }

}
