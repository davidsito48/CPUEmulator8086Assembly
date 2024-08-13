package backend;

import java.util.Scanner;

public class Procesador {

    private int AR;
    private int AC;
    private int PC;
    private int DR;
    private int IR;
    private int TR;
    private int OUTR;
    private int INPR;
    private int E;
    private int I;
    private String Instruccion;
    private int tiempo;
    private int M[];
    private int poscision;

    public Procesador(int Memoria[]) {
        M = Memoria;
    }

    public void step() {

        switch (tiempo) {

            case 0:
                AR = PC;
                Instruccion = null;
                poscision = 0;
                break;

            case 1:
                IR = M[AR];
                PC++;
                poscision = 1;
                break;

            case 2:
                String binario = Conversiones.intToBin(IR);
                AR = Integer.parseInt(binario.substring(4), 2);

                /////////////////////////////////////////////////
                if (binario.charAt(0) == '0') {
                    I = 0;
                } else {
                    I = 1;
                }

                /////////////////////////////////////////////////
                String hexadecimal = Conversiones.intToHex(IR);
                decodificacion(hexadecimal);
                poscision = 2;
                break;

            case 3:
                String hexadecimal1 = Conversiones.intToHex(IR);
                System.out.println(hexadecimal1);

                if ((hexadecimal1.charAt(0) == '7') || (hexadecimal1.charAt(0) == 'f')) {

                    if (I == 0) { //REGISTRO

                        switch (Instruccion) {

                            case "CLA":
                                AC = 0;
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "CLE":
                                E = 0;
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "CMA":
                                String binAC = Conversiones.intToBin(AC);
                                String cma = "";

                                for (int i = 0; i < binAC.length(); i++) {
                                    if (binAC.charAt(i) == '1') {
                                        cma = cma + "0";
                                    } else {
                                        cma = cma + "1";
                                    }
                                }
                                AC = Conversiones.binToInt(cma);
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "CME":

                                if (E == 0) {
                                    E = 1;
                                } else {
                                    E = 0;
                                }
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "CIR":

                                String cirAC = Conversiones.intToBin(AC);
                                String cirE = Integer.toString(E);
                                String preCIR = "";
                                String CIR = "";
                                preCIR = cirE + cirAC;
                                CIR = preCIR.substring(0,16);
                                E = Conversiones.binToInt(String.valueOf(preCIR.charAt(16)));
                                AC = Conversiones.binToInt(CIR);
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "CIL":
                                String cirAC2 = Conversiones.intToBin(AC);
                                String cirE2 = Integer.toString(E);
                                String preCIL = "";
                                String CIL = "";
                                preCIL = cirAC2 + cirE2;
                                CIL = preCIL.substring(1,17);
                                E = Conversiones.binToInt(String.valueOf(preCIL.charAt(0)));
                                AC = Conversiones.binToInt(CIL);
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "INC":
                                AC++;
                                tiempo = -1;
                                poscision = 3;
                                break;

                            case "HLT":
                                System.out.println("Programa terminado, deteniendo!");
                                tiempo = -1;
                                poscision = 3;
                                break;
                        }

                    } else { //ENTRADA/SALIDA

                        switch (Instruccion) {
                            case "INP":
                                AC = INPR;
                                tiempo = -1;
                                poscision = 3;
                                break;
                            case "OUT":
                                OUTR = AC;
                                tiempo = -1;
                                poscision = 3;
                                break;
                        }

                    }

                    //MEMORIA
                } else {
                    if (I == 0) { //DIRECTO
                        poscision = 3;
                    } else { //INDIRECTO

                        String binario1 = Conversiones.intToBin(M[AR]);
                        AR = Integer.parseInt(binario1.substring(4), 2);
                        poscision = 3;
                    }
                }

                break;

            case 4:
                switch (Instruccion) {

                    case "AND":
                    case "ADD":
                    case "LDA":
                    case "ISZ":
                        DR = M[AR];
                        poscision = 4;
                        break;

                    case "STA":
                        String acBin = Conversiones.intToBin(AC);
                        String subAC = acBin.substring(1);
                        int acFin = Conversiones.binToInt(subAC);

                        M[AR] = acFin;
                        tiempo = -1;
                        poscision = 4;
                        break;

                    case "BUN":
                        PC = AR;
                        tiempo = -1;
                        poscision = 4;
                        break;

                    case "BSA":
                        M[AR] = PC;
                        AR++;
                        poscision = 4;
                        break;
                }
                break;

            case 5:
                switch (Instruccion) {

                    case "AND":
                        String acBin = Conversiones.intToBin(AC);
                        String drBin = Conversiones.intToBin(DR);
                        String andOperator = "";

                        for (int i = 0; i < acBin.length(); i++) {
                            if (acBin.charAt(i) == '1' && drBin.charAt(i) == '1') {
                                andOperator = andOperator + "1";
                            } else {
                                andOperator = andOperator + "0";
                            }
                        }

                        AC = Conversiones.binToInt(andOperator);
                        tiempo = -1;
                        poscision = 5;
                        break;

                    case "ADD":
                        AC = AC + DR;
                        tiempo = -1;
                        poscision = 5;
                        break;

                    case "LDA":
                        AC = DR;
                        tiempo = -1;
                        poscision = 5;
                        break;

                    case "BSA":
                        PC = AR;
                        tiempo = -1;
                        poscision = 5;
                        break;

                    case "ISZ":
                        DR++;
                        poscision = 5;
                        break;
                }
                break;

            case 6:
                switch (Instruccion) {

                    case "ISZ":
                        String drBin = Conversiones.intToBin(DR);
                        String subDR = drBin.substring(1);
                        int drFin = Conversiones.binToInt(subDR);

                        M[AR] = drFin;

                        if (DR == 0) {
                            PC++;
                        }
                        tiempo = -1;
                        poscision = 6;
                        break;

                }
                break;
        }

        tiempo++;
    }

    @Override
    public String toString() {

        return "Estatus: (" + (tiempo - 1) + ")"
                + "\n INSTRUCCION: " + Instruccion
                + "\n AR = " + Conversiones.intToHex(AR).substring(1)
                + "\n PC = " + Conversiones.intToHex(PC).substring(1)
                + "\n DR = " + Conversiones.intToHex(DR)
                + "\n TR = " + Conversiones.intToHex(TR)
                + "\n IR = " + Conversiones.intToHex(IR)
                + "\n AC = " + Conversiones.intToHex(AC)
                + "\n OUTR = " + Conversiones.intToHex(OUTR).substring(2)
                + "\n INPR = " + Conversiones.intToHex(INPR).substring(2)
                + "\n E = " + E
                + "\n I = " + I;

    }

    private void decodificacion(String hexadecimal) {

        hexadecimal = hexadecimal.toUpperCase();

        switch (hexadecimal.charAt(0)) {

            case '0':
            case '8':
                Instruccion = "AND";
                break;

            case '1':
            case '9':
                Instruccion = "ADD";
                break;

            case '2':
            case 'A':
                Instruccion = "LDA";
                break;

            case '3':
            case 'B':
                Instruccion = "STA";
                break;

            case '4':
            case 'C':
                Instruccion = "BUN";
                break;

            case '5':
            case 'D':
                Instruccion = "BSA";
                break;

            case '6':
            case 'E':
                Instruccion = "ISZ";
                break;

        }

        switch (hexadecimal) {

            case "7800":
                Instruccion = "CLA";
                break;

            case "7400":
                Instruccion = "CLE";
                break;

            case "7200":
                Instruccion = "CMA";
                break;

            case "7100":
                Instruccion = "CME";
                break;

            case "7080":
                Instruccion = "CIR";
                break;

            case "7040":
                Instruccion = "CIL";
                break;

            case "7020":
                Instruccion = "INC";
                break;

            case "7010":
                Instruccion = "SPA";
                break;

            case "7008":
                Instruccion = "SNA";
                break;

            case "7004":
                Instruccion = "SZA";
                break;

            case "7002":
                Instruccion = "SZE";
                break;

            case "7001":
                Instruccion = "HLT";
                break;

            case "F800":
                Instruccion = "INP";
                break;

            case "F400":
                Instruccion = "OUT";
                break;

            case "F200":
                Instruccion = "SKI";
                break;

            case "F100":
                Instruccion = "SKO";
                break;

            case "F080":
                Instruccion = "ION";
                break;

            case "F040":
                Instruccion = "IOF";
                break;
        }

    }

    public int getAC() {
        return AC;
    }

    public int getAR() {
        return AR;
    }

    public int getPC() {
        return PC;
    }

    public int getDR() {
        return DR;
    }

    public int getIR() {
        return IR;
    }

    public int getTR() {
        return TR;
    }

    public int getOUTR() {
        return OUTR;
    }

    public int getINPR() {
        return INPR;
    }

    public int getE() {
        return E;
    }

    public int getI() {
        return I;
    }

    public String getInstruccion() {
        return Instruccion;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setINPR(int INPR) {
        this.INPR = INPR;
    }

    public int getPoscision() {
        return poscision;
    }

}
