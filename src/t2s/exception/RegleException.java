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
 * Classe RegleException heritant de AnalyseException
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */

public class RegleException extends AnalyseException {
	
	private static final long	serialVersionUID	= 1L;
	
	/**
	 * Constructeur par defaut de RegleException
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param ligne
	 *            La ligne ou l'exception a ete levee
	 */
	public RegleException(final int ligne) {
		super(ligne);
	}
	
	/**
	 * Constructeur par parametre de RegleException
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param message
	 *            Le message contenu dans l'exception
	 * @param messageSonore
	 *            Le message sonore contenu dans l'exception
	 * @param ligne
	 *            La ligne ou l'exceptiona ete levee
	 */
	public RegleException(final String message, final String messageSonore, final int ligne) {
		super(message, messageSonore, ligne);
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
		System.out.println("Erreur SIVOX Regle : " + this.messageErreur + " a la ligne " + this.ligne);
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
		s.println("Erreur SIVOX Regle : " + this.messageErreur + " a la ligne " + this.ligne);
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
		w.println("Erreur SIVOX Regle : " + this.messageErreur + " a la ligne " + this.ligne);
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
		return (new RegleException(this.messageErreur, this.messageSonoreErreur, this.ligne));
	}
}
