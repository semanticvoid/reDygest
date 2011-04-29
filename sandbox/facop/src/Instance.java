/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author semanticvoid
 */
public class Instance {
    
    public int id;
    public String line;
    public String label;
    public String features;
    
    public Instance(int id, String line, String label) {
        this.id = id;
        this.line = line;
        this.label = label;
        this.features = "";
    }
    
    public String toVWString() {
        return label + " 1 " + id + features;
    }
}
