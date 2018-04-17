package swarm_algorithm;

import java.util.HashMap;

public class Run {
    private  String fileName;
    private  int numberOfElites;    
    private  int populationSize; 
    private  int maxGenerations; 
    private int dataPointFrequency;
    private  int numberOfParents; // for crossover operation
    private  int numberOfCrossoverPoints;
    private  double probabilityCrossover;
    private  double probabilityMutate;
    private  int paTournamentSize;    
    private  int parentAlgo;
    private  int cpMode;
    private int swarmCount;
    private double penalty;
    private int regroupPeriod;
    private double localTrialPct;
    private int trials;
    private double crossPoint1;
    private double crossPoint2;
    
    public Run(HashMap<String, Object> run) {
        this.setTrials(Integer.parseInt(run.get("trials").toString()));        
        this.setPopulationSize(Integer.parseInt(run.get("populationSize").toString()));        
        this.setMaxGenerations(Integer.parseInt(run.get("maxGenerations").toString()));
        this.setDataPointFrequency(Integer.parseInt(run.get("dataPointFrequency").toString()));
        this.setFileName((String)run.get("fileName"));
        this.setSwarmCount(Integer.parseInt(run.get("swarmCount").toString()));
        this.setRegroupPeriod(Integer.parseInt(run.get("regroupPeriod").toString()));
        this.setLocalTrialPct(Double.parseDouble(run.get("localTrialPct").toString()));  
        this.setCrossPoint1(Double.parseDouble(run.get("crosspt1").toString()));    
        this.setCrossPoint2(Double.parseDouble(run.get("crosspt2").toString()));  
        // The following are unused in PSO algorithm.
//        this.setNumberOfElites(Integer.parseInt(run.get("numberOfElites").toString()));
//        this.setNumberOfParents(Integer.parseInt(run.get("numberOfParents").toString()));
//        this.setNumberOfCrossoverPoints(Integer.parseInt(run.get("numberOfCrossoverPoints").toString()));
//        this.setProbabilityCrossover(Double.parseDouble(run.get("probabilityCrossover").toString()));
//        this.setProbabilityMutate(Double.parseDouble(run.get("probabilityMutate").toString()));   
//        this.setPaTournamentSize(Integer.parseInt(run.get("paTournamentSize").toString()));
//        this.setParentAlgo(Integer.parseInt(run.get("parentAlgo").toString()));
//        this.setCpMode(Integer.parseInt(run.get("cpMode").toString()));
       // this.setPenalty(Double.parseDouble(run.get("penalty").toString()));  
        
    }
    
  

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getNumberOfElites() {
        return numberOfElites;
    }

    public void setNumberOfElites(int numberOfElites) {
        this.numberOfElites = numberOfElites;
    }

    public int getPopulationSize() {
       
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;       
    }

    public int getMaxGenerations() {
        return maxGenerations;
    }

    public void setMaxGenerations(int maxGenerations) {
        this.maxGenerations = maxGenerations;
    }
    

    public int getDataPointFrequency() {
        return dataPointFrequency;
    }

    public void setDataPointFrequency(int dataPointFrequency) {
        this.dataPointFrequency = dataPointFrequency;
    }

    public int getNumberOfParents() {
        return numberOfParents;
    }

    public void setNumberOfParents(int numberOfParents) {
        this.numberOfParents = numberOfParents;
    }

    public int getNumberOfCrossoverPoints() {
        return numberOfCrossoverPoints;
    }

    public void setNumberOfCrossoverPoints(int numberOfCrossoverPoints) {
        this.numberOfCrossoverPoints = numberOfCrossoverPoints;
    }

    public double getProbabilityCrossover() {
        return probabilityCrossover;
    }

    public void setProbabilityCrossover(double probabilityCrossover) {
        this.probabilityCrossover = probabilityCrossover;
    }

    public double getProbabilityMutate() {
        return probabilityMutate;
    }

    public void setProbabilityMutate(double probabilityMutate) {
        this.probabilityMutate = probabilityMutate;
    }

    public int getPaTournamentSize() {
        return paTournamentSize;
    }

    public void setPaTournamentSize(int paTournamentSize) {
        this.paTournamentSize = paTournamentSize;
    }

    public  int getParentAlgo() {
        return parentAlgo;
    }

    public  void setParentAlgo(int parentAlgo) {
        this.parentAlgo = parentAlgo;
    }

    public  int getCpMode() {
        return cpMode;
    }

    public  void setCpMode(int cpMode) {
        this.cpMode = cpMode;
    }

    /**
     * @return the penalty
     */
    public double getPenalty() {
        return penalty;
    }

    /**
     * @param penalty the penalty to set
     */
    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    /**
     * @return the swarmCount
     */
    public int getSwarmCount() {
        return swarmCount;
    }

    /**
     * @param swarmCount the swarmCount to set
     */
    public void setSwarmCount(int swarmCount) {
        this.swarmCount = swarmCount;
    }

    /**
     * @return the regroupPeriod
     */
    public int getRegroupPeriod() {
        return regroupPeriod;
    }

    /**
     * @param regroupPeriod the regroupPeriod to set
     */
    private void setRegroupPeriod(int regroupPeriod) {
        this.regroupPeriod = regroupPeriod;
    }

    /**
     * @return the localTrialPct
     */
    public double getLocalTrialPct() {
        return localTrialPct;
    }

    /**
     * @param localTrialPct the localTrialPct to set
     */
    public void setLocalTrialPct(double localTrialPct) {
        this.localTrialPct = localTrialPct;
    }

    /**
     * @return the trials
     */
    public int getTrials() {
        return trials;
    }

    /**
     * @param trials the trials to set
     */
    public void setTrials(int trials) {
        this.trials = trials;
    }

    /**
     * @return the crossPoint1
     */
    public double getCrossPoint1() {
        return crossPoint1;
    }

    /**
     * @param crossPoint1 the crossPoint1 to set
     */
    public void setCrossPoint1(double crossPoint1) {
        this.crossPoint1 = crossPoint1;
    }

    /**
     * @return the crossPoint2
     */
    public double getCrossPoint2() {
        return crossPoint2;
    }

    /**
     * @param crossPoint2 the crossPoint2 to set
     */
    public void setCrossPoint2(double crossPoint2) {
        this.crossPoint2 = crossPoint2;
    }
    
}
