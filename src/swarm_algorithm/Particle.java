package swarm_algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class Particle implements Comparable<Particle> {

    private String[] memberIds; // relies on knowing input data :(
    private MemberPool pool; //[TODO: does chromosome need the pool?]
    private boolean isValid;
    private int particleSize; // [TODO: is this needed?]
    private Group[] groups;
    private int numValidGroup;
    private double particleGH;
 
    private Particle pBest = null;


    public Particle(MemberPool pool) {

        this.numValidGroup = 0;
        this.memberIds = new String[Defines.particleSize];
        this.isValid = false;
        this.particleSize = 0;
        this.particleGH = 0.0;
        this.pool = pool;
        
    }

    /**
     * Copy constructor.
     * @param particle Copy this chromosome.
     */
    public Particle(Particle particle) {
        this.numValidGroup = particle.numValidGroup;
        this.memberIds = new String[particle.particleSize];
        System.arraycopy(particle.memberIds, 0, this.memberIds, 0, particle.particleSize);
        this.groups = new Group[particle.getGroups().length];
        System.arraycopy(particle.groups, 0, this.groups, 0, particle.groups.length);
        this.isValid = particle.isValid;
        this.particleSize = particle.particleSize;
        this.particleGH = particle.particleGH;
        this.pool = particle.pool;
        this.pBest = particle.pBest;
    }

    public void addMember(String id) {
        this.memberIds[this.particleSize] = id;
        this.particleSize++;
//      System.out.println("particleSize=" + particleSize);
    }
    
    public void addMemberIfNotMember(String id) {
        for(String memberId : this.memberIds) {
            if(memberId != null && memberId.equals(id)) {
                
                return;
            }
        }
        //System.out.println("particleSize=" + particleSize);

        this.addMember(id);
    }

    public boolean isValidSize() {
        return (this.particleSize == Defines.particleSize);
    }

    public boolean isValid() {
//        this.isValid = (this.isValidSize() && this.calcValid());
        return this.isIsValid();
    }

    /**
     * Checks to see whether all groups are valid according to requirements.
     * Requires: - exactly 4 members. - GH > 0.5 - Euclidean distance > 2 for at
     * least one pair in group.
     *
     * @return True if all groups are valid; false otherwise.
     */
    private boolean calcValid() {
        boolean groupCheck = true;
        /*if (!this.isValidSize()) {
            Log.debugMsg("Not Valid Size");
            return false;
        }*/
        /*ArrayList<String> geneList = new ArrayList(Arrays.asList(this.memberIds));
        for(Member member : this.pool.getGenes()) {
            if(!geneList.contains(member.getStudentId())) {
                Log.debugMsg("invalid genes missing");
                return false;
            }
        }*/
        // Examine each group
        this.setNumValidGroup(0);
        for (Group group : this.groups) {
            if (!group.isValid()) {
                groupCheck = false;
            } else {
                this.setNumValidGroup(this.getNumValidGroup() + 1);
            }
        }
        if(!groupCheck) {           
            return false;
        }
        
        return true;
    }

    /**
     * Checks to see if a gene is contained in this chromosome.
     * @param gene A gene to look for in this chromosome.
     * @return True if this gene is contained in this chromosome; false otherwise.
     */
    public boolean containsGene(String gene) {
        for (int i = 0; i < this.getMemberIds().length; i++) {
            if (gene.equals(this.getMember(i))) {
                return true;
            }
        }
        return false;
    }
    
//    public boolean checkValid2() {
//        // First, layer groups on  top of chromosome. First four student genes
//        // are group 1; next 4 are group 2, ....
//        for (int i = 0; i < Defines.totalGroups; i++) {
//            Group g = new Group();
//            for (int j = 0; j < Defines.GROUP_SIZE; j++) {
//                g.addMember(this.pool.getGene(this.geneIds[((i * Defines.GROUP_SIZE) + j)]));
//            }
////            System.out.println("GH = " + g.getGH());
//            if (!g.isValid()) {
//                return false;
//            }
//        }
//        return true;
//    }

    /**
     * Getter - returns groups[]
     *
     * @return Array of groups, where each group is an array of student genes.
     */
    public Group[] getGroups() {
        return this.groups;
    }

    /**
     * Divides the chromosome into groups.
     *
     * @return Array of groups, where each group is an array of student genes.
     */
    public void makeGroups() {
        // How many groups will we have?
        int nGroups = this.getPool().getPoolSize() / Defines.GROUP_SIZE; // TODO: revaluate reliance on this math working out.

        this.setGroups(new Group[nGroups]);

        int nextGenePos = 0;

        for (int groupIdx = 0; groupIdx < nGroups; groupIdx++) {
            groups[groupIdx] = new Group();
            for (int geneIdx = 0; geneIdx < Defines.GROUP_SIZE; geneIdx++) {
                groups[groupIdx].addMember(this.getPool().getGene(this.getMemberIds()[nextGenePos++]));                
            }
            // Now that group membership is complete, trigger the calculation
            // of the various group measures. 
            groups[groupIdx].calcMetrics();
           
        }
        // set up other fields
        this.setParticleSize(this.getMemberIds().length);
        this.calcTotalGH();
        this.setIsValid(this.calcValid());
    }

    /**
     * The chromosome's total GH - i.e., the sum of the GH for each member
     * group, is the metric for optimization. This routine calculates chromosome
     * GH and sets the global chromoGH.
     *
     * @return Chromosome's GH.
     */
    private void calcTotalGH() {
        double sumGH = 0;
        if (this.groups == null) {
            this.makeGroups();
        }
        for (Group group : this.getGroups()) {
            if(group.isValid()) {
                sumGH += group.getGH();
            }
        }
        // update this particle's personal best iff current fitness exceeds 
        // previous best, and count of valid groups in particle isn't decreasing.
        if(this.pBest == null || (this.pBest.getTotalGH() < sumGH && this.pBest.getNumValidGroup() <= this.getNumValidGroup())) {
            this.pBest = this;           
        }
        this.setParticleGH(sumGH);
    }
    private void calcTotalGHwithPenalty() {
        double sumGH = 0;
        if (this.groups == null) {
            this.makeGroups();
        }
        for (Group group : this.getGroups()) {
            sumGH += group.getGH();
        }
        if (Defines.useInvPenalty) {
            if (this.isIsValid()) {
                this.setParticleGH(sumGH);
            } else {
                this.setParticleGH(sumGH * Defines.chromoInvPenalty);
            }
        } else {
            this.setParticleGH(sumGH);
        }
    }
    public double getTotalGH() {
        if (this.getParticleGH() == 0.0) {
            this.calcTotalGH();
        }
        return this.getParticleGH();
    }

    /**
     * Used in crossover operation, getSplice returns the piece of a particle
     * that will be copied to another parent.
     *
     * @param crossPoints Array of 1 or more crossover points.
     * @return A subset of the geneIds from this particle.
     */
    public ArrayList<String> getSplice(ArrayList<Integer> crossPoints) {
        int startPoint;
        int endPoint;

        // Maximum two crossover points in this implementation, used to define
        // endpoints for the particle section to be "crossed over".
        if (crossPoints.size() > 0) {
            startPoint = crossPoints.get(0);
            if (crossPoints.size() > 1) {
                endPoint = crossPoints.get(1);
            } else {
                endPoint = this.getMemberIds().length;
            }
        } else {
            Log.debugMsg("Chromosome.getSplice error: crossover point array is empty.");
            return null;
        }

        // First, rather than complicate things with a wraparound splice, we'll 
        // just switch start and end points when start comes after end. This works
        // because the parents end up the same, but switched - parent1 is what parent2
        // would have been, and vice versa.
        if (startPoint > endPoint) {
            int temp = startPoint;
            startPoint = endPoint;
            endPoint = temp;
        }

        // Extract a copy of the crossover splice - i.e., subsection of this particle.
        ArrayList<String> splice = new ArrayList<String>();
        for (int i = startPoint; i <= endPoint; i++) {
            splice.add(this.getMemberIds()[i]);
        }

        return splice;
    }

    /**
     * Copy one gene from this particle
     *
     * @param num The gene position we seek
     * @return String The gene Id.
     */
    public String getMember(int num) {
        String gene = "";
        try  {
            gene = this.getMemberIds()[num];
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.debugMsg("EXCEPTION: ArrayIndexOutOfBoundsException in Chromosome.getGene");
            String msg = String.format("\tgeneIds.length= %d\n", this.getMemberIds().length);
            msg += String.format("\tDefines.CHROMOSOME_SIZE = %d\n", Defines.particleSize);
            msg += String.format("\trequesting gene #%d\n", num);
            msg += String.format("\tChromosome size = %d\n", this.particleSize);
            Log.debugMsg(msg);
            ex.printStackTrace();
            System.exit(-1);
        }
        return gene;
    }
    
    /**
     * Mutation will need to change single genes within the particle.
     * @param genePos The position of the gene to change.
     * @param geneId The new value for the gene at position genePos.
     */
    public void setParticle(int genePos, String geneId) {
        try  {
            this.memberIds[genePos] = geneId;
        } catch (ArrayIndexOutOfBoundsException ex) {
            Log.debugMsg("EXCEPTION: ArrayIndexOutOfBoundsException in Chromosome.setGene");
            String msg = String.format("\tgeneIds.length= %d\n", this.getMemberIds().length);
            msg += String.format("\tDefines.CHROMOSOME_SIZE = %d\n", Defines.particleSize);
            msg += String.format("\tAttemtpt to set gene #%d to %s\n", genePos, geneId);
            msg += String.format("\tChromosome size = %d\n", this.particleSize);
            ex.printStackTrace();
            Log.debugMsg(msg);
            System.exit(-1);
        }
    }

    /**
     * Output for debugging.
     */
    public void print() {
        for (int i = 0; i < this.getMemberIds().length; i++) {
            //System.out.println(this.geneIds[i]);
            this.getPool().getGene(this.getMemberIds()[i]).print();
        }
    }
    
    public void printGroups() {
        for (int i = 0; i < this.groups.length; i++) {
            System.out.format("Group %d:\t%s\t\tGH=%.2f\tMaxDist=%.1f\n", 
                    i+1, this.groups[i].toString(), this.groups[i].getGH(), this.groups[i].getMaxDistance());
        }
    }
    
    public void printGeneSequence() {
        String output = "";
        for (int i = 0; i < this.getMemberIds().length; i++) {
            output += i  + "-" + this.getMemberIds()[i] + ", ";
        }
        Log.debugMsg(output);
    }
    

    public int getNumValidGroup() {
        return numValidGroup;
    }

    public int getParticleSize() {
        return particleSize;
    }

    @Override
    public int compareTo(Particle chromo) {
        if (this.getParticleGH() < chromo.getTotalGH()) {
            return 1;
        } else if (this.getParticleGH() > chromo.getTotalGH()) {
            return -1;
        } else {
            return 0;
        }
    }
    
    /**
     * Checks to see if two particles match gene for gene.
     * @param chromo Compare this particle to the current particle.
     * @return Returns true if particles match at each gene; false otherwise.
     */
    public boolean equivTo(Particle chromo) {
        if (this.getParticleSize() != chromo.getParticleSize()) {
            return false;
        }
        
        if (this.getTotalGH() != chromo.getTotalGH()) {
            return false;
        }
        
        for (int i = 0; i < this.getParticleSize(); i++) {
            if (!this.getMember(i).equals(chromo.getMember(i))) {
                return false;
            }
        }        
        return true;        
    }

    /**
     * @param geneIds the geneIds to set
     */
//    public void setGeneIds(String[] geneIds) {
//        this.setGeneIds(geneIds);
//    }

    /**
     * @param pool the pool to set
     */
    public void setPool(MemberPool pool) {
        this.pool = pool;
    }

    /**
     * @param geneIds the geneIds to set
     */
    public void setMemberIds(String[] geneIds) {
        System.arraycopy(geneIds, 0, this.memberIds, 0, geneIds.length);
    }

    /**
     * @param isValid the isValid to set
     */
    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * @param particleSize the particleSize to set
     */
    public void setParticleSize(int particleSize) {
        this.particleSize = particleSize;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(Group[] groups) {
        this.groups = groups;
    }

    /**
     * @param numValidGroup the numValidGroup to set
     */
    public void setNumValidGroup(int numValidGroup) {
        this.numValidGroup = numValidGroup;
    }

    /**
     * @param particleGH the chromoGH to set
     */
    public void setParticleGH(double particleGH) {
        this.particleGH = particleGH;
    }

    /**
     * @return the geneIds
     */
    public String[] getMemberIds() {
        return memberIds;
    }

    /**
     * @return the pool
     */
    public MemberPool getPool() {
        return pool;
    }

    /**
     * @return the isValid
     */
    public boolean isIsValid() {
        return isValid;
    }

    /**
     * @return the particleGH
     */
    public double getParticleGH() {
        return particleGH;
    }

    public Particle getpBest() {
        return pBest;
    }

    public void setpBest(Particle pBest) {
        this.pBest = pBest;
    }

    

    
    
    
}
