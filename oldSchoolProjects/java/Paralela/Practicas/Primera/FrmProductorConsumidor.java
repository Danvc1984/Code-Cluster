import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.table.DefaultTableModel;

public class FrmProductorConsumidor extends javax.swing.JFrame {

    
    public FrmProductorConsumidor() {
        
        productores = new LinkedList<>();
        consumidores = new LinkedList<>();
        
        ocupados = new LinkedList<>();
        lock = new ReentrantLock();
        almacenLleno = lock.newCondition();
        almacenVacio = lock.newCondition();
        
        initComponents();
    }
    
    Planificador planificador;
    Thread hiloPlanificador;
    int orden;
    final LinkedList<Proceso> productores;
    final LinkedList<Proceso> consumidores;
    
    public final ReentrantLock lock;
    public final Condition almacenLleno;
    public final Condition almacenVacio;
    public final LinkedList<String> ocupados;
    public int capacidad;
    
    int productorSeleccionado;
    int consumidorSeleccionado;
    
    public void ActualizarTabla(boolean quien) {
        
        LinkedList<Proceso> trabajadores = quien ? productores : consumidores;
        DefaultTableModel modeloTabla = (DefaultTableModel)(
            quien ? tblProductores.getModel() : tblConsumidores.getModel()
        );
        
        synchronized(modeloTabla) {
            
            modeloTabla.setRowCount(0);
            for(int i = 0; i < trabajadores.size(); i++) {
                if(quien)
                    modeloTabla.addRow(new Object[] {
                        ((Productor)trabajadores.get(i)).pid,
                        trabajadores.get(i).info
                    });
                else
                    modeloTabla.addRow(new Object[] {
                        ((Consumidor)trabajadores.get(i)).pid,
                        trabajadores.get(i).info
                    });
            }
        }
    }
    
    public void ActualizarProductos() {
        
        DefaultTableModel modeloTabla = (DefaultTableModel)tblAlmacen.getModel();
        synchronized(modeloTabla) {
            modeloTabla.setRowCount(0);
            ocupados.forEach((paquete) -> {
                modeloTabla.addRow(new Object[] { paquete });
            });
            lblOcupados.setText(String.valueOf(ocupados.size()));
        }
    }

  
    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnAgregarProductor = new javax.swing.JButton();
        btnAgregarConsumidor = new javax.swing.JButton();
        lblOcupados = new javax.swing.JLabel();
        lblProductores = new javax.swing.JLabel();
        lblConsumidores = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblCapacidad = new javax.swing.JLabel();
        sldTiempoFuera = new javax.swing.JSlider();
        sldTiempoDentro = new javax.swing.JSlider();
        sldCapacidad = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblTiempoFuera = new javax.swing.JLabel();
        lblTiempoDentro = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProductores = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblConsumidores = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAlmacen = new javax.swing.JTable();
        btnQuitarProductor = new javax.swing.JButton();
        btnQuitarConsumidor = new javax.swing.JButton();
        sldQuantum = new javax.swing.JSlider();
        jLabel7 = new javax.swing.JLabel();
        lblQuantum = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Productor - Consumidor");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        btnAgregarProductor.setText("Agregar");
        btnAgregarProductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductorActionPerformed(evt);
            }
        });

        btnAgregarConsumidor.setText("Agregar");
        btnAgregarConsumidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarConsumidorActionPerformed(evt);
            }
        });

        lblOcupados.setText("0");
        lblOcupados.setToolTipText("");

        lblProductores.setText("0");
        lblProductores.setToolTipText("");

        lblConsumidores.setText("0");

        jLabel1.setText("Ocupados:");

        jLabel2.setText("Capacidad:");

        lblCapacidad.setText("0");

        sldTiempoFuera.setMaximum(5000);
        sldTiempoFuera.setValue(1000);
        sldTiempoFuera.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldTiempoFueraStateChanged(evt);
            }
        });

        sldTiempoDentro.setMaximum(5000);
        sldTiempoDentro.setValue(1000);
        sldTiempoDentro.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldTiempoDentroStateChanged(evt);
            }
        });

        sldCapacidad.setMaximum(30);
        sldCapacidad.setValue(10);
        sldCapacidad.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldCapacidadStateChanged(evt);
            }
        });

        jLabel3.setText("Tiempo fuera del almacen (seg): ");

        jLabel4.setText("Tiempo dentro del almacen (seg): ");

        lblTiempoFuera.setText("0");

        lblTiempoDentro.setText("0");

        tblProductores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProductores);

        tblConsumidores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblConsumidores);

        jLabel5.setText("Productores:");

        jLabel6.setText("Consumidores:");

        tblAlmacen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Paquete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tblAlmacen);

        btnQuitarProductor.setText("Quitar");
        btnQuitarProductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarProductorActionPerformed(evt);
            }
        });

        btnQuitarConsumidor.setText("Quitar");
        btnQuitarConsumidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarConsumidorActionPerformed(evt);
            }
        });

        sldQuantum.setMaximum(5000);
        sldQuantum.setMinimum(50);
        sldQuantum.setToolTipText("");
        sldQuantum.setValue(100);
        sldQuantum.setVisible(false);
        sldQuantum.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sldQuantumStateChanged(evt);
            }
        });

        jLabel7.setText("");

        lblQuantum.setText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTiempoDentro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sldTiempoDentro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblOcupados)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblCapacidad)
                                .addGap(18, 18, 18)
                                .addComponent(sldCapacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblProductores)
                                .addGap(132, 132, 132)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblConsumidores))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAgregarProductor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnQuitarProductor)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(btnAgregarConsumidor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnQuitarConsumidor))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTiempoFuera))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(lblQuantum)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sldTiempoFuera, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sldQuantum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(lblQuantum))
                    .addComponent(sldQuantum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(lblTiempoFuera))
                    .addComponent(sldTiempoFuera, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(lblTiempoDentro))
                    .addComponent(sldTiempoDentro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(lblProductores)
                        .addComponent(lblConsumidores)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregarProductor)
                        .addComponent(btnQuitarProductor))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregarConsumidor)
                        .addComponent(btnQuitarConsumidor)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblOcupados)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2)
                        .addComponent(lblCapacidad))
                    .addComponent(sldCapacidad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        
        orden = 0;
        productorSeleccionado = -1;
        consumidorSeleccionado = -1;
        
        tblProductores.getSelectionModel().addListSelectionListener(e -> {
            productorSeleccionado = e.getFirstIndex();
        });
        
        tblConsumidores.getSelectionModel().addListSelectionListener(e -> {
            consumidorSeleccionado = e.getFirstIndex();
        });
        
        capacidad = sldCapacidad.getValue();
        planificador = new Planificador();
        hiloPlanificador = new Thread(planificador);
        hiloPlanificador.start();
        
        lblCapacidad.setText(String.valueOf(sldCapacidad.getValue()));
        lblTiempoDentro.setText(String.valueOf(sldTiempoDentro.getValue() / 1000d));
        lblTiempoFuera.setText(String.valueOf(sldTiempoFuera.getValue() / 1000d));
        // lblQuantum.setText(String.valueOf(sldQuantum.getValue() / 1000d));
    }

    private void btnAgregarProductorActionPerformed(java.awt.event.ActionEvent evt) {
        
        Productor productor = new Productor(
            this,
            ++orden,
            sldQuantum.getValue(),
            sldTiempoDentro.getValue(),
            sldTiempoFuera.getValue()
        );
        productores.add(productor);
        planificador.agregarProceso(productor);
        ActualizarTabla(true);
    }
    private void btnAgregarConsumidorActionPerformed(java.awt.event.ActionEvent evt) {
        Consumidor consumidor = new Consumidor(
            this,
            ++orden,
            sldQuantum.getValue(),
            sldTiempoDentro.getValue(),
            sldTiempoFuera.getValue()
        );
        consumidores.add(consumidor);
        planificador.agregarProceso(consumidor);
        ActualizarTabla(false);
    }
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        try {
            hiloPlanificador.interrupt();
            hiloPlanificador.join();
        } catch(InterruptedException ex) {
            System.err.println("ERROR");
        }
    }
    private void sldCapacidadStateChanged(javax.swing.event.ChangeEvent evt) {
        capacidad = sldCapacidad.getValue();
        lblCapacidad.setText(String.valueOf(capacidad));
    }

    private void sldTiempoFueraStateChanged(javax.swing.event.ChangeEvent evt) {
        int val = sldTiempoFuera.getValue();
        lblTiempoFuera.setText(String.valueOf(val / 1000d));
    }

    private void sldTiempoDentroStateChanged(javax.swing.event.ChangeEvent evt) {
        int val = sldTiempoDentro.getValue();
        lblTiempoDentro.setText(String.valueOf(val / 1000d));
    }
    private void btnQuitarProductorActionPerformed(java.awt.event.ActionEvent evt) {
        if(productorSeleccionado < 0) return;
        DefaultTableModel modeloTabla = (DefaultTableModel)tblProductores.getModel();
        synchronized(modeloTabla) {
            
            modeloTabla.removeRow(productorSeleccionado);
            productores.get(productorSeleccionado).interrupt();
            productores.remove(productorSeleccionado);
            productorSeleccionado = -1;
        }
    }
    private void btnQuitarConsumidorActionPerformed(java.awt.event.ActionEvent evt) {
        if(consumidorSeleccionado < 0) return;
        DefaultTableModel modeloTabla = (DefaultTableModel)tblConsumidores.getModel();
        synchronized(modeloTabla) {
            
            modeloTabla.removeRow(consumidorSeleccionado);
            consumidores.get(consumidorSeleccionado).interrupt();
            consumidores.remove(consumidorSeleccionado);
            consumidorSeleccionado = -1;
        }
    }
    private void sldQuantumStateChanged(javax.swing.event.ChangeEvent evt) {//
        int val = sldQuantum.getValue();
        // lblQuantum.setText(String.valueOf(val / 1000d));
    }
    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmProductorConsumidor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
      
        java.awt.EventQueue.invokeLater(() -> {
            new FrmProductorConsumidor().setVisible(true);
        });
    }

   
    private javax.swing.JButton btnAgregarConsumidor;
    private javax.swing.JButton btnAgregarProductor;
    private javax.swing.JButton btnQuitarConsumidor;
    private javax.swing.JButton btnQuitarProductor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblCapacidad;
    private javax.swing.JLabel lblConsumidores;
    private javax.swing.JLabel lblOcupados;
    private javax.swing.JLabel lblProductores;
    private javax.swing.JLabel lblQuantum;
    private javax.swing.JLabel lblTiempoDentro;
    private javax.swing.JLabel lblTiempoFuera;
    private javax.swing.JSlider sldCapacidad;
    private javax.swing.JSlider sldQuantum;
    private javax.swing.JSlider sldTiempoDentro;
    private javax.swing.JSlider sldTiempoFuera;
    private javax.swing.JTable tblAlmacen;
    private javax.swing.JTable tblConsumidores;
    private javax.swing.JTable tblProductores;
}
