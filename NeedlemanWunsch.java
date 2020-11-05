/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package needlemanwunsch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 *
 * @author Kalliopi
 */
public class NeedlemanWunsch {

    public static void main(String[] args) {
        
        String test,optimal;
        String[] ps;
        Path p = new Path();
        MakeGrid grid = new MakeGrid();
        grid.printGrid();
        test = grid.optimal();
        grid.pathFinder();
        ps = grid.secondPaths();
        optimal = p.findOutput(test);
        try{
            File output = new File("results.txt");
            FileOutputStream fos = new FileOutputStream(output);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            
            bw.write("\t\tNeedleman-Wunsch Results\n");
            bw.write("\t\t===========================\n\n");
            bw.write("Best Alignment:\n================\n"+optimal+"\n");
            bw.newLine();
            for(int l =0;l<ps.length;l++){
                if(ps[l]!=null){
                    String path = p.findOutput(ps[l]);
                    bw.write("\nAlignment:\n===========\n"+path+"\n");
                }
            }
            bw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    
   
    
}
