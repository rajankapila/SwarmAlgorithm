package swarm_algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Parser {
    public void parse(String fileName) throws IOException {
        DataReader reader = new DataReader(fileName); // TODO: use args to set this
        BufferedReader br = new BufferedReader (new FileReader(fileName));
        String[] d = null;
        StringTokenizer columnsToken = null;
        int lineNumber = 0, tokenNumber = 0;
        int columnCount = 0;
        String line = "";
        PrintWriter writer, writer2;
        HashMap<String, HashMap<String, String>> data = new HashMap<String, HashMap<String, String>>();
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter("output_new.csv", false)));
            writer2 = new PrintWriter(new BufferedWriter(new FileWriter("output_new2.csv", false)));
            String output = "";
            String o = "";
           
            while(!line.equalsIgnoreCase("Best Overall GH")) {
                HashMap<String, String> t = new HashMap<String, String>();
                if(line.isEmpty()) {
                    output += br.readLine() + this.newline();
                } else {
                    output += line + this.newline();
                }
                line = br.readLine();
                String[] b = line.split(",");
                String key = "";
                String popSize = b[1];                
                for(int i = 0; i < b.length; i++) {
                    if(i != 1) {
                        key += i;
                        if(i != b.length -1) {
                           key += "-";
                        }
                    }
                }
                
                output += line + this.newline();
                output += br.readLine() + this.newline();
                output += br.readLine() + this.newline();
                output += br.readLine() + this.newline();
                
                line = br.readLine();//generations                
                String[] a = line.split(",");
                for(int i = 0; i < a.length; i++) {
                    if(i == 0) {
                        output += a[i] + ", ";
                    }
                    if((i >0 && (Long.parseLong(a[i].trim()) % 1000 == 0) || i == (a.length-1) || i == 1)) {
                        output += a[i] + ", ";
                    }
                }            
                output += this.newline();
                
                line = br.readLine();//max gh
                a = line.split(",");
                for(int i = 0; i < a.length; i++) {
                    if(i == 0) {
                        output += a[i] + ", ";
                    }
                    if((i >0 && (((i + 1) % 1000 == 0)) || i == (a.length-1) || i == 1)) {
                        output += a[i] + ", ";
                    }
                }
                output += this.newline();
                line = br.readLine();//average gh
                a = line.split(",");
                for(int i = 0; i < a.length; i++) {
                    if(i == 0) {
                        output += a[i] + ", ";
                    }
                    if((i >0 && (((i + 1) % 1000 == 0)) || i == (a.length-1) || i == 1)) {
                        output += a[i] + ", ";
                    }
                }
                output += this.newline();
                output += br.readLine() + this.newline();
                output += br.readLine() +  this.newline();
                line = br.readLine();
                a = line.split(",");
                for(int i = 0; i < a.length; i++) {
                    
                    output += a[i] + ",";
                    
                }
                output += this.newline();
                line = br.readLine();//generation count
                a = line.split(",");
                for(int i = 0; i < a.length; i++) {
                    
                    
                    output += a[i] + ",";
                    
                }
                output +=  this.newline();
                line = br.readLine();
               
            }
            output += line +  this.newline();
            output += br.readLine() + this.newline();
            output += br.readLine() + this.newline();
            output += br.readLine() + this.newline();
            output += br.readLine() + this.newline();
            output += br.readLine() + this.newline();
            writer.print(output);
            writer.close();
        
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    private String newline() {
        return "\n";
    }
}
