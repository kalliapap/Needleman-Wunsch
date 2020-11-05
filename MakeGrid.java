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
import java.util.Vector;

/**
 * @author Kalliopi
 */
public class MakeGrid {
    
    int[][] grid; 
    Parameters p = new Parameters();
    String rowSeq, colSeq;
    Vector<Path> optPaths = new Vector<>(); 
        
    /**
     * Returns the character at index i in a given sequence.
     * 
     * @param  i location of character in the sequence
     * @param  seq the sequence to take char
     * @return the character of sequence at i
     */
    public char getAmino(int i, String seq){
        return seq.charAt(i);
    }
    
    /**
     * Prints the two sequences and the grid.
     */
    public void printGrid(){
        ParseFiles parser = new ParseFiles();
        Protein[] prot = new Protein[2];
        prot = parser.readFiles();
        
        System.out.println("First Sequence -> Header: "+prot[0].getHeader()+"\nSequence: "+prot[0].getSequence());
        System.out.println("Second Sequence -> Header: "+prot[1].getHeader()+"\nSequence: "+prot[1].getSequence());
        rowSeq = prot[0].getSequence();
        colSeq = prot[1].getSequence();
        grid = new int[rowSeq.length()][colSeq.length()];
        makeGrid( rowSeq,  colSeq);
        for(int row=0;row<rowSeq.length();row++){
            for(int col=0;col<colSeq.length();col++){
                System.out.print(grid[row][col]+"\t");
            }
            System.out.println();
        }        
    }
    
    /**
     * Initializes the grid.
     */
    public void initGrid(){
        for(int row=0;row<rowSeq.length();row++){
            for(int col=0;col<colSeq.length();col++){
                grid[row][col] = 0;
            }
        }
    }
    
    /**
     * Constructs the grid for the two sequences using
     * a scoring system.
     * 
     * @param rowSeq the first sequence to be aligned
     * @param colSeq the second sequence to be aligned
     */
    public void makeGrid(String rowSeq, String colSeq){
        System.out.println("make grid...");
        int left, top, topLeft;
        int d = -8;
        initGrid();
        for(int row=0;row<rowSeq.length();row++){
            grid[row][0] = row*d;
            for(int col=0;col<colSeq.length();col++){
                grid[0][col] = col*d;
            }
        }
        for(int row=1;row<rowSeq.length();row++){
            char first = rowSeq.charAt(row);
            for(int col=1;col<colSeq.length();col++){
                char second = colSeq.charAt(col);
                int score = p.getScore(first, second);
                //calculation of neighbors score
                left = grid[row][col-1] + d;
                top = grid[row-1][col] + d;
                topLeft = grid[row-1][col-1] + score;
                //epilogh tou megaluterou
                grid[row][col] = Math.max(left, Math.max(top, topLeft));
            }
        }        
    }
    
    /**
     * Finds the optimal alignment.
     * @return a string with the optimal alignments
     */
    public String optimal(){
        String rowAlign = "";
        String colAlign = "";
        int i = rowSeq.length()-1, j = colSeq.length()-1;
        int bestScore = 0;
        while(i>0 || j>0){
            //if match alignes the two characters
            if(i>0 && j>0 && grid[i][j] == grid[i-1][j-1]+p.getScore(rowSeq.charAt(i), colSeq.charAt(j))){
                bestScore+= p.getScore(rowSeq.charAt(i), colSeq.charAt(j));
                rowAlign += getAmino(i,rowSeq);
                colAlign += getAmino(j,colSeq);
                i--;
                j--;
            //gap insertion in the second sequence
            }else if(i > 0 && grid[i][j] == (grid[i-1][j]-8)){
                bestScore-=8;
                rowAlign += getAmino(i,rowSeq);
                colAlign += "-";
                i--;
            //gap insertion in the first sequence
            }else{
                bestScore-=8;
                rowAlign += "-";
                colAlign += getAmino(j,colSeq);
                j--;
            }
        }
                
        rowAlign = new StringBuffer(rowAlign).reverse().toString();
        colAlign = new StringBuffer(colAlign).reverse().toString();
        //save the two final aligned sequences in a string separated 
        //with \n and return that string so that i can then write it
        //in the results file
        String out="";
        out += rowAlign+"\n"+colAlign+"\n"+"Score: "+bestScore+"\n";
        
        System.out.println("BEST ALIGNMENT\n--------------");
        System.out.println(rowAlign);
        System.out.println(colAlign);
        System.out.println("Score: "+bestScore);
        return out;
    }
    
    /**
     * Finds if there are more than one paths possible. Keeps the node,
     * the alignment, the score until this point in order to return.
     */
    public void pathFinder(){
        String rowAlign = "";
        String colAlign = "";
        int i = rowSeq.length()-1, j = colSeq.length()-1;
        int bestScore = 0;
        String direction="";
         while(i>0 || j>0){
            //elegxos gia uparksh diakladwshs
            if(i>0 && j>0 && grid[i][j] == grid[i-1][j-1]+p.getScore(rowSeq.charAt(i), colSeq.charAt(j))
                    && (grid[i][j] == (grid[i-1][j]-8) || grid[i][j] == (grid[i][j-1]-8))){
                //an uparxei diakladwsh apothikeusi twn stoixeiwn pou xreiazontai 
                //direction -> h kateuthunsh pou prepei na paei otan epistrepsei gia 
                //epomeno monopati se auto to shmeio
                if(grid[i][j] == (grid[i-1][j]-8))
                    direction = "left";
                else 
                    direction="top";
                //vector me ta shmeia diakladwshs 
                optPaths.add(new Path(direction,rowAlign,colAlign,i,j,bestScore));                         
            }
            if(i>0 && j>0 && grid[i][j] == grid[i-1][j-1]+p.getScore(rowSeq.charAt(i), colSeq.charAt(j))){
                bestScore+= p.getScore(rowSeq.charAt(i), colSeq.charAt(j));
                rowAlign += getAmino(i,rowSeq);
                colAlign += getAmino(j,colSeq);
                i--;
                j--;                
            }else if(i > 0 && grid[i][j] == (grid[i-1][j]-8)){
                bestScore-=8;
                rowAlign += getAmino(i,rowSeq);
                colAlign += "-";
                i--;
            }else{
                bestScore-=8;
                rowAlign += "-";
                colAlign += getAmino(j,colSeq);
                j--;
            }
        }                
    }
    
    /**
     * Finds other optimal paths.
     * @return 
     */
    public String[] secondPaths(){
        String paths[] = new String[10];
        int CounterOfPaths = 2;
        int pathsCounter=0;
        String out = "",ret="";
       // System.out.println("bika secondPaths"+optPaths.size());
        for(Path tmpPath : optPaths){ //gia oles tis diakladwseis pou vriskontai sto optPaths
            String rowAlign = tmpPath.getRowAlign();
            String colAlign = tmpPath.getColAlign();
            int i = tmpPath.getI(),j = tmpPath.getJ();
            int bestScore = tmpPath.getScore();
            out = "";
            while(i>0 || j>0){
                //gap in the first sequence
                if(j > 0 && grid[i][j] == (grid[i][j-1]-8)){
                    bestScore-=8;
                    rowAlign += "-";
                    colAlign += getAmino(j,colSeq);
                    j--;
                }
                //gap in the second sequence
                else if(i > 0 && grid[i][j] == (grid[i-1][j]-8)){
                    bestScore-=8;
                    rowAlign += getAmino(i,rowSeq);
                    colAlign += "-";
                    i--;
                //match alignes two sequences
                }else{
                    bestScore+= p.getScore(rowSeq.charAt(i), colSeq.charAt(j));
                    rowAlign += getAmino(i,rowSeq);
                    colAlign += getAmino(j,colSeq);
                    i--;
                    j--;
                }
            }
            rowAlign = new StringBuffer(rowAlign).reverse().toString();
            colAlign = new StringBuffer(colAlign).reverse().toString();
            out += rowAlign+"\n"+colAlign+"\n"+"Score: "+bestScore+"\n";
            System.out.println("ALIGNMENT: "+CounterOfPaths);
            System.out.println(rowAlign);
            System.out.println(colAlign);
            System.out.println("and Score: "+bestScore);
            CounterOfPaths++;
            
            if(pathsCounter < 5){
                paths[pathsCounter] = out;
                pathsCounter++;
            }
           
            
        }  
       return paths;
    }
}
