package driver;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.rule.Variable;
 
public class RemoteDriver {
	
	static int port = 4321;
	static String host = "localhost";
	
    public static void main(String[] args) throws IOException {
    
        Socket kkSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        
        ArrayList<Double> xVal = new ArrayList<>();
        ArrayList<Double> yVal = new ArrayList<>();
        ArrayList<Double> angleVal = new ArrayList<>();
        ArrayList<Double> turnVal = new ArrayList<>();

        try {
            kkSocket = new Socket(host, port);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:"  + host);
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + host);
            e.printStackTrace();
            System.exit(1);
        }
 
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
 
        double x, y;
        double angle;
        
        // requisicao da posicao do caminhao
        out.println("r");
        while ((fromServer = in.readLine()) != null) {
        	StringTokenizer st = new StringTokenizer(fromServer);
        	x = Double.valueOf(st.nextToken()).doubleValue();
        	y = Double.valueOf(st.nextToken()).doubleValue();
        	angle = Double.valueOf(st.nextToken()).doubleValue();

//        	System.out.println("x: " + x + " y: " + y + " angle: " + angle);
        	
        	/////////////////////////////////////////////////////////////////////////////////////
        	// TODO sua lógica fuzzy vai aqui use os valores de x,y e angle obtidos. x e y estao em [0,1] e angulo [0,360)
        	
			FIS fis = FIS.load("src/Rules.fcl", true);
//        	FIS fis = FIS.load("/home/decio/Desktop/fuzzyTruckContest/src/remoteDriver/fuzzyTruck.fcl", true);

			fis.setVariable("x", x);
			fis.setVariable("y", y);
			fis.setVariable("actualAngle", angle);
			
			fis.evaluate();
			
			Variable angleVariable = fis.getVariable("turnAmount");

			xVal.add(x);
			yVal.add(y);
			angleVal.add(angle);
			turnVal.add(angleVariable.getValue());
			
        	double teste = angleVariable.getValue();
        	
        	double respostaDaSuaLogica = teste; // atribuir um valor entre -1 e 1 para virar o volante pra esquerda ou direita.
        	
        	///////////////////////////////////////////////////////////////////////////////// Acaba sua modificacao aqui
        	// envio da acao do volante
        	out.println(respostaDaSuaLogica);
        	
            // requisicao da posicao do caminhao        	
        	out.println("r");	
        }
 
        writeData(xVal.toString(), "x.txt");
        writeData(yVal.toString(), "y.txt");
        writeData(angleVal.toString(), "angle.txt");
        writeData(turnVal.toString(), "turn.txt");
        
        out.close();
        in.close();
        stdIn.close();
        kkSocket.close();
    }
    
    private static void writeData(String data, String fileName){
		try {
			File file = new File(fileName); 
			if (!file.exists()) {
				file.createNewFile();
			}
 
			BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
			data = data.substring(1, data.length()-1);
			bw.append(data);
			bw.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}