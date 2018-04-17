package swarm_algorithm;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class main {

    static final String RUN_FILE = "runs-pso-trials.txt"; // run control params
    static final String INPUT_FILE = "input.txt"; // data

    private static ArrayList<Swarm> generations = new ArrayList<Swarm>();
    private static ArrayList<Run> runs = new ArrayList<Run>();
    private static MemberPool pool;
    private Trial trial;
    private String trialOutput;

    public static void main(String[] args) throws Exception {
        main app = new main();
        app.loadRuns(RUN_FILE);
        app.execute(INPUT_FILE);
        wake_up_user(5, 300);
    }

    private static void wake_up_user(int rings, long delay) {
        for (int i = 0; i < rings; i++) {
            java.awt.Toolkit.getDefaultToolkit().beep();
            try {
                Thread.sleep(delay);
            } catch (Exception e) {

            }
        }
    }

    public void execute(String inputFile) {
//        testParmSets(); System.exit(1);

        final int MODE_EXPLORE = 0; // Uses ParmSet 
        final int MODE_DIRECTED = 1; // Uses global: runs
        int mode = MODE_DIRECTED;
        ArrayList<Trial> trials = new ArrayList<Trial>();
        trialOutput = "";

        // Collect all student genes into pool - do this only once. Must use 
        // same data for meaningful comparison? 
        pool = loadStudentData(inputFile);
//        app.testChromosomesValidity();

        int runNum = 1;
//        int trials = 10;
        switch (mode) {
            case MODE_DIRECTED:

                if (runs != null) {
                    for (Run run : runs) { // each row in run file creates one Run in runs
                        setRunParms(run);
                        System.out.println("--------------------------------------------------");
                        System.out.format("Run #%d starts at %s\n", runNum++, DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));
                        reportSettings();
                        System.out.println("\nRunning " + run.getTrials() + " trials with these settings.");
                        for (int i = 0; i < run.getTrials(); i++) {
                            trial = new Trial(run);
                            System.out.println("Trial #" + (i + 1));
                            run(pool);
                            trials.add(trial);
                        }
                        reportTrials(trials, run);
                    }
                }
                writeTrialOutput();
                break;

            case MODE_EXPLORE:

                ParmSet parms = new ParmSet();
                while (!parms.isDone()) {
                    setRunParms(parms);
                    System.out.println("--------------------------------------------------");
                    System.out.format("Run #%d starts at %s\n", runNum++, DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));
                    reportSettings();
                    run(pool);
                }

                break;

        }
        Statistics.outputOverallBest();

        System.out.format("Finished at %s.\n", DateFormat.getTimeInstance(DateFormat.MEDIUM).format(new Date()));

    }

    /**
     * Sets operational parameters - read from file
     *
     * @param run Input file data with run parameters
     */
    public void setRunParms(Run run) {
        Defines.runGenerations = run.getMaxGenerations();
        Defines.popSize = run.getPopulationSize();
        Defines.dataPointFrequency = run.getDataPointFrequency();
        Defines.swarmCount = run.getSwarmCount();
        Defines.swarmSize = Defines.popSize / Defines.swarmCount;
        Defines.regroupPeriod = run.getRegroupPeriod();
        Defines.localTrialPct = run.getLocalTrialPct();
        Defines.trials = run.getTrials();
        Defines.c1 = run.getCrossPoint1();
        Defines.c2 = run.getCrossPoint2();
        // The following are not used with PSO algorithm.
//        Defines.probCrossover = run.getProbabilityCrossover();
//        Defines.probMutate = run.getProbabilityMutate();
//        Defines.parentAlgo = run.getParentAlgo();
//        Defines.tournamentSize = run.getPaTournamentSize();
//        Defines.cpMode = run.getCpMode();
//        Defines.eliteCt = run.getNumberOfElites();
//        Defines.crossoverPoints = run.getNumberOfCrossoverPoints();
//        Defines.chromoInvPenalty = run.getPenalty();
//        Defines.useInvPenalty = true;
//        Defines.crossoverParentCt = 2; // works with our crossover operator
    }

    /**
     * Sets operational parameters - from a ParmSet object
     *
     * @param Parmset parms a parameter generator
     */
    private void setRunParms(ParmSet parms) {
        parms.getNextParmSet();
        Defines.popSize = parms.getPopSize();
        Defines.runGenerations = parms.getGenerations();
        Defines.swarmCount = parms.getSwarmCount();
        Defines.swarmSize = Defines.popSize / Defines.swarmCount;
        // The following are not used in PSO algorithm.
//        Defines.probCrossover = parms.getPCross();
//        Defines.probMutate = parms.getPMutate();
//        Defines.parentAlgo = parms.getSelectionModels();
//        Defines.tournamentSize = parms.getTournamentSize();
//        Defines.cpMode = parms.getCPointModels();
//        Defines.eliteCt = 2; // not implemented in ParmSet
//        Defines.chromoInvPenalty = parms.getPenalty();
//        Defines.useInvPenalty = parms.getPenaltyModel();
//        Defines.crossoverParentCt = 2; // works with our crossover operator

        Defines.dataPointFrequency = 1000; // not implemented in ParmSet - report data each dpf/1000 generations
        Defines.crossoverPoints = 2; // not implemented in ParmSet - works with CO operator
        System.out.println(parms.toString());
    }

    /**
     * testing iteration through parmsets
     */
    public void testParmSets() {
        ParmSet parms = new ParmSet();
        while (!parms.isDone()) {
            System.out.println(parms.toString());
            parms.getNextParmSet();
        }
    }

    /**
     * Sets operational parameters
     *
     * @param i
     */
    private void loadParmSet6() {
        Defines.runGenerations = 10000;
        Defines.useInvPenalty = true;
        Defines.chromoInvPenalty = 0.1;
        Defines.parentAlgo = Defines.PA_RANDOM;
        Defines.tournamentSize = 0;
        Defines.probCrossover = 0.600000;
        Defines.probMutate = 0.010000;
        Defines.popSize = 50;
        Defines.cpMode = Defines.CP_NO_BOUNDARY;
        System.out.format("Pop=%d Pc=%f Pm=%f Gens=%d XPtMod=%s SelMod=%s PenMod=%s\n",
                Defines.popSize,
                Defines.probCrossover,
                Defines.probMutate,
                Defines.runGenerations,
                Defines.cpMode == Defines.CP_NO_BOUNDARY ? "nobds" : "rand",
                Defines.parentAlgo == Defines.PA_RANDOM ? "rand" : (Defines.parentAlgo == Defines.PA_ROULETTE ? "roul" : "tour(" + Defines.tournamentSize + ")"),
                Defines.useInvPenalty ? "Pen(" + Defines.chromoInvPenalty + ")" : "NoPen"
        );
    }

    /**
     * This method is for testing; checks to see how often randomly updated
     * chromosomes are valid.
     */
    public void testChromosomesValidity() {
        // Collect all student genes into pool
        MemberPool pool = loadStudentData("input.txt");

        ParticleGenerator particleGenerator = new ParticleGenerator(pool);

        // update chromosomes
        int chromoValid = 0;
        int chromoInvalid = 0;
        int nTests = 10000;
//        for (int i = 0; i < nTests; i++) {
//            //generates a new chromosome randomly from seed data
//            Chromosome chromosome = chromosomeGenerator.update("random");
//            if (chromosome.isValid()) {
//                chromoValid++;
//            } else {
//                chromoInvalid++;
//            }
//        }        
//        System.out.format("Over %d trials, valid chromosomes are randomly generated at a rate of %f%%.\n", nTests, (chromoValid / nTests * 100.0));
//        System.out.format("%d successes, %d failures in %d trials.\n", chromoValid, chromoInvalid, nTests);
        int totalTrials = 0;
        int chromoReqd = 1;
        Log.debugMsg("Starting random chromosome generation, lets see how long this takes");
        Timer.start();
        while (chromoValid < chromoReqd) {
            totalTrials++;

            //generates a new chromosome randomly from seed data
            Particle particle = particleGenerator.generate("random");
            if (particle.isValid()) {
                chromoValid++;
            } else {
                chromoInvalid++;
            }
            if (chromoInvalid % 10000 == 0) {
                Log.debugMsg(chromoInvalid + " invalid chromosomes to date.");
            }
        }
        System.out.format("To generate %d valid chromosomes, %d trials were required.\n", chromoReqd, totalTrials);
        System.out.format("Successful chromosome generation rate: %f\n", ((double) chromoValid / (double) totalTrials));
        Timer.end();
    }

    /**
     * This is the program executive.
     *
     * @param run
     * @param pool
     */
    public void run(MemberPool pool) {

        double bestScore = 0.0;
        Particle bestParticle = null;

        Statistics statistics = new Statistics(Defines.dataPointFrequency);

        ParticleGenerator particleGenerator = new ParticleGenerator(pool);
//        Swarm swarm = new Swarm(pool);
        ArrayList<Swarm> swarms = new ArrayList<Swarm>();

        // update initial population
//        for (int i = 0; i < Defines.popSize; i++) {
//            //generates a new chromosome randomly from seed data
//            Particle particle = particleGenerator.generate("random");
//            swarm.addParticle(particle);
//        }
        SwarmGen generation = new SwarmGen();
        // update initial population
        // assign random particles to swarms (multi-swarm architecture)
        for (int i = 0; i < Defines.swarmCount; i++) {
            Swarm swarm = new Swarm(pool);
            for (int j = 0; j < Defines.swarmSize; j++) {
                swarm.addParticle(particleGenerator.generate("random"));
            }
            generation.add(swarm);
//            swarms.add(swarm);
        }

//        Generation generation = new Generation(swarm);
        boolean done = false;
        boolean localMovePhase = true;
        int generationCount = 1;

        generation.evaluateSwarmGen(); // tell the population to identify best chromosomes.
        statistics.addGeneration(generation);
        statistics.addMaxScoreCount(generation.getMaxGH()); // are we tracking best chromo?
//        generation.print();

        // Iterate through building a new population for each generation:
        // 1) Promote elite chromosomes from old pop to new pop
        // 2) Build new chromosomes for new pop by repeating until new pop is full:
        //    2.1) Identify parent(s)
        //    2.2) Crossover parents
        //    2.3) Mutate parents
        //    2.4) Add offspring to new pop
        // 3) Evaluate the new population
        // 4) Evaluate progress across generations - converging? improving?
        while (!done) {

            //newSwarm.addParticles(swarm.getEliteParticles());
//            swarm.resetValidParticles();
            // Choose parent(s)
            //swarm.selectParents();
            // Generate new swarm position
//            swarm.move();
            // For the first part of operation (Defines.localTrialPct of
            // total generations, we move locally within each of several
            // swarms. Then, we move into the finishing phase where all
            // particles move within a single global swarm.
            if (localMovePhase && generationCount > Defines.localTrialPct * Defines.runGenerations) {
                // We aggregateSwarms all swarms into a single swarm for the wrapup
                generation.aggregateSwarms();
                localMovePhase = false;
                System.out.format("## Reached generation %d; switching to global swarm.\n", generationCount);
                reportSettings();
            }
            generation.move();

            // How good is the new pop?
//            swarm.evaluateSwarm();
            generation.evaluateSwarmGen();

            // track best results so far - redundant with Generations?
//            if (swarm.getBestScore() > bestScore) {
//                bestScore = swarm.getBestScore();
//                bestParticle = swarm.getBestParticle();
//            }
            if (generation.getBestScore() > bestScore) {
                bestScore = generation.getBestScore();
                bestParticle = generation.getBestParticle();
                System.out.format("New high score----------= %.2f (valid groups: %d; gens: %d)\n", bestScore, bestParticle.getNumValidGroup(), generationCount);
            }

            // Shuffle particles every now and then...
            if (generationCount % Defines.regroupPeriod == 0) {
                generation = this.redistribute(generation);
            }

//            generation = new Generation(swarm);
            statistics.addMaxScoreCount(generation.getMaxGH());

            if (generationCount % Defines.dataPointFrequency == 0) {
                Log.debugMsg("Generation=" + generationCount);
                generation.print();
                statistics.addGeneration(generation);
            }

            generationCount++;
            done = generationCount > Defines.runGenerations;

        }
        try {
            statistics.output();
        } catch (FileNotFoundException e) {
            Log.debugMsg("Output? File Not Found");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.format("Best particle encountered scored: %f\n", bestScore);
        if (bestParticle != null) {
            if (bestParticle.isValid()) {
                bestParticle.printGroups();
            } else {
                System.out.println("...but it isn't valid.");
            }
        }
        System.out.println("==================================================\n");
        trial.setBestGH(bestScore);
        trial.setBestParticle(bestParticle);

    }

    /**
     * Periodically shuffle particles into different swarms.
     *
     * @param swarms
     * @return
     */
    private SwarmGen redistribute(SwarmGen gen) {
//        gen.dump();

        // first, collect all particles from all swarms in gen
        ArrayList<Particle> particles = new ArrayList<Particle>();
        for (Swarm swarm : gen.getSwarms()) {
            particles.addAll((ArrayList<Particle>) swarm.getParticles());
        }

        SwarmGen newGen = new SwarmGen();

        // assign random particles to swarms (multi-swarm architecture)
        for (int i = 0; i < Defines.swarmCount; i++) {
            Swarm swarm = new Swarm(pool);

            // Get random particles from population
            for (int j = 0; j < Defines.swarmSize; j++) {
                int pos = Defines.randNum(0, (particles.size() - 1));
                Particle particle = particles.remove(pos);
                swarm.addParticle(particle);
            }

            // Remember (positionally) corresponding swarm's best so far
            Particle particle = gen.getSwarm(i).getBestParticle();
            swarm.setBestParticle(particle);
            if (particle != null) {
                swarm.setBestScore(particle.getTotalGH());
            }

            newGen.add(swarm);
            newGen.setBestParticle(gen.getBestParticle());
            newGen.setMaxGH(gen.getMaxGH());
            newGen.evaluateSwarmGen();
//            swarms.add(swarm);
        }

//        newGen.dump();
        return newGen;
    }

    private void matchChildAndParent(ArrayList<Particle> offspring, Swarm population) {
        int counter = 0;
        if (offspring != null) {
            ArrayList<Particle> parents = population.getParents();
            for (int i = 0; i < parents.size(); i++) {
                for (int j = 0; j < offspring.size(); j++) {
                    if (parents.get(i).equivTo(offspring.get(j))) {
                        counter++;
                    }
                }
            }
        }
        System.out.format("%d matches between offspring (of %d) matched some parent (pop=%d).\n",
                counter, offspring.size(), Defines.popSize);
    }

    public void chromosomeValidityReport(Particle chromosome) {
        Log.debugMsg("Chromosome is " + (chromosome.isValid() ? "" : " not ") + "valid.");
    }

    /**
     * Grabs student data from input file and stores it in the pool. Each
     * student is a gene.
     */
    public MemberPool loadStudentData(String fileName) {
        MemberPool pool = new MemberPool();
        DataReader reader = new DataReader(fileName); // TODO: use args to set this
        reader.readFile(",");
        HashMap<String, ArrayList<Integer>> data = reader.getData();
        Iterator it = data.entrySet().iterator();

        int geneCount = 0;

        while (it.hasNext()) {

            Map.Entry studentGeneData = (Map.Entry) it.next();

            //add genes to the pool based on the id and array of scores
            // studentID => studentInfoArray
            pool.addGene(new Member((String) studentGeneData.getKey(), (ArrayList<Integer>) studentGeneData.getValue()));

            geneCount++;

        }
        Defines.particleSize = geneCount;
        Defines.totalGroups = Defines.particleSize / Defines.GROUP_SIZE;
        return pool;
    }

    /**
     * Loads parameter file. File contains a list of comma-separated parameter :
     * value pairs. E.g. maxGenerations : 100000, ....
     *
     * @param fileName
     * @throws Exception
     */
    public void loadRuns(String fileName) throws Exception {
        DataReader reader = new DataReader(fileName); // TODO: use args to set this
        ArrayList<HashMap<String, Object>> runsList = reader.readRunsFile(",");

        if (runsList != null && runsList.size() > 0) {
            for (int i = 0; i < runsList.size(); i++) {
                Run run = new Run(runsList.get(i));
                runs.add(run);
            }
        } else {
            throw new Exception("no runs file");
        }
    }

    public void report(Run run) {

        System.out.format("Population:\t%d\n", run.getPopulationSize());
        System.out.format("Prob crossover:\t%f\n", run.getProbabilityCrossover());
        System.out.format("Prob mutation:\t%f\n", run.getProbabilityMutate());
        System.out.format("Generations:\t%d\n", run.getMaxGenerations());
        System.out.format("Chromo size:\t%d\n", Defines.particleSize);
        System.out.format("Crossover point selection:\t%s\n", run.getCpMode() == Defines.CP_NO_BOUNDARY ? "No group boundary points" : "Unconstrained random");
        System.out.format("Number of crossover points:\t%d\n", run.getNumberOfCrossoverPoints());
        System.out.format("Parent selection:\t%s\n", run.getParentAlgo() == Defines.PA_RANDOM ? "Random"
                : (run.getParentAlgo() == Defines.PA_ROULETTE ? "" + "Roulette" : "Tournament" + " (" + Defines.paTournamentSize + ")"));

    }

    public void reportSettings() {
        System.out.println("+=========================");
        System.out.println("Run settings: ");
        System.out.format("Population:       %d\n", Defines.popSize);
        System.out.format("Particle size:    %d\n", Defines.particleSize);
        System.out.format("Generations:      %d\n", Defines.runGenerations);
        System.out.format("Swarm count:      %d\n", Defines.swarmCount);
        System.out.format("Swarm size:       %d\n", Defines.swarmSize);
        System.out.format("Local gens:       %.2f%%\n", Defines.localTrialPct * 100);
        System.out.format("Crossover probs:  c1=%.2f, c2=%.2f\n", Defines.c1, Defines.c2);
        System.out.format("Regroup period:   %d\n", Defines.regroupPeriod);
        // The following are not used in PSO algorithm.
//        System.out.format("Prob crossover:\t%f\n", Defines.probCrossover);
//        System.out.format("Prob mutation:\t%f\n", Defines.probMutate);
//        System.out.format("Crossover point selection:\t%s\n", Defines.cpMode == Defines.CP_NO_BOUNDARY ? "No group boundary points" : "Unconstrained random");
//        System.out.format("Number of crossover points:\t%d\n", Defines.crossoverPoints);
//        System.out.format("Parent selection:\t%s\n", Defines.parentAlgo == Defines.PA_RANDOM ? "Random"
//                : (Defines.parentAlgo == Defines.PA_ROULETTE ? "" + "Roulette" : "Tournament" + " (" + Defines.tournamentSize + ")"));
//        System.out.format("Penalize invalid chromo:\t%s\n", Defines.useInvPenalty ? "Yes (" + Defines.chromoInvPenalty + ")" : "No");

        System.out.println("+-------------------------");
    }

    public void reportTrials(ArrayList<Trial> trials, Run run) {
        double totalGH = 0.0;
        double avgGH = 0.0;

        for (Trial trial : trials) {
            totalGH += trial.getBestGH();
        }

        avgGH = totalGH / trials.size();

        trialOutput += String.format("%.2f average best score over %d trials [Pop: %d; Gens: %d; Swarms: %d; SwarmSize: %d; Local %%: %.2f, Regroup: %d, c1/c2: %.2f/%.2f]\n",
                avgGH,
                run.getTrials(),
                run.getPopulationSize(),
                run.getMaxGenerations(),
                run.getSwarmCount(),
                run.getPopulationSize() / run.getSwarmCount(),
                run.getLocalTrialPct(),
                Defines.regroupPeriod,
                Defines.c1,
                Defines.c2);
    }

    public void writeTrialOutput() {

        PrintWriter writer;
        String fileName = "trials_" + Timer.getTime() + ".txt";

        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            writer.print(trialOutput);
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
