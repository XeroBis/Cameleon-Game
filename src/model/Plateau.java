package model;

public class Plateau {
	
	
	private char[][] plateau;
	private int taille;
	public static char blanc = 'A', bleu = 'B', rouge = 'R';
	
	public Plateau(int n) {
		this.taille = 3*pow(2, n);
		this.plateau = new char[this.taille][this.taille];
		for(int i = 0; i < this.taille; i++) {
			for(int j = 0; j < this.taille; j++) {
				plateau[i][j] = blanc;
			}
		}
	}
	
	/**
     * Cette fonction retourne i à la puissance n
     *
     * @param i, le nombre a mettre à la puissance et n la puissance
     * @return i^n
     */
	private int pow(int i, int n) {
		int result = 1;
		for (int x = 0; x < n; x++) {
			result = result * i;
		}
		return result;
	}
	
	/**
     * Cette fonction dessine le tableau dans le terminal
     */
	public void to_string() {

		System.out.print("  ");
        if (this.taille > 9) {
        	System.out.print("");
        }
        for(int k = 0; k<this.taille; k++){
            System.out.print(" " + k + "");
        }
        System.out.println();
        for(int i = 0; i < this.taille; i++){
        	if(this.taille> 9 && i <= 9) {
        		System.out.print(" " +i + "[");
        	} else {
        		System.out.print(i + "[");
        	}
            for(int j = 0; j< this.taille; j++){
                System.out.print(" " + this.plateau[i][j]);
            }
            System.out.println(" ]");
        }
	}
	
	
	
	/**
     * Cette fonction change la valeur, si possible, se trouvant en x,y
     *
     * @param color , la couleur que l'on doit insérer
     * @param x, la coordonnées en x
     * @param y, la coordonnées en y
     * @return int, -1 si pas de changement, 0 si changement de couleur effectuée.
     */
	public int[] changeValue(char color, int x, int y, boolean voisin) {
		int[] res = new int[3];
		if(x<0 || y <0 || x >= this.taille || y >= this.taille) {
			res[0] = -1;
			return res;
		}
		System.out.print("x : " + x + ", y: " + y);
		if(this.plateau[x][y] == blanc && !voisin) {
			this.plateau[x][y] = color;
			if(color == rouge) {
				res[2] = 1;
			} else {
				res[1] = 1;
			}
			
			return res;
		} else if (this.plateau[x][y] != color && this.plateau[x][y] != blanc && voisin){
			this.plateau[x][y] = color;
			if(color == bleu) {
				res[1] = 1;
				res[2] = -1;
			} else {
				res[1] = -1;
				res[2] = 1;
			}
			res[0] = 0;
			return res;
		} else {
			res[0] = -1;
			return res;
		}
	}
	
	public int getTaille() {
		return this.taille;
	}
}

