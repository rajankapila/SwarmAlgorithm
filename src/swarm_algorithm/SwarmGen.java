package swarm_algorithm;

import java.util.ArrayList;

/**
 * SwarmGen is a collection of Swarm.
 */
public class SwarmGen {

    private ArrayList<Swarm> swarms;
//    private Swarm swarm;
    private boolean isValidFound = false;
    private double totalGH;
    private double maxGH;
    private double averageGH;
    private String output;
    private Particle bestParticle;
    private boolean isBestValid;
//    private Run run;

    /**
     * Constructor
     *
     * @param swarm
     */
    public SwarmGen() {
        this.swarms = new ArrayList<Swarm>();
        this.isBestValid = false;
        this.bestParticle = null;
//        this.run = run;
        this.output = "";
        this.totalGH = 0.0;
        this.maxGH = 0.0;
    }

    public void add(Swarm swarm) {
        swarm.sort();
        this.swarms.add(swarm);
    }

    public void print() {
        Log.debugMsg(this.output);
        int nValidParticles = 0;
        // count all valid particles in our collection of swarms
        for (int i = 0; i < this.swarms.size(); i++) { // for each of our swarms
            nValidParticles += this.swarms.get(i).getValidParticles();
        }
        Log.debugMsg(String.format("Population avg (for %d swarms) GH: %.2f. (%d out of %d particles valid)",
                this.swarms.size(), (this.averageGH), nValidParticles, Defines.popSize));
//       if (this.isBestValid) {
//           System.out.println("Best valid chromosome of its generation:");
//           this.bestChromo.print();
//       }
    }

    public boolean isValidFound() {
        return this.isValidFound;
    }

    public double getMaxGH() {
        return this.maxGH;
    }

    /**
     * What's the best GH we've found in this collection of swarms?
     *
     * @return
     */
    public double getBestScore() {
        return this.getMaxGH();
    }

    public double getAverageGH() {
        return this.averageGH;
    }

    public Particle getBestParticle() {
        return this.bestParticle;
    }

    /**
     * Trigger swarm evaluation to track best particles.
     */
    public void evaluateSwarmGen() {
        //[TODO: require new validGroupCount >= old]
        this.totalGH = 0.0;
        for (Swarm swarm : swarms) {
            swarm.evaluateSwarm();
        }
        this.findBest();
        this.averageGH = this.totalGH / Defines.popSize;
    }

    /**
     * Identify the best in one swarm; update our best across all swarms.
     *
     * @param swarm
     */
    public void findBest() {
        double localGH;

        for (Swarm swarm : this.swarms) {
            for (Particle particle : swarm.getParticles()) {
                localGH = particle.getTotalGH();

                // update swarm global best score iff particle's fitness exceeds
                // prior global best, and particle's valid group count isn't smaller
                // than previous best particle's valid group count.
                if (localGH > this.getBestScore() && (this.getBestScore() == 0 || this.getBestParticle().getNumValidGroup() <= particle.getNumValidGroup())) {

//                if (localGH > this.getMaxGH()) {
                    String msg = "";
                    if (this.getBestParticle() != null) {
                        msg = String.format("Updating SwarmGen best: replace %.2f/%d with ", this.getBestScore(), this.getBestParticle().getNumValidGroup());
                    } else {
                        msg = "Initializing SwarmGen best with: ";
                    }

                    this.setMaxGH(localGH);
                    this.setBestParticle(particle);
                    this.isBestValid = particle.isValid();

                    msg += String.format("%.2f/%d\n", localGH, particle.getNumValidGroup());
                    System.out.println(msg);
                }

                this.totalGH += localGH;
            }

            this.output = this.toString();
        }
    }

    public String toString() {
        String str = "";
        final char DELIM = ';';

        for (Swarm swarm : this.swarms) {
            for (Particle particle : swarm.getParticles()) {

                double localGH = particle.getTotalGH();

                // Tack on a comma if it's needed - i.e., we're in mid-line
                if (str.length() > 0) {
                    if (DELIM != str.charAt(str.length() - 1)) {
                        str += ", ";
                    }
                }

                str += String.format("%.2f", localGH);
                str += "-" + particle.getNumValidGroup();

                if (particle.isValid()) {
                    str += "-VALID";
                    this.isValidFound = true;
                }

            }
            str += DELIM;
        }

        return str;

    }

    /**
     * Trigger particle movement within each swarm.
     */
    public void move() {
        for (Swarm swarm : swarms) {
            swarm.move();
        }

    }

    /**
     * Need to get all swarms for particle reallocation.
     *
     * @return
     */
    public ArrayList<Swarm> getSwarms() {
        return swarms;
    }

    /**
     * Get a single swarm
     *
     * @param i
     * @return
     */
    public Swarm getSwarm(int i) {
        if (i < this.swarms.size()) {
            return this.swarms.get(i);
        } else {
            return null;
        }
    }

    /**
     * Debug: show our swarms
     */
    public void dump() {
        int swcount = 0;
        System.out.format("* * * SwarmGen.dump(): %d swarms; top GH ever: %s/%.2f (%d);\n",
                this.swarms.size(), this.getBestParticle().toString(), this.getMaxGH(), this.getBestParticle().getNumValidGroup());
        for (Swarm swarm : this.swarms) {
            System.out.format("* * * * Swarm %d: size is %d; current best: %s/%.2f (%d)\n",
                    ++swcount, swarm.getParticles().size(), swarm.getBestParticle().toString(), swarm.getBestScore(), swarm.getBestParticle().getNumValidGroup());
        }
    }

    /**
     * @param maxGH the maxGH to set
     */
    public void setMaxGH(double maxGH) {
        this.maxGH = maxGH;
    }

    /**
     * @param bestParticle the bestParticle to set
     */
    public void setBestParticle(Particle bestParticle) {
        this.bestParticle = bestParticle;
    }

    /**
     * Collect all swarms into a single swarm. Sets the global swarms.
     */
    public void aggregateSwarms() {
        Swarm oneSwarm = new Swarm(this.swarms.get(0).getPool());

        for (Swarm swarm : this.swarms) {
            oneSwarm.addParticles(swarm.getParticles());
        }

        oneSwarm.evaluateSwarm();

        this.swarms = new ArrayList<Swarm>();
        this.swarms.add(oneSwarm);
        this.evaluateSwarmGen();

        Defines.swarmCount = 1;
        Defines.swarmSize = Defines.popSize;
    }

}
