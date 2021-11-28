package model;

import java.util.ArrayList;

public class Model {
	// ******************** Attributs et Constructeurs de la classe Model
	// ********************//

	private Plateau plateau; // le plateau de jeu.
	private QuadTree quadTree; // l'arbre pour les régions.
	private int size; // la taille du coté.

	private ArrayList<Point> redPoints, bluePoints; // 2 tableau qui contiennent les points qui peuvent être pris.
	private int redScore, blueScore; // les deux ints des scores.

	/*
	 * constructeur de Model
	 */
	public Model() {
		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();

		this.redScore = 0;
		this.blueScore = 0;
	}

	public Model(int k) {
		this.setNewK(k);

		this.redPoints = new ArrayList<Point>();
		this.bluePoints = new ArrayList<Point>();

		this.redScore = 0;
		this.blueScore = 0;
	}

	public void setNewK(int k) {
		this.plateau = new Plateau(k);
		this.quadTree = buildingQT(null, k, new Point(0, 0));
		this.size = 3 * (int) Math.pow(2, k);
	}

	private QuadTree buildingQT(QuadTree father, int k, Point p) {
		if (k == 0) {
			return new QuadTree(father, true, QuadTree.blanc, p, null, null, null, null);
		} else {
			int dim = (int) (3 * Math.pow(2, k));

			Point p1 = new Point(p.getx() + dim / 2, p.gety());
			Point p2 = new Point(p.getx() + dim / 2, p.gety() + dim / 2);
			Point p3 = new Point(p.getx(), p.gety() + dim / 2);

			return new QuadTree(null, false, QuadTree.blanc, p, buildingQT(father, k - 1, p),
					buildingQT(father, k - 1, p1), buildingQT(father, k - 1, p2), buildingQT(father, k - 1, p3));
		}
	}

	// ******************** Fonctions pour le mode de jeu "Brave"
	// ********************//

	public boolean colorationBrave(int ligne, int col, int couleur) {
		if (plateau.couleurCase(ligne, col) != 0 || ligne < 0 || ligne >= size || col < 0 || col >= size) {
			System.out.println("Mouvement Interdit !");
			return false;
		} else {
			coloration(ligne, col, couleur);
			recolorationBrave(ligne, col, couleur);

			return true;
		}
	}

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

	public void botBraveGlouton(int color) {
		actualizingArrayPoints();
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
						int nb = nbOpponentColor(p.gety() + i, p.getx() + j, color);
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
		colorationBrave(p.gety(), p.getx(), color);
		actualizingArrayPoints();
	}

	// ******************** Fonctions pour le mode de jeu "Téméraire"
	// ********************//

	public boolean colorationTemeraire(int ligne, int col, int couleur) {
		System.out.println("taille pts red :" + this.redPoints.size() + ", taille pts bleu :" + this.bluePoints.size());
		if (plateau.couleurCase(ligne, col) != this.plateau.blanc) {
			System.out.println("Mouvement Interdit !");
			return false;
		} else {
			coloration(ligne, col, couleur); // R1
			recolorationTemeraire(ligne, col, couleur); // R2

			Point p = getSmallRegionTopLeft(ligne, col);
			if (isSmallRegionFull(p.gety(), p.getx())) {

				recoloringSmallRegion(p.gety(), p.getx(), couleur);
				recoloringSmallRegionQuadTree(ligne, col, couleur, this.quadTree);

				acquiringRegion(p.gety(), p.getx(), couleur, this.quadTree);
				System.out.println("fin premier");
				acquiringRegion(p.gety(), p.getx(), couleur, this.quadTree);
			}

		}
		return true;
	}

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

	public Point getSmallRegionTopLeft(int ligne, int col) {
		int x = col - (col % 3);
		int y = ligne - (ligne % 3);
		return new Point(x, y);
	}

	public void acquiringRegion(int ligne, int col, int couleur, QuadTree qt) {
		if (qt.getisSterile()) {
			qt.setValue(couleur);
			recoloringSmallRegion(ligne, col, couleur);
		} else {
			int rge = 0;
			int ble = 0;

			for (int i = 0; i < 4; i++) {
				if (qt.getQt(i).getValue() == QuadTree.rouge) {
					rge++;
				}
				if (qt.getQt(i).getValue() == QuadTree.bleu) {
					ble++;
				}

			}

			if (rge + ble == 4) {
				for (int i = 0; i < 4; i++) {
					if (qt.getQt(i).getValue() != couleur) {
						if (couleur == this.plateau.bleu) {
							if (rge > ble) {
								qt.setValue(2);
							} else {
								qt.setValue(couleur);
							}
						} else if (couleur == this.plateau.rouge) {
							System.out.println("valeur bleu: " + ble + ", valeur rouge :" + rge);
							if (ble > rge) {
								qt.setValue(1);
							} else {
								qt.setValue(couleur);
							}
						}
						qt.setValue(couleur);
						acquiringRegion(qt.getQt(i).getPoint().gety(), qt.getQt(i).getPoint().getx(), couleur,
								qt.getQt(i));
					}
				}
			} else {
				if (col >= qt.getQt(2).getPoint().getx()) {
					if (ligne >= qt.getQt(2).getPoint().gety()) {
						acquiringRegion(ligne, col, couleur, qt.getQt(2));
					} else {
						acquiringRegion(ligne, col, couleur, qt.getQt(1));
					}
				} else {
					if (ligne >= qt.getQt(2).getPoint().gety()) {
						acquiringRegion(ligne, col, couleur, qt.getQt(3));
					} else {
						acquiringRegion(ligne, col, couleur, qt.getQt(0));
					}
				}
			}
		}
	}

	public boolean isSmallRegionFull(int ligne, int col) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (plateau.couleurCase(ligne + i, col + j) == Plateau.blanc) {
					return false;
				}
			}
		}
		return true;
	}

	public void recoloringSmallRegionQuadTree(int ligne, int col, int color, QuadTree father) {
		if (father.getisSterile()) {
			father.setValue(color);
		} else {
			if (col >= father.getQt(2).getPoint().getx()) {
				if (ligne >= father.getQt(2).getPoint().gety()) {
					recoloringSmallRegionQuadTree(ligne, col, color, father.getQt(2));
				} else {
					recoloringSmallRegionQuadTree(ligne, col, color, father.getQt(1));
				}
			} else {
				if (ligne >= father.getQt(2).getPoint().gety()) {
					recoloringSmallRegionQuadTree(ligne, col, color, father.getQt(3));
				} else {
					recoloringSmallRegionQuadTree(ligne, col, color, father.getQt(0));
				}

			}
		}
	}

	public void recoloringSmallRegion(int ligne, int col, int couleur) {

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				plateau.changerValeur(ligne + i, col + j, couleur);
			}
		}
	}

	public Point getBestMoveTemeraire(int color) {
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
							int nb = nbOpponentColor(p.gety() + i, p.getx() + j, color);
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
			p = this.redPoints.get(k);
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i != 0 || j != 0) {
						if (plateau.couleurCase(p.gety() + i, p.getx() + j) == this.plateau.blanc) {
							int nb = nbOpponentColor(p.gety() + i, p.getx() + j, color);
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
	
	public void botTemeraireGlouton(int color) {
		Point bestMove = getBestMoveTemeraire(color);
		this.colorationTemeraire(bestMove.gety(), bestMove.getx(), color);
		this.updateScoreTemeraire();
		this.actualizingArrayPointsTemeraire();
	}

	public void updateScoreTemeraire() {
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
	 * Fonction testant si la zone qui contient le point en ligne col contient 8
	 * case coloriée et peut donc être capturé
	 */
	public int getNumberOfZoneTaken(int ligne, int col, int color) {
		// en premier on test si les coord données remplisse une partie
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
		if (nbZone == 3) { // on peut colorier une grande zone, donc on test le père
			if (father.getFather() == null) {
				System.out.println("test");
				return 4;
			} else {
				return 4 * TestIfGetBiggerZone(father);
			}
		} else {
			return 1; // si on ne capture pas la grande zone alors on a capturer seulement une petite
						// zone.
		}

	}

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


	// ******************** Fonctions générales ********************//

	public void RemplirTableau(int ligne, int col, int couleur) {

		this.coloration(ligne, col, couleur);
		this.colorationQuadTree(ligne, col, couleur);
	}

	public void colorationQuadTree(int ligne, int col, int couleur) {
		Point p = getSmallRegionTopLeft(ligne, col);

		if (isSmallRegionFull(p.gety(), p.getx())) {
			recoloringSmallRegion(p.gety(), p.getx(), couleur);
			acquiringRegion(p.gety(), p.getx(), couleur, this.quadTree);
		}
	}

	public void coloration(int ligne, int col, int couleur) {
		if (couleur == 1 && plateau.couleurCase(ligne, col) == 2) {
			this.redScore--;
			this.blueScore++;
		} else if (couleur == 2 && plateau.couleurCase(ligne, col) == 1) {
			this.redScore++;
			this.blueScore--;
		} else if (couleur == 1 && plateau.couleurCase(ligne, col) == 0) {
			this.blueScore++;
		} else if (couleur == 2 && plateau.couleurCase(ligne, col) == 0) {
			this.redScore++;
		}
		if (plateau.couleurCase(ligne, col) == 0) {
			plateau.decrementerUncoloredNb();
		}

		plateau.changerValeur(ligne, col, couleur);
		// plateau.decrementerUncoloredNb();

		switchingColors(ligne, col, couleur);
	}

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

	public void afficher() {
		this.afficherScores();
		this.plateau.afficherPlateau();
	}

	public void afficherQT() {
		this.quadTree.printValuePoint();
	}

	public boolean estTerminee() {
		return plateau.estEntierementColorie();
	}

	public Plateau getPlateau() {
		return this.plateau;
	}

	public int getSize() {
		return this.size;
	}

	public void afficherScores() {
		System.out.println("rouge : " + redScore + "; bleu : " + blueScore);
	}

	@SuppressWarnings("unlikely-arg-type")
	public void actualizingArrayPoints() {

		Point p;
		ArrayList<Integer> removePoints = new ArrayList<Integer>();
		for (int i = redPoints.size() - 1; i >= 0; i--) {
			p = redPoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) != 2) {
				removePoints.add(i);
			}
		}
		for (int i = 0; i < removePoints.size(); i++) {
			redPoints.remove(removePoints.get(i));
		}
		removePoints.clear();

		for (int i = bluePoints.size() - 1; i >= 0; i--) {
			p = bluePoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) != 1) {
				removePoints.add(i);
			}
		}
		for (int i = 0; i < removePoints.size(); i++) {
			this.bluePoints.remove(removePoints.get(i));
		}
		removePoints.clear();
	}

	@SuppressWarnings("unlikely-arg-type")
	public void actualizingArrayPointsTemeraire() {
		Point p;
		int size = this.redPoints.size();
		for(int i =0; i < size; i++) {
			p = this.redPoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == this.plateau.bleu || !isNotLock(p.gety(), p.getx(), quadTree)) {
				this.redPoints.remove(i);
				size--;
				i--;
			}
		}
		
		size = this.bluePoints.size();
		for(int i =0; i < size; i++) {
			p = this.bluePoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == this.plateau.rouge || !isNotLock(p.gety(), p.getx(), quadTree)) {
				this.bluePoints.remove(i);
				size--;
				i--;
			}
		}
		
		
		
		
		/*
		ArrayList<Integer> removePoints = new ArrayList<Integer>();
		
		for (int i = redPoints.size() - 1; i >= 0; i--) {
			p = redPoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == this.plateau.bleu
					|| !isNotLock(p.gety(), p.getx(), quadTree)) {
				removePoints.add(i);
			}
		}
		for (int i = 0; i < removePoints.size(); i++) {
			redPoints.remove(removePoints.get(i));
		}
		removePoints.clear();	

		for (int i = bluePoints.size() - 1; i >= 0; i--) {
			p = bluePoints.get(i);
			if (!hasFreeNeighbor(p.gety(), p.getx()) || plateau.couleurCase(p.gety(), p.getx()) == this.plateau.rouge
					|| !isNotLock(p.gety(), p.getx(), quadTree)) {
				removePoints.add(i);
			}
		}
		for (int i = 0; i < removePoints.size(); i++) {
			this.bluePoints.remove(removePoints.get(i));
		}
		removePoints.clear();
		*/
		
	}

	public boolean hasFreeNeighbor(int ligne, int col) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (plateau.couleurCase(ligne + i, col + j) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	public int nbOpponentColor(int ligne, int col, int couleur) {
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
}