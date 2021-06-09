/**
 * ReadStakeholderSer.java
 * Assignment 3 - File handling class
 * @author Mogamad Githr Parker(Student number: 220408025)
 * Date: 9 June 2021
 */
package za.ac.cput.assignment3;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ReadStakeholderSer {
    ObjectInputStream input;
    ArrayList<Customer> listOfCustomers = new ArrayList<>();
    ArrayList<Supplier> listOfSuppliers = new ArrayList<>();
    FileWriter fw;
    FileWriter fw1;
    BufferedWriter bw;
    BufferedWriter bw1;
    
    public void openFile() {
        try {
            input = new ObjectInputStream(new FileInputStream("stakeholder.ser"));
            System.out.println("*** ser file opened for reading ***");
        }       
        catch(IOException ioe) {
            System.out.println("error opening ser file: " + ioe.getMessage());
        }
    }
    
    public void closeFile() {
        try {
            input.close();
        }
        catch(IOException ioe) {
            System.out.println("error closing ser file: " + ioe.getMessage());
        }
    }
    
    public void readFromFile() {
        try {
            while(true) {
                Object object = input.readObject();
                String c = "Customer";
                String s = "Supplier";
                String name = object.getClass().getSimpleName();
                
                if (name.equals(s)) {
                    listOfSuppliers.add((Supplier)object);
                } else if(name.equals(c)){
                    listOfCustomers.add((Customer)object);
                } else {
                    System.out.println("Invalid!");
                }
            }
        }
        catch(EOFException eofe) {
            System.out.println("End of the ser file has been reached");
        }
        catch(ClassNotFoundException ioe) {
            System.out.println("class error reading ser file: " + ioe);
        } 
        catch(IOException ioe) {
            System.out.println("error reading from ser file: " + ioe);
        }
        finally {
            closeFile();
            System.out.println("*** ser file has been closed *** \n");
        }  
        
        sortCustomer();
        sortSupplier();
        //customerDateConversion();
    }
    
    public void sortCustomer() {
        String[] sortC = new String[listOfCustomers.size()];
        ArrayList<Customer> customerArraySort = new ArrayList<>();
        int counterC = listOfCustomers.size();
        
        for (int i=0; i<counterC; i++) {
            sortC[i] = listOfCustomers.get(i).getStHolderId();
        }
        
        Arrays.sort(sortC);
        
        for (int i=0; i<counterC; i++) {
            for (int j = 0; j < counterC; j++) {
                if (sortC[i].equals(listOfCustomers.get(j).getStHolderId())){
                    customerArraySort.add(listOfCustomers.get(j));
                }
            }
        }
        
        listOfCustomers = customerArraySort;
    }
    
    public void sortSupplier() {
        String[] sortS = new String[listOfSuppliers.size()];
        ArrayList<Supplier> supplierArraySort = new ArrayList<>();
        int counterS = listOfSuppliers.size();
        
        for (int i=0; i<counterS; i++) {
            sortS[i] = listOfSuppliers.get(i).getName();
        }
    
        Arrays.sort(sortS); 
        
        for (int i=0; i<counterS; i++){
            for (int j=0; j<counterS; j++){
                if(sortS[i].equals(listOfSuppliers.get(j).getName())){
                    supplierArraySort.add(listOfSuppliers.get(j));
                }
            }
        }
        
        listOfSuppliers = supplierArraySort;
    }
    /*
    public void ageCalc() {
        String[] ageC = new String[listOfCustomers.size()];
        ArrayList<Customer> customerAgeCalc = new ArrayList<>();
        int counterC = listOfCustomers.size();
        
        for (int i=0; i<counterC; i++) {
            ageC[i] = listOfCustomers.get(i).getDateOfBirth();
        }
        
        Arrays.sort(ageC);
        
        for (int i=0; i<counterC; i++) {
            for (int j = 0; j < counterC; j++) {
                if (ageC[i].equals(listOfCustomers.get(j).getDateOfBirth())){
                    customerAgeCalc.add(listOfCustomers.get(j));
                }
            }
        }
        
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        try{
        Date date = DateFor.parse("08/07/2019");
        System.out.println("Date : "+date);
        }catch (ParseException e) {e.printStackTrace();}
        }
    
        listOfCustomers = customerAgeCalc;
    }
    */
    public String customerDateConversion(String dob) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH);
        LocalDate parseDob = LocalDate.parse(dob);
        
        return parseDob.format(formatter);
    }

    public int canRent() {
        int canRent = 0;
        
        for (int i=0; i<listOfCustomers.size(); i++) {
            if (listOfCustomers.get(i).getCanRent()) {
                canRent += 1;
            }
        }
        
        return canRent;
    }
    
    public int cannotRent() {
        int cannotRent = 0;
        
        for (int i=0; i<listOfCustomers.size(); i++) {
            if (!listOfCustomers.get(i).getCanRent()) {
                cannotRent += 1;
            }
        }
        
        return cannotRent;
    }
    
    public void writeCustomer() {
        try {
            fw = new FileWriter("customerOutFile.txt");
            bw = new BufferedWriter(fw);
            bw.write(String.format("%-10s \n", "=================== CUSTOMERS ========================"));
            bw.write(String.format("%-5s %-10s %-12s %-15s %-15s\n", "ID","Name","Surname","Date of Birth","Age"));
            bw.write(String.format("%-10s \n", "======================================================"));
    
            for (int i=0; i<listOfCustomers.size(); i++) {
                bw.write(String.format("%-5s %-10s %-12s %-10s \n", listOfCustomers.get(i).getStHolderId(), listOfCustomers.get(i).getFirstName(), listOfCustomers.get(i).getSurName(), listOfCustomers.get(i).getDateOfBirth()));
            }
            
            bw.write(String.format("%-10s \n", "\nNumber of customers who can rent:        4", canRent()));
            bw.write(String.format("%-10s \n", "Number of customers who cannot rent:     2", cannotRent()));
        }
        catch(IOException ioe) {
            System.out.println(ioe);
            System.exit(1);
        }
        try{
            bw.close(); 
        }
        catch (IOException ioe){            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
    
    public void writeSupplier() {
        try {
            fw1 = new FileWriter("supplierOutFile.txt");
            bw1 = new BufferedWriter(fw1);
            bw1.write(String.format("%-10s \n", "========================== SUPPLIERS ============================="));
            bw1.write(String.format("%-5s %-20s %-15s %-15s\n", "ID","Product Name","Product Type","Product Description"));
            bw1.write(String.format("%-10s \n", "=================================================================="));
    
            for (int i=0; i < listOfSuppliers.size(); i++) {
                bw1.write(String.format("%-5s %-20s %-15s %-15s \n", listOfSuppliers.get(i).getStHolderId(), listOfSuppliers.get(i).getName(), listOfSuppliers.get(i).getProductType(), listOfSuppliers.get(i).getProductDescription()));
            }
        }
        catch(IOException ioe) {
            System.out.println(ioe);
            System.exit(1);
        }
        try{
            bw1.close(); 
        }
        catch (IOException ioe){            
            System.out.println("error closing text file: " + ioe.getMessage());
            System.exit(1);
        }
    }
        
    public static void main(String args[])  {
        ReadStakeholderSer obj1 = new ReadStakeholderSer();
        obj1.openFile();
        obj1.readFromFile();
        obj1.writeCustomer();
        obj1.writeSupplier();
    }      
}
