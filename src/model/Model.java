package model;

import java.util.ArrayList;

public class Model {
	// **************************************** Attributs et Constructeurs de la classe Model **************************************** //

	private Plateau plateau; // le plateau de jeu.
	private QuadTree quadTree; // l'arbre pour les r�gions.
	private int size; // la taille du cot�.

	private ArrayList<Point> redPoints, bluePoints; // 2 tableau qui contiennent les points qui peuvent �tre pris.
	private int redScore, blueScore; // les deux ints des scores.
	private boolean isGloutonne;
	private boolean isBrave;
	

	/*
	 * @purpose, construit le model
	 */
	public Model() {
		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();

		this.redScore = 0;
		this.blueScore = 0;
		this.isGloutonne = true;
		this.isBrave = true;
	}
	/*
	 * @purpose, construit le model
	 * @param k, le k de 3 * 2^k
	 */
	public Model(int k) {
		this.setNewK(k);
		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();
		this.redScore = 0;
		this.blueScore = 0;
		this.isGloutonne = true;
		this.isBrave = true;
	}

	/*
	 * @purpose, construit les attributs de Model, c�d le quadTree et le plateau selon le k
	 * @param k, le k de 3 * 2^k
	 * @complexity 
	 */
	public void setNewK(int k) {
		this.plateau = new Plateau(k);
		this.size = 3 * (int) Math.pow(2, k);
		this.quadTree = buildingQT(null, k, new Point(0, 0), 3 * (int) Math.pow(2, k));

	}
	/*
	 * @purpose Construit tout les files d'un quadTree selon k et P le point haut gauche de ce m�me quadTree
	 * @param father, le quadTree p�re
	 * @param k, le k de l'�quation size = 3 * 2^k
	 * @param size
	 * @complexity O(4^k)
	 */
	private QuadTree buildingQT(QuadTree father, int k, Point p, int size) {
		if (k == 0) {
			return new QuadTree(father, true, QuadTree.blanc, p, null, null, null, null, size);
		} else {
			int dim = (int) (3 * Math.pow(2, k));

			Point p1 = new Point(p.getx() + dim / 2, p.gety());
			Point p2 = new Point(p.getx() + dim / 2, p.gety() + dim / 2);
			Point p3 = new Point(p.getx(), p.gety() + dim / 2);

			return new QuadTree(null, false, QuadTree.blanc, p, buildingQT(father, k - 1, p, size / 2),
					buildingQT(father, k - 1, p1, size / 2), buildingQT(father, k - 1, p2, size / 2),
					buildingQT(father, k - 1, p3, size/2), size);
		}
	}

	// **************************************** Fonctions pour le mode de jeu "Brave" ****************************************//
	
	
	/*
	 * @purpose change la couleur du tableau d'une case selon les r�gles BRAVE du jeu du cam�l�on
	 * @param ligne, col les coordon�es du point � r�colorier
	 * @param couleur, la couleur du point � colorier
	 * @complexity O(1)
	 */
	public boolean colorationBrave(int ligne, int col, int couleur) {
		if (plateau.couleurCase(ligne, col) != 0 || ligne < 0 || ligne >= size || col < 0 || col >= size) {
			return false;
		} else {
			coloration(ligne, col, couleur);
			recolorationBrave(ligne, col, couleur);

			return true;
		}
	}
	/*
	 * @purpose change la couleur des voisins de la case situ�e en ligne, col suivant les r�gles
	 * @param ligne et col, les coordon�es du point 
	 * @param couleur, la couleur de la case que l'on � recolori�
	 * @complexity O(1)
	 */
	public void recolorationBrave(int ligne, int col, int couleur) {
		for (int i = ligne - 1; i < ligne + 2; i++) {
			if (i >= 0 && i < this.size) {
				for (int j = col - 1; j < col + 2; j++) {
					if (j >= 0 && j < this.size && plateau.couleurCase(i, j) != 0) {
						coloration(i, j, couleur);
					}
				}
			}
		}
	}

	/*
	 * @purpose retourne le meilleur point � jouer selon les r�gles de jeu BRAVE !
	 * @param couleur, la couleur que l'on veut jouer
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public Point EvalCaseBrave(int color) {
		ArrayList<Point> mvp = new ArrayList<Point>();
		Point p = null;
		int max = 0;
		ArrayList<Point> Points;
		if (color == 1) {
			Points = this.redPoints;
		} else {
			Points = this.bluePoints;
		}

		for (int k = 0; k < Points.size(); k++) {
			p = Points.get(k);
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						int nb = nbOpponentColorBrave(p.gety() + i, p.getx() + j, color);
						if (nb > max && plateau.couleurCase(p.gety() + i, p.getx() + j) != -1
								&& plateau.couleurCase(p.gety() + i, p.getx() + j) == 0) {
							mvp.clear();
							max = nb;
							mvp.add(new Point(p.getx() + j, p.gety() + i));
						} else if (nb == max && plateau.couleurCase(p.gety() + i, p.getx() + j) != -1
								&& plateau.couleurCase(p.gety() + i, p.getx() + j) == 0) {
							mvp.add(new Point(p.getx() + j, p.gety() + i));
						}
					}
				}
			}
		}
		if (mvp.isEmpty()) {
			p = new Point((int) (Math.random() * this.size), (int) (Math.random() * this.size));
		} else {
			p = mvp.get((int) (Math.random() * mvp.size()));
		}
		return p;
	}
	
	/*
	 * @purpose Joue le tour du bot Glouton Brave
	 * @param couleur, la couleur que l'on veut jouer
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public void JouerGloutonBrave(int color) {
		actualizingArrayPointsBrave();
		Point p = EvalCaseBrave(color);
		colorationBrave(p.gety(), p.getx(), color);
		actualizingArrayPointsBrave();
	}

	/*
	 * @purpose Actualise les tableaux des points attaquables 
	 * @complexity O(n), n �tant le nombre de points stok�es dans ses deux tableaux
	 */
	public void actualizingArrayPointsBrave() {
		Point p;
		int size = this.redPoints.size();
		for (int i = 0; i < size; i++) {
			p = this.redPoints.get(i);
			if (!hasFreeNeighborBrave(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == Plateau.bleu) {
				this.redPoints.remove(i);
				size--;
				i--;
			}
		}

		size = this.bluePoints.size();
		for (int i = 0; i < size; i++) {
			p = this.bluePoints.get(i);
			if (!hasFreeNeighborBrave(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == Plateau.rouge) {
				this.bluePoints.remove(i);
				size--;
				i--;
			}
		}
	}

	/*
	 * @purpose Retourne le nombre d'ennemie autour de la case en ligne, col
	 * @param ligne, col les coord du points que l'on veut tester.
	 * @param couleur, la couleur que l'on veut jouer
	 * @complexity O(1)
	 */
	public int nbOpponentColorBrave(int ligne, int col, int couleur) {
		int nb = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((plateau.couleurCase(ligne + i, col + j) != -1)
						&& (plateau.couleurCase(ligne + i, col + j) != couleur)
						&& (plateau.couleurCase(ligne + i, col + j) != 0)) {
					nb++;
				}
			}
		}
		return nb;
	}

	/*
	 * @purpose d�termine si la case � une case voisine qui est blanche
	 * @param ligne, col
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public boolean hasFreeNeighborBrave(int ligne, int col) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (plateau.couleurCase(ligne + i, col + j) == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	// **************************************** Fonctions pour le mode de jeu "T�m�raire" **************************************** //

	
	/*
	 * @purpose colorie le plateau de la couleur en suivant les r�gles du mode de jeu TEMERAIRE
	 * @param ligne, col, les coordon�es du point � colorier
	 * @param couleur, la couleur dont il faut recolorier la case
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public boolean colorationTemeraire(int ligne, int col, int couleur) {
		if (plateau.couleurCase(ligne, col) != this.plateau.blanc) {
			//System.out.println("Mouvement Interdit !");
			return false;
		} else {
			coloration(ligne, col, couleur); // R1
			recolorationTemeraire(ligne, col, couleur); // R2

			int size = 3;
			Point p = getTopLeftPointOfRegion(ligne, col, size);
			
			boolean isRegionFull = isRegionFull(p.gety(), p.getx(), size);

			while (isRegionFull) {
				if (size == this.size) {
					this.RemplirRegion(p.gety(), p.getx(), couleur, this.quadTree, size);
					isRegionFull = false;
				} else {
					
					// appel une fonction pour r��crir le plateau et l'arbre
					this.RemplirRegion(p.gety(), p.getx(), couleur, this.quadTree, size);
					
					// p = point  angle haut gauche de la plus grande r�gions
					size = size * 2;
					p = this.getTopLeftPointOfRegion(ligne, col, size);
					
					isRegionFull = isRegionFull(p.gety(), p.getx(), size); // on test si la r�gion au dessus est pleine
				}

			}
			this.CalculeScore();

		}
		return true;
	}
	
	/*
	 * @purpose permet de r�cup�rer les coordon�es du coin haut gauche de 
	 * la r�gion dont la taille est size et donc la r�gion contient le point de coordon�es ligne, col
	 * @param ligne, col, les coordon�es d'un point quelconque dans le plateau
	 * @param size, la taille de la r�gion d'on l'on veut le point haut gauche
	 * @return Point haut gauche de la r�gion
	 * @complexity O(1)
	 */
	public Point getTopLeftPointOfRegion(int ligne, int col, int size) {
		int x = col - (col % size);
		int y = ligne - (ligne % size);
		return new Point(x, y);
	}

	/*
	 * @purpose recolorie les cases voisines selon les r�gles du mode de jeu TEMERAIRE
	 * @param ligne, col
	 * @param couleur
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public void recolorationTemeraire(int ligne, int col, int couleur) {
		for (int i = ligne - 1; i < ligne + 2; i++) {
			if (i >= 0 && i < this.size) {
				for (int j = col - 1; j < col + 2; j++) {
					if ((j >= 0) && (j < this.size) && (plateau.couleurCase(i, j) != 0)
							&& (isNotLock(i, j, this.quadTree))) {
						coloration(i, j, couleur);
					}
				}
			}
		}
	}

	/*
	 * @purpose Fonction permettant de savoir si un point de coordonn�es ligne, col et locked, c'est � dire si la r�gion � laquelle il appartient est compl�te
	 * @param ligne, col les coordonn�es du point
	 * @param qt, le quadTree p�re de la r�gion
	 * @return boolean, si le point est dans une r�gion qui est d�j� captur�
	 * @complexity O(n), n �tant dimension max du quadTree
	 */
	public boolean isNotLock(int ligne, int col, QuadTree qt) {
		if (qt.getisSterile()) {
			return qt.getValue() == QuadTree.blanc;
		} else if (qt.getValue() != QuadTree.blanc) {
			return false;
		} else {
			if (col >= qt.getQt(2).getPoint().getx()) {
				if (ligne >= qt.getQt(2).getPoint().gety()) {
					return isNotLock(ligne, col, qt.getQt(2));
				} else {
					return isNotLock(ligne, col, qt.getQt(1));
				}
			} else {
				if (ligne >= qt.getQt(2).getPoint().gety()) {
					return isNotLock(ligne, col, qt.getQt(3));
				} else {
					return isNotLock(ligne, col, qt.getQt(0));
				}
			}
		}
	}

	/*
	 * @purpose permet la capture des zones et donc du changement de valeur dans le tableau de cases.
	 * @param ligne, col les coordon�es du point
	 * @param quadTree, le quadTree courant
	 * @param size, la taille du quadTree courant
	 * @complexity O(n^2), n �tant size
	 */
	public void RemplirRegion(int ligne, int col, int couleur, QuadTree quadTree, int size) {
		if (quadTree != null) {
			if (quadTree.getisSterile()) {
				if (this.isRegionFull(ligne, col, size)) {
					if (this.hasMorePiece(ligne, col, couleur, size)) {
						quadTree.setValue(couleur);
						recoloringRegionPlateau(ligne, col, couleur, size);
					} else {
						quadTree.setValue(this.getOppositeColor(couleur));
						recoloringRegionPlateau(ligne, col, this.getOppositeColor(couleur), size);
					}
					quadTree.setIsSterile(true);
					quadTree.deleteSons();
				}
			} else {
				if (col >= quadTree.getQt(2).getPoint().getx()) {
					if (ligne >= this.quadTree.getQt(2).getPoint().gety()) {
						this.RemplirRegion(ligne, col, couleur, quadTree.getQt(2), size);
					} else {
						this.RemplirRegion(ligne, col, couleur, quadTree.getQt(1), size);
					}
				} else {
					if (ligne >= quadTree.getQt(2).getPoint().gety()) {
						this.RemplirRegion(ligne, col, couleur, quadTree.getQt(3), size);
					} else {
						this.RemplirRegion(ligne, col, couleur, quadTree.getQt(0), size);
					}
				}

			}
		}
	}
	
	
	/*
	 * @purpose d�termine si la couleur est dominante dans une r�gion (si �galit� est consid�r� dominante)
	 * @param ligne, col les coordon�es du point
	 * @param couleur, la couleur courante
	 * @param size, la taille de la r�gion
	 * return boolean, vraie si couleur dominante, faux sinon
	 * @complexity O(n^2), n �tant size 
	 */
	public boolean hasMorePiece(int ligne, int col, int couleur, int size) {
		int bleu = 0;
		int rouge = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (ligne + i < this.size && col + j < this.size) {
					int color = this.plateau.couleurCase(ligne + i, col + j);
					if (color == Plateau.rouge) {
						rouge++;
					} else if (color == Plateau.bleu) {
						bleu++;
					}
				}
			}
		}

		if (couleur == Plateau.rouge) {
			if (bleu > rouge) {
				return false;
			} else {
				return true;
			}
		} else {
			if (rouge > bleu) {
				return false;
			} else {
				return true;
			}
		}

	}

	/*
	 * @purpose permet de savoir si une region est pleine
	 * @return boolean si la r�gion est pleine
	 * @complexity O(n^2), n �tant size
	 */
	public boolean isRegionFull(int ligne, int col, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (plateau.couleurCase(ligne + i, col + j) == Plateau.blanc) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * @purpose recolorie le plateau de la coordonn�e ligne, col sur un carr� de size x size
	 * @param ligne, col les coordon�es du point haut gauche de la r�gion, 
	 * @param size la taille de la r�gion que l'on doit recolorier
	 * @param couleur, la couleur utilis� pour le recoloriage
	 * @complexity O(n^2), n �tant size
	 */
	public void recoloringRegionPlateau(int ligne, int col, int couleur, int size) {
		
		if(this.quadTree.getValue(ligne, col) != couleur) {
			
		}
		
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (ligne + i < this.size && col + j < this.size) {
					plateau.changerValeur(ligne + i, col + j, couleur);
				}
			}
		}
	}

	/*
	 * @purpose permet de r�cup�rer le meilleur move en terme de diff�rence de point avec l'adversaire
	 * @param color, la couleur que l'on veut tester
	 * @return Point, le meilleur point actuel (qui fait gagner le plus de point)
	 * @complexity O(n^?)
	 */
	public Point EvalCaseTemeraire(int color) {
		this.actualizingArrayPointsTemeraire();
		ArrayList<Point> mvp = new ArrayList<Point>();
		Point p = null;
		int max = 0;
		int nbZoneMax = 0;

		ArrayList<Point> Points = redPoints;
		Points.addAll(bluePoints);
		for (int k = 0; k < this.redPoints.size(); k++) {
			p = this.redPoints.get(k);
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						if (plateau.couleurCase(p.gety() + i, p.getx() + j) == this.plateau.blanc) {
							int nb = nbOpponentColorBrave(p.gety() + i, p.getx() + j, color);
							int nbZone = getNumberOfZoneTaken(p.gety() + i, p.getx() + j, color);

							if (nbZone > nbZoneMax) {
								nbZoneMax = nbZone;
								mvp.clear();
								mvp.add(new Point(p.getx() + j, p.gety() + i));
							} else if (nbZone == nbZoneMax && nbZoneMax != 0) {
								mvp.add(new Point(p.getx() + j, p.gety() + i));

							} else if (nb > max && nbZoneMax == 0) {
								mvp.clear();
								mvp.add(new Point(p.getx() + j, p.gety() + i));
								max = nb;
							} else if (nb == max && nbZoneMax == 0) {
								mvp.add(new Point(p.getx() + j, p.gety() + i));
							}
						}
					}
				}
			}
		}

		for (int k = 0; k < this.bluePoints.size(); k++) {
			p = this.bluePoints.get(k);
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						if (plateau.couleurCase(p.gety() + i, p.getx() + j) == this.plateau.blanc) {
							int nb = nbOpponentColorBrave(p.gety() + i, p.getx() + j, color);
							int nbZone = getNumberOfZoneTaken(p.gety() + i, p.getx() + j, color);

							if (nbZone > nbZoneMax) {
								nbZoneMax = nbZone;
								mvp.clear();
								mvp.add(new Point(p.getx() + j, p.gety() + i));
							} else if (nbZone == nbZoneMax && nbZoneMax != 0) {
								mvp.add(new Point(p.getx() + j, p.gety() + i));

							} else if (nb > max && nbZoneMax == 0) {
								mvp.clear();
								mvp.add(new Point(p.getx() + j, p.gety() + i));
								max = nb;
							} else if (nb == max && nbZoneMax == 0) {
								mvp.add(new Point(p.getx() + j, p.gety() + i));
							}
						}
					}
				}

			}
		}
		if (mvp.isEmpty()) {
			p = new Point((int) (Math.random() * this.size), (int) (Math.random() * this.size));
		} else {
			p = mvp.get((int) (Math.random() * mvp.size()));
		}
		return p;
	}

	/*
	 * @purpose d�termine le nombre d'ennemie capturable autour d'une case
	 * @param ligne, col les coord de la case que l'on teste
	 * @param couleur, la couleur actuelle
	 * @complexity O(1)
	 */
	public int nbOpponentColorTemeraire(int ligne, int col, int couleur) {
		int nb = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if ((plateau.couleurCase(ligne + i, col + j) != -1)
						&& (plateau.couleurCase(ligne + i, col + j) != couleur)
						&& (plateau.couleurCase(ligne + i, col + j) != 0) && isNotLock(ligne + i, col + j, quadTree)) {
					nb++;
				}
			}
		}
		return nb;
	}

	/*
	 * @purpose Joue le coup du bot du glouton en suivant les r�gles du mode de jeu TEMERAIRE
	 * @param color, la couleur courante
	 * @complexity O(n)
	 */
	public void JouerGloutonTemeraire(int color) {
		this.actualizingArrayPointsTemeraire();
		Point bestMove = EvalCaseTemeraire(color);
		this.colorationTemeraire(bestMove.gety(), bestMove.getx(), color);
		this.CalculeScore();
		this.actualizingArrayPointsTemeraire();
	}

	/*
	 * @purpose calcul le score actuel de la partie
	 * @param ligne, col
	 * @complexity O(n^2), n �tant la taille du cot� du plateau
	 */
	public void CalculeScore() {
		int scoreBleu = 0;
		int scoreRouge = 0;

		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (this.plateau.couleurCase(i, j) == this.plateau.rouge) {
					scoreRouge++;
				} else if (this.plateau.couleurCase(i, j) == this.plateau.bleu) {
					scoreBleu++;
				}
			}
		}
		this.redScore = scoreRouge;
		this.blueScore = scoreBleu;
	}

	/*
	 * @purpose d�termine le nombre de zone captur� en prenant un point 
	 * @param ligne, col les coordonn�es du point
	 * @param color, la couleur courante
	 * @return int, le nombre de zone captur�es
	 * @complexity O(n) ?
	 */
	public int getNumberOfZoneTaken(int ligne, int col, int color) {
		// en premier on test si les coord donn�es remplisse une partie
		QuadTree t = this.quadTree;
		boolean descendu = false;
		int etage = 1;
		while (!descendu) {
			if (!t.getisSterile()) {
				if (ligne < (this.size) / (2 * etage) && col < (this.size) / (2 * etage)) {
					t = t.getQt(0);

				} else if (ligne >= (this.size / (2 * etage)) && col < (this.size) / (2 * etage)) {
					t = t.getQt(1);

				} else if (ligne >= (this.size / (2 * etage)) && col >= (this.size) / (2 * etage)) {
					t = t.getQt(2);

				} else if (ligne < (this.size / (2 * etage)) && col >= (this.size) / (2 * etage)) {
					t = t.getQt(3);
				}
				etage++;
			} else {
				descendu = true;
			}
		}
		if (this.getNumberOfCaseColoried(t.getPoint().getx(), t.getPoint().gety()) == 8) {
			return TestIfGetBiggerZone(t);
		} else {
			return 0;
		}
	}

	
	/*
	 * @purpose d�termine si la r�gion p�re de la zone dans laquel le point se trouve est captur� si cette m�me r�gion est captur�e
	 * @param son, le quadTree courant
	 * @return int, le nombre de petite zone captur� 
	 * @complexity O(n), n �tant la dimension maximum du QuadTree de notre partie
	 */
	public int TestIfGetBiggerZone(QuadTree son) {
		if (son.getFather() == null) {
			return 1;
		}
		QuadTree father = son.getFather();
		int nbZone = 0;
		for (int i = 0; i <= 3; i++) {
			if (father.getQt(i).getValue() != this.plateau.blanc) {
				nbZone++;
			}
		}
		if (nbZone == 3) { // on peut colorier une grande zone, donc on test le p�re
			if (father.getFather() == null) {
				return 4;
			} else {
				return 4 * TestIfGetBiggerZone(father);
			}
		} else {
			return 1; // si on ne capture pas la grande zone alors on a capturer seulement une petite
						// zone.
		}

	}

	/*
	 * @purpose retourne le nombre de case colori�e autour d'une case
	 * @param ligne, col, les coordonn�es de la case 
	 * @return int, le nombre de case colori�e
	 * @complexity O(1)
	 */
	public int getNumberOfCaseColoried(int ligne, int col) {
		int caseColorie = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (plateau.couleurCase(ligne + i, col + j) != this.plateau.blanc) {
					caseColorie++;
				}
			}
		}
		return caseColorie;
	}
	
	/*
	 * @purpose actualise les tableaux bleuPoints et redSize
	 * @complexity O(n), n �tant la somme de la taille de bluePoints ainsi que de la taille de RedPoints
	 */
	public void actualizingArrayPointsTemeraire() {
		Point p;
		int size = this.redPoints.size();
		for (int i = 0; i < size; i++) {
			p = this.redPoints.get(i);
			if (!hasFreeNeighborBrave(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == Plateau.bleu
					|| !isNotLock(p.gety(), p.getx(), quadTree)) {
				this.redPoints.remove(i);
				size--;
				i--;
			}
		}

		size = this.bluePoints.size();
		for (int i = 0; i < size; i++) {
			p = this.bluePoints.get(i);
			if (!hasFreeNeighborBrave(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == Plateau.rouge
					|| !isNotLock(p.gety(), p.getx(), quadTree)) {
				this.bluePoints.remove(i);
				size--;
				i--;
			}
		}
	}


	// **************************************** Fonctions g�n�rales ****************************************//

	/*
	 * @purpose colorie une case // seulement utilis� si on charge un fichier
	 * @param ligne, col, les coordon�es de la case
	 * @param couleur, la couleur de la case
	 * @complexity 
	 */
	public void RemplirTableau(int ligne, int col, int couleur) {
		this.coloration(ligne, col, couleur);
	}

	/*
	 * @purpose modifie completement le QuadTree selon les modifications faite sur le plateau
	 * @param quadTree, pour la r�cursivit�
	 * @complexity O(n)
	 */
	public void RemplirQuadTree(QuadTree quadTree) {
		Point p = quadTree.getPoint();
		if (this.isRegionFull(p.gety(), p.getx(), quadTree.getSize())) {
			quadTree.setValue(this.plateau.couleurCase(p.gety(), p.getx()));
			quadTree.setIsSterile(true);
			quadTree.deleteSons();
		} else {
			if (!quadTree.getisSterile()) {
				RemplirQuadTree(quadTree.getQt(0));
				RemplirQuadTree(quadTree.getQt(1));
				RemplirQuadTree(quadTree.getQt(2));
				RemplirQuadTree(quadTree.getQt(3));
			}
		}
	}
	
	/*
	 * @purpose retourne la couleur oppos�e
	 * @param couleur
	 * @return int, la couleur opposs�e
	 * @complexity O(n), n �tant le nombre de case ennemi pouvant �tre capturer
	 */
	public int getOppositeColor(int couleur) {
		if (couleur == Plateau.rouge) {
			return Plateau.bleu;
		} else {
			return Plateau.rouge;
		}
	}
	
	/*
	 * @purpose colorie le plateau
	 * @param ligne, col
	 * @complexity O(1)
	 */
	public void coloration(int ligne, int col, int couleur) {
		if (couleur == 1 && this.plateau.couleurCase(ligne, col) == 2) {
			this.redScore--;
			this.blueScore++;
		} else if (couleur == 2 && this.plateau.couleurCase(ligne, col) == 1) {
			this.redScore++;
			this.blueScore--;
		} else if (couleur == 1 && this.plateau.couleurCase(ligne, col) == 0) {
			this.blueScore++;
		} else if (couleur == 2 && this.plateau.couleurCase(ligne, col) == 0) {
			this.redScore++;
		}
		if (this.plateau.couleurCase(ligne, col) == 0) {
			this.plateau.decrementerUncoloredNb();
		}

		this.plateau.changerValeur(ligne, col, couleur);

		switchingColors(ligne, col, couleur);
	}

	
	/*
	 * @purpose ajoute le point jou�e dans le tableau de points de la couleur correspondante
	 * @param ligne, col
	 * @param couleur la couleur de la case
	 * @complexity O(1)
	 */
	public void switchingColors(int ligne, int col, int couleur) {
		switch (couleur) {
		case 1:
			this.bluePoints.add(new Point(col, ligne));
			break;

		case 2:
			this.redPoints.add(new Point(col, ligne));
			break;

		default:
			break;
		}
	}

	
	// **************************************** pour AFFICHAGE ****************************************//
	
	public void afficher() {
		this.afficherScores();
		this.plateau.afficherPlateau();
	}

	public void afficherScores() {
		System.out.println("rouge : " + redScore + "; bleu : " + blueScore);
	}


	// **************************************** GETTER & SETTER ****************************************//
	
	public int getSize() {
		return this.size;
	}

	public boolean isGloutonne() {
		return isGloutonne;
	}

	public boolean isBrave() {
		return isBrave;
	}
	public boolean estTerminee() {
		return this.plateau.estEntierementColorie();
	}
	public Plateau getPlateau() {
		return this.plateau;
	}
	public QuadTree getQuadTree() {
		return this.quadTree;
	}
}