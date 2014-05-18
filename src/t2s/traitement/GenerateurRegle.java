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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import t2s.exception.AnalyseException;
import t2s.util.ConfigFile;

/**
 * Un generateur de Regles, ecrites au format PERL dans un fichier texte.
 */
public class GenerateurRegle {
	
	private BufferedReader	br;
	private Hashtable<String, String>	   ensembles;
	private int	           noLigne;
	private boolean	       vide;
	
	/**
	 * Construction standart d'un generateur de regles a partir d'un fichier.
	 * 
	 * @param path
	 *            le chemin d'acces au fichier utilise (encodage <code>ISO-8859-1</code>)
	 */
	public GenerateurRegle(final String path) {
		try {
			final FileInputStream fos = new FileInputStream(path);
			this.br = new BufferedReader(new InputStreamReader(fos, ConfigFile.rechercher("ENCODAGE_FICHIER")));
			this.ensembles = new Hashtable<String, String>();
			this.noLigne = 0;
			this.vide = false;
			initEnsembles();
		} catch (final FileNotFoundException e) {
			System.out.println("SI_VOX WARNING [GenerateurRegle] :  Erreur lors du chargement du fichier de regles");
		} catch (final AnalyseException e) {
			System.out.println("SI_VOX WARNING [GenerateurRegle] : Une erreur d'analyse est survenue !");
			System.out.println(e);
		} catch (final UnsupportedEncodingException e) {
			System.out.println("SI_VOX WARNING [GenerateurRegle] : Encodage inconnu");
		}
	}
	
	/**
	 * Pour savoir s'il reste encore des regles a lire dans le fichier
	 * 
	 * @return true si on n'a plus de regles a lire, false sinon.
	 */
	public boolean vide() {
		return this.vide;
	}
	
	/**
	 * Femreture en lecture du fichier de regles.
	 */
	public void close() {
		try {
			this.br.close();
		} catch (final Exception e) {
			System.out.println("SI_VOX WARNING [GenerateurRegle.close] : Erreur lors de la fermeture du fichier de r�gles !");
		}
	}
	
	/**
	 * Analyse d'une ligne du fichier (chargement dans le tampon et transformation)
	 * 
	 * @param br
	 *            le lecteur bufferise servant a faire la lecture du fichier.
	 * @return une instance de <code>StringTokenizer</code> associe aux caracteres <code>'	','n'</code> et <code>''</code>
	 */
	public StringTokenizer tokensLine(final BufferedReader br) throws AnalyseException {
		String linein = "";
		try {
			while ((linein != null) && (linein.equals("") || comment(linein))) {
				linein = br.readLine();
				this.noLigne++;
			}
		} catch (final IOException e) {
			erreur(1);
		}
		if (linein == null) {
			erreur(9);
		}
		return new StringTokenizer(linein, " 	\n\n");
	}
	
	/**
	 * Pour initialiser les classes de lettres (initialisation du generateur)
	 * <p>
	 * <b>Remarque</b> : Modifie par effet de bord la table de hachage globale des ensembles de lettres
	 * </p>
	 */
	public void initEnsembles() throws AnalyseException {
		final StringTokenizer line = tokensLine(this.br);
		if (line.hasMoreTokens()) {
			final String first = line.nextToken();
			if (classes(first)) {
				initClasses();
			} else if (!rules(first)) {
				erreur(2);
			}
		} else {
			erreur(1);
		}
	}
	
	/**
	 * Pour creer l'ensemble des classes
	 */
	private void initClasses() throws AnalyseException {
		String first = "";
		do {
			final StringTokenizer line = tokensLine(this.br);
			if (line.hasMoreTokens()) {
				first = line.nextToken();
				if (classs(first)) {
					initOneClass(line);
				} else if (!rules(first)) {
					erreur(2);
				}
			}
		} while (!rules(first));
	}
	
	/**
	 * Pour creer une classe unique
	 * Precondition :
	 * On arrive ici APRES avoir reconnu le tag de classe dans line
	 * 
	 * @param line
	 *            le <code>StringTokenizer</code> que l'on veut analyser
	 */
	private void initOneClass(final StringTokenizer line) throws AnalyseException {
		String name = "";
		String value = "";
		if (line.hasMoreTokens()) {
			name = line.nextToken();
			if (!majuscules(name)) {
				erreur(3);
			}
			if (this.ensembles.containsKey(name)) {
				erreur(4);
			}
		} else {
			erreur(5);
		}
		if (line.hasMoreTokens()) {
			value = line.nextToken();
			if (comment(value)) {
				erreur(6);
			}
			value = analyseExpReg(value);
		} else {
			erreur(6);
		}
		this.ensembles.put(name, value);
	}
	
	/**
	 * Pour analyser une expression reguliere
	 * Postcondition :
	 * On aura leve une exception si :
	 * - La chaine ne correspond pas a une expression reguliere de syntaxe correcte
	 * - Il y a des majuscules qui ne correspondent pas a un ident de classe
	 * 
	 * @return la chaine dans laquelle on a substitue les ident de classe par leurs valeurs
	 */
	
	private String analyseExpReg(final String value) throws AnalyseException {
		try {
			Pattern.compile(value);
		} catch (final PatternSyntaxException pe) {
			erreur(7);
		}
		String res = "";
		final int lg = value.length();
		int i = 0;
		
		while (i < lg) {
			char cour = value.charAt(i);
			i++;
			String ident = "";
			if (!majuscule(cour)) {
				res += cour;
			} else {
				ident += cour;
				while ((i < lg) && majuscule(cour) && !this.ensembles.containsKey(ident)) {
					cour = value.charAt(i);
					i++;
					if (majuscule(cour)) {
						ident += cour;
					}
				}
				if (this.ensembles.containsKey(ident)) {
					res = res + this.ensembles.get(ident);
				} else {
					erreur(8);
				}
			}
		}
		return res;
	}
	
	/**
	 * Fabrication d'une nouvelle regle a partir d'une ligne du fichier .
	 * <p>
	 * <b>Definition</b> : Une regle est de la forme suivante :
	 * <p>
	 * <center> <code> pref [[ <racine> ]] suf -> phoneme </code> </center>
	 * </p>
	 * <p>
	 * <ul>
	 * <li> <code>pref</code> et <code>suf</code> sont des expressions regulieres syntaxiquement correctes</li>
	 * <li> <code>racine</code> et <code>phoneme</code> sont des chaines de caracteres standarts
	 * </ul>
	 * </p>
	 * 
	 * @return une nouvelle Regle construite a partir de ce qu'on vient de lire.
	 * @throws AnalyseException
	 */
	public Regle nouvelleRegle() throws AnalyseException {
		final StringTokenizer line = tokensLine(this.br);
		if (!line.hasMoreTokens()) {
			erreur(9);
		}
		String courant = line.nextToken();
		if (fin(courant)) {
			this.vide = true;
			return null;
		} else {
			String pref;
			if (ouvrant(courant)) {
				pref = "";
			} else {
				pref = analyseExpReg(courant);
				if (!line.hasMoreTokens()) {
					erreur(10);
				}
				courant = line.nextToken();
				if (!ouvrant(courant)) {
					erreur(10);
				}
			}
			final String racine = analyseRacine(line);
			if (!line.hasMoreTokens()) {
				erreur(11);
			}
			courant = line.nextToken();
			String suf;
			if (fleche(courant)) {
				suf = "";
			} else {
				suf = analyseExpReg(courant);
				if (!line.hasMoreTokens()) {
					erreur(11);
				}
				courant = line.nextToken();
				if (!fleche(courant)) {
					erreur(11);
				}
			}
			final String phoneme = analysePhoneme(line);
			return new Regle(pref, racine, suf, phoneme);
		}
	}
	
	/**
	 * Pour analyser la racine
	 * Precondition :
	 * 1. On vient de lire le tag '[['
	 * Postcondition :
	 * 1. si la chaine est correcte, line commence apres le tag ']]'
	 * 
	 * @param line
	 *            le tokenizer de la ligne que l'on analyse.
	 * @return La chaine de caracteres qui represente la racine.
	 */
	private String analyseRacine(final StringTokenizer line) throws AnalyseException {
		if (!line.hasMoreTokens()) {
			erreur(12);
		}
		String courant = line.nextToken();
		if (!minuscules(courant)) {
			erreur(13);
		}
		final String racine = courant;
		if (!line.hasMoreTokens()) {
			erreur(14);
		}
		courant = line.nextToken();
		if (!fermant(courant)) {
			erreur(14);
		}
		return racine;
	}
	
	/**
	 * Pour analyser le phoneme
	 * Precondition :
	 * 1. on vient de lire le tage de transition '->'
	 * Postcondition :
	 * 1. si la chaine est correcte, 'line' est vide
	 * 
	 * @param line
	 *            le tokenizer de la ligne que l'on analyse.
	 * @return La chaine de caracteres qui represente la racine.
	 */
	private String analysePhoneme(final StringTokenizer line) throws AnalyseException {
		String pho = "";
		String courant = "";
		while (line.hasMoreTokens() && !comment(courant)) {
			courant = line.nextToken();
			if (!comment(courant)) {
				pho = pho + " " + courant;
			}
		}
		return pho;
	}
	
	/**
	 * teste si une chaine est entierement en majuscule
	 * 
	 * @param s
	 *            la chaine en question
	 * @return true si c'est le cas, false sinon
	 */
	private static boolean majuscules(final String s) {
		for (int i = 0; i < s.length(); i++) {
			if (!majuscule(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Teste si une chaine est composee de lettre acceptee comme racine
	 * Definition :
	 * accpetee comme faisant partie de la racine = lettre minuscule, ' ou _
	 * 
	 * @param s
	 *            la chaine de caractere a analyser
	 * @return true si c'est bon, false sinon.
	 */
	private static boolean minuscules(final String s) {
		for (int i = 0; i < s.length(); i++) {
			final char c = s.charAt(i);
			if ((c != '\'') && (c != '_') && (c != '~') && (c != '�')) {
				if (!Character.isLowerCase(c)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Teste si un caractere est en majuscule
	 * 
	 * @param c
	 *            le caractere en question
	 * @return true si c'est bon, false sinon
	 */
	private static boolean majuscule(final char c) {
		return Character.isUpperCase(c);
	}
	
	/**
	 * Teste si une chaine est un commentaire
	 * Definition :
	 * commentaire = ligne commencant par #
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est un commentaire, false sinon
	 */
	private boolean comment(final String s) {
		if (s.length() != 0) {
			int i;
			for (i = 0; (i < s.length()) && (s.charAt(i) == ' '); i++) {
			}
			if (i < s.length()) {
				return s.charAt(i) == '#';
			}
		}
		return false;
	}
	
	/**
	 * Teste si une chaine est le tag de fin de fichier
	 * Definition :
	 * tag de fin de fichier = mot cle 'END'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean fin(final String s) {
		return s.equals("END");
	}
	
	/**
	 * Teste si une chaine est le tag d'ouverture de racine
	 * Definition :
	 * tag d'ouverture de racine = mot cle '[['
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean ouvrant(final String s) {
		return s.equals("[[");
	}
	
	/**
	 * Teste si une chaine est le tag de fermeture de racine
	 * Definition :
	 * tag de fermeture de racine = mot cle ']]'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean fermant(final String s) {
		return s.equals("]]");
	}
	
	/**
	 * Teste si une chaine est le tag de transition
	 * Definition :
	 * tag de transition = mot cle '->'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean fleche(final String s) {
		return s.equals("->");
	}
	
	/**
	 * Teste si une chaine est le tag de debut de definition des classes
	 * Definition :
	 * tag de definition des classes = mot cle 'CLASSES'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean classes(final String s) {
		return s.equals("CLASSES");
	}
	
	/**
	 * teste si une chaine est le tag de classe
	 * Definition :
	 * tag de classe = mot cle 'CLASS'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean classs(final String s) {
		return s.equals("CLASS");
	}
	
	/**
	 * teste si une chaine est le tag de regles
	 * Definition :
	 * tag de regles = mot cle 'RULES'
	 * 
	 * @param s
	 *            la chaine a tester
	 * @return true si c'est le tag en question, false sinon
	 */
	private boolean rules(final String s) {
		return s.equals("RULES");
	}
	
	/**
	 * Methode pour transmettre les exceptions selon le type d'erreur
	 * 
	 * @param i
	 *            l'entier correspondant au code d'erreur rencontree
	 */
	private void erreur(final int i) throws AnalyseException {
		switch (i) {
			case 1:
				throw new AnalyseException("Fin fichier de regles", "fin du fichier de raigle", this.noLigne);
			case 2:
				throw new AnalyseException("Manque mot clef CLASSES ou RULES", "il manque le mo clai classe ou roulz", this.noLigne);
			case 3:
				throw new AnalyseException("Les mots clefs doivent etre en majuscules", "les mo clai doive etre en majuscule", this.noLigne);
			case 4:
				throw new AnalyseException("Identificateur deja utilise", "identificateur daija utilisai", this.noLigne);
			case 5:
				throw new AnalyseException("Manque nom de classe", "il manque le nom de classe", this.noLigne);
			case 6:
				throw new AnalyseException("Manque definition de classe", "il manque la daifinission de la classe", this.noLigne);
			case 7:
				throw new AnalyseException("Mauvaise syntaxe d'expression reguliere : ", "movaise sintax daixpraission raiguliaire", this.noLigne);
			case 8:
				throw new AnalyseException("Majuscules interdites en dehors des identificateurs d'ensembles", "majuscule intairdite", this.noLigne);
			case 9:
				throw new AnalyseException("Tag END attendu", "le tague fin ai attendu", this.noLigne);
			case 10:
				throw new AnalyseException("Tag [[ attendu", "le tague crochai ouvran crochai ouvran ai attendu", this.noLigne);
			case 11:
				throw new AnalyseException("Tag -> attendu", "le tague flaiche ai attendu", this.noLigne);
			case 12:
				throw new AnalyseException("Racine de regle attendue", "la racine des raigle ai attendu", this.noLigne);
			case 13:
				throw new AnalyseException("La racine de regle doit etre en minuscules", "la racine des raigle doi etre en minuscule", this.noLigne);
			case 14:
				throw new AnalyseException("Tag ]] attendu", "le tague crochai fermai fermai ai attendu", this.noLigne);
		}
	}
}
