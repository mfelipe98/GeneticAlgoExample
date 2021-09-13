import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class Main {

    //Valid Genes
    static final String GENES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ 1234567890";

    //Target string
    static final String TARGET = "Testing Testing 123";

    static final int POPULATION_SIZE = 100;

    //Function to generate random int in a range
    public static int randomInt(int min, int max){
        return (int)Math.floor(Math.random()*(max-min+1)+min);
    }

    //Create random genes for mutation
    public static char mutatedGenes(){
        int len = GENES.length();
        int r = randomInt(0, len-1);
        return GENES.charAt(r);
    }

    public static String createGnome(){
        int len = TARGET.length();
        String gnome = "";
        for (int i=0; i<len; i++){
            gnome += mutatedGenes();
        }
        return gnome;
    }

    public static void main(String[] args){
        //current generation
        int generation = 0;

        ArrayList<Individual> population = new ArrayList<Individual>();
        boolean found = false;

        //create initial population
        for (int i=0; i<POPULATION_SIZE; i++){
            String gnome = createGnome();
            population.add(new Individual(gnome));
        }

        while(!found){
            //sort the population in ascending order of fitness
            Collections.sort(population);

            //check fitness of first individual; if 0, there is a match
            if(population.get(0).fitness <= 0){
                found = true;
                break;
            }

            //if not, generate new generation of offspring
            ArrayList<Individual> newGeneration = new ArrayList<Individual>();

            //perform elitism: top 10% goes to next generation
            int s = (10*POPULATION_SIZE)/100;
            for(int i=0; i<s; i++){
                newGeneration.add(population.get(i));
            }

            //from 50% of fittest population, individuals will mate to produce offspring
            s = (90*POPULATION_SIZE)/100;
            for(int i=0; i<s; i++){
                int len = population.size();
                int r = randomInt(0,50);
                Individual parent1 = population.get(r);
                r = randomInt(0,50);
                Individual parent2 = population.get(r);
                Individual offspring = parent1.mate(parent2);
                newGeneration.add(offspring);
            }
            population = newGeneration;
            System.out.print("Generation: " + generation + "\t");
            System.out.print("String: " + population.get(0).chromosome + "\t");
            System.out.println("Fitness: " + population.get(0).fitness + "\t");

            generation++;

        }
        System.out.print("Generation: " + generation + "\t");
        System.out.print("String: " + population.get(0).chromosome + "\t");
        System.out.println("Fitness: " + population.get(0).fitness + "\t");
    }


}

//Class representing an individual in a population
class Individual implements Comparable {
    String chromosome;
    int fitness;

    //Constructor class
    public Individual(String chromosome){
        this.chromosome = chromosome;
        fitness = calFitness();
    }

    //Create child from 2 parents
    public Individual mate(Individual par2){

        //chromosome of child
        String child_chromosome = "";

        int len = chromosome.length();
        for (int i = 0; i < len; i++){
            //generate a random probability
            double p = Math.random();

            //if p<0.45, insert gene from parent 1
            if(p<0.45) child_chromosome += chromosome.charAt(i);

            //if 0.45<=p<0.9, insert gene from parent 2
            else if(p<0.9) child_chromosome += par2.chromosome.charAt(i);

            //otherwise insert random gene from mutation to maintain diversity
            else child_chromosome += Main.mutatedGenes();
        }
        return new Individual(child_chromosome);
    }

    public int calFitness(){
        int len = Main.TARGET.length();
        int fitness = 0;
        for (int i = 0; i < len; i++){
            if(chromosome.charAt(i) != Main.TARGET.charAt(i)) fitness++;
        }
        return fitness;
    }


    @Override
    public int compareTo(Object o) {
        int comp = ((Individual)o).fitness;
        return this.fitness - comp;
    }
}
