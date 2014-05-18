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

import java.util.Vector;

import t2s.util.ConfigFile;
import t2s.util.Random;
import t2s.util.SuiteCroissante;

/**
 * Classe de Phoneme
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */
public class Phoneme {
	
	private String	               pho;
	private Vector<CoupleProsodie>	prosodie;
	private int	                   longueur;
	
	/**
	 * Pour creer un phoneme a partir d'une chaine de caractere
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         <p>
	 *         <b>Remarque</b> : la suite de couple de prosodie est vide
	 *         </p>
	 * @param pho
	 *            la chaine de caractere representant le phoneme
	 */
	public Phoneme(final String pho) {
		this.pho = pho;
		this.prosodie = new Vector<CoupleProsodie>();
		this.longueur = duree(pho);
	}
	
	/**
	 * Pour creer un phoneme a partir d'une chaine de caractere, en specifiant sa duree
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         <p>
	 *         <b>Remarque</b> : la suite de couple de prosodie est vide
	 *         </p>
	 * @param pho
	 *            la chaine de caractere representant le phoneme.
	 * @param l
	 *            la duree du phoneme
	 */
	public Phoneme(final String pho, final int l) {
		this.pho = pho;
		this.prosodie = new Vector<CoupleProsodie>();
		this.longueur = l;
	}
	
	/**
	 * Pour retrouver la chaine de caractere du phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la chaine de caracteres representant le phoneme
	 */
	public String getPho() {
		return this.pho;
	}
	
	/**
	 * Pour recuperer la suite de couples de prosodies du phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return le <code>Vector</code>
	 */
	public Vector<CoupleProsodie> getProsodie() {
		return this.prosodie;
	}
	
	/**
	 * Pour recuperer la longueur (duree) du phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la duree du phoneme
	 */
	public int getLongueur() {
		return this.longueur;
	}
	
	/**
	 * Pour modifier la chaine de caracteres du phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param pho
	 *            la nouvelle chaine de caractere
	 */
	public void setPho(final String pho) {
		this.pho = pho;
	}
	
	/**
	 * Pour modifier la duree du phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param longueur
	 *            la nouvelle duree
	 */
	public void setLongueur(final int longueur) {
		this.longueur = longueur;
	}
	
	/**
	 * Pour allonger un Phoneme en fonction du type de syntagme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le syntagme dont fait partie le phoneme
	 */
	public void allonge(final Syntagme s) {
		final int l = (s.court()) ? 50 : 100;
		this.longueur = this.longueur + l;
	}
	
	/**
	 * Pour savoir si le phoneme est une pause
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return true si c'est le cas, false sinon
	 */
	public boolean estPause() {
		return ("_".equals(this.pho));
	}
	
	/**
	 * Pour savoir s'il s'agit d'une d'une occlusive voisee
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         <p>
	 *         <b>Besoin d'aide ?</b> : <i>cf.</i> <a href="http://fr.wikipedia.org/wiki/Occlusive"><code>WikiPedia</code></a>
	 *         </p>
	 * @return true si c'est le cas, false sinon
	 */
	public boolean occlusiveVoisee() {
		return ((this.pho == "b") || (this.pho == "d") || (this.pho == "g"));
	}
	
	/**
	 * Pour Calculer automatiquement la duree d'un phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            une String representant un phoneme
	 * @return int : la duree associee
	 *         <p>
	 *         Fonction qui determine la duree a appliquer au phoneme s.
	 *         </p>
	 */
	private int duree(final String s) {
		final String[][] groupe = { { "R", "l", "H" }, { "d", "n", "j", "w" }, { "b", "v", "Z", "m", "N", "i", "y" }, { "t", "k", "z", "e", "a", "o", "u" }, { "p", "O", "E" }, { "f", "S", "s", "2" } };
		
		final int sign = (Math.random() > 0.5) ? 1 : -1;
		final int facteur = (int) (sign * 4 * Math.random());
		
		if (s.endsWith("~")) {
			return 120 + facteur;
		}
		
		for (int i = 0; i < groupe.length; i++) {
			for (int j = 0; j < groupe[i].length; j++) {
				if (s.equals(groupe[i][j])) {
					return 65 + (10 * i) + facteur;
				}
			}
		}
		
		return 70 + facteur;
	}
	
	/**
	 * Pour affecter une prosodie (suite de couple de Prosodie) au phoneme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param f
	 *            la frequence autour de laquelle on fait varier les phonemes longs
	 */
	protected void setProsodie(int f) {
		final int l = this.longueur;
		final int nbVariations = (l < 95) ? 1 : Random.unsignedDelta(1, Integer.parseInt(ConfigFile.rechercher("NB_VARIATIONS_PITCH")));
		final SuiteCroissante pourcentage = new SuiteCroissante(nbVariations);
		final Vector<CoupleProsodie> v = new Vector<CoupleProsodie>();
		if (occlusiveVoisee()) {
			f -= 10;
		}
		for (int i = 0; i <= nbVariations; i++) {
			final CoupleProsodie cp = new CoupleProsodie(pourcentage.next(), Random.delta(f, 4));
			v.add(cp);
		}
		this.prosodie = new Vector<CoupleProsodie>(v);
	}
	
	/**
	 * Methode standart d'affichage d'un phoneme (conformite au format MBROLA)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la representation du phoneme au format MBROLA
	 */
	@Override
	public String toString() {
		String s = getPho() + " " + getLongueur() + " ";
		for (int i = 0; i < this.prosodie.size(); i++) {
			final CoupleProsodie couple = this.prosodie.get(i);
			s += " " + couple.getPourcentage() + " " + couple.getFrequence();
		}
		return s;
	}
}
