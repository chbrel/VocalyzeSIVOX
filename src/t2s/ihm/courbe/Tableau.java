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
package t2s.ihm.courbe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Vector;

import t2s.newProsodies.courbe.CalculCourbe;

/**
 * Classe Tableau
 * 
 * @author Ecole Polytechnique de Sophia Antipolis
 */
public class Tableau {
	
	private final int	    mArrowLength, mTickSize, padding;
	private int	            current	= 0;
	public static Dimension	dim;
	private final Vector<Point2D>	listePointsCles, listePoints;
	private final boolean	      visible	= true;
	
	/**
	 * Constructeur par defaut de tableau
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param d
	 *            La dimension du tableau
	 */
	public Tableau(final Dimension d) {
		this.listePointsCles = new Vector<Point2D>();
		this.listePoints = new Vector<Point2D>();
		dim = d;
		this.mArrowLength = 4;
		this.mTickSize = 4;
		this.padding = 50;
	}
	
	/**
	 * Methode qui retourne la dimension du tableau
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La dimension du tableau
	 */
	public Dimension getDimension() {
		return dim;
	}
	
	/**
	 * Methode qui retourne la liste des points cles
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La liste des points cles
	 */
	public Vector<Point2D> getListePointsCles() {
		return this.listePointsCles;
	}
	
	/**
	 * Methode qui retourne la liste des points
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return La liste des points
	 */
	public Vector<Point2D> getListePoints() {
		return this.listePoints;
	}
	
	/**
	 * Methode qui retourne l element courant
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return L'element courant
	 */
	public int getCurrent() {
		return this.current;
	}
	
	/**
	 * Methode qui modifie la liste des points cles
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param lpc
	 */
	public void setListePointsCles(final Vector<Point2D> lpc) {
		this.listePointsCles.clear();
		this.listePointsCles.addAll(lpc);
	}
	
	/**
	 * Methode qui modifie la liste des points
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param lp
	 *            La nouvelle liste des points
	 */
	public void setListePoints(final Vector<Point2D> lp) {
		this.listePoints.clear();
		this.listePoints.addAll(lp);
	}
	
	/**
	 * Methode qui modifie la dimension
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param d
	 *            la nouvelle dimension
	 */
	public void setDimension(final Dimension d) {
		dim = d;
	}
	
	/**
	 * Methode qui indique la visibilite
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return true si visible et false sinon
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * Methode qui vide la liste de points cles et la liste de points
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public void clear() {
		this.listePointsCles.clear();
		this.listePoints.clear();
	}
	
	/**
	 * M�thode qui v�rifie si un point de coordonn�es (x,y) appartient � la liste de points cles
	 * 
	 * @param x
	 *            La coordonnee x du point a tester
	 * @param y
	 *            La coordonne y du point a tester
	 * @return True si le point appartient a la liste des points cle et false sinon
	 */
	public boolean contains(final double x, final double y) {
		for (int i = 0; i < this.listePointsCles.size(); ++i) {
			if (this.listePointsCles.get(i).contains(x, y)) {
				this.listePointsCles.get(this.current).setColor(Color.BLUE);
				this.current = i;
				this.listePointsCles.get(this.current).setColor(Color.PINK);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Methode de convertion graphique
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param point
	 *            Le point 2D
	 * @return le pointd2D double
	 */
	public java.awt.geom.Point2D.Double convertGraphique(final Point2D point) {
		final int epsilon = 20;
		final int coefY = (Tableau.dim.height - 60 - 70) / ((2 * (int) CalculCourbe.amplitude) + 20);
		final float coefX = 720.0f / CalculCourbe.dureeTotale;
		final java.awt.geom.Point2D.Double p = new java.awt.geom.Point2D.Double(50 + (point.getX() * coefX), dim.height - 60 - epsilon - (point.getY() * coefY));
		return p;
	}
	
	/**
	 * Methode qui converti
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param vpoint
	 *            Le vecteur de Point2D d'entree
	 * @return Un vecteur de Point2D
	 */
	public Vector<Point2D> convertCoordPho(final Vector<Point2D> vpoint) {
		final int epsilon = 20;
		final int coefY = (Tableau.dim.height - 60 - 70) / ((2 * (int) CalculCourbe.amplitude) + 20);
		final float coefX = 720.0f / CalculCourbe.dureeTotale;
		final Vector<Point2D> v = new Vector<Point2D>();
		for (int i = 0; i < vpoint.size(); i++) {
			final Point2D p = new Point2D((vpoint.get(i).getX() - 50) / coefX, ((((vpoint.get(i).getY() - dim.height) + 60 + epsilon) / (-coefY)) + (int) CalculCourbe.frequence)
			        - (CalculCourbe.amplitude + 10));
			v.add(p);
		}
		return v;
	}
	
	/**
	 * M�thode qui dessine la courbe, et les points cles
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 */
	public void draw(final Graphics2D g) {
		if (this.listePointsCles == null) {
			return;
		}
		if (!this.visible || (this.listePointsCles.size() <= (2 - 1))) {
			return;
		}
		drawAxes(g);
		Iterator<Point2D> it = null;
		final Line2D.Double line = new Line2D.Double();
		Point2D p1, p2;
		it = this.listePoints.iterator();
		p2 = it.next();
		while (it.hasNext()) {
			p1 = p2;
			p2 = it.next();
			if ((p1 != null) && (p2 != null)) {
				line.setLine(new java.awt.geom.Point2D.Double(p1.getX(), p1.getY()), new java.awt.geom.Point2D.Double(p2.getX(), p2.getY()));
				g.draw(line);
			}
		}
		for (it = this.listePointsCles.iterator(); it.hasNext();) {
			it.next().draw(g);
		}
	}
	
	/**
	 * M�thode qui dessine le repere
	 * 
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param g2
	 *            Le dessinateur Graphics3D
	 */
	void drawAxes(final Graphics2D g2) {
		g2.setPaint(Color.WHITE);
		g2.setStroke(new BasicStroke(2.0f));
		g2.setFont(new Font("Courier", Font.BOLD, 14)); // legende
		g2.drawLine(this.padding, dim.height - this.padding, dim.width - this.padding, dim.height - this.padding); // axe horizontal
		g2.drawLine(this.padding, this.padding, this.padding, dim.height - this.padding); // axe vertical
		g2.drawLine(this.padding, this.padding, this.padding - this.mArrowLength, this.padding + this.mArrowLength); // fl�che axe vertical
		g2.drawLine(this.padding, this.padding, this.padding + this.mArrowLength, this.padding + this.mArrowLength);
		g2.drawLine(dim.width - this.padding, dim.height - this.padding, dim.width - this.padding - this.mArrowLength, dim.height - this.padding - this.mArrowLength); // fl�che axe horizontal
		g2.drawLine(dim.width - this.padding, dim.height - this.padding, dim.width - this.padding - this.mArrowLength, (dim.height - this.padding) + this.mArrowLength);
		
		final int lgrAxeX = dim.width - 170;
		final int dureeTotale = CalculCourbe.dureeTotale;
		int lgrGraduation = 0, dureePhoConcat = 0;
		double dureePho = 0;
		for (int i = 0; i < CalculCourbe.listePhoSymboles.size(); i++) {
			dureePho = CalculCourbe.dureePhonemes.get(i);
			dureePhoConcat += dureePho;
			if (CalculCourbe.listePhoSymboles.size() > 25) {
				if ((i % 2) == 0) {
					g2.drawString("[" + CalculCourbe.listePhoSymboles.get(i) + "]", (50 + lgrGraduation) - 10, 40); // phoneme
				} else {
					g2.drawString("[" + CalculCourbe.listePhoSymboles.get(i) + "]", (50 + lgrGraduation) - 10, 55); // phoneme
				}
			} else {
				g2.drawString("[" + CalculCourbe.listePhoSymboles.get(i) + "]", (50 + lgrGraduation) - 10, 40); // phoneme
			}
			lgrGraduation = (dureePhoConcat * lgrAxeX) / dureeTotale;
			g2.drawLine(50 + lgrGraduation, dim.height - 50 - this.mTickSize, 50 + lgrGraduation, (dim.height - 50) + this.mTickSize); // graduations horizontales
			if (CalculCourbe.listePhoSymboles.size() > 25) {
				if ((i % 2) == 0) {
					g2.drawString(dureePhoConcat + "", (50 + lgrGraduation) - 10, dim.height - 25); // duree phoneme
				} else {
					g2.drawString(dureePhoConcat + "", (50 + lgrGraduation) - 10, dim.height - 10); // duree phoneme
				}
			} else {
				g2.drawString(dureePhoConcat + "", (50 + lgrGraduation) - 10, dim.height - 20); // duree phoneme
			}
		}
		
		g2.drawLine(this.padding - this.mTickSize, this.padding + 20, this.padding + this.mTickSize, this.padding + 20); // graduations verticales
		g2.drawLine(this.padding - this.mTickSize, dim.height - 60, this.padding + this.mTickSize, dim.height - 60);
		g2.drawLine(this.padding - this.mTickSize, ((dim.height - 60 - (this.padding + 20)) / 2) + this.padding + 20, this.padding + this.mTickSize, ((dim.height - 60 - (this.padding + 20)) / 2)
		        + this.padding + 20);
		g2.drawString("fr�quence (Hz)", 15, 20);
		g2.drawString("dur�e", dim.width - 60, dim.height - 25);
		g2.drawString("(ms)", dim.width - 55, dim.height - 10);
		g2.drawString("0", this.padding, dim.height - 20); // origine
		g2.drawString((int) CalculCourbe.frequence + "", 15, ((dim.height - 60 - (this.padding + 20)) / 2) + this.padding + 20 + 3); // frequence
		g2.drawString((int) CalculCourbe.frequence + (int) CalculCourbe.amplitude + 10 + "", 15, this.padding + 20 + 3); // frequence + amplitude
		g2.drawString((int) (CalculCourbe.frequence - (int) CalculCourbe.amplitude - 10) + "", 15, (dim.height - 60) + 3); // frequence - amplitude
	}
}
