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
package t2s.newProsodies;

import java.util.Vector;

import t2s.prosodie.ListePhonemes;
import t2s.prosodie.Phoneme;
import t2s.traitement.Phrase;

public class Prosodie1 {
	
	private final Vector<Phrase>	listePhrase;
	
	public Prosodie1(final Vector<Phrase> l) {
		this.listePhrase = l;
	}
	
	public Vector<Phoneme> prosodier() {
		final Vector<Phoneme> v = new Vector<Phoneme>();
		final ListePhonemes l = new ListePhonemes();
		
		for (final Phrase p : this.listePhrase) {
			l.ajouterPhonemes(p.getPhrase(), p.getProsodie());
		}
		
		for (int i = 0; i < l.getPhonemes().size(); ++i) {
			v.add(l.getPhonemes().get(i));
		}
		
		return v;
	}
}
