package swarm_algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Swarm {

    private ArrayList<Particle> particles;

    private ArrayList<Particle> parents;
    private ArrayList<Particle> offspring;
    private MemberPool pool;
    private Integer validParticles = 0;
    private boolean isSorted = false;
//    private Run run;

    private double bestScore = 0; // highest GH found in population for valid chromo
    private Particle bestParticle; // best valid chromo 

    /**
     * Constructor: create a population; initialize the student gene pool and
     * the number of parents.
     *
     * @param pool Student gene pool
     * @param Defines.crossoverParentCt Number of parents selected for
     * reproduction.
     */
    public Swarm(MemberPool pool) {
        this.bestScore = 0.0;
        this.pool = pool;
        this.particles = new ArrayList<Particle>();
    }

    /**
     * Add a particle to the population.
     *
     * @param c Particle to be added to the population.
     */
    public void addParticle(Particle c) {
        this.particles.add(c);
    }

    /**
     * Add an array of particles to the population.
     *
     * @param particles Particle array to be added to this population.
     */
    public void addParticles(ArrayList<Particle> particles) {
        if (particles != null) {
            for (Particle chromo : particles) {
                this.particles.add(chromo);
            }
        }
    }

    /**
     * We need to know which are the highest scoring particles for help in
     * selecting parents. 
     */
    public void evaluateSwarm() {
        int count = 0;
        this.resetValidParticles();
        for (Particle particle : particles) {
            double particleGH = particle.getTotalGH();
            if (particle.isValid()) {
                this.validParticles++;
            }
            // update swarm global best score iff particle's fitness exceeds
            // prior global best, and particle's valid group count isn't smaller
            // than previous best particle's valid group count.
            if (particleGH > this.getBestScore() && (this.getBestScore() == 0 || this.getBestParticle().getNumValidGroup() <= particle.getNumValidGroup())) {
                this.setBestScore(particleGH);
                this.setBestParticle(particle);
//                System.out.println("Setting swarm best score----------=" + particle.getNumValidGroup() + " - " + particleGH);
            }
            
            //Log.debugMsg(chromo.getTotalGH().toString());
//            this.map.put(chromoGH, chromo);
        }
    }

    /**
     * Elitism automatically promotes some of the best of this population into
     * the next generation. This method identifies the elite particles that
     * get automatically promoted.
     * @return 
     */
    public ArrayList<Particle> getEliteParticles() {
        // SortedMap instead of HashMap? Duplicate key values? 
        ArrayList<Particle> elites = new ArrayList<Particle>();
//      ArrayList<Chromosome> sortedList = this.getChromosomesSorted();
        boolean firstRun = true;

        //this.sort();
        Collections.sort(this.particles);

        while (elites.size() < Defines.eliteCt) {
            for (Particle particle : this.particles) {
                if (elites.size() < Defines.eliteCt) {
                    if (firstRun && particle.isValid()) {
                        elites.add(particle);
                    } else {
                        elites.add(particle);
                    }
                }
            }
            firstRun = false;
        }
        return elites;
    }

    /**
     * Sort the particles by GH
     *
     *
     * @return 
     */
    public ArrayList<Particle> getParticlesSorted() {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        Particle temp;   //holding variable
        ArrayList<Particle> sortedParticles = this.particles;
        while (flag) {
            flag = false;    //set flag to false awaiting a possible swap
            for (j = 0; j < sortedParticles.size() - 1; j++) {
                if (sortedParticles.get(j).getTotalGH() < sortedParticles.get(j + 1).getTotalGH()) // change to > for ascending sort
                {
                    temp = sortedParticles.get(j);                //swap elements
                    sortedParticles.set(j, sortedParticles.get(j + 1));
                    sortedParticles.set(j + 1, temp);
                    flag = true;            //shows a swap occurred 
                }
            }
        }
        return sortedParticles;
    }

    /**
     * Indicates whether this population has its full complement of particles.
     *
     * @return True if this population contains its full complement of
     * particles; false otherwise.
     */
    public boolean isFull() {
        return this.particles.size() >= Defines.popSize;
    }

    /**
     * Nominate parents for reproduction. Several approaches are possible. We'll
     * start with a purely random selection of two parents.
     */
    public void selectParents() {
        // Create a new parent list for this reproductive cycle. Let the garbage
        // handler dispose of the old list, if there was one.
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                this.parents = this.selectParentsRandom();
                break;

            case Defines.PA_ROULETTE:
                this.parents = this.selectParentsRoulette();
                break;

            case Defines.PA_TOURNAMENT:
                this.parents = this.selectParentsTournament();
                break;
        }
//        this.howGoodAreParents();
    }

    /**
     * Nominate parents for reproduction. Several approaches are possible. We'll
     * start with a purely random selection of two parents.
     */
    public void selectParents1() {
        // Create a new parent list for this reproductive cycle. Let the garbage
        // handler dispose of the old list, if there was one.
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                this.parents = this.selectParentsRandom();
                break;

            case Defines.PA_ROULETTE:
                this.parents = this.selectParentsRoulette();
                break;

            case Defines.PA_TOURNAMENT:
                this.parents = this.selectParentsTournament();
                break;
        }
//        this.howGoodAreParents();
    }

    /**
     * for testing - displays selection results.
     */
    private void howGoodAreParents() {
        String msg = "From chromo with GH scores:  ";
        for (int i = 0; i < this.particles.size(); i++) {
            msg += this.particles.get(i).getTotalGH() + "-" + this.particles.get(i).getNumValidGroup() + " ";
        }

        msg += "\n\tChose parents (";
        switch (Defines.parentAlgo) {
            case Defines.PA_RANDOM:
                msg += "random";
                break;

            case Defines.PA_ROULETTE:
                msg += "roulette";
                break;

            case Defines.PA_TOURNAMENT:
                msg += "tournament";
                msg += String.format(" (size %d)", Defines.tournamentSize);
                break;
        }
        msg += ") ";
        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            msg += this.parents.get(i).getTotalGH() + " ";
        }
        Log.debugMsg(msg);
    }

    /**
     * Tournament parent selection - should outperform Roulette. Increasing
     * tournament size increases selection pressure. Increased selection
     * pressure causes faster convergence. Must balance convergence with
     * exploration.
     *
     * @return Parents.
     */
    private ArrayList<Particle> selectParentsTournament() {
        ArrayList<Particle> parents = new ArrayList<Particle>(Defines.crossoverParentCt);
        ArrayList<Particle> matingPool = new ArrayList<Particle>(Defines.crossoverParentCt);

        // Run tournaments to select parents - for each parent
        for (int parentIdx = 0; parentIdx < Defines.crossoverParentCt; parentIdx++) {

            // Run tournaments - get random contestants (run.getPaTournamentSize())
            for (int tournIdx = 0; tournIdx < Defines.tournamentSize; tournIdx++) {
                int contestantID = Defines.randNum(0, this.particles.size() - 1);
                matingPool.add(this.particles.get(contestantID));
            }
            Collections.sort(matingPool);
            parents.add(matingPool.get(0));
        }

        return parents;
    }

    /**
     * Roulette selection of parents.
     *
     * @return Parents.
     */
    private ArrayList<Particle> selectParentsRoulette() {
        ArrayList<Particle> parents = new ArrayList<Particle>(Defines.crossoverParentCt);
        double sumGH = 0.0; // sums GH for all particles in this pop
        Random randgen = new Random(); // random number generator
        for (Particle chromo : this.particles) {
            sumGH += chromo.getTotalGH();
        }
        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            double parentRandomizer = randgen.nextDouble() * sumGH; // get random #
            double aggGH = 0.0; // aggregate the GH until we reach our random #
            int chromoIdx = 0; // identifies the particle in the pop
            Particle parentCandidate;
            do {
                parentCandidate = this.particles.get(chromoIdx++);
                aggGH += parentCandidate.getTotalGH();
            } while (aggGH < parentRandomizer);
            parents.add(parentCandidate);
        }
        return parents;
    }

    /**
     * Random selection of parents from this particle.
     *
     * @return Parents.
     */
    private ArrayList<Particle> selectParentsRandom() {
        ArrayList<Particle> parents = new ArrayList<Particle>(Defines.crossoverParentCt);

        for (int i = 0; i < Defines.crossoverParentCt; i++) {
            // Generate random index into particles in range [0..size-1]
            int randomParent = Defines.randNum(0, particles.size() - 1);
            // Remember the new parent
            parents.add(particles.get(randomParent));
        }
        return parents;
    }

    /**
     * Crossover executive - selects and initiates crossover operation.
     */
    public void crossover() {

        // Perform crossover with probability Defines.PROB_CROSSOVER
        if (Defines.probCrossover > Math.random()) {
            this.crossoverOX();
        } else {
            // randomly select one of the parents to copy without crossover
            int idx = Defines.randNum(0, this.parents.size() - 1);
            Particle newChild = this.parents.get(idx);
            this.offspring = new ArrayList<Particle>();
            this.offspring.add(newChild);
        }
    }

    /**
     * Performs order crossover (OX) operation on parents. As discussed by
     * Potvin. Generates one offspring from two parents.
     */
    public void crossoverOX() {

        // [TODO: consider usefulness of a crossover point that coincides with
        // group boundary. If both crossovers are group boundaries, the crossover does nothing.]
        ArrayList<Integer> crossPoints = getCrossoverPoints(Defines.cpMode);

        Collections.sort(crossPoints);

        ParticleGenerator chromoGen = new ParticleGenerator(this.getPool());

        this.offspring = new ArrayList<Particle>(Arrays.asList(chromoGen.generateOffspringOX(this.parents, crossPoints)));

    }

    /**
     * Crossover point executive
     *
     * @return
     */
    private ArrayList<Integer> getCrossoverPoints(int mode) {

        ArrayList<Integer> cPoints = new ArrayList<Integer>();

        int cPoint1 = this.getCrossoverPoint(mode);
        int cPoint2;
        do {
            cPoint2 = this.getCrossoverPoint(mode);
        } while (cPoint2 == cPoint1);

        for (int i = 0; i < Defines.crossoverPoints; i++) {
            cPoints.add(this.getCrossoverPoint(mode));
        }

        return cPoints;
    }

    /**
     * Get a crossover point
     *
     * @param mode Pure random, or not on group boundary
     * @return A new crossover point
     */
    private int getCrossoverPoint(int mode) {
        int cPoint = 0;

        switch (mode) {
            case Defines.CP_PURE_RANDOM:
                cPoint = Defines.randNum(0, Defines.particleSize - 1);
                break;

            case Defines.CP_NO_BOUNDARY:
                do {
                    cPoint = Defines.randNum(0, Defines.particleSize - 1);
                } while (cPoint % Defines.GROUP_SIZE == 0 || cPoint % Defines.GROUP_SIZE == 3);
                break;
        }

        return cPoint;
    }

    /**
     * Performs order crossover (OX) operation on parents. As discussed by
     * Potvin.
     */
    public void origCrossover() {

        ArrayList<Integer> crossPoints;

        // Perform crossover with probability Defines.PROB_CROSSOVER
        if (Defines.probCrossover > Math.random()) {
            // Choose random crossover points within the particle
            // [TODO: consider usefulness of a crossover point that coincides with
            // group boundary. If both crossovers are group boundaries, the crossover does nothing.]
            crossPoints = new ArrayList<Integer>();
            for (int i = 0; i < Defines.crossoverPoints; i++) {
                crossPoints.add(Defines.randNum(0, Defines.particleSize - 1));
            }
        } else {
            // Parents are used without crossover - no crossover points; 
            crossPoints = null;
        }

        ParticleGenerator particleGenerator = new ParticleGenerator(this.getPool());
        this.offspring = new ArrayList<Particle>(Arrays.asList(particleGenerator.generateOffspring(this.parents, crossPoints)));
    }

    /**
     * Perform mutation operation on offspring. We define the mutation operation
     * for this application as the exchange of two genes within the particle.
     * Mutation is considered for each of the offspring.
     */
    public void mutate() {
        if (this.offspring != null) {
            for (int i = 0; i < this.offspring.size(); i++) {

                if (Defines.probMutate > Math.random()) {
                    // OK, choose two genes to switch
                    int nGene1 = Defines.randNum(0, Defines.particleSize - 1);
                    int nGene2 = nGene1;
                    // Make sure gene2 is not the same gene as gene1
                    while (nGene2 == nGene1) {
                        nGene2 = Defines.randNum(0, Defines.particleSize - 1);
                    }

                    // Switch two genes
                    String temp = this.offspring.get(i).getMember(nGene1);

                    this.offspring.get(i).setParticle(nGene1, this.offspring.get(i).getMember(nGene2));
                    this.offspring.get(i).setParticle(nGene2, temp);
                }
                // Regenerate the particle
                ParticleGenerator particleGenerator = new ParticleGenerator();
                particleGenerator.update(this.offspring.get(i));
            }

        }

    }

    /**
     * The children leave the nest.
     *
     * @return ArrayList<Particle> of new children.
     */
    public ArrayList<Particle> getOffspring() {
        return this.offspring;
    }

    public ArrayList<Particle> getParticles() {
        return this.particles;
    }

    public ArrayList<Particle> getParents() {
        return this.parents;
    }

    /**
     * Get the historical best from this swarm.
     * @return the bestScore
     */
    public double getBestScore() {
        return bestScore;
    }

    /**
     * @return the bestChromo
     */
    public Particle getBestParticle() {
        return bestParticle;
    }

    public void resetValidParticles() {
        this.validParticles = 0;
    }
    
    public Integer getValidParticles() {
        return validParticles;
    }

    public double getMaxGH() {
        this.sort();
        return this.particles.get(0).getTotalGH();
    }

    public void sort() {
        if (!this.isSorted) {
            Collections.sort(this.particles);
            this.isSorted = true;
        }
    }

    /**
     * @param bestParticle the bestChromo to set
     */
    public void setBestParticle(Particle bestParticle) {
//        this.bestChromo = bestChromo;
        ParticleGenerator particleGen = new ParticleGenerator(this.getPool());
        this.bestParticle = particleGen.clone(bestParticle);
    }
    
    /*
     * This function moves the particles position using crossover and reverse functions
     * 
     */
    
    public void move() {
        for(int i = 0; i < this.particles.size(); i++) {
            
            this.particles.set(i, this.crossoverPSO(this.particles.get(i)));
            this.particles.set(i, this.reversePSO(this.particles.get(i)));
        }    
    }
    /**
     * 
     * This function sends a particle to the crossover operator. The crossover function 
     * is carried out with the pBest and gBest particles
     * 
     * @param particle the particle to be crossed over 
     * @return newly generated particle
     */
    
    public Particle crossoverPSO(Particle particle) {
        
        ParticleGenerator particleGenerator = new ParticleGenerator();        
        particle = particleGenerator.crossoverPSO(this, particle, "pBest");        
        particle = particleGenerator.crossoverPSO(this, particle, "gBest");             
        return particle;
    }
    
    /**
     * This function sends a particle to have a section of it reversed
     * 
     * @param particle
     * @return modifed particle
     */
    
    public Particle reversePSO(Particle particle) {
        ParticleGenerator particleGenerator = new ParticleGenerator();
        return particleGenerator.reversePSO(particle);
    
    }

    /**
     * @return the pool
     */
    public MemberPool getPool() {
        return pool;
    }

    /**
     * @param bestScore the bestScore to set
     */
    public void setBestScore(double bestScore) {
        this.bestScore = bestScore;
    }

}
