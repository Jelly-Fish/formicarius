/**
 * *****************************************************************************
 * Copyright (c) 2014 - 2015, Thomas.H Warner. All rights reserved.
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
package fr.com.jellyfish.jfgformicarius.formicarius.world.zone.cave;

import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.CardinalityDefinition;
import fr.com.jellyfish.jfgformicarius.formicarius.world.zone.ZoneWallCardinalityDefintions;

/**
 *
 * @author thw
 */
public class CaveZoneWallCardinalityDefintions extends ZoneWallCardinalityDefintions {

    /**
     * String value of cardinalities.
     */
    public static final String NORTH_STRVAL = "north";
    public static final String EAST_STRVAL = "east";
    public static final String SOUTH_STRVAL = "south";
    public static final String WEST_STRVAL = "west";
    
    /**
     * 
     */
    public CaveZoneWallCardinalityDefintions() {
        super(CardinalityDefinition.NORTH, CardinalityDefinition.EAST,
            CardinalityDefinition.SOUTH, CardinalityDefinition.WEST);
    }
    
    /**
     * Define only wall to be dropped by adding extra CardinalityDefinition.
     * @param c CardinalityDefinition collection.
     */
    public CaveZoneWallCardinalityDefintions(final CardinalityDefinition ... c) {
        super(c);
    }
    
    /**
     * 
     * @param o
     */
    public CaveZoneWallCardinalityDefintions(final Object o) {
        super();
    }

}
