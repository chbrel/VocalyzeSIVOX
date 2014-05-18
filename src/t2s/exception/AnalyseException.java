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
package t2s.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Classe Analyse Exception heritant de SIVOXException
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */

public class AnalyseException extends SIVOXException {
	
	private static final long	serialVersionUID	= 1L;
	protected int	          ligne;
	
	/**
	 * Constructeur par defaut de AnalyseException
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param l
	 *            La ligne ou l'on a levee l'exception
	 */
	public AnalyseException(final int l) {
		super();
		this.ligne = l;
	}
	
	/**
	 * Constructeur par parametre de AnalyseException
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param message
	 *            Le message contenu dans l'exception AnalyseException
	 * @param messageSonore
	 *            Le message sonore contenu dans l'exception
	 * @param l
	 *            La ligne ou l'on a leve l'exception
	 */
	public AnalyseException(final String message, final String messageSonore, final int l) {
		super(message, messageSonore);
		this.ligne = l;
	}
	
	/**
	 * Methode toString
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         (non-Javadoc)
	 * @see t2s.exception.SIVOXException#toString()
	 * @return Le message contenu dans l'exception
	 */
	@Override
	public String toString() {
		return (super.toString() + " a la ligne " + this.ligne);
	}
	
	/**
	 * Methode PrintStackStrace
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace()
	 */
	@Override
	public void printStackTrace() {
		System.out.println("Erreur SIVOX Analyse : " + this.messageErreur + " a la ligne " + this.ligne);
	}
	
	/**
	 * Methode printStackTrace(PrintStream)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	 */
	@Override
	public void printStackTrace(final PrintStream s) {
		s.println("Erreur SIVOX Analyse : " + this.messageErreur + " a la ligne " + this.ligne);
	}
	
	/**
	 * Methode printStackTrace(PrintWriter)
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         (non-Javadoc)
	 * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
	 */
	@Override
	public void printStackTrace(final PrintWriter w) {
		w.print("Erreur SIVOX Analyse : " + this.messageErreur + " a la ligne " + this.ligne);
	}
	
	/**
	 * Methode qui change la ligne de l'exception
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param l
	 *            Le ligne ou l'on a levee l'exception
	 */
	public void setLigne(final int l) {
		this.ligne = l;
	}
	
	/**
	 * Methode qui retourne le numero de la ligne ou l'on a levee l'exception
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return Le numero de ligne
	 */
	public int getLigne() {
		return (this.ligne);
	}
	
	/**
	 * Methode clone
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 *         (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * @return Une copie de l'exception courante
	 */
	@Override
	public Object clone() {
		return (new AnalyseException(this.messageErreur, this.messageSonoreErreur, this.ligne));
	}
}
