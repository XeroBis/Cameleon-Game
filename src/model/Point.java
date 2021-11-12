package model;

public class Point 
{
	private int x,y;
	
	//Constructeur du Point de coordonnées x et y dans un plan en 2D
	//@param x coordonnée x du point
	//@param y coordonnée y du point
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
	
	
	//Méthode permettant de changer les coordonnées du point
	//@param a nouvelle coordonnée en x
	//@param b nouvelle coordonnée en y
	public void changeboth(int a, int b)
	{
		this.x = a;
		this.y = b;
	}
}
