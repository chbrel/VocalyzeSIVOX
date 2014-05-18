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
 * Une classe permettant la gestion des indices dans les chaines de caracteres
 */
public class Indice {
	
	private int	indice;
	
	/**
	 * Construit une instance d'Indice suivant le parametre fourni
	 * 
	 * @param i
	 *            le numero de l'indice voulu
	 */
	public Indice(final int i) {
		this.indice = i;
	}
	
	/**
	 * Construit une instance d'indice initialise a 0
	 */
	public Indice() {
		this(0);
	}
	
	/**
	 * Incremente l'indice d'un facteur donne
	 * 
	 * @param i
	 *            le facteur en question
	 */
	public void inc(final int i) {
		this.indice += i;
	}
	
	/**
	 * Incremente l'indice d'un facteur 1
	 */
	public void inc() {
		this.indice++;
	}
	
	/**
	 * Pour avoir la valeur courante de l'indice
	 * 
	 * @return la valeur courante stocke dans l'instance
	 */
	public int val() {
		return this.indice;
	}
	
	/**
	 * Pour mettre e jour la valeur stockee dans l'Indice
	 * 
	 * @param i
	 *            la nouvelle valeur
	 */
	public void val(final int i) {
		this.indice = i;
	}
	
	/**
	 * Teste l'egalite entre l'indice courant et le parametre
	 * 
	 * @param i
	 *            la valeur a tester
	 * @return true si egalite, false sinon
	 */
	public boolean egal(final int i) {
		return this.indice == i;
	}
	
	/**
	 * Teste la superiorite stricte pour l'indice courant et le parametre
	 * 
	 * @param i
	 *            le parametree a tester
	 * @return true si <code>courant > i</code>, false sinon
	 */
	public boolean plusGrand(final int i) {
		return this.indice > i;
	}
	
	/**
	 * Teste la superiorite au sens large pour l'indice courant et le parametre
	 * 
	 * @param i
	 *            le parametree a tester
	 * @return true si <code>courant >= i</code>, false sinon
	 */
	public boolean grandOuEgal(final int i) {
		return this.indice >= i;
	}
	
	/**
	 * Pour afficher une instance d'indice
	 * 
	 * @return une chaine de caracteres representant l'indice
	 */
	@Override
	public String toString() {
		return " " + this.indice + " ";
	}
}
