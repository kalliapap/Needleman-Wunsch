/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package needlemanwunsch;

/**
 *
 * @author Kalliopi
 */
public class Path {
    private String direction,rowAlign,colAlign;
    private int i,j ,score;

    /**
     * A structure to keep the variables i need
     * to return in a node for a second path
     * 
     * @param direction left or top, where to go 
     * @param rowAlign  the alignment for the first sequence for the previous nodes 
     * @param colAlign the alignment for the second sequence for the previous nodes 
     * @param i index i for node to return
     * @param j index j for node to return
     * @param score for the alignment
     */
    public Path(String direction, String rowAlign, String colAlign, int i, int j, int score) {
        this.direction = direction;
        this.rowAlign = rowAlign;
        this.colAlign = colAlign;
        this.i = i;
        this.j = j;
        this.score = score;
    }

    public Path() {
    }

    /**
     *
     * @return direction left or top
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @return the alignment for the first sequence
     */
    public String getRowAlign() {
        return rowAlign;
    }

    /**
     *
     * @return the alignment for the second sequence
     */
    public String getColAlign() {
        return colAlign;
    }

    /**
     *
     * @return the index i of a node
     */
    public int getI() {
        return i;
    }

    /**
     *
     * @return the index j of a node
     */
    public int getJ() {
        return j;
    }

    /**
     *
     * @return alignment score
     */
    public int getScore() {
        return score;
    }   
    
    /**
     * Takes the whole alignment and separates every 60 amino acids save the 
     * final output format in a string and returns it.
     * 
     * @param out string with the two final aligned sequences separated with \n
     * @return the final alignment separated per 60 amino acids
     */
    public String findOutput(String out){
       int totalCount1,toRem=0;
       String[] seq = new String[3];
       for(int i = 0;i<seq.length;i++)
           seq[i] = "";
       String fin="",ret="";
       seq = out.split("\n");
       //upologismos se poses 60ades mporei na xwristei h stoixisi
        if(seq[0].length() >= seq[1].length()){
            if(seq[0].length()%60==0){
                totalCount1 = seq[0].length()/60;
            }else{
                totalCount1 = seq[0].length()/60+1;
            }
        }else{
            if(seq[1].length()%60==0){
                totalCount1 = seq[1].length()/60;
            }else{
                totalCount1 = seq[1].length()/60+1;
            }
        }
       int start = 0;
        for(int i=0;i<totalCount1;i++){
            fin="";
            //60ades gia thn prwth akolouthia stoixisis
            for(int j = start;j<(toRem+60);j++){
                if(j < seq[0].length()){
                    fin += seq[0].charAt(j);   
                }
            }
            fin += "\n";
            //60ades gia th deuterh akolouthia stoixisis
            for(int j = start;j<(toRem+60);j++){
                if(j < seq[1].length()){
                    fin += seq[1].charAt(j);
                }
            }
            start = toRem+60;
            toRem = start;
            fin +="\n";
            ret += "\n"+fin;
        }
        ret += "\n\n"+seq[2];  
        return ret;
    }
    
}
