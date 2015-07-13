import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.table.AbstractTableModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdesktop.swingx.JXTable;


public class SheetUI extends JDialog{
    
    private static final long serialVersionUID = 1L;
    private static JXTable guessTable;
    private static DataModel myDataModel;

    public SheetUI(JFrame parent, String title, List<Date> starts, List<Date >ends, List<String> cats, List<String> descs) {   
        super(parent, title);
        this.setModal(true);
        System.out.println("creating the window..");
        myDataModel = new DataModel();
        // set the position of the window
        Point p = new Point(400, 400);
        setLocation(p.x, p.y);
        // Create a message
        for (int i =0; i< starts.size(); i++) {
            String[] boo = {starts.get(i).toString(), ends.get(i).toString(), cats.get(i), descs.get(i)};
            myDataModel.addRow(boo);
        }
        guessTable = new JXTable(myDataModel);
        guessTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        guessTable.packAll();
        guessTable.setPreferredScrollableViewportSize(guessTable.getPreferredSize());
        JScrollPane tablePane = new JScrollPane(guessTable);
        getContentPane().add(tablePane);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    public class DataModel extends AbstractTableModel {
        
        private static final long serialVersionUID = 1L;
        private List<String[]> tableData;
        private final String[] columnName = {"Date & Time Started", "Date & Time Ended", "Category", "Description"};
        private final int columnCount;
        
        /**
         * Constructs a dataModel as a list of String arrays
         */
        public DataModel() {
            tableData = new ArrayList<String[]>();
            columnCount = 4;
        }
        public String getColumnName(int index) {
            return columnName[index];
        }
        public int getRowCount() { return tableData.size(); }
        public int getColumnCount() { return columnCount; }
        public Object getValueAt(int row, int col) {
            return tableData.get(row)[col];
        }
        /**
         * @param boo row to add as a string array
         */
        public void addRow(String[] boo) { 
            tableData.add(boo); 
            fireTableDataChanged();
        }
        /**
         * clears all the rows from the table
         */
        public void clearRows() {
            tableData = new ArrayList<String[]>();
            fireTableDataChanged();
        }
        /**
         * @param value the value to place at the given indices
         * @param rowIndex the index of the row
         * @param colIndex the index of the column
         */
        public void setValue(String value, int rowIndex, int colIndex) {
            tableData.get(rowIndex)[colIndex] = value;
            fireTableDataChanged();
        }
    }
}
