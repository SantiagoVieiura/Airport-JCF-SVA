package core.view;

import core.model.Flight;
import core.model.Location;
import core.model.Passenger;
import core.model.Plane;
import core.controller.AirportController;
import core.controller.FlightController;
import core.controller.PassengerController;
import core.controller.PlaneController;
import core.controller.StorageController;
import core.controller.utils.Observer;
import core.controller.utils.Response;
import core.controller.utils.Status;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author edangulo
 */
public class AirportFrame extends javax.swing.JFrame implements Observer{

    /**
     * Creates new form AirportFrame
     */
    private int x, y;
    private ArrayList<Passenger> passengers;
    private ArrayList<Plane> planes;
    private ArrayList<Location> locations;
    private ArrayList<Flight> flights;
    
    
    public final StorageController storage;
    private AirportController control;
    private PassengerController passenger;
    private PlaneController planeC;
    private FlightController flightC;
    
    public AirportFrame() throws IOException {
        initComponents();

        this.passengers = new ArrayList<>();
        this.planes = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.flights = new ArrayList<>();

        this.setBackground(new Color(0, 0, 0, 0));
        this.setLocationRelativeTo(null);

        this.generateMonths();
        this.generateDays();
        this.generateHours();
        this.generateMinutes();
        this.blockPanels();
        
        this.storage = new  StorageController();
        this.storage.registerObserver(this);
        this.control = new AirportController(storage);
        this.passenger = new PassengerController(storage);
        this.planeC = new PlaneController(storage);
        this.flightC = new FlightController(storage);
        
        this.update();
        this.userSelectAdd();
        
    }
    
    @Override
    public void update() {
        refreshPassengersTable();
        refreshMyFlightsTable();
        refreshAllFlights();
        refreshAllPlanes();
        refreshAllLocations();
        
        this.cbPlaneAdd();
        this.cbFlightAdd();
        this.cbLocationAdd();
        
    }
    
    public void refreshPassengersTable() {
        ArrayList<Passenger> passengers = storage.getPassengers();
        DefaultTableModel model = (DefaultTableModel) table9.getModel(); 
        model.setRowCount(0);

        for (Passenger p : passengers) 
            model.addRow(new Object[]{p.getId(),p.getFullname(),p.getBirthDate(),p.calculateAge(),p.generateFullPhone(),p.getCountry(),p.getNumFlights()}); 
    }
    
    public void refreshMyFlightsTable() {
        DefaultTableModel model = (DefaultTableModel) table8.getModel();
        model.setRowCount(0);

        String selected = (String) userSelect.getSelectedItem();
        if (selected != null && !selected.equals("Select User")) {
            long passengerId = Long.parseLong(selected);
            List<Flight> flights = flightC.getFlightsId(passengerId);
            for (Flight flight : flights) 
                model.addRow(new Object[]{flight.getId(),flight.getDepartureDate(),flight.calculateArrivalDate()});
        }
    }
    
    public void refreshAllFlights(){
        List<Flight> flights = flightC.getFlightsId2();
        DefaultTableModel model = (DefaultTableModel) table10.getModel();
        model.setRowCount(0);

        for (Flight flight : flights) 
            model.addRow(new Object[]{flight.getId(),flight.getDepartureLocation().getAirportId(),flight.getArrivalLocation().getAirportId(),(flight.getScaleLocation() == null ? "-" : flight.getScaleLocation().getAirportId()),flight.getDepartureDate(),flight.calculateArrivalDate(),flight.getPlane().getId(),flight.getNumPassengers()});
    }
    
    public void refreshAllPlanes(){
        List<Plane> planes = planeC.getAllPlanes(); 

        DefaultTableModel model = (DefaultTableModel) table11.getModel();
        model.setRowCount(0);

        for (Plane plane : planes) 
            model.addRow(new Object[]{plane.getId(),plane.getBrand(),plane.getModel(),plane.getMaxCapacity(),plane.getAirline(),plane.getNumFlights()});
    }
    
    public void refreshAllLocations(){
        List<Location> locations = control.getAllLocations();
        DefaultTableModel model = (DefaultTableModel) table12.getModel();
        model.setRowCount(0);

        for (Location location : locations)
            model.addRow(new Object[]{location.getAirportId(),location.getAirportName(),location.getAirportCity(),location.getAirportCountry()});
    }
    
    private void cbLocationAdd(){  //PROFE PORFAVOR VEA QUE ESTOS MÉTODOS CUMPLEN MVC DEBIDO A QUE ESE STORAGE ES EN REALIDAD STORAGECONTROLLER, POR LO QUE STORAGE NO SE VE AFECTADO NI SE INVOCA DESDE ACA
        List<String> ids = storage.getLocationIds();
        cbLocation51.removeAllItems();
        cbLocation52.removeAllItems();
        cbLocation53.removeAllItems();
        cbLocation51.addItem("Location");
        cbLocation52.addItem("Location");
        cbLocation53.addItem("Location");
        
        for (String id : ids){
            cbLocation51.addItem(id);
            cbLocation52.addItem(id);
            cbLocation53.addItem(id);
        }
    }
    
    private void cbPlaneAdd(){//PROFE PORFAVOR VEA QUE ESTOS MÉTODOS CUMPLEN MVC DEBIDO A QUE ESE STORAGE ES EN REALIDAD STORAGECONTROLLER, POR LO QUE STORAGE NO SE VE AFECTADO NI SE INVOCA DESDE ACA
        List<String> ids = storage.getPlaneIds();
        cbPlane.removeAllItems();
        cbPlane.addItem("Plane");
        for (String id : ids)
            cbPlane.addItem(id);
    }
    
    private void cbFlightAdd(){//PROFE PORFAVOR VEA QUE ESTOS MÉTODOS CUMPLEN MVC DEBIDO A QUE ESE STORAGE ES EN REALIDAD STORAGECONTROLLER, POR LO QUE STORAGE NO SE VE AFECTADO NI SE INVOCA DESDE ACA
        List<String> ids = storage.getFlightIds();
        cbFlight.removeAllItems();
        cbId.removeAllItems();
        cbFlight.addItem("Flight");
        cbId.addItem("ID");
        for (String id : ids){
            cbFlight.addItem(id);
            cbId.addItem(id);
        }
    }
    
    private void userSelectAdd(){//PROFE PORFAVOR VEA QUE ESTOS MÉTODOS CUMPLEN MVC DEBIDO A QUE ESE STORAGE ES EN REALIDAD STORAGECONTROLLER, POR LO QUE STORAGE NO SE VE AFECTADO NI SE INVOCA DESDE ACA
        System.out.println("A");
        List<String> ids = storage.getPassengerIds();
        userSelect.removeAllItems();
        userSelect.addItem("Select User");
        for (String id : ids) 
            userSelect.addItem(id);
    }

    private void blockPanels() {
        //9, 11
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) {
            if (i != 9 && i != 11) {
                jTabbedPane1.setEnabledAt(i, false);
            }
        }
    }

    private void generateMonths() {
        for (int i = 1; i < 13; i++) {
            MONTH.addItem("" + i);
            MONTH1.addItem("" + i);
            MONTH5.addItem("" + i);
        }
    }

    private void generateDays() {
        for (int i = 1; i < 32; i++) {
            DAY.addItem("" + i);
            DAY1.addItem("" + i);
            DAY5.addItem("" + i);
        }
    }

    private void generateHours() {
        for (int i = 0; i < 24; i++) {
            MONTH2.addItem("" + i);
            MONTH3.addItem("" + i);
            MONTH4.addItem("" + i);
            cbHour.addItem("" + i);
        }
    }

    private void generateMinutes() {
        for (int i = 0; i < 60; i++) {
            DAY2.addItem("" + i);
            DAY3.addItem("" + i);
            DAY4.addItem("" + i);
            cbMinute.addItem("" + i);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRound1 = new core.view.PanelRound();
        panelRound2 = new core.view.PanelRound();
        btnExit = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        panel1 = new javax.swing.JPanel();
        user = new javax.swing.JRadioButton();
        administrator = new javax.swing.JRadioButton();
        userSelect = new javax.swing.JComboBox<>();
        panel2 = new javax.swing.JPanel();
        lblCountry = new javax.swing.JLabel();
        lblId2 = new javax.swing.JLabel();
        lblFirstName = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        lblBirthday = new javax.swing.JLabel();
        lblAddSymbol = new javax.swing.JLabel();
        txtPrefix = new javax.swing.JTextField();
        txtId2 = new javax.swing.JTextField();
        txtBirthdate = new javax.swing.JTextField();
        txtCountry = new javax.swing.JTextField();
        txtPhoneNumber = new javax.swing.JTextField();
        lblPhone = new javax.swing.JLabel();
        lblSeparator21 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        lblSeparator23 = new javax.swing.JLabel();
        MONTH = new javax.swing.JComboBox<>();
        txtFirstName = new javax.swing.JTextField();
        lblSeparator22 = new javax.swing.JLabel();
        DAY = new javax.swing.JComboBox<>();
        btnRegister = new javax.swing.JButton();
        panel3 = new javax.swing.JPanel();
        lblId3 = new javax.swing.JLabel();
        txtId3 = new javax.swing.JTextField();
        lblBrand = new javax.swing.JLabel();
        txtBrand = new javax.swing.JTextField();
        txtModel = new javax.swing.JTextField();
        lblModel = new javax.swing.JLabel();
        txtMaxCap = new javax.swing.JTextField();
        lblMaxCap = new javax.swing.JLabel();
        txtAirline = new javax.swing.JTextField();
        lblAirline = new javax.swing.JLabel();
        btnCreatePlane = new javax.swing.JButton();
        panel4 = new javax.swing.JPanel();
        lblAirpotId = new javax.swing.JLabel();
        txtAirportId = new javax.swing.JTextField();
        lblAirpotName = new javax.swing.JLabel();
        txtAirportName = new javax.swing.JTextField();
        txtAirportCity = new javax.swing.JTextField();
        lblAirportCity = new javax.swing.JLabel();
        lblAirpotCountry = new javax.swing.JLabel();
        txtAirportCountry = new javax.swing.JTextField();
        txtAirportLatid = new javax.swing.JTextField();
        lblAirportLatit = new javax.swing.JLabel();
        lblAirportLong = new javax.swing.JLabel();
        txtAirportLong = new javax.swing.JTextField();
        btnCreateAirport = new javax.swing.JButton();
        panel5 = new javax.swing.JPanel();
        lblId5 = new javax.swing.JLabel();
        txtId5 = new javax.swing.JTextField();
        lblPlane = new javax.swing.JLabel();
        cbPlane = new javax.swing.JComboBox<>();
        cbLocation51 = new javax.swing.JComboBox<>();
        lblDepartureLocation = new javax.swing.JLabel();
        cbLocation52 = new javax.swing.JComboBox<>();
        lblArrivalLocation = new javax.swing.JLabel();
        lblScaleLocation = new javax.swing.JLabel();
        cbLocation53 = new javax.swing.JComboBox<>();
        lblDuration52 = new javax.swing.JLabel();
        lblDuration51 = new javax.swing.JLabel();
        lblDepartureDate = new javax.swing.JLabel();
        txtDepartureDate = new javax.swing.JTextField();
        lblSeparator51 = new javax.swing.JLabel();
        MONTH1 = new javax.swing.JComboBox<>();
        lblSeparator54 = new javax.swing.JLabel();
        DAY1 = new javax.swing.JComboBox<>();
        lblSeparator55 = new javax.swing.JLabel();
        MONTH2 = new javax.swing.JComboBox<>();
        lblSeparator56 = new javax.swing.JLabel();
        DAY2 = new javax.swing.JComboBox<>();
        MONTH3 = new javax.swing.JComboBox<>();
        lblSeparator52 = new javax.swing.JLabel();
        DAY3 = new javax.swing.JComboBox<>();
        lblSeparator53 = new javax.swing.JLabel();
        MONTH4 = new javax.swing.JComboBox<>();
        DAY4 = new javax.swing.JComboBox<>();
        btnCreateFlight = new javax.swing.JButton();
        panel6 = new javax.swing.JPanel();
        lblI6 = new javax.swing.JLabel();
        txtId6 = new javax.swing.JTextField();
        lblFirstName6 = new javax.swing.JLabel();
        txtFirstName6 = new javax.swing.JTextField();
        lblLastName6 = new javax.swing.JLabel();
        txtLastName6 = new javax.swing.JTextField();
        lblBirthdate6 = new javax.swing.JLabel();
        txtBirthdate6 = new javax.swing.JTextField();
        MONTH5 = new javax.swing.JComboBox<>();
        DAY5 = new javax.swing.JComboBox<>();
        txtPhoneNumber6 = new javax.swing.JTextField();
        lblSeparator61 = new javax.swing.JLabel();
        txtPrefix6 = new javax.swing.JTextField();
        lblAddSymbol5 = new javax.swing.JLabel();
        lblPhone6 = new javax.swing.JLabel();
        lblCountry6 = new javax.swing.JLabel();
        txtCountry6 = new javax.swing.JTextField();
        btnUpdateInfo = new javax.swing.JButton();
        panel7 = new javax.swing.JPanel();
        txtId7 = new javax.swing.JTextField();
        lblId7 = new javax.swing.JLabel();
        lblFight = new javax.swing.JLabel();
        cbFlight = new javax.swing.JComboBox<>();
        btnAddFlight = new javax.swing.JButton();
        panel8 = new javax.swing.JPanel();
        scrollPane8 = new javax.swing.JScrollPane();
        table8 = new javax.swing.JTable();
        btnRefreshMyFlights = new javax.swing.JButton();
        panel9 = new javax.swing.JPanel();
        scrollPane9 = new javax.swing.JScrollPane();
        table9 = new javax.swing.JTable();
        btnRefreshPassengers = new javax.swing.JButton();
        panel10 = new javax.swing.JPanel();
        scrollPane10 = new javax.swing.JScrollPane();
        table10 = new javax.swing.JTable();
        btnRefreshFlights = new javax.swing.JButton();
        panel11 = new javax.swing.JPanel();
        btnRefreshPlanes = new javax.swing.JButton();
        scrollPane11 = new javax.swing.JScrollPane();
        table11 = new javax.swing.JTable();
        panel12 = new javax.swing.JPanel();
        scrollPane12 = new javax.swing.JScrollPane();
        table12 = new javax.swing.JTable();
        btnRefreshLocations = new javax.swing.JButton();
        panel13 = new javax.swing.JPanel();
        cbHour = new javax.swing.JComboBox<>();
        lblHours = new javax.swing.JLabel();
        lblId13 = new javax.swing.JLabel();
        cbId = new javax.swing.JComboBox<>();
        lblMinutes = new javax.swing.JLabel();
        cbMinute = new javax.swing.JComboBox<>();
        btnDelay = new javax.swing.JButton();
        panelRound3 = new core.view.PanelRound();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelRound1.setRadius(40);
        panelRound1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRound2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelRound2MouseDragged(evt);
            }
        });
        panelRound2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelRound2MousePressed(evt);
            }
        });

        btnExit.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnExit.setText("X");
        btnExit.setBorderPainted(false);
        btnExit.setContentAreaFilled(false);
        btnExit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelRound2Layout = new javax.swing.GroupLayout(panelRound2);
        panelRound2.setLayout(panelRound2Layout);
        panelRound2Layout.setHorizontalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound2Layout.createSequentialGroup()
                .addContainerGap(1083, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        panelRound2Layout.setVerticalGroup(
            panelRound2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound2Layout.createSequentialGroup()
                .addComponent(btnExit)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        panelRound1.add(panelRound2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1150, -1));

        jTabbedPane1.setFont(new java.awt.Font("Yu Gothic UI", 0, 14)); // NOI18N

        panel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        user.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        user.setText("User");
        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });
        panel1.add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 230, -1, -1));

        administrator.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        administrator.setText("Administrator");
        administrator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                administratorActionPerformed(evt);
            }
        });
        panel1.add(administrator, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 164, -1, -1));

        userSelect.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        userSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select User" }));
        userSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSelectActionPerformed(evt);
            }
        });
        panel1.add(userSelect, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 300, 130, -1));

        jTabbedPane1.addTab("Administration", panel1);

        panel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCountry.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCountry.setText("Country:");
        panel2.add(lblCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, -1, -1));

        lblId2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblId2.setText("ID:");
        panel2.add(lblId2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, -1, -1));

        lblFirstName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblFirstName.setText("First Name:");
        panel2.add(lblFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, -1, -1));

        lblLastName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblLastName.setText("Last Name:");
        panel2.add(lblLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, -1, -1));

        lblBirthday.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblBirthday.setText("Birthdate:");
        panel2.add(lblBirthday, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, -1, -1));

        lblAddSymbol.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAddSymbol.setText("+");
        panel2.add(lblAddSymbol, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 20, -1));

        txtPrefix.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtPrefix, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 340, 50, -1));

        txtId2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtId2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, 130, -1));

        txtBirthdate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtBirthdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 90, -1));

        txtCountry.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtCountry, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, 130, -1));

        txtPhoneNumber.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtPhoneNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 340, 130, -1));

        lblPhone.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPhone.setText("Phone:");
        panel2.add(lblPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, -1, -1));

        lblSeparator21.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator21.setText("-");
        panel2.add(lblSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 280, 30, -1));

        txtLastName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtLastName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 220, 130, -1));

        lblSeparator23.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator23.setText("-");
        panel2.add(lblSeparator23, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 30, -1));

        MONTH.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));
        panel2.add(MONTH, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 280, -1, -1));

        txtFirstName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel2.add(txtFirstName, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 130, -1));

        lblSeparator22.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator22.setText("-");
        panel2.add(lblSeparator22, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, 30, -1));

        DAY.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));
        panel2.add(DAY, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, -1, -1));

        btnRegister.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        panel2.add(btnRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 480, -1, -1));

        jTabbedPane1.addTab("Passenger registration", panel2);

        panel3.setLayout(null);

        lblId3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblId3.setText("ID:");
        panel3.add(lblId3);
        lblId3.setBounds(53, 96, 22, 25);

        txtId3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel3.add(txtId3);
        txtId3.setBounds(180, 93, 130, 31);

        lblBrand.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblBrand.setText("Brand:");
        panel3.add(lblBrand);
        lblBrand.setBounds(53, 157, 50, 25);

        txtBrand.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel3.add(txtBrand);
        txtBrand.setBounds(180, 154, 130, 31);

        txtModel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel3.add(txtModel);
        txtModel.setBounds(180, 213, 130, 31);

        lblModel.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblModel.setText("Model:");
        panel3.add(lblModel);
        lblModel.setBounds(53, 216, 55, 25);

        txtMaxCap.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel3.add(txtMaxCap);
        txtMaxCap.setBounds(180, 273, 130, 31);

        lblMaxCap.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMaxCap.setText("Max Capacity:");
        panel3.add(lblMaxCap);
        lblMaxCap.setBounds(53, 276, 109, 25);

        txtAirline.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        panel3.add(txtAirline);
        txtAirline.setBounds(180, 333, 130, 31);

        lblAirline.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirline.setText("Airline:");
        panel3.add(lblAirline);
        lblAirline.setBounds(53, 336, 70, 25);

        btnCreatePlane.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCreatePlane.setText("Create");
        btnCreatePlane.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreatePlaneActionPerformed(evt);
            }
        });
        panel3.add(btnCreatePlane);
        btnCreatePlane.setBounds(490, 480, 120, 40);

        jTabbedPane1.addTab("Airplane registration", panel3);

        lblAirpotId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirpotId.setText("Airport ID:");

        txtAirportId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtAirportId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAirportIdActionPerformed(evt);
            }
        });

        lblAirpotName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirpotName.setText("Airport name:");

        txtAirportName.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        txtAirportCity.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblAirportCity.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirportCity.setText("Airport city:");

        lblAirpotCountry.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirpotCountry.setText("Airport country:");

        txtAirportCountry.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        txtAirportLatid.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblAirportLatit.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirportLatit.setText("Airport latitude:");

        lblAirportLong.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAirportLong.setText("Airport longitude:");

        txtAirportLong.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        btnCreateAirport.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCreateAirport.setText("Create");
        btnCreateAirport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateAirportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel4Layout = new javax.swing.GroupLayout(panel4);
        panel4.setLayout(panel4Layout);
        panel4Layout.setHorizontalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel4Layout.createSequentialGroup()
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel4Layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAirpotId)
                            .addComponent(lblAirpotName)
                            .addComponent(lblAirportCity)
                            .addComponent(lblAirpotCountry)
                            .addComponent(lblAirportLatit)
                            .addComponent(lblAirportLong))
                        .addGap(80, 80, 80)
                        .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAirportLong, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAirportId, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAirportName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAirportCity, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAirportCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAirportLatid, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panel4Layout.createSequentialGroup()
                        .addGap(515, 515, 515)
                        .addComponent(btnCreateAirport, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(515, 515, 515))
        );
        panel4Layout.setVerticalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel4Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panel4Layout.createSequentialGroup()
                        .addComponent(lblAirpotId)
                        .addGap(36, 36, 36)
                        .addComponent(lblAirpotName)
                        .addGap(34, 34, 34)
                        .addComponent(lblAirportCity)
                        .addGap(35, 35, 35)
                        .addComponent(lblAirpotCountry)
                        .addGap(35, 35, 35)
                        .addComponent(lblAirportLatit))
                    .addGroup(panel4Layout.createSequentialGroup()
                        .addComponent(txtAirportId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(txtAirportName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(txtAirportCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(txtAirportCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(txtAirportLatid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addGroup(panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAirportLong)
                    .addComponent(txtAirportLong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 71, Short.MAX_VALUE)
                .addComponent(btnCreateAirport, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47))
        );

        jTabbedPane1.addTab("Location registration", panel4);

        lblId5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblId5.setText("ID:");

        txtId5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblPlane.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPlane.setText("Plane:");

        cbPlane.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbPlane.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Plane" }));

        cbLocation51.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbLocation51.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        lblDepartureLocation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDepartureLocation.setText("Departure location:");

        cbLocation52.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbLocation52.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        lblArrivalLocation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblArrivalLocation.setText("Arrival location:");

        lblScaleLocation.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblScaleLocation.setText("Scale location:");

        cbLocation53.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbLocation53.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Location" }));

        lblDuration52.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDuration52.setText("Duration:");

        lblDuration51.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDuration51.setText("Duration:");

        lblDepartureDate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblDepartureDate.setText("Departure date:");

        txtDepartureDate.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblSeparator51.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator51.setText("-");

        MONTH1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        lblSeparator54.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator54.setText("-");

        DAY1.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        lblSeparator55.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator55.setText("-");

        MONTH2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        lblSeparator56.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator56.setText("-");

        DAY2.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        MONTH3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        lblSeparator52.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator52.setText("-");

        DAY3.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        lblSeparator53.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator53.setText("-");

        MONTH4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        DAY4.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        btnCreateFlight.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnCreateFlight.setText("Create");
        btnCreateFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateFlightActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel5Layout = new javax.swing.GroupLayout(panel5);
        panel5.setLayout(panel5Layout);
        panel5Layout.setHorizontalGroup(
            panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel5Layout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addComponent(lblScaleLocation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbLocation53, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                        .addComponent(lblArrivalLocation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbLocation52, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addComponent(lblDepartureLocation)
                        .addGap(46, 46, 46)
                        .addComponent(cbLocation51, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId5)
                            .addComponent(lblPlane))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId5)
                            .addComponent(cbPlane, 0, 130, Short.MAX_VALUE))))
                .addGap(45, 45, 45)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDuration52)
                    .addComponent(lblDuration51)
                    .addComponent(lblDepartureDate))
                .addGap(18, 18, 18)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addComponent(txtDepartureDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(MONTH1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSeparator51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSeparator54, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(DAY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(MONTH2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSeparator55, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSeparator56, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(DAY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30))
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addComponent(MONTH3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSeparator52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(DAY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(panel5Layout.createSequentialGroup()
                                .addComponent(MONTH4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14)
                                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSeparator53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panel5Layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addComponent(DAY4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreateFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(530, 530, 530))
        );
        panel5Layout.setVerticalGroup(
            panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel5Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblId5))
                    .addComponent(txtId5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPlane)
                    .addComponent(cbPlane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(MONTH2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeparator55)
                    .addComponent(lblSeparator56)
                    .addComponent(DAY2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel5Layout.createSequentialGroup()
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblDepartureLocation)
                                .addComponent(cbLocation51, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDepartureDate))
                            .addComponent(txtDepartureDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(MONTH1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSeparator51)
                            .addComponent(lblSeparator54)
                            .addComponent(DAY1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblArrivalLocation)
                                .addComponent(cbLocation52, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDuration51))
                            .addComponent(MONTH3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSeparator52)
                            .addComponent(DAY3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MONTH4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblSeparator53)
                            .addComponent(DAY4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblScaleLocation)
                                .addComponent(cbLocation53, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDuration52)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addComponent(btnCreateFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        jTabbedPane1.addTab("Flight registration", panel5);

        lblI6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblI6.setText("ID:");

        txtId6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        txtId6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtId6ActionPerformed(evt);
            }
        });

        lblFirstName6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblFirstName6.setText("First Name:");

        txtFirstName6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblLastName6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblLastName6.setText("Last Name:");

        txtLastName6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblBirthdate6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblBirthdate6.setText("Birthdate:");

        txtBirthdate6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        MONTH5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        MONTH5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Month" }));

        DAY5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        DAY5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Day" }));

        txtPhoneNumber6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblSeparator61.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblSeparator61.setText("-");

        txtPrefix6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblAddSymbol5.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblAddSymbol5.setText("+");

        lblPhone6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblPhone6.setText("Phone:");

        lblCountry6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblCountry6.setText("Country:");

        txtCountry6.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        btnUpdateInfo.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnUpdateInfo.setText("Update");
        btnUpdateInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateInfoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel6Layout = new javax.swing.GroupLayout(panel6);
        panel6.setLayout(panel6Layout);
        panel6Layout.setHorizontalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel6Layout.createSequentialGroup()
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel6Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblI6)
                                .addGap(108, 108, 108)
                                .addComponent(txtId6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblFirstName6)
                                .addGap(41, 41, 41)
                                .addComponent(txtFirstName6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblLastName6)
                                .addGap(43, 43, 43)
                                .addComponent(txtLastName6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblBirthdate6)
                                .addGap(55, 55, 55)
                                .addComponent(txtBirthdate6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(MONTH5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(DAY5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblPhone6)
                                .addGap(56, 56, 56)
                                .addComponent(lblAddSymbol5, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtPrefix6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(lblSeparator61, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtPhoneNumber6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel6Layout.createSequentialGroup()
                                .addComponent(lblCountry6)
                                .addGap(63, 63, 63)
                                .addComponent(txtCountry6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panel6Layout.createSequentialGroup()
                        .addGap(507, 507, 507)
                        .addComponent(btnUpdateInfo)))
                .addContainerGap(555, Short.MAX_VALUE))
        );
        panel6Layout.setVerticalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel6Layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblI6)
                    .addComponent(txtId6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFirstName6)
                    .addComponent(txtFirstName6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLastName6)
                    .addComponent(txtLastName6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBirthdate6)
                    .addComponent(txtBirthdate6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MONTH5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DAY5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPhone6)
                    .addComponent(lblAddSymbol5)
                    .addComponent(txtPrefix6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeparator61)
                    .addComponent(txtPhoneNumber6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCountry6)
                    .addComponent(txtCountry6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(btnUpdateInfo)
                .addGap(113, 113, 113))
        );

        jTabbedPane1.addTab("Update info", panel6);

        txtId7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N

        lblId7.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblId7.setText("ID:");

        lblFight.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblFight.setText("Flight:");

        cbFlight.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbFlight.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Flight" }));

        btnAddFlight.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnAddFlight.setText("Add");
        btnAddFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddFlightActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel7Layout = new javax.swing.GroupLayout(panel7);
        panel7.setLayout(panel7Layout);
        panel7Layout.setHorizontalGroup(
            panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel7Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblId7)
                    .addComponent(lblFight))
                .addGap(79, 79, 79)
                .addGroup(panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbFlight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtId7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(829, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAddFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(509, 509, 509))
        );
        panel7Layout.setVerticalGroup(
            panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel7Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addGroup(panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel7Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblId7))
                    .addComponent(txtId7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFight)
                    .addComponent(cbFlight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 288, Short.MAX_VALUE)
                .addComponent(btnAddFlight, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );

        jTabbedPane1.addTab("Add to flight", panel7);

        table8.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        table8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Departure Date", "Arrival Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane8.setViewportView(table8);

        btnRefreshMyFlights.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRefreshMyFlights.setText("Refresh");
        btnRefreshMyFlights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshMyFlightsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel8Layout = new javax.swing.GroupLayout(panel8);
        panel8.setLayout(panel8Layout);
        panel8Layout.setHorizontalGroup(
            panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel8Layout.createSequentialGroup()
                .addGap(269, 269, 269)
                .addComponent(scrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(291, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnRefreshMyFlights)
                .addGap(527, 527, 527))
        );
        panel8Layout.setVerticalGroup(
            panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel8Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(scrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(btnRefreshMyFlights)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show my flights", panel8);

        table9.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        table9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Name", "Birthdate", "Age", "Phone", "Country", "Num Flight"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane9.setViewportView(table9);

        btnRefreshPassengers.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRefreshPassengers.setText("Refresh");
        btnRefreshPassengers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPassengersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel9Layout = new javax.swing.GroupLayout(panel9);
        panel9.setLayout(panel9Layout);
        panel9Layout.setHorizontalGroup(
            panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel9Layout.createSequentialGroup()
                .addGroup(panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel9Layout.createSequentialGroup()
                        .addGap(489, 489, 489)
                        .addComponent(btnRefreshPassengers))
                    .addGroup(panel9Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(scrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1078, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        panel9Layout.setVerticalGroup(
            panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel9Layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addComponent(scrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRefreshPassengers)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Show all passengers", panel9);

        table10.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        table10.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Departure Airport ID", "Arrival Airport ID", "Scale Airport ID", "Departure Date", "Arrival Date", "Plane ID", "Number Passengers"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane10.setViewportView(table10);

        btnRefreshFlights.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRefreshFlights.setText("Refresh");
        btnRefreshFlights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshFlightsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel10Layout = new javax.swing.GroupLayout(panel10);
        panel10.setLayout(panel10Layout);
        panel10Layout.setHorizontalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel10Layout.createSequentialGroup()
                .addGroup(panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel10Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(scrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel10Layout.createSequentialGroup()
                        .addGap(521, 521, 521)
                        .addComponent(btnRefreshFlights)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        panel10Layout.setVerticalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel10Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(scrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRefreshFlights)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Show all flights", panel10);

        btnRefreshPlanes.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRefreshPlanes.setText("Refresh");
        btnRefreshPlanes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshPlanesActionPerformed(evt);
            }
        });

        table11.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Brand", "Model", "Max Capacity", "Airline", "Number Flights"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane11.setViewportView(table11);

        javax.swing.GroupLayout panel11Layout = new javax.swing.GroupLayout(panel11);
        panel11.setLayout(panel11Layout);
        panel11Layout.setHorizontalGroup(
            panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel11Layout.createSequentialGroup()
                .addGroup(panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel11Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(btnRefreshPlanes))
                    .addGroup(panel11Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(scrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 816, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        panel11Layout.setVerticalGroup(
            panel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel11Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addComponent(scrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnRefreshPlanes)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all planes", panel11);

        table12.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Airport ID", "Airport Name", "City", "Country"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPane12.setViewportView(table12);

        btnRefreshLocations.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnRefreshLocations.setText("Refresh");
        btnRefreshLocations.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshLocationsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel12Layout = new javax.swing.GroupLayout(panel12);
        panel12.setLayout(panel12Layout);
        panel12Layout.setHorizontalGroup(
            panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel12Layout.createSequentialGroup()
                .addGroup(panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel12Layout.createSequentialGroup()
                        .addGap(508, 508, 508)
                        .addComponent(btnRefreshLocations))
                    .addGroup(panel12Layout.createSequentialGroup()
                        .addGap(226, 226, 226)
                        .addComponent(scrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(272, Short.MAX_VALUE))
        );
        panel12Layout.setVerticalGroup(
            panel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel12Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addComponent(scrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(btnRefreshLocations)
                .addGap(17, 17, 17))
        );

        jTabbedPane1.addTab("Show all locations", panel12);

        cbHour.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbHour.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hour" }));

        lblHours.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblHours.setText("Hours:");

        lblId13.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblId13.setText("ID:");

        cbId.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID" }));

        lblMinutes.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        lblMinutes.setText("Minutes:");

        cbMinute.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        cbMinute.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Minute" }));

        btnDelay.setFont(new java.awt.Font("Yu Gothic UI", 0, 18)); // NOI18N
        btnDelay.setText("Delay");
        btnDelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel13Layout = new javax.swing.GroupLayout(panel13);
        panel13.setLayout(panel13Layout);
        panel13Layout.setHorizontalGroup(
            panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel13Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel13Layout.createSequentialGroup()
                        .addComponent(lblMinutes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel13Layout.createSequentialGroup()
                        .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblId13)
                            .addComponent(lblHours))
                        .addGap(79, 79, 79)
                        .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbHour, 0, 105, Short.MAX_VALUE)
                            .addComponent(cbId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(820, 820, 820))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel13Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDelay)
                .addGap(531, 531, 531))
        );
        panel13Layout.setVerticalGroup(
            panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel13Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId13)
                    .addComponent(cbId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHours)
                    .addComponent(cbHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(panel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMinutes)
                    .addComponent(cbMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 307, Short.MAX_VALUE)
                .addComponent(btnDelay)
                .addGap(33, 33, 33))
        );

        jTabbedPane1.addTab("Delay flight", panel13);

        panelRound1.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 41, 1150, 620));

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1150, Short.MAX_VALUE)
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        panelRound1.add(panelRound3, new org.netbeans.lib.awtextra.AbsoluteConstraints(-2, 660, 1150, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelRound1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void panelRound2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_panelRound2MousePressed

    private void panelRound2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelRound2MouseDragged
        this.setLocation(this.getLocation().x + evt.getX() - x, this.getLocation().y + evt.getY() - y);
    }//GEN-LAST:event_panelRound2MouseDragged

    private void administratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_administratorActionPerformed
        if (user.isSelected()) {
            user.setSelected(false);
            userSelect.setSelectedIndex(0);

        }
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) 
            jTabbedPane1.setEnabledAt(i, true);

        jTabbedPane1.setEnabledAt(5, false);
        jTabbedPane1.setEnabledAt(6, false);
        jTabbedPane1.setEnabledAt(7, false);
    }//GEN-LAST:event_administratorActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        if (administrator.isSelected()) {
            administrator.setSelected(false);
        }
        for (int i = 1; i < jTabbedPane1.getTabCount(); i++) 
            jTabbedPane1.setEnabledAt(i, false);

        jTabbedPane1.setEnabledAt(9, true);
        jTabbedPane1.setEnabledAt(5, true);
        jTabbedPane1.setEnabledAt(6, true);
        jTabbedPane1.setEnabledAt(7, true);
        jTabbedPane1.setEnabledAt(11, true);
    }//GEN-LAST:event_userActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        String id = txtId2.getText();
        String firstname = txtFirstName.getText();
        String lastname = txtLastName.getText();
        String year = txtBirthdate.getText();
        String month = MONTH.getItemAt(MONTH.getSelectedIndex());
        String day = String.valueOf(DAY.getItemAt(DAY.getSelectedIndex()));
        String phoneCode = txtPrefix.getText();
        String phone = txtPhoneNumber.getText();
        String country = txtCountry.getText();
        
        
        Response response = passenger.registerPassenger(id, firstname, lastname, day, month, year, phoneCode, phone, country);
        if (Status.CREATED == response.getStatus()){
            update();
            this.userSelectAdd();
            JOptionPane.showMessageDialog(null, response.getMessage(), "Successfully registered", JOptionPane.INFORMATION_MESSAGE);
            txtId2.setText("");
            txtFirstName.setText("");
            txtLastName.setText("");
            txtBirthdate.setText("");
            txtPrefix.setText("");
            txtPhoneNumber.setText("");
            txtCountry.setText("");
            DAY.setSelectedIndex(0);
            MONTH.setSelectedIndex(0);
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnRegisterActionPerformed

    private void btnCreatePlaneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreatePlaneActionPerformed
        // TODO add your handling code here:
        String id = txtId3.getText();
        String brand = txtBrand.getText();
        String model = txtModel.getText();
        String maxCapacity = txtMaxCap.getText();
        String airline = txtAirline.getText();


        Response response = planeC.registerPlane(id, brand, model, maxCapacity, airline);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Successfully Registered", JOptionPane.INFORMATION_MESSAGE);
            txtId3.setText("");
            txtBrand.setText("");
            txtModel.setText("");
            txtMaxCap.setText("");
            txtAirline.setText("");
            this.cbPlane.addItem(id);
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);

        
    }//GEN-LAST:event_btnCreatePlaneActionPerformed

    private void btnCreateAirportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateAirportActionPerformed
        String id = txtAirportId.getText();
        String name = txtAirportName.getText();
        String city = txtAirportCity.getText();
        String country = txtAirportCountry.getText();
        String latitude = txtAirportLatid.getText();
        String longitude = txtAirportLong.getText();


        Response response = control.registerAirport(id, name, city, country, latitude, longitude);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Successfully Registered", JOptionPane.INFORMATION_MESSAGE);
            txtAirportId.setText("");
            txtAirportName.setText("");
            txtAirportCity.setText("");
            txtAirportCountry.setText("");
            txtAirportLatid.setText("");
            txtAirportLong.setText("");
            this.cbLocation51.addItem(id);
            this.cbLocation52.addItem(id);
            this.cbLocation53.addItem(id);
        }
        else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);

    }//GEN-LAST:event_btnCreateAirportActionPerformed

    private void btnCreateFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateFlightActionPerformed
        // TODO add your handling code here:
        String id = txtId5.getText();
        String planeId = cbPlane.getItemAt(cbPlane.getSelectedIndex());
        String departureLocationId = cbLocation51.getItemAt(cbLocation51.getSelectedIndex());
        String arrivalLocationId = cbLocation52.getItemAt(cbLocation52.getSelectedIndex());
        String scaleLocationId = cbLocation53.getItemAt(cbLocation53.getSelectedIndex());
        String year = txtDepartureDate.getText();
        String month = MONTH1.getItemAt(MONTH1.getSelectedIndex());
        String day = DAY1.getItemAt(DAY1.getSelectedIndex());
        String hour = MONTH2.getItemAt(MONTH2.getSelectedIndex());
        String minutes = DAY2.getItemAt(DAY2.getSelectedIndex());
        String hoursDurationsArrival = MONTH3.getItemAt(MONTH3.getSelectedIndex());
        String minutesDurationsArrival = DAY3.getItemAt(DAY3.getSelectedIndex());
        String hoursDurationsScale = MONTH4.getItemAt(MONTH4.getSelectedIndex());
        String minutesDurationsScale = DAY4.getItemAt(DAY4.getSelectedIndex());


        Response response = flightC.registerFlight(id, planeId, departureLocationId, arrivalLocationId, scaleLocationId, year, month, day, hour, minutes, hoursDurationsArrival, minutesDurationsArrival, hoursDurationsScale, minutesDurationsScale);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Successfully registered", JOptionPane.INFORMATION_MESSAGE);
            txtId5.setText("");
            txtDepartureDate.setText("");
            MONTH1.setSelectedIndex(0);
            DAY1.setSelectedIndex(0);
            MONTH2.setSelectedIndex(0);
            DAY2.setSelectedIndex(0);
            MONTH3.setSelectedIndex(0);
            DAY3.setSelectedIndex(0);
            MONTH4.setSelectedIndex(0);
            DAY4.setSelectedIndex(0);
            cbPlane.setSelectedIndex(0);
            cbLocation51.setSelectedIndex(0);
            cbLocation52.setSelectedIndex(0);
            cbLocation53.setSelectedIndex(0);
            
            this.cbFlight.addItem(id);
            this.cbId.addItem(id);
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Register Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnCreateFlightActionPerformed

    private void btnUpdateInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateInfoActionPerformed
        // TODO add your handling code here:
        String id = txtId6.getText();
        String firstname = txtFirstName6.getText();
        String lastname = txtLastName6.getText();
        String year = txtBirthdate6.getText();
        String month = MONTH.getItemAt(MONTH5.getSelectedIndex());
        String day = DAY.getItemAt(DAY5.getSelectedIndex());
        String phoneCode = txtPrefix6.getText();
        String phone = txtPhoneNumber6.getText();
        String country = txtCountry6.getText();
        
        
        Response response = passenger.updatePassenger(id, firstname, lastname, day, month, year, phoneCode, phone, country);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Successfully Updated", JOptionPane.INFORMATION_MESSAGE);
            txtId6.setText("");
            txtFirstName6.setText("");
            txtLastName6.setText("");
            txtBirthdate6.setText("");
            MONTH5.setSelectedIndex(0);
            DAY5.setSelectedIndex(0);
            txtPrefix6.setText("");
            txtPhoneNumber6.setText("");
            txtCountry6.setText("");
            
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnUpdateInfoActionPerformed

    private void btnAddFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddFlightActionPerformed
        // TODO add your handling code here:
        String passengerId = txtId7.getText();
        String flightId = cbFlight.getItemAt(cbFlight.getSelectedIndex());
        
        Response response = flightC.addToFlight(passengerId, flightId);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Flight Booked Successfully", JOptionPane.INFORMATION_MESSAGE);
            txtId7.setText("");
            cbFlight.setSelectedIndex(0);
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Update Error", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnAddFlightActionPerformed

    private void btnDelayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelayActionPerformed
        String flightId = cbId.getItemAt(cbId.getSelectedIndex());
        String hours = cbHour.getItemAt(cbHour.getSelectedIndex());
        String minutes = cbMinute.getItemAt(cbMinute.getSelectedIndex());

        Response response = flightC.delayFlight(flightId, hours, minutes);
        if (Status.CREATED == response.getStatus()){
            JOptionPane.showMessageDialog(null, response.getMessage(), "Flight Delayed Successfully", JOptionPane.INFORMATION_MESSAGE);
            cbId.setSelectedIndex(0);
            cbHour.setSelectedIndex(0);
            cbMinute.setSelectedIndex(0);
            update();
        }else
            JOptionPane.showMessageDialog(null, response.getMessage(), "Error while trying to Delay", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnDelayActionPerformed

    private void btnRefreshMyFlightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshMyFlightsActionPerformed
        refreshMyFlightsTable();
    }//GEN-LAST:event_btnRefreshMyFlightsActionPerformed

    private void btnRefreshPassengersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPassengersActionPerformed
        refreshPassengersTable();
    }//GEN-LAST:event_btnRefreshPassengersActionPerformed

    private void btnRefreshFlightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshFlightsActionPerformed
        refreshAllFlights();
    }//GEN-LAST:event_btnRefreshFlightsActionPerformed

    private void btnRefreshPlanesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshPlanesActionPerformed
        refreshAllPlanes();
    }//GEN-LAST:event_btnRefreshPlanesActionPerformed

    private void btnRefreshLocationsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshLocationsActionPerformed
       refreshAllLocations();
    }//GEN-LAST:event_btnRefreshLocationsActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnExitActionPerformed

    private void userSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSelectActionPerformed
        try {
            String id = userSelect.getSelectedItem().toString();
            if (! id.equals(userSelect.getItemAt(0))) {
                txtId6.setText(id);
                txtId7.setText(id);
            }
            else{
                txtId6.setText("");
                txtId7.setText("");
            }
            update();
        } catch (Exception e) {
        }
    }//GEN-LAST:event_userSelectActionPerformed

    private void txtAirportIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAirportIdActionPerformed
        
    }//GEN-LAST:event_txtAirportIdActionPerformed

    private void txtId6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtId6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtId6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> DAY;
    private javax.swing.JComboBox<String> DAY1;
    private javax.swing.JComboBox<String> DAY2;
    private javax.swing.JComboBox<String> DAY3;
    private javax.swing.JComboBox<String> DAY4;
    private javax.swing.JComboBox<String> DAY5;
    private javax.swing.JComboBox<String> MONTH;
    private javax.swing.JComboBox<String> MONTH1;
    private javax.swing.JComboBox<String> MONTH2;
    private javax.swing.JComboBox<String> MONTH3;
    private javax.swing.JComboBox<String> MONTH4;
    private javax.swing.JComboBox<String> MONTH5;
    private javax.swing.JRadioButton administrator;
    private javax.swing.JButton btnAddFlight;
    private javax.swing.JButton btnCreateAirport;
    private javax.swing.JButton btnCreateFlight;
    private javax.swing.JButton btnCreatePlane;
    private javax.swing.JButton btnDelay;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnRefreshFlights;
    private javax.swing.JButton btnRefreshLocations;
    private javax.swing.JButton btnRefreshMyFlights;
    private javax.swing.JButton btnRefreshPassengers;
    private javax.swing.JButton btnRefreshPlanes;
    private javax.swing.JButton btnRegister;
    private javax.swing.JButton btnUpdateInfo;
    private javax.swing.JComboBox<String> cbFlight;
    private javax.swing.JComboBox<String> cbHour;
    private javax.swing.JComboBox<String> cbId;
    private javax.swing.JComboBox<String> cbLocation51;
    private javax.swing.JComboBox<String> cbLocation52;
    private javax.swing.JComboBox<String> cbLocation53;
    private javax.swing.JComboBox<String> cbMinute;
    private javax.swing.JComboBox<String> cbPlane;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAddSymbol;
    private javax.swing.JLabel lblAddSymbol5;
    private javax.swing.JLabel lblAirline;
    private javax.swing.JLabel lblAirportCity;
    private javax.swing.JLabel lblAirportLatit;
    private javax.swing.JLabel lblAirportLong;
    private javax.swing.JLabel lblAirpotCountry;
    private javax.swing.JLabel lblAirpotId;
    private javax.swing.JLabel lblAirpotName;
    private javax.swing.JLabel lblArrivalLocation;
    private javax.swing.JLabel lblBirthdate6;
    private javax.swing.JLabel lblBirthday;
    private javax.swing.JLabel lblBrand;
    private javax.swing.JLabel lblCountry;
    private javax.swing.JLabel lblCountry6;
    private javax.swing.JLabel lblDepartureDate;
    private javax.swing.JLabel lblDepartureLocation;
    private javax.swing.JLabel lblDuration51;
    private javax.swing.JLabel lblDuration52;
    private javax.swing.JLabel lblFight;
    private javax.swing.JLabel lblFirstName;
    private javax.swing.JLabel lblFirstName6;
    private javax.swing.JLabel lblHours;
    private javax.swing.JLabel lblI6;
    private javax.swing.JLabel lblId13;
    private javax.swing.JLabel lblId2;
    private javax.swing.JLabel lblId3;
    private javax.swing.JLabel lblId5;
    private javax.swing.JLabel lblId7;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblLastName6;
    private javax.swing.JLabel lblMaxCap;
    private javax.swing.JLabel lblMinutes;
    private javax.swing.JLabel lblModel;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblPhone6;
    private javax.swing.JLabel lblPlane;
    private javax.swing.JLabel lblScaleLocation;
    private javax.swing.JLabel lblSeparator21;
    private javax.swing.JLabel lblSeparator22;
    private javax.swing.JLabel lblSeparator23;
    private javax.swing.JLabel lblSeparator51;
    private javax.swing.JLabel lblSeparator52;
    private javax.swing.JLabel lblSeparator53;
    private javax.swing.JLabel lblSeparator54;
    private javax.swing.JLabel lblSeparator55;
    private javax.swing.JLabel lblSeparator56;
    private javax.swing.JLabel lblSeparator61;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel10;
    private javax.swing.JPanel panel11;
    private javax.swing.JPanel panel12;
    private javax.swing.JPanel panel13;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel panel3;
    private javax.swing.JPanel panel4;
    private javax.swing.JPanel panel5;
    private javax.swing.JPanel panel6;
    private javax.swing.JPanel panel7;
    private javax.swing.JPanel panel8;
    private javax.swing.JPanel panel9;
    private core.view.PanelRound panelRound1;
    private core.view.PanelRound panelRound2;
    private core.view.PanelRound panelRound3;
    private javax.swing.JScrollPane scrollPane10;
    private javax.swing.JScrollPane scrollPane11;
    private javax.swing.JScrollPane scrollPane12;
    private javax.swing.JScrollPane scrollPane8;
    private javax.swing.JScrollPane scrollPane9;
    private javax.swing.JTable table10;
    private javax.swing.JTable table11;
    private javax.swing.JTable table12;
    private javax.swing.JTable table8;
    private javax.swing.JTable table9;
    private javax.swing.JTextField txtAirline;
    private javax.swing.JTextField txtAirportCity;
    private javax.swing.JTextField txtAirportCountry;
    private javax.swing.JTextField txtAirportId;
    private javax.swing.JTextField txtAirportLatid;
    private javax.swing.JTextField txtAirportLong;
    private javax.swing.JTextField txtAirportName;
    private javax.swing.JTextField txtBirthdate;
    private javax.swing.JTextField txtBirthdate6;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtCountry6;
    private javax.swing.JTextField txtDepartureDate;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtFirstName6;
    private javax.swing.JTextField txtId2;
    private javax.swing.JTextField txtId3;
    private javax.swing.JTextField txtId5;
    private javax.swing.JTextField txtId6;
    private javax.swing.JTextField txtId7;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtLastName6;
    private javax.swing.JTextField txtMaxCap;
    private javax.swing.JTextField txtModel;
    private javax.swing.JTextField txtPhoneNumber;
    private javax.swing.JTextField txtPhoneNumber6;
    private javax.swing.JTextField txtPrefix;
    private javax.swing.JTextField txtPrefix6;
    private javax.swing.JRadioButton user;
    private javax.swing.JComboBox<String> userSelect;
    // End of variables declaration//GEN-END:variables
}
