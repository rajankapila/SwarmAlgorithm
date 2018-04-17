package swarm_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ParticleGenerator {

    private MemberPool pool;

    public ParticleGenerator(MemberPool pool) {
        this.pool = pool;
    }

    public ParticleGenerator() {

    }

    /**
     * Drives the particle generation process.
     *
     * @param type What type of generator should we activate?
     * @return A new Chromosome.
     */
    public Particle generate(String type) {
        Particle particle = null;

        if (type.equals("random")) {
            particle = this.generateRandom();
        }

        return particle;
    }

    /**
     * Use this constructor after modifying an existing particle's genes. This
     * will generate internal constructs (groups, valid switch).
     *
     * @param particle A (genetically?) modified, pre-existing particle.
     * @return The updated particle.
     */
    public Particle update(Particle particle) {
        particle.makeGroups();
        return particle;
    }
    
    public Particle clone(Particle particle) {
        Particle newParticle = new Particle(particle);
//        this.update(newChromosome);
        
        // all this is done in the copy constructor
//        newChromosome.setNumValidGroup(chromo.getNumValidGroup());
//        newChromosome.setGeneIds(chromo.getGeneIds());
//        newChromosome.setIsValid(chromo.isIsValid());
//        newChromosome.setChromosomeSize(chromo.getChromosomeSize());
//        newChromosome.setChromoGH(chromo.getChromoGH());
//        newChromosome.setPool(chromo.getPool());
        
        return newParticle;        
    }

    /**
     * Generate a random particle - i.e., randomly organize the student genes
     * into a new particle.
     *
     * @return A new random particle.
     */
    private Particle generateRandom() {
        Particle newParticle = new Particle(this.pool);
//        ArrayList<String> geneIds = new ArrayList<String>(Arrays.asList(this.pool.genesArray));
        ArrayList<String> geneIds = new ArrayList<String>(this.pool.genesArray);

        String geneList = ""; // just a debugging tracker

        // Create a new particle, one gene at a time. Randomly select a gene
        // from the geneIds collection and add it to the particle. Keep going
        // until all genes have been moved from geneIds to particle.
        while (geneIds.size() > 0) {

            // Select a gene from geneIds by generating a random number in 
            // [0..size-1].
            int randomGenePos = (Defines.randNum(0, geneIds.size() - 1));

            // Move randomly selected gene from geneIds to new particle
            String newGene = geneIds.remove(randomGenePos);
            newParticle.addMember(newGene);

            // Just remember for debugging
            geneList += String.format("%d [%s], ", randomGenePos, newGene);
        }

        newParticle.makeGroups();

        //System.out.print(geneList);
        return newParticle;
    }

    /**
     * Generate a random particle - i.e., randomly organize the student genes
     * into a new particle.
     *
     * @return A new random particle.
     */
    private Particle generateRandom2() {
        Particle newParticle = new Particle(this.pool);
        Set<String> geneIds = new HashSet<String>(this.pool.genesArray);

        boolean goodSelect;
//        String l = "";
        for (int i = 0; i < Defines.particleSize; i++) {
            goodSelect = false;

            while (!goodSelect) {
                Integer random = (int) Math.ceil(Math.random() * (Defines.particleSize));
                if (geneIds.contains(random.toString())) {
//                    l += random.toString() + ",";
                    newParticle.addMember(random.toString());
                    goodSelect = true;
                    geneIds.remove(random.toString());
                }
            }

        }
        //System.out.print(l);
        return newParticle;
    }

    /**
     * Crossover operation invokes this to build new particles. Using
     * crossover points, extract splices from parent particles and exchange
     * with the other parent, creating two new offspring. [TODO: revisit
     * hardcoded 2 offspring]
     *
     * @param parents Parent particles used in reproduction
     * @param crossPoints Crossover points that delineate the splices for
     * exchange between parents in the reproduction.
     * @return An array of offspring particles.
     */
    public Particle[] generateOffspring(ArrayList<Particle> parents, ArrayList<Integer> crossPoints) {
        Particle[] offspring = new Particle[2];
        // Initialize offspring Chromosomes
        for (int i = 0; i < offspring.length; i++) {
            offspring[i] = new Particle(this.pool);
        }

        // No point repeatedly invokeing get method
        Particle parent0 = parents.get(0);
        Particle parent1 = parents.get(1);

        // startPoint and endPoint are the crossover points. They mark the start 
        // and end points in the list of geneIds where exchange/crossover happens.
        int startPoint;
        int endPoint;

        // Maximum two crossover points in this implementation, used to define
        // endpoints for the particle section to be "crossed over".
        // Note: includes flexibility for one or two crossover points.
        if (crossPoints != null) {
            if (crossPoints.size() > 0) {
                startPoint = crossPoints.get(0);
                if (crossPoints.size() > 1) {
                    endPoint = crossPoints.get(1);
                } else {
                    endPoint = Defines.particleSize;
                }

                // First, rather than complicate things with a wraparound splice, we'll 
                // just switch start and end points if startPoint comes after endPoint. 
                // This works because the parents end up the same, but switched: 
                // parent1 ends up as what parent2 would have been, and vice versa.
                if (startPoint > endPoint) {
                    int temp = startPoint;
                    startPoint = endPoint;
                    endPoint = temp;
                }

            } else {
                Log.debugMsg("Chromosome.getSplice error: crossover point array is empty.");
                return null; // [TODO: Might want to exit here.]
            }
        } else {
            // crosspoints is null; we're not doing crossover, just replicating 
            // parents. We'll force this with artificial values for startPoint
            // and endPoint.
            startPoint = Defines.particleSize + 1;
            endPoint = -1;
        }

        // Build offspring using one parent and splice from other parent, one 
        // gene at a time. [TODO: revisit hardcoding]        
        for (int geneIdx = 0; geneIdx < Defines.particleSize; geneIdx++) {
            if (geneIdx < startPoint || geneIdx > endPoint) {
                offspring[0].addMember(parent0.getMember(geneIdx));
                offspring[1].addMember(parent1.getMember(geneIdx));
            } else {
                offspring[0].addMember(parent1.getMember(geneIdx));
                offspring[1].addMember(parent0.getMember(geneIdx));
            }
        }

        return offspring;
    }

    /**
     * Crossover operation OX invokes this to build a new particle. Using
     * crossover points, extract splices from parent particles and exchange
     * with the other parent, then fix, creating one new offspring.
     *
     * Approach: 1) splice defined by crosspoints is copied from parent 1 to
     * offspring. 2) parent 2 fills the rest of the offspring
     *
     * @param parents Parent particles used in reproduction
     * @param crossPoints Crossover points that delineate the splices for
     * exchange between parents in the reproduction.
     * @return An array of offspring particles.
     */
    public Particle[] generateOffspringOX(ArrayList<Particle> parents, ArrayList<Integer> crossPoints) {
        
//        Log.debugMsg(String.format("Crossover points are %d and %d out of %d genes in chromo. Mode is %s",
//                crossPoints.get(0), crossPoints.get(1), Defines.particleSize, (Defines.cpMode == Defines.CP_NO_BOUNDARY?"no boundary":"random")));
        
        Particle child = new Particle(this.pool);
        Particle parent0 = parents.get(0);
        Particle parent1 = parents.get(1);

        int startPoint = crossPoints.get(0).intValue();
        int endPoint = crossPoints.get(1).intValue();

        // copy subsection between crossPoints from first parent to child
        for (int i = startPoint; i <= endPoint; i++) {
            child.setParticle(i, parent0.getMember(i));
        }

        int parentIdx; // Points to gene in second parent
        if (endPoint < Defines.particleSize - 1) {
            parentIdx = endPoint + 1;
        } else {
            parentIdx = 0;
        }

        // fill child's end from second parent, without duplication
        for (int childIdx = endPoint + 1; childIdx < Defines.particleSize; childIdx++) {
            while (child.containsGene(parent1.getMember(parentIdx))) {
                parentIdx = pointerIncrement(parentIdx, Defines.particleSize);
            }
            child.setParticle(childIdx, parent1.getMember(parentIdx));
            parentIdx = pointerIncrement(parentIdx, Defines.particleSize);
        }

        // fill child's start from second parent, without duplication
        for (int childIdx = 0; childIdx < startPoint; childIdx++) {
            while (child.containsGene(parent1.getMember(parentIdx))) {
                parentIdx = pointerIncrement(parentIdx, Defines.particleSize);
            }
            child.setParticle(childIdx, parent1.getMember(parentIdx));
            parentIdx = pointerIncrement(parentIdx, Defines.particleSize);
        }
        
        child.makeGroups(); // finishes setup

        // For consistency with other methods, we return the single offspring in an array.
        return new Particle[]{child};

    }

    /**
     * Increments an index (or pointer), not beyond maximum, wrapping to 0.
     *
     * @param idx A index into an array of values
     * @param max Maximum value for this index.
     * @return new value for index.
     */
    private int pointerIncrement(int idx, int max) {
        // assumes we're indexing a 0-based array.
        if (idx >= max - 1) {
            idx = 0;
        } else {
            idx++;
        }
        return idx;
    }

    private ArrayList<Integer> sortCrossPoints(ArrayList<Integer> crossPoints) {
        return null;

    }
    
   
    /**
     * This function creates a new particle by crossing a particle with the pBest particle
     * and gBest particle
     * @param swarm the current swarm
     * @param particle the initial particle
     * @param type which best particle should be crossed over
     * @return newly generated particle
     */
    public Particle crossoverPSO(Swarm swarm, Particle particle, String type) {
        int k, m, startPoint, endPoint, memberCount = 0;
        Particle newParticle = new Particle(particle.getPool());
        String[] members;      
        
//        Log.debugMsg(String.format("Crossover points are %d and %d out of %d genes in chromo. Mode is %s",
//                crossPoints.get(0), crossPoints.get(1), Defines.particleSize, (Defines.cpMode == Defines.CP_NO_BOUNDARY?"no boundary":"random")));
        //generate random starting location
        k = Defines.randNum(0, Defines.particleSize - 1);
        
        //calculate m (number of members to cross over) depending type of best particle
        if(type.equalsIgnoreCase("pBest")) {
            m = (int)(Defines.c1 * Defines.randNum(0, Defines.particleSize - 1));
            members = particle.getpBest().getMemberIds();
        } else if(type.equalsIgnoreCase("gBest")) {
            m = (int)(Defines.c2 * Defines.randNum(0, Defines.particleSize - 1));            
            members = swarm.getBestParticle().getMemberIds();            
        } else {
            return null;        
        }        
       
        startPoint = k;
        endPoint = startPoint + m;
        /*System.out.println("type=" + type);
        System.out.println("k=" + k);
        System.out.println("m=" + m);
        System.out.println("startPoint=" + startPoint);
        System.out.println("endPoint=" + endPoint);
      */
        //add the sequence startPoint to endPoint to the beginning of the new particle
        //with regards to wrapping around the end
        for (int i = startPoint; i <= endPoint; i++) {
            //calculate index, if beyond particle size then wrap to beginning of best
            //particle
            int index = (i < Defines.particleSize)? i : (i -  Defines.particleSize);            
            newParticle.addMember(members[index]);
            memberCount++;
        }
        
        //add the rest of the original particle to complete the new particle 
        for(int i = 0; i < Defines.particleSize; i++) {            
            newParticle.addMemberIfNotMember(particle.getMember(i));    
            memberCount++;
        }        
        newParticle.setpBest(particle.getpBest());
        
        return newParticle;
    }
    
    /**
     * This function generates a random start point and end point and reverses the
     * sequence between those two points
     * 
     * @param particle
     * @return the modified particle
     */
    
    public Particle reversePSO (Particle particle){
        int startPoint = Defines.randNum(0, Defines.particleSize - 1);
        int endPoint = Defines.randNum(startPoint, Defines.particleSize - 1);
        String[] newMembers = new String[Defines.particleSize];
        //System.out.println("startPoint=" + startPoint);
        //System.out.println("endPoint=" + endPoint);
        for(int i = 0; i < startPoint; i++) {
            //System.out.println("i=" + i);
            newMembers[i] = particle.getMember(i);
        }
        for(int i = endPoint; i >= startPoint; i--) {
            int index = startPoint + (endPoint - i);
            //System.out.println("index=" + index + " i=" + i);
            newMembers[index] = particle.getMember(i);
        }
        for(int i = endPoint + 1; i < Defines.particleSize; i++) {
            //System.out.println("i=" + i);
            newMembers[i] = particle.getMember(i);
        }
        particle.setMemberIds(newMembers);
        return particle;
    
    }

}
