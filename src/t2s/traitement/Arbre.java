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

import t2s.exception.AnalyseException;
import t2s.util.ConfigFile;
import t2s.util.Indice;

/** Un arbre <i>(prefixe)</i> des regles pour retrouver les phonemes correspondant a des groupes de lettres. */

public class Arbre {
	
	private Arbre	    frere;
	private Arbre	    fils;
	private String	    lettre;
	private ListeRegles	regles;
	
	/**
	 * Constructeur d'arbre a partir d'un repertoire contenant des fichiers de regles.
	 * <p>
	 * <b>Organisation du repertoire de regles</b> :
	 * </p>
	 * <p>
	 * <ul>
	 * <li>fichier <code>preposition.txt</code> contenant les prepositions (pour couper les phrases)</li>
	 * <li>fichier <code>regle.txt</code> contenant les regles de prononciation</li>
	 * <li>fichier <code>exception.txt</code> contenant les exceptions de prononciations</li>
	 * <li>fichier <code>acronymes.txt</code> contenant les acronymes de la langue francaise</li>
	 * </ul>
	 * </p>
	 * 
	 * @param path
	 *            le chemin d'acces au fichiers de regles
	 */
	public Arbre(final String path) throws AnalyseException {
		this();
		final GenerateurPreposition prop = new GenerateurPreposition(ConfigFile.rechercher("PREPOSITIONS"));
		Regle p = prop.nouvellePreposition();
		
		while (!prop.vide()) {
			this.ajouter(p);
			p = prop.nouvellePreposition();
		}
		prop.close();
		
		creerLexique(ConfigFile.rechercher("REGLES"));
		creerLexique(ConfigFile.rechercher("EXCEPTIONS"));
		creerLexique(ConfigFile.rechercher("ACCRONYMES"));
	}
	
	/**
	 * Constructeur d'arbre vide.
	 * <p>
	 * <b>Definition</b> : Un arbre vide contient uniquement la lettre 'a'
	 * </p>
	 */
	private Arbre() {
		this.frere = null;
		this.fils = null;
		this.lettre = "a";
		this.regles = new ListeRegles();
	}
	
	/**
	 * Construction d'arbre par copie
	 * 
	 * @param a
	 *            l'arbre porefixe que l'on souhaite copier dans this.
	 */
	private Arbre(final Arbre a) {
		this.frere = a.frere;
		this.fils = a.fils;
		this.lettre = a.lettre;
		this.regles = a.regles;
	}
	
	/**
	 * Pour trouver les phonemes associe a une phrase
	 * 
	 * @param phrase
	 *            la phrase que l'on veut transformer en phonemes
	 * @return la liste des phonemes qui va bien ^_^.
	 */
	public String trouverPhoneme(final String phrase) {
		final Indice i = new Indice();
		String res = "";
		String tmp = "";
		while (i.val() < phrase.length()) {
			tmp = trouverPhoneme(phrase, i);
			if (!vide(tmp)) {
				res += tmp;
			} else {
				i.inc();
			}
		}
		return res;
	}
	
	/**
	 * Methode d'affichage standart (affichage en largeur).
	 * <p>
	 * <b>Precondition</b> : L'arbre n'est pas vide.
	 * </p>
	 * 
	 * @return une chaine de caractere representant l'arbre
	 */
	@Override
	public String toString() {
		final FileArbre f = new FileArbre();
		f.ajouter(this);
		String s = "";
		while (!f.vide()) {
			final Arbre a = f.retirer();
			s += a.getRegles().toString() + "\n";
			Arbre cour = a.getFrere();
			while (cour != null) {
				f.ajouter(cour);
				cour = cour.getFrere();
			}
			if (a.getFils() != null) {
				f.ajouter(a.getFils());
			}
		}
		return s;
	}
	
	/**
	 * Methode de remplissage de l'arbre
	 * 
	 * @param s
	 *            le fichier a analyser
	 */
	private void creerLexique(final String s) throws AnalyseException {
		final GenerateurRegle ana = new GenerateurRegle(s);
		Regle a = ana.nouvelleRegle();
		while (!ana.vide()) {
			this.ajouter(a);
			a = ana.nouvelleRegle();
		}
		ana.close();
	}
	
	/**
	 * Pour obtenir le frere de l'arbre
	 * 
	 * @return l'arbre frere
	 */
	private Arbre getFrere() {
		return this.frere;
	}
	
	/**
	 * Pour obtenir le fils de l'arbre
	 * 
	 * @return l'arbre fils
	 */
	private Arbre getFils() {
		return this.fils;
	}
	
	/**
	 * Pour obtenir la lettre presente a la racine
	 * 
	 * @return la lettre en question
	 */
	private String getLettre() {
		return this.lettre;
	}
	
	/**
	 * Pour obtenir la liste de regles de l'arbre
	 * 
	 * @return une instance de <code>ListeRegles</code> ad'hoc.
	 */
	private ListeRegles getRegles() {
		return this.regles;
	}
	
	/**
	 * Pour modifier le frere d'un arbre
	 * 
	 * @param l
	 *            la lettre presente a la racine de nouveau frere
	 */
	private void ajouterFrere(final String l) {
		final Arbre ar = new Arbre();
		ar.lettre = l;
		this.frere = ar;
	}
	
	/**
	 * Pour modifier le fils d'un arbre
	 * 
	 * @param l
	 *            la lettre presente a la racine de nouveau fils
	 */
	private void ajouterFils(final String l) {
		final Arbre ar = new Arbre();
		ar.lettre = l;
		this.fils = ar;
	}
	
	/**
	 * Pour ajouter en tete de l'arbre
	 * 
	 * @param l
	 *            la
	 */
	private void ajouterDebut(final String l) {
		final Arbre f = new Arbre(this);
		this.frere = f;
		this.fils = null;
		this.lettre = l;
		this.regles = new ListeRegles();
	}
	
	/**
	 * Ajouter une regle a la liste de regles de <code>this</code>.
	 * 
	 * @param regle
	 *            la regle que l'on veut ajouter
	 */
	private void ajouter(final Regle regle) {
		ajouter(regle.getRacine(), regle);
	}
	
	/**
	 * ajoute une regle a la liste de regles, par rapport a une chaine de caractere.
	 * 
	 * @param mot
	 *            la chaine de caractere 'racine'.
	 * @param regle
	 *            la regle a ajouter.
	 */
	private void ajouter(final String mot, final Regle regle) {
		final String lettre = mot.substring(0, 1);
		final String fin = mot.substring(1);
		if (lettre.equals(getLettre())) {
			if (fin.length() == 0) {
				this.regles.ajouter(regle);
			} else {
				if (getFils() == null) {
					ajouterFils(fin.substring(0, 1));
				}
				getFils().ajouter(fin, regle);
			}
		} else if (lettre.compareTo(getLettre()) > 0) {
			if (getFrere() == null) {
				ajouterFrere(lettre);
			}
			getFrere().ajouter(mot, regle);
		} else {
			ajouterDebut(lettre);
			ajouter(mot, regle);
		}
	}
	
	/**
	 * Pour trouver la liste de phoneme correspondant a un mot a partir d'un indice
	 * <p>
	 * <b>Remarque</b> : on choisit la regle qui permet d'unifier la plus grande chaine commencant a l'indice i.
	 * </p>
	 * <p>
	 * <b>Postcondition</b> : i designe la prochaine lettre a analyser
	 * </p>
	 * 
	 * @param mot
	 *            le mot a analyser
	 * @param i
	 *            l'indice a partir duquel on analyse
	 * @return la liste des phoneme qui va bien(a partir de l'indice i)
	 */
	private String trouverPhoneme(final String mot, final Indice i) {
		String res = ListeRegles.PAS_DE_REGLE;
		final String lettre = mot.substring(i.val(), i.val() + 1);
		
		if (lettre.equals(getLettre())) {
			i.inc();
			if (!i.egal(mot.length()) && (getFils() != null)) {
				final int saveInd = i.val();
				final String resFils = getFils().trouverPhoneme(mot, i);
				if (!vide(resFils)) {
					res = resFils;
				} else {
					i.val(saveInd);
					res = getRegles().trouverPhoneme(mot, i.val());
				}
			} else {
				res = getRegles().trouverPhoneme(mot, i.val());
			}
		} else if (lettre.compareTo(getLettre()) > 0) {
			if (getFrere() != null) {
				final int saveInd = i.val();
				final String resFrere = getFrere().trouverPhoneme(mot, i);
				if (!vide(resFrere)) {
					res = resFrere;
				} else {
					i.val(saveInd);
				}
			}
		} else {
			res = ListeRegles.PAS_DE_REGLE;
		}
		return res;
	}
	
	/**
	 * Pour savoir si une chaine de caractere est egale au tag "PAS DE REGLES".
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le cas, false sinon
	 */
	private static boolean vide(final String s) {
		return s.equals(ListeRegles.PAS_DE_REGLE);
	}
	
	/**
	 * Classes privee, utilisee pour l'affichage de l'arbre (debuggage)
	 */
	
	private class FileArbre {
		private ChaineArbre	entree;
		private ChaineArbre	sortie;
		
		public FileArbre() {
			this.entree = null;
			this.sortie = null;
		}
		
		private FileArbre(final FileArbre f) {
			this.entree = f.entree;
			this.sortie = f.sortie;
		}
		
		/**
		 * Ajoute un nouvel objet dans la file.
		 */
		public void ajouter(final Arbre objet) {
			if (vide()) {
				this.entree = new ChaineArbre(objet);
				this.sortie = this.entree;
			} else {
				if (!dejaMis(objet, new FileArbre(this))) {
					this.entree.suiv = new ChaineArbre(objet);
					this.entree = this.entree.suiv;
				}
			}
		}
		
		private boolean dejaMis(final Arbre a, final FileArbre f) {
			if (f.vide()) {
				return false;
			} else {
				final Arbre af = f.retirer();
				if ((af.getRegles().toString()).equals(a.getRegles().toString())) {
					return true;
				} else {
					return dejaMis(a, f);
				}
			}
		}
		
		/**
		 * Retire un objet de la file et retourne cet objet.
		 * Precondition : la file n'est pas vide.
		 */
		public Arbre retirer() {
			final Arbre x = this.sortie.a;
			this.sortie = this.sortie.suiv;
			if (this.sortie == null) {
				this.entree = null;
			}
			return x;
		}
		
		/**
		 * Retourne l'objet situe en tete de la file.
		 * Precondition : la file n'est pas vide.
		 */
		@SuppressWarnings("unused")
		public Arbre suivant() {
			return this.sortie.a;
		}
		
		/**
		 * Teste si la file est vide.
		 */
		public boolean vide() {
			return this.entree == null;
		}
		
		/**
		 * Teste si la file est pleine.
		 */
		@SuppressWarnings("unused")
		public boolean pleine() {
			return false;
		}
	}
	
	private class ChaineArbre {
		private final Arbre	a;
		private ChaineArbre	suiv;
		
		public ChaineArbre(final Arbre a) {
			this.a = a;
		}
		
		@SuppressWarnings("unused")
		public ChaineArbre(final Arbre a, final ChaineArbre s) {
			this.a = a;
			this.suiv = s;
		}
	}
}
