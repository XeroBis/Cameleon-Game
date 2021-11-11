package model;

public class QuadTree { // utiliser pour gérer les régions.

	private int side;
	private int n;
	private boolean isFull;
	private int nbCaseFull;
	public static char blanc = 'A', bleu = 'B', rouge = 'R';
	private char color;
	private QuadTree[] sons;
	private QuadTree father;

	public QuadTree(int n) {
		this.isFull = false;
		this.nbCaseFull = 0;
		this.color = blanc;
		this.father = null;
		this.sons = null;
		if (n == 0) {
			this.side = 3;
		} else {
			this.side = (int) (6 * this.pow(2, n - 1));
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
	 * Cette fonction sert à savoir si l'on peut ajouter une couleur dans certaines coordonnés.
	 * Ajoute ou non une valeur dans une région selon les coordonnées x et y et si test est a vrai on n'ajoute
	 * pas de valeur
	 *
	 * @param x 
	 * @param y 
	 * @param test, si à vrai pas d'ajout seulement test
	 * @return int, -1 si full et 0 si ajout possible.
	 */
	public int addValue(int x, int y, boolean test) {
		if (this.testIfFull()) {
			return -1;
		} else {
			if (this.side == 3) {
				if(!test) {
					this.nbCaseFull++;
				}
				return 0;
			} else {
				if(this.n == 0) {
					return -1;
				}
				if (this.sons == null) {
					this.sons = new QuadTree[4];
				}
				if (x < this.side / 2 && y < this.side / 2) { // coin haut gauche
					if (this.sons[0] == null) {
						this.sons[0] = new QuadTree(this.n - 1);
					}
					System.out.println("test taille n :" + this.n);
					sons[0].addValue(x, y, test);

				} else if (x < this.side / 2 && y >= this.side / 2) {// coint haut droit
					if (this.sons[1] == null) {
						this.sons[1] = new QuadTree(this.n - 1);
					}
					sons[1].addValue(x, y, test);
				} else if (x >= this.side / 2 && y < this.side / 2) { // coin bas gauche
					if (this.sons[2] == null) {
						this.sons[2] = new QuadTree(this.n - 1);
					}
					sons[2].addValue(x, y, test);
				} else if (x >= this.side / 2 && y >= this.side / 2) { // coin bas gauche
					if (this.sons[3] == null) {
						this.sons[3] = new QuadTree(this.n - 1);
					}
					sons[3].addValue(x, y, test);
				}
			}
		}

		return 0;
	}

	
	
	/**
	 * fonction qui test si un QuadTree est plein
	 *
	 * @return boolean si le QuadTree actuel est plein (modifie aussi la valeur de isFull)
	 */
	private boolean testIfFull() {
		if (this.nbCaseFull == this.side * this.side) {
			this.isFull = true;
			this.father.nbCaseFull++;
			this.father.testIfFull();
			return true;
		} else {
			this.isFull = false;
			return false;
		}
	}

	public int getSide() {
		return this.side;
	}
}
