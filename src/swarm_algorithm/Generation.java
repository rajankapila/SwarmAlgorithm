package swarm_algorithm;

import java.util.ArrayList;



public class Generation {
    private Swarm swarm;
    private boolean isValidFound = false;
    private double totalGH;
    private double maxGH;
    private double averageGH;
    private String output;
    private Particle bestParticle;
    private boolean isBestValid;
//    private Run run;
    
    
    public Generation (Swarm swarm) {
        this.isBestValid = false;
        this.bestParticle = null;
        this.swarm = swarm;
//        this.run = run;
        this.output = "";
        double localGH;
        this.totalGH = 0.0;
        this.maxGH = 0.0;
        
        this.swarm.sort();
        ArrayList<Particle> particles = this.swarm.getParticles();      
        for(int i = 0; i < particles.size(); i++) {
            localGH = particles.get(i).getTotalGH();
            if (localGH > this.maxGH) {
                this.maxGH = localGH;
                this.bestParticle = particles.get(i);
                this.isBestValid = this.bestParticle.isValid();
            }
            this.totalGH += localGH;
            
            this.output += String.format("%.2f",localGH);
            this.output += "-" + particles.get(i).getNumValidGroup();
            if(particles.get(i).isValid()) {
                this.output += "-VALID";
                this.isValidFound = true;              
            }
            if(i < particles.size() - 1) {
                this.output += ", ";
            }            
        }
        this.averageGH = this.totalGH / this.swarm.getParticles().size();
    }
    
   public void print() {
       
       
       Log.debugMsg(this.output);
       Log.debugMsg(String.format("Population avg GH: %.2f. (%d out of %d chromosomes valid)",
               (this.averageGH), this.swarm.getValidParticles(), Defines.popSize));
//       if (this.isBestValid) {
//           System.out.println("Best valid chromosome of its generation:");
//           this.bestChromo.print();
//       }
   }
   
   public boolean isValidFound() {
       return this.isValidFound;
   }

   public double getMaxGH() {
       return maxGH;
   }

   public double getAverageGH() {
       return averageGH;
   }
   
   public Particle getBestChromo() {
       return this.bestParticle;
   }
   
   
    
}
