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

package t2s;

import java.io.FileWriter;
import java.util.Vector;

import t2s.newProsodies.Analyser;
import t2s.prosodie.Phoneme;
import t2s.son.JukeBox;
import t2s.son.LecteurTexte;
import t2s.son.SynthetiseurMbrola;
import t2s.util.ConfigFile;

/**
 * Une A.P.I. pour utiliser la synthèse vocale dans les projets DeViNT
 * 
 * @author Ecole Polytechnique Universitaire Nice Sophia Antipolis
 */
public class SIVOXDevint {
	private JukeBox	           jk;	     // pour jouer les wav
	private LecteurTexte	   lt;	     // pour choisir une voix
	private SynthetiseurMbrola	s;
	private Analyser	       an;
	private boolean	           on;	     // true/false pour valider/invalider la synthèse SIVOX
	private int	               prosodie; // code la prosodie utilisée, de 1 à 3 (3 par défaut)
	                                     
	/**
	 * Constructeur par défaut : voix de Thierry
	 * prosodie = 3, la plus performante
	 */
	public SIVOXDevint() {
		this.jk = new JukeBox();
		this.lt = new LecteurTexte(jk);
		this.on = true;
		lt.setVoix(1);
		this.prosodie = 3;
	}
	
	/**
	 * Constructeur pour fixer la voix
	 * 
	 * @param voix
	 *            , de 1 à 7 pour fr1, fr2, ... fr7
	 */
	public SIVOXDevint(final int voix) {
		this();
		final int nbvoix = Integer.parseInt(ConfigFile.rechercher("NBVOIX")); // nombre de voix disponibles
		int vox;
		vox = (voix > nbvoix) ? nbvoix : voix;
		vox = (voix < 1) ? 1 : voix;
		lt.setVoix(vox);
	}
	
	/**
	 * Pour lire un texte long à voix haute en boucle
	 * 
	 * @param text
	 *            , chaîne à lire à voix haute
	 */
	public void loopText(final String text) {
		play(text, true, false);
	}
	
	/**
	 * Pour lire le son d'un fichier .wav en boucle
	 * 
	 * @param fichier
	 *            nom du fichier (wave) à lire
	 */
	public void loopWav(final String fichier) {
		this.jk.playSound(fichier, true, false);
	}
	
	/**
	 * Pour lire un texte court (sans ponctuation) à voix haute
	 * 
	 * @param text
	 *            , chaîne de caractères à lire à voix haute
	 */
	
	public void playShortText(final String text) {
		this.playShortText(text, false);
	}
	/**
	 * Pour lire un texte court (sans ponctuation) à voix haute
	 * 
	 * @param text
	 *            , chaîne de caractères à lire à voix haute
	 */
	
	public void playShortText(final String text, boolean wait) {
		if (!this.on) {
			return;
		}
		an = new Analyser(text, this.prosodie);
		@SuppressWarnings("unused")
		final Vector<Phoneme> listePhonemes = an.analyserGroupes();
		s = new SynthetiseurMbrola(jk, lt.getVoix(), ConfigFile.rechercher("REPERTOIRE_PHO_WAV"), ConfigFile.rechercher("FICHIER_PHO_WAV")+ text.hashCode());
		s.play(wait);
	}
	
	/**
	 * Pour lire un texte long à voix haute
	 * 
	 * @param text
	 *            , chaîne à lire à voix haute
	 */
	public void playText(final String text) {
		this.playText(text, false);
	}
	
	/**
	 * Pour lire un texte long à voix haute
	 * 
	 * @param text
	 *            , chaîne à lire à voix haute
	 */
	public void playText(final String text, boolean wait) {
		play(text, false, wait);
	}
	
	/**
	 * Pour lire le son d'un fichier .wav
	 * 
	 * @param fichier
	 *            wave à lire
	 */
	public void playWav(final String fichier) {
		this.playWav(fichier, false);
	}
	
	/**
	 * Pour lire le son d'un fichier .wav
	 * 
	 * @param fichier
	 *            wave à lire
	 */
	public void playWav(final String fichier, boolean wait) {
		this.jk.playSound(fichier, false, wait);
	}
	
	/**
	 * Pour fixer la prosodie utilisée
	 * 
	 * @param p
	 *            , entier de 1 à 3
	 */
	public void setProsodie(final int p) {
		int pro;
		pro = (p < 1) ? 1 : p;
		pro = (p > 3) ? 3 : p;
		this.prosodie = pro;
	}
	
	public int getProsodie() {
		return this.prosodie;
	}
	
	/**
	 * Pour fixer la voix utilisée si la synthèse parle
	 * 
	 * @param voix
	 *            , de 1 à 7
	 */
	public void setVoix(final int voix) {
		int vox;
		final int nbvoix = Integer.parseInt(ConfigFile.rechercher("NBVOIX")); // nombre de voix disponibles dans ressources
		System.out.println(nbvoix);
		vox = (voix > nbvoix) ? nbvoix : voix;
		vox = (voix < 1) ? 1 : voix;
		lt.setVoix(vox);
	}
	
	/**
	 * Pour stopper la synthèse vocale et donc arréter le son en cours de lecture
	 * on stoppe le jukebox jk, qui lit les sons wave, le lecteur texte lt, et la synthèse s
	 */
	public void stop() {
		if (this.jk != null) {
			this.jk.stop(false);
		}
	}
	
	public void forceStop() {
		if (this.jk != null) {
			this.jk.stop(true);
		}
	}
	
	// Pour basculer entre voix on / voix off
	public void toggle() {
		this.on = !this.on;
	}
	
	/**
	 * renvoie l'état du toggle voix on/voix off
	 * 
	 * @return
	 */
	public boolean getToggle() {
		return this.on;
	}
	
	// pour créer des fichiers wave en silence
	public void muet(final String text, final String out) {
		// if ( !on ) return;
		an = new Analyser(text, this.prosodie);
		final Vector<Phoneme> listePhonemes = an.analyserGroupes();
		final String chainePho = an.afficher(listePhonemes);
		try {
			final FileWriter fw = new FileWriter(out + ".pho");
			fw.write(chainePho);
			fw.close();
		} catch (final Exception e) {
			System.out.println("erreur création fichier phonème.");
		}
		s = new SynthetiseurMbrola(jk, lt.getVoix(), out, "");
		s.muet();
	}
	
	// appelé par loopText et playText avec valeur flagloop diff�rente
	public void play(final String text, final boolean flagloop) {
		this.play(text, flagloop, false);
	}
	
	// appelé par loopText et playText avec valeur flagloop diff�rente
	public void play(final String text, final boolean flagloop, boolean wait) {
		
		if (!this.on) {
			return;
		}
		an = new Analyser(text, this.prosodie);
		@SuppressWarnings("unused")
		final Vector<Phoneme> listePhonemes = an.analyserGroupes();
		s = new SynthetiseurMbrola(jk, lt.getVoix(), ConfigFile.rechercher("REPERTOIRE_PHO_WAV"), ConfigFile.rechercher("FICHIER_PHO_WAV") + text.hashCode());
		// System.out.println("RAPIDITE: "+ConfigFile.rechercher("RAPIDITE"));
		if (flagloop) {
			s.loop();
		} else {
			s.play(wait);
		}
	}
	
}
