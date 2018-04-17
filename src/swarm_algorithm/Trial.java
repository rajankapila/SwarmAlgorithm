/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package swarm_algorithm;

/**
 *
 * @author paul
 */
public class Trial {
    private Particle bestParticle;
    private double bestGH;
    private Run run;
    
    Trial(Run r) {
        this.run = r;
    }

    /**
     * @return the bestParticle
     */
    public Particle getBestParticle() {
        return bestParticle;
    }

    /**
     * @param bestParticle the bestParticle to set
     */
    public void setBestParticle(Particle bestParticle) {
        this.bestParticle = bestParticle;
    }

    /**
     * @return the bestGH
     */
    public double getBestGH() {
        return bestGH;
    }

    /**
     * @param bestGH the bestGH to set
     */
    public void setBestGH(double bestGH) {
        this.bestGH = bestGH;
    }

    /**
     * @return the run
     */
    public Run getRun() {
        return run;
    }

    /**
     * @param run the run to set
     */
    public void setRun(Run run) {
        this.run = run;
    }
    
    
    
}
