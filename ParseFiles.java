/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package needlemanwunsch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.lang.System.exit;
import java.util.Scanner;

/**
 *
 * @author Kalliopi
 */
public class ParseFiles {
    /**
     * User input to choose format. 
     * Gives 1 for fasta 2 for uniprot.
     * 
     * @return 1 or 2 
    */
    public int fileFormat(){
        System.out.println("Press 1 for FASTA\nPress 2 for UniProt");
        Scanner scan = new Scanner(System.in);
        int x = scan.nextInt();
        while( x != 1 && x != 2){
            System.out.println("Please give a correct option!");
            x = scan.nextInt();
        }
        if(x == 1){
            System.out.println("Fasta format selected!");
            
        }else if(x == 2){
            System.out.println("Uniprot format selected!");
        }
            
        return x;
    }
    
    /**
     * Asks from user to insert the filenames for the sequences
     * and calls readFastaFile() or readUniprotFile() for each 
     * file
     * 
     * @return sequences and headers
     * @see fileFormat()
     * @see readFastaFile(File f)
     * @see readUniprotFile(File f)
     */
    public Protein[] readFiles(){
        int format = fileFormat();
        String file1, file2;
        Protein[] seq = new Protein[2];
        System.out.println("Please give the filename for the first sequence");
        Scanner scan = new Scanner(System.in);
        file1 = scan.nextLine();
        File f1 = new File(file1);
        System.out.println("Please give the filename for the second sequence");
        file2 = scan.nextLine();
        File f2 = new File(file2);
        if(format == 1){
            seq[0] = readFastaFile(f1);
            seq[1] = readFastaFile(f2);
        }else{
            seq[0] = readUniprotFile(f1);
            seq[1] = readUniprotFile(f2);
        }
        return seq;
    }
   
    /**
    * Reads a fasta format file and separates header and sequence.
    * 
    * @param fastaFile file to get sequence  
    * @return   sequence and header from the input file in Protein object
    * @see Protein
    */
    public Protein readFastaFile(File fastaFile){
        InputStream fasta;
        String line;
        Protein p = new Protein();
        try {
            fasta = new FileInputStream(fastaFile);
            InputStreamReader inputStream = new InputStreamReader(fasta);
            BufferedReader buff = new BufferedReader(inputStream);
            int lineNb = 0;
            String s = "*";
            while ((line = buff.readLine()) != null){
                if(!line.startsWith(">", 0) && lineNb == 0){
                    System.out.println("File is not in FASTA format");
                    exit(-1);
                }
                if(line.startsWith(">", 0)){
                    if (lineNb == 0) {
                        p.setHeader(line); 
                    }
                }else{
                    s+=line;
                }
                lineNb++;
            }
            p.setSequence(s); 
            buff.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
        return p;
    }
    
    /**
     * Reads a Uniprot file. Keeps AC number and sequence.
     * Removes spaces from the sequence per 10 amino acids(uniprot format)
     * 
     * @param uniFile file in uniprot format to get sequence
     * @return AC number and sequence in a Protein object 
     * @see Protein
     */
    public Protein readUniprotFile(File uniFile){
        InputStream fasta;
        System.out.println("Start Reading");
        Protein p = new Protein();
        String tmp="*";
        boolean oneSeq = true, firstLine = true;
        try {
            fasta = new FileInputStream(uniFile);
            InputStreamReader inputStream = new InputStreamReader(fasta);
            BufferedReader buff = new BufferedReader(inputStream);
            String line;
            while((line = buff.readLine())!=null){
                if(line.startsWith("AC", 0) ){
                    if(oneSeq == false)
                        break;
                    System.out.println(line.subSequence(4, line.length()));
                    p.setHeader((String) line.subSequence(4, line.length())); 
                    System.out.println("" + p.getHeader());
                    oneSeq =false;
                }else if(line.startsWith("SQ", 0)){
                    while(!(line=buff.readLine()).equals("//") && !line.startsWith("//", 0)){
                        line = line.replaceAll("\\s+","");
                        tmp+=line;
                        p.setSequence(tmp); 
                    }
                }
            }
            System.out.println(p.getSequence());
            buff.close();  
        }
        catch (Exception e) {
            e.printStackTrace();
        } 
        return p;
    }
}
