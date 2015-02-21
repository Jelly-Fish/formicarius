/*******************************************************************************
 * Copyright (c) 2014, Thomas.H Warner.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 *******************************************************************************/

package fr.com.jellyfish.jfgformicarius.formicarius.game;

import fr.com.jellyfish.jfgformicarius.formicarius.library.Library;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.input.InGameInputHelper;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.abstractentities.AbstractEntity;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.ImageIOImageData;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.FrameConst;
import fr.com.jellyfish.jfgformicarius.formicarius.constants.IconConst;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.biologicals.MainCharacter;
import fr.com.jellyfish.jfgformicarius.formicarius.entities.tiles.CaveEntrance;
import fr.com.jellyfish.jfgformicarius.formicarius.exceptions.InitializationException;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.DrawingHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.GLLightHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.SoundManager;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.TimeVariablesHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.entities.EntityHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.helpers.input.LibraryInputHelper;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.CollidableObject;
import fr.com.jellyfish.jfgformicarius.formicarius.interfaces.Spawnable;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticExceptionMsgs;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticFrameVars;
import fr.com.jellyfish.jfgformicarius.formicarius.staticvars.StaticSoundVars;
import fr.com.jellyfish.jfgformicarius.formicarius.texture.TextureLoader;
import fr.com.jellyfish.jfgformicarius.formicarius.utils.CursorUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class Game {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * 
     */
    public static boolean fullscreen = false;
    
    /**
     * Singleton instance.
     */
    private static Game instance;
    
    /**
     * True if the game is currently "running", ie the game loop is looping.
     */
    public static boolean running = true;
    
    /**
     * The loader responsible for converting images into OpenGL textures.
     */
    private TextureLoader textureLoader;

    /**
     * SoundManager to make sound with ^^.
     */
    private SoundManager soundManager;
    
    /**
     * Time variables helper.
     */
    private final TimeVariablesHelper timeHelper;
    
    /**
     * Mouse and keybord input helper.
     */
    private final InGameInputHelper inGameInputHelper;
    
    /**
     * Out of gameplay input helper.
     */
    private final LibraryInputHelper libraryInputHelper;
    
    /**
     * Light effects helper.
     */
    private final GLLightHelper lightingHelper;

    /**
     * Entity collections helper.
     */
    private final EntityHelper entityHelper;
    
    /**
     * Library instance reference.
     */
    private final Library library;
    
    /**
     * 
     */
    public static boolean transiting = false;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructor">
    private Game() {
        StaticFrameVars.fullScreen = Game.fullscreen;
        this.library = new Library();
        this.entityHelper = new EntityHelper();
        this.timeHelper = new TimeVariablesHelper();
        this.inGameInputHelper = new InGameInputHelper(this);
        this.libraryInputHelper = new LibraryInputHelper(this);
        this.lightingHelper = new GLLightHelper(this, FrameConst.FRM_WIDTH_800,
            FrameConst.FRM_HEIGHT_600);
        Game.running = init();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Main loop methods">
    /**
     * Run the main game loop, method keeps rendering scene and requesting
     * callback update on screen.
     */
    public void loop() {
        
        while (Game.running) {

            // clear screen on each loop.
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            
            // let subsystem paint :
            frameRendering();
            // update window contents :
            Display.update();
        }

        // clean it all up...
        soundManager.destroy();
        lightingHelper.destroy();
        Display.destroy();
    }
    
    /**
     * Frame rendering called by loop : do all processing required 
     * by a loop step for in game or out/menu modes.
     */
    private void frameRendering() {

        Display.sync(60);
        // work out how long its been since the last update, this
        // will be used to calculate how far the entities should
        // move this loop :
        long delta = this.timeHelper.getTime() - this.timeHelper.previousLoopT;
        this.timeHelper.previousLoopT = this.timeHelper.getTime();
        this.timeHelper.previousFPSt += delta;
        this.timeHelper.fps++;

        // update FPS counter if a second has passed
        if (this.timeHelper.previousFPSt >= this.timeHelper.SECOND_MS_VALUE) {
            Display.setTitle(FrameConst.FRAME_TITLE + " (FPS: " + this.timeHelper.fps + ")");
            this.timeHelper.previousFPSt = 0;
            this.timeHelper.fps = 0;
        }

        
        if (!InGameInputHelper.gamePaused && !Game.transiting) {
            
            /**
             * Here, the order in which the entities are appended to tempEnts
             * List<AbstractEntity> will impat on draw order. 
             * AbstractEntity afterRender method will push entity to LinkedList 
             * for drawing at index 0 if not defined to be pushed too a different 
             * index of the collection used for drawing.
             * MainCharacter AbstractEntity must be first added to draw queue so
             * another entity can find it's index add append itsef to queue at 
             * index - 1.
             */
            final MainCharacter mc = (MainCharacter)
                this.entityHelper.getMainEntities().get(MainCharacter.class.getSimpleName());
            DrawingHelper.getInstance().getDrawableQueue().add(0, mc);
            final List<AbstractEntity> tempEnts = new ArrayList<>();
            tempEnts.addAll(this.entityHelper.getObjectEntities().values());
            tempEnts.addAll(this.entityHelper.getGlobalEntities().values());
            tempEnts.addAll(this.entityHelper.getInteractableEntities().values());
            tempEnts.addAll(this.entityHelper.getMainEntities().values());
            if (this.entityHelper.getStaticEntities().containsKey(CaveEntrance.REF)) {
                tempEnts.add(this.entityHelper.getStaticEntities().get(CaveEntrance.REF));
            }
            
            // cycle round asking each entity to move/draw/update/dologic itself
            for (AbstractEntity entity : tempEnts) {
                
                entity.doLogic();
                entity.move(delta);
                entity.beforeRender(false);
                
                if (entity instanceof CollidableObject) {
                    
                    entity.collideWith(mc);
                    entity.collideWith((AbstractEntity)mc.getIgnitable());
                    entity.collideWith((AbstractEntity)mc.getSpawnable());
                    // Collide Static object with tempEnts AbstractEntity collection.
                    for (AbstractEntity iteractable : this.entityHelper.getInteractableEntities().values()) {
                        entity.collideWith(iteractable);
                    }
                }
                
                entity.afterRender(false);
            }
            
            // Perform AbstractEntity drawing & GLLight entities rendering:
            DrawingHelper.getInstance().drawStaticGame(!Game.transiting);
            
            // Resolve movemvent of the main entity. First assume this entity
            // isn't moving. If either cursor key is pressed then update movement.
            this.entityHelper.getMainEntities().get(MainCharacter.class.getSimpleName()).setHorizontalMovement(0);
            this.entityHelper.getMainEntities().get(MainCharacter.class.getSimpleName()).setVerticalMovement(0);
            
            // Finally get input :
            this.inGameInputHelper.observeKeyBoardInput();
            this.inGameInputHelper.observeMouseInput();
        } else if (InGameInputHelper.gamePaused && !Game.transiting) {
            
            // Switch to library display.
            this.library.libraryRendering();
            
            // Finally get input :
            this.libraryInputHelper.observeKeyBoardInput();
            this.libraryInputHelper.observeMouseInput();
        } else if (Game.transiting) {
            // Game transition :
            DrawingHelper.getInstance().drawStaticGame(!Game.transiting);
            this.entityHelper.getFader().beforeRender(false);
            this.entityHelper.getFader().draw();
        }
    }
    
    /**
     * call main loop.
     */
    public void execute() {
        loop();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="initialization">
    /**
     * Init master method.
     */
    private boolean init() {
        // initialize the window beforehand.
        try {
            initDisplayMode();
            Display.setTitle(FrameConst.FRAME_TITLE);
            Display.setFullscreen(StaticFrameVars.fullScreen);
            try {
                Display.setIcon(new ByteBuffer[] {
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(
                            new File(FrameConst.ICON)), true, true, null),
                    new ImageIOImageData().imageToByteBuffer(ImageIO.read(
                            new File(FrameConst.ICON)), true, true, null)
                });
            } catch (final IOException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
            
            Display.create();
            CursorUtils.appendNativeCurcor(IconConst.WAND_CURSOR2);

            // enable/disable Open GL funcs :
            glEnable(GL_TEXTURE_2D);
            glEnable(GL_ALPHA_TEST);
            glEnable(GL_BLEND);
            // disable the OpenGL depth test : rendering 2D graphics
            glDisable(GL_DEPTH_TEST);

            glMatrixMode(GL_PROJECTION);
            
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               
            glClearDepth(1);
            
            // this call to Blend makes lights work:
            //glBlendFunc(GL_ONE, GL_ONE);
            // this call to blend is good for sprites but not for lights:
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            
            glAlphaFunc(GL_ALWAYS, GL_ONE);
            
            glLoadIdentity();

            glOrtho(0, FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600, 0, -1, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glViewport(0, 0, FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600);
            
            glEnable(GL_STENCIL_TEST);
            glOrtho(0, FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600, 0, 1, -1);
            glMatrixMode(GL_MODELVIEW);
            glEnable(GL_STENCIL_TEST);
            glClearColor(0, 0, 0, 0);
            
            // global lighting setup :
            lightingHelper.renderLighting(1.0f, 1.0f, 1.0f);
            
            /*******************************************************************
             * Light rendering tests :
             * Following code needs a fixand is deprecated till then.
             *
            lightingHelper.lightingSetup();
            try {
                //---------------------- shaders ---------------/
                // needs fixing : Sprite/textures gain undisered trabsparency when
                // using glBlendFunc(GL_ONE, GL_ONE) which seems to be the only
                // OPENGL setup to make shader lighting work.
                lightingHelper.initGLLightRendering(GLLightHelper.SHADER1);
                //------- TEST : --------------//
                lightingHelper.lightEntities.put("test",
                    new GLLight(new Vector2f(300.0f, 300.0f), 1.0f, 5.0f, 10.0f));
                // END TEST
                //---------------------- end shaders -----------/
            } catch (final GLLightHelperException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
            ********************************************************************/

            textureLoader = new TextureLoader();

            // create sound manager, and initialize it with 8 channels
            // 1 channel for sounds, 6 for effects.
            soundManager = new SoundManager();
            soundManager.initialize(24);
            // load sound data :
            initSoundData();
            // Finally start init processes.
            kickstart();
        } catch (final LWJGLException le) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, le);
            return false;
        } catch (final InitializationException iex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, iex);
            return false;
        }
        
        return true;
    }

    /**
     * Sets the display mode for fullscreen mode.
     */
    @SuppressWarnings({"BroadCatchBlock", "TooBroadCatch"})
    private boolean initDisplayMode() {

        try {
            // Previously :
            /*DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(
                    FrameConst.FRM_WIDTH_800,
                    FrameConst.FRM_HEIGHT_600,
                    -1, -1, -1, -1, 60, 60);

            org.lwjgl.util.Display.setDisplayMode(dm, new String[]{
                "width=" + FrameConst.FRM_WIDTH_800,
                "height=" + FrameConst.FRM_HEIGHT_600,
                "freq=" + 60,
                "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
            });*/
            
            // Updated method :
            Display.setDisplayMode(new DisplayMode(FrameConst.FRM_WIDTH_800, FrameConst.FRM_HEIGHT_600));
            Display.setVSyncEnabled(true);
            return true;

        } catch (final LWJGLException le) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, le);
        } catch (final Exception ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    /**
     * Init all sounds & sound effects.
     *
     * @param paths
     */
    private void initSoundData() {
        
        // Mouvements.
        StaticSoundVars.step_grass1 = soundManager.addSound(StaticSoundVars.STEP_GRASS1);
        // Errors.
        StaticSoundVars.error1 = soundManager.addSound(StaticSoundVars.ERROR1);
        // Spells.
        StaticSoundVars.spell1 = soundManager.addSound(StaticSoundVars.SPELL1);
        StaticSoundVars.spell2 = soundManager.addSound(StaticSoundVars.SPELL2);
        StaticSoundVars.spell3 = soundManager.addSound(StaticSoundVars.SPELL3);
        StaticSoundVars.spell4 = soundManager.addSound(StaticSoundVars.SPELL4);
        StaticSoundVars.spell5 = soundManager.addSound(StaticSoundVars.SPELL5);
        StaticSoundVars.spell6 = soundManager.addSound(StaticSoundVars.SPELL6);
        // Quakes.
        StaticSoundVars.spellring = soundManager.addSound(StaticSoundVars.SPELLRING);
        // Cat :
        StaticSoundVars.kittycat = soundManager.addSound(StaticSoundVars.KITTYCAT);
        StaticSoundVars.grumpycat = soundManager.addSound(StaticSoundVars.GRUMPY_CAT);
        // Evil fog :
        StaticSoundVars.evilfog = soundManager.addSound(StaticSoundVars.EVILFOG);
        // Blades :
        StaticSoundVars.bloody_dagger = soundManager.addSound(StaticSoundVars.BLOODY_DAGGER);
        StaticSoundVars.golbez_axe = soundManager.addSound(StaticSoundVars.GOLBEZ_AXE);
        // Bloody squash !
        StaticSoundVars.bloody_squash1 = soundManager.addSound(StaticSoundVars.BLOODY_SQUASH1);
        StaticSoundVars.bloody_squash2 = soundManager.addSound(StaticSoundVars.BLOODY_SQUASH2);
        // Screams !
        StaticSoundVars.scream1 = soundManager.addSound(StaticSoundVars.SCREAM1);
        StaticSoundVars.scream2 = soundManager.addSound(StaticSoundVars.SCREAM2);
        StaticSoundVars.scream3 = soundManager.addSound(StaticSoundVars.SCREAM3);
        StaticSoundVars.scream4 = soundManager.addSound(StaticSoundVars.SCREAM4);
        StaticSoundVars.scream5 = soundManager.addSound(StaticSoundVars.SCREAM5);
        // Hurts !
        StaticSoundVars.hurt1 = soundManager.addSound(StaticSoundVars.HURT1);
        StaticSoundVars.hurt2 = soundManager.addSound(StaticSoundVars.HURT2);
        StaticSoundVars.metal_clatter = soundManager.addSound(StaticSoundVars.METAL_CLATTER);
        StaticSoundVars.metal_impact1 = soundManager.addSound(StaticSoundVars.METAL_IMPACT1);
        StaticSoundVars.spell_impact1 = soundManager.addSound(StaticSoundVars.SPELL_IMPACT1);
        // Laughs !
        StaticSoundVars.cunning_laugh = soundManager.addSound(StaticSoundVars.CUNNING_LAUGH);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Singleton accessor.
     * @return 
     */
    public static Game getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new Game();
            // Below, call to triggerTransition() method in MagicalHumanoid Class
            // will build a new Zone instance in the same way a as when transiting :
            instance.getEntityHelper().getMainCharacter().getTransitionAction().triggerTransition();
            return instance;
        }
    }

    /**
     * Return this class's instance EntityHelper instance's global 
     * AbstractEntity map collection.
     * @return 
     */
    public Map<String, AbstractEntity> accessGlobalEntities() {
        return this.entityHelper.getGlobalEntities();
    }
    
    /**
     * Kick start inits.
     */
    private void kickstart() throws InitializationException {
        
        if (entityHelper != null && library != null) {
            entityHelper.init(this);
            library.init(this);
            library.build();
        } else {
            throw new InitializationException(String.format(StaticExceptionMsgs.INIT_EX_MSG,
                entityHelper == null, library == null));
        }
    }
    
    /**
     * Sleep for a fixed number of milliseconds.
     *
     * @param duration The amount of time in milliseconds to sleep for
     */
    public void sleep(final long duration) {
        try {
            Thread.sleep((duration * this.timeHelper.ticksPS) / 1000);
        } catch (final InterruptedException ie) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ie);
        }
    }
    
    /**
     * Clear necessary AbstractEntity collections for zone transition :
     * dispose of Spawnable for example.
     */
    public void clearEntityCollectionsForTransition() {
        
        final List<AbstractEntity> temp = new ArrayList<>();
        temp.addAll(this.accessGlobalEntities().values());
        temp.addAll(this.entityHelper.getInteractableEntities().values());
        
        /**
         * All entities that need a clean up must go through this loop.
         * @see SpellRing clear method. If no call to clear() is performed
         * then entity is spawen = true and therefor canot be spawned again.
         */
        for (AbstractEntity ae : temp) {
            if (ae instanceof Spawnable) {
                ((Spawnable) ae).clear();
            }
        }
        
        this.entityHelper.getInteractableEntities().clear();
        this.entityHelper.getObjectEntities().clear();
        this.entityHelper.getStaticEntities().clear();
        this.accessGlobalEntities().clear();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters & setters">
    public GLLightHelper getLightingHelper() {
        return lightingHelper;
    }
    
    public LibraryInputHelper getLibraryInputHelper() {
        return libraryInputHelper;
    }
    
    public boolean isRunning() {
        return Game.running;
    }

    public TextureLoader getTextureLoader() {
        return textureLoader;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public EntityHelper getEntityHelper() {
        return this.entityHelper;
    }
    
    public Library getLibrary() {
        return library;
    }
    //</editor-fold>
    
}
