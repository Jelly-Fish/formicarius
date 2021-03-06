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
package fr.com.jfish.jfgformicarius.formicarius.world.zone;

/**
 *
 * @author thw
 */
public class ZonePosition implements Comparable<ZonePosition> {

    private int x;
    private int y;
    
    public ZonePosition(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(final ZonePosition o) {

        return o.getX() == this.x && o.getY() == this.y ? 0 : -1;
    }

    @Override
    public boolean equals(final Object o) {

        if (!(o instanceof ZonePosition)) {
            return false;
        }

        return ((ZonePosition) o).getX() == this.x && ((ZonePosition) o).getY() == this.y;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        return hash;
    }
    
    public void incrementX() {
        ++this.x;
    }
    
    public void incrementY() {
        ++this.y;
    }
    
    public void decrementX() {
        --this.x;
    }
    
    public void decrementY() {
        --this.y;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
        public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
