rem compilation de VocalyzeSIVOX
javac -encoding utf8 -nowarn -cp ../lib t2s/*.java t2s/exception/*.java t2s/ihm/*.java t2s/ihm/courbe/*java t2s/prosodie/*.java t2s/son/*.java  t2s/newProsodies/*.java t2s/newProsodies/courbe/*.java t2s/traitement/*.java t2s/util/*.java t2s/chant/*.java -d ../bin64 -extdirs ../lib
pause

rem génération de ../bin/SI_VOX.jar
cd ..\bin64
jar cmf  ../src/manifest-64bits.mf SI_VOX.jar t2s
pause
