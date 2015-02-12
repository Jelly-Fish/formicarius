# formicarius
LWJGL OpenGL 2d game project

Setup instructions :

1_clone repo<br>
2_unzip ressources/lwjgl-2.9.1.zip to directoryof your choice<br>
3_in IDE jfgformicarius >> properties >> run : add path to natives<br>
Add this line to VM Options :<br>
-Djava.library.path=C:\path\towards\unzipped\lwjgl-2.9.1\native\[windows][linux]<br>
[windows][linux] depending on operating system. See unzipped lwjgl-2.9.1 file<br>
4_in IDE jfgformicarius >> properties >> run : select Main class :<br>
fr.com.jellyfish.jfformicarius.formicarius.game.GameStarter<br>

that's it. Clean/build with dependencies and run.
