package model;

import static java.lang.Math.pow;

public class Plateau {
    int[][] tableau;
    public static int bleu = 1, rouge = 2, blanc = 0;
    private final int taille;
    private int side;

    Plateau(int n){
        this.taille = n;
        this.tableau = new int[(int) (3*pow(2,n))][(int) (3*pow(2,n))];
        this.side = (int) (3*pow(2, n));
        for(int i =0; i < 3 * pow(2,n); i++){
            for (int j = 0; j< 3*pow(2,n); j++){
                this.tableau[i][j] = 0;
            }
        }
    }
    /**
     * This method print in the terminal the array containing value of 0, 1 or 2
     * we also show index for column and line
     */
    public void to_string(){
        System.out.print("  ");
        for(int k = 0; k<3*pow(2, this.taille); k++){
            System.out.print(" " + k + "");
        }
        System.out.println();
        for(int i = 0; i < 3 * pow(2,this.taille); i++){
            System.out.print(i + "[");
            for(int j = 0; j< 3*pow(2,this.taille); j++){
                System.out.print(" " +this.tableau[i][j]);
            }
            System.out.println(" ]");

        }
    }

    /**
     * This method change the color of certain case in array with the color given in parameter
     * it retrun int so we can know what error it gave.
     *
     * return value :
     * -2 :  already coloried by the color given in parameter
     * -1 : already coloried by the other color than the one give in parameter
     *  0 : we change the color of the emplacement given with the color
     *  1 : can't be coloried because x and y given in parameter are bigger that the array or smaller than 0
     *  2 : can't be coloried beacause x is bigger than array or smaller than 0
     *  3 : can't be coloried because y is bigger than array or smaller than 0
     *
     * @param color, la couleur du joueur
     * @param  x  coordinate x of desired new point
     * @param  y coordinate y of desired new point
     * @param voisin boolean, si l'appel se fait pour colorier un voisin
     * @return -2, -1, 0, 1, 2 or 3 : error value
     */
    int[] BraveChangeValue(int color, int x, int y, boolean voisin){
        int[] res = new int[3];
        if ((x > tableau.length-1 && y > tableau.length-1)|| (x< 0 && y < 0)){
            res[0] = 1;
            return res;
        }
        if (x> tableau.length-1|| x< 0){
            res[0] = 2;
            return res;
        }
        if (y > tableau.length-1 || y < 0){
            res[0] = 3;
            return res;
        }
        if (voisin && tableau[x][y] != color && tableau[x][y] != blanc) {
            tableau[x][y] = color;
            res[color] +=1;
            if(color == bleu){
                res[rouge]--;
            }else {
                res[bleu]--;
            }
            return res;
        }
        if (tableau[x][y] == blanc && !voisin){
            res[color] ++;
            tableau[x][y] = color;
            return res;
        } else if (tableau[x][y] == color) {
            res[0] = -2;
            return res;
        } else {
            res[0]= -1;
            return res;
        }
    }

    int[] BraveChangeValue(int color, int x, int y){
        int[] res = BraveChangeValue(color,x, y, false);
        if(res[0] !=0){
            return res;
        }else {
            int[] voisin = BraveChangeValueVoisin(color, x, y);
            res[1] += voisin[1];
            res[2] += voisin[2];
        }
        return res;
    }
    /**
     * This method change the color of every voisin, if possible, with the color given in parameter
     *
     * @param  x  coordinate x of desired new point
     * @param  y coordinate y of desired new point
     * @param color, la couleur du joueur
     */
    int[] BraveChangeValueVoisin(int color, int x, int y){
        int[] res = new int[3];
        int[][] res0 = new int[9][3];
        res0[0] = BraveChangeValue(color, x-1, y-1, true);
        res0[1] = BraveChangeValue(color, x-1, y, true);
        res0[2] = BraveChangeValue(color, x-1, y+1, true);
        res0[3] = BraveChangeValue(color, x, y-1, true);
        res0[4] = BraveChangeValue(color, x, y+1, true);
        res0[5] = BraveChangeValue(color, x+1, y-1, true);
        res0[6] = BraveChangeValue(color, x+1, y, true);
        res0[7] = BraveChangeValue(color, x+1, y+1, true);
        for (int i = 0; i < 8; i++) {
            if(res0[i][0]==0){
                res[1] += res0[i][1];
                res[2] += res0[i][2];
            }
        }
        return res;
    }

    int getTaille(){
        return this.taille;
    }
    int getSide(){
        return this.side;
    }
}

