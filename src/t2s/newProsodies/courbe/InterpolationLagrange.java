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
package t2s.newProsodies.courbe;

import java.util.Vector;

import t2s.ihm.courbe.Point2D;

/**
 * Permet d'obtenir les ordonnees d'une fonction construite par interpolation
 * a l'aide d'une liste de points
 */
public class InterpolationLagrange {
	
	private final Vector<Point2D>	points; // les points utilises pour faire l'interpolation
	                                        
	/**
	 * Constructeur de la classe
	 */
	public InterpolationLagrange() {
		this.points = new Vector<Point2D>();
	}
	
	public InterpolationLagrange(final Vector<Point2D> l) {
		this.points = l;
	}
	
	/**
	 * Renvoie la valeur au point d'abscisse x de la fonction polynome
	 * (resultat de l'interpollation)
	 * 
	 * @param x
	 *            : abscisse
	 * @return: ordonnee au point d'abscisse x
	 */
	public double valeurFonctionInterpolee(final double x) {
		double y = 0;
		for (int i = 0; i < this.points.size(); i++) {
			y = y + (new Double((this.points.get(i).getY())).doubleValue() * coeffLagrange(x, i));
		}
		return y;
	}
	
	/**
	 * Calcule le coefficient de Lagrange Li
	 * 
	 * @param x
	 *            : abscisse
	 * @param i
	 *            : indice
	 * @return
	 */
	private double coeffLagrange(final double x, final int i) {
		double num = 1, denom = 1;
		for (int j = 0; j < this.points.size(); j++) {
			if (i != j) {
				num = num * (x - new Double((this.points.get(j).getX())).doubleValue());
				denom = denom * (new Double((this.points.get(i).getX())).doubleValue() - new Double((this.points.get(j).getX())).doubleValue());
			}
		}
		return num / denom;
	}
	
	/**
	 * Ajoute un point d'interpolation
	 * 
	 * @param x
	 *            : abscisse
	 * @param y
	 *            : ordonnee
	 */
	public void addPoint(final double x, final double y) {
		final Point2D point = new Point2D(x, y);
		this.points.add(point);
	}
	
	/**
	 * Ajoute une liste de points
	 * 
	 * @param listePoints
	 *            : Liste de points (formee d'une liste contenant l'abscisse puis l'ordonnee)
	 */
	public void addPoints(final Vector<Point2D> listePoints) {
		for (int i = 0; i < listePoints.size(); i++) {
			this.points.add(listePoints.get(i));
		}
	}
	
	/**
	 * Supprime le point d'abscisse x de la liste des points d'interpolation
	 * 
	 * @param x
	 */
	public void removePoint(final double x) {
		int i = 0;
		while ((x != (new Double(this.points.get(i).getX()).doubleValue())) && (i < this.points.size())) {
			i++;
		}
		if (i != this.points.size()) {
			this.points.remove(i);
		}
	}
	
	/**
	 * Supprime le point d'indice "indice" de la liste des points d'interpolation
	 * 
	 * @param indice
	 */
	public void removePointAtIndice(final int indice) {
		if (indice < this.points.size()) {
			this.points.remove(indice);
		}
	}
	
	public Vector<Point2D> getPointsCle() {
		return this.points;
	}
}
