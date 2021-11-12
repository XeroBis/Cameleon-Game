package model;

public class Point 
{
	private int x,y;
	
	//Constructeur du Point de coordonn�es x et y dans un plan en 2D
	//@param x coordonn�e x du point
	//@param y coordonn�e y du point
	public Point (int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	//Ensemble de getters
	public int getx()
	{
		return this.x;
	}
	public int gety()
	{
		return this.y;
	}
	
	
	//M�thode permettant de changer les coordonn�es du point
	//@param a nouvelle coordonn�e en x
	//@param b nouvelle coordonn�e en y
	public void changeboth(int a, int b)
	{
		this.x = a;
		this.y = b;
	}
}
