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

package t2s.son;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import t2s.exception.AnalyseException;
import t2s.exception.PlusDePhraseException;
import t2s.prosodie.ListePhonemes;
import t2s.traitement.Arbre;
import t2s.traitement.Phrase;
import t2s.traitement.Pretraitement;
import t2s.util.ConfigFile;
import t2s.util.LecteurFichier;

/**
 * Classe LecteurTexte
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */

public class LecteurTexte {
	
	// le fichier pour ecrire les wav ou pho
	private final static String	FICHIER_PHO_WAV	   = ConfigFile.rechercher("FICHIER_PHO_WAV");
	private final static String	REPERTOIRE_PHO_WAV	= ConfigFile.rechercher("REPERTOIRE_PHO_WAV");
	
	private final String	    outFile;
	private String	            voix;
	private Arbre	            arbre;
	private Pretraitement	    pt;
	private boolean	            vide;
	private BufferedReader	    cin;
	private JukeBox	        jk;
	
	private SynthetiseurMbrola	sb;
	
	/**
	 * Constructeur complet de Lecteur de Texte
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le texte a lire
	 * @param of
	 *            le fichier ou l'on ecrit les phonemes a prononcer
	 * @param v
	 *            la voix a utiliser pour lire ces phonemes
	 * @param isFile
	 *            Lit - on depuis un fichier ou depuis l'entree standard ?
	 */
	public LecteurTexte(JukeBox jk, final String s, final String of, final String v, final boolean isFile) {
		this.outFile = of;
		this.vide = false;
		this.voix = v;
		this.sb = null;
		boolean stdin;
		if ("-".equals(s)) {
			stdin = true;
			this.cin = new BufferedReader(new InputStreamReader(System.in));
		} else {
			stdin = false;
			final String texte = (isFile) ? (new LecteurFichier(s)).toutLire() : s;
			this.pt = new Pretraitement(texte);
		}
		try {
			this.arbre = new Arbre("");
		} catch (final AnalyseException a) {
			System.out.println(a);
		}
		if (stdin) {
			interactifMode();
		}
	}
	
	/**
	 * Methode d'affichage standard
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return une chaine de caractere ad'hoc
	 */
	@Override
	public String toString() {
		String s = "";
		s = "  Sortie :=" + this.outFile + "\n";
		return s;
	}
	
	/**
	 * Constructeur allege utilisant des valeurs par defaut
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le texte a lire
	 * @param isFile
	 *            Lit-on depuis un fichier ?
	 */
	public LecteurTexte(JukeBox jk, final String s, final boolean isFile) {
		this(jk, s, REPERTOIRE_PHO_WAV + "/" + FICHIER_PHO_WAV, SynthetiseurMbrola.VOIX1, isFile);
	}
	
	public LecteurTexte(JukeBox jk, final File f, final String out) {
		this(jk, f.toString(), out, SynthetiseurMbrola.VOIX1, true);
	}
	
	/**
	 * Constructeur completement allege
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param s
	 *            le texte a lire
	 */
	public LecteurTexte(String s, JukeBox jk) {
		this(jk, s, false);
	}
	
	/**
	 * Constructeur vide
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public LecteurTexte(JukeBox jk) {
		this(jk, "", false);
	}
	
	/**
	 * Pour lire du texte en mode interactif.
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	private void interactifMode() {
		try {
			String text = "";
			String ligne;
			while (true) {
				ligne = this.cin.readLine();
				if (ligne != null) {
					if (ligne.equals("<QUIT>")) {
						System.exit(0);
					} else if (!ligne.equals("<END>")) {
						text = text + ligne;
					} else {
						this.pt = new Pretraitement(text);
						muet();
						System.out.println(this.outFile + ".wav");
						this.vide = false;
						text = "";
					}
				} else {
					try {
						System.out.println("sleeping...");
						Thread.sleep(200);
					} catch (final InterruptedException e) {
						System.out.println("InterruptedException in interactifMode()");
					}
				}
			}
		} catch (final IOException e) {
			System.out.println("<IOException>");
		}
	}
	
	/**
	 * Pour mettre a jour la voix utilise par le synthetiseur
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param v
	 *            le numero de la voix (parmi 1,2, 3, 5, 6 ou 7 (defaut))
	 */
	public void setVoix(int v) {
		final int nbvoix = Integer.parseInt(ConfigFile.rechercher("NBVOIX"));
		v = (v > nbvoix) ? nbvoix : v;
		switch (v) {
			case 1:
				this.voix = SynthetiseurMbrola.VOIX1;
				break;
			case 2:
				this.voix = SynthetiseurMbrola.VOIX2;
				break;
			case 3:
				this.voix = SynthetiseurMbrola.VOIX3;
				break;
			case 4:
				this.voix = SynthetiseurMbrola.VOIX4;
				break;
			case 5:
				this.voix = SynthetiseurMbrola.VOIX5;
				break;
			case 6:
				this.voix = SynthetiseurMbrola.VOIX6;
				break;
			case 7:
				this.voix = SynthetiseurMbrola.VOIX7;
				break;
			default:
				this.voix = SynthetiseurMbrola.VOIX1;
				break;
		}
	}
	
	public String getVoix() {
		return this.voix;
	}
	
	/**
	 * Pour savoir si le texte est vide
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public boolean vide() {
		return this.vide;
	}
	
	/**
	 * Pour changer le texte a lire
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public void setTexte(final String s) {
		if (s == null) {
			throw new IllegalArgumentException("texte vide");
		}
		this.pt = new Pretraitement(s);
		this.vide = false;
	}
	
	/**
	 * Pour recharger les fichiers de regles (reconstruction de l'arbre)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param pathRegle
	 *            l'emplacement du repertoire contenant les regles
	 * @param pathPrepo
	 *            l'emplacement contenant les prepositions (DEPRECATED)
	 */
	public void reloadArbre() throws AnalyseException {
		this.arbre = new Arbre("");
	}
	
	/**
	 * Pour lire le texte phrase par phrase avec Mbrola
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la chaine de caractere represenant la liste de phonemes a prononcer.
	 */
	public String play() {
		ListePhonemes l = new ListePhonemes();
		try {
			final Phrase p = this.pt.nouvellePhrase();
			if (p != null) {
				l = new ListePhonemes(this.arbre.trouverPhoneme(p.getPhrase()), p.getProsodie());
				System.out.println(this.outFile);
				l.ecrirePhonemes(this.outFile + ".pho");
				this.sb = new SynthetiseurMbrola(jk, this.voix, REPERTOIRE_PHO_WAV + "/", FICHIER_PHO_WAV);
				this.sb.play(true);
			}
		} catch (final PlusDePhraseException e) {
			this.vide = true;
		}
		return l.toString();
	}
	
	/**
	 * Pour lire la totalite d'un texte avec Mbrola
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la chaine de caracteres representant la liste de phonemes a prononcer.
	 */
	public String playAll() {
		final ListePhonemes l = new ListePhonemes();
		try {
			Phrase p = this.pt.nouvellePhrase();
			while (true) {
				l.ajouterPhonemes(this.arbre.trouverPhoneme(p.getPhrase()), p.getProsodie());
				p = this.pt.nouvellePhrase();
			}
		} catch (final PlusDePhraseException e) {
			this.vide = true;
			l.ecrirePhonemes(this.outFile + ".pho");
			this.sb = new SynthetiseurMbrola(jk, this.voix, REPERTOIRE_PHO_WAV + "/", FICHIER_PHO_WAV);
			this.sb.play(true);
		}
		return l.toString();
	}
	
	/**
	 * Pour generer un fichier de phoneme en mode silencieux (ne rien prononcer)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param mb
	 *            l'emplacement de MBROLA.
	 */
	public String muet() {
		final ListePhonemes l = new ListePhonemes();
		try {
			Phrase p = this.pt.nouvellePhrase();
			while (true) {
				l.ajouterPhonemes(this.arbre.trouverPhoneme(p.getPhrase()), p.getProsodie());
				p = this.pt.nouvellePhrase();
				l.ecrirePhonemes(this.outFile + ".pho");
			}
		} catch (final PlusDePhraseException e) {
			this.vide = true;
			this.sb = new SynthetiseurMbrola(jk, this.voix, REPERTOIRE_PHO_WAV + "/", FICHIER_PHO_WAV);
			this.sb.muet();
		}
		return l.toString();
	}
}
