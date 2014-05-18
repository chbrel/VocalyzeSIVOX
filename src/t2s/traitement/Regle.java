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
package t2s.traitement;

import t2s.util.Indice;

/**
 * Regle de la langue francaise et des exceptions sur les phonemes.
 * <p>
 * Une instance de Regle est composee :
 * <p>
 * <ul>
 * <li>D'un prefixe (expression reguliere)</li>
 * <li>D'un suffixe (expression reguliere)</li>
 * <li>D'une racine</li>
 * <li>D'une chaine courante contenant les phonemes de la regle</li> </ul
 * <p>
 * A toute regle, on associe automatiquement une priorite, calcule par le poid de chacune des expressions regulieres presente en suffixe et en prefixe.
 * </p>
 */
public class Regle {
	
	private final String	prefix;
	private final String	suffix;
	private final String	racine;
	private String	     phoneme;
	private final int	 priorite;
	
	/**
	 * Pour construire une Regle vide.
	 * <p>
	 * Tous les parametres sont initialise a la chaine vide.
	 */
	public Regle() {
		this("", "", "", "");
	}
	
	/**
	 * Pour construire une Regle sans suffixe ni prefixe.
	 * <p>
	 * <b> Remarque </b> : utilise pour les regles sur les prepositions
	 * </p>
	 * 
	 * @param r
	 *            la racine de la regle
	 * @param ph
	 *            la chaine contenant les phonemes
	 */
	public Regle(final String r, final String ph) {
		this("", r, "", ph);
	}
	
	/**
	 * Pour construire une Regle complete.
	 * 
	 * @param p
	 *            le prefixe
	 * @param r
	 *            la racine
	 * @param s
	 *            le suffixe
	 * @param ph
	 *            la chaine contenant les phonemes
	 */
	public Regle(final String p, final String r, final String s, final String ph) {
		this.prefix = p;
		this.suffix = s;
		this.racine = r;
		this.phoneme = ph;
		this.priorite = poids(p) + poids(s);
	}
	
	/**
	 * Pour recuperer le prefixe de la regle
	 * 
	 * @return l'expression reguliere de l'element prefix.
	 */
	public String getPrefix() {
		return this.prefix;
	}
	
	/**
	 * Pour recuperer le suffixe de la regle
	 * 
	 * @return l'expression reguliere de l'element suffix.
	 */
	public String getSuffix() {
		return this.suffix;
	}
	
	/**
	 * Pour recuperer la racine de la regle
	 * 
	 * @return la racine de la regle.
	 */
	public String getRacine() {
		return this.racine;
	}
	
	/**
	 * Pour recuperer le phoneme de la regle
	 * 
	 * @return le phoneme associe a l'element courant.
	 */
	public String getPhoneme() {
		return this.phoneme;
	}
	
	/**
	 * Pour recuperer la priorite de la regle.
	 * <p>
	 * <b>Remarque</b> : La priorite est la somme du poid de chacune des E.R. presente dans la regle
	 * </p>
	 * <p>
	 * <center> { <code>priorite <-- poid(prefixe) + poid(suffixe)</code> </center>
	 * </p>
	 * <p>
	 * <b> Calcul du poid </b> : Il s'agit de la longueur maximale de la chaine definie par l'E.R. <br>
	 * En cas de choix (<code>'|'</code>), on prend la longueur de la plus grande chaine.
	 * </p>
	 * 
	 * @return la priorite associee a l'element courant.
	 */
	public int priorite() {
		return this.priorite;
	}
	
	/**
	 * Pour modifier la chaine de phonemes
	 * 
	 * @param ph
	 *            la nouvelle chaine a mettre dans la Regle
	 */
	public void setPhoneme(final String ph) {
		this.phoneme = ph;
	}
	
	private static int poids(final String s) {
		return poids(s, new Indice());
	}
	
	/**
	 * Renvoie le poids de l'expression reguliere s
	 * Definition :
	 * poids = longueur max de la chaine definie par l'expression reguliere s.
	 * { Quand il y a un choix '|', on prend la longueur de la plus grande chaine. }
	 * Precondition :
	 * 1. s est une expression reguliere syntaxiquement correcte
	 * 2. On a calcule le poids jusqu'a l'indice ind
	 */
	private static int poids(final String s, final Indice ind) {
		if (fin(s, ind)) {
			return 0;
		} else {
			int max = poidsTerme(s, ind);
			while (!fin(s, ind)) {
				final char cour = s.charAt(ind.val());
				ind.inc();
				if (cour == '|') {
					final int lgCour = poidsTerme(s, ind);
					if (lgCour > max) {
						max = lgCour;
					}
				}
			}
			return max;
		}
	}
	
	/**
	 * Renvoie le poids d'un sous-terme.
	 * Definition :
	 * les sous-termes sont separes pas des '|'
	 * Precondition :
	 * 1. s est une expression reguliere syntaxiquement correcte
	 * 2. on a calcule le poids jusqu'a l'indice ind
	 * Postcondition :
	 * 1. ind designe '|', ')' ou la fin de s
	 */
	private static int poidsTerme(final String s, final Indice ind) {
		if (finTerme(s, ind)) {
			return 0;
		} else {
			final char cour = s.charAt(ind.val());
			if (cour == '[') {
				ind.val(s.indexOf("]", ind.val()));
				ind.inc();
				return 1 + poidsTerme(s, ind);
			} else {
				if (cour == '(') {
					ind.inc();
					final int lg = poids(s, ind);
					ind.inc();
					return lg + poidsTerme(s, ind);
				} else {
					ind.inc();
					return 1 + poidsTerme(s, ind);
				}
			}
		}
	}
	
	/**
	 * Determine si s[ind] est la fin d'un terme de s
	 * Definition :
	 * fin d'un terme = vide ou egal a ')' ou '|'
	 */
	private static boolean finTerme(final String s, final Indice ind) {
		if (ind.egal(s.length())) {
			return true;
		} else {
			final char c = s.charAt(ind.val());
			return (c == ')') || (c == '|');
		}
	}
	
	/**
	 * Determine si ind est la fin de s
	 * Definition :
	 * fin de la chaine = s[ind] est vide ou egal a ')'
	 */
	private static boolean fin(final String s, final Indice ind) {
		if (ind.egal(s.length())) {
			return true;
		} else {
			final char c = s.charAt(ind.val());
			return (c == ')');
		}
	}
	
	/**
	 * Pour afficher une Regle de maniere lisible
	 * 
	 * @return la chaine de caracteres qui va bien ^_^.
	 */
	@Override
	public String toString() {
		return ("pref : " + this.prefix + " racine : " + this.racine + " suff :  " + this.suffix + " pho :  " + this.phoneme + "\n");
	}
	
	/**
	 * Une methode executable pour faire des tests
	 */
	public static void main(final String[] s) {
		System.out.println(poids("(a|[ab][ef])(a|b)|ab[cbfe]a|a[ab](de|abc)dfe", new Indice(0)));
		System.out.println(poids("a", new Indice(0)));
	}
}
