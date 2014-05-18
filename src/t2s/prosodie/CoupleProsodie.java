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
 * Classe qui represente un couple pourcentage-frequence
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */
public class CoupleProsodie {
	
	private int	pourcentage;
	private int	freq;
	
	/**
	 * Constructeur par defaut de CoupleProsodie
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public CoupleProsodie() {
		this.pourcentage = 0;
		this.freq = 0;
	}
	
	/**
	 * Constructeur par parametre de CoupleProsodie
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param p
	 *            Le pourcentage du couple
	 * @param f
	 *            La frequence du couple
	 */
	public CoupleProsodie(final int p, final int f) {
		this.pourcentage = p;
		this.freq = f;
	}
	
	/**
	 * Methode qui retourne le pourcentage
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return Le pourcentage du couple
	 */
	public int getPourcentage() {
		return this.pourcentage;
	}
	
	/**
	 * Methode qui retourne la frequence
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La frequence du couple
	 */
	public int getFrequence() {
		return this.freq;
	}
	
	/**
	 * Methode qui modifie le pourcentage du couple
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param p
	 *            Le nouveau pourcentage
	 */
	public void setPourcentage(final int p) {
		this.pourcentage = p;
	}
	
	/**
	 * Methode qui modifie la frequence du couple
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param f
	 *            La nouvelle frequence du couple
	 */
	public void setFrequence(final int f) {
		this.freq = f;
	}
	
	/**
	 * Methode qui retourne une chaine representant le couple
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	@Override
	public String toString() {
		return getPourcentage() + " " + getFrequence() + " ";
	}
}
