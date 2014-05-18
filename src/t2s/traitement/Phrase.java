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

// import java.util.*;

/**
 * Pour representer une phrase (chaine de caracteres) et sa prosodie (ponctuation).
 */
public class Phrase {
	
	/** Tag de prosodie 'point' */
	public final static int	POINT	      = 0;
	/** Tag de prosodie 'virgule' */
	public final static int	VIRGULE	      = 1;
	/** Tag de prosodie 'interogation' */
	public final static int	INTERROGATION	= 2;
	/** Tag de prosodie par defaut */
	public final static int	DEFAUT	      = 3;
	/** Tag de prosodie 'exclamation' */
	public final static int	EXCLAMATION	  = 4;
	public final static int	DEUXPOINTS	  = 5;
	public final static int	SLASH	      = 6;
	public final static int	POINTVIRGULE	= 7;
	
	private int	            prosodie;
	private final String	phrase;
	
	/**
	 * Constructeur de phrase standart.
	 * 
	 * @param phrase
	 *            la chaine de caractere correspondante
	 * @param prosodie
	 *            le code associe a la prosodie de cette phrase.
	 */
	public Phrase(final String phrase, final int prosodie) {
		this.phrase = phrase;
		this.prosodie = prosodie;
	}
	
	/**
	 * Pour recuperer le texte de cette phrase.
	 * 
	 * @return la chaine de caracteres de la phrase.
	 */
	public String getPhrase() {
		return this.phrase;
	}
	
	/**
	 * Pour affecter un type de prosodie a la phrase
	 * 
	 * @param p
	 *            la nouvelle prosodie.
	 */
	public void setProsodie(final int p) {
		this.prosodie = p;
	}
	
	/**
	 * Pour recuperer la prosodie associee a la phrase
	 * 
	 * @return l'entier ad'hoc.
	 */
	public int getProsodie() {
		return this.prosodie;
	}
	
	/**
	 * Methode d'affichage standart d'une instance de phrase.
	 * 
	 * @return la phrase sous forme de String
	 */
	@Override
	public String toString() {
		return "phrase : " + this.phrase + "prosodie : " + this.prosodie;
	}
}
