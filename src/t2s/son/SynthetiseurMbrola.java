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
 * Tutor (2004) : Helene Collavizza [ helen@essi.fr ]
 * Jean-Paul Stromboni [ strombon@essi.fr ]
 * Contributor :
 * (2004) : Louis Parisot [ parisot@essi.fr ]
 * (2005) : Sebastien Mosser [ mosser@essi.fr ]
 * Institute :
 * Polytechnich school, University of Nice - Sophia Antipolis (FRANCE)
 * This program is free software. It uses mbrola speech synthesizers system.
 * You can redistribute it and/or modify it under the terms of the MBROLA
 * Licenses { http://tcts.fpms.ac.be/synthesis/mbrola.html }.
 */

package t2s.son;

import t2s.util.ConfigFile;

/** pour de synthetiser un fichier sonore a partir d'un fichier de phonemes */

public class SynthetiseurMbrola {
	
	/**
	 * Les base vocales utilisee par MBROLA
	 */
	protected final static String	NBVOIX	= ConfigFile.rechercher("NBVOIX");
	protected final static String	VOIX1	= ConfigFile.rechercher("VOIX_1");
	protected final static String	VOIX2	= ConfigFile.rechercher("VOIX_2");
	protected final static String	VOIX3	= ConfigFile.rechercher("VOIX_3");
	protected final static String	VOIX4	= ConfigFile.rechercher("VOIX_4");
	protected final static String	VOIX5	= ConfigFile.rechercher("VOIX_5");
	protected final static String	VOIX6	= ConfigFile.rechercher("VOIX_6");
	protected final static String	VOIX7	= ConfigFile.rechercher("VOIX_7");
	
	// protected final static Double RAPIDITE = new Double(ConfigFile.rechercher("RAPIDITE"));
	
	/*
	 * On ne declare plus cette constante une fois pour toute.
	 * La rapidite est susceptible d'etre modifiee a tout moment dans le jeu.
	 */
	protected static Double	      RAPIDITE;
	protected final static Double	VOLUME	= new Double(ConfigFile.rechercher("VOLUME"));
	
	private String	              exe;	                                                   // l'executable de mbrola
	static String	              voix;	                                               // la librairie de voix
	static String	              FR;	                                                   // frequency ratio (option -f)
	private final String	      pathFichier;	                                           // le repertoire des fichiers
	private final String	      fichier;	                                               // le nom de fichier de phoneme (sans extension)
	                                                                                       
	private JukeBox	              jb;
	
	/**
	 * Constructeur complet
	 * 
	 * @param mb
	 *            le repertoire ou se trouve MBROLA (racine des 2 versions)
	 * @param v
	 *            la voix a utiliser pour synthetiser le fichier sonore
	 * @param pf
	 *            le chemin d'acces au repertoire contenant les fichiers a traiter
	 * @param f
	 *            Le nom du fichier de phoneme a traiter, sans l'extension '.pho'
	 */
	public SynthetiseurMbrola(JukeBox jb, final String v, final String pf, final String f) {
		RAPIDITE = new Double(ConfigFile.rechercher("RAPIDITE")); // On recharge la rapidite en permanence. -------> Code modifie
		if (System.getProperties().getProperty("os.name").contains("Linux")) {
			this.exe = ConfigFile.rechercher("EXE_LINUX");
		} else if (System.getProperties().getProperty("os.name").contains("Windows")) {
			this.exe = ConfigFile.rechercher("EXE_WINDOWS");
		} else {
			this.exe = ConfigFile.rechercher("EXE_MAC");
		}
		voix = v;
		this.pathFichier = pf;
		this.fichier = f;
		this.jb = jb;
	}
	
	/**
	 * Constructeur completement allege
	 * 
	 * @param pf
	 *            le chemin d'acces au repertoire contenant les fichiers a traiter
	 * @param f
	 *            Le nom du fichier de phoneme a traiter, sans l'extension '.pho'
	 */
	public SynthetiseurMbrola(JukeBox jb, final String pf, final String f) {
		this(jb, VOIX1, pf, f);
	}
	
	/** Pour creer le fichier wav et le lire dans un JuxeBox */
	public void play(boolean wait) {
		prepare("play");
		this.jb.playSound(this.pathFichier + this.fichier + ".wav", false, wait, true);
	}
	
	public void loop() {
		prepare("play");
		this.jb.playSound(this.pathFichier + this.fichier + ".wav", true, false, true);
	}
	
	/**
	 * Pour seulement creer le fichier wav, mais sans le lire.
	 */
	public void muet() {
		prepare("muet");
	}
	
	/**
	 * prepare le processus pour la transformation des phonemes en wave
	 */
	private void prepare(final String mode) {
		final Runtime r = Runtime.getRuntime();
		// System.out.println(Integer.parseInt(NBVOIX)+1);
		if (voix.equals(VOIX1)) {
			FR = ConfigFile.rechercher("FR1");
		}
		if (voix.equals(VOIX2)) {
			FR = ConfigFile.rechercher("FR2");
		}
		if (voix.equals(VOIX3)) {
			FR = ConfigFile.rechercher("FR3");
		}
		if (voix.equals(VOIX4)) {
			FR = ConfigFile.rechercher("FR4");
		}
		if (voix.equals(VOIX5)) {
			FR = ConfigFile.rechercher("FR5");
		}
		if (voix.equals(VOIX6)) {
			FR = ConfigFile.rechercher("FR6");
		}
		if (voix.equals(VOIX7)) {
			FR = ConfigFile.rechercher("FR7");
		}
		// chaine de caractere contenant la commande mbrola
		final String cmd = this.exe + " -t " + RAPIDITE + " " + " -f " + FR + " " + " -v " + VOLUME + " " + voix + " " + this.pathFichier + this.fichier + ".pho " + this.pathFichier + this.fichier
		        + ".wav";
		// System.out.println(cmd);
		try {
			// long start = System.currentTimeMillis();
			final Process proc = r.exec(cmd);
			boolean finished = false;
			while (!finished) {
				try {
					Thread.sleep(10);
					proc.exitValue();
					finished = true;
				} catch (final IllegalThreadStateException e) {
					// pas encore termine
				}
			}
		} catch (final Exception e) {
			System.out.println("SynthetiseurMbrola: " + mode + " erreur!");
		}
	}
	
}
