package model;

public class Model {

    private Plateau plateau;
    private Tree tree;
    int scoreBleu;
    int scoreRouge;
    public static int blanc = 0, bleu = 1, rouge = 2;
    boolean isBrave;

    public Model(int n, boolean isBrave){
        this.scoreBleu = 0;
        this.scoreRouge = 0;
        this.isBrave = isBrave;
        this.tree = new Tree();
        this.tree = tree.createFamily(null, n);
    }
    
    public void to_string(){
        this.tree.to_string();
    }

    public int[] changevalue(int color, int x, int y){
        if(this.getBrave()){
            return BraveChangeValue(color,x, y);
        }else {
            return  TemeraireChangeValue(color, x, y);
        }
    }

    private int[] BraveChangeValue(int color, int x, int y){
        return this.tree.changeValue(this.tree, color, x, y, true);
    }

    private int[] TemeraireChangeValue(int color, int x, int y){
        return this.tree.changeValue(this.tree, color, x, y, false);
    }

    public Plateau getPlateau() {
    	return this.plateau;
    }
    
    public Tree getTree() {
    	return this.tree;
    }
    
    public int getTaille(){
    	return tree.getTaille();
    }
    
    public int getSide(){
    	return tree.getSide();
    }
    
    boolean getBrave(){
        return  this.isBrave;
    }
}
