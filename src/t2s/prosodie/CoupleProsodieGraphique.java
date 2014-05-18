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
package t2s.prosodie;

/**
 * Classe representant un couple prosodie graphique heritant de coupleProsodie
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */
public class CoupleProsodieGraphique extends CoupleProsodie {
	
	private int	x;
	private int	y;
	
	/**
	 * Constructeur par defaut de CoupleProsodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public CoupleProsodieGraphique() {
		super();
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Constructeur par parametre de CoupleProsodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param p
	 *            Le pourcentage du couple
	 * @param f
	 *            La frequence du couple
	 * @param x
	 *            La position x du couple
	 * @param y
	 *            La position y du couple
	 */
	public CoupleProsodieGraphique(final int p, final int f, final int x, final int y) {
		super(p, f);
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Methode qui retourne la position x du coupleprosodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La position x
	 */
	public int getX() {
		return (this.x);
	}
	
	/**
	 * Methode qui retourne la position y du coupleProsodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La position y
	 */
	public int getY() {
		return (this.y);
	}
	
	/**
	 * Methode qui modifie la position x du coupleProsodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param x
	 *            La nouvelle position x
	 */
	public void setX(final int x) {
		if (x >= 0) {
			this.x = x;
		}
	}
	
	/**
	 * Methode qui modifie la position y du coupleProsodieGraphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param y
	 */
	public void setY(final int y) {
		if (y >= 0) {
			this.y = y;
		}
	}
}
