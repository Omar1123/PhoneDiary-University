/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phoneagend;

import java.util.Scanner;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 *
 * @author root
 */
public class PhoneAgend {

    private FileOutputStream fileStream;    
    private File FILENAME = new File("/root/NetBeansProjects/PhoneAgend/src/phoneagend/AGENDA.txt");
    private DataOutputStream data;    
    
    private Scanner scanner = new Scanner(System.in);   
    private int option, finish = 1;
    
    private String name, number, searchValue, numberToDelete;
    private boolean decitions = true;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            PhoneAgend phone = new PhoneAgend();
            phone.run();
        } catch (Exception ex) {
            
        }
    }
        
    private void run() {
        
        System.out.println("Agenda.");  
        createDiary(FILENAME); // Se crea la agenda a partir de un metodo 
         
        agend();
    }
    
    private void agend() {
        while(finish == 1) {
            menu(); // Se visualiza el menu             
            option = scanner.nextInt(); // Se obtiene que accion desea realizar el usuario                
            options(option); // Se muestra que accion se realizara   
        }       
    }
    
    private void menu() {
        System.out.println("1....Nuevo contacto.");
        System.out.println("2....Consultar contactos.");
        System.out.println("3....Búsqueda de contacto."); 
        System.out.println("4.....Eliminación de contacto.");         
    }
    
    private void options(int options) {
        
        switch(options) {
            case 1:
                
                System.out.println("Nuevo contacto.");                
                
                System.out.println("Ingrese el nombre");
                name = scanner.next();
                
                System.out.println("Ingrese su numero");
                number = scanner.next();
                
                createContact(FILENAME,name, number);       
                
                finishAgend();
                
                break;
            case 2:
                
                System.out.println("Consultando la lista de contactos");
                
                getContacts(FILENAME);
                
                finishAgend();
                
                break;
            case 3:
                
                System.out.println("Búsqueda de contacto."); 
                
                searchContact();
                
                finishAgend();
                
                break;
            case 4:
                
                System.out.println("Eliminación de contacto.");         
                
                System.out.println("Ingrese el numero para eliminarlo");
                numberToDelete = scanner.next();
                
                deleteContact(FILENAME,numberToDelete);
                
                finishAgend();
                
                break;
        }        
    }
    
    private void createDiary(File file) {
        
        System.out.println("Creando el documento con la agenda");
        
        try {          
            if(!file.exists()){
                file.createNewFile();
            }          
        } catch (Exception ex) {            
            System.out.println(ex.getMessage());
        } 
    }
    
    private void createContact(File file,String name, String number) {        
        try {
          
            if(!file.exists()){  
                file.createNewFile();
            }
            
            BufferedWriter writter =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "utf-8"));
            
            writter.write(name + "\r\n");
            writter.write(number + "\r\n");
          
            writter.close();
        } catch (Exception ex) {
          //Captura un posible error le imprime en pantalla 
            System.out.println(ex.getMessage());
        }      
    }
    
    private void searchContact() {
        System.out.println("Ingrese el numero a buscar");
        
        searchValue = scanner.next();
        
        try{
            
            int index = 0;
            String name= "";            
            
            BufferedReader file = new BufferedReader(new FileReader(FILENAME));;
            String line = "";
            
            boolean search = false;
            boolean getNext= false;
            
            while((line = file.readLine())!= null && !getNext){
                index++;
                if(search){                    
                    getNext = true;
                }else if(line.indexOf(searchValue)!= -1){
                    search = true;
                    System.out.println("Este numero se ha encontrado : "+ line + "    En el indice " + index);
                }else{
                    name = line;
                } 
            }

            System.out.println("El contacto es: "+ name);
            
        }catch(IOException exception){
            System.out.println(exception);
        }
        
    }
    
    private void getContacts(File file) {
        try {
            
            if(file.exists()){
                
                BufferedReader buffer = new BufferedReader(new FileReader(file));                
                String lines;
                                
                while((lines = buffer.readLine())!=null) {                
                    System.out.println(lines);              
                }
                
                buffer.close();
              }else{
                System.out.println("Fichero No Existe");
              }
        } catch (Exception ex) {
            /*Captura un posible error y le imprime en pantalla*/ 
             System.out.println(ex.getMessage());
        }
    }
    
    private void deleteContact(File file,String number) {
        
        Random numaleatorio= new Random(3816L);         
        
        String renameFile= file.getParent()+"/auxiliar"+String.valueOf(Math.abs(numaleatorio.nextInt()))+".txt";        
        File newFile =new File(renameFile);
        
        try {
            
            if(file.exists()){
                
                BufferedReader Flee= new BufferedReader(new FileReader(file));
                String Slinea;
                
                while((Slinea=Flee.readLine())!=null) { 
                    if (!Slinea.toUpperCase().trim().equals(number.toUpperCase().trim())) {                       
                       migrateFile(newFile, Slinea);                               
                    }else{                        
                    }             
                }
                
                String SnomAntiguo = file.getName();                
                deleteFile(file);
                
                newFile.renameTo(file);                
                Flee.close();
            }else{
                System.out.println("Fichero No Existe");
            }
        } catch (Exception ex) {
             System.out.println(ex.getMessage());
        }
    }
    
    private void finishAgend() {
        System.out.println("Desea seguir usando la agenda(1:SI/0:No)");
        
        finish = scanner.nextInt();
        
        if(finish == 1) {
            agend();
        } else {
            System.out.println("Gracias por usar la agenda");
        }            
    }
    
    private void deleteFile(File file) {
        try {
            if(file.exists()){            
                file.delete(); 
                System.out.println("Fichero Borrado con Exito");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void migrateFile(File file, String chain) {
        try {
            
            if(!file.exists()){
                file.createNewFile();
            }
            
            BufferedWriter Fescribe=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true), "utf-8"));
            
            Fescribe.write(chain + "\r\n");            
            Fescribe.close();
        } catch (Exception ex) {            
            System.out.println(ex.getMessage());
        } 
    }
}
