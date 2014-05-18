/*
 * ﻿Copyright 2004-2007, Christian BREL, Hélène COLLAVIZZA, Sébastien MOSSER, Jean-Paul STROMBONI,
 * This file is part of project 'VocalyzeSIVOX'
 * 'VocalyzeSIVOX' is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 'VocalyzeSIVOX'is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with 'VocalyzeSIVOX'. If not, see <http://www.gnu.org/licenses/>.
 */
package t2s.ihm.courbe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Cette classe red�finit un Point2D
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */
public class Point2D {
	
	public static final int	     WIDTH	 = 8;
	public static final int	     HEIGHT	 = 8;
	java.awt.geom.Point2D.Double	coord;
	private final Rectangle	     rect;
	private boolean	             visible	= true;
	private Color	             color	 = Color.BLUE;
	
	/**
	 * Constructeur par defaut
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public Point2D() {
		this.coord = new java.awt.geom.Point2D.Double();
		this.rect = new Rectangle(0, 0, WIDTH, HEIGHT);
	}
	
	/**
	 * Constructeur qui prend un point en param�tre
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param p
	 *            Le point 2d
	 */
	public Point2D(final Point2D p) {
		this.coord = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
		this.rect = new Rectangle(0, 0, WIDTH, HEIGHT);
		this.rect.setLocation((int) p.getX() - 4, (int) p.getY() - 4);
	}
	
	/**
	 * Constructeur qui prend les coordonn�es d'un point en param�tre
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param x
	 *            La coordonnee x
	 * @param y
	 *            La coordonnee y
	 */
	public Point2D(final double x, final double y) {
		this.coord = new java.awt.geom.Point2D.Double(x, y);
		this.rect = new Rectangle(0, 0, WIDTH, HEIGHT);
		this.rect.setLocation((int) x - 4, (int) y - 4);
	}
	
	/**
	 * Methode qui retourne la couleur du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La couleur du point
	 */
	public Color getColor() {
		return this.color;
	}
	
	/**
	 * Methode qui retourne la coordonnee x du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La coordonnee x du point
	 */
	public double getX() {
		return this.coord.getX();
	}
	
	/**
	 * Methode qui retourne la coordonnee y du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return
	 */
	public double getY() {
		return this.coord.getY();
	}
	
	/**
	 * Methode qui change l'etat visible du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param b
	 *            true si le point doit etre visible et false sinon
	 */
	public void setVisible(final boolean b) {
		this.visible = b;
	}
	
	/**
	 * Methode qui change la couleur du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param c
	 *            La nouvelle couleur du point
	 */
	public void setColor(final Color c) {
		this.color = c;
	}
	
	/**
	 * Methode qui change la location du point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param x
	 *            La coordonnee x
	 * @param y
	 *            La coordonnee y
	 */
	public void setLocation(final double x, final double y) {
		this.coord.setLocation(x, y);
		this.rect.setLocation((int) x - 4, (int) y - 4);
	}
	
	/**
	 * Methode qui modifie la location
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param x
	 *            La coordonnee x
	 * @param y
	 *            La coordonnee y
	 * @param dim
	 *            La dimension
	 */
	public void setLocation(double x, double y, final Dimension dim) {
		if (x < (WIDTH / 2)) {
			x = WIDTH / 2;
		} else if (x > (dim.width - (WIDTH / 2))) {
			x = dim.width - (WIDTH / 2);
		}
		if (y < (HEIGHT / 2)) {
			y = HEIGHT / 2;
		} else if (y > (dim.height - (HEIGHT / 2))) {
			y = dim.height - (HEIGHT / 2);
		}
		setLocation(x, y);
	}
	
	/**
	 * Methode qui indique si le point est visible ou pas
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return True si le point est visible et false sinon
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * V�rifie si les coordonn�es appartiennent au point
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param x
	 *            La coordonnee x
	 * @param y
	 *            La coordonnee y
	 * @return true si les coordonnee appartienent au point et false sinon
	 */
	public boolean contains(final double x, final double y) {
		if ((x > (getX() - (WIDTH / 2))) && (x < (getX() + (WIDTH / 2)))) {
			if ((y > (getY() - (HEIGHT / 2))) && (y < (getY() + (HEIGHT / 2)))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * M�thode qui dessine les points
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param g
	 *            Le graphics2D dessinateur
	 */
	public void draw(final Graphics2D g) {
		if (!this.visible) {
			return;
		}
		g.setPaint(this.color);
		g.fill(this.rect);
	}
	
	/**
	 * Transforme un point de cette classe en un Point2D de java.awt.geom
	 * 
	 * @return le point 2D de java.awt.geom
	 */
	public java.awt.geom.Point2D.Double toAwt() {
		final java.awt.geom.Point2D.Double point = new java.awt.geom.Point2D.Double(getX(), getY());
		return point;
	}
	
}
