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
import java.util.StringTokenizer;

import t2s.exception.AnalyseException;
import t2s.util.ConfigFile;

/**
 * Classe d'analyse du fichier qui contient la liste des prepositions.
 * <p>
 * <b> Remarque </b>: selon la preposition, on fera une pause longue ou courte.
 * </p>
 */
public class GenerateurPreposition {
	
	/** Une pause courte */
	public static final int	COURT	= 1;
	/** Une pause longue */
	public static final int	LONG	= 0;
	/** Absence de pause */
	public static final int	VIDE	= -1;
	
	private BufferedReader	br;
	private int	            noLigne;
	private int	            duree;
	private boolean	        vide;
	
	/**
	 * Construit un generateur de regle a partir d'un fichier de preposition (encodage <code>ISO-8859-1</code>)
	 * 
	 * @param path
	 *            le chemin d'acces au fichier de prepositions
	 */
	public GenerateurPreposition(final String path) {
		try {
			final FileInputStream fos = new FileInputStream(path);
			this.br = new BufferedReader(new InputStreamReader(fos, ConfigFile.rechercher("ENCODAGE_FICHIER")));
			this.noLigne = 0;
			this.vide = false;
			this.duree = VIDE;
		} catch (final FileNotFoundException e) {
			System.out.println("SI_VOX WARNING [GenerateurPreposition] : Erreur lors du chargement du fichier de regles" + path);
			System.out.println(e);
		} catch (final UnsupportedEncodingException e) {
			System.out.println("SI_VOX WARNING [GenerateurPreposition] : Encodage inconnu");
			System.out.println(e);
		}
	}
	
	/**
	 * Pour savoir s'il reste encore des regles a lire
	 * 
	 * @return false s'il reste encore des regles a lire, true sinon.
	 */
	public boolean vide() {
		return this.vide;
	}
	
	/** Pour fermer en lecture le fichier de preposition specifie dans le constructeur. */
	public void close() {
		try {
			this.br.close();
		} catch (final Exception e) {
			System.out.println("SI_VOX WARNING [GenerateurPreposition.close] : Erreur lors de la fermeture du fichier de regles");
		}
	}
	
	/**
	 * Pour lire une ligne a notre maniere dans un lecteur bufferise
	 * 
	 * @param br
	 *            le lecteur bufferise en question
	 * @return une instance de <code>StringTokenizer</code>, reference par les caracteres <code>'	', '
'</code> et <code>''</code>
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
			erreur(4);
		}
		return new StringTokenizer(linein, " 	\n");
	}
	
	/**
	 * Pour analyser une ligne du fichier, et en produire une instance de Regle.
	 * <p>
	 * <b>Definition</b> : une preposition est de la forme suivante
	 * </p>
	 * <p>
	 * <center> <code>preposition -> phoneme </code></center>
	 * </p>
	 * <p>
	 * <ul>
	 * <li> <code>'preposition'</code> et <code>'phoneme'</code> sont des chaines de caracteres.</li>
	 * <li> <code>'preposition'</code> est en minuscules et les <code>' '</code> ont ete remplaces par <code>'_'</code>.</li>
	 * </ul>
	 * </p>
	 * 
	 * @return une nouvelle Regle sur une preposition
	 * @throws AnalyseException
	 */
	public Regle nouvellePreposition() throws AnalyseException {
		StringTokenizer line = tokensLine(this.br);
		if (!line.hasMoreTokens()) {
			erreur(4);
		}
		String courant = line.nextToken();
		if (fin(courant)) {
			this.vide = true;
			return null;
		} else {
			if (pause(courant)) {
				line = tokensLine(this.br);
				if (!line.hasMoreTokens()) {
					erreur(6);
				}
				courant = line.nextToken();
				if (pauseLongue(courant)) {
					this.duree = LONG;
				} else {
					this.duree = COURT;
				}
			} else if (this.duree == VIDE) {
				erreur(2);
			}
			if (!minuscules(courant)) {
				erreur(3);
			}
			String preposition = courant;
			if (!line.hasMoreTokens()) {
				erreur(5);
			}
			courant = line.nextToken();
			if (!fleche(courant)) {
				erreur(5);
			}
			String phoneme = analysePhoneme(line);
			if (phoneme.equals("")) {
				erreur(7);
			}
			preposition = "_".concat(preposition);
			if (!preposition.endsWith("qu'")) {
				preposition = preposition.concat("_");
			}
			final String debut = (this.duree == COURT) ? " % " : " %% ";
			phoneme = debut.concat(phoneme);
			return new Regle(preposition, phoneme);
		}
	}
	
	/**
	 * Analyse et renvoie le phoneme associe a la ligne courante
	 * Precondition :
	 * on vient de lire la chaine '->'
	 * Postcondition :
	 * Si la chaine est correcte, line est vide
	 * 
	 * @param line
	 *            le StringTokenizer de la ligne courante
	 * @return une String contenant la liste des phoneme de la preposition
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
	 * Teste si une chaine est acceptee comme preposition
	 * Definition :
	 * Chaine acceptee = contient des lettres minuscules, ' ou _
	 * 
	 * @return true si c'est bon, false sinon
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
	 * Teste si une chaine est un commentaire
	 * Definition :
	 * commentaire = ligne commencant par #
	 * 
	 * @param s
	 *            la chaine de caractere representant la ligne a analyser
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
	 * tag de fin de fichier = mots cle END
	 * 
	 * @return true si on est la fin, false sinon
	 */
	private boolean fin(final String s) {
		return s.equals("END");
	}
	
	/**
	 * Teste si une chaine est le tag de transition.
	 * Definition :
	 * tag de transition : La fleche '->'
	 * 
	 * @param s
	 *            la chaine a analyser
	 * @return true si c'est le bon tag, false sinon
	 */
	private boolean fleche(final String s) {
		return s.equals("->");
	}
	
	/**
	 * Teste si une chaine est le tag de pause courte.
	 * Definition :
	 * tag de pause courte = mot cle "PAUSE_COURTE"
	 * 
	 * @param s
	 *            la chaine a analyser
	 * @return true si c'est le bon tag, false sinon
	 */
	private boolean pauseCourte(final String s) {
		return s.equals("PAUSE_COURTE");
	}
	
	/**
	 * Teste si une chaine est le tag de pause longue
	 * Definition :
	 * tag de pause longue = mot cle "PAUSE_LONGUE"
	 * 
	 * @param s
	 *            la chaine a analyser
	 * @return true si c'est le bon tag, false sinon
	 */
	private boolean pauseLongue(final String s) {
		return s.equals("PAUSE_LONGUE");
	}
	
	/**
	 * teste si une chaine est un tag de pause (courte ou longue)
	 * 
	 * @param s
	 *            la chaine a analyser
	 * @return true si c'est un tag de pause, false sinon
	 */
	private boolean pause(final String s) {
		return pauseCourte(s) || pauseLongue(s);
	}
	
	/**
	 * Methode pour transmettre les exceptions
	 */
	private void erreur(final int i) throws AnalyseException {
		switch (i) {
			case 1:
				throw new AnalyseException("Fin fichier de regles", "fin du fichier de raigle", this.noLigne);
			case 2:
				throw new AnalyseException("Manque mot clef PAUSE_COURTE ou PAUSE_LONGUE", "il manque le mo clai pause courte ou pause longue", this.noLigne);
			case 3:
				throw new AnalyseException("Les prepositions doivent etre en minuscules, les espaces doivent etre remplaces par des _", "", this.noLigne);
			case 4:
				throw new AnalyseException("tag END attendu", "tague de fin attendu", this.noLigne);
			case 5:
				throw new AnalyseException("tag '->' attendu", "tague flaiche attendu", this.noLigne);
			case 6:
				throw new AnalyseException("Preposition attendue", "praipozission attendu", this.noLigne);
			case 7:
				throw new AnalyseException("Phonemes de la preposition attendue", "fonaime de la praipozission attendu", this.noLigne);
		}
	}
}
