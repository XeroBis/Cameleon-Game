package model;
import static java.lang.Math.pow;

public class Tree {
    private boolean isFilled;
    private boolean isSterile;
    public static char blanc = 'A', bleu = 'B', rouge = 'R';
    private char color;
    private Tree[] sons;
    private Tree father;
    private int numberOfSons;
    private int taille;
    private int side;

    Tree(){// une feuille.
        this.color = blanc;
        this.isFilled = false;
        this.isSterile = true;
        this.numberOfSons = 0;
    }
    Tree(int n) {
        super();
        this.taille = n;
        if (n == -1){
            this.side = 3;
        } else {
            this.side = (int) (6 * pow(2, n - 1));
        }
    }

    /**
     * Cette fonction retourne un Tree rempli de 0 avec 3*2^n branches
     *
     * @param n le parametre qui décide du nombre de case du plateau de jeu caméléon
     * @return Tree
     */
    Tree createFamily(Tree father, int n){
        if(n == -1){//on retourne un fils unique
            Tree t = new Tree(n);
            t.setisSterile(true);
            t.setFather(father);
            t.setNumberOfSons(0);
            t.setColor(blanc);
            return t;
        }
        Tree f = new Tree(n);
        f.setFather(father);
        f.setisSterile(false);
        if(n == 0){ // on crée les 9 derniers fils
            f.setNumberOfSons(9);
            f.setSons(new Tree[9]);
            for(int i = 0; i < 9; i++){
                f.sons[i] = createFamily(f, n-1);
            }
        } else {
            f.setNumberOfSons(4); // on divise en 4 régions.
            f.setSons(new Tree[4]);
            for(int i = 0; i < 4; i++){
                f.sons[i] = createFamily(f,n-1);
            }
        }
        return f;
    }

    /**
     * Cette fonction change la couleur d'une casse si possible et renvoie un tableau contenant
     * le statut d'erreur ainsi que les changement de valeur des points des 2 joueurs.
     *
     * errorstatue :
     * 0 : tout va bien
     * -1 : case déja colorié.
     * @param father, color, x, y, voisin
     * @return int[] : errorstatue, bleu et rouge
     */
    private int[] changeValue(Tree father, char color, int x, int y, boolean voisin, boolean brave){
        int [] res = new int[3]; // le tableau contenant le changement de valeur des points du joueur bleu, rouge ainsi que si la valeur à été changé
        int side = father.getSide();
        boolean done = false;

        while(!done){
            if(father.getisFilled()){
                res[0] = -1;
                return res;
            }
            if(father.getNumberOfSons()==4){
                if (x < side / 2 && y < side / 2) {
                    if(father.getSons()[0].getisFilled()){
                        res[0] = -1;
                        return res;
                    }
                    father = father.getSons()[0];

                } else if (x < side / 2 && y >= side / 2) {
                    if(father.getSons()[1].getisFilled()){
                        res[0] = -1;
                        return res;
                    }
                    father = father.getSons()[1];
                    side = side/2;
                    y = y - side;

                } else if (x >= side / 2 && y < side / 2) {
                    if(father.getSons()[2].getisFilled()){
                        res[0] = -1;
                        return res;
                    }
                    father = father.getSons()[2];
                    side = side/2;
                    x = x - side;

                } else if (x >= side / 2 && y >= side / 2) {
                    if(father.getSons()[3].getisFilled()){
                        res[0] = -1;
                        return res;
                    }

                    father = father.getSons()[3];
                    side = side/2;
                    x = x - side;
                    y = y - side;
                }
            }
            if(father.getNumberOfSons() == 9){
                int pos;
                if (x < 0 || y < 0) {
                    res[0]= -1;
                    return res;
                }
                if(x>2 || y > 2){
                    res[0] = -1;
                    return res;
                }
                pos = x*3+y;

                if (voisin && father.getSons()[pos].getColor() !=color && father.getSons()[pos].getColor() != blanc) {
                    father.setSon(color, pos);
                    res[color]++;
                    if (color == bleu) {
                        res[2]--;
                    } else {
                        res[1]--;
                    }
                }else if (father.getSons()[pos].getColor() != blanc && !voisin){
                	System.out.println("test erreur, couleur : " + father.getSons()[pos].getColor());
                    res[0] = -1;
                    return res;

                } else if (father.getSons()[pos].getColor() == blanc && !voisin){
                    father.setSon(color, pos);

                    int[] filled = checkIfFilled(father);
                    if(color == bleu) {
                    	res[1]++;
                    } else {
                    	res[2]++;
                    }
                    if(!brave){
                        while(filled[0] == 0) {
                            int pts = getNbOfPoints(father);
                            father.setisFilled(true);
                            if (filled[1]> filled[2]){// il y a plus de bleu que de rouge on change tout en bleu
                                for (int i = 0; i <father.getNumberOfSons() ; i++) {
                                    if(father.getSons()[i].getColor() == rouge){
                                        res[2] -= pts;
                                        father.setSon(bleu, i);
                                        res[1] += pts;
                                    }
                                }
                            }else if (filled[1]< filled[2]){ // il y a plus de rouge que de bleu
                                for (int i = 0; i <father.getNumberOfSons() ; i++) {
                                    if(father.getSons()[i].getColor() == bleu){
                                        res[1] -= pts;
                                        father.setSon(rouge, i);
                                        res[2] += pts;
                                    }
                                }
                            }else {//si il y a égalité
                                for (int i = 0; i <father.getNumberOfSons() ; i++) {
                                    if(color==bleu){
                                        father.setSon(bleu, i);
                                        res[2] -= pts;
                                        res[1] += pts;
                                    } else {
                                        father.setSon(rouge, i);
                                        res[2] += pts;
                                        res[1] -= pts;
                                    }
                                }
                            }
                            filled = checkIfFilled(father.getFather());
                        }
                    }
                }

                done = true;
            }

        }
        return  res;
    }

    int getNbOfPoints(Tree father){
        if(father.getNumberOfSons()==9){
            return 1;
        } else {
            if(father.getSons()[0].getNumberOfSons()==9){
                return 9;
            } else {
                return 4 * getNbOfPoints(father.getSons()[0]);
            }
        }
    }
    /**
     * Cette fonction change la couleur d'une case, si possible, ainsi que ses voisins (si le changement de case était possible)
     * et renvoie un tableau contenan le statut d'erreur ainsi que les changement de valeur des points des 2 joueurs.
     *
     * @param father, color, x, y, voisin
     * @return int[] : errorstatue, bleu et rouge
     */
    int[] changeValue(Tree father, char color, int x, int y, boolean brave){
        int[] res = changeValue(father, color, x, y, false, brave);
        if(res[0]==-1){
            return res;
        } else {
            int[] voisin = changeValueVoisin(father, color, x, y, brave);
            res[1] += voisin[1];
            res[2] += voisin[2];
        }
        return res;
    }

    /**
     * Cette fonction change la couleur de toute les cases voisines de celle en coordonée x et y, si possible.
     * Renvoie un tableau contenant le statut d'erreur ainsi que les changement de valeur des points des 2 joueurs.
     *
     * @param father, color, x, y, voisin
     * @return int[] : errorstatue, bleu et rouge
     */
    private int[] changeValueVoisin(Tree father, char color, int x, int y, boolean brave){
        int[] res = new int[3];
        int[][] res0 = new int[9][3];
        res0[0] = changeValue(father, color, x-1, y-1, true,brave);
        res0[1] = changeValue(father, color, x-1, y, true, brave);
        res0[2] = changeValue(father, color, x-1, y+1, true, brave);
        res0[3] = changeValue(father, color, x, y-1, true, brave);
        //res0[4] = changeValue(father, color, x, y, true);
        res0[4] = changeValue(father, color, x, y+1, true, brave);
        res0[5] = changeValue(father, color, x+1, y-1, true, brave);
        res0[6] = changeValue(father, color, x+1, y, true, brave);
        res0[7] = changeValue(father, color, x+1, y+1, true, brave);
        for (int i = 0; i < 8; i++) {
            if(res0[i][0]==0){
                res[1] += res0[i][1];
                res[2] += res0[i][2];
            }
        }
        return res;
    }

    /**
     * Cette fonction retourne un tableau,
     * en indice 0 : 0 si remplie, -1 sinon,
     * en indice 1 : le nombre de case bleu
     * en indice 2 : le nombre de case rouge
     *
     * @param father l'arbre que l'on doit vérifier si il est remplie.
     * @return int[] contenant la valeur de l'erreur (0 remplie, -1 pas remplie) , le nombre de bleu ainsi que le nombre de rouge
      */
    int[] checkIfFilled(Tree father){
        int[] colors = new int[3];
        if(father.isSterile){
            colors[0] = -1;
            return colors;
        } else if(father.getNumberOfSons() == 9 || father.getNumberOfSons() == 4){
            Tree[] sons = father.getSons();
            for (int i = 0 ; i < father.getNumberOfSons(); i++){
                if (sons[i].getColor() == bleu) {
                    colors[1] += 1;
                } else if (sons[i].getColor() == rouge){
                    colors [2] += 1;
                } else if(sons[i].getColor() == blanc){
                    colors[0] = -1;
                }
            }
            return colors;
        }
        return colors;
    }

    private void addValue(Tree father, int x, int y, char color) {
        int side = father.getSide();
        if (father.getNumberOfSons() == 4) {
            if (x < side/ 2 && y < side / 2) {
                father = father.getSons()[0];
                addValue(father, x, y, color);


            } else if (x < father.getSide() / 2 && y >= father.getSide() / 2) {
                father = father.getSons()[1];
                y = y - father.getSide()/2;
                addValue(father, x, y, color);


            } else if (x >= father.getSide() / 2 && y < father.getSide() / 2) {
                father = father.getSons()[2];
                side = side/2;
                x = x - father.getSide()/2;

                addValue(father, x, y, color);

            } else if (x >= father.getSide() / 2 && y >= father.getSide() / 2) {
                father = father.getSons()[3];
                x = x - father.getSide()/2;
                y = y - father.getSide()/2;
                addValue(father, x, y, color);
            }
        } else if (father.getNumberOfSons() == 9) {
            int pos;
            pos = x * 3 + y;
            father.setSon(color, pos);
        }
    }


    private Tree cloneFromTab(int[][] tab) {
        Tree t = new Tree(this.taille);
        t = t.createFamily(t, this.taille);
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                t.addValue(t,i,j,bleu);
            }
        }
        return t;
    }
    /**
     * Cette fonction transforme notre arbre en tableau de tableau.
     *
     * @return int[][] : toute les valeurs du plateau de jeu
     */
    char[][] getTableau(){
        // on transforme notre abre en tableau pour répercuter nos modifications de région sur le tableau
        char[][] res = new char[this.getSide()][this.getSide()];
        for (int i = 0; i <this.getSide() ; i++) {
            res[i] = getRow(this, i);
        }
        return res;
    }

    char[] getRow(Tree father, int row){
        char[] res = new char[father.getSide()*4];
        if(father.getNumberOfSons()==9){
            Tree[] sons = father.getSons();
            res[0] = sons[3*row].color;
            res[1] = sons[3*row+1].color;
            res[2] = sons[3*row+2].color;

        } else if (father.getNumberOfSons() == 4){
            int halfside = father.getSide()/2;
            Tree[] sons = father.getSons();
            char[] res1;
            if (row>= halfside){
                res = getRow(sons[2], row - halfside);
                res1 = getRow(sons[3], row - halfside);
                for(int i = 0; i < halfside; i++){
                    res[halfside+ i] = res1[i];
                }
            } else {
                res = getRow(sons[0], row );
                res1 = getRow(sons[1], row);
                for(int i = 0; i < halfside; i++){
                    res[halfside+ i] = res1[i];
                }
            }
        }
        return res;
    }

    public void to_string(){
        System.out.print("  ");
        char[][] tableau = this.getTableau();
        for(int k = 0; k<3*pow(2, this.taille); k++){
            System.out.print(" " + k + "");
        }
        System.out.println();
        for(int i = 0; i < 3 * pow(2,this.taille); i++){
            System.out.print(i + "[");
            for(int j = 0; j< 3*pow(2,this.taille); j++){
                System.out.print(" " + tableau[i][j]);
            }
            System.out.println(" ]");

        }
    }

    public int IAGloutonTestValue(Tree father, int color, int x, int y){
        int pts = 0;
        int[] colors = new int[9];
        int indice = 0;
        for (int i = x - 1; i < x+2 ; i++) {
            if(i>= 0 && i < father.getSide()){
                for (int j = y-1; j <y+2; j++) {
                    if(j >=0 && j < father.getSide()){
                        if (i != x || j != y) {
                            colors[indice] = getColor(father, i, j);
                        }
                        indice++;

                    }
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            if(colors[i]==rouge){
                pts++;
            }
        }
        return pts;

    }
    public char getColor(Tree father, int x, int y){
        if (father.getNumberOfSons()== 9){
            int pos = x * 3 + y;
            return father.getSons()[pos].getColor();
        } else {
            int halfside = father.getSide()/2;
            if (x >= halfside && y >= halfside){
                return getColor(father.getSons()[3], x-halfside, y-halfside);
            } else if (x >= halfside){
                return getColor(father.getSons()[2], x-halfside, y);
            } else if (y >= halfside){
                return getColor(father.getSons()[1], x, y-halfside);
            } else {
                return getColor(father.getSons()[0], x, y);
            }
        }
    }
    public int[] IAGlouton(boolean brave) {
        //on va tester toute les coordonnées.
        int [] res = new int[2];
        int maxScore = 0;
        for (int i = 0; i <this.getSide() ; i++) {
            for (int j = 0; j < this.getSide(); j++) {
                if(this.getColor(this, i, j)==blanc){
                    int pts = IAGloutonTestValue(this,bleu, i, j);
                    if(pts > maxScore){
                        System.out.println("maxscore " +  maxScore + ", pts: " + pts + ", res[0] = "+ i + ", res[1] =" +j);
                        maxScore = pts;
                        res[0] = i;
                        res[1] = j;
                    }
                }

            }

        }
        return res;
    }


    public void IAintelligent(){

    }
    /**
     * Partie Getter et Setter
     */
    char getColor(){
        return this.color;
    }
    void setColor(char c){
        this.color = c;
    }

    boolean getisFilled(){
        return this.isFilled;
    }
    void setisFilled(boolean r){
        this.isFilled = r;
    }

    boolean getisSterile(){ return this.isSterile; }
    void setisSterile(boolean isSterile){
        this.isSterile = isSterile;
    }

    void setNumberOfSons(int numberOfSons){
        this.numberOfSons = numberOfSons;
    }
    int getNumberOfSons(){
        return this.numberOfSons;
    }

    Tree[] getSons(){
        return this.sons;
    }
    void setSons(Tree[] sons){
        this.sons = sons;
    }
    void setSon(char color, int pos) {
        this.sons[pos].color = color;
    }
    void setSon(Tree son, int indice){
        this.sons[indice] = son;
    }

    int getTaille(){
        return this.taille;
    }
    void setTaille(int n){
        this.taille = n;
    }

    int getSide(){
        return this.side;
    }
    void setSide(int side){
        this.side = side;
    }

    Tree getFather(){
        return this.father;
    }
    void setFather(Tree father){
        this.father = father;
    }
}