///////////////////////////////////////////////////////////////////////////////////////////
Polytech Nice-Sophia Antipolis
26/05/2013
Robin VIVANT SI3 G3
///////////////////////////////////////////////////////////////////////////////////////////


Cette nouvelle version 1.1 apporte uniquement un correctif pour une fonctionnalité majeure, 
la possibilité d'attendre la fin de la lecture d'un son.

Modifications d'utilisation de la classe t2s.SIVOXDevint :

public void playShortText(final String text, boolean wait);
public void playText(final String text, boolean wait);
public void playWav(final String fichier, boolean wait);
public void play(final String text, final boolean flagloop, boolean wait);

Les méthodes ci-dessus se voit dotées d'un nouveau paramètre "wait" qui, si il vaut true, 
indique que le son qui sera joué ne pourra pas être interrompu...

public void stop();
public void forceStop();

... sauf en appelant la fonction forceStop() qui est là pour arreter toute la file 
d'attente de lecture. La méthode stop() essaye seulement d'arreter les sons dans la file 
d'attente qui ont été lancés avec "wait = false";

Du point de vue technique, le plus gros des modifications est situé dans t2s.son.JukeBox
Il est fait usage d'un mutex, il faudrait voir si il est vraiment indispensable et/ou si 
des problèmes d'accès concurent n'ont pas été oubliés.	

	

