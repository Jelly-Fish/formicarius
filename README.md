# formicarius
LWJGL OpenGL 2d game project
Setup instructions :
1_clone repo
2_unzip ressources/lwjgl-2.9.1.zip to directoryof your choice
3_in IDE jfgformicarius >> properties >> run : add path to natives. 
Add this line to VM Options :
-Djava.library.path=C:\path\towards\unzipped\lwjgl-2.9.1\native\[windows][linux]
[windows][linux] depending on operating system. See unzipped lwjgl-2.9.1 file.
4_in IDE jfgformicarius >> properties >> run : select Main class :
fr.com.jellyfish.jfformicarius.formicarius.game.GameStarter

that's it. Clean/build with dependencies and run.