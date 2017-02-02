package fr.com.jfish.jfgformicarius.formicarius.utils;

import fr.com.jfish.jfgformicarius.formicarius.texture.ResizableSprite;
import fr.com.jfish.jfgformicarius.formicarius.texture.Sprite;
import fr.com.jfish.jfgformicarius.formicarius.texture.TextureLoader;

/**
 *
 * @author thw
 */
public class SpriteUtils {
    
    /**
     * Create or get a sprite which displays the image that is pointed to in the
     * classpath by "ref"
     *
     * @param loader reference to the loader to use
     * @param ref reference to the image to load
     * @return A sprite that can be drawn onto the current graphics context.
     */
    public static Sprite getSprite(final TextureLoader loader, final String ref) {
        return new Sprite(loader, ref);
    }
    
    /**
     * Create or get a resizable sprite which displays the image that is pointed 
     * to in the classpath by "ref".
     *
     * @param loader reference to the loader to use
     * @param ref reference to the image to load
     * @return A ResizableSprite that can be drawn onto the current graphics context.
     */
    public static ResizableSprite getResizableSprite(final TextureLoader loader, final String ref) {
        return new ResizableSprite(loader, ref);
    }
    
}
