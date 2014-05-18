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
/*
 * SI VOX Copyright (C) 2004 - 2005
 * Author :
 * ESSI2 school project (2004) : Affouard, Lemonnier, Fournols ,Lizzul
 * Tutor (2004) : H�l�ne Collavizza [ helen@essi.fr ]
 * Jean-Paul Stromboni [ strombon@essi.fr ]
 * Contributor :
 * (2004) : Louis Parisot [ parisot@essi.fr ]
 * (2005) : S�bastien Mosser [ mosser@essi.fr ]
 * Institute :
 * Polytechnich school, University of Nice - Sophia Antipolis (FRANCE)
 * This program is free software. It uses mbrola speech synthesizers system.
 * You can redistribute it and/or modify it under the terms of the MBROLA
 * Licenses { http://tcts.fpms.ac.be/synthesis/mbrola.html }.
 */

package t2s.traitement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Une implementation d'une liste simplement chainee pour des instances de <code>Regle</code>.
 * <p>
 * <b>Remarque</b> : les regles sont classees par taille de <code>(suffixe + prefixe)</code>.On applique ainsi la r�gle la plus grande possible en premier
 * </p>
 */
public class ListeRegles {
	
	/** Le tag correpsondant a la situation ou il n'y a pas de regles */
	public static final String	PAS_DE_REGLE	= "vide";
	
	ListeRegles	               suivant;
	Regle	                   tete;
	
	/**
	 * Constructeur de liste vide.
	 * <p>
	 * <b>Remarque</b> : Une liste de regles vide n'a ni suivant ni tete (mis a <code>null</code>).
	 * </p>
	 */
	public ListeRegles() {
		this.suivant = null;
		this.tete = null;
	}
	
	/**
	 * Construit une liste de Regle.
	 * 
	 * @param regle
	 *            la regle a rajouter en tete dans la liste
	 * @param suivant
	 *            la liste de regles qui suivra <code>regle</code>
	 */
	private ListeRegles(final Regle regle, final ListeRegles suivant) {
		this.suivant = suivant;
		this.tete = regle;
	}
	
	/**
	 * Pour recuperer l'element suivant dans la liste (une liste de regles, prive de la tete de <code>this<code>)
	 * 
	 * @return l'element suivant.
	 */
	public ListeRegles getListeSuivante() {
		return this.suivant;
	}
	
	/**
	 * Pour recuperer la regle presente en tete de liste
	 * 
	 * @return l'element present en tete.
	 */
	public Regle getRegle() {
		return this.tete;
	}
	
	/**
	 * Pour ajouter une regle a la liste.
	 * <p>
	 * <b>Remarque</b> : On respecte un ordre decroissant sur la taille du suffixe et du prefixe
	 * </p>
	 * 
	 * @param t
	 *            la regle que l'on veut ajouter dans la liste courante
	 */
	public void ajouter(final Regle t) {
		if (estVide() || (t.priorite() > this.tete.priorite())) {
			this.suivant = new ListeRegles(this.tete, this.suivant);
			this.tete = t;
		} else {
			this.suivant.ajouter(t);
		}
	}
	
	/**
	 * Pour savoir si la liste est vide.
	 * 
	 * @return true si c'est le cas, false sinon.
	 */
	public boolean estVide() {
		return this.tete == null;
	}
	
	/**
	 * Pour trouver les phonemes associe a un mot.
	 * <p>
	 * <b>Remarque</b> : On applique la premiere regle qui s'unifie a la sous-chaine du <code>mot</code> se terminant sur <code>indice</code>
	 * </p>
	 * 
	 * @param mot
	 *            le mot que l'on veut transformer
	 * @param indice
	 *            l'entier representant l'indice sur lequel on finit l'analyse.
	 * @return le phoneme correspondant a la partie du mot unifiee
	 */
	public String trouverPhoneme(final String mot, final int indice) {
		if (estVide()) {
			return PAS_DE_REGLE;
		} else {
			final String corps = getRegle().getRacine();
			final int debutRacine = indice - corps.length();
			final String prefixe = getRegle().getPrefix();
			final String suffixe = getRegle().getSuffix();
			
			Pattern p = Pattern.compile(corps + suffixe);
			Matcher m = p.matcher(mot);
			if (m.find(debutRacine) && (m.start() == debutRacine)) {
				p = Pattern.compile(prefixe + corps);
				m = p.matcher(mot);
				boolean prefixeOK = false;
				while (!prefixeOK && m.find()) {
					if (m.end() == indice) {
						prefixeOK = true;
					}
				}
				if (prefixeOK) {
					return getRegle().getPhoneme();
				}
			}
			return getListeSuivante().trouverPhoneme(mot, indice);
		}
	}
	
	/**
	 * Methode d'affichage standart d'une liste de regles.
	 * 
	 * @return la chaine de caracteres qui va bien ^_^.
	 */
	@Override
	public String toString() {
		if (estVide()) {
			return "";
		} else {
			return getRegle().toString() + getListeSuivante().toString();
		}
	}
}
