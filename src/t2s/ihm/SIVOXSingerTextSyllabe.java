/*
* ﻿Copyright 2004-2007, Christian BREL, Hélène COLLAVIZZA, Sébastien MOSSER, Jean-Paul STROMBONI,
* 
* This file is part of project 'VocalyzeSIVOX'
* 
* 'VocalyzeSIVOX' is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* 'VocalyzeSIVOX'is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with 'VocalyzeSIVOX'. If not, see <http://www.gnu.org/licenses/>.
*/
package t2s.ihm;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Classe SIVOXSingerTextSyllabe representant le texte en syllabe d'un chant
 * @author Ecole Polytechnique de Sophia Antipolis
 * @version 1.0
 */

public class SIVOXSingerTextSyllabe extends CTabFolder {

	private InterfaceSingerGenerale i = null;
	
	private CTabItem itemChantSyllabe = null;
	private boolean isMaximized = false;
	
	private Composite cadre = null;
	private Button bouttonCreerSyllabe = null;
	private FormData bouttonCreerSyllabeData = null;
	private Text text = null;
	private FormData textData = null;
	
	/**
	 * Constructeur par defaut de SIVOXSingerTextSyllabe
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param arg0 Le composite parent de SIVOXSingerTextSyllabe
	 * @param arg1 L'option de SIVOXSingerTextSyllabe
	 * @param i1 l'interfaceSingerGenerale appelante
	 */
	public SIVOXSingerTextSyllabe(Composite arg0, int arg1, InterfaceSingerGenerale i1) {
		super(arg0, arg1);
		this.setMaximizeVisible(true);
		this.setSimple(false);
		i = i1;
		
		//creation du ctabitem avec son composite interne
		itemChantSyllabe = new CTabItem(this, SWT.BORDER);
		itemChantSyllabe.setText("Syllabes");
		cadre = new Composite(this, SWT.BORDER);
		cadre.setLayout(new FormLayout());
		itemChantSyllabe.setControl(cadre);
		this.setSelection(itemChantSyllabe);
		
		bouttonCreerSyllabe = new Button(cadre, SWT.PUSH);
		bouttonCreerSyllabe.setText("Creer un texte en syllabe...");
		bouttonCreerSyllabeData = new FormData();
		bouttonCreerSyllabeData.left = new FormAttachment(0);
		bouttonCreerSyllabeData.top = new FormAttachment(0);
		bouttonCreerSyllabe.setLayoutData(bouttonCreerSyllabeData);
		
		text = new Text(cadre, SWT.H_SCROLL | SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
		text.setText("");
		textData = new FormData();
		textData.left = new FormAttachment(0);
		textData.right = new FormAttachment(100);
		textData.bottom = new FormAttachment(100);
		textData.top = new FormAttachment(bouttonCreerSyllabe);
		text.setLayoutData(textData);
		
		//evenement sur le boutton reduire ou agrandir
		this.addCTabFolder2Listener(new CTabFolder2Listener() {
			public void close(CTabFolderEvent e) {}
			public void maximize(CTabFolderEvent arg0) {
				if(isMaximized == false)
				{
					setMaximized(true);
					isMaximized = true;
					i.maximiserSyllabe();
				}
			}
			public void minimize(CTabFolderEvent arg0) {}
			public void restore(CTabFolderEvent arg0) {
				if(isMaximized == true)
				{
					setMaximized(false);
					isMaximized = false;
					i.restaurerMaximisation();
				}
			}
			public void showList(CTabFolderEvent arg0) {}
		});
		
		//evenement de clic sur le boutton creer syllabe
		bouttonCreerSyllabe.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {}
			public void widgetSelected(SelectionEvent arg0) {
				AssistantCreationSyllabe acs = new AssistantCreationSyllabe(i);
				text.setText(acs.open());
			}
		});
		
		//ecouteur sur la modification de texte
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				i.setNonAJour();				
			}
		});
		
		//ecouteur sur le clavier pour le text pour l appui sur TAB
		text.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) {
				if(arg0.keyCode == 9)
				{
					arg0.doit = false;
					bouttonCreerSyllabe.setFocus();
				}
			}
			public void keyReleased(KeyEvent arg0) {}
		});
		
		// chargement des images
		try {
			itemChantSyllabe.setImage(new Image(i.getDisplay(), InformationSysteme.getRepertoireImage16()+"nouveau_projet_16_16.png"));
		} catch (Exception e) {
			MessageBox messageBox = new MessageBox(i.getShell(), SWT.ICON_ERROR | SWT.OK);
	        messageBox.setMessage("les images n'ont pas pu etre charge, le programme risque de ne pas fonctionner correctement !");
	        messageBox.setText("SINGER Si-Vox - Erreur");
	        messageBox.open();
		}
		
	}
	
	/**
	 * Methode qui retourne le contenu du texte
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @return Le texte contenu dans SIVOXSingerTextSyllabe
	 */
	public String getText()
	{
		return(text.getText().toString());
	}
	
	/**
	 * Methode qui modifie le texte contenu dans SIVOXSingerTextSyllabe
	 * @author Ecole Polytechnique de Sophia Antipolis
	 * @param t Le nouveau texte
	 */
	public void setText(String t)
	{
		text.setText(t);
	}
}
