/**
 * *****************************************************************************
 * Copyright (c) 2014, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *****************************************************************************
 */
package fr.com.jfish.jfgformicarius.formicarius.helpers;

import fr.com.jfish.jfgformicarius.formicarius.entities.lights.GLLight;
import fr.com.jfish.jfgformicarius.formicarius.exceptions.GLLightHelperException;
import fr.com.jfish.jfgformicarius.formicarius.game.Game;
import fr.com.jfish.jfgformicarius.formicarius.staticvars.StaticExceptionMsgs;
import fr.com.jfish.jfgformicarius.formicarius.utils.BufferUtils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author thw
 */
public class GLLightHelper {

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Main main instance ref.
     */
    private final Game game;
    public static final String SHADER1 = "src/formicarius/entities/lights/shaders/shader.frag";
    private final Integer FRAME_WIDTH;
    private final Integer FRAME_HEIGHT;

    /**
     * GLLight collection.
     */
    public Map<String, GLLight> lightEntities = new HashMap<>();

    /**
     * OPENGL returns.
     */
    private int shaderProgram;
    private int fragmentShader;
    
    private float r = 6.0f, g = 6.0f, b = 6.0f, a = 1.0f;
    private float lightAmbient[] = new float[4];
    private float lightDiffuse[] = {0.5f, 0.5f, 0.5f, 1.0f};
    private float lightPosition[] = {1.0f, 1.0f, 1.0f, 10.0f};
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     *
     * @param game
     * @param w
     * @param h
     */
    public GLLightHelper(final Game game, final int w, final int h) {
        this.game = game;
        this.FRAME_HEIGHT = h;
        this.FRAME_WIDTH = w;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">    
    /**
     * 
     * @param r
     * @param g
     * @param b 
     */
    public void renderLighting(final float r, final float g, final float b) {
        
        glBegin(GL_QUADS); 
        {
            glColor3f(r, g, b);
        }
        glEnd();
        glClear(GL_STENCIL_BUFFER_BIT);
    }
        
    /**
     * Destroy / clearup.
     */
    public void destroy() {
        glDeleteShader(fragmentShader);
        glDeleteProgram(shaderProgram);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="deprecated methods"> 
    /**
     *
     */
    @Deprecated
    public void renderLights() {

        for (GLLight light : this.lightEntities.values()) {
            
            // TODO : necessary ???
            /*glColorMask(true, true, true, true);
            glStencilFunc(GL_ALWAYS, 1, 1);
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
            glStencilFunc(GL_EQUAL, 0, 1);*/
            glColorMask(true, true, true, true);
            glUseProgram(shaderProgram);
            glUniform2f(glGetUniformLocation(shaderProgram, "lightLocation"),
                light.location.x, FRAME_HEIGHT - light.location.y);
            glUniform3f(glGetUniformLocation(shaderProgram, "lightColor"),
                light.red, light.green, light.blue);
            glBegin(GL_QUADS);
            {
                glVertex2f(0, 0);
                glVertex2f(0, FRAME_HEIGHT);
                glVertex2f(FRAME_WIDTH, FRAME_HEIGHT);
                glVertex2f(FRAME_WIDTH, 0);
            }
            glEnd();
            glUseProgram(0);
            glClear(GL_STENCIL_BUFFER_BIT);
        }
        glColor3f(0, 0, 0);
    }
    
    /**
     * Setup lighting
     */
    @Deprecated
    public void lightingSetup() {
        
        lightAmbient[0] = r;
        lightAmbient[1] = g;
        lightAmbient[2] = b;
        lightAmbient[3] = a;
       
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT,
                BufferUtils.allocateFloats(lightAmbient, GL_2D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE,
                BufferUtils.allocateFloats(lightDiffuse, GL_2D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,
                BufferUtils.allocateFloats(lightPosition, GL_2D));
        GL11.glEnable(GL11.GL_LIGHT1);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    
    /**
     *
     * @param shaderPath
     * @throws formicarius.exceptions.GLLightHelperException
     */
    @Deprecated
    public void initGLLightRendering(final String shaderPath) throws GLLightHelperException {

        shaderProgram = glCreateProgram();
        fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        final StringBuilder fragmentShaderSource = new StringBuilder();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(shaderPath));
            while ((line = reader.readLine()) != null) {
                fragmentShaderSource.append(line).append("\n");
            }
        } catch (final FileNotFoundException fnfe) {
            Logger.getLogger(GLLightHelper.class.getName()).log(Level.SEVERE, null, fnfe);
        } catch (final IOException ioe) {
            Logger.getLogger(GLLightHelper.class.getName()).log(Level.SEVERE, null, ioe);
        }
        glShaderSource(fragmentShader, fragmentShaderSource);
        glCompileShader(fragmentShader);
        if (glGetShaderi(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new GLLightHelperException(
                String.format(StaticExceptionMsgs.GL_LIGHT_HELPER_EX, shaderPath));
        }
        glAttachShader(shaderProgram, fragmentShader);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, FRAME_WIDTH, FRAME_HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        glEnable(GL_STENCIL_TEST);
        glClearColor(0, 0, 0, 0);
    }
    //</editor-fold>

}
