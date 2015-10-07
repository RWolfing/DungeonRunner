package de.dungeonrunner;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTarget;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.Tile;
import tiled.core.TileLayer;
import tiled.core.TileSet;

public class SFMLTileRenderer implements Drawable {

	private final Map map;

	public SFMLTileRenderer(Map map) {
		this.map = map;
		loadTextures();
	}

	public void paintTileLayerTest(RenderTarget target, RenderStates states, TileLayer layer) {
		final int tileWidth = map.getTileWidth();
        final int tileHeight = map.getTileHeight();
        
       	final Vector2i targetSize = target.getSize();
        final Rectangle boundsInTiles = layer.getBounds();
        
        TextureHolder textureHolder = TextureHolder.getInstance();
        
        for(int x = 0; x < boundsInTiles.getWidth(); x++){
        	for(int y = 0; y < boundsInTiles.getHeight(); y++){
        		//System.out.println(x + "/" + y);
        		final Tile tile = layer.getTileAt(x, y);
        		if(tile == null){
        			continue;
        		} else {
					RectangleShape shape = new RectangleShape();
					shape.setSize(new Vector2f(tileWidth, tileHeight));
	        		shape.setTexture(textureHolder.getTileTexture(tile.getId()));
	        		shape.setPosition(x * tileWidth, y * tileHeight);
	        		target.draw(shape);	
        		}
        	}
        }
	}

	private void loadTextures() {
		final int numberTileSets = map.getTileSets().size();
		final HashMap<Integer, java.awt.Image> mTileImages = new HashMap<>();
		TextureHolder texHolder = TextureHolder.getInstance();
		Image tempTileImage = new Image();
		Texture tempTexture = new Texture();

		for (int i = 0; i < numberTileSets; i++) {
			TileSet tileSet = map.getTileSets().get(i);
			System.out.println("Iterating tileSet " + tileSet.getName());
			final int numberTiles = tileSet.size();
			for (int o = 0; o < numberTiles; o++) {
				Tile tile = tileSet.getTile(o);
				mTileImages.put(tile.getId(), tile.getImage());
			}
		}

		for (Entry<Integer, java.awt.Image> entry : mTileImages.entrySet()) {
			try {
				tempTileImage.create((BufferedImage) entry.getValue());
				tempTexture.loadFromImage(tempTileImage);
				texHolder.loadTileTexture(entry.getKey(), tempTexture);
				System.out.println("Loaded new texture: " + entry.getKey() + ", " + tempTexture.toString());
			} catch (TextureCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void draw(RenderTarget target, RenderStates states) {

		for (MapLayer layer : map) {
			if (layer instanceof TileLayer) {
				paintTileLayerTest(target, states, (TileLayer) layer);
			}
		}

	}

}
