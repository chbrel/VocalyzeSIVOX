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

/**
 * Une classe pour generer des suites de nombre aleatoires croissants
 */

public class SuiteCroissante {
	
	private static final int	PAS	= Integer.parseInt(ConfigFile.rechercher("PAS_SUITE"));
	private final int	     min;
	private final int	     max;
	private int	             etape;
	private int	             courant;
	
	/**
	 * Pour construire une suite croissante
	 * 
	 * @param m
	 *            la valeur minimale
	 * @param ma
	 *            la valeur maximale
	 * @param l
	 *            la longueur de la suite
	 */
	public SuiteCroissante(final int m, final int ma, final int l) {
		this.min = m;
		this.max = ma;
		this.courant = 0;
		this.etape = 1;
	}
	
	/**
	 * Constructeur par defaut : Utilise les valeurs du fichier de configuraiton
	 * 
	 * @param l
	 *            la longueur de la suite
	 */
	public SuiteCroissante(final int l) {
		this(Integer.parseInt(ConfigFile.rechercher("MIN_SUITE")), Integer.parseInt(ConfigFile.rechercher("MAX_SUITE")), l);
	}
	
	/**
	 * Pour obtenir la prochaine valeur de la suite
	 * 
	 * @return la valeur entiere qui va bien !
	 */
	public int next() {
		this.courant = Math.min(this.max, this.courant + Random.unsignedDelta(this.min, PAS * this.etape));
		this.etape++;
		return this.courant;
	}
}
