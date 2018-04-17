package swarm_algorithm;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberPool {
    private ArrayList<Member> genes = new ArrayList<Member>();
    private HashMap<String, Integer> genesHash = new HashMap<String, Integer>();
//    String[] genesArray = new String[Defines.chromosomeSize];
    ArrayList<String> genesArray = new ArrayList<String>(); 
    
    public void addGene(Member gene) {
        this.genes.add(gene);
        
        this.genesHash.put(gene.getStudentId(), this.genes.size()-1);
//        genesArray[this.genes.size()-1] = gene.getStudentId();
        this.genesArray.add(gene.getStudentId());
    }
    
    public Member getGene(String id) {
        int geneId = this.genesHash.get(id);
        return this.genes.get(geneId);
    }

    public ArrayList<Member> getSorted(String type, String direction) {
        ArrayList<Member> sortedGenes = new ArrayList<Member>();
        if(type != null && type.equalsIgnoreCase("score")) {
            if(direction != null && direction.equalsIgnoreCase("ASC")) {
                sortedGenes =  this.bubbleSort(sortedGenes);
            }
        }
        return sortedGenes;
    }
    
    /**
     * Returns the size of the gene pool - i.e., how many student genes we have.
     * @return Pool size - i.e., number of student genes in the pool.
     */
    public int getPoolSize() {
        return this.genes.size();
    }

    public void print(ArrayList<Member> genes) {
        for(Member g : genes) {
            g.print();            
        }
    }

    public void print() {
        for(Member g : this.genes) {
            g.print();            
        }
    }        
    public ArrayList<Member> getGenes() {
        return genes;
    }
    public ArrayList<String> getGenesArray() {
        return genesArray;
    }
    private  ArrayList<Member> bubbleSort(ArrayList<Member> sortedGenes) {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        Member temp;   //holding variable
        sortedGenes = this.genes;
        while ( flag )
        {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < sortedGenes.size() -1;  j++ )
            {
                if ( sortedGenes.get(j).getAttrScore() > sortedGenes.get(j + 1).getAttrScore())   // change to > for ascending sort
                {                              
                    temp = sortedGenes.get(j);                //swap elements
                    sortedGenes.set(j, sortedGenes.get(j + 1));
                    sortedGenes.set(j + 1, temp);                        
                    flag = true;            //shows a swap occurred 
                }
            }
        }
        return sortedGenes;
    }
}
