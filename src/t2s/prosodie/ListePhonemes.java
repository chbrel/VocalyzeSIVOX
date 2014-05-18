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

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import t2s.traitement.Phrase;

/**
 * lasse representant les phonemes d'une phrase
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */
public class ListePhonemes {
	
	public static final String	PAUSE	= "_";
	private final List<Phoneme>	phonemes;
	
	/**
	 * Constructeur par defaut de ListePhonemes
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public ListePhonemes() {
		this.phonemes = new ArrayList<Phoneme>();
	}
	
	/**
	 * Constructeur de liste de phoneme pour une phrase donnees
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ph
	 *            la phrase en question
	 * @param ponct
	 *            la ponctuation de cette phrase
	 */
	public ListePhonemes(final String ph, final int ponct) {
		this.phonemes = genererPhonemes(ph, ponct);
	}
	
	/**
	 * Pour ajouter les phonemes d'une phrase
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ph
	 *            la phrase que l'on veut rajouter
	 * @param ponct
	 *            la ponctuation de cette phrase
	 */
	public void ajouterPhonemes(final String ph, final int ponct) {
		this.phonemes.addAll(genererPhonemes(ph, ponct));
	}
	
	/**
	 * Pour creer le vecteur des phonemes de la phrase avec la prosodie
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ph
	 *            la phrase en question
	 * @param ponct
	 *            la ponctuation de cette phrase.
	 * @return un Vector contenant les phonemes de la phrase
	 */
	private static List<Phoneme> genererPhonemes(String ph, final int ponct) {
		Syntagme s = new Syntagme();
		final List<Phoneme> result = new ArrayList<Phoneme>();
		List<Phoneme> tempo = new ArrayList<Phoneme>();
		int coupure = ph.indexOf("%");
		while (coupure != -1) {
			if (coupure != 1) {
				s = Syntagme.type(ph, coupure);
				tempo = genererLesPhonemes(ph, coupure - 1, s);
				appliquerProsodie(tempo, s);
				result.addAll(tempo);
			}
			ph = elaguer(ph, coupure);
			coupure = ph.indexOf("%");
		}
		s = Syntagme.typeFin(ponct);
		tempo = genererLesPhonemes(ph, ph.length(), s);
		appliquerProsodie(tempo, s);
		result.addAll(tempo);
		return result;
	}
	
	/**
	 * Pour creer le vecteur des phonemes de la phrase ph jusqu'a l'indice fin avec seulement les durees
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ph
	 *            la chaine contenant les phonemes
	 * @param fin
	 *            : l'indice de fin de la chaine a considerer
	 * @param synt
	 *            : type de la phrase
	 * @return : le vecteur contenant les phonemes de ph[0:fin]
	 */
	private static List<Phoneme> genererLesPhonemes(final String ph, final int fin, final Syntagme synt) {
		final List<Phoneme> result = new ArrayList<Phoneme>();
		final StringTokenizer st = new StringTokenizer(ph.substring(0, fin));
		while (st.hasMoreTokens()) {
			final String s = st.nextToken();
			result.add(new Phoneme(s));
		}
		result.get(0).allonge(synt);
		result.get(result.size() - 1).allonge(synt);
		result.add(new Phoneme(PAUSE, synt.dureePause()));
		return result;
	}
	
	/**
	 * Pour supprimer '%' ou '%%' du debut d'une phrase
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ph
	 *            la phrase en question
	 * @param coupure
	 *            l'endroit de <code>ph</code> que l'on souhaite analyser
	 */
	private static String elaguer(final String ph, final int coupure) {
		if (ph.charAt(coupure + 1) == '%') {
			return ph.substring(coupure + 2);
		} else {
			return ph.substring(coupure + 1);
		}
	}
	
	/**
	 * Pour appliquer un schema de prosodie en fonction du type de Syntagme
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         <p>
	 *         <b>Postcondition</b> : Le schema de prosodie ad'hoc est applique a <code>pho</code> par <i>side-effect</i>
	 *         </p>
	 * @param pho
	 *            : le vecteur des phonemes avec les duree
	 * @param synt
	 *            : le Syntagme associe
	 */
	public static void appliquerProsodie(final List<Phoneme> pho, final Syntagme synt) {
		final int length = pho.size();
		int i = 0;
		final Courbe c = new Courbe(synt, length);
		while (i < length) {
			final Phoneme p = (Phoneme) pho.get(i);
			if (!p.estPause()) {
				p.setProsodie(c.nextValue());
			}
			i++;
		}
	}
	
	/**
	 * Pour obtenir la duree de la prononciation de la liste complete des phonemes
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return la duree ad'hoc
	 */
	public int dureePhonemes() {
		int n = 0;
		final int max = nombrePhonemes();
		for (int i = 0; i < max; i++) {
			n += this.phonemes.get(i).getLongueur();
		}
		return n;
	}
	
	/**
	 * Pour obtenir le nombre de phonemes present dans cette liste
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return le nombre de phonemes en questions
	 */
	public int nombrePhonemes() {
		return this.phonemes.size();
	}
	
	/**
	 * Pour ecrire les phonemes dans un fichier
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param name
	 *            le nom du fichier danslequel on souhaite ecrire ces phonemes
	 */
	public void ecrirePhonemes(final String name) {
		try {
			final FileWriter fw = new FileWriter(name);
			fw.write(toString());
			fw.close();
		} catch (final Exception e) {
			System.out.println("SI_VOX WARNING [ListePhonemes.ecrirePhonemes] : Erreur lors de l'ecriture du fichier .pho");
		}
	}
	
	/**
	 * Pour afficher la liste de phoneme au format MBROLA
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		final int max = nombrePhonemes();
		for (int i = 0; i < max; i++) {
			buffer.append(this.phonemes.get(i).toString());
			buffer.append("\n");
		}
		return buffer.toString();
	}
	
	public List<Phoneme> getPhonemes() {
		return this.phonemes;
	}
	
	/**
	 * Une methode executable de test
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public static final void main(final String[] s) {
		final ListePhonemes p = new ListePhonemes("b o~ Z u R % i l", Phrase.VIRGULE);
		System.out.println("main :\n" + p);
		p.ecrirePhonemes("bidon");
	}
}
