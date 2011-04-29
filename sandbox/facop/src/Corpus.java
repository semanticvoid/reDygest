
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author semanticvoid
 */
public class Corpus {
    
    List<Instance> instances;
    
    public Corpus() {
        instances = new ArrayList<Instance>();
    }
    
    public void addInstance(Instance i) {
        instances.add(i);
    }
    
    public List<Instance> getInstances() {
        return instances;
    }
}
