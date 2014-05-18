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

package t2s.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class LecteurFichier {
	
	private BufferedReader	   br;
	
	/**
	 * Le codage utilise dans les fichiers, une constante !
	 */
	
	public static final String	CODAGE	= ConfigFile.rechercher("ENCODAGE_FICHIER"); // "ISO-8859-1";
	                                                                                 
	/**
	 * construit un lecteur de fichier (encode selon CODAGE)
	 * 
	 * @param name
	 *            le nom du fichier que l'on voudra lire
	 */
	public LecteurFichier(final String name) {
		try {
			final FileInputStream fos = new FileInputStream(name);
			this.br = new BufferedReader(new InputStreamReader(fos, CODAGE));
		} catch (final UnsupportedEncodingException ue) {
			System.err.println("SI_VOX WARNING [LecteurTexte] : Probleme de codage du fichier !");
		} catch (final FileNotFoundException fe) {
			System.err.println("SI_VOX WARNING [LecteurTexte] : Fichier '" + name + "' introuvable !");
		}
	}
	
	/**
	 * Pour lire effectivement la totalite du fichier
	 * 
	 * @return une chaine de caracteres contenant la totalite du fichier
	 */
	public String toutLire() {
		int lecture = 0;
		final StringBuffer buffer = new StringBuffer();
		try {
			while ((lecture = this.br.read()) != -1) {
				buffer.append((char) lecture);
			}
			this.br.close();
		} catch (final Exception e) {
			System.err.println("SI_VOX WARNING [LecteurTexte.toutLire] : erreur !");
			e.printStackTrace();
		}
		return buffer.toString();
	}
}
