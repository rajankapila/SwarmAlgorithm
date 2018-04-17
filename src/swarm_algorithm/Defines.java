package swarm_algorithm;

public class Defines {

    // Constraints set in problem declaration
    public static final Double MIN_DISTANCE = 2.0;
    public static final Double MIN_GH = 0.5;
    public static final Integer GROUP_SIZE = 4;
    public static final Integer NUMBER_OF_ATTRIBUTES = 7;

    
    //PSO config
    public static double c1 = 0.5;
    public static double c2 = 0.7;
    
    // Parent selection algorithms
    public static final int PA_RANDOM = 0;
    public static final int PA_ROULETTE = 1;
    public static final int PA_TOURNAMENT = 2;

    // Crossover point selection parameters
    public static final int CP_PURE_RANDOM = 0;
    public static final int CP_NO_BOUNDARY = 1;

    

    // Program parameters
    // [TODO: set these through args?]
//    public static int populationSize;
    public static int runGenerations;
    public static int crossoverParentCt; // for crossover operation
    public static int crossoverPoints;
//    public static final double PROB_CROSSOVER = 0.8;
//    public static final double PROB_MUTATE = 0.05;
    public static int eliteCt; //Elite parent count    
    public static int paTournamentSize;
    public static int parentAlgo = PA_TOURNAMENT;
    public static int cpMode = CP_PURE_RANDOM;
    public static double chromoInvPenalty;
    public static boolean useInvPenalty;
    public static double probCrossover;
    public static double probMutate;
    public static int popSize;
    public static int tournamentSize;
    
    public static int dataPointFrequency;//Statistics
    
    public static Integer particleSize;
    public static Integer totalGroups;
    
    // for multi-swarm architecture
    public static int swarmCount;
    public static int swarmSize;
    public static int regroupPeriod;
    public static double localTrialPct;
    public static int trials;

    public static void setDefaults() {
        runGenerations = 100000;
        crossoverParentCt = 2; // for crossover operation
        crossoverPoints = 2;
        paTournamentSize = 5;
        parentAlgo = PA_TOURNAMENT;
        cpMode = CP_PURE_RANDOM;
        chromoInvPenalty = 0.5;
        useInvPenalty = true;
        probCrossover = 0.8;
        probMutate = 0.05;
        popSize = 10;
        tournamentSize = 5;
        eliteCt = 2;
        swarmCount = 1;
        swarmSize = popSize / swarmCount;
        regroupPeriod = 5;
        localTrialPct = 0.9;
        
        dataPointFrequency = 1000;
        trials = 1;
    
        c1 = 0.5;
        c2 = 0.7;
    }

    /**
     * Return a random number in the range [rangeLow, rangeHigh].
     *
     * @param rangeLow Lowest number we can return
     * @param rangeHigh Highest number we can return.
     * @return A random number in the range [rangeLow, rangeHigh].
     */
    public static Integer randNum(int rangeLow, int rangeHigh) {
        return (int) Math.floor(Math.random() * (rangeHigh - rangeLow + 1)) + rangeLow;
    }

}
