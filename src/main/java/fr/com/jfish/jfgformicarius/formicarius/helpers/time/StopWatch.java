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
 ******************************************************************************
 */
package fr.com.jfish.jfgformicarius.formicarius.helpers.time;

import fr.com.jfish.jfgformicarius.formicarius.exceptions.StopWatchNotStartedException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stop watch for calculating elapsed times between N events.
 * @author thw
 */
public class StopWatch { 

    //<editor-fold defaultstate="collapsed" desc="variables">
    /**
     * Started System current milliseconds time.
     */
    private long start;
    
    /**
     * Is started ?
     */
    private boolean started = false;
    
    /**
     * Maximum elapsed time defined by object using this object.
     */
    private double maxElapsedTime;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Constructor.
     * @param maxElapsedTime
     */
    public StopWatch(final double maxElapsedTime) {
        this.maxElapsedTime = maxElapsedTime;
        start();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods">
    /**
     * Start stop watch.
     */
    public final void start() {
         start = System.currentTimeMillis();
         started = true;
    }
    
    /**
     * Stop.
     */
    private void stop() {
        started = false;
    }
    
    /**
     * return time in seconds since this object was created.
     * @return elapsed time double.
     * @throws fr.com.jfish.jfgformicarius.formicarius.exceptions.StopWatchNotStartedException
     */
    public double elapsedTime() throws StopWatchNotStartedException {
        if (!started) {
            throw new StopWatchNotStartedException();
        } else {
            long now = System.currentTimeMillis();
            return (now - start) / 1000.0;
        }
    } 
    
    /**
     * Return boolean = MAX_ELAPSED_TIME greater than System elapsed time since
     * last call to StopWatch class start() method.
     * @return boolean MAX_ELAPSED_TIME greater than System elapsed time.
     */
    public boolean hasReachedMaxElapsedMS() {
        try {
            return this.maxElapsedTime < elapsedTime();
        } catch (final StopWatchNotStartedException swex) {
            Logger.getLogger(StopWatch.class.getName()).log(Level.WARNING, null, swex);
            // If stop watch has not yet been started this method's use is obsolete.
            // Therefor return false to prevent any actions conditioned on this
            // method's return true statement.
            return false;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getter & setters">
    public double getMaxElapsedTime() {
        return maxElapsedTime;
    }

    public void setMaxElapsedTime(double maxElapsedTime) {
        this.maxElapsedTime = maxElapsedTime;
    }
    //</editor-fold>
}
