
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;


public class BillTrackUI extends JFrame {
    
    private static final long serialVersionUID = 1L;

    //private static int rowNumSecondTop;
    
    private static BillTrackUI main;
    private static int[] currentEventTime;
    private static Date currentStartTime;
    private static List<JButton> topCases;
    //private static List<JButton> secondTopCases = new ArrayList<JButton>();
    private static JLabel caseLabel = new JLabel("Other Case: ");
    private static JComboBox<String> caseGuess = new JComboBox<String>();
    private static JButton caseSelect = new JButton("Select Case");
    private static Tuple[] caseNamesAndIDs;
    
    private static String sheetCase;
    private static Date sheetStart;
    private static Date sheetEnd;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;        
    private JMenu viewMenu;
    private JMenu billingMenu;
    private JMenuItem addEvent; 
    private JMenuItem removeEvent;
    private JMenuItem exitAction;
    private JMenuItem changeTopCases; 
    private JMenuItem addRowEvent;
    private JMenuItem viewSheet;
    private JMenuItem createBill;
    private JMenuItem viewCalendar;
    private static Connection con;
    private static PreparedStatement pst;
    private static ResultSet rs;
    private static String url;
    private static String user;
    private static String password;
    private static int numTopCases;
    private static int numNotTopCases;
    private static Tuple[] notTopCases;
    private static int numAllCases;
    
    public BillTrackUI() {
        setupGUI();

        setResizable(false);
        pack();
    }
    
    /**
     * Sets up the GUI layout and main components
     */
    private void setupGUI() {
        con = null;
        pst = null;
        rs = null;

        url = "jdbc:mysql://localhost:3306/casesevents";
        user = "kerrynic";
        password = "thomas51";
        
        caseNamesAndIDs = getAllCases();
        notTopCases = guesses();
        String[] butts = new String[numNotTopCases];
        for (int i=0; i<numNotTopCases; i++) {
            butts[i] = notTopCases[i].x;
        }
        caseGuess = new JComboBox<String>(butts);
        //AutoCompleteDecorator.decorate(caseGuess);
        caseGuess.setMaximumSize(new Dimension(200,30));
        caseLabel.setFont(new Font("Arial", Font.PLAIN, 25));
        caseGuess.setFont(new Font("Arial", Font.PLAIN, 20));
        caseSelect.setFont(new Font("Arial", Font.PLAIN, 20));
        addActionListenerToCaseButton(notTopCases);
        //rowNumSecondTop = 2;
        //int placeTop = 0;
        //int placeSec = 0;
        topCases = getCaseButtons();
        for (int i=0; i<(20-numTopCases); i++) {
            topCases.add(new JButton());
        }
        
        Dimension d = new Dimension(275,75);
        for (int i=0; i<20; i++) {
            topCases.get(i).setMinimumSize(d);
            topCases.get(i).setFont(new Font("Arial", Font.PLAIN, 20));
        }
        
        /*secondTopCases.add(new JButton("ROMERO, Natalie"));
        secondTopCases.add(new JButton("GROSSMAN, Barb"));
        secondTopCases.add(new JButton("LUPIN, Remus"));
        secondTopCases.add(new JButton("NICHOLSON, Julie"));
        secondTopCases.add(new JButton("BLAHBLAH, Rob"));
        secondTopCases.add(new JButton("GOOBER, Blue"));
        secondTopCases.add(new JButton("LOBLAW, Bob"));
        secondTopCases.add(new JButton("RUBIO, Marco"));
        secondTopCases.add(new JButton("CONNELLY, Margaret"));
        secondTopCases.add(new JButton("DEFURIA, Nicole"));
        
        Dimension de = new Dimension(200,10);
        for (int i=0; i<10; i++) {
            secondTopCases.get(i).setMinimumSize(de);
        }*/
        
        //Create the GroupLayout of the game
        //It's broken into three different panes that are staked vertically
        //They all sit on the main pane
        Container mainPane = getContentPane();
        GroupLayout mainLayout = new GroupLayout(mainPane);
        mainPane.setLayout(mainLayout);
        
        //The 3 sub-panels
        Container topPane = new Panel();
        GroupLayout topLayout = new GroupLayout(topPane);
        topPane.setLayout(topLayout);
        //Container middlePane = new Panel();
        //GroupLayout middleLayout = new GroupLayout(middlePane);
       // middlePane.setLayout(middleLayout);
        Container bottomPane = new Panel();
        GroupLayout bottomLayout = new GroupLayout(bottomPane);
        bottomPane.setLayout(bottomLayout);
        
        //The Grouplayout of how the 3 subpanels relate to each other
        mainLayout.setHorizontalGroup(
                mainLayout.createParallelGroup(Alignment.CENTER)
                    .addComponent(topPane)
                    .addComponent(bottomPane)
        );
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                    .addComponent(topPane)
                    .addComponent(bottomPane)
        );
        
        //The grouplayout of the top line with the new puzzle components
        Container topTopPane = new Panel();
        GroupLayout topTopLayout = new GroupLayout(topTopPane);
        topTopPane.setLayout(topTopLayout);
        Container topBottomPane = new Panel();
        GroupLayout topBottomLayout = new GroupLayout(topBottomPane);
        topBottomPane.setLayout(topBottomLayout);
        Container middleTopPane = new Panel();
        GroupLayout middleTopLayout = new GroupLayout(middleTopPane);
        middleTopPane.setLayout(middleTopLayout);
        Container middleBottomPane = new Panel();
        GroupLayout middleBottomLayout = new GroupLayout(middleBottomPane);
        middleBottomPane.setLayout(middleBottomLayout);
        
        topLayout.setAutoCreateGaps(true);
        topLayout.setAutoCreateContainerGaps(true);
        topLayout.setHorizontalGroup(
                topLayout.createParallelGroup()
                    .addComponent(topTopPane)
                    .addComponent(topBottomPane)
                    .addComponent(middleTopPane)
                    .addComponent(middleBottomPane)
        );
        topLayout.setVerticalGroup(
                topLayout.createSequentialGroup()
                    .addComponent(topTopPane)
                    .addComponent(topBottomPane)
                    .addComponent(middleTopPane)
                    .addComponent(middleBottomPane)
        );
        topTopLayout.setAutoCreateGaps(true);
        topTopLayout.setAutoCreateContainerGaps(true);
        topTopLayout.setHorizontalGroup(
                topTopLayout.createSequentialGroup()
                    .addComponent(topCases.get(0))
                    .addComponent(topCases.get(1))
                    .addComponent(topCases.get(2))
                    .addComponent(topCases.get(3))
                    .addComponent(topCases.get(4))
        );
        topTopLayout.setVerticalGroup(
                topTopLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(topCases.get(0))
                    .addComponent(topCases.get(1))
                    .addComponent(topCases.get(2))
                    .addComponent(topCases.get(3))
                    .addComponent(topCases.get(4))
        );
        topBottomLayout.setAutoCreateGaps(true);
        topBottomLayout.setAutoCreateContainerGaps(true);
        topBottomLayout.setHorizontalGroup(
                topBottomLayout.createSequentialGroup()
                    .addComponent(topCases.get(5))
                    .addComponent(topCases.get(6))
                    .addComponent(topCases.get(7))
                    .addComponent(topCases.get(8))
                    .addComponent(topCases.get(9))
        );
        topBottomLayout.setVerticalGroup(
                topBottomLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(topCases.get(5))
                    .addComponent(topCases.get(6))
                    .addComponent(topCases.get(7))
                    .addComponent(topCases.get(8))
                    .addComponent(topCases.get(9))
        );
        middleTopLayout.setAutoCreateGaps(true);
        middleTopLayout.setAutoCreateContainerGaps(true);
        middleTopLayout.setHorizontalGroup(
                middleTopLayout.createSequentialGroup()
                    .addComponent(topCases.get(10))
                    .addComponent(topCases.get(11))
                    .addComponent(topCases.get(12))
                    .addComponent(topCases.get(13))
                    .addComponent(topCases.get(14))
        );
        middleTopLayout.setVerticalGroup(
                middleTopLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(topCases.get(10))
                    .addComponent(topCases.get(11))
                    .addComponent(topCases.get(12))
                    .addComponent(topCases.get(13))
                    .addComponent(topCases.get(14))
        );
        middleBottomLayout.setAutoCreateGaps(true);
        middleBottomLayout.setAutoCreateContainerGaps(true);
        middleBottomLayout.setHorizontalGroup(
                middleBottomLayout.createSequentialGroup()
                    .addComponent(topCases.get(15))
                    .addComponent(topCases.get(16))
                    .addComponent(topCases.get(17))
                    .addComponent(topCases.get(18))
                    .addComponent(topCases.get(19))
        );
        middleBottomLayout.setVerticalGroup(
                middleBottomLayout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(topCases.get(15))
                    .addComponent(topCases.get(16))
                    .addComponent(topCases.get(17))
                    .addComponent(topCases.get(18))
                    .addComponent(topCases.get(19))
        );
        
        bottomLayout.setAutoCreateGaps(true);
        bottomLayout.setAutoCreateContainerGaps(true);
        bottomLayout.setHorizontalGroup(
                bottomLayout.createSequentialGroup()
                    .addComponent(caseLabel)
                    .addComponent(caseGuess)
                    .addComponent(caseSelect)
        );
        bottomLayout.setVerticalGroup(
                bottomLayout.createParallelGroup(Alignment.CENTER)
                    .addComponent(caseLabel)
                    .addComponent(caseGuess)
                    .addComponent(caseSelect)
        );
        
        //Menu Items
        
        menuBar = new JMenuBar();
        

        fileMenu = new JMenu("File");
        fileMenu.setFont(new Font("Arial", Font.BOLD, 15));
        viewMenu = new JMenu("View");
        viewMenu.setFont(new Font("Arial", Font.BOLD, 15));
        billingMenu = new JMenu("Billing");
        billingMenu.setFont(new Font("Arial", Font.BOLD, 15));
        
        addEvent = new JMenuItem("Add Event");
        removeEvent = new JMenuItem("Remove Event"); 
        exitAction = new JMenuItem("Exit"); 

        changeTopCases = new JMenuItem("Change Top Cases"); 
        addRowEvent = new JMenuItem("Add Row"); 
        
        viewSheet = new JMenuItem("View Sheet");
        addActionListenerToViewSheetMenu();
        createBill = new JMenuItem("Create Bill");
        viewCalendar = new JMenuItem("View Calendar");

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(billingMenu);

        setJMenuBar(menuBar);
        
        fileMenu.add(addEvent);
        fileMenu.add(removeEvent);
        fileMenu.add(exitAction);
        viewMenu.add(changeTopCases);
        viewMenu.add(addRowEvent);
        billingMenu.add(viewSheet);
        billingMenu.add(createBill);
        billingMenu.add(viewCalendar);
        
        //Pack the components and general organization of gui
        pack();
        setTitle("Bill Track");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE); 

    }
    
    /**creates and returns the case buttons
     * @return new case buttons
     */
    public List<JButton> getCaseButtons() {
        List<JButton> newTopCases = new ArrayList<JButton>();
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT SUM(CASE WHEN Location=1 THEN 1 ELSE 0 END) FROM Cases");
            rs = pst.executeQuery();
            numTopCases = 0;
            
            while (rs.next()) {
                numTopCases = rs.getInt(1);
            }
            pst = con.prepareStatement("SELECT LastName, FirstName, Id FROM Cases WHERE Location=1");
            rs = pst.executeQuery();
            for (int i =0; i<numTopCases; i++) {
                rs.next();
                JButton newCase = new JButton();
                newCase.setName("topCase" + i);
                String lastname = rs.getString(1);
                String firstname = rs.getString(2);
                int caseID = rs.getInt(3);
                newCase.setText(lastname + ", " + firstname);
                newCase.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newCase.setBackground(Color.red);
                        TimingUI timingUI = new TimingUI(main, "Working on " + lastname + ", " + firstname);
                        //System.out.println("Start Time: " + currentStartTime);
                        //System.out.println("Length of Time: " + currentEventTime[2] + " " + currentEventTime[1] + " " + currentEventTime[0]);
                        if (!timingUI.getCancelFlag()) {
                            currentEventTime = timingUI.getTime();
                            currentStartTime = timingUI.getStartTime();
                            //System.out.println(currentStartTime);
                            InputUI inputUI = new InputUI(main, "Input Case Event", lastname, firstname, currentEventTime);
                            if (!inputUI.getCancelFlag()) {
                                makeDatabaseEntry(caseID, currentStartTime, currentEventTime, inputUI.getCategoryResponse(), inputUI.getDescriptionResponse());
                            }
                        }
                        newCase.setBackground(null);
                    }
                });
                newTopCases.add(newCase);
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Prepared.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Prepared.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return newTopCases;
    }
    
    public void makeDatabaseEntry(int caseID, Date StartTime, int[] TimeLength, String category, String Description) {
        try {
            DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //System.out.println(StartTime);
            String starttime = timeFormat.format(StartTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(StartTime);
            cal.add(Calendar.SECOND, TimeLength[0]);
            cal.add(Calendar.MINUTE, TimeLength[1]);
            cal.add(Calendar.HOUR, TimeLength[2]);
            String endTime = timeFormat.format(cal.getTime());
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("INSERT INTO Events(CaseId, DateTimeStart, DateTimeEnd, Category, Description) VALUES(?,?,?,?,?)");
            pst.setInt(1, caseID);
            pst.setString(2, starttime);
            pst.setString(3, endTime);
            pst.setString(4, category);
            pst.setString(5, Description);
            pst.executeUpdate();
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Prepared.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Prepared.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
    
    public void addActionListenerToCaseButton(Tuple[] nottopCases) {
        caseSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String lastFirst = (String) caseGuess.getSelectedItem();
                String[] lastSplitFirst= lastFirst.split(", ");
                TimingUI timingUI = new TimingUI(main, "Working on " + lastFirst);
                int caseid = numNotTopCases+numTopCases+1;
                for (int i = 0; i<numNotTopCases; i++) {
                    if (nottopCases[i].x == lastFirst){
                        caseid = nottopCases[i].y;
                    }  
                }
                //System.out.println("Start Time: " + currentStartTime);
                //System.out.println("Length of Time: " + currentEventTime[2] + " " + currentEventTime[1] + " " + currentEventTime[0]);
                if (!timingUI.getCancelFlag()) {
                    currentEventTime = timingUI.getTime();
                    currentStartTime = timingUI.getStartTime();
                    //System.out.println(currentStartTime);
                    InputUI inputUI = new InputUI(main, "Input Case Event", lastSplitFirst[0], lastSplitFirst[1], currentEventTime);
                    if (!inputUI.getCancelFlag()) {
                        makeDatabaseEntry(caseid, currentStartTime, currentEventTime, inputUI.getCategoryResponse(), inputUI.getDescriptionResponse());
                    }
                }
            }
        });
    }
    //
    
    public void addActionListenerToViewSheetMenu() {
        viewSheet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] blahs = new String[numAllCases];
                    for (int i=0; i<numAllCases; i++) {
                        blahs[i] = caseNamesAndIDs[i].x;
                    }
                    SheetSelectorUI sheetSelectorUI = new SheetSelectorUI(main, "Spreadsheet Selection", blahs);
                    if (!sheetSelectorUI.getCancelFlag()) {
                        sheetCase = sheetSelectorUI.getCaseResponse();
                        sheetStart = sheetSelectorUI.getFirstDate();
                        sheetEnd = sheetSelectorUI.getLastDate();
                        //System.out.println("Sheet Start: " + sheetStart);
                        //System.out.println("Sheet End: " + sheetEnd);
                        int case_id = numAllCases+1;
                        for (int i = 0; i<numAllCases; i++) {
                            if (caseNamesAndIDs[i].x == sheetCase){
                                case_id = caseNamesAndIDs[i].y;
                            }  
                        }
                        con = DriverManager.getConnection(url, user, password);
                        pst = con.prepareStatement("SELECT SUM(CASE WHEN CaseId = ? AND DateTimeStart >= ? AND DateTimeEnd <= ? THEN 1 ELSE 0 END) FROM Events");
                        pst.setInt(1, case_id);
                        pst.setTimestamp(2, new java.sql.Timestamp(sheetStart.getTime()));
                        pst.setTimestamp(3, new java.sql.Timestamp(sheetEnd.getTime()));
                        rs = pst.executeQuery();
                        
                        int numRelEvents = 0;
                        
                        while (rs.next()) {
                            numRelEvents = rs.getInt(1);
                        }
                        //System.out.println("Events: " + numRelEvents);
                        
                        pst = con.prepareStatement("SELECT DateTimeStart, DateTimeEnd, Category, Description FROM Events WHERE CaseId = ? AND DateTimeStart > ? AND DateTimeEnd < ? ORDER BY DateTimeStart ASC");
                        pst.setInt(1, case_id);
                        pst.setTimestamp(2, new java.sql.Timestamp(sheetStart.getTime()));
                        pst.setTimestamp(3, new java.sql.Timestamp(sheetEnd.getTime()));
                        rs = pst.executeQuery();
                        List<Date> starts = new ArrayList<Date>();
                        List<Date> ends = new ArrayList<Date>();
                        List<String> cats = new ArrayList<String>();
                        List<String> descs = new ArrayList<String>();
                        
                        for (int i=0; i<numRelEvents; i++) {
                            rs.next();
                            starts.add(rs.getTimestamp(1));
                            //System.out.print("End Times: " + rs.getTimestamp(2));
                            ends.add(rs.getTimestamp(2));
                            cats.add(rs.getString(3));
                            descs.add(rs.getString(4));
                        }
                        SheetUI sheetUI = new SheetUI(main, "Case SpreadSheet", starts, ends, cats, descs);
                    }
                } catch (SQLException ex) {
                    Logger lgr = Logger.getLogger(Prepared.class.getName());
                    lgr.log(Level.SEVERE, ex.getMessage(), ex);

                } finally {

                    try {
                        if (pst != null) {
                            pst.close();
                        }
                        if (con != null) {
                            con.close();
                        }

                    } catch (SQLException ex) {
                        Logger lgr = Logger.getLogger(Prepared.class.getName());
                        lgr.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
            }
        });
    }
    
    public Tuple[] getAllCases() {
        Tuple[] cases = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT COUNT(*) FROM Cases");
            rs = pst.executeQuery();
            numAllCases = 0;
            
            while (rs.next()) {
                numAllCases = rs.getInt(1);
            }
            pst = con.prepareStatement("SELECT LastName, FirstName, Id FROM Cases");
            rs = pst.executeQuery();
            cases = new Tuple[numAllCases];
            for (int i =0; i<numAllCases; i++) {
                rs.next();
                cases[i] = new Tuple(rs.getString(1) + ", " + rs.getString(2), rs.getInt(3));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Prepared.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Prepared.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return cases;
    }
    
    public Tuple[] guesses() {
        Tuple[] cases = null;
        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT SUM(CASE WHEN Location=2 THEN 1 ELSE 0 END) FROM Cases");
            rs = pst.executeQuery();
            numNotTopCases = 0;
            
            while (rs.next()) {
                numNotTopCases = rs.getInt(1);
            }
            pst = con.prepareStatement("SELECT LastName, FirstName, Id FROM Cases WHERE Location=2");
            rs = pst.executeQuery();
            cases = new Tuple[numNotTopCases];
            for (int i =0; i<numNotTopCases; i++) {
                rs.next();
                cases[i] = new Tuple(rs.getString(1) + ", " + rs.getString(2), rs.getInt(3));
            }

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Prepared.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Prepared.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return cases;
    }
    
    public class Tuple { 
        public final String x; 
        public final int y; 
        public Tuple(String x, Integer y) { 
          this.x = x; 
          this.y = y; 
        } 
    }
    
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                main = new BillTrackUI();
                main.setVisible(true);
            }
        });
    }
}
