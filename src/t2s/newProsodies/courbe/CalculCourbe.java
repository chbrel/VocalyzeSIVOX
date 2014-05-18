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
package t2s.newProsodies.courbe;

import java.util.Vector;

import t2s.ihm.courbe.Point2D;
import t2s.ihm.courbe.Tableau;
import t2s.prosodie.Phoneme;
import t2s.util.ConfigFile;

/**
 * Cette classe permet de calculer tous les parametres necessaires pour construire la
 * courbe qui est affichee en bas de l'interface graphique.
 * Elle utilise l'interpolation de Lagrange pour calculer les points.
 */
public class CalculCourbe {
	
	public static Vector<Double>	dureePhonemes	 = new Vector<Double>();	                                // liste des duree en ms de tous les phonemes
	public static int	          dureeTotale;	                                                                // duree en ms de tout le texte
	private final int	          NOMBRE_POINTS	     = 200;	                                                // nombre de points pour dessiner la courbes
	public static double	      vitesse	         = new Double(ConfigFile.rechercher("RAPIDITE"));	        // valeur par defaut dans le fichier .conf
	public static double	      frequence	         = new Double(ConfigFile.rechercher("ANALYSER_FREQUENCE"));
	public static double	      amplitude	         = new Double(ConfigFile.rechercher("ANALYSER_AMPLITUDE"));
	public static Vector<String>	listePhoSymboles	= new Vector<String>();	                            // liste des String representant les phonemes
	                                                                                                            
	private Vector<Point2D>	      listePointsCles;
	private InterpolationLagrange	il;	                                                                    // la fonction permettant de recuperer les points de la courbe interpolee
	                                                                                                            
	/**
	 * Constructeur par defaut de la classe
	 */
	public CalculCourbe() {
		this.listePointsCles = new Vector<Point2D>();
		this.il = new InterpolationLagrange();
	}
	
	/**
	 * Constructeur de la classe. Construit directement la courbe interpol�e � l'aide
	 * de la liste des phonemes passes en parametre. Ce constructeur va choisir un certain
	 * nombre de phonemes comme points cles pour construire la courbe par interpolation
	 * 
	 * @param l
	 *            : liste des phonemes
	 */
	public CalculCourbe(final Vector<Phoneme> l) {
		this.listePointsCles = new Vector<Point2D>();
		this.il = new InterpolationLagrange();
		int duree = 0, dureeCourante = 0, compteur = 0;
		listePhoSymboles.clear();
		dureeTotale = 0;
		
		for (int i = 0; i < l.size(); ++i) {
			dureeTotale += l.get(i).getLongueur();
		}
		
		for (int i = 0; i < l.size(); ++i) {
			dureePhonemes.add(i, new Double(l.get(i).getLongueur()));
			listePhoSymboles.add(l.get(i).getPho());
			duree = l.get(i).getLongueur();
			final int epsilon = 20;
			final int coefY = (Tableau.dim.height - 60 - 70) / ((2 * (int) amplitude) + 20);
			final float coefX = 720.0f / dureeTotale;
			final int posPoint = (int) (frequence - amplitude - 10);
			for (int j = 0; j < l.get(i).getProsodie().size(); ++j) {
				if ((compteur % (dureeTotale / 350)) == 0) {
					this.listePointsCles.add(new Point2D(50 + ((dureeCourante + ((duree * l.get(i).getProsodie().get(j).getPourcentage()) / 100)) * coefX), (Tableau.dim.height - 60 - epsilon - ((l
					        .get(i).getProsodie().get(j).getFrequence() - posPoint) * coefY))));
				}
				/*
				 * listePointsCles.add(new Point2D((dureeCourante + duree * l.get(i).getProsodie().get(j).getPourcentage() / 100)
				 * , (l.get(i).getProsodie().get(j).getFrequence() - posPoint)));
				 */
				compteur++;
			}
			dureeCourante += duree;
		}
	}
	
	/**
	 * Genere les phonemes sous forme de String a partir de la courbe interpolee.
	 * 
	 * @param listePointsCles
	 *            : les points cles de la courbe interpolee
	 * @param nbCP
	 *            : nombre de couples de prosodie par phoneme
	 * @return String: la liste des phonemes generes a partir de la courbe interpolee
	 */
	public String genererPhonemes(final Vector<Point2D> listePointsCles, final int nbCP) {
		String resultat = "";
		double debut = 0;
		double fin = 0;
		this.il.addPoints(listePointsCles);
		for (int i = 0; i < listePhoSymboles.size(); i++) {
			debut = fin;
			fin += dureePhonemes.get(i);
			resultat += listePhoSymboles.get(i) + " " + dureePhonemes.get(i).intValue() + "  ";
			final Vector<Point2D> liste = recupererPoints(nbCP, debut, fin);
			int pourcentage = 0;
			if (!listePhoSymboles.get(i).equals("_")) {
				for (int j = 0; j < liste.size(); j++) {
					pourcentage = (int) (((liste.get(j).getX() - debut) / (fin - debut)) * 100);
					resultat += pourcentage + " " + (int) liste.get(j).getY() + " ";
				}
			}
			resultat += "\n";
		}
		return resultat;
		
	}
	
	/**
	 * Renvoie "nombre" points equitablement repartis dans l'intervalle de temps compris entre
	 * tempsDebut et tempsFin
	 * 
	 * @param nombre
	 *            : le nombre de points a renvoyer
	 * @param tempsDebut
	 *            : le debut de l'intervalle
	 * @param tempsFin
	 *            : la fin de l'intervalle
	 * @return: liste de points repartis entre [tempsDebut, tempsFin]
	 */
	private Vector<Point2D> recupererPoints(final int nombre, final double tempsDebut, final double tempsFin) {
		final Vector<Point2D> liste = new Vector<Point2D>();
		final double intervalle = (tempsFin - tempsDebut) / nombre;
		for (int i = 0; i < nombre; i++) {
			int nb = (int) this.il.valeurFonctionInterpolee(tempsDebut + (i * intervalle));
			if (nb < (frequence - amplitude)) {
				nb = (int) (frequence - amplitude - 10);
			}
			if (nb > (frequence + amplitude)) {
				nb = (int) (frequence + amplitude + 40);
			}
			liste.add(new Point2D(tempsDebut + (i * intervalle), nb));
		}
		return liste;
	}
	
	public Vector<Point2D> getListePointsCles() {
		return this.listePointsCles;
	}
	
	public void setListePointsCles(final Vector<Point2D> l) {
		this.listePointsCles = l;
	}
	
	public Vector<Point2D> genererListePoints(final Vector<Point2D> l) {
		this.listePointsCles = l;
		this.il = new InterpolationLagrange(this.listePointsCles);
		return interpolerCourbe();
	}
	
	/**
	 * Calcule les points pour dessiner la courbe
	 * 
	 * @return: un tableau contenant freqMin et freqMax
	 */
	public Vector<Point2D> interpolerCourbe() {
		final Vector<Point2D> v = new Vector<Point2D>();
		for (int i = 0; i < this.NOMBRE_POINTS; ++i) {
			final Point2D point = new Point2D(((double) dureeTotale * i) / this.NOMBRE_POINTS, this.il.valeurFonctionInterpolee(((double) dureeTotale * i) / this.NOMBRE_POINTS));
			if ((point.getX() >= this.listePointsCles.get(0).getX()) && (point.getX() <= this.listePointsCles.get(this.listePointsCles.size() - 1).getX())) {
				v.add(point);
			}
		}
		return v;
	}
	
}
