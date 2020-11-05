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
public class Protein {
    private String header, sequence;

    public Protein() {}

    /**
     * Constructor.
     * @param header the name of the sequence
     * @param sequence the amino acid sequence
     */
    public Protein(String header, String sequence) {
        this.header = header;
        this.sequence = sequence;
    }

    /**
     *
     * @return the header 
     */
    public String getHeader() {
        return header;
    }

    /**
     *  
     * @param header the name of the sequence
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     *
     * @return amino acid sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     *
     * @param sequence the amino acid sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    
    
}
