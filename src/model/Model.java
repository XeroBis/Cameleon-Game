package model;

public class Model {

    private Plateau plateau;
    private QuadTree quadTree;
    int scoreBleu;
    int scoreRouge;
	public static char blanc = 'A', bleu = 'B', rouge = 'R';
    boolean isBrave;
    private int side;
    
    public Model(int n, boolean isBrave) {
    	this.scoreBleu = 0;
    	this.scoreRouge = 0;
    	this.isBrave = isBrave;
    	this.plateau = new Plateau(n);
    	if(!isBrave) {
    		this.quadTree = new QuadTree(n);
    	}
    	this.side = this.plateau.getTaille();
    }
    
    public int[] changeValue(char color, int x, int y) {
    	if(this.isBrave) {
    		return changeValueBrave( color,  x,  y);
    	} else {
    		return changeValueTemeraire(color, x, y);
    	}
    }
    
    public int[] changeValueBrave(char color, int x, int y) {
    	int[] score = new int[3];
    	score = this.plateau.changeValue(color, x, y, false);
    	if(score[0] == 0) {
    		int[][] scoreV = new int[8][3];
    		scoreV[0] = this.plateau.changeValue(color, x+1, y+1, true);
    		scoreV[1] = this.plateau.changeValue(color, x+1, y, true);
    		scoreV[2] = this.plateau.changeValue(color, x+1, y-1, true);
    		scoreV[3] = this.plateau.changeValue(color, x, y+1, true);
    		scoreV[4] = this.plateau.changeValue(color, x, y-1, true);
    		scoreV[5] = this.plateau.changeValue(color, x-1, y+1, true);
    		scoreV[6] = this.plateau.changeValue(color, x-1, y, true);
    		scoreV[7] = this.plateau.changeValue(color, x-1, y+1, true);
    		for(int i = 0; i < 8; i++) {
    			if(scoreV[i][0] == 0) {
    				score[1] += scoreV[i][1];
    				score[2] += scoreV[i][2];
    			}
    		}
    		return score;
    	} else {
    		score[0] = -1;
    		return score;
    	}
    }
    
    public int[] changeValueTemeraire(char color, int x, int y) {
    	int[] score = new int[3];
    	score = this.plateau.changeValue(color, x, y, false);
    	if(score[0] == 0) {
    		this.quadTree.addValue(x, y, false);
    		// tous les voisins :
    		int[][] scoreV = new int[8][3];
    		scoreV[0] = changeValueTemeraireVoisin(color, x+1, y+1);
    		scoreV[1] = changeValueTemeraireVoisin(color, x+1, y-1);
    		scoreV[2] = changeValueTemeraireVoisin(color, x+1, y);
    		scoreV[3] = changeValueTemeraireVoisin(color, x, y+1);
    		scoreV[4] = changeValueTemeraireVoisin(color, x, y-1);
    		scoreV[5] = changeValueTemeraireVoisin(color, x-1, y+1);
    		scoreV[6] = changeValueTemeraireVoisin(color, x-1, y);
    		scoreV[7] = changeValueTemeraireVoisin(color, x-1, y-1);
    		for(int i = 0; i < 8; i++) {
    			if(scoreV[i][0] == 0) {
    				score[1] += scoreV[i][1];
    				score[2] += scoreV[i][2];
    			}
    		}
    		return score;
    	}else {
    		score[0] = -1;
    		return score;
    	}
    }
    
    public int[] changeValueTemeraireVoisin(char color, int x, int y) {
    	int[] scoreV = new int[3];
    	if(this.quadTree.addValue(x,y, true) == 0) {
			scoreV = this.plateau.changeValue(color, x, y, true);
			if(scoreV[0] == 0) {
				this.quadTree.addValue(x,y, false);
			}
		}
    	return scoreV;
    }
    
    public int getSide() {
    	return this.side;
    }
    
    public void to_string() {
    	this.plateau.to_string();
    }
    
}
