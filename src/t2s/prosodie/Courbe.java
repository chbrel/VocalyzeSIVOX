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

import t2s.util.ConfigFile;

/**
 * Classe decrivant les courbes
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */

public class Courbe {
	
	private final Syntagme	synt;
	private final int	   frequenceInit;
	private final int	   nbPoint;
	private final int	   hauteurNiveau;
	private double	       coeffk;
	private int	           xn;
	
	/**
	 * Constructeur par defaut
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le syntagme associe a la courbe
	 * @param f
	 *            la frequence initiale de la courbe
	 * @param n
	 *            le nombre de points utilise par la courbe
	 * @param h
	 *            la hauteur entre les 4 niveaux des courbes
	 */
	public Courbe(final Syntagme s, final int f, final int n, final int h) {
		this.synt = s;
		this.frequenceInit = f;
		this.nbPoint = n;
		this.hauteurNiveau = h;
		if (s.mineur()) {
			this.coeffk = -(Integer.parseInt(ConfigFile.rechercher("COEFF_K_MINEUR")) * h) / Math.pow(1 - n, 2);
		} else if (s.majeur()) {
			this.coeffk = -(Integer.parseInt(ConfigFile.rechercher("COEFF_K_MAJEUR")) * h) / Math.pow(1 - n, 2);
		} else {
			this.coeffk = 0;
		}
		this.xn = 0;
	}
	
	/**
	 * Constructeur de courbe plus facile d'acces (valeur par defaut)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le type de syntagme
	 * @param n
	 *            le nombre de points de la courbe
	 */
	public Courbe(final Syntagme s, final int n) {
		this(s, Integer.parseInt(ConfigFile.rechercher("FREQUENCE_INIT")), n, Integer.parseInt(ConfigFile.rechercher("HAUTEUR_PALIER")));
	}
	
	/**
	 * Pour construire une courbe constante
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le syntagme associe
	 */
	public Courbe(final Syntagme s) {
		this(s, Integer.parseInt(ConfigFile.rechercher("FREQUENCE_INIT")), -1, Integer.parseInt(ConfigFile.rechercher("HAUTEUR_PALIER")));
	}
	
	/**
	 * Pour obtenir la prochaine valeur de la courbe (iterateur)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return l'entier correspondant
	 */
	public int nextValue() {
		if (this.xn == -1) {
			if (this.synt.finExclam()) {
				return this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_EXCLAMATION")) * this.hauteurNiveau);
			} else {
				return this.frequenceInit + this.hauteurNiveau;
			}
		} else {
			this.xn++;
			if (this.synt.mineur()) {
				return valueA();
			}
			if (this.synt.majeur()) {
				return valueB();
			}
			if (this.synt.finInterro()) {
				return valueC();
			}
			if (this.synt.finExclam()) {
				return valueE();
			}
			return valueD();
		}
	}
	
	/**
	 * Methode permettant de savoir s'il reste des points a calculer sur la courbe
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return true s'il reste des points, false sinon
	 */
	public boolean hasMoreValue() {
		return this.xn < this.nbPoint;
	}
	
	/**
	 * Calcul du point suivant, pour une courbe de type A
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la valeur ad'hoc
	 */
	private int valueA() {
		final double d = this.xn - this.nbPoint;
		return (int) (this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_HAUTEUR_A")) * this.hauteurNiveau) + (this.coeffk * Math.pow(d,
		        Integer.parseInt(ConfigFile.rechercher("PUISSANCE_A")))));
	}
	
	/**
	 * Calcul du point suivant, pour une courbe de type B
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la valeur ad'hoc
	 */
	private int valueB() {
		final double d = this.xn - this.nbPoint;
		return (int) (this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_HAUTEUR_B")) * this.hauteurNiveau) + (this.coeffk * Math.pow(d,
		        Integer.parseInt(ConfigFile.rechercher("PUISSANCE_B")))));
	}
	
	/**
	 * Calcul du point suivant, pour une courbe de type C
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la valeur ad'hoc
	 */
	private int valueC() {
		final double r = Math.pow((double) (this.xn - 1) / (this.nbPoint - 1), Integer.parseInt(ConfigFile.rechercher("PUISSANCE_C")));
		return (int) ((this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_HAUTEUR_C")) * this.hauteurNiveau)) - (Integer.parseInt(ConfigFile.rechercher("COEFF_H_SQRT_C"))
		        * this.hauteurNiveau * Math.sqrt(1 - r)));
	}
	
	/**
	 * Calcul du point suivant, pour une courbe de type D
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la valeur ad'hoc
	 */
	private int valueD() {
		return (this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_HAUTEUR_D")) * this.hauteurNiveau))
		        - ((Integer.parseInt(ConfigFile.rechercher("COEFF_H_N-1_D")) * this.hauteurNiveau * (this.xn - 1)) / (this.nbPoint - 1));
	}
	
	/**
	 * Calcul du point suivant, pour une courbe de type E
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la valeur ad'hoc
	 */
	private int valueE() {
		return (this.frequenceInit + (Integer.parseInt(ConfigFile.rechercher("COEFF_HAUTEUR_E")) * this.hauteurNiveau))
		        - ((Integer.parseInt(ConfigFile.rechercher("COEFF_H_N-1_E")) * this.hauteurNiveau * (this.xn - 1)) / (this.nbPoint - 1));
	}
	
	/**
	 * Une methode de test executable
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public static void main(final String[] s) {
		final Courbe c = new Courbe(new Syntagme(Syntagme.MINEUR, Syntagme.PAUSE_COURTE), 40);
		System.out.println(c.coeffk);
		while (c.hasMoreValue()) {
			System.out.println(c.nextValue());
		}
	}
}
