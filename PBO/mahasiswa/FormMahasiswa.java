package mahasiswa;

import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class FormMahasiswa extends JFrame {
    private String [] judul = {"NIM", "Nama","Alamat"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblnama = new  JLabel("Nama");
    JTextField txnama = new JTextField(20);
    JLabel lblnim = new JLabel("NIM");
    JTextField txnim = new JTextField(10);
    JLabel lblalamat = new JLabel("Alamat");
    JTextArea alamat = new JTextArea();
    JScrollPane sca = new JScrollPane(alamat);
    JButton btadd = new  JButton("Simpan");
    JButton btnew = new JButton ("Baru");
    JButton btdel = new JButton ("Hapus");
    JButton btedit = new JButton ("Ubah");
    
    FormMahasiswa(){
        super("Data Mahasiswa");
        setSize(460, 300);
        pnl.setLayout(null);
        pnl.add(lblnim);
        lblnim.setBounds(20, 10, 80, 20);
        pnl.add(txnim);
        txnim.setBounds(105, 10, 100, 20);
        pnl.add(lblnama);
        lblnama.setBounds(20, 33, 80, 20);
        pnl.add(txnama);
        txnama.setBounds(105, 33, 175, 20);
        pnl.add(lblalamat);
        lblalamat.setBounds(20, 56, 80, 20);
        pnl.add(sca);
        sca.setBounds(105, 56, 175, 45);
        
        pnl.add(btnew);
        btnew.setBounds(300, 10, 125, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
            btnewAksi(e);
        }
    });
        pnl.add(btadd);
        btadd.setBounds(300, 33, 125, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            btaddAksi(e);
            }
        });
    pnl.add(btedit);
    btedit.setBounds(300, 56, 125, 20);
    btedit.setEnabled(false);
    btedit.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e){
            bteditAksi(e);
    }
  });
  pnl.add(btdel);
  btdel.setBounds(300, 79, 125, 20);
  btdel.setEnabled(false);
  btdel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
              btdelAksi(e);
      }
  });
  df = new DefaultTableModel(null, judul);
  tab.setModel(df);
  scp.getViewport().add(tab);
  tab.setEnabled(true);
  tab.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
          tabMouseClicked(evt);
      }
  });
  scp.setBounds(20, 110, 405, 130);
  pnl.add(scp);
  getContentPane().add(pnl);
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setVisible(true);
}
    
void loadData(){
    try{
        Connection cn = new ConnecDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "SELECT * FROM mahasiswa";
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()){
            String nim, nama, alamat;
            nim = rs.getString("NIM");
            nama = rs.getString("Nama");
            alamat = rs.getString("Alamat");
            String [] data = {nim, nama, alamat};
            df.addRow(data);
        }
        rs.close();
        cn.close();
    }  catch (SQLException ex) {
        ex.printStackTrace();
    }
}
void clearTable(){
    int numRow = df.getRowCount();
    for(int i=0; i<numRow; i++) {
        df.removeRow(0);
    }
}
void clearTextField(){
    txnim.setText(null);
    txnama.setText(null);
    alamat.setText(null);
}

void simpanData(Mahasiswa M){
    try{
        Connection cn = new ConnecDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "INSERT INTO mahasiswa (NIM, Nama, Alamat) " +
                "VALUES ('" + M.getNIM()+ "', '" + M.getNama() + "', '" + M.getAlamat() +"')";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                "Info Proses", JOptionPane.INFORMATION_MESSAGE);
        String [] data = {M.getNIM(), M.getNama(), M.getAlamat()};
        df.addRow(data);
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
void hapusData(String nim){
    try{
        Connection cn = new ConnecDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "DELETE FROM mahasiswa WHERE NIM = '"+nim+"'";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus", "Info Proses",
                JOptionPane.INFORMATION_MESSAGE);
        df.removeRow(tab.getSelectedRow());
        clearTextField();
    } catch (SQLException ex){
        ex.printStackTrace();
    }
}

void ubahData(Mahasiswa M, String nim) {
    try{
        Connection cn = new ConnecDB().getConnect();
        Statement st = cn.createStatement();
        String sql = "UPDATE mahasiswa SET NIM='"+  M.getNIM()+"', Nama ='" + M.getNama()+"', Alamat='" + M.getAlamat()+"' WHERE NIM='" +nim+"'";
        int result = st.executeUpdate(sql);
        cn.close();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diubah","Info Proses",
                JOptionPane.INFORMATION_MESSAGE);
        clearTable();
        loadData();
    } catch (SQLException ex){
        ex.printStackTrace();
    }
}
private void btnewAksi(ActionEvent evt){
    txnim.setText(null);
    txnama.setText(null);
    alamat.setText(null);
    btedit.setEnabled(false);
    btdel.setEnabled(false);
    btadd.setEnabled(true);
}
private void btaddAksi(ActionEvent evt){
    Mahasiswa M = new Mahasiswa();
    M.setNIM(txnim.getText());
    M.setNama(txnama.getText());
    M.setAlamat(alamat.getText());
    simpanData(M);
}

private void btdelAksi(ActionEvent evt){
    int status;
    status = JOptionPane.showConfirmDialog(null,"Yakin data akan dihapus?",
            "Konfirmasi",JOptionPane.OK_CANCEL_OPTION);
    if(status == 0){
        hapusData(txnim.getText());
    }
}
private void bteditAksi(ActionEvent evt){
    Mahasiswa M = new Mahasiswa();
    M.setNIM(txnim.getText());
    M.setNama(txnama.getText());
    M.setAlamat(alamat.getText());
    ubahData(M, tab.getValueAt(tab.getSelectedRow(),0).toString());
}
private void tabMouseClicked(MouseEvent evt){
    btedit.setEnabled(true);
    btdel.setEnabled(true);
    btadd.setEnabled(false);
    String nim, nama, alt;
    nim = tab.getValueAt(tab.getSelectedRow(), 0).toString();
    nama = tab.getValueAt(tab.getSelectedRow(), 1).toString();
    alt = tab.getValueAt(tab.getSelectedRow(), 2).toString();
    txnim.setText(nim);
    txnama.setText(nama);
    alamat.setText(alt);
}

public static void main(String [] args) {
    new FormMahasiswa().loadData();
}
}
